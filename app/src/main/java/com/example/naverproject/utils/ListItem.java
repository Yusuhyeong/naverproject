/*
 * ListItem
 * 2023.06.02
 */

package com.example.naverproject.utils;

import java.io.Serializable;

public class ListItem implements Serializable {
        private String title;
        private String link;

        public ListItem(String title, String link) {
                this.title = title;
                this.link = link;
        }

        public String getTitle() {
                return this.title;
        }

        public String getLink() {
                return this.link;
        }
}
