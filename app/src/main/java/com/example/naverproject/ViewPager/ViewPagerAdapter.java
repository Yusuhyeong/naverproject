/*
 * ViewPagerAdapter
 * 2023.06.02
 */

package com.example.naverproject.ViewPager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.naverproject.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewHolderPage> {

        private ArrayList<ViewPagerItem> listData;

        public ViewPagerAdapter(ArrayList<ViewPagerItem> data) {
                this.listData = data;
        }

        @Override
        public ViewHolderPage onCreateViewHolder(ViewGroup parent, int viewType) {
                Context context = parent.getContext();
                View view = LayoutInflater.from(context).inflate(R.layout.item_viewpager, parent, false);
                return new ViewHolderPage(view);
        }

        @Override
        public void onBindViewHolder(ViewHolderPage holder, int position) {
                if (holder instanceof ViewHolderPage) {
                        ViewHolderPage viewHolder = (ViewHolderPage) holder;
                        viewHolder.onBind(listData.get(position));
                }
        }

        @Override
        public int getItemCount() {
                return listData.size();
        }
}