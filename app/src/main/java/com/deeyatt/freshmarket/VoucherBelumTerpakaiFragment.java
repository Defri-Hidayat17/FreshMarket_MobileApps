package com.deeyatt.freshmarket;

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

    public VoucherBelumTerpakaiFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout
        View view = inflater.inflate(R.layout.fragment_voucher_belum_terpakai, container, false);

        recyclerView = view.findViewById(R.id.recyclerVoucher);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        int[] gambarVoucher = {
                R.drawable.voucher_satu,
                R.drawable.voucher_dua,
        };

        adapter = new VoucherAdapter(gambarVoucher);
        recyclerView.setAdapter(adapter);


        // (Optional) Tambahkan jarak antar item
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull android.graphics.Rect outRect, @NonNull View view,
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
