package com.chris.mordic.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by chris on 16-6-23.
 * Email: soymantag@163.coom
 */
public class MyViewPager extends ViewPager {
    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * {@inheritDoc}
     *
     * @param ev
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(getParent()!=null)
         getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
