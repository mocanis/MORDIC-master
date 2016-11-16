package com.chris.mordic.ui;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chris on 16-6-2.
 * Email: soymantag@163.coom
 */
public class ScrollToTopBehavior extends CoordinatorLayout.Behavior<View>{
    int offsetTotal = 0;
    boolean scrolling = false;

    public ScrollToTopBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return true;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        offset(child, dyConsumed);
    }

    public void offset(View child,int dy){
        int old = offsetTotal;
        int top = offsetTotal - dy;//child的顶部
        top = Math.max(top, -child.getHeight());
        top = Math.min(top, 0);
        offsetTotal = top;
        if (old == offsetTotal){
            scrolling = false;
            return;
        }
        int delta = offsetTotal-old;
        child.offsetTopAndBottom(delta);
        scrolling = true;
    }

}
