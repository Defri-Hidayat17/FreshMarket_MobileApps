package com.deeyatt.freshmarket;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfileActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 123;
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int CAMERA_REQUEST = 101;

    private CircleImageView imageProfile;
    private ImageView btnEditPhoto, iconCalendar;
    private TextView textBirthday, textGender;
    private EditText editName, editEmail, editBio;
    private Uri selectedImageUri = null;

    private ApiService apiService;
    private String emailLogin;
    private ProgressDialog progressDialog; // Tambahan Loading

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

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

        progressDialog = new ProgressDialog(this); // Inisialisasi ProgressDialog
        progressDialog.setMessage("Menyimpan data...");
        progressDialog.setCancelable(false);

        findViewById(R.id.btnUpdate).setOnClickListener(v -> updateProfileToMySQL());

        btnBack.setOnClickListener(v -> {
            playScaleAnimation(v);
            finish();
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.34/freshmarket/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        emailLogin = (user != null) ? user.getEmail() : "";

        View.OnClickListener genderClickListener = v -> {
            playScaleAnimation(v);
            showGenderPicker();
        };
        textGender.setOnClickListener(genderClickListener);
        btnGender.setOnClickListener(genderClickListener);

        View.OnClickListener imageClick = v -> {
            playScaleAnimation(v);
            requestPermissions();
        };
        imageProfile.setOnClickListener(imageClick);
        btnEditPhoto.setOnClickListener(imageClick);

        View.OnClickListener calendarClick = v -> {
            playScaleAnimation(v);
            showDatePicker();
        };
        textBirthday.setOnClickListener(calendarClick);
        iconCalendar.setOnClickListener(calendarClick);

        if (!emailLogin.isEmpty()) {
            loadProfileFromMySQL(emailLogin);
        }
    }

    private void loadProfileFromMySQL(String email) {
        apiService.getProfile(email).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null && "success".equals(response.body().status)) {
                    UserModel.Data user = response.body().data;
                    editName.setText(user.name);
                    editEmail.setText(user.email);
                    editBio.setText(user.bio);
                    textBirthday.setText(user.birthday);
                    textGender.setText(user.gender);

                    if (user.photo != null && !user.photo.isEmpty()) {
                        Glide.with(EditProfileActivity.this).load(user.photo).into(imageProfile);
                    } else {
                        imageProfile.setImageResource(R.drawable.baseline_person_24);
                    }
                } else {
                    Toast.makeText(EditProfileActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfileToMySQL() {
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String bio = editBio.getText().toString().trim();
        String birthday = textBirthday.getText().toString().trim();
        String gender = textGender.getText().toString().trim();
        String photo = (selectedImageUri != null) ? selectedImageUri.toString() : "";

        progressDialog.show(); // Tampilkan Loading

        Call<ResponseBody> call = apiService.updateProfile(name, email, bio, birthday, gender, photo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss(); // Hilangkan Loading
                Toast.makeText(EditProfileActivity.this, "Profil berhasil disimpan!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss(); // Hilangkan Loading
                Toast.makeText(EditProfileActivity.this, "Gagal: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS);
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
                    String dateStr = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    textBirthday.setText(dateStr);
                },
                year, month, day
        );

        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Simpan", datePickerDialog);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Batal", datePickerDialog);

        datePickerDialog.setOnShowListener(dialog -> {
            int colorHijau = Color.parseColor("#007F5F");
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(colorHijau);
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(colorHijau);
        });

        datePickerDialog.show();
    }

    private void showGenderPicker() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View sheet = getLayoutInflater().inflate(R.layout.dialog_gender_picker, null);
        dialog.setContentView(sheet);

        sheet.findViewById(R.id.btnMale).setOnClickListener(v -> {
            playScaleAnimation(v);
            textGender.setText("Laki-laki");
            dialog.dismiss();
        });

        sheet.findViewById(R.id.btnFemale).setOnClickListener(v -> {
            playScaleAnimation(v);
            textGender.setText("Perempuan");
            dialog.dismiss();
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
