package com.deeyatt.freshmarket;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 123;
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int CAMERA_REQUEST = 101;

    private CircleImageView imageProfile;
    private ImageView btnEditPhoto, iconCalendar;
    private TextView textBirthday, textGender;
    private EditText editName, editEmail, editBio;
    private Uri selectedImageUri = null;

    // Firebase
    private FirebaseFirestore firestore;
    private StorageReference storageRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // === Init Views ===
        imageProfile = findViewById(R.id.imageProfile);
        btnEditPhoto = findViewById(R.id.btnEditPhoto);
        textBirthday = findViewById(R.id.textBirthday);
        iconCalendar = findViewById(R.id.btnCalendar);
        textGender = findViewById(R.id.textGender);
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editBio = findViewById(R.id.editBio);
        ImageView btnGender = findViewById(R.id.btngender);
        ImageView btnBack = findViewById(R.id.btnBack);

        findViewById(R.id.btnUpdate).setOnClickListener(v -> updateProfileToFirebase());

        // === Firebase Init ===
        firestore = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Load data user dari Firestore
        loadUserProfile();

        btnBack.setOnClickListener(v -> {
            playScaleAnimation(v);
            v.postDelayed(() -> {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }, 150);
        });

        // Gender
        View.OnClickListener genderClickListener = v -> {
            playScaleAnimation(v);
            showGenderPicker();
        };
        textGender.setOnClickListener(genderClickListener);
        btnGender.setOnClickListener(genderClickListener);

        // Foto profile
        View.OnClickListener imageClick = v -> {
            playScaleAnimation(v);
            requestPermissions();
        };
        imageProfile.setOnClickListener(imageClick);
        btnEditPhoto.setOnClickListener(imageClick);

        // Kalender
        View.OnClickListener calendarClick = v -> {
            playScaleAnimation(v);
            showDatePicker();
        };
        textBirthday.setOnClickListener(calendarClick);
        iconCalendar.setOnClickListener(calendarClick);
    }

    private void loadUserProfile() {
        if (currentUser == null) return;
        DocumentReference docRef = firestore.collection("users").document(currentUser.getUid());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                editName.setText(documentSnapshot.getString("name"));
                editEmail.setText(documentSnapshot.getString("email"));
                editBio.setText(documentSnapshot.getString("bio"));
                textBirthday.setText(documentSnapshot.getString("birthday"));
                textGender.setText(documentSnapshot.getString("gender"));

                String photoUrl = documentSnapshot.getString("photoUrl");
                if (photoUrl != null && !photoUrl.isEmpty()) {
                    Glide.with(this).load(photoUrl).into(imageProfile);
                } else {
                    // default jika tidak ada foto
                    imageProfile.setImageResource(R.drawable.baseline_person_24);
                }
            }
        });
    }

    private void updateProfileToFirebase() {
        if (currentUser == null) return;

        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String bio = editBio.getText().toString().trim();
        String birthday = textBirthday.getText().toString().trim();
        String gender = textGender.getText().toString().trim();

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("bio", bio);
        userMap.put("birthday", birthday);
        userMap.put("gender", gender);

        // Jika ada foto baru yang dipilih
        if (selectedImageUri != null) {
            String uniqueFileName = "profileImages/" + currentUser.getUid() + "_" + UUID.randomUUID() + ".jpg";
            StorageReference profileRef = storageRef.child(uniqueFileName);
            profileRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> profileRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                userMap.put("photoUrl", uri.toString());
                                saveUserMap(userMap);
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Gagal ambil URL foto", Toast.LENGTH_SHORT).show()))
                    .addOnFailureListener(e -> Toast.makeText(this, "Upload gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            saveUserMap(userMap);
        }
    }

    private void saveUserMap(Map<String, Object> userMap) {
        firestore.collection("users").document(currentUser.getUid())
                .set(userMap)
                .addOnSuccessListener(aVoid -> Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this, "Gagal memperbarui profil", Toast.LENGTH_SHORT).show());
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, REQUEST_PERMISSIONS);
        } else {
            showImagePickerDialog();
        }
    }

    private void showImagePickerDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View sheet = getLayoutInflater().inflate(R.layout.dialog_image_picker, null);
        dialog.setContentView(sheet);

        sheet.findViewById(R.id.btnKamera).setOnClickListener(v -> {
            playScaleAnimation(v);
            openCamera();
            dialog.dismiss();
        });

        sheet.findViewById(R.id.btnGaleri).setOnClickListener(v -> {
            playScaleAnimation(v);
            openGallery();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                selectedImageUri = data.getData();
                Glide.with(this).load(selectedImageUri).into(imageProfile);
            }

            if (requestCode == CAMERA_REQUEST && data != null && data.getExtras() != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                selectedImageUri = getImageUri(photo);
                Glide.with(this).load(selectedImageUri).into(imageProfile);
            }
        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "profile", null);
        return Uri.parse(path);
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                R.style.DatePickerTheme,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    String dateStr = String.format("%02d / %02d / %04d", selectedDay, selectedMonth + 1, selectedYear);
                    textBirthday.setText(dateStr);
                },
                year, month, day
        );

        // Tambahkan tombol manual agar jelas
        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Simpan", datePickerDialog);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Batal", datePickerDialog);

        // Ubah warna tombol ke #007F5F
        datePickerDialog.setOnShowListener(dialog -> {
            int customColor = Color.parseColor("#007F5F");
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(customColor);
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(customColor);
        });

        datePickerDialog.show();
    }

    private void showGenderPicker() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View sheet = getLayoutInflater().inflate(R.layout.dialog_gender_picker, null);
        dialog.setContentView(sheet);

        View layoutMale = sheet.findViewById(R.id.btnMale);
        View layoutFemale = sheet.findViewById(R.id.btnFemale);

        layoutMale.setOnClickListener(v -> {
            playScaleAnimation(v);
            v.postDelayed(() -> {
                textGender.setText("Laki-laki");
                dialog.dismiss();
            }, 150);
        });

        layoutFemale.setOnClickListener(v -> {
            playScaleAnimation(v);
            v.postDelayed(() -> {
                textGender.setText("Perempuan");
                dialog.dismiss();
            }, 150);
        });

        dialog.show();
    }

    private void playScaleAnimation(View view) {
        ScaleAnimation scale = new ScaleAnimation(
                1f, 0.9f, 1f, 0.9f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        );
        scale.setDuration(100);
        scale.setRepeatCount(1);
        scale.setRepeatMode(ScaleAnimation.REVERSE);
        view.startAnimation(scale);
    }
}
