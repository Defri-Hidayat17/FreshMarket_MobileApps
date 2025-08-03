package com.deeyatt.freshmarket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private ImageView btnBackCheckout;
    private EditText editAlamat, editPesanPenjual;
    private TextView textTotalHarga, textPilihVoucher, textMetodePembayaran;
    private Button btnPesanSekarang;
    private RecyclerView recyclerCheckout;

    private ArrayList<CartItem> selectedItems;
    private CheckoutAdapter checkoutAdapter;
    private int totalHarga = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // --- Init View ---
        btnBackCheckout = findViewById(R.id.btnBackCheckout);
        editAlamat = findViewById(R.id.editAlamat);
        editPesanPenjual = findViewById(R.id.editPesanPenjual);
        textTotalHarga = findViewById(R.id.textTotalHarga);
        textPilihVoucher = findViewById(R.id.textPilihVoucher);
        textMetodePembayaran = findViewById(R.id.textMetodePembayaran);
        btnPesanSekarang = findViewById(R.id.btnPesanSekarang);
        recyclerCheckout = findViewById(R.id.recyclerCheckout);

        // --- Ambil data dari Intent ---
        selectedItems = getIntent().getParcelableArrayListExtra("selected_items");
        if (selectedItems == null) {
            selectedItems = new ArrayList<>();
        }

        // --- Hitung total harga ---
        for (CartItem item : selectedItems) {
            int harga = parseHarga(item.getHarga());
            totalHarga += harga * item.getQty();
        }
        textTotalHarga.setText("Total: Rp " + totalHarga);

        // --- Setup RecyclerView untuk produk yang dipilih ---
        recyclerCheckout.setLayoutManager(new LinearLayoutManager(this));
        checkoutAdapter = new CheckoutAdapter(this, selectedItems);
        recyclerCheckout.setAdapter(checkoutAdapter);

        // --- Button Back ---
        btnBackCheckout.setOnClickListener(v -> finish());

        // --- Navigasi ke Voucher Page ---
        textPilihVoucher.setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivity.this, VoucherPage.class);
            intent.putExtra("OPEN_TAB", 0); // 0 = tab "Belum Terpakai"
            startActivity(intent);
        });

        // --- Pesan Sekarang ---
        btnPesanSekarang.setOnClickListener(v -> {
            String alamat = editAlamat.getText().toString().trim();
            String pesan = editPesanPenjual.getText().toString().trim();

            Intent intent = new Intent(this, PesananBerhasilActivity.class);
            intent.putExtra("selected_items", selectedItems);
            intent.putExtra("total_harga", totalHarga);
            intent.putExtra("alamat", alamat);
            intent.putExtra("pesan_penjual", pesan);
            startActivity(intent);

            finish();
        });

        // Metode Pembayaran fixed COD
        textMetodePembayaran.setText("COD");
    }

    /**
     * Ambil angka harga sebelum '/' (contoh "5000/3pcs" -> 5000)
     */
    private int parseHarga(String hargaStr) {
        if (hargaStr == null || hargaStr.isEmpty()) return 0;
        try {
            int slashIndex = hargaStr.indexOf('/');
            String hargaAngka = (slashIndex != -1) ? hargaStr.substring(0, slashIndex).trim() : hargaStr.trim();
            hargaAngka = hargaAngka.replaceAll("[^0-9]", "");
            return Integer.parseInt(hargaAngka);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
