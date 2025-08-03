package com.deeyatt.freshmarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    public interface OnCartActionListener {
        void onCartChanged();
        void onDeleteItem(String idProduk);
    }

    public interface OnItemClickListener {
        void onItemClick(CartItem item);
    }

    private final Context context;
    private final List<CartItem> cartList;
    private final OnCartActionListener listener;
    private OnItemClickListener onItemClickListener;

    public CartAdapter(Context context, List<CartItem> cartList, OnCartActionListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.listener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CartItem item = cartList.get(position);

        // Tampilkan data produk
        holder.textProductName.setText(item.getNama_produk());
        holder.textProductPrice.setText("Rp " + item.getHarga());
        holder.textProductDesc.setText("Produk segar dan sehat");
        holder.textQuantity.setText(String.valueOf(item.getQty()));

        // Load gambar
        String imageUrl = "http://192.168.1.36/freshmarket/images/" + item.getGambar();
        Glide.with(context).load(imageUrl).into(holder.imageProduct);

        // Tombol tambah qty
        holder.buttonIncrease.setOnClickListener(v -> {
            int qty = item.getQty() + 1;
            updateQtyOnServer(item.getId(), qty);
            item.setQty(qty);
            holder.textQuantity.setText(String.valueOf(qty));
            applyScaleAnimation(holder.buttonIncrease);
            if (listener != null) listener.onCartChanged();
        });

        // Tombol kurang qty
        holder.buttonDecrease.setOnClickListener(v -> {
            int qty = item.getQty();
            if (qty > 1) {
                qty--;
                updateQtyOnServer(item.getId(), qty);
                item.setQty(qty);
                holder.textQuantity.setText(String.valueOf(qty));
                applyScaleAnimation(holder.buttonDecrease);
                if (listener != null) listener.onCartChanged();
            }
        });

        // Tombol hapus item
        holder.buttonDelete.setOnClickListener(v -> {
            applyScaleAnimation(holder.buttonDelete);
            String idCart = item.getId();
            cartList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartList.size());
            if (listener != null) listener.onDeleteItem(idCart);
        });

        // Klik item → trigger ke fragment/activity
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageProduct, buttonDecrease, buttonIncrease, buttonDelete;
        TextView textProductName, textProductPrice, textProductDesc, textQuantity;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            textProductName = itemView.findViewById(R.id.textProductName);
            textProductPrice = itemView.findViewById(R.id.textProductPrice);
            textProductDesc = itemView.findViewById(R.id.textProductDesc);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            buttonDecrease = itemView.findViewById(R.id.buttonDecrease);
            buttonIncrease = itemView.findViewById(R.id.buttonIncrease);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

    private void applyScaleAnimation(View view) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1f, 1.2f,
                1f, 1.2f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(150);
        scaleAnimation.setRepeatCount(1);
        scaleAnimation.setRepeatMode(ScaleAnimation.REVERSE);
        view.startAnimation(scaleAnimation);
    }

    private void updateQtyOnServer(String idCart, int qty) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.updateCartQty(idCart, qty).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                // Tidak ada Toast untuk update qty
            }

            @Override
            public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                // Tidak ada Toast untuk update qty
            }
        });
    }
}
