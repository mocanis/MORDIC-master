package com.chris.mordic.base;

import android.view.View;

/**
 * Created by chris on 16-6-1.
 * Email: soymantag@163.coom
 */
public abstract class BaseHolder<HOLDERBASENTYPE> {

    private final View mHolderView;
    private HOLDERBASENTYPE	mData;

    public BaseHolder() {
        mHolderView = initHolderView();
        mHolderView.setTag(this);
    }

    public View getHolderView(){
        return mHolderView;
    }

    public abstract View initHolderView();
    public void setDataAndRefreshHolderView(HOLDERBASENTYPE data){
        mData = data;
        refreshHolderView(data);
    }

    protected abstract void refreshHolderView(HOLDERBASENTYPE data);
}
