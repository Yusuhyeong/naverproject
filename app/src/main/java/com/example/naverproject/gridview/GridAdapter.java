package com.example.naverproject.gridview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.naverproject.R;
import com.example.naverproject.utils.ListItem;

import java.util.List;

public class GridAdapter extends BaseAdapter {
        private Context context;
        private List<ListItem> items;

        public GridAdapter(Context context, List<ListItem> items) {
                this.context = context;
                this.items = items;
        }

        @Override
        public int getCount() {
                return items.size();
        }

        @Override
        public Object getItem(int position) {
                return items.get(position);
        }

        @Override
        public long getItemId(int position) {
                return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder;

                if (convertView == null) {
                        // LayoutInflater를 사용하여 grid_item.xml 파일을 inflate
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflater.inflate(R.layout.grid_item, parent, false);

                        // ViewHolder를 생성하여 convertView에 저장
                        holder = new ViewHolder();
                        holder.titleTextView = convertView.findViewById(R.id.titleTextView);
                        holder.linkTextView = convertView.findViewById(R.id.linkTextView);
                        convertView.setTag(holder);
                } else {
                        // convertView가 재사용되는 경우 ViewHolder를 가져옴
                        holder = (ViewHolder) convertView.getTag();
                }

                // 해당 position에 있는 GridItem 객체를 가져옴
                ListItem item = items.get(position);

                // round처리
                holder.linkTextView.setClipToOutline(true);

                // ViewHolder의 View들에 데이터 설정
                holder.titleTextView.setText(item.getTitle());
                Glide.with(context).load(item.getLink()).placeholder(R.drawable.loading).error(R.drawable.error).into(holder.linkTextView);

                return convertView;
        }

        // ViewHolder 클래스
        private static class ViewHolder {
                TextView titleTextView;
                ImageView linkTextView;
        }
}
