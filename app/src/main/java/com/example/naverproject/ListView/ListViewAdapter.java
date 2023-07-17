/*
 * ListViewAdapter
 * 2023.06.02
 */

package com.example.naverproject.ListView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.naverproject.R;
import com.example.naverproject.utils.ListItem;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
        private ArrayList<ListItem> list;
        private Context context;

        public ListViewAdapter(Context context, ArrayList<ListItem> list) {
                this.context = context;
                this.list = list;
        }

        @Override
        public int getCount() {
                return list.size();
        }

        @Override
        public ListItem getItem(int position) {
                return list.get(position);
        }

        @Override
        public long getItemId(int position) {
                return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                @SuppressLint("ViewHolder")
                View view = LayoutInflater.from(context).inflate(R.layout.list_view, null);

                TextView imageIndex = view.findViewById(R.id.image_index);
                imageIndex.setText(String.valueOf(position + 1)); // 기본 방식
                ImageView iv = view.findViewById(R.id.imageview);

                iv.setClipToOutline(true);


                if (getCount() != 0) {
                        Glide.with(view).load(list.get(position).getLink()).placeholder(R.drawable.loading).error(R.drawable.error).into(iv);
                        TextView tv = view.findViewById(R.id.textview);
                        tv.setText(list.get(position).getTitle());
                } else if (getCount() == 0) {
                        new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                        }, 400);
                }

                return view;
        }
}