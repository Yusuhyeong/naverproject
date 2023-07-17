/*
 * DataPage
 * 2023.06.02
 */

package com.example.naverproject.ViewPager;

public class ViewPagerItem {
        private String link;
        private int position;
        private int count;

        public ViewPagerItem(String link, int position, int count) {
                this.link = link;
                this.position = position;
                this.count = count;
        }

        public String getLink() {
                return link;
        }

        public int getPosition() {
                return position;
        }

        public int getCount() {
                return count;
        }

        public void setLink(String link) {
                this.link = link;
        }

        public void setPosition(int position) {
                this.position = position;
        }
}
