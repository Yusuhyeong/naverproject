/*
 * HomeFragment
 * 2023.06.26
 */

package com.example.naverproject;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.naverproject.ViewPager.ListActivity;
import com.example.naverproject.api.ApiResponseJSON;
import com.example.naverproject.api.ApiResponseXML;
import com.example.naverproject.api.ApiSearchImage;
import com.example.naverproject.fragment.MainActivity;
import com.example.naverproject.gridview.DetailActivity;
import com.example.naverproject.utils.Constants;
import com.example.naverproject.utils.HomeValue;
import com.example.naverproject.utils.ListItem;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{
        private static final String CHANNEL_ID = "my_channel_id";
        private static final int NOTIFICATION_ID = 1;

        @SuppressLint("MissingPermission")
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
                String title = remoteMessage.getNotification().getTitle();
                String message = remoteMessage.getNotification().getBody();

                Map<String, String> data = remoteMessage.getData();
                String action = data.get(Constants.ACTION);


                // 알림 생성
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true);


                switch (action) {
                        case Constants.HOME:
                        case Constants.SEARCH:
                        case Constants.SETTING: {
                                Intent intent = new Intent(this, MainActivity.class);
                                intent.putExtra(Constants.CHECK_FRAGMENT, action);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                builder.setContentIntent(pendingIntent);
                                break;
                        }
                        case Constants.HOME_DETAIL: {
                                Intent intent = new Intent(this, DetailActivity.class);
                                String keyword = data.get(Constants.KEWYWORD);

                                intent.putExtra(Constants.KEWYWORD, keyword);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                builder.setContentIntent(pendingIntent);
                                break;
                        }
                        case Constants.LIST_DETAIL: {
                                Intent intent = new Intent(this, ListActivity.class);
                                String keyword = data.get(Constants.KEWYWORD);

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
                                                        }
                                                }

                                                @Override
                                                public void onFailure(@NonNull Call<ApiResponseJSON> call, @NonNull Throwable t) {
                                                        // 오류 처리
                                                }
                                        });
                                } else {
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

                                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                                builder.setContentIntent(pendingIntent);
                                break;
                        }
                }
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
        }
}