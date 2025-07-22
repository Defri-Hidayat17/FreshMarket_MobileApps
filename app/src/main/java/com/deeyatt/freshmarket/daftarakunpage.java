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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class daftarakunpage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAnalytics mAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftarakunpage);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        mAnalytics = FirebaseAnalytics.getInstance(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText inputPassword = findViewById(R.id.inputPassword);
        EditText inputEmail = findViewById(R.id.inputEmail); // pastikan kamu punya ini di XML
        TextView criteriaMinLength = findViewById(R.id.criteria_min_length);
        TextView criteriaUppercase = findViewById(R.id.criteria_uppercase);
        TextView criteriaLowercase = findViewById(R.id.criteria_lowercase);
        TextView criteriaNumber = findViewById(R.id.criteria_number);
        TextView tvDaftar = findViewById(R.id.textView2);
        ImageView imageView2 = findViewById(R.id.imageView2);
        ImageView imageView14 = findViewById(R.id.imageView14);

        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();
                criteriaMinLength.setVisibility(!password.isEmpty() ? View.VISIBLE : View.GONE);
                criteriaUppercase.setVisibility(!password.isEmpty() ? View.VISIBLE : View.GONE);
                criteriaLowercase.setVisibility(!password.isEmpty() ? View.VISIBLE : View.GONE);
                criteriaNumber.setVisibility(!password.isEmpty() ? View.VISIBLE : View.GONE);

                criteriaMinLength.setCompoundDrawablesWithIntrinsicBounds(0, 0, password.length() >= 8 ? R.drawable.centang : 0, 0);
                criteriaUppercase.setCompoundDrawablesWithIntrinsicBounds(0, 0, password.matches(".*[A-Z].*") ? R.drawable.centang : 0, 0);
                criteriaLowercase.setCompoundDrawablesWithIntrinsicBounds(0, 0, password.matches(".*[a-z].*") ? R.drawable.centang : 0, 0);
                criteriaNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, password.matches(".*[0-9].*") ? R.drawable.centang : 0, 0);
            }
            @Override public void afterTextChanged(Editable s) { }
        });

        String text = "Saya menyetujui penyimpanan dan penggunaan data pribadi saya serta menyetujui ketentuan layanan dan kebijakan privasi";
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 77, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 78, 95, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 96, 99, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 100, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDaftar.setText(spannableString);

        final boolean[] isChecked = {false};
        imageView2.setOnClickListener(v -> {
            imageView2.setImageResource(isChecked[0] ? R.drawable.kotak_ceklis_black : R.drawable.kotak_ceklis_on_black);
            isChecked[0] = !isChecked[0];
        });

        imageView14.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isChecked[0]) {
                Toast.makeText(this, "Kamu harus menyetujui syarat & ketentuan", Toast.LENGTH_SHORT).show();
                return;
            }

            imageView14.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100)
                    .withEndAction(() -> {
                        imageView14.animate().scaleX(1f).scaleY(1f).setDuration(100);

                        // DAFTAR DENGAN FIREBASE
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        // Log signup success
                                        Bundle logBundle = new Bundle();
                                        logBundle.putString("signup_email", email);
                                        mAnalytics.logEvent("signup_success", logBundle);

                                        Toast.makeText(this, "Pendaftaran berhasil", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, verifikasiotp_page.class));
                                        finish();
                                    } else {
                                        Toast.makeText(this, "Gagal daftar: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        // Log signup failed
                                        Bundle errBundle = new Bundle();
                                        errBundle.putString("signup_error", task.getException().getMessage());
                                        mAnalytics.logEvent("signup_failed", errBundle);
                                    }
                                });
                    });
        });

        final boolean[] isPasswordVisible = {false};
        inputPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP &&
                        event.getRawX() >= (inputPassword.getRight() - inputPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    if (isPasswordVisible[0]) {
                        inputPassword.setTransformationMethod(new PasswordTransformationMethod());
                        inputPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eye_off, 0);
                    } else {
                        inputPassword.setTransformationMethod(null);
                        inputPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.lock, 0, R.drawable.eye, 0);
                    }
                    isPasswordVisible[0] = !isPasswordVisible[0];
                    inputPassword.setSelection(inputPassword.getText().length());
                    return true;
                }
                return false;
            }
        });
    }
}
