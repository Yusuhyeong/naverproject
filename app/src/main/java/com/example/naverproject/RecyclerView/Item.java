package com.example.naverproject.RecyclerView;

import com.example.naverproject.utils.ListItem;

import java.util.List;

public class Item {
    private String itemTitle;
    // 하위 리사이클러뷰 아이템으로 정의한 subItemList를 전역변수로 선언한다.
    private List<ListItem> subItemList;

    public Item(String itemTitle, List<ListItem> subItemList) {
        this.itemTitle = itemTitle;
        // 하위 리사이클러뷰
        this.subItemList = subItemList;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public List<ListItem> getSubItemList() {
        return subItemList;
    }

    public void setSubItemList(List<ListItem> subItemList) {
        this.subItemList = subItemList;
    }
}