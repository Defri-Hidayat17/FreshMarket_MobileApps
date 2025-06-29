package com.deeyatt.freshmarket;

public class Voucher {
    private String nama;
    private String deskripsi;
    private String masaBerlaku;

    public Voucher(String nama, String deskripsi, String masaBerlaku) {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.masaBerlaku = masaBerlaku;
    }

    public String getNama() {
        return nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getMasaBerlaku() {
        return masaBerlaku;
    }
}
