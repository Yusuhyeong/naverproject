package com.example.naverproject.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.naverproject.R;
import com.example.naverproject.utils.ListItem;

import java.util.List;

// 자식 어답터
public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder> {

    private List<ListItem> subItemList;

    SubItemAdapter(List<ListItem> subItemList) {
        this.subItemList = subItemList;
    }

    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_sub_item_home, viewGroup, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubItemViewHolder subItemViewHolder, int i) {
        if(subItemViewHolder instanceof  SubItemViewHolder){
            SubItemViewHolder subHolder = (SubItemViewHolder) subItemViewHolder;
            subHolder.onBind(subItemList.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return subItemList.size();
    }

    class SubItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubItemTitle;
        ImageView tvSubItemLink;
        private LinearLayout sblayout;
        ListItem subitem;

        SubItemViewHolder(View itemView) {
            super(itemView);
            tvSubItemTitle = itemView.findViewById(R.id.tv_sub_item_title);
            tvSubItemLink = itemView.findViewById(R.id.img_sub_item);
            sblayout = itemView.findViewById(R.id.sb_layout);
        }

        public void onBind(ListItem subitem){
            this.subitem = subitem;

            tvSubItemTitle.setText(subitem.getTitle());
            Glide.with(this.sblayout).load(subitem.getLink()).placeholder(R.drawable.loading).error(R.drawable.error).into(tvSubItemLink);
        }
    }
}