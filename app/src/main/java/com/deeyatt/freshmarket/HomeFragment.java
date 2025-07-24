package com.deeyatt.freshmarket;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class HomeFragment extends Fragment {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private ImageView imageView20;

    // Overlay Welcome
    private View welcomeOverlay;
    private Button btnGoToEditProfile;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --- Overlay Welcome ---
        welcomeOverlay = view.findViewById(R.id.welcomeOverlay);
        btnGoToEditProfile = view.findViewById(R.id.btnGoToEditProfile);

        checkFirstLoginAndShowOverlay();

        btnGoToEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
            hideWelcomeOverlay();
        });

        // Klik di luar card tidak menutup overlay
        welcomeOverlay.setOnClickListener(null);

        // --- Scroll voucher center ---
        HorizontalScrollView scrollView = view.findViewById(R.id.horizontalScrollView);
        LinearLayout linearLayout = view.findViewById(R.id.linearLayoutVouchers);

        scrollView.post(() -> {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            ImageView imageView22 = view.findViewById(R.id.imageView22);
            int imageWidth = imageView22.getWidth();
            int scrollTo = (imageView22.getLeft() + imageWidth / 2) - (screenWidth / 2);
            scrollView.scrollTo(scrollTo, 0);
        });

        scrollView.post(() -> {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            int halfScreen = screenWidth / 2;
            ImageView imageView22 = view.findViewById(R.id.imageView22);
            int imageWidth = imageView22.getWidth();
            linearLayout.setPadding(
                    halfScreen - (imageWidth / 2),
                    0,
                    halfScreen - (imageWidth / 2),
                    0
            );
            int scrollTo = imageView22.getLeft() - (halfScreen - (imageWidth / 2));
            scrollView.scrollTo(scrollTo, 0);
        });

        // --- Menu Buttons ---
        view.findViewById(R.id.imageView24).setOnClickListener(v -> openMenuWithAnim(v, menu_sayuran.class));
        view.findViewById(R.id.imageView25).setOnClickListener(v -> openMenuWithAnim(v, menu_buah.class));
        view.findViewById(R.id.imageView26).setOnClickListener(v -> openMenuWithAnim(v, menu_daging.class));
        view.findViewById(R.id.imageView27).setOnClickListener(v -> openMenuWithAnim(v, menu_sembako.class));
        view.findViewById(R.id.imageView28).setOnClickListener(v -> openMenuWithAnim(v, menu_frozenfood.class));

        // --- Lokasi Google Maps ---
        imageView20 = view.findViewById(R.id.imageView20);
        imageView20.setOnClickListener(v -> animateAndOpenMaps());

        // --- Logo Text Color ---
        TextView textView9 = view.findViewById(R.id.textView9);
        String freshText = "Fresh";
        String marketText = "Market";
        SpannableString spannable = new SpannableString(freshText + marketText);

        spannable.setSpan(
                new ForegroundColorSpan(Color.parseColor("#0F9E4F")),
                0,
                freshText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        spannable.setSpan(
                new ForegroundColorSpan(Color.parseColor("#85C442")),
                freshText.length(),
                freshText.length() + marketText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        textView9.setText(spannable);
    }

    private void checkFirstLoginAndShowOverlay() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("FreshMarketPrefs", Context.MODE_PRIVATE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        boolean showOverlay = false;

        if (user != null) {
            // --- Reset flag jika user berbeda (login baru) ---
            String lastUid = prefs.getString("lastUid", null);
            if (lastUid == null || !lastUid.equals(user.getUid())) {
                prefs.edit()
                        .putString("lastUid", user.getUid())
                        .putBoolean("isGoogleFirstLogin", true)
                        .putBoolean("isManualFirstLogin", true)
                        .apply();
            }

            boolean isGoogle = false;
            for (UserInfo profile : user.getProviderData()) {
                if ("google.com".equals(profile.getProviderId())) {
                    isGoogle = true;
                    break;
                }
            }
            if (isGoogle && prefs.getBoolean("isGoogleFirstLogin", true)) {
                showOverlay = true;
                prefs.edit().putBoolean("isGoogleFirstLogin", false).apply();
            } else if (!isGoogle && prefs.getBoolean("isManualFirstLogin", true)) {
                showOverlay = true;
                prefs.edit().putBoolean("isManualFirstLogin", false).apply();
            }
        }

        if (showOverlay) {
            showWelcomeOverlay();
        }
    }

    private void showWelcomeOverlay() {
        welcomeOverlay.setVisibility(View.VISIBLE);
        welcomeOverlay.bringToFront();
        welcomeOverlay.setAlpha(0f);
        welcomeOverlay.animate().alpha(1f).setDuration(500).start();

        // Sembunyikan Bottom Navigation
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottomNavigationView);
            if (bottomNav != null) bottomNav.setVisibility(View.GONE);
        }
    }

    private void hideWelcomeOverlay() {
        welcomeOverlay.setVisibility(View.GONE);

        // Tampilkan kembali Bottom Navigation
        if (getActivity() != null) {
            View bottomNav = getActivity().findViewById(R.id.bottomNavigationView);
            if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
        }
    }

    private void openMenuWithAnim(View v, Class<?> targetActivity) {
        playScaleAnimation(v);
        v.postDelayed(() -> {
            Intent intent = new Intent(requireContext(), targetActivity);
            startActivity(intent);
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }, 150);
    }

    private void animateAndOpenMaps() {
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
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation) {
                imageView20.startAnimation(scaleDown);
            }
            @Override public void onAnimationRepeat(Animation animation) {}
        });

        scaleDown.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation) {
                checkLocationPermissionAndOpenMaps();
            }
            @Override public void onAnimationRepeat(Animation animation) {}
        });

        imageView20.startAnimation(scaleUp);
    }

    private void checkLocationPermissionAndOpenMaps() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
        } else {
            openGoogleMaps();
        }
    }

    private void openGoogleMaps() {
        double latitude = -6.2551726;
        double longitude = 107.2690336;
        String uri = "https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGoogleMaps();
            } else {
                Toast.makeText(requireContext(), "Izin lokasi ditolak", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void playScaleAnimation(View view) {
        ScaleAnimation scale = new ScaleAnimation(
                1f, 0.9f, 1f, 0.9f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        );
        scale.setDuration(100);
        scale.setRepeatCount(1);
        scale.setRepeatMode(ScaleAnimation.REVERSE);
        view.startAnimation(scale);
    }
}
