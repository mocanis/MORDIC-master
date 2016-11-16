package com.chris.mordic.data;

/**
 * Created by chris on 16-6-7.
 * Email: soymantag@163.coom
 */

import java.util.List;

/**
 * daily sentence
 */
public class GankBean {
    List<String> category;
    boolean error;

    class results {
        List<Item> Android;
        List<Item> IOS;
        List<Item> rest;
        List<Item> expand;
        List<Item> welfare;
    }

    class Item {
        String _id;
        String createdAt;
        String desc;
        String publishedAt;
        String type;
        String url;
        String who;
        boolean used;
    }
}



