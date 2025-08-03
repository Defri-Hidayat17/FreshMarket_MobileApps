package com.deeyatt.freshmarket;

public class BaseResponse {
    private boolean status;
    private String message;

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
