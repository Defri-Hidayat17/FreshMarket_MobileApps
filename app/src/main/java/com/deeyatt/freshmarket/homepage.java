package com.deeyatt.freshmarket;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class homepage extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        bottomNav = findViewById(R.id.bottomNavigationView);

        // Matikan ripple
        bottomNav.setItemRippleColor(ColorStateList.valueOf(Color.TRANSPARENT));
        bottomNav.setItemRippleColor(null);

        // ✅ Pasang listener untuk navigasi fragment
        bottomNav.setOnItemSelectedListener(item -> {
            // Reset semua item skala normal
            for (int i = 0; i < bottomNav.getMenu().size(); i++) {
                int id = bottomNav.getMenu().getItem(i).getItemId();
                if (bottomNav.findViewById(id) != null) {
                    bottomNav.findViewById(id).animate().scaleX(1f).scaleY(1f).setDuration(150).start();
                }
            }

            // Scale item aktif
            if (bottomNav.findViewById(item.getItemId()) != null) {
                bottomNav.findViewById(item.getItemId()).animate().scaleX(1.2f).scaleY(1.2f).setDuration(150).start();
            }

            // Load fragment sesuai item
            Fragment selectedFragment;
            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_cart) {
                selectedFragment = new CartFragment();
            } else {
                selectedFragment = new ProfileFragment();
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();

            return true;
        });

        // ✅ Baca intent dan set awal fragment
        if (savedInstanceState == null) {
            String destination = getIntent().getStringExtra("navigate_to");
            if ("cart".equals(destination)) {
                bottomNav.setSelectedItemId(R.id.nav_cart);
            } else if ("profile".equals(destination)) {
                bottomNav.setSelectedItemId(R.id.nav_profile);
            } else {
                bottomNav.setSelectedItemId(R.id.nav_home);
            }
        }
    }
}
