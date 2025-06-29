package com.deeyatt.freshmarket;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class PesananPage extends AppCompatActivity {

    TabLayout tabLayout;
    ImageView btnBack;
    TextView titlePesanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_page);

        tabLayout = findViewById(R.id.tabLayout);
        btnBack = findViewById(R.id.btnBack);
        titlePesanan = findViewById(R.id.titlePesanan);

        titlePesanan.setText("Pesanan Saya");

        // Tombol kembali dengan animasi
        btnBack.setOnClickListener(v -> {
            playScaleAnimation(v);
            v.postDelayed(() -> {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }, 150);
        });

        // Tambahkan semua tab
        tabLayout.addTab(tabLayout.newTab().setText("Dikemas"));
        tabLayout.addTab(tabLayout.newTab().setText("Dikirim"));
        tabLayout.addTab(tabLayout.newTab().setText("Selesai"));
        tabLayout.addTab(tabLayout.newTab().setText("Pengembalian"));
        tabLayout.addTab(tabLayout.newTab().setText("Dibatalkan"));

// Setelah semua tab ditambahkan
        Typeface customTypeface = ResourcesCompat.getFont(this, R.font.lexenddecasemibold);

// Loop semua tab dan ubah font
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null && tab.getCustomView() == null) {
                TextView tabTextView = new TextView(this);
                tabTextView.setText(tab.getText());
                tabTextView.setTypeface(customTypeface);
                tabTextView.setTextSize(14); // ukuran bisa kamu sesuaikan
                tabTextView.setTextColor(getResources().getColorStateList(R.color.tab_selector)); // pakai selector kalau perlu
                tab.setCustomView(tabTextView);
            }
        }



        // Default fragment saat pertama dibuka
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.pesananFragmentContainer, new PesananDikemasFragment())
                .commit();

        // Ganti fragment sesuai tab yang dipilih
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selected = null;

                switch (tab.getPosition()) {
                    case 0:
                        selected = new PesananDikemasFragment();
                        break;
                    case 1:
                        selected = new PesananDikirimFragment();
                        break;
                    case 2:
                        selected = new PesananSelesaiFragment();
                        break;
                    case 3:
                        selected = new PesananPengembalianFragment();
                        break;
                    case 4:
                        selected = new PesananDibatalkanFragment();
                        break;
                }

                if (selected != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.pesananFragmentContainer, selected)
                            .commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
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
