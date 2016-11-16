package com.chris.mordic.holder;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.chris.mordic.R;
import com.chris.mordic.base.BaseHolder;
import com.chris.mordic.utils.UIUtils;

/**
 * Created by chris on 16-5-31.
 * Email: soymantag@163.coom
 */
public class DailyIamgeHolder extends BaseHolder{

    private ViewPager mVp_home;

    public DailyIamgeHolder() {
    }

    @Override
    public View initHolderView() {
        View view = View.inflate(UIUtils.getContext(),R.layout.daily_iamge,null);
        mVp_home = (ViewPager) view.findViewById(R.id.vp_home);
        return mVp_home;
    }

    @Override
    protected void refreshHolderView(Object data) {

    }
}
