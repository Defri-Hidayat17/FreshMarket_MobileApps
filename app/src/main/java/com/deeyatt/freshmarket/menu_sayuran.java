package com.deeyatt.freshmarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class menu_sayuran extends AppCompatActivity {

    private int cartCount = 0;
    private TextView cartBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_sayuran);

        // Inset padding (biar gak ketabrak status bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView btnBack = findViewById(R.id.imageView1);
        ImageView btnCart = findViewById(R.id.imageView30);
        cartBadge = findViewById(R.id.cartBadge);

        btnBack.setOnClickListener(v -> {
            playScaleAnimation(v);
            Intent intent = new Intent(menu_sayuran.this, homepage.class);
            intent.putExtra("navigate_to", "home");
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        btnCart.setOnClickListener(v -> {
            playScaleAnimation(v);
            Intent intent = new Intent(menu_sayuran.this, homepage.class);
            intent.putExtra("navigate_to", "cart");
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });



        setupCartButtons();
    }

    private void setupCartButtons() {
        int[] btnIds = {
                R.id.btnKeranjang1, R.id.btnKeranjang2, R.id.btnKeranjang3,
                R.id.btnKeranjang4, R.id.btnKeranjang5, R.id.btnKeranjang6
        };

        for (int id : btnIds) {
            ImageView btn = findViewById(id);
            btn.setOnClickListener(v -> {
                playScaleAnimation(v);
                cartCount++;
                updateBadge();
            });
        }
    }

    private void updateBadge() {
        if (cartBadge != null) {
            cartBadge.setText(String.valueOf(cartCount));
            cartBadge.setVisibility(View.VISIBLE);
        }
    }

    private void playScaleAnimation(View view) {
        ScaleAnimation scale = new ScaleAnimation(
                1f, 0.9f, 1f, 0.9f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(100);
        scale.setRepeatCount(1);
        scale.setRepeatMode(ScaleAnimation.REVERSE);
        view.startAnimation(scale);
    }
}
