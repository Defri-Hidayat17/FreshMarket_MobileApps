package com.deeyatt.freshmarket;

public class UserModel {
    public String status;
    public Data data;

    public static class Data {
        public int id;
        public String name;
        public String email;
        public String bio;
        public String birthday;
        public String gender;
        public String photo;
    }
}
