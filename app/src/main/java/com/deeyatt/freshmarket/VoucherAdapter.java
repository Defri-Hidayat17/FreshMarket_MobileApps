package com.deeyatt.freshmarket;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

    private final int[] voucherImages; // array gambar drawable

    public VoucherAdapter(int[] voucherImages) {
        this.voucherImages = voucherImages;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        holder.imageVoucher.setImageResource(voucherImages[position]);

        holder.itemView.setOnClickListener(v -> {
            v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100)
                    .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(100).start())
                    .start();

            v.postDelayed(() -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, VoucherPage.class);
                context.startActivity(intent);
            }, 300);
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
