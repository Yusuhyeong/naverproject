/*
 * HomeValue
 * 2023.06.02
 */

package com.example.naverproject.utils;

import java.util.ArrayList;
import java.util.List;

public class HomeValue{
        private Boolean Tag;
        private List<ListItem> searchImageList;
        private List<Object> searchLinkList;
        private List<Object> searchTitleList;
        private int resultCount;
        private String keyword;
        private int uiOptions;


        private HomeValue() {
                // 초기화
                searchImageList = new ArrayList<>();
                searchLinkList = new ArrayList<Object>();
                searchTitleList = new ArrayList<Object>();
                resultCount = 0;
                keyword = "";
                Tag = true;
                uiOptions = 0;
        }

        private static class LazyHolder {
                private static final HomeValue INSTANCE = new HomeValue();
        }

        public static HomeValue getInstance() {
                return LazyHolder.INSTANCE;
        }

        public Boolean getTag() {
                return Tag;
        }

        public List<ListItem> getSearchImageList() {
                return searchImageList;
        }

        public List<Object> getSearchLinkList() {
                return searchLinkList;
        }

        public List<Object> getSearchTitleList() {
                return searchTitleList;
        }

        public int getResultCount() {
                return resultCount;
        }

        public String getKeyword() {
                return keyword;
        }
        public int getUiOptions() { return uiOptions; }

        public void setTag(Boolean tag) {
                Tag = tag;
        }

        public void setSearchImageList(List<ListItem> searchImageList) {
                this.searchImageList = searchImageList;
        }

        public void setSearchLinkList(List<Object> searchLinkList) {
                this.searchLinkList = searchLinkList;
        }

        public void setSearchTitleList(List<Object> searchTitleList) {
                this.searchTitleList = searchTitleList;
        }

        public void setResultCount(int resultCount) {
                this.resultCount = resultCount;
        }

        public void setKeyword(String keyword) {
                this.keyword = keyword;
        }

        public void setUiOptions(int uiOptions) {
                this.uiOptions = uiOptions;
        }
}