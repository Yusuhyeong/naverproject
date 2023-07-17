package com.example.naverproject.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.naverproject.R;
import com.example.naverproject.gridview.DetailActivity;

import java.util.List;

// 상위 어답터
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<Item> itemList;
    Context context;

    public ItemAdapter(Context context, List<Item> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_home, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        Item item = itemList.get(i);
        itemViewHolder.tvItemTitle.setText(item.getItemTitle());

        // 자식 레이아웃 매니저 설정
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                itemViewHolder.rvSubItem.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(item.getSubItemList().size());

        // 자식 어답터 설정
        SubItemAdapter subItemAdapter = new SubItemAdapter(item.getSubItemList());

        itemViewHolder.rvSubItem.setLayoutManager(layoutManager);
        itemViewHolder.rvSubItem.setAdapter(subItemAdapter);
        itemViewHolder.rvSubItem.setRecycledViewPool(viewPool);

        itemViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = itemViewHolder.getAdapterPosition();
                if(index != RecyclerView.NO_POSITION){
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("keyword", itemList.get(index).getItemTitle());
                    context.startActivity(intent);
                }
            }
        });

        /*itemViewHolder.moreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = itemViewHolder.getAdapterPosition();
                if(index != RecyclerView.NO_POSITION){
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("keyword", itemList.get(index).getItemTitle());
                    context.startActivity(intent);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemTitle;
        private RecyclerView rvSubItem;
        private LinearLayout linearLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            // 부모 타이틀
            tvItemTitle = itemView.findViewById(R.id.tv_item_title);
            // 자식아이템 영역
            rvSubItem = itemView.findViewById(R.id.rv_sub_item);
            linearLayout = itemView.findViewById(R.id.moreLayout);
        }
    }
}