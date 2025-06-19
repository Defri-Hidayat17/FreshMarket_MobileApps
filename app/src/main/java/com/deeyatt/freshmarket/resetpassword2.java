package com.deeyatt.freshmarket;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class resetpassword2 extends AppCompatActivity {

    EditText inputPasswordBaru, konfirmasiPasswordBaru;
    ImageView imageView19;

    boolean isPasswordVisible1 = false;
    boolean isPasswordVisible2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword2);

        inputPasswordBaru = findViewById(R.id.inputPasswordBaru);
        konfirmasiPasswordBaru = findViewById(R.id.konfirmasipasswordbaru);
        imageView19 = findViewById(R.id.imageView19);

        // =============================
        // Toggle eye di inputPasswordBaru
        inputPasswordBaru.setOnTouchListener((v, event) -> {
            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                int drawableEnd = 2; // index drawableRight
                if (event.getRawX() >= (inputPasswordBaru.getRight() - inputPasswordBaru.getCompoundDrawables()[drawableEnd].getBounds().width())) {
                    isPasswordVisible1 = !isPasswordVisible1;
                    if (isPasswordVisible1) {
                        inputPasswordBaru.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        inputPasswordBaru.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye, 0);
                    } else {
                        inputPasswordBaru.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        inputPasswordBaru.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_off, 0);
                    }
                    inputPasswordBaru.setSelection(inputPasswordBaru.length()); // cursor di akhir
                    return true;
                }
            }
            return false;
        });

        // =============================
        // Toggle eye di konfirmasiPasswordBaru
        konfirmasiPasswordBaru.setOnTouchListener((v, event) -> {
            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                int drawableEnd = 2; // index drawableRight
                if (event.getRawX() >= (konfirmasiPasswordBaru.getRight() - konfirmasiPasswordBaru.getCompoundDrawables()[drawableEnd].getBounds().width())) {
                    isPasswordVisible2 = !isPasswordVisible2;
                    if (isPasswordVisible2) {
                        konfirmasiPasswordBaru.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        konfirmasiPasswordBaru.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye, 0);
                    } else {
                        konfirmasiPasswordBaru.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        konfirmasiPasswordBaru.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_off, 0);
                    }
                    konfirmasiPasswordBaru.setSelection(konfirmasiPasswordBaru.length());
                    return true;
                }
            }
            return false;
        });

        // =============================
        // ImageView19 ➜ scale animation + intent ke resetpassword_berhasil
        imageView19.setOnClickListener(v -> {
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
                    imageView19.startAnimation(scaleDown);
                }

                @Override
                public void onAnimationRepeat(Animation animation) { }
            });

            scaleDown.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }

                @Override
                public void onAnimationEnd(Animation animation) {
                    // Navigasi ke halaman resetpassword_berhasil
                    Intent intent = new Intent(resetpassword2.this, resetpassword_berhasil.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onAnimationRepeat(Animation animation) { }
            });

            imageView19.startAnimation(scaleUp);
        });

    }
}
