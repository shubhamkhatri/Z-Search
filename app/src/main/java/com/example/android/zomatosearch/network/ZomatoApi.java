package com.example.android.zomatosearch.network;

import com.example.android.zomatosearch.model.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ZomatoApi {

    @Headers("user-key: bcb6e7d3e9422946c72cb17ad992792e")
    @GET("search")
    Call<ApiResponse> getNearbyRestaurants(@Query("lat") double lat,
                                           @Query("lon") double lon,
                                           @Query("count") int count,
                                           @Query("radius") double radius,
                                           @Query("q") String s);
}