package com.deeyatt.freshmarket;

import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PengaturanNotifikasi extends AppCompatActivity {

    private static final String PREFS_NAME = "notif_prefs";
    private SharedPreferences prefs;

    ImageView btnBack;
    Switch switchtampil, switchgetar, switchsuara, switchlayar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_notifikasi);

        // 1. Inset handling agar layout tidak tertutup status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 2. Inisialisasi SharedPreferences
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // 3. Inisialisasi tombol back dengan animasi
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            playScaleAnimation(v);
            new Handler().postDelayed(() -> {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }, 150);
        });

        // 4. Inisialisasi switch dan set state awal dari prefs
        switchtampil = findViewById(R.id.switchtampil);
        switchgetar  = findViewById(R.id.switchgetar);
        switchsuara  = findViewById(R.id.switchsuara);
        switchlayar  = findViewById(R.id.switchlayar);

        switchtampil.setChecked(prefs.getBoolean("pref_tampil", true));
        switchgetar .setChecked(prefs.getBoolean("pref_getar", true));
        switchsuara .setChecked(prefs.getBoolean("pref_suara", true));
        switchlayar .setChecked(prefs.getBoolean("pref_layar", true));

        // 5. Listener untuk setiap switch
        switchtampil.setOnCheckedChangeListener((btn, isOn) -> {
            prefs.edit().putBoolean("pref_tampil", isOn).apply();
            Toast.makeText(this,
                    isOn ? "Notifikasi akan ditampilkan" : "Notifikasi disembunyikan",
                    Toast.LENGTH_SHORT).show();
        });

        switchgetar.setOnCheckedChangeListener((btn, isOn) -> {
            prefs.edit().putBoolean("pref_getar", isOn).apply();
            if (isOn) {
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (vib != null && vib.hasVibrator()) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        vib.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vib.vibrate(300);
                    }
                }
                Toast.makeText(this, "Getar diaktifkan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Getar dinonaktifkan", Toast.LENGTH_SHORT).show();
            }
        });

        switchsuara.setOnCheckedChangeListener((btn, isOn) -> {
            prefs.edit().putBoolean("pref_suara", isOn).apply();
            if (isOn) {
                // mainkan suara default notifikasi sebagai contoh
                try {
                    Uri notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notif);
                    r.setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build());
                    r.play();
                } catch (Exception e) { /* ignore */ }
                Toast.makeText(this, "Suara notifikasi diaktifkan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Suara notifikasi dinonaktifkan", Toast.LENGTH_SHORT).show();
            }
        });

        switchlayar.setOnCheckedChangeListener((btn, isOn) -> {
            prefs.edit().putBoolean("pref_layar", isOn).apply();
            Toast.makeText(this,
                    isOn ? "Notifikasi muncul di layar kunci" : "Notifikasi disembunyikan di layar kunci",
                    Toast.LENGTH_SHORT).show();
        });
    }

    // Animasi scale untuk tombol
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
