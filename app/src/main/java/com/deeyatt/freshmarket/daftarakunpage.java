package com.deeyatt.freshmarket;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class daftarakunpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftarakunpage);

        // Inset listener (optional, biar fullscreen aman)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ========================
        // 1) Deklarasi sesuai ID FreshMarket
        // ========================
        EditText inputPassword = findViewById(R.id.inputPassword);
        TextView criteriaMinLength = findViewById(R.id.criteria_min_length);
        TextView criteriaUppercase = findViewById(R.id.criteria_uppercase);
        TextView criteriaLowercase = findViewById(R.id.criteria_lowercase);
        TextView criteriaNumber = findViewById(R.id.criteria_number);
        TextView tvDaftar = findViewById(R.id.textView2);
        ImageView imageView2 = findViewById(R.id.imageView2);
        ImageView imageView14 = findViewById(R.id.imageView14);

        // ========================
        // 2) Logika Validasi Password
        // ========================
        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();

                // Tampilkan kriteria kalau ada input
                if (!password.isEmpty()) {
                    criteriaMinLength.setVisibility(View.VISIBLE);
                    criteriaUppercase.setVisibility(View.VISIBLE);
                    criteriaLowercase.setVisibility(View.VISIBLE);
                    criteriaNumber.setVisibility(View.VISIBLE);
                } else {
                    criteriaMinLength.setVisibility(View.GONE);
                    criteriaUppercase.setVisibility(View.GONE);
                    criteriaLowercase.setVisibility(View.GONE);
                    criteriaNumber.setVisibility(View.GONE);
                }

                // Validasi kriteria
                criteriaMinLength.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, password.length() >= 8 ? R.drawable.centang : 0, 0
                );
                criteriaUppercase.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, password.matches(".*[A-Z].*") ? R.drawable.centang : 0, 0
                );
                criteriaLowercase.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, password.matches(".*[a-z].*") ? R.drawable.centang : 0, 0
                );
                criteriaNumber.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, password.matches(".*[0-9].*") ? R.drawable.centang : 0, 0
                );
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // ========================
        // 3) Logika spannable untuk textView2
        // ========================
        String text = "Saya menyetujui penyimpanan dan penggunaan data pribadi saya serta menyetujui ketentuan layanan dan kebijakan privasi";
        SpannableString spannableString = new SpannableString(text);
        // Highlight sesuai keinginanmu: ini contoh
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 77, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 78, 95, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 96, 99, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 100, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDaftar.setText(spannableString);

        // ========================
        // 4) Logika toggle imageView2 (kotak centang)
        // ========================
        final boolean[] isChecked = {false};
        imageView2.setOnClickListener(v -> {
            if (isChecked[0]) {
                imageView2.setImageResource(R.drawable.kotak_ceklis_black);
            } else {
                imageView2.setImageResource(R.drawable.kotak_ceklis_on_black);
            }
            isChecked[0] = !isChecked[0];
        });

        // ========================
        // 5) Effect membesar + pindah page saat klik imageView14
        // ========================
        imageView14.setOnClickListener(v -> {
            imageView14.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100)
                    .withEndAction(() -> {
                        imageView14.animate().scaleX(1f).scaleY(1f).setDuration(100);
                        // Ganti NextPage.class ke activity tujuanmu
                        Intent intent = new Intent(daftarakunpage.this, verifikasiotp_page.class);
                        startActivity(intent);
                    });
        });

        // Status icon (eye / eye_off)
        final boolean[] isPasswordVisible = {false};

// Tambahkan listener sentuh di EditText
        inputPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2; // index drawableRight

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (inputPassword.getRight() - inputPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // Toggle
                        if (isPasswordVisible[0]) {
                            inputPassword.setTransformationMethod(new PasswordTransformationMethod());
                            inputPassword.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.lock, 0, R.drawable.eye_off, 0);
                        } else {
                            inputPassword.setTransformationMethod(null);
                            inputPassword.setCompoundDrawablesWithIntrinsicBounds(
                                    R.drawable.lock, 0, R.drawable.eye, 0);
                        }
                        isPasswordVisible[0] = !isPasswordVisible[0];
                        inputPassword.setSelection(inputPassword.getText().length());
                        return true; // event handled
                    }
                }
                return false;
            }
        });


    }
}
