package com.deeyatt.freshmarket;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.animation.ScaleAnimation;

import com.google.android.material.tabs.TabLayout;

public class VoucherPage extends AppCompatActivity {

    TabLayout tabLayout;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher_page);

        tabLayout = findViewById(R.id.tabLayout);
        btnBack = findViewById(R.id.btnBack);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            playScaleAnimation(v);

            // Tunggu animasi selesai baru finish
            v.postDelayed(() -> {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }, 150); // 150ms = durasi animasi playScaleAnimation()
        });

        // Tambahkan tab
        tabLayout.addTab(tabLayout.newTab().setText("Terpakai"));
        tabLayout.addTab(tabLayout.newTab().setText("Belum Terpakai"));
        tabLayout.addTab(tabLayout.newTab().setText("Kadaluwarsa"));

        // Tampilkan fragment default
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.voucherFragmentContainer, new VoucherTerpakaiFragment())
                .commit();

        // Aksi ketika tab diklik
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selected = null;
                switch (tab.getPosition()) {
                    case 0:
                        selected = new VoucherTerpakaiFragment();
                        break;
                    case 1:
                        selected = new VoucherBelumTerpakaiFragment();
                        break;
                    case 2:
                        selected = new VoucherKadaluwarsaFragment();
                        break;
                }

                if (selected != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.voucherFragmentContainer, selected)
                            .commit();
                }
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
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
