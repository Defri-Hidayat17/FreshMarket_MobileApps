package com.deeyatt.freshmarket;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class onboarding_satu extends AppCompatActivity {

    boolean isPageOne = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_satu);

// Di dalam onCreate:
        ImageView buttonMulai = findViewById(R.id.imageView7);

// Lalu listener:
        buttonMulai.setOnClickListener(v -> {
            Intent intent = new Intent(onboarding_satu.this, loginpage.class);
            startActivity(intent);
            finish();
        });


        // Elemen halaman 1
        ImageView image1 = findViewById(R.id.imageView);
        TextView text1 = findViewById(R.id.textView);
        TextView desc1 = findViewById(R.id.textView1);

        // Elemen halaman 2
        ImageView image2 = findViewById(R.id.imageView6);
        TextView text2 = findViewById(R.id.textView2);
        TextView desc2 = findViewById(R.id.textView3);

        // Dots
        ImageView dot1 = findViewById(R.id.imageView3);
        ImageView dot2 = findViewById(R.id.imageView4);

        // Tombol next & back
        ImageView nextBtn = findViewById(R.id.imageView8);
        ImageView backBtn = findViewById(R.id.imageView9);

        // Tambahkan ini untuk efek membesar:
        nextBtn.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    break;
            }
            return false; // biar onClick tetap jalan
        });

        // Tambahkan ini untuk efek membesar:
        backBtn.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    break;
            }
            return false; // biar onClick tetap jalan
        });

        // Tambahkan ini untuk efek membesar:
        buttonMulai.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    break;
            }
            return false; // biar onClick tetap jalan
        });


        // Buat animasi
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(300);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(300);

        // NEXT button pakai animasi
        nextBtn.setOnClickListener(v -> {
            if (isPageOne) {
                // Fade out halaman 1
                image1.startAnimation(fadeOut);
                text1.startAnimation(fadeOut);
                desc1.startAnimation(fadeOut);

                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        image1.setVisibility(View.GONE);
                        text1.setVisibility(View.GONE);
                        desc1.setVisibility(View.GONE);

                        image2.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.VISIBLE);
                        desc2.setVisibility(View.VISIBLE);
                        buttonMulai.setVisibility(View.VISIBLE);

                        image2.startAnimation(fadeIn);
                        text2.startAnimation(fadeIn);
                        desc2.startAnimation(fadeIn);
                        buttonMulai.startAnimation(fadeIn);

                        dot1.setImageResource(R.drawable.dotnonaktif);
                        dot2.setImageResource(R.drawable.dot_aktif);

                        nextBtn.setVisibility(View.GONE);
                        backBtn.setVisibility(View.VISIBLE);

                        isPageOne = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
            }
        });

        // BACK button pakai animasi
        backBtn.setOnClickListener(v -> {
            if (!isPageOne) {
                image2.startAnimation(fadeOut);
                text2.startAnimation(fadeOut);
                desc2.startAnimation(fadeOut);
                buttonMulai.startAnimation(fadeOut);

                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        image2.setVisibility(View.GONE);
                        text2.setVisibility(View.GONE);
                        desc2.setVisibility(View.GONE);
                        buttonMulai.setVisibility(View.GONE);

                        image1.setVisibility(View.VISIBLE);
                        text1.setVisibility(View.VISIBLE);
                        desc1.setVisibility(View.VISIBLE);

                        image1.startAnimation(fadeIn);
                        text1.startAnimation(fadeIn);
                        desc1.startAnimation(fadeIn);

                        dot1.setImageResource(R.drawable.dot_aktif);
                        dot2.setImageResource(R.drawable.dotnonaktif);

                        nextBtn.setVisibility(View.VISIBLE);
                        backBtn.setVisibility(View.GONE);

                        isPageOne = true;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
            }
        });
    }
}
