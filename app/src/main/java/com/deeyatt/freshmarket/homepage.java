package com.deeyatt.freshmarket;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class homepage extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        bottomNav = findViewById(R.id.bottomNavigationView);

        // Matikan efek ripple
        bottomNav.setItemRippleColor(ColorStateList.valueOf(Color.TRANSPARENT));
        bottomNav.setItemRippleColor(null);

        // Listener bottom navigation
        bottomNav.setOnItemSelectedListener(item -> {
            // Reset semua item ke skala normal
            for (int i = 0; i < bottomNav.getMenu().size(); i++) {
                int id = bottomNav.getMenu().getItem(i).getItemId();
                if (bottomNav.findViewById(id) != null) {
                    bottomNav.findViewById(id).animate()
                            .scaleX(1f).scaleY(1f)
                            .setDuration(150).start();
                }
            }

            // Besarkan item yang dipilih
            if (bottomNav.findViewById(item.getItemId()) != null) {
                bottomNav.findViewById(item.getItemId()).animate()
                        .scaleX(1.2f).scaleY(1.2f)
                        .setDuration(150).start();
            }

            // Tentukan fragment yang dipilih
            Fragment selectedFragment;
            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_cart) {
                // Ambil id produk dari intent jika ada
                String idProduk = getIntent().getStringExtra("id");

                CartFragment cartFragment = new CartFragment();
                if (idProduk != null && !idProduk.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", idProduk);
                    cartFragment.setArguments(bundle);
                }
                selectedFragment = cartFragment;
            } else {
                selectedFragment = new ProfileFragment();
            }

            // Replace fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();

            return true;
        });

        // Saat pertama kali buka activity
        String destination = getIntent().getStringExtra("navigate_to");
        String idProduk = getIntent().getStringExtra("id");

        if ("cart".equals(destination)) {
            CartFragment cartFragment = new CartFragment();
            if (idProduk != null && !idProduk.isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putString("id", idProduk);
                cartFragment.setArguments(bundle);
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, cartFragment)
                    .commit();

            bottomNav.setSelectedItemId(R.id.nav_cart);

        } else if ("profile".equals(destination)) {
            bottomNav.setSelectedItemId(R.id.nav_profile);
        } else {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }
}
