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
        bottomNav.setItemRippleColor(ColorStateList.valueOf(Color.TRANSPARENT));


        // ✅ Matikan ripple
        bottomNav.setItemRippleColor(null);

        // ✅ Pasang listener SATU KALI SAJA
        bottomNav.setOnItemSelectedListener(item -> {
            // Reset scale semua item
            for (int i = 0; i < bottomNav.getMenu().size(); i++) {
                int id = bottomNav.getMenu().getItem(i).getItemId();
                if (bottomNav.findViewById(id) != null) {
                    bottomNav.findViewById(id).animate().scaleX(1f).scaleY(1f).setDuration(150).start();
                }
            }

            // Scale item yang dipilih
            if (bottomNav.findViewById(item.getItemId()) != null) {
                bottomNav.findViewById(item.getItemId()).animate().scaleX(1.2f).scaleY(1.2f).setDuration(150).start();
            }

            // Load fragment
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

        // ✅ Pertama kali buka: Home
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }
}
