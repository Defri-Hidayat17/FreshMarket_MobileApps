package com.deeyatt.freshmarket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class menu_sayuran extends AppCompatActivity {

    private int cartCount = 0;
    private TextView cartBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_sayuran);

        // Atur padding biar tidak ketabrak status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView btnBack = findViewById(R.id.imageView1);
        ImageView btnCart = findViewById(R.id.imageView30);
        cartBadge = findViewById(R.id.cartBadge);

        // Back ke homepage
        btnBack.setOnClickListener(v -> {
            playScaleAnimation(v);
            Intent intent = new Intent(menu_sayuran.this, homepage.class);
            intent.putExtra("navigate_to", "home");
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Klik icon cart → ke FragmentCart
        btnCart.setOnClickListener(v -> {
            playScaleAnimation(v);
            Intent intent = new Intent(menu_sayuran.this, homepage.class);
            intent.putExtra("navigate_to", "cart");
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Tombol add keranjang per produk
        setupCartButtons();

        // Ambil jumlah cart dari server saat pertama kali load
        getCartCountFromServer();
    }

    private void setupCartButtons() {
        int[] btnIds = {
                R.id.btnKeranjang1, R.id.btnKeranjang2, R.id.btnKeranjang3,
                R.id.btnKeranjang4, R.id.btnKeranjang5, R.id.btnKeranjang6
        };

        for (int btnId : btnIds) {
            ImageView btn = findViewById(btnId);
            btn.setOnClickListener(v -> {
                playScaleAnimation(v);

                String idProduk = "1";
                if (btnId == R.id.btnKeranjang2) idProduk = "2";
                else if (btnId == R.id.btnKeranjang3) idProduk = "3";
                else if (btnId == R.id.btnKeranjang4) idProduk = "4";
                else if (btnId == R.id.btnKeranjang5) idProduk = "5";
                else if (btnId == R.id.btnKeranjang6) idProduk = "6";

                addToCart(idProduk);
            });
        }
    }

    /**
     * Tambahkan produk ke cart di server
     */
    private void addToCart(String idProduk) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<BaseResponse> call = apiService.addToCart(idProduk, 1);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isStatus()) {
                        // Ambil ulang jumlah cart dari server supaya akurat
                        getCartCountFromServer();
                        Toast.makeText(menu_sayuran.this,
                                response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(menu_sayuran.this,
                                "Gagal tambah: " + response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(menu_sayuran.this,
                            "Response tidak valid", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Toast.makeText(menu_sayuran.this,
                        "Gagal koneksi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Ambil jumlah item dalam cart dari server
     */
    private void getCartCountFromServer() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ResponseCartList> call = apiService.getCart();

        call.enqueue(new Callback<ResponseCartList>() {
            @Override
            public void onResponse(Call<ResponseCartList> call, Response<ResponseCartList> response) {
                if (response.isSuccessful() && response.body() != null &&
                        response.body().isStatus() && response.body().getData() != null) {
                    cartCount = response.body().getData().size();
                } else {
                    cartCount = 0;
                }
                updateBadge();
            }

            @Override
            public void onFailure(Call<ResponseCartList> call, Throwable t) {
                cartCount = 0;
                updateBadge();
            }
        });
    }

    private void updateBadge() {
        if (cartBadge != null) {
            if (cartCount > 0) {
                cartBadge.setText(String.valueOf(cartCount));
                cartBadge.setVisibility(View.VISIBLE);
            } else {
                cartBadge.setVisibility(View.GONE);
            }
        }
    }

    private void playScaleAnimation(View view) {
        ScaleAnimation scale = new ScaleAnimation(
                1f, 0.9f, 1f, 0.9f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        );
        scale.setDuration(100);
        scale.setRepeatCount(1);
        scale.setRepeatMode(ScaleAnimation.REVERSE);
        view.startAnimation(scale);
    }
}
