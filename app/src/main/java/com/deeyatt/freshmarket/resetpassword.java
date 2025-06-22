package com.deeyatt.freshmarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class resetpassword extends AppCompatActivity {

    EditText inputEmail;
    ImageView imageView19;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi EditText dan ImageView
        inputEmail = findViewById(R.id.inputEmail);
        imageView19 = findViewById(R.id.imageView19);

        imageView19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // VALIDASI: Cek apakah inputEmail kosong
                String email = inputEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(resetpassword.this, "Anda belum mengisi Email", Toast.LENGTH_SHORT).show();
                    return; // stop animasi & intent kalau kosong
                }

                // Scale up
                ScaleAnimation scaleUp = new ScaleAnimation(
                        1.0f, 1.1f,
                        1.0f, 1.1f,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                scaleUp.setDuration(150);

                // Scale down
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
                        // Pindah ke resetpassword2
                        Intent intent = new Intent(resetpassword.this, resetpassword2.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) { }
                });

                imageView19.startAnimation(scaleUp);
            }
        });

    }
}
