package com.deeyatt.freshmarket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<CartItem> cartList = new ArrayList<>();
    private TextView textTotal, textEmpty;
    private Button buttonCheckout;

    // simpan ID produk yang dicentang
    private final Set<String> selectedIds = new HashSet<>();

    private static final int REQUEST_CHECKOUT = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        textTotal = view.findViewById(R.id.textTotal);
        textEmpty = view.findViewById(R.id.textEmpty);
        buttonCheckout = view.findViewById(R.id.buttonCheckout);

        cartAdapter = new CartAdapter(requireContext(), cartList, new CartAdapter.OnCartActionListener() {
            @Override
            public void onCartChanged() {
                syncSelectedIds();
                updateSelectedTotalSafe();
            }

            @Override
            public void onDeleteItem(String idProduk) {
                deleteCartItem(idProduk);
            }

            @Override
            public void onSelectionChanged() {
                syncSelectedIds();
                updateSelectedTotalSafe();
            }
        });

        cartAdapter.setOnItemClickListener(this::navigateToCheckoutSingle);
        recyclerView.setAdapter(cartAdapter);

        buttonCheckout.setOnClickListener(v -> navigateToCheckoutSelected());

        loadCartFromServer();
        return view;
    }

    private void navigateToCheckoutSingle(CartItem item) {
        // Single item checkout
        int total = parseHarga(item.getHarga()) * item.getQty();
        Intent intent = new Intent(getContext(), CheckoutActivity.class);
        intent.putExtra("nama_produk", item.getNama_produk());
        intent.putExtra("harga_produk", item.getHarga());
        intent.putExtra("qty_produk", item.getQty());
        intent.putExtra("gambar_produk", item.getGambar());
        intent.putExtra("total_harga", total);
        startActivityForResult(intent, REQUEST_CHECKOUT);
    }

    private void navigateToCheckoutSelected() {
        ArrayList<CartItem> selectedItems = new ArrayList<>();
        for (CartItem item : cartList) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }

        if (selectedItems.isEmpty()) {
            Toast.makeText(getContext(), "Pilih produk terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        int total = calculateSelectedTotal();
        Intent intent = new Intent(getContext(), CheckoutActivity.class);
        intent.putParcelableArrayListExtra("selected_items", selectedItems);
        intent.putExtra("total_harga", total);
        startActivityForResult(intent, REQUEST_CHECKOUT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECKOUT && resultCode == Activity.RESULT_OK) {
            loadCartFromServer();
        }
    }

    private int parseHarga(String hargaStr) {
        if (hargaStr == null || hargaStr.isEmpty()) return 0;
        try {
            int slashIndex = hargaStr.indexOf('/');
            String hargaAngka = (slashIndex != -1) ? hargaStr.substring(0, slashIndex).trim() : hargaStr.trim();
            hargaAngka = hargaAngka.replaceAll("[^0-9]", "");
            return Integer.parseInt(hargaAngka);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int calculateSelectedTotal() {
        int total = 0;
        for (CartItem item : cartList) {
            if (item.isSelected()) {
                int harga = parseHarga(item.getHarga());
                int subtotal = harga * item.getQty();
                Log.d("CartFragment", "Item: " + item.getNama_produk() +
                        ", Harga: " + harga + ", Qty: " + item.getQty() +
                        ", Subtotal: " + subtotal);
                total += subtotal;
            }
        }
        Log.d("CartFragment", "Total selected akhir = " + total);
        return total;
    }

    private void loadCartFromServer() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseCartList> call = apiService.getCart();

        call.enqueue(new Callback<ResponseCartList>() {
            @Override
            public void onResponse(@NonNull Call<ResponseCartList> call,
                                   @NonNull Response<ResponseCartList> response) {
                if (!isAdded()) return;

                cartList.clear();

                if (response.isSuccessful() && response.body() != null &&
                        response.body().isStatus() && response.body().getData() != null &&
                        !response.body().getData().isEmpty()) {

                    cartList.addAll(response.body().getData());

                    // restore centang
                    for (CartItem item : cartList) {
                        item.setSelected(selectedIds.contains(item.getId()));
                    }

                    cartAdapter.notifyDataSetChanged();
                    updateSelectedTotalSafe();
                    showCart();
                } else {
                    showEmptyCart();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseCartList> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                showEmptyCart();
                Toast.makeText(requireContext(),
                        "Gagal memuat keranjang: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void syncSelectedIds() {
        selectedIds.clear();
        for (CartItem item : cartList) {
            if (item.isSelected()) {
                selectedIds.add(item.getId());
            }
        }
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
                Toast.makeText(requireContext(),
                        "Gagal hapus: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void updateSelectedTotalSafe() {
        int total = calculateSelectedTotal();
        if (getActivity() == null) return;
        getActivity().runOnUiThread(() -> textTotal.setText("Total: Rp " + total));
    }
}
