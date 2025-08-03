package com.deeyatt.freshmarket;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VoucherBelumTerpakaiFragment extends Fragment {

    private RecyclerView recyclerView;
    private VoucherAdapter adapter;

    public VoucherBelumTerpakaiFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voucher_belum_terpakai, container, false);

        recyclerView = view.findViewById(R.id.recyclerVoucher);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Voucher aktif (belum terpakai)
        int[] gambarVoucher = {
                R.drawable.voucher_satu,
                R.drawable.voucher_dua
        };

        adapter = new VoucherAdapter(gambarVoucher, voucherResId -> {
            // Klik voucher (kalau mau nanti kirim balik ke Checkout)
        });
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 16;
                outRect.bottom = 16;
                outRect.left = 24;
                outRect.right = 24;
            }
        });

        return view;
    }
}
