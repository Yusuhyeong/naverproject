/*
 * MainActivity
 * 2023.06.07
 */

package com.example.naverproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.naverproject.R;
import com.example.naverproject.ViewPager.ListActivity;
import com.example.naverproject.api.ApiResponseJSON;
import com.example.naverproject.api.ApiResponseXML;
import com.example.naverproject.api.ApiSearchImage;
import com.example.naverproject.gridview.DetailActivity;
import com.example.naverproject.utils.Constants;
import com.example.naverproject.utils.HomeValue;
import com.example.naverproject.utils.ListItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{
        private String moveActivity = Constants.HOME;
        HomeFragment homeFragment;
        SearchFragment searchFragment;
        SettingFragment settingFragment;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

                int option = getWindow().getDecorView().getSystemUiVisibility();
                option ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                option ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                HomeValue.getInstance().setUiOptions(option);
                getWindow().getDecorView().setSystemUiVisibility(HomeValue.getInstance().getUiOptions());

                Intent mainIntent = getIntent();
                getIntentData(mainIntent);

                setFragment(moveActivity);

                NavigationBarView navigationBarView = findViewById(R.id.bottom_navigationview);
                navigationBarView.setOnItemSelectedListener(item -> {
                        int itemId = item.getItemId();
                        if (itemId == R.id.home) {
                                if(homeFragment == null){
                                        homeFragment = new HomeFragment();
                                        getSupportFragmentManager().beginTransaction().add(R.id.containers, homeFragment).commit();
                                }
                                if(homeFragment != null)
                                        getSupportFragmentManager().beginTransaction().show(homeFragment).commit();
                                if(searchFragment != null)
                                        getSupportFragmentManager().beginTransaction().hide(searchFragment).commit();
                                if(settingFragment != null)
                                        getSupportFragmentManager().beginTransaction().hide(settingFragment).commit();
                                return true;
                        }  else if (itemId == R.id.search) {
                                if(searchFragment == null) {
                                        searchFragment = new SearchFragment();
                                        getSupportFragmentManager().beginTransaction().add(R.id.containers, searchFragment).commit();
                                }
                                if(homeFragment != null)
                                        getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
                                if(searchFragment != null)
                                        getSupportFragmentManager().beginTransaction().show(searchFragment).commit();
                                if(settingFragment != null)
                                        getSupportFragmentManager().beginTransaction().hide(settingFragment).commit();
                                return true;
                        }else if (itemId == R.id.setting) {
                                if(settingFragment == null) {
                                        settingFragment = new SettingFragment();
                                        getSupportFragmentManager().beginTransaction().add(R.id.containers, settingFragment).commit();
                                }
                                if(homeFragment != null)
                                        getSupportFragmentManager().beginTransaction().hide(homeFragment).commit();
                                if(searchFragment != null)
                                        getSupportFragmentManager().beginTransaction().hide(searchFragment).commit();
                                if(settingFragment != null)
                                        getSupportFragmentManager().beginTransaction().show(settingFragment).commit();
                                return true;
                        }
                        return false;
                });
        }

        /**
         *
         * @param intent fcm을 통해 받아오는 intent정보
         */
        public void getIntentData(Intent intent){
                // 앱이 종료된 시점에서 FCM에서 오는 데이터 받기
                if (intent.getExtras() != null) {
                        for (String key : intent.getExtras().keySet()) {
                                // 페이로드 데이터 key값 중 Fragment or Activity값 받아오기
                                Object value = intent.getExtras().get(key);
                                if (value instanceof String) {
                                        if(key.equals(Constants.ACTION)){
                                                // Fragment or Activity 결정
                                                moveActivity = (String) value;
                                        }
                                        // 페이로드 데이터 중 key값에 action 이외에 검색단어를 받아올 keyword가 있다면
                                        else if(key.equals(Constants.KEWYWORD)){
                                                // keyword값 결정
                                                String keyword = (String) value;

                                                if(moveActivity.equals(Constants.HOME_DETAIL)){
                                                        setFragment(Constants.HOME);
                                                        // home의 상세화면으로 이동
                                                        Intent keywordIntent = new Intent(this, DetailActivity.class);
                                                        keywordIntent.putExtra(Constants.KEWYWORD, keyword);

                                                        startActivity(keywordIntent);
                                                }

                                                else if(moveActivity.equals(Constants.LIST_DETAIL)){
                                                        setFragment(Constants.SEARCH);
                                                        // search의 상세화면으로 이동
                                                        Intent keywordIntent = new Intent(this, ListActivity.class);
                                                        keywordIntent.putExtra(Constants.KEWYWORD, keyword);

                                                        // 데이터 저장
                                                        if (Constants.USE_JSON_DATA) {
                                                                ApiSearchImage.getInstance().searchImagesJSON(keyword, 20, 1, new Callback<ApiResponseJSON>() {
                                                                        @Override
                                                                        public void onResponse(@NonNull Call<ApiResponseJSON> call, @NonNull Response<ApiResponseJSON> response) {
                                                                                if (response.isSuccessful()) {
                                                                                        ApiResponseJSON apiResult = response.body();

                                                                                        List<ListItem> viewpagerItem = apiResult.getItems();
                                                                                        HomeValue.getInstance().setKeyword(keyword);
                                                                                        HomeValue.getInstance().setSearchImageList(viewpagerItem);
                                                                                        HomeValue.getInstance().setResultCount(apiResult.getTotal());

                                                                                        startActivity(keywordIntent);
                                                                                }
                                                                        }
                                                                        @Override
                                                                        public void onFailure(@NonNull Call<ApiResponseJSON> call, @NonNull Throwable t) {
                                                                                // 오류 처리
                                                                        }
                                                                });
                                                        }
                                                        else {
                                                                ApiSearchImage.getInstance().searchImagesXML(keyword, 10, 1, new Callback<ApiResponseXML>() {
                                                                        @Override
                                                                        public void onResponse(@NonNull Call<ApiResponseXML> call, @NonNull Response<ApiResponseXML> response) {
                                                                                if (response.isSuccessful()) {
                                                                                        ApiResponseXML apiResult = response.body();

                                                                                        List<ListItem> viewpagerItem = apiResult.getItems();
                                                                                        HomeValue.getInstance().setKeyword(keyword);
                                                                                        HomeValue.getInstance().setSearchImageList(viewpagerItem);
                                                                                        HomeValue.getInstance().setResultCount(apiResult.getChannel().getTotal());
                                                                                }
                                                                        }
                                                                        @Override
                                                                        public void onFailure(@NonNull Call<ApiResponseXML> call, @NonNull Throwable t) {
                                                                                // 오류 처리
                                                                        }
                                                                });
                                                        }
                                                }
                                        }
                                }
                        }

                        // 알람이 켜져있을 때의 FCM메세지를 통하여 FirebaseMessagingService의 intent값 받아오기
                        if(intent.getStringExtra(Constants.CHECK_FRAGMENT) != null){
                                // fagment결정
                                moveActivity = getIntent().getStringExtra(Constants.CHECK_FRAGMENT);
                        }
                }
                else{
                        Log.d("FCM Message", "no key, value");
                }
        }

        /**
         *
         * @param menuId : 클릭된 메뉴 item (setting->home, search->home)
         */
        public void setSelectedMenu(int menuId) {
                BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigationview);
                Menu menu = bottomNavigationView.getMenu();
                MenuItem menuItem = menu.findItem(menuId);
                if (menuItem != null) {
                        menuItem.setChecked(true);
                }
        }

        public void setFragment(String checkFragment){
                switch (checkFragment) {
                        case Constants.HOME:
                                homeFragment = new HomeFragment();
                                getSupportFragmentManager().beginTransaction().add(R.id.containers, homeFragment).commit();
                                setSelectedMenu(R.id.home);
                                break;
                        case Constants.SEARCH:
                                searchFragment = new SearchFragment();
                                getSupportFragmentManager().beginTransaction().add(R.id.containers, searchFragment).commit();
                                setSelectedMenu(R.id.search);
                                break;
                        case Constants.SETTING:
                                settingFragment = new SettingFragment();
                                getSupportFragmentManager().beginTransaction().add(R.id.containers, settingFragment).commit();
                                setSelectedMenu(R.id.setting);
                                break;
                }
        }
}
