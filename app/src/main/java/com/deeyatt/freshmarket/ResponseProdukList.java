package com.deeyatt.freshmarket;

import java.util.List;

public class ResponseProdukList {
    private boolean status;
    private List<Produk> data;

    public boolean isStatus() { return status; }
    public List<Produk> getData() { return data; }
}
