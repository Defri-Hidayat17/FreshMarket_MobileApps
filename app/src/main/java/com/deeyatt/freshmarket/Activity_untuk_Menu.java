package com.deeyatt.freshmarket;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

public class Activity_untuk_Menu extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_untuk_menu);

        String destination = getIntent().getStringExtra("navigate_to");

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_menu);
        NavController navController = navHostFragment.getNavController();

        if (destination != null) {
            if (destination.equals("home")) {
                navController.navigate(R.id.homeFragment);
            } else if (destination.equals("cart")) {
                navController.navigate(R.id.cartFragment);
            }
        }
    }
}
