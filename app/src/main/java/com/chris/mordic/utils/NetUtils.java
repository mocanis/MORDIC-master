package com.chris.mordic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.chris.mordic.base.BaseRecyclerAdapter;
import com.chris.mordic.conf.Constants;
import com.chris.mordic.data.DSBean;
import com.chris.mordic.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.Collections;

import static com.chris.mordic.utils.DateUtils.initDate;

/**
 * Created by chris on 16-6-7.
 * Email: soymantag@163.coom
 */
public class NetUtils {

    public static void getDailySentences(String url, final String date, final Context context){

        if(date != null)
            url = url+"?date="+date;
        System.out.println("url:"+url);
        GsonRequest<DSBean> gsonRequest = new GsonRequest<DSBean>(Request.Method.GET, url,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error:" + error.getMessage());
                    }
                }, new Response.Listener<DSBean>() {
            @Override
            public void onResponse(DSBean response) {
                if(date != ""){
                    HomeFragment.mDailySectences.add(1,response);//从当前日期后面插入
                }else {
                    //判断并发送请求每日一句的json
                    if(!HomeFragment.mToday.equals(response.dateline)){
                        if(HomeFragment.mDailySectences.size()>0)
                        HomeFragment.mDailySectences.set(0,response);
                        else HomeFragment.mDailySectences.add(response);
                        ArrayList<String> offsetDates= initDate(response.dateline);//算出当前日期之前的4天
                        HomeFragment.mToday = response.dateline;
                        System.out.println("offsetDates.size:"+offsetDates.size());
                        int size1 =offsetDates.size();
                        int size2 =HomeFragment.mDailySectences.size();
                        //删除超过5天的数据
                        for(int i=0;i<size1;i++){
                            if(size2>=size1+1)
                            HomeFragment.mDailySectences.remove(size2-1-i);
                        }
                        for (int i=0;i<size1;i++){
                            getDailySentences(Constants.URLS.DSURL,offsetDates.get(i),context);
                        }
                    }
                    if(HomeFragment.mDailySectences.size()>=5){
                        Collections.sort(HomeFragment.mDailySectences);
      /*                  ArrayList<DSBean> temp = new ArrayList<>();
                        temp.addAll(HomeFragment.mDailySectences);
                        HomeFragment.mDailySectences.clear();
                        HomeFragment.mDailySectences.addAll(temp);*/
                        BaseRecyclerAdapter.mVp_Adapter.notifyDataSetChanged();
                        HomeFragment.mAdapter.notifyDataSetChanged();


                    }
                }
            }
        },DSBean.class);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(gsonRequest);
    }

    public static void initImageLoader(String url,Context context,final ImageView imageView) {

                ImageLoader imageLoader = VolleyTools.getInstance(context).getImageLoader();
                imageLoader.get(url, new ImageLoader.ImageListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error:" + error.getMessage());

                    }

                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        final Bitmap bitmap = response.getBitmap();
                        //不能直接更新UI
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });

                    }
                }, 500, 500);//宽高为200*100

            }


}
