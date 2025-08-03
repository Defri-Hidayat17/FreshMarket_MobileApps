package com.deeyatt.freshmarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartList = new ArrayList<>();
    private TextView textTotal, textEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        textTotal = view.findViewById(R.id.textTotal);
        textEmpty = view.findViewById(R.id.textEmpty);

        cartAdapter = new CartAdapter(requireContext(), cartList, new CartAdapter.OnCartActionListener() {
            @Override
            public void onCartChanged() {
                updateTotal();
            }

            @Override
            public void onDeleteItem(String idProduk) {
                deleteCartItem(idProduk);
            }
        });

        // Navigasi ke CheckoutActivity ketika item diklik
        cartAdapter.setOnItemClickListener(item -> navigateToCheckout(item));

        recyclerView.setAdapter(cartAdapter);
        loadCartFromServer();
        return view;
    }

    private void navigateToCheckout(CartItem item) {
        int total = calculateTotal();

        Intent intent = new Intent(getContext(), CheckoutActivity.class);
        intent.putExtra("nama_produk", item.getNama_produk());
        intent.putExtra("harga_produk", item.getHarga());
        intent.putExtra("qty_produk", item.getQty());
        intent.putExtra("gambar_produk", item.getGambar());
        intent.putExtra("total_harga", total);
        startActivity(intent);
    }

    private int calculateTotal() {
        int total = 0;
        for (CartItem item : cartList) {
            try {
                int harga = Integer.parseInt(item.getHarga().replace(".", "").trim());
                total += harga * item.getQty();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return total;
    }

    private void loadCartFromServer() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseCartList> call = apiService.getCart();

        call.enqueue(new Callback<ResponseCartList>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCartList> call, @NonNull Response<ResponseCartList> response) {
                if (!isAdded()) return;

                cartList.clear();
                if (response.isSuccessful() && response.body() != null &&
                        response.body().isStatus() && response.body().getData() != null &&
                        !response.body().getData().isEmpty()) {
                    cartList.addAll(response.body().getData());
                    cartAdapter.notifyDataSetChanged();
                    updateTotal();
                    showCart();
                } else {
                    showEmptyCart();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCartList> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                showEmptyCart();
                Toast.makeText(requireContext(), "Gagal memuat keranjang: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteCartItem(String idProduk) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.deleteCartItem(idProduk).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Item dihapus", Toast.LENGTH_SHORT).show();
                loadCartFromServer();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Gagal hapus: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEmptyCart() {
        cartList.clear();
        cartAdapter.notifyDataSetChanged();
        textEmpty.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        textTotal.setText("Total: Rp 0");
    }

    private void showCart() {
        textEmpty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void updateTotal() {
        textTotal.setText("Total: Rp " + calculateTotal());
    }
}
