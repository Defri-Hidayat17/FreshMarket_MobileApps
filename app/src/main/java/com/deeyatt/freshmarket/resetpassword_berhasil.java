package com.deeyatt.freshmarket;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class resetpassword_berhasil extends AppCompatActivity {

    TextView textView2;
    ImageView imageView17, checkmark, imageView18;
    ProgressBar progressBar;
    View overlay;

    int progressStatus = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword_berhasil);

        // Hubungkan ke layout
        textView2 = findViewById(R.id.textView2);
        imageView17 = findViewById(R.id.imageView17);
        checkmark = findViewById(R.id.checkmark);
        imageView18 = findViewById(R.id.imageView18);
        progressBar = findViewById(R.id.progressBar);
        overlay = findViewById(R.id.overlay);

        // PART2: state awal
        progressBar.setVisibility(View.VISIBLE);
        overlay.setVisibility(View.VISIBLE);
        textView2.setVisibility(View.VISIBLE);
        imageView17.setVisibility(View.VISIBLE);
        imageView18.setVisibility(View.VISIBLE); // ikut visible
        checkmark.setVisibility(View.GONE); // muncul belakangan

        // Mulai progress bar
        startProgressBar();

        // Next button scale + intent
        imageView17.setOnClickListener(v -> {
            ScaleAnimation scaleUp = new ScaleAnimation(
                    1.0f, 1.1f,
                    1.0f, 1.1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            scaleUp.setDuration(150);

            ScaleAnimation scaleDown = new ScaleAnimation(
                    1.1f, 1.0f,
                    1.1f, 1.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            scaleDown.setDuration(150);

            scaleUp.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    imageView17.startAnimation(scaleDown);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            scaleDown.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    Intent intent = new Intent(resetpassword_berhasil.this, loginpage.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            imageView17.startAnimation(scaleUp);
        });
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
