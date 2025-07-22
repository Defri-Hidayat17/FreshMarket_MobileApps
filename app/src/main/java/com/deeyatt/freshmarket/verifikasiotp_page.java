package com.deeyatt.freshmarket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class verifikasiotp_page extends AppCompatActivity {

    private FirebaseAuth mAuth;

    TextView textView1, successText, textView2;
    ImageView imageView15, imageView16, imageView17, checkmark, imageView18;
    ProgressBar progressBar;
    View overlay;

    int progressStatus = 0;
    Handler handler = new Handler();
    RotateAnimation rotateAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifikasiotp_page);

        mAuth = FirebaseAuth.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find views
        textView1 = findViewById(R.id.textView1);
        successText = findViewById(R.id.successText);
        textView2 = findViewById(R.id.textView2);
        imageView15 = findViewById(R.id.imageView15);
        imageView16 = findViewById(R.id.imageView16);
        imageView17 = findViewById(R.id.imageView17);
        imageView18 = findViewById(R.id.imageView18);
        checkmark = findViewById(R.id.checkmark);
        progressBar = findViewById(R.id.progressBar);
        overlay = findViewById(R.id.overlay);

        // Initial state
        textView1.setVisibility(View.VISIBLE);
        successText.setVisibility(View.VISIBLE);
        imageView15.setVisibility(View.VISIBLE);
        imageView16.setVisibility(View.VISIBLE);

        textView2.setVisibility(View.GONE);
        imageView17.setVisibility(View.GONE);
        imageView18.setVisibility(View.GONE);
        checkmark.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        overlay.setVisibility(View.GONE);

        // Loading animation
        rotateAnim = new RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnim.setDuration(1000);
        rotateAnim.setRepeatCount(Animation.INFINITE);
        rotateAnim.setInterpolator(this, android.R.interpolator.linear);
        imageView15.startAnimation(rotateAnim);

        // === Kirim Email Verifikasi ===
        imageView16.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();

            if (user != null && !user.isEmailVerified()) {
                imageView15.setVisibility(View.VISIBLE);

                user.sendEmailVerification().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(verifikasiotp_page.this,
                                "Email verifikasi telah dikirim ke " + user.getEmail(),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(verifikasiotp_page.this,
                                "Gagal mengirim email verifikasi.",
                                Toast.LENGTH_LONG).show();
                    }
                });

                // Jalankan animasi klik
                animateButton(imageView16, this::switchToPart2);

            } else {
                Toast.makeText(verifikasiotp_page.this,
                        "Email sudah diverifikasi atau user belum login.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ➡ Lanjut ke login
        imageView17.setOnClickListener(v -> animateButton(imageView17, () -> {
            Intent intent = new Intent(verifikasiotp_page.this, loginpage.class);
            startActivity(intent);
            finish();
        }));
    }

    // Cek status verifikasi saat kembali ke activity
    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.reload().addOnCompleteListener(task -> {
                if (user.isEmailVerified()) {
                    imageView15.clearAnimation();
                    imageView15.setVisibility(View.GONE);

                    textView2.setVisibility(View.VISIBLE);
                    imageView17.setVisibility(View.VISIBLE);
                    checkmark.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void switchToPart2() {
        textView1.setVisibility(View.GONE);
        successText.setVisibility(View.GONE);
        imageView16.setVisibility(View.GONE);

        textView2.setVisibility(View.VISIBLE);
        imageView17.setVisibility(View.VISIBLE);
        imageView18.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        overlay.setVisibility(View.VISIBLE);

        startProgressBar();
    }

    private void animateButton(View view, Runnable onEnd) {
        ScaleAnimation scaleUp = new ScaleAnimation(
                1.0f, 1.1f, 1.0f, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleUp.setDuration(150);

        ScaleAnimation scaleDown = new ScaleAnimation(
                1.1f, 1.0f, 1.1f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleDown.setDuration(150);

        scaleUp.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation) {
                view.startAnimation(scaleDown);
            }
            @Override public void onAnimationRepeat(Animation animation) {}
        });

        scaleDown.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation) {
                onEnd.run();
            }
            @Override public void onAnimationRepeat(Animation animation) {}
        });

        view.startAnimation(scaleUp);
    }

    private void startProgressBar() {
        progressStatus = 0;

        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 1;
                handler.post(() -> progressBar.setProgress(progressStatus));
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            handler.post(this::showCheckmark);
        }).start();
    }

    private void showCheckmark() {
        checkmark.setVisibility(View.VISIBLE);

        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(500);
        fadeIn.setFillAfter(true);

        checkmark.startAnimation(fadeIn);
    }
}
