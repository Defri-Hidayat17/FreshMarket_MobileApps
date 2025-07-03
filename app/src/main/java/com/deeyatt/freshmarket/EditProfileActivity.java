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

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 123;
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int CAMERA_REQUEST = 101;

    private CircleImageView imageProfile;
    private ImageView btnEditPhoto, iconCalendar;
    private TextView textBirthday;
    private TextView textGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageProfile = findViewById(R.id.imageProfile);
        btnEditPhoto = findViewById(R.id.btnEditPhoto);
        textBirthday = findViewById(R.id.textBirthday);
        iconCalendar = findViewById(R.id.btnCalendar);
        textGender = findViewById(R.id.textGender);
        ImageView btnGender = findViewById(R.id.btngender);
        ImageView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            playScaleAnimation(v);
            v.postDelayed(() -> {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }, 150);
        });

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
            Toast.makeText(this, "Kalender dibuka", Toast.LENGTH_SHORT).show();

            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    R.style.DatePickerTheme,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String dateStr = String.format("%02d / %02d / %04d", selectedDay, selectedMonth + 1, selectedYear);
                        textBirthday.setText(dateStr);
                    },
                    year, month, day
            );

            datePickerDialog.setOnShowListener(dialog -> {
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#007F5F"));
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#007F5F"));
            });

            datePickerDialog.show();
        };

        textBirthday.setOnClickListener(calendarClick);
        iconCalendar.setOnClickListener(calendarClick);
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imageProfile.setImageURI(imageUri);
        }

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageProfile.setImageBitmap(photo);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                showImagePickerDialog();
            } else {
                Toast.makeText(this, "Akses ditolak.", Toast.LENGTH_SHORT).show();
            }
        }
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

    private void showGenderPicker() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View sheet = getLayoutInflater().inflate(R.layout.dialog_gender_picker, null);
        dialog.setContentView(sheet);

        View layoutMale = sheet.findViewById(R.id.btnMale); // ini kotak, bukan TextView saja
        View layoutFemale = sheet.findViewById(R.id.btnFemale);

        layoutMale.setOnClickListener(v -> {
            playScaleAnimation(v);
            v.postDelayed(() -> {
                textGender.setText("Laki-laki");
                dialog.dismiss();
            }, 150); // delay sesuai durasi animasi
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
}
