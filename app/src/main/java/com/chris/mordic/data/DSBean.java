package com.chris.mordic.data;

import java.util.List;

/**
 * Created by chris on 16-6-7.
 * Email: soymantag@163.coom
 */
/**daily sentence*/
public class DSBean implements Comparable<DSBean> {
    public String sid;
    public String tts;
    public String content;
    public String note;
    public String love;
    public String translation;
    public String picture;
    public String picture2;
    public String caption;
    public String dateline;
    public String s_pv;
    public String sp_pv;
    public String fenxiang_img;
    public List<TagBean> tags;

    @Override
    public int compareTo(DSBean another) {
        if(Integer.parseInt(sid)>Integer.parseInt(another.sid))
        return -1;
        return 1;
    }


    public class TagBean {
        public int id;
        public String name;
    }


}



