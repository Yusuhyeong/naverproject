/*
 * ApiSearchImage
 * 2023.06.02
 */

package com.example.naverproject.api;

import androidx.annotation.NonNull;

import com.example.naverproject.utils.Constants;
import com.tickaroo.tikxml.TikXml;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiSearchImage {
        private NaverImageApiJSON naverImageApiJSON;
        private NaverImageApiXML naverImageApiXML;

        public ApiSearchImage() {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(new Interceptor() {
                        @NonNull
                        @Override
                        public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                                Request originalRequest = chain.request();
                                Request.Builder builder = originalRequest.newBuilder()
                                        .header(Constants.HEADER_ID, Constants.CLIENT_ID)
                                        .header(Constants.HEADER_SECRET, Constants.CLIENT_SECRET);
                                Request newRequest = builder.build();
                                return chain.proceed(newRequest);
                        }
                });

                if(Constants.USE_JSON_DATA){
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(Constants.BASE_URL)
                                .client(httpClient.build())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        naverImageApiJSON = retrofit.create(NaverImageApiJSON.class);
                }
                else{
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(Constants.BASE_URL)
                                .client(httpClient.build())
                                .addConverterFactory(TikXmlConverterFactory.create(new TikXml.Builder().exceptionOnUnreadXml(false).build()))
                                .build();

                        naverImageApiXML = retrofit.create(NaverImageApiXML.class);
                }
        }

        private static class LazyHolder {
                private static final ApiSearchImage INSTANCE = new ApiSearchImage();
        }

        public static ApiSearchImage getInstance() {
                return LazyHolder.INSTANCE;
        }

        /**
         * @param searchText 검색 단어
         * @param display    보여질 검색 개수
         * @param start      검색 위치
         */

        public void searchImagesJSON(String searchText, int display, int start, Callback<ApiResponseJSON> callback) {
                Map<String, String> queryMap = new HashMap<>();
                queryMap.put(Constants.QUERY, searchText);
                queryMap.put(Constants.DISPLAY, Integer.toString(display));
                queryMap.put(Constants.START, Integer.toString(start));

                Call<ApiResponseJSON> call = naverImageApiJSON.searchImagesJSON(queryMap);
                call.enqueue(callback);
        }

        public void searchImagesXML(String searchText, int display, int start, Callback<ApiResponseXML> callback) {
                Map<String, String> queryMap = new HashMap<>();
                queryMap.put(Constants.QUERY, searchText);
                queryMap.put(Constants.DISPLAY, Integer.toString(display));
                queryMap.put(Constants.START, Integer.toString(start));

                Call<ApiResponseXML> call = naverImageApiXML.searchImagesXML(queryMap);
                call.enqueue(callback);
        }
}