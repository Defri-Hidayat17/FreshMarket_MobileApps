package com.deeyatt.freshmarket;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PesananBerhasilActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesanan_berhasil);

        TextView textPesan = findViewById(R.id.textPesanBerhasil);
        Button btnKembali = findViewById(R.id.btnKembaliHome);

        btnKembali.setOnClickListener(v -> {
            finish(); // kembali ke halaman sebelumnya (misal homepage)
        });
    }
}
