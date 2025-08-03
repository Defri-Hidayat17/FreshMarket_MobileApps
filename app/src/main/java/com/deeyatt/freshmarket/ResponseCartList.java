package com.deeyatt.freshmarket;

import java.util.List;

public class ResponseCartList {
    private boolean status;
    private List<CartItem> data;

    public boolean isStatus() {
        return status;
    }

    public List<CartItem> getData() {
        return data;
    }
}
