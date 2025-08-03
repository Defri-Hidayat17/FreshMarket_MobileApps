package com.deeyatt.freshmarket;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private String id;          // id dari tabel cart
    private String id_produk;   // id produk asli
    private String nama_produk;
    private String harga;
    private String gambar;
    private String deskripsi;
    private int qty;
    private boolean selected;   // Tambahan: untuk checkbox

    public CartItem() {
    }

    // --- Parcelable Constructor ---
    protected CartItem(Parcel in) {
        id = in.readString();
        id_produk = in.readString();
        nama_produk = in.readString();
        harga = in.readString();
        gambar = in.readString();
        deskripsi = in.readString();
        qty = in.readInt();
        selected = in.readByte() != 0;
    }

    // --- Parcelable CREATOR ---
    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    // --- Getter ---
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

    public String getDeskripsi() {
        return deskripsi;
    }

    public int getQty() {
        return qty;
    }

    public boolean isSelected() {
        return selected;
    }

    // --- Setter ---
    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    // --- Parcelable Method ---
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(id_produk);
        dest.writeString(nama_produk);
        dest.writeString(harga);
        dest.writeString(gambar);
        dest.writeString(deskripsi);
        dest.writeInt(qty);
        dest.writeByte((byte) (selected ? 1 : 0));
    }
}
