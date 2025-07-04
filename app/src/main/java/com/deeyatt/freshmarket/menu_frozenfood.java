package com.deeyatt.freshmarket;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class menu_frozenfood extends AppCompatActivity {

    private int cartCount = 0; // untuk menyimpan jumlah item di keranjang
    private TextView cartBadge; // untuk menampilkan badge keranjang

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_frozenfood); // layout menu_buah.xml

        // Biar tidak ketabrak status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi tombol back & keranjang
        ImageView btnBack = findViewById(R.id.imageView1);
        ImageView btnCart = findViewById(R.id.imageView30);
        cartBadge = findViewById(R.id.cartBadge);

        // Tombol Back → kembali ke halaman utama
        btnBack.setOnClickListener(v -> {
            playScaleAnimation(v);
            Intent intent = new Intent(menu_frozenfood.this, homepage.class);
            intent.putExtra("navigate_to", "home");
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Tombol Keranjang → navigasi ke halaman keranjang
        btnCart.setOnClickListener(v -> {
            playScaleAnimation(v);
            Intent intent = new Intent(menu_frozenfood.this, homepage.class);
            intent.putExtra("navigate_to", "cart");
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Siapkan tombol keranjang (tambahkan ke cart)
        setupCartButtons();
    }

    // Fungsi ini akan menetapkan klik pada tombol keranjang dan update badge
    private void setupCartButtons() {
        int[] btnIds = {
                R.id.btnKeranjang1, R.id.btnKeranjang2,
                R.id.btnKeranjang3, R.id.btnKeranjang4
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

    // Fungsi ini akan memperbarui tampilan badge keranjang
    private void updateBadge() {
        if (cartBadge != null) {
            cartBadge.setText(String.valueOf(cartCount));
            cartBadge.setVisibility(View.VISIBLE);
        }
    }

    // Efek animasi kecil saat tombol ditekan
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