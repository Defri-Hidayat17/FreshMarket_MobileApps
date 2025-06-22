package com.deeyatt.freshmarket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class loginpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // === EFEK SCALE ===
        TextView textView6 = findViewById(R.id.textView6);
        TextView textView8 = findViewById(R.id.textView8);
        ImageView imageView10 = findViewById(R.id.imageView10);

        addScaleEffect(textView6);
        addScaleEffect(textView8);
        addScaleEffect(imageView10);

        // === PINDAH KE DAFTAR ===
        TextView daftarSekarang = findViewById(R.id.textView8);
        daftarSekarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginpage.this, daftarakunpage.class);
                startActivity(intent);
            }
        });

        textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginpage.this, resetpassword.class);
                startActivity(intent);
                finish();
            }
        });


        imageView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginpage.this, homepage.class);
                startActivity(intent);
                finish();
            }
        });


        // === TOGGLE PASSWORD VISIBILITY ===
        EditText passwordEditText = findViewById(R.id.inputPassword);
        final boolean[] isPasswordVisible = {false};

        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.lock,
                0,
                R.drawable.eye_off,
                0
        );

        passwordEditText.setOnTouchListener((v, event) -> {
            final int DRAWABLE_RIGHT = 2;
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (passwordEditText.getRight()
                        - passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                    if (isPasswordVisible[0]) {
                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.lock,
                                0,
                                R.drawable.eye_off,
                                0
                        );
                        isPasswordVisible[0] = false;
                    } else {
                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(
                                R.drawable.lock,
                                0,
                                R.drawable.eye,
                                0
                        );
                        isPasswordVisible[0] = true;
                    }
                    passwordEditText.setSelection(passwordEditText.getText().length());
                    return true;
                }
            }
            return false;
        });

        // === REMEMBER ME ===
        ImageView kotakCeklis = findViewById(R.id.imageView5);
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        boolean isRememberMe = prefs.getBoolean("rememberMe", false);

        if (isRememberMe) {
            kotakCeklis.setImageResource(R.drawable.kotak_ceklis_on);
        } else {
            kotakCeklis.setImageResource(R.drawable.kotak_ceklis);
        }

        kotakCeklis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean currentStatus = prefs.getBoolean("rememberMe", false);
                if (currentStatus) {
                    kotakCeklis.setImageResource(R.drawable.kotak_ceklis);
                    editor.putBoolean("rememberMe", false);
                } else {
                    kotakCeklis.setImageResource(R.drawable.kotak_ceklis_on);
                    editor.putBoolean("rememberMe", true);
                }
                editor.apply();
            }
        });

    }

    // === HELPER SCALE EFFECT ===
    private void addScaleEffect(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate().scaleX(1.1f).scaleY(1.1f).setDuration(100).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                    break;
            }
            return false;
        });
    }

}
