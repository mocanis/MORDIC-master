package com.chris.mordic.data;

/**
 * Created by chris on 16-6-7.
 * Email: soymantag@163.coom
 */

import java.util.List;

/**
 * daily sentence
 */
public class GankPicBean {

    public boolean error;

    public List<Item> results ;

    public class Item {
        public String _id;
        public String createdAt;
        public String desc;
        public String publishedAt;
        public String source;
        public String type;
        public String url;
        public String who;
        public boolean used;
    }
}



