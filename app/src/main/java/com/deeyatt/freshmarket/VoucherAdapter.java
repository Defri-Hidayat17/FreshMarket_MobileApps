package com.deeyatt.freshmarket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

    private final int[] voucherImages; // array gambar drawable
    private final OnVoucherClickListener listener;

    // Tambahkan interface callback untuk kirim data voucher yang dipilih
    public interface OnVoucherClickListener {
        void onVoucherClick(int voucherResId);
    }

    public VoucherAdapter(int[] voucherImages, OnVoucherClickListener listener) {
        this.voucherImages = voucherImages;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        int voucherResId = voucherImages[position];
        holder.imageVoucher.setImageResource(voucherResId);

        holder.itemView.setOnClickListener(v -> {
            // Animasi klik
            v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100)
                    .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(100).start())
                    .start();

            // Delay biar animasi kelihatan
            v.postDelayed(() -> {
                if (listener != null) {
                    listener.onVoucherClick(voucherResId);
                }
            }, 200);
        });
    }

    @Override
    public int getItemCount() {
        return voucherImages.length;
    }

    public static class VoucherViewHolder extends RecyclerView.ViewHolder {
        ImageView imageVoucher;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            imageVoucher = itemView.findViewById(R.id.imageVoucher);
        }
    }
}
