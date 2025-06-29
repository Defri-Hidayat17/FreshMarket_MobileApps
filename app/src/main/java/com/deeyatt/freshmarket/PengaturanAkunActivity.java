package com.deeyatt.freshmarket;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PengaturanAkunActivity extends AppCompatActivity {

    ImageView btnBack, delete1;
    Switch switchLokasi1, switchKamera1, switchFile1;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_akun);

        // Inisialisasi views
        btnBack = findViewById(R.id.btnBack);
        delete1 = findViewById(R.id.delete1);
        switchLokasi1 = findViewById(R.id.switchLokasi1);
        switchKamera1 = findViewById(R.id.switchKamera1);
        switchFile1 = findViewById(R.id.switchFile1);

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("PengaturanPrefs", MODE_PRIVATE);

        // Set status terakhir dari SharedPreferences
        switchLokasi1.setChecked(getSwitchState("lokasi"));
        switchKamera1.setChecked(getSwitchState("kamera"));
        switchFile1.setChecked(getSwitchState("file"));

        // Listener untuk menyimpan status saat diubah
        switchLokasi1.setOnCheckedChangeListener((buttonView, isChecked) -> saveSwitchState("lokasi", isChecked));
        switchKamera1.setOnCheckedChangeListener((buttonView, isChecked) -> saveSwitchState("kamera", isChecked));
        switchFile1.setOnCheckedChangeListener((buttonView, isChecked) -> saveSwitchState("file", isChecked));

        // Tombol kembali dengan animasi dan kembali ke fragment sebelumnya
        btnBack.setOnClickListener(v -> {
            playScaleAnimation(v);
            new Handler().postDelayed(() -> {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }, 150);
        });

        // Hapus akun dialog
        delete1.setOnClickListener(v -> {
            playScaleAnimation(v);
            new Handler().postDelayed(this::showConfirmDialog, 150);
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

    private void saveSwitchState(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private boolean getSwitchState(String key) {
        return sharedPreferences.getBoolean(key, false); // default: OFF
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_delete, null);
        builder.setView(view);

        TextView btnTidak = view.findViewById(R.id.btnTidak);
        TextView btnIya = view.findViewById(R.id.btnIya);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        btnTidak.setOnClickListener(v -> dialog.dismiss());

        btnIya.setOnClickListener(v -> {
            dialog.dismiss();

            Toast toast = Toast.makeText(getApplicationContext(), "Akun berhasil dihapus", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            new Handler().postDelayed(() -> {
                Intent intent = new Intent(PengaturanAkunActivity.this, loginpage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }, 1200);
        });
    }
}
