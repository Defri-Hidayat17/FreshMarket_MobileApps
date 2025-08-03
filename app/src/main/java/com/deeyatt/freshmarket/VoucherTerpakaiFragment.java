package com.deeyatt.freshmarket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class VoucherTerpakaiFragment extends Fragment {

    public VoucherTerpakaiFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Layout hanya menampilkan teks
        return inflater.inflate(R.layout.fragment_voucher_terpakai, container, false);
    }
}
