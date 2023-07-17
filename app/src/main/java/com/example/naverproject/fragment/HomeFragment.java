/*
 * HomeFragment
 * 2023.06.08
 */

package com.example.naverproject.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.naverproject.R;
import com.example.naverproject.RecyclerView.Item;
import com.example.naverproject.RecyclerView.ItemAdapter;
import com.example.naverproject.api.ApiResponseJSON;
import com.example.naverproject.api.ApiResponseXML;
import com.example.naverproject.api.ApiSearchImage;
import com.example.naverproject.utils.Constants;
import com.example.naverproject.utils.ListItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
        private String[] category = {"액션 게임", "인디 게임", "캐주얼 게임", "레이싱 게임", "대전 게임", "스포츠 게임"};
        private List<Item> homeItemList = new ArrayList<>();
        private List<ListItem> nullItem = new ArrayList<>();
        private ItemAdapter itemAdapter;
        public HomeFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        @SuppressLint("MissingInflatedId")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
                View v = inflater.inflate(R.layout.fragment_home, container, false);
                RecyclerView rvItem = v.findViewById(R.id.rv_item);

                nullItem.add(new ListItem("", ""));

                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

                itemAdapter = new ItemAdapter(getActivity(), homeItemList);
                rvItem.setAdapter(itemAdapter);
                rvItem.setLayoutManager(layoutManager);

                buildItemList();

                return v;
        }

        private void buildItemList() {
                if (Constants.USE_JSON_DATA) { // jsonData boolean값 true면 Json데이터 양식
                        for (int i = 0; i < category.length; i++) {
                                int finalI = i;
                                homeItemList.add(finalI, new Item(category[finalI], nullItem));
                                ApiSearchImage.getInstance().searchImagesJSON(category[i], 10, 1, new Callback<ApiResponseJSON>() {
                                        @Override
                                        public void onResponse(Call<ApiResponseJSON> call, Response<ApiResponseJSON> response) {
                                                if (response.isSuccessful()) {
                                                        ApiResponseJSON apiResult = response.body();

                                                        List<ListItem> recyclerItem = apiResult.getItems();
                                                        homeItemList.set(finalI, new Item(category[finalI], recyclerItem));

                                                        requireActivity().runOnUiThread(() -> itemAdapter.notifyDataSetChanged());
                                                } else {
                                                        // 실패 처리
                                                }
                                        }
                                        @Override
                                        public void onFailure(Call<ApiResponseJSON> call, Throwable t) {
                                                // 오류 처리
                                        }
                                });
                        }
                }
                else {
                        for (int i = 0; i < category.length; i++) {
                                int finalI = i;
                                homeItemList.add(finalI, new Item(category[finalI], nullItem));
                                ApiSearchImage.getInstance().searchImagesXML(category[i], 10, 1, new Callback<ApiResponseXML>() {
                                        @Override
                                        public void onResponse(Call<ApiResponseXML> call, Response<ApiResponseXML> response) {
                                                if (response.isSuccessful()) {
                                                        ApiResponseXML apiResult = response.body();

                                                        List<ListItem> recyclerItem = apiResult.getItems();
                                                        homeItemList.add(new Item(category[finalI], recyclerItem));

                                                        requireActivity().runOnUiThread(() -> itemAdapter.notifyDataSetChanged());
                                                } else {
                                                        // 실패 처리
                                                }
                                        }
                                        @Override
                                        public void onFailure(Call<ApiResponseXML> call, Throwable t) {
                                                // 오류 처리
                                        }
                                });
                        }
                }
        }
}