package com.chris.mordic.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.chris.mordic.R;
import com.chris.mordic.base.BaseFragment;
import com.chris.mordic.base.TabFragment;
import com.chris.mordic.conf.Constants;
import com.chris.mordic.factory.FragmentFactory;
import com.chris.mordic.ui.ChangeColorIconWithTextView;
import com.chris.mordic.utils.LogUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Email: soymantag@163.coom
 */
public class MainActivity extends FragmentActivity {

    private ViewPager mVp_main;
    private List<Fragment> mTabs = new ArrayList<>();
    private String[] mTitles = new String[]{"First Fragment!",
            "Second Fragment!", "Third Fragment!", "Fourth Fragment!"};
    private List<ChangeColorIconWithTextView> mTabIndicator = new ArrayList<>();
    private ChangeColorIconWithTextView mTab_one;
    private ChangeColorIconWithTextView mTab_two;
    private ChangeColorIconWithTextView mTab_three;
    private ChangeColorIconWithTextView mTab_four;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initView();
        initDate();
        initEvent();
    }

    private void initView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
/*            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);*/
        }
        setContentView(R.layout.activity_main);
        mVp_main = (ViewPager) findViewById(R.id.vp_home);
        mTab_one = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_one);
        mTab_two = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_two);
        mTab_three = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_three);
        mTab_four = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_four);

        mTabIndicator.add(mTab_one);
        mTabIndicator.add(mTab_two);
        mTabIndicator.add(mTab_three);
        mTabIndicator.add(mTab_four);

    }

    private void initDate() {
        for (String title : mTitles) {
            TabFragment tabFragment = new TabFragment();
            Bundle args = new Bundle();
            args.putString("title", title);
            tabFragment.setArguments(args);
            mTabs.add(tabFragment);
        }
        MyFragmentStatePagerAdapter mAdapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager());
        mVp_main.setAdapter(mAdapter);

    }

    private void getJsonFromNet(String word) {
        HttpUtils httpUtils = new HttpUtils();
        String url = Constants.URLS.BASEURL;
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("w", word);
        params.addQueryStringParameter("key", Constants.URLS.JINSHANKEY);
        params.addQueryStringParameter("type", "json");
        httpUtils.send(HttpRequest.HttpMethod.GET, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String wordData = responseInfo.result;
                LogUtils.v(wordData);
                //存入数据库


            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(getApplicationContext(), "获取单词数据失败", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initEvent() {
        View.OnClickListener tab_onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                resetOtherTabs();
                switch (v.getId()) {
                    case R.id.id_indicator_one:
                        mTabIndicator.get(0).setIconAlpha(1.0f);
                        mVp_main.setCurrentItem(0, false);
                        break;
                    case R.id.id_indicator_two:
                        mTabIndicator.get(1).setIconAlpha(1.0f);
                        mVp_main.setCurrentItem(1, false);
                        break;
                    case R.id.id_indicator_three:
                        mTabIndicator.get(2).setIconAlpha(1.0f);
                        mVp_main.setCurrentItem(2, false);
                        break;
                    case R.id.id_indicator_four:
                        mTabIndicator.get(3).setIconAlpha(1.0f);
                        mVp_main.setCurrentItem(3, false);
                        break;

                }
            }
        };

        mTab_one.setOnClickListener(tab_onClickListener);
        mTab_two.setOnClickListener(tab_onClickListener);
        mTab_three.setOnClickListener(tab_onClickListener);
        mTab_four.setOnClickListener(tab_onClickListener);


        final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (positionOffset > 0) {
                    ChangeColorIconWithTextView left = mTabIndicator.get(position);
                    ChangeColorIconWithTextView right = mTabIndicator.get(position + 1);

                    left.setIconAlpha(1 - positionOffset);
                    right.setIconAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = FragmentFactory.getFragment(position);
                if (fragment != null && position==0) {
                    ((BaseFragment)fragment).getLoadingPager().loadData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mVp_main.addOnPageChangeListener(onPageChangeListener);
        mTab_one.setIconAlpha(1.0f);
        mVp_main.setCurrentItem(0);
        /**************************注意此句，在view初始化后触发选择首页*********************/
        mVp_main.post(new Runnable(){
            @Override
            public void run() {
                onPageChangeListener.onPageSelected(mVp_main.getCurrentItem());
            }
        });
    }

    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicator.size(); i++) {
            mTabIndicator.get(i).setIconAlpha(0);
        }
    }

    class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter {


        public MyFragmentStatePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            Fragment fragment = FragmentFactory.getFragment(position);
            return fragment;
        }

        /**
         * Return the number of views available.
         */
        @Override
        public int getCount() {
            if (mTitles != null)
                return mTitles.length;
            return 0;
        }
    }

}
