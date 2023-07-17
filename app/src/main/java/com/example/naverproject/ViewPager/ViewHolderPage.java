/*
 * ViewHolderPage
 * 2023.06.02
 */

package com.example.naverproject.ViewPager;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.naverproject.R;

public class ViewHolderPage extends RecyclerView.ViewHolder {

        private ImageView imageLink; // 이미지를 띄울 변수
        private RelativeLayout rlLayout; // 이미지를 띄울 layout
        private TextView viewIndex;

        ViewPagerItem data; // ViewPager2에서 사용될 arraylist 전달 DataPage

        ViewHolderPage(View itemView) {
                super(itemView);

                viewIndex = itemView.findViewById(R.id.view_index);
                imageLink = itemView.findViewById(R.id.image_link); // ImageView
                rlLayout = itemView.findViewById(R.id.rl_layout); // ViewPager2에 사용되는 imageview의 layout id
        }

        public void onBind(ViewPagerItem data) {
                this.data = data;

                viewIndex.setText(String.valueOf(String.valueOf(data.getPosition() + 1) + " / " + data.getCount()));
                Glide.with(this.rlLayout).load(data.getLink()).placeholder(R.drawable.loading2).error(R.drawable.error2).into(imageLink); // ViewPager2상에서 ImageView띄우기
        }
}