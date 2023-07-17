/*
 * ApiResponseJSON
 * 2023.06.02
 */

package com.example.naverproject.api;

import com.example.naverproject.utils.ListItem;

import java.util.List;

public class ApiResponseJSON {
        private int total;
        private List<ListItem> items;

        public int getTotal() {
                return total;
        }

        public void setTotal(int total) {
                this.total = total;
        }

        public List<ListItem> getItems() {
                return items;
        }

        public void setItems(List<ListItem> items) {
                this.items = items;
        }
}