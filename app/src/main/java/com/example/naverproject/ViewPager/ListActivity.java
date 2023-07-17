/*
 * ListActivity
 * 2023.06.07
 */

package com.example.naverproject.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.naverproject.R;
import com.example.naverproject.api.ApiResponseJSON;
import com.example.naverproject.api.ApiResponseXML;
import com.example.naverproject.api.ApiSearchImage;
import com.example.naverproject.utils.Constants;
import com.example.naverproject.utils.HomeValue;
import com.example.naverproject.utils.ListItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListActivity extends AppCompatActivity {
        public ArrayList<ListItem> imageList = new ArrayList<>(); // listview로 출력하기 위한 image list
        public ViewPager2 viewPager2; // 상세 이미지를 보여줄 viewPager2
        public int start = 0; // 마지막 페이지로 인해 불러올 index 데이터
        public int currentPostion = 0;

        public ArrayList<ViewPagerItem> list = new ArrayList<>(); // apdater로 들어갈 값
        private ViewPagerAdapter viewPagerAdapter; // 상세 이미지 데이터 adpater
        String Tag = "ListActivity";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_list);

                int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
                uiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                uiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                getWindow().getDecorView().setSystemUiVisibility(uiOptions);

                ImageButton imageButton = findViewById(R.id.backtn);
                Intent intent = getIntent();

                int index = intent.getIntExtra("position", 0); // 클릭된 listView의 해당 index값 저장

                HomeValue.getInstance().setTag(true);

                // list.clear()후 adpater2로 보여질 List에 listLink값 저장
                list.clear();

                for (int i = 0; i < HomeValue.getInstance().getSearchImageList().size(); i++) {
                        list.add(new ViewPagerItem(HomeValue.getInstance().getSearchImageList().get(i).getLink(), i, HomeValue.getInstance().getResultCount()));
                        imageList.add(i, HomeValue.getInstance().getSearchImageList().get(i));
                }

                HomeValue.getInstance().setSearchImageList(imageList);

                viewPager2 = findViewById(R.id.viewPager2);
                viewPager2.setPageTransformer(new ZoomOutPageTransformer()); // ViewPager2 애니메이션 효과, https://developer.android.com/training/animation/screen-slide-2?hl=ko#views 참고

                viewPagerAdapter = new ViewPagerAdapter(list); // 상세이미지에 대한 adpater할당
                viewPager2.setAdapter(viewPagerAdapter);
                viewPager2.setCurrentItem(index, false); // MainActivity로 받아온 position을 통해 해당 index값을 ViewPager2에 출력

                // 마지막 페이지가 클릭됐을 시 toast메세지로 마지막 페이지임을 알리는 부분
                if (HomeValue.getInstance().getResultCount() == index + 1) {
                        Toast.makeText(getApplicationContext(), "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show();
                }

                // 마지막 화면에 대한 추가 리스트 출력 (추가 이미지 출력이 가능하지만 마지막 화면에서 setAdapter를 다시 호출하여 화면이 돌아가는 현상)

                // ViewPager 추가 20개 이미지 로딩
                viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onPageSelected(int position) {
                        super.onPageSelected(position);

                        if ((HomeValue.getInstance().getResultCount() == position + 1) && HomeValue.getInstance().getTag()) {
                                Toast.makeText(getApplicationContext(), "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show();
                        }

                        if (position == list.size() - 2) { // 만약 마지막페이지에서 2번째 페이지에 도달했을 경우 ( 마지막 이미지에 도달했을 때 보다 그 전부터 이미지를 할당받아 넣어 놓고 자연스럽게 전환될 수 있도록 유도 )
                                start = imageList.size(); // 불러올 데이터의 처음 index값 ( API호출시 start지점이 될 수 있고 저장될 데이터의 index에 check_index를 더하여 저장된 데이터 이후의 데이터를 저장할 수 있게 한다 )

                                // 데이터 저장 방식은 MainActivity와 동일
                                if (Constants.USE_JSON_DATA) {
                                        Log.i(Tag, "Json파싱");
                                        ApiSearchImage.getInstance().searchImagesJSON(HomeValue.getInstance().getKeyword(), 20, start + 1, new Callback<ApiResponseJSON>() {
                                                @Override
                                                public void onResponse(@NonNull Call<ApiResponseJSON> call, @NonNull Response<ApiResponseJSON> response) {
                                                        if (response.isSuccessful()) {
                                                                ApiResponseJSON apiResult = response.body();

                                                                List<ListItem> viewpagerItem = apiResult.getItems();
                                                                imageList.addAll(viewpagerItem);

                                                                for(int i = 0; i < viewpagerItem.size(); i++){
                                                                        list.add(new ViewPagerItem(viewpagerItem.get(i).getLink(), i + start, HomeValue.getInstance().getResultCount()));
                                                                }
                                                        }
                                                }
                                                @Override
                                                public void onFailure(@NonNull Call<ApiResponseJSON> call, @NonNull Throwable t) {
                                                        // 오류 처리
                                                }
                                        });
                                } else {
                                        Log.i(Tag, "Xml파싱");
                                        ApiSearchImage.getInstance().searchImagesXML(HomeValue.getInstance().getKeyword(), 20, start + 1, new Callback<ApiResponseXML>() {
                                                @Override
                                                public void onResponse(@NonNull Call<ApiResponseXML> call, @NonNull Response<ApiResponseXML> response) {
                                                        if (response.isSuccessful()) {
                                                                ApiResponseXML apiResult = response.body();

                                                                List<ListItem> viewpagerItem = apiResult.getItems();
                                                                imageList.addAll(viewpagerItem);

                                                                for(int i = 0; i < viewpagerItem.size(); i++){
                                                                        list.add(new ViewPagerItem(viewpagerItem.get(i).getLink(), i + start, HomeValue.getInstance().getResultCount()));
                                                                }
                                                        }
                                                }
                                                @Override
                                                public void onFailure(Call<ApiResponseXML> call, Throwable t) {
                                                        // 오류 처리
                                                }
                                        });
                                }
                        }
                        viewPagerAdapter.notifyDataSetChanged();
                        }
                });

                // 뒤로 가기 버튼
                imageButton.setOnClickListener(view -> onBackPressed());
        }


        @Override
        public void onBackPressed() {
                currentPostion = viewPager2.getCurrentItem();
                Intent listIntent = new Intent();
                listIntent.putExtra("updateListView", true);
                listIntent.putExtra("currentPosition", currentPostion);
                setResult(Activity.RESULT_OK, listIntent);
                ListActivity.super.onBackPressed();
                finish();
        }
}