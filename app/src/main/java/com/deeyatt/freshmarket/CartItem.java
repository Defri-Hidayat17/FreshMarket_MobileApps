package com.deeyatt.freshmarket;

public class CartItem {
    private String id;          // id dari tabel cart
    private String id_produk;   // id produk asli
    private String nama_produk;
    private String harga;
    private String gambar;
    private int qty;

    public String getId() {
        return id;
    }

    public String getId_produk() {
        return id_produk;
    }

    public String getNama_produk() {
        return nama_produk;
    }

    public String getHarga() {
        return harga;
    }

    public String getGambar() {
        return gambar;
    }

    public int getQty() {
        return qty;
    }

    // ==== Tambahan setter supaya bisa update qty ====
    public void setQty(int qty) {
        this.qty = qty;
    }
}
