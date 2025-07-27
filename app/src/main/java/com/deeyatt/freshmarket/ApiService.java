package com.deeyatt.freshmarket;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // Ambil profil dari MySQL (GET)
    @GET("get_profile.php")
    Call<UserModel> getProfile(@Query("email") String email);

    // Update profil ke MySQL (POST)
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
}
