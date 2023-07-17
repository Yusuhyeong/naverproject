/*
 * SearchFragment
 * 2023.06.07
 */

package com.example.naverproject.fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.naverproject.ListView.ListViewAdapter;
import com.example.naverproject.R;
import com.example.naverproject.ViewPager.ListActivity;
import com.example.naverproject.api.ApiResponseJSON;
import com.example.naverproject.api.ApiResponseXML;
import com.example.naverproject.api.ApiSearchImage;
import com.example.naverproject.utils.Constants;
import com.example.naverproject.utils.HomeValue;
import com.example.naverproject.utils.ListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment implements AbsListView.OnScrollListener { // 검색 api
        public int lastVisibleItemIndex = 0;
        public int start; // 스크롤시 기존 list의 size를 저장할 변수, 추가 데이터 요구시 이 크기를 시작으로 api호출
        public final String Tag = "SearchFragment";
        public final ArrayList<ListItem> imageList = new ArrayList<>(); // listview로 출력하기 위한 image list
        private ImageButton searchButton;
        private ListViewAdapter listViewAdapter; // listview로 출력하기 위한 adapter
        private ListView listView; // 화면에 출력될 listview
        // 스크롤 상태에 쓰일 변수들 (loading, scrollBottonCheck)
        private boolean loading = false; // 화면을 로딩하는 중인지 체크하는 플래그
        private boolean scrollBottomCheck = false; // scroll이 최하단까지 내려갔는지 체크하는 플래그
        private boolean lastVisibleItemCheck = false;
        private ActivityResultLauncher<Intent> launcher;

        public SearchFragment() {
                // Required empty public constructor
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
                // Inflate the layout for this fragment
                super.onCreate(savedInstanceState);

                getActivity().getWindow().getDecorView().setSystemUiVisibility(HomeValue.getInstance().getUiOptions());

                View v = inflater.inflate(R.layout.fragment_serach, container, false);

                EditText searchText = v.findViewById(R.id.searchText); // 검색할 단어를 사용할 변수 (EditText)
                TextView searchResult = v.findViewById(R.id.searchResult); // 검색한 단어의 검색 결과 개수
                searchButton = v.findViewById(R.id.searchBtn); // 검색을 실행할 사용할 변수 (imageButton)
                listView = v.findViewById(R.id.listview); // 이미지 노출될 listview

                listViewAdapter = new ListViewAdapter(getActivity(), imageList);
                listView.setAdapter(listViewAdapter);
                listView.setOnScrollListener(this);
                final InputMethodManager manager = (InputMethodManager) requireActivity().getSystemService(INPUT_METHOD_SERVICE);

                // EditText enter_key 이벤트 처리
                searchText.setOnKeyListener((v1, keyCode, event) -> {
                        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                                searchButton.callOnClick();
                                return true;
                        }
                        return false;
                });

                // 버튼 클릭 이벤트
                searchButton.setOnClickListener(view -> {
                        getActivity().getWindow().getDecorView().setSystemUiVisibility(HomeValue.getInstance().getUiOptions());

                        HomeValue.getInstance().setKeyword(searchText.getText().toString().trim()); // 검색할 단어(EditText)를 실질적으로 함수의 파라미터로 사용하기 위해 string문자열로 변경
                        // 검색 단어가 없을 시 이벤트
                        if (HomeValue.getInstance().getKeyword().equals("")) {
                                Toast.makeText(getActivity(), "검색 단어를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                                // 검색화면 클릭시 이벤트 발생 이전 list, arraylist 초기화
                                manager.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                HomeValue.getInstance().getSearchImageList().clear();
                                HomeValue.getInstance().getSearchLinkList().clear();
                                HomeValue.getInstance().getSearchTitleList().clear();
                                imageList.clear();

                                HomeValue.getInstance().setTag(true);

                                if (Constants.USE_JSON_DATA) { // jsonData boolean값 true면 Json데이터 양식
                                        ApiSearchImage.getInstance().searchImagesJSON(HomeValue.getInstance().getKeyword(), 20, 1, new Callback<ApiResponseJSON>() {
                                                @Override
                                                public void onResponse(@NonNull Call<ApiResponseJSON> call, @NonNull Response<ApiResponseJSON> response) {
                                                        if (response.isSuccessful()) {
                                                                ApiResponseJSON apiResult = response.body();

                                                                List<ListItem> listItem = apiResult.getItems();
                                                                searchResult.setText(String.format("총 검색 결과 : %d", apiResult.getTotal()));
                                                                HomeValue.getInstance().setResultCount(apiResult.getTotal());

                                                                if(apiResult.getTotal() == 0){
                                                                        requireActivity().runOnUiThread(() -> {
                                                                                Toast.makeText(getActivity(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                                                                        });
                                                                }
                                                                else{
                                                                        imageList.addAll(listItem);
                                                                        HomeValue.getInstance().setSearchImageList(apiResult.getItems());
                                                                        requireActivity().runOnUiThread(() -> {
                                                                                // 데이터 처리 후의 추가 작업 수행
                                                                                listViewAdapter.notifyDataSetChanged();
                                                                                listView.setSelection(0);
                                                                        });
                                                                }
                                                        } else {
                                                                // 실패 처리
                                                        }
                                                }

                                                @Override
                                                public void onFailure(@NonNull Call<ApiResponseJSON> call, @NonNull Throwable t) {
                                                        // 오류 처리
                                                }
                                        });
                                } else { // jsonData boolean값 false면 xml데이터 양식
                                        ApiSearchImage.getInstance().searchImagesXML(HomeValue.getInstance().getKeyword(), 20, 1, new Callback<ApiResponseXML>() {
                                                @Override
                                                public void onResponse(@NonNull Call<ApiResponseXML> call, @NonNull Response<ApiResponseXML> response) {
                                                        if (response.isSuccessful()) {
                                                                ApiResponseXML apiResult = response.body();

                                                                List<ListItem> listItem = apiResult.getItems();
                                                                searchResult.setText(String.format("총 검색 결과 : %d", apiResult.getChannel().getTotal()));
                                                                HomeValue.getInstance().setResultCount(apiResult.getChannel().getTotal());

                                                                if(apiResult.getChannel().getTotal() == 0){
                                                                        requireActivity().runOnUiThread(() -> {
                                                                                Toast.makeText(getActivity(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                                                                                // progressDialog.dismiss();
                                                                        });
                                                                }
                                                                else{
                                                                        imageList.addAll(listItem);
                                                                        HomeValue.getInstance().setSearchImageList(apiResult.getItems());
                                                                        requireActivity().runOnUiThread(() -> {
                                                                                // 데이터 처리 후의 추가 작업 수행
                                                                                listViewAdapter.notifyDataSetChanged();
                                                                                listView.setSelection(0);
                                                                        });
                                                                }
                                                        } else {
                                                                // 실패 처리
                                                        }
                                                }
                                                @Override
                                                public void onFailure(@NonNull Call<ApiResponseXML> call, @NonNull Throwable t) {
                                                        // 오류 처리
                                                }
                                        });
                                }
                        }
                });

                launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                        if (result.getResultCode() == RESULT_OK) {
                                Intent data = result.getData();
                                boolean updateListView = Objects.requireNonNull(data).getBooleanExtra("updateListView", false);
                                int currentIndex = data.getIntExtra("currentPosition", 0);

                                if (updateListView) {
                                        new Thread(() -> {
                                                imageList.clear();
                                                imageList.addAll(HomeValue.getInstance().getSearchImageList());
                                                SearchFragment.this.requireActivity().runOnUiThread(() -> {
                                                        // 데이터 처리 후의 추가 작업 수행
                                                        listViewAdapter.notifyDataSetChanged();
                                                        listView.setSelection(currentIndex);
                                                });
                                        }).start();
                                }
                        }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                Intent mainIntent = new Intent(getActivity(), ListActivity.class); // ListActivity로 넘길 intent 변수
                                mainIntent.putExtra("position", position); // 이미지 인덱스를 ListActivity로 넘길 intent
                                mainIntent.putExtra(Constants.KEWYWORD, searchText.getText().toString());
                                launcher.launch(mainIntent);
                        }
                });

                return v;
        }


        // 스크롤 이벤트
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (!scrollBottomCheck) {
                        HomeValue.getInstance().setTag(true);
                }

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        if (lastVisibleItemCheck && !loading) {
                                loading = true;
                                searchButton.setEnabled(false);

                                start = imageList.size(); // start지점 및 listLink, titleLink에 넣을 인덱스 추가 값

                                // 더이상 검색 결과가 없을 때 "더이상 데이터가 없습니다." toast메세지 출력
                                if ((HomeValue.getInstance().getResultCount() == start) && HomeValue.getInstance().getTag()) {
                                        Toast.makeText(getActivity(), "더이상 데이터가 없습니다.", Toast.LENGTH_SHORT).show();
                                        HomeValue.getInstance().setTag(false);
                                } else {
                                        if (Constants.USE_JSON_DATA) {
                                                ApiSearchImage.getInstance().searchImagesJSON(HomeValue.getInstance().getKeyword(), 20, start+1, new Callback<ApiResponseJSON>() {
                                                        @Override
                                                        public void onResponse(@NonNull Call<ApiResponseJSON> call, @NonNull Response<ApiResponseJSON> response) {
                                                                if (response.isSuccessful()) {
                                                                        ApiResponseJSON apiResult = response.body();

                                                                        List<ListItem> listItem = apiResult.getItems();

                                                                        imageList.addAll(listItem);
                                                                        HomeValue.getInstance().setSearchImageList(imageList);
                                                                        requireActivity().runOnUiThread(() -> {
                                                                                // 데이터 처리 후의 추가 작업 수행
                                                                                listViewAdapter.notifyDataSetChanged();
                                                                                loading = false;
                                                                        });
                                                                } else {

                                                                }
                                                        }
                                                        @Override
                                                        public void onFailure(@NonNull Call<ApiResponseJSON> call, @NonNull Throwable t) {
                                                                // 오류 처리
                                                        }
                                                });
                                        } else {
                                                ApiSearchImage.getInstance().searchImagesXML(HomeValue.getInstance().getKeyword(), 20, start+1, new Callback<ApiResponseXML>() {
                                                        @Override
                                                        public void onResponse(@NonNull Call<ApiResponseXML> call, @NonNull Response<ApiResponseXML> response) {
                                                                if (response.isSuccessful()) {
                                                                        ApiResponseXML apiResult = response.body();

                                                                        List<ListItem> listItem = apiResult.getItems();
                                                                        imageList.addAll(listItem);

                                                                        requireActivity().runOnUiThread(() -> {
                                                                                // 데이터 처리 후의 추가 작업 수행
                                                                                listViewAdapter.notifyDataSetChanged();
                                                                                loading = false;
                                                                        });
                                                                } else {
                                                                        // 실패 처리
                                                                }
                                                        }
                                                        @Override
                                                        public void onFailure(@NonNull Call<ApiResponseXML> call, @NonNull Throwable t) {
                                                                // 오류 처리
                                                        }
                                                });
                                        }
                                }
                                searchButton.setEnabled(true);
                        }
                }
        }

        // 스크롤 상태
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                scrollBottomCheck = (totalItemCount > 0) && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleItemCount);

                lastVisibleItemIndex = firstVisibleItem + visibleItemCount - 1;
                lastVisibleItemCheck = (totalItemCount > 0) && (lastVisibleItemIndex == totalItemCount - 1);
        }
}