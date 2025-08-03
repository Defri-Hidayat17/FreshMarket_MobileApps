package com.deeyatt.freshmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.MyViewHolder> {

    private final Context context;
    private final List<CartItem> itemList;

    public CheckoutAdapter(Context context, List<CartItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_checkout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CartItem item = itemList.get(position);
        holder.textNamaProduk.setText(item.getNama_produk());
        holder.textQty.setText("x" + item.getQty());
        holder.textHarga.setText("Rp " + item.getHarga());

        String imageUrl = "http://192.168.1.36/freshmarket/images/" + item.getGambar();
        Glide.with(context).load(imageUrl).into(holder.imageProduk);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduk;
        TextView textNamaProduk, textQty, textHarga;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduk = itemView.findViewById(R.id.imageProduk);
            textNamaProduk = itemView.findViewById(R.id.textNamaProduk);
            textQty = itemView.findViewById(R.id.textQty);
            textHarga = itemView.findViewById(R.id.textHarga);
        }
    }
}
