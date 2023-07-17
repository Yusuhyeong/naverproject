package com.example.naverproject.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface NaverImageApiJSON {
        @GET("/v1/search/image?")
        Call<ApiResponseJSON> searchImagesJSON(@QueryMap Map<String, String> queryMap);
}