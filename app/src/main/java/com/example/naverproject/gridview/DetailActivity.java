/*
 * DetailActivity
 * 2023.06.08
 */


package com.example.naverproject.gridview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.naverproject.R;
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

public class DetailActivity extends AppCompatActivity implements AbsListView.OnScrollListener{
        public String moreKeyword;
        private final List<ListItem> gridItemList = new ArrayList<>();
        private GridAdapter gridAdapter;
        private boolean lastVisibleItemCheck = false;
        private boolean loading = false; // 화면을 로딩하는 중인지 체크하는 플래그

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_detail);

                int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
                uiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                uiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                getWindow().getDecorView().setSystemUiVisibility(uiOptions);

                ImageButton imageButton = findViewById(R.id.backbtn);
                TextView moreText = findViewById(R.id.moreText);

                Intent intent = getIntent();
                moreKeyword = intent.getStringExtra("keyword");

                moreText.setText(moreKeyword);
                GridView gridView = findViewById(R.id.gridView);

                buildItemList(moreKeyword, 21, 1);

                gridAdapter = new GridAdapter(this, gridItemList);
                gridView.setAdapter(gridAdapter);
                gridView.setOnScrollListener(this);

                imageButton.setOnClickListener(view -> onBackPressed());
        }

        private void buildItemList(String keyword, int display, int start) {
                if (Constants.USE_JSON_DATA) { // jsonData boolean값 true면 Json데이터 양식
                        ApiSearchImage.getInstance().searchImagesJSON(keyword, display, start, new Callback<ApiResponseJSON>() {
                                @Override
                                public void onResponse(Call<ApiResponseJSON> call, Response<ApiResponseJSON> response) {
                                        if (response.isSuccessful()) {
                                                ApiResponseJSON apiResult = response.body();

                                                List<ListItem> gridItem = apiResult.getItems();
                                                gridItemList.addAll(gridItem);

                                                runOnUiThread(() -> {
                                                        gridAdapter.notifyDataSetChanged();
                                                        loading = false;
                                                });
                                        } else {
                                                // 실패 처리
                                        }
                                }
                                @Override
                                public void onFailure(Call<ApiResponseJSON> call, Throwable t) {
                                        // 오류 처리
                                }
                        });
                } else {
                        ApiSearchImage.getInstance().searchImagesXML(keyword, display, start, new Callback<ApiResponseXML>() {
                                @Override
                                public void onResponse(Call<ApiResponseXML> call, Response<ApiResponseXML> response) {
                                        if (response.isSuccessful()) {
                                                ApiResponseXML apiResult = response.body();

                                                List<ListItem> gridItem = apiResult.getItems();
                                                gridItemList.addAll(gridItem);

                                                runOnUiThread(() -> {
                                                        gridAdapter.notifyDataSetChanged();
                                                        loading = false;
                                                });
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

        @Override
        public void onBackPressed() {
                DetailActivity.super.onBackPressed();
                finish();
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        if (lastVisibleItemCheck && !loading) {
                                loading = true;
                                int start = gridItemList.size();

                                buildItemList(moreKeyword, 21, start);
                        }
                }
        }

        // loading = false
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleItemIndex = firstVisibleItem + visibleItemCount - 1;
                lastVisibleItemCheck = totalItemCount > 0 && lastVisibleItemIndex == totalItemCount - 1;
        }
}