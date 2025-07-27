package com.deeyatt.freshmarket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

            // ambil data
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String savedUid = prefs.getString("uid", null);
            boolean rememberMe = prefs.getBoolean("rememberMe", false);
            boolean rememberMeGoogle = prefs.getBoolean("rememberMeGoogle", false);

            Intent intent;
            if (user != null && savedUid != null && user.getUid().equals(savedUid)
                    && (rememberMe || rememberMeGoogle)) {
                // Sudah login + centang ingat saya → langsung homepage
                intent = new Intent(MainActivity.this, homepage.class);
            } else {
                boolean firstOpen = prefs.getBoolean("firstOpen", true);
                if (firstOpen) {
                    intent = new Intent(MainActivity.this, onboarding_satu.class);
                } else {
                    intent = new Intent(MainActivity.this, loginpage.class);
                }
            }
            startActivity(intent);
            finish();
        }, 2000); // delay 2 detik
    }
}
