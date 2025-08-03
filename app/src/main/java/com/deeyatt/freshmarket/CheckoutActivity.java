package com.deeyatt.freshmarket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class CheckoutActivity extends AppCompatActivity {

    private ImageView btnBackCheckout, imageProductCheckout;
    private EditText editAlamat, editPesanPenjual;
    private TextView textNamaProduk, textHargaProduk, textQtyProduk, textTotalHarga, textPilihVoucher, textMetodePembayaran;
    private Button btnPesanSekarang;

    private String namaProduk, hargaProduk, gambarProduk;
    private int qtyProduk, totalHarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // --- Init View ---
        btnBackCheckout = findViewById(R.id.btnBackCheckout);
        imageProductCheckout = findViewById(R.id.imageProductCheckout);
        editAlamat = findViewById(R.id.editAlamat);
        editPesanPenjual = findViewById(R.id.editPesanPenjual);
        textNamaProduk = findViewById(R.id.textNamaProduk);
        textHargaProduk = findViewById(R.id.textHargaProduk);
        textQtyProduk = findViewById(R.id.textQtyProduk);
        textTotalHarga = findViewById(R.id.textTotalHarga);
        textPilihVoucher = findViewById(R.id.textPilihVoucher);
        textMetodePembayaran = findViewById(R.id.textMetodePembayaran);
        btnPesanSekarang = findViewById(R.id.btnPesanSekarang);

        // --- Ambil data dari Intent ---
        namaProduk = getIntent().getStringExtra("nama_produk");
        hargaProduk = getIntent().getStringExtra("harga_produk");
        gambarProduk = getIntent().getStringExtra("gambar_produk");
        qtyProduk = getIntent().getIntExtra("qty_produk", 1);
        totalHarga = getIntent().getIntExtra("total_harga", 0);

        // --- Set data ke view ---
        textNamaProduk.setText(namaProduk);
        textHargaProduk.setText("Rp " + hargaProduk);
        textQtyProduk.setText("x" + qtyProduk);
        textTotalHarga.setText("Total: Rp " + totalHarga);

        if (gambarProduk != null && !gambarProduk.isEmpty()) {
            String imageUrl = "http://192.168.1.36/freshmarket/images/" + gambarProduk;
            Glide.with(this).load(imageUrl).placeholder(android.R.drawable.ic_menu_gallery).into(imageProductCheckout);
        } else {
            imageProductCheckout.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        // --- Button Back ---
        ImageView btnBackCheckout = findViewById(R.id.btnBackCheckout);
        btnBackCheckout.setOnClickListener(v -> {
            finish(); // menutup CheckoutActivity dan kembali ke CartFragment
        });


        // --- Navigasi ke Pilih Voucher ---
        textPilihVoucher.setOnClickListener(v -> {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            // Cek apakah fragment voucher sudah ada
            if (!(currentFragment instanceof VoucherBelumTerpakaiFragment)) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new VoucherBelumTerpakaiFragment())
                        .addToBackStack(null) // supaya bisa kembali dengan tombol back
                        .commit();
            }
        });



        // --- Pesan Sekarang ---
        btnPesanSekarang.setOnClickListener(v -> {
            // Ambil alamat yang diketik
            String alamat = editAlamat.getText().toString().trim();
            String pesan = editPesanPenjual.getText().toString().trim();

            Intent intent = new Intent(this, PesananBerhasilActivity.class);
            intent.putExtra("nama_produk", namaProduk);
            intent.putExtra("harga_produk", hargaProduk);
            intent.putExtra("qty_produk", qtyProduk);
            intent.putExtra("total_harga", totalHarga);
            intent.putExtra("alamat", alamat);
            intent.putExtra("pesan_penjual", pesan);
            startActivity(intent);

            finish();
        });

        // Metode Pembayaran fixed COD
        textMetodePembayaran.setText("COD");
    }
}
