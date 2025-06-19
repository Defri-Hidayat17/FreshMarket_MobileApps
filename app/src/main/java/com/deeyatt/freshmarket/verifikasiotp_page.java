package com.deeyatt.freshmarket;

import android.content.Intent; // <-- Tambahkan ini
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class verifikasiotp_page extends AppCompatActivity {

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

        // Atur EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Hubungkan ke layout
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

        // PART 1: set state awal
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

        // ⏳ START ROTATE ANIMATION untuk imageView15 (loading)
        rotateAnim = new RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnim.setDuration(1000);
        rotateAnim.setRepeatCount(Animation.INFINITE);
        rotateAnim.setInterpolator(this, android.R.interpolator.linear);
        imageView15.startAnimation(rotateAnim);

        // Klik imageView16 (cek status verif)
        imageView16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Scale animasi: membesar → normal lagi
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
                    public void onAnimationStart(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        imageView16.startAnimation(scaleDown);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });

                scaleDown.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        switchToPart2();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });

                imageView16.startAnimation(scaleUp);
            }
        });

        // Klik imageView17 (next button) - fix: HARUS di dalam onCreate!
        imageView17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    public void onAnimationStart(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        imageView17.startAnimation(scaleDown);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });

                scaleDown.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) { }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // Intent ke login_page
                        Intent intent = new Intent(verifikasiotp_page.this, loginpage.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });

                imageView17.startAnimation(scaleUp);
            }
        });
    }

    private void switchToPart2() {
        // PART 1 → GONE
        textView1.setVisibility(View.GONE);
        successText.setVisibility(View.GONE);
        imageView15.setVisibility(View.GONE);
        imageView16.setVisibility(View.GONE);

        // PART 2 → VISIBLE
        textView2.setVisibility(View.VISIBLE);
        imageView17.setVisibility(View.VISIBLE);
        imageView18.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        overlay.setVisibility(View.VISIBLE); // Layar gelap

        // Hentikan rotasi loading
        imageView15.clearAnimation();

        // Mulai progress bar
        startProgressBar();
    }

    private void startProgressBar() {
        progressStatus = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    handler.post(() -> progressBar.setProgress(progressStatus));
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                handler.post(() -> showCheckmark());
            }
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
