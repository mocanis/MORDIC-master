package com.chris.mordic.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chris.mordic.R;
import com.chris.mordic.base.BaseFragment;
import com.chris.mordic.base.BaseRecyclerAdapter;
import com.chris.mordic.base.LoadingPager;
import com.chris.mordic.data.DSBean;
import com.chris.mordic.data.GankPicBean;
import com.chris.mordic.protocol.DSProtocol;
import com.chris.mordic.protocol.GankPicProtocol;
import com.chris.mordic.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chris on 16-5-30.
 * Email: soymantag@163.coom
 */
public class HomeFragment extends BaseFragment {

    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.invis)
    LinearLayout invis;
    public static ArrayList<DSBean> mDailySectences = new ArrayList<>();
    //public static ArrayList<DSBean> mDailySectences ;
    public static String mToday = "2013-05-01";
    public static BaseRecyclerAdapter mAdapter;
    private static final String KEY_SELECTED_PAGE = "KEY_SELECTED_PAGE";
    public static List<DataBean> mStaggerDatas = new ArrayList<>();
    private List<DataBean> mDatas = new ArrayList<DataBean>();
    private int[] mListIcons = new int[]{R.mipmap.g1, R.mipmap.g2, R.mipmap.g3, R.mipmap.g4,
            R.mipmap.g5, R.mipmap.g6, R.mipmap.g7, R.mipmap.g8, R.mipmap.g9, R.mipmap.g10, R
            .mipmap.g11, R.mipmap.g12, R.mipmap.g13, R.mipmap.g14, R.mipmap.g15, R.mipmap
            .g16, R.mipmap.g17, R.mipmap.g18, R.mipmap.g19, R.mipmap.g20, R.mipmap.g21, R
            .mipmap.g22, R.mipmap.g23, R.mipmap.g24, R.mipmap.g25, R.mipmap.g26, R.mipmap
            .g27, R.mipmap.g28, R.mipmap.g29};
    public static int[] mStraggeredIcons = new int[]{R.mipmap.p1, R.mipmap.p2, R.mipmap.p3, R
            .mipmap.p4, R.mipmap.p5, R.mipmap.p6, R.mipmap.p7, R.mipmap.p8, R.mipmap.p9, R
            .mipmap.p10, R.mipmap.p11, R.mipmap.p12, R.mipmap.p13, R.mipmap.p14, R.mipmap
            .p15, R.mipmap.p16, R.mipmap.p17, R.mipmap.p18, R.mipmap.p19, R.mipmap.p20, R
            .mipmap.p21, R.mipmap.p22, R.mipmap.p23, R.mipmap.p24, R.mipmap.p25, R.mipmap
            .p26, R.mipmap.p27, R.mipmap.p28, R.mipmap.p29, R.mipmap.p30, R.mipmap.p31, R
            .mipmap.p32, R.mipmap.p33, R.mipmap.p34, R.mipmap.p35, R.mipmap.p36, R.mipmap
            .p37, R.mipmap.p38, R.mipmap.p39, R.mipmap.p40, R.mipmap.p41, R.mipmap.p42, R
            .mipmap.p43, R.mipmap.p44};


    private static List<String> mPicutures =new ArrayList<>();
    private static List<GankPicBean.Item> mGankPicBeans =new ArrayList<>();
    private View mLl_home;
    private Activity mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        if(mContext == null){
            System.out.println("********************************null***********************");
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static List<String> getPicutures() {
        return mPicutures;
    }
    public static List<GankPicBean.Item> getGankPics() {
        return mGankPicBeans;
    }

    public LoadingPager.LoadedResult initData(String date) {
        try {

            DSProtocol DSProtocol0 = new DSProtocol();
            DSBean dsBean0 = DSProtocol0.loadData(null);
            mToday=dsBean0.dateline;
/*            DSProtocol DSProtocol1 = new DSProtocol();
            DSBean dsBean1 = DSProtocol1.loadData(mToday);
            System.out.println("dsbean1:"+dsBean1.dateline);*/
            DSProtocol DSProtocol2 = new DSProtocol();
            DSBean dsBean2 = DSProtocol2.loadData(DateUtils.getpreDate(mToday,1));
            System.out.println("dsbean2:"+dsBean2.dateline);
            DSProtocol DSProtocol3 = new DSProtocol();
            DSBean dsBean3 = DSProtocol3.loadData(DateUtils.getpreDate(mToday,2));
            System.out.println("dsbean3:"+dsBean3.dateline);
            DSProtocol DSProtocol4 = new DSProtocol();
            DSBean dsBean4 = DSProtocol4.loadData(DateUtils.getpreDate(mToday,3));
            System.out.println("dsbean4:"+dsBean4.dateline);
            DSProtocol DSProtocol5 = new DSProtocol();
            DSBean dsBean5 = DSProtocol5.loadData(DateUtils.getpreDate(mToday,4));
            System.out.println("dsbean5:"+dsBean5.dateline);

            LoadingPager.LoadedResult state1 = checkState(dsBean0);
            LoadingPager.LoadedResult state2 = checkState(dsBean2);
            LoadingPager.LoadedResult state3 = checkState(dsBean3);
            LoadingPager.LoadedResult state4 = checkState(dsBean4);
            LoadingPager.LoadedResult state5 = checkState(dsBean5);

            GankPicProtocol gankPicProtocol = new GankPicProtocol();
            GankPicBean gankPicBean = gankPicProtocol.loadData("1");
            LoadingPager.LoadedResult gankPicState = checkState(gankPicBean);
            if (state1 != LoadingPager.LoadedResult.SUCCESS
                    || state2 != LoadingPager.LoadedResult.SUCCESS
                    || state3 != LoadingPager.LoadedResult.SUCCESS
                    || state4 != LoadingPager.LoadedResult.SUCCESS
                    || state5 != LoadingPager.LoadedResult.SUCCESS
                    || gankPicState != LoadingPager.LoadedResult.SUCCESS
                    ) {// 如果不成功,就直接返回,走到这里说明homeBean是ok
                System.out.println("获取图片失败");
                return LoadingPager.LoadedResult.ERROR;
            }

            mPicutures.add(dsBean0.picture2);///*picture的URL
            mPicutures.add(dsBean2.picture2);///*picture的URL
            mPicutures.add(dsBean3.picture2);///*picture的URL
            mPicutures.add(dsBean4.picture2);///*picture的URL
            mPicutures.add(dsBean5.picture2);///*picture的URL


                mGankPicBeans.addAll(gankPicBean.results);

            return LoadingPager.LoadedResult.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadedResult.ERROR;
        }

        /*for (int i = 0; i < mStraggeredIcons.length; i++) {
            int iconId = mStraggeredIcons[i];
            DataBean dataBean = new DataBean();
            dataBean.iconId = iconId;
            dataBean.text = "我是item" + i;
            mStaggerDatas.add(dataBean);
        }
        for (int i = 0; i < mListIcons.length; i++) {
            int iconId = mListIcons[i];
            DataBean dataBean = new DataBean();
            dataBean.iconId = iconId;
            dataBean.text = "我是item" + i;
            mDatas.add(dataBean);
        }
        getDailySentences(Constants.URLS.DSURL, "", this.getActivity());

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(20000);
                if (mDailySectences != null) {
                    System.out.println(mDailySectences.size());
                    for (int i = 0; i < mDailySectences.size(); i++) {
                        System.out.println(mDailySectences.get(i).dateline);
                    }
                } else {
                    System.out.println("mDailySectences is null");
                }
            }
        }).start();*/
    }

    /**
     * @des 返回成功视图, 但是不知道具体实现, 所以定义成抽象方法
     * @des 它是LoadingPager同名方法
     * @call 正在加载数据完成之后, 并且数据加载成功, 我们必须告知具体的成功视图
     */
    @Override
    public View initSuccessView() {

        mLl_home = View.inflate(mContext, R.layout.fragment_home, null);
        mRecyclerView = (RecyclerView) mLl_home.findViewById(R.id.recyclerView);
        ButterKnife.inject(mLl_home);
        for (int i = 0; i < mStraggeredIcons.length; i++) {
            int iconId = mStraggeredIcons[i];
            DataBean dataBean = new DataBean();
            dataBean.iconId = iconId;
            dataBean.text = "我是item" + i;
            mStaggerDatas.add(dataBean);
        }
        for (int i = 0; i < mListIcons.length; i++) {
            int iconId = mListIcons[i];
            DataBean dataBean = new DataBean();
            dataBean.iconId = iconId;
            dataBean.text = "我是item" + i;
            mDatas.add(dataBean);
        }
        initStaggeredGridAdapterV();
        return mLl_home;
    }

    public static void refreshViewPager() {
        mAdapter.notifyDataSetChanged();
    }

    private void initStaggeredGridAdapterV() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,2);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //MyStaggeredAdapter myStaggeredAdapter = new MyStaggeredAdapter(this.getActivity(),mStaggerDatas);
        mAdapter = new BaseRecyclerAdapter(mContext, mGankPicBeans);
        mRecyclerView.setAdapter(mAdapter);

        //mRecyclerView.setAdapter(myStaggeredAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                System.out.println("dx:"+dx);
                System.out.println("dy:"+dy);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            BaseRecyclerAdapter.mSelectedPage = savedInstanceState.getInt(KEY_SELECTED_PAGE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt(KEY_SELECTED_PAGE, mAdapter.getCurrentPosition());
    }

    public class DataBean {
        public String text;
        public int iconId;
    }
}
