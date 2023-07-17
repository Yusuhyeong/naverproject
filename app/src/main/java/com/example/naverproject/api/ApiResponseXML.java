/*
 * ApiResponseXML
 * 2023.06.02
 */

package com.example.naverproject.api;

import com.example.naverproject.utils.ListItem;
import com.tickaroo.tikxml.annotation.Attribute;
import com.tickaroo.tikxml.annotation.Element;
import com.tickaroo.tikxml.annotation.PropertyElement;
import com.tickaroo.tikxml.annotation.Xml;

import java.util.ArrayList;
import java.util.List;

@Xml(name = "rss")
public class ApiResponseXML {
        @Attribute(name = "version")
        private String version;
        @Element(name = "channel")
        private Channel channel;

        public String getVersion() {
                return version;
        }

        public void setVersion(String version) {
                this.version = version;
        }

        public Channel getChannel() {
                return channel;
        }
        public void setChannel(Channel channel) {
                this.channel = channel;
        }

        public List<ListItem> getItems() {
                List<ListItem> itemList = new ArrayList<>();
                if (channel != null && channel.getItems() != null) {
                        List<Item> items = channel.getItems();
                        for (int i = 0; i < items.size(); i++) {
                                Item item = items.get(i);
                                itemList.add(item.toListItem());
                        }
                }
                return itemList;
        }

        @Xml(name = "channel")
        public static class Channel {
                @PropertyElement(name = "total")
                private int total;

                @Element(name = "item")
                private List<Item> items;

                public int getTotal() {
                        return total;
                }

                public List<Item> getItems() {
                        return items;
                }

                public void setTotal(int total) {
                        this.total = total;
                }

                public void setItems(List<Item> items) {
                        this.items = items;
                }
        }

        @Xml(name = "item")
        public static class Item {

                @PropertyElement(name = "title")
                private String title;

                @PropertyElement(name = "link")
                private String link;

                public String getTitle() {
                        return title;
                }

                public String getLink() {
                        return link;
                }

                public void setTitle(String title) {
                        this.title = title;
                }

                public void setLink(String link) {
                        this.link = link;
                }

                public ListItem toListItem() {
                        return new ListItem(title, link);
                }
        }
}
