package com.deeyatt.freshmarket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class resetpassword extends AppCompatActivity {

    private EditText inputEmail;
    private ImageView imageView19, imageView20;
    private FirebaseAuth mAuth;

    private String userEmail = null;
    private boolean isResetLinkSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        // Inisialisasi
        inputEmail = findViewById(R.id.inputEmail);
        imageView19 = findViewById(R.id.imageView19); // Tombol kirim
        imageView20 = findViewById(R.id.imageView20); // Tombol lanjut


        mAuth = FirebaseAuth.getInstance();
        imageView20.setVisibility(View.GONE); // Sembunyikan tombol lanjut di awal

        // Tombol Kirim Email Reset
        imageView19.setOnClickListener(v -> {
            userEmail = inputEmail.getText().toString().trim();

            if (TextUtils.isEmpty(userEmail)) {
                Snackbar.make(inputEmail, "Anda belum mengisi Email", Snackbar.LENGTH_SHORT).show();
                return;
            }

            mAuth.sendPasswordResetEmail(userEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            isResetLinkSent = true;

                            Snackbar.make(inputEmail,
                                    "Link reset password telah dikirim ke: " + userEmail,
                                    Snackbar.LENGTH_LONG).show();

                            // Delay dan animasi muncul tombol lanjut
                            new Handler().postDelayed(() -> {
                                imageView19.setVisibility(View.GONE);
                                fadeInImage(imageView20);
                            }, 1000);
                        } else {
                            Snackbar.make(inputEmail,
                                    "Gagal mengirim email: " + task.getException().getMessage(),
                                    Snackbar.LENGTH_LONG).show();
                        }
                    });
        });

        // Tombol Lanjut (setelah reset password)
        imageView20.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();

            if (!isResetLinkSent) {
                Snackbar.make(inputEmail, "Silakan reset password terlebih dahulu via email.", Snackbar.LENGTH_SHORT).show();
                return;
            }

            if (user == null) {
                // User sudah reset password (logout otomatis)
                Snackbar.make(inputEmail,
                        "Reset Password berhasil! Silakan login ulang.",
                        Snackbar.LENGTH_LONG).show();

                animateAndNavigateToLogin();
            } else {
                Snackbar.make(inputEmail,
                        "Silakan reset password terlebih dahulu via email.",
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


        // Saat kembali dari Gmail, jika user sudah logout (password diganti)
        FirebaseUser user = mAuth.getCurrentUser();

        if (isResetLinkSent && user == null && imageView20.getVisibility() != View.VISIBLE) {
            imageView19.setVisibility(View.GONE);
            fadeInImage(imageView20);
        }
    }

    // Animasi Fade-In Smooth
    private void fadeInImage(ImageView imageView) {
        imageView.setVisibility(View.VISIBLE);
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(800); // smooth
        fadeIn.setFillAfter(true);
        imageView.startAnimation(fadeIn);
    }

    // Animasi Scale dan Pindah ke Login Page
    private void animateAndNavigateToLogin() {
        ScaleAnimation scaleUp = new ScaleAnimation(
                1.0f, 1.1f, 1.0f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleUp.setDuration(150);
        scaleUp.setFillAfter(true);

        ScaleAnimation scaleDown = new ScaleAnimation(
                1.1f, 1.0f, 1.1f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleDown.setDuration(150);
        scaleDown.setFillAfter(true);

        scaleUp.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView20.startAnimation(scaleDown);
            }

            @Override public void onAnimationRepeat(Animation animation) {}
        });

        scaleDown.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(resetpassword.this, loginpage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override public void onAnimationRepeat(Animation animation) {}
        });

        imageView20.startAnimation(scaleUp);
    }
}
