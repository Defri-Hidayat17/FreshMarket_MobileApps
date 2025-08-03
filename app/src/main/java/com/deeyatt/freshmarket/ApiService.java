package com.deeyatt.freshmarket;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // ================== PROFILE ==================
    @GET("get_profile.php")
    Call<UserModel> getProfile(@Query("email") String email);

    @FormUrlEncoded
    @POST("update_profile.php")
    Call<ResponseBody> updateProfile(
            @Field("name") String name,
            @Field("email") String email,
            @Field("bio") String bio,
            @Field("birthday") String birthday,
            @Field("gender") String gender,
            @Field("photo") String photo
    );

    // ================== PRODUK ==================
    @GET("get_produk.php")
    Call<ResponseProdukList> getProduk();

    @GET("get_produk_by_id.php")
    Call<ResponseProdukSingle> getProdukById(@Query("id") String id);

    // ================== CART ==================
    @FormUrlEncoded
    @POST("add_to_cart.php")
    Call<BaseResponse> addToCart(
            @Field("id_produk") String idProduk,
            @Field("qty") int qty
    );


    @GET("get_cart.php")
    Call<ResponseCartList> getCart();

    @FormUrlEncoded
    @POST("delete_cart_item.php")
    Call<ResponseBody> deleteCartItem(@Field("id") String cartId);

    @FormUrlEncoded
    @POST("update_cart_qty.php")
    Call<BaseResponse> updateCartQty(
            @Field("id") String cartId,   // id dari tabel cart
            @Field("qty") int qty
    );



}
