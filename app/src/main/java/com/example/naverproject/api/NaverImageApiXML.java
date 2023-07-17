package com.example.naverproject.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface NaverImageApiXML {
        @GET("/v1/search/image.xml")
        Call<ApiResponseXML> searchImagesXML(@QueryMap Map<String, String> queryMap);
}