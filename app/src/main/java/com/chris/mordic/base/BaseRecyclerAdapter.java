package com.chris.mordic.base;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chris.mordic.R;
import com.chris.mordic.conf.Constants;
import com.chris.mordic.data.GankPicBean;
import com.chris.mordic.factory.ThreadPoolFactory;
import com.chris.mordic.fragment.HomeFragment;
import com.chris.mordic.protocol.GankPicProtocol;
import com.chris.mordic.ui.MyViewPager;
import com.chris.mordic.utils.BitmapCacheUtils;
import com.chris.mordic.utils.LogUtils;
import com.chris.mordic.utils.UIUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 16-6-5.
 * Email: soymantag@163.coom
 */
public class BaseRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_FOOTER = 2;
    public static int mSelectedPage = 0;
    private List<GankPicBean.Item> mDatas = new ArrayList<>();
    private Activity mContext;
    private int mHeaderCount = 1;
    private int mFooterCount = 1;
    private OnItemClickListener mListener;
    private LoadMoreTask mLoadMoreTask;
    private Footer mLoadMoreHolder;

    private int[] mListIcons = new int[]{R.mipmap.g1, R.mipmap.g2, R.mipmap.g3, R.mipmap.g4,
            R.mipmap.g5, R.mipmap.g6, R.mipmap.g7};
    public static PagerAdapter mVp_Adapter;
    private BitmapCacheUtils bitmapCacheUtils;
    private AutoScrollTask mAutoScrollTask;

    public BaseRecyclerAdapter(Activity context, List<GankPicBean.Item> data) {
        mContext = context;
        mDatas = data;
        bitmapCacheUtils = new BitmapCacheUtils(context);
    }

    public void setOnItemClickListener(OnItemClickListener li) {
        mListener = li;
    }


    public void addDatas(List<GankPicBean.Item> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public int getCurrentPosition() {
        return mSelectedPage;
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderCount == 0 && mFooterCount == 0) {
            return TYPE_NORMAL;
        }
        if (position == 0 && mHeaderCount != 0) {
            return TYPE_HEADER;
        }
        if (position + 1 == getItemCount() && mFooterCount != 0)
            return TYPE_FOOTER;
        return TYPE_NORMAL;
    }

    /**
     * @return
     * @des 决定有没有加载更多, 默认是返回true, 但是子类可以覆写此方法, 如果子类返回的是flase, 就不会去加载更多
     * @call getView中滑动底的时候会调用
     */
    private boolean hasLoadMore() {
        return true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        if (viewType == TYPE_HEADER && mHeaderCount != 0) {
            return onCreateHeader(parent, viewType);
        }
        if (viewType == TYPE_FOOTER && mFooterCount != 0) {

            if (mLoadMoreHolder == null) {
                mLoadMoreHolder = (Footer) onCreateFooter(parent, Footer.STATE_LOADING);
            }

            return mLoadMoreHolder;
        }
        return onCreateNormalHolder(viewType);
    }

    /**
     * @des 滑到底之后, 应该去拉取更多的数据
     * @call 滑到底的时候
     */
    private void perFormLoadMore() {
        if (mLoadMoreTask == null) {// 1次
            // 修改loadmore当前的视图为加载中
            int state = Footer.STATE_LOADING;
            setDataAndRefreshFooter(mLoadMoreHolder, state);
            LogUtils.sf("###开启线程,加载更多");
            mLoadMoreTask = new LoadMoreTask();
            ThreadPoolFactory.getNormalPool().execute(mLoadMoreTask);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            setDataAndRefreshHeader(viewHolder, position);
            return;
        }
        if (getItemViewType(position) == TYPE_FOOTER) {
            setDataAndRefreshFooter(viewHolder, position);
            /*=============== 自动加载更多 ===============*/
            // 去开始加载更多
            if (hasLoadMore()) {//本应用设为恒true
                perFormLoadMore();
            } else {
                setDataAndRefreshFooter(mLoadMoreHolder, Footer.STATE_NONE);
            }
            return;
        }

        final int pos = getRealPosition(viewHolder);
        final GankPicBean.Item data = mDatas.get(pos);
        setDataAndRefreshNormalHolder(viewHolder, pos, data);

        if (mListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(pos, data);
                }
            });
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (holder.getLayoutPosition() == 0 || holder.getLayoutPosition() + 1 == getItemCount())) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    public int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getLayoutPosition();
        return mHeaderCount == 0 ? position : position - 1;
    }

    @Override
    public int getItemCount() {
        int itemCount = mDatas.size() + mHeaderCount + mFooterCount;
        return itemCount;
    }

    public RecyclerView.ViewHolder onCreateNormalHolder(final int viewType) {
        View layout = View.inflate(mContext, R.layout.item_stagger, null);
        return new NormalHolder(layout);
    }

    public void setDataAndRefreshNormalHolder(RecyclerView.ViewHolder viewHolder, final int RealPosition, GankPicBean.Item data) {
        if (viewHolder instanceof NormalHolder) {

            ((NormalHolder) viewHolder).mTvName.setText(data.publishedAt.substring(0, 10));
            //BitmapHelper.display(((NormalHolder) viewHolder).mIvIcon, data.url);
/*            bitmapCacheUtils.display(((NormalHolder) viewHolder).mIvIcon,data.url);*/
            if(!mContext.isFinishing()){

                Glide.with(BaseApplication.getContext())
                        .load(data.url)
                        .centerCrop()
                        .into(((NormalHolder) viewHolder).mIvIcon);
            }
            //Picasso.with(mContext).load(data.url).into(((NormalHolder) viewHolder).mIvIcon);
            ((NormalHolder) viewHolder).mIvIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ((NormalHolder) viewHolder).mIvIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, mDatas.get(RealPosition).publishedAt, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public RecyclerView.ViewHolder onCreateHeader(ViewGroup parent, final int viewType) {
        //注意此句
        View header = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_head, parent, false);

        return new Header(header);
    }

    final List<String> pictures = HomeFragment.getPicutures();

    public void setDataAndRefreshHeader(final RecyclerView.ViewHolder viewHolder, int RealPosition) {
        //BitmapHelper.display(image_homevp, HomeFragment.mDailySectences.get(position).picture + mDatas.get(position));

        mVp_Adapter = new PagerAdapter() {

            @Override
            public int getCount() {

                return Integer.MAX_VALUE;

            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                position = position % pictures.size();
                View item_home_vp = View.inflate(mContext, R.layout.item_home_viewpager, null);
                ImageView image_homevp = (ImageView) item_home_vp.findViewById(R.id.image_homevp);
                //image_homevp.setScaleType(ImageView.ScaleType.CENTER_CROP);//按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)
                //image_homevp.setImageResource(R.mipmap.h23);
                //NetUtils.initImageLoader(pictures.get(position), mContext, image_homevp);
                //BitmapHelper.display(image_homevp, pictures.get(position));
                if(!mContext.isFinishing()){

                    Glide.with(BaseApplication.getContext())
                            .load(pictures.get(position))
                            .centerCrop()
                            .into(image_homevp);
                    container.addView(item_home_vp);
                }
                return item_home_vp;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                System.out.println("destroyItem");
                mSelectedPage = position - 1;
                container.removeView((View) object);
            }

        };

        ((Header) viewHolder).mViewPager.setAdapter(mVp_Adapter);
        ((Header) viewHolder).mViewPager.setCurrentItem(Integer.MAX_VALUE/2);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "head", Toast.LENGTH_SHORT).show();
            }
        });
        //设置为立方体切换
        //((Header) viewHolder).mViewPager.setPageTransformer(true, new CubeOutTransformer());
        if(mAutoScrollTask == null){
            mAutoScrollTask = new AutoScrollTask(((Header) viewHolder).mViewPager);
            mAutoScrollTask.start();
        }

        ((Header) viewHolder).mViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mAutoScrollTask.stop();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                        mAutoScrollTask.start();
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
        //setViewPagerScrollSpeed(((Header) viewHolder).mViewPager,500);
    }
    //控制viewpager页面切换速度
    private void setViewPagerScrollSpeed(ViewPager viewPager, int speed) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            ViewPagerScroller viewPagerScroller = new ViewPagerScroller(viewPager.getContext(), new OvershootInterpolator(0.6F));
            field.set(viewPager, viewPagerScroller);
            viewPagerScroller.setDuration(speed);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public class ViewPagerScroller extends Scroller {
        private int mDuration;

        public ViewPagerScroller(Context context) {
            super(context);
        }

        public ViewPagerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public void setDuration(int mDuration) {
            this.mDuration = mDuration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, this.mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, this.mDuration);
        }

    }
    class AutoScrollTask implements Runnable {
        ViewPager mViewPager;
        public AutoScrollTask(ViewPager viewPager) {
            mViewPager = viewPager;
        }

        public void start(){
            UIUtils.postTaskDelay(this, 3000);
        }
        public void stop(){
            UIUtils.removeTask(this);
        }
        @Override
        public void run() {
            int item = mViewPager.getCurrentItem();
//            if(item == pictures.size()-1){
//                item = 0;
//            }else
                item++;
            mViewPager.setCurrentItem(item);
            start();
        }
    }

    public RecyclerView.ViewHolder onCreateFooter(ViewGroup parent, final int viewType) {
        View footer = View.inflate(mContext, R.layout.item_foot, null);
        return new Footer(footer);
    }

    public void setDataAndRefreshFooter(RecyclerView.ViewHolder viewHolder, int state) {
        ((Footer) viewHolder).mContainerLoading.setVisibility(View.GONE);
        ((Footer) viewHolder).mContainerRetry.setVisibility(View.GONE);
        switch (state) {
            case Footer.STATE_LOADING://正在加载更多中...
                ((Footer) viewHolder).mContainerLoading.setVisibility(View.VISIBLE);
                break;
            case Footer.STATE_RETRY://加载更多失败
                ((Footer) viewHolder).mContainerRetry.setVisibility(View.VISIBLE);
                break;
            case Footer.STATE_NONE:
                Toast.makeText(UIUtils.getContext(),"没有更多数据了",Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 去开始加载更多
                if (hasLoadMore()) {//本应用设为恒true
                    perFormLoadMore();
                } else {
                    setDataAndRefreshFooter(mLoadMoreHolder, Footer.STATE_NONE);
                }
            }
        });
    }

    public class Holder extends RecyclerView.ViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, GankPicBean.Item data);
    }

    public class NormalHolder extends BaseRecyclerAdapter.Holder {
        public TextView mTvName;
        public ImageView mIvIcon;

        public NormalHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.item_straggered_tv);
            mIvIcon = (ImageView) itemView.findViewById(R.id.item_straggered_iv);
        }

    }

    public class Header extends BaseRecyclerAdapter.Holder {
        private final MyViewPager mViewPager;
        private final LinearLayout mLl_indicator;

        public Header(View itemView) {
            super(itemView);
            mViewPager = (MyViewPager) itemView.findViewById(R.id.viewPager);
            mLl_indicator = (LinearLayout) itemView.findViewById(R.id.item_home_picture_container_indicator);
            for (int i = 0; i < pictures.size(); i++) {
                View indicatorView = new View(UIUtils.getContext());
                indicatorView.setBackgroundResource(R.mipmap.indicator_normal);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dip2Px(5), UIUtils.dip2Px(5));// dp-->px
                // 左边距
                params.leftMargin = UIUtils.dip2Px(5);
                // 下边距
                params.bottomMargin = UIUtils.dip2Px(5);
                mLl_indicator.addView(indicatorView, params);

                // 默认选中效果
                if (i == 0) {
                    indicatorView.setBackgroundResource(R.mipmap.indicator_selected);
                }
            }
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    position = position % pictures.size();
                    for (int i = 0; i < pictures.size(); i++) {
                        View indicatorView = mLl_indicator.getChildAt(i);
                        // 还原背景
                        indicatorView.setBackgroundResource(R.mipmap.indicator_normal);

                        if (i == position) {
                            indicatorView.setBackgroundResource(R.mipmap.indicator_selected);
                        }
                    }
                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }

    }

    public class Footer extends BaseRecyclerAdapter.Holder {

        public static final int STATE_LOADING = 0;
        public static final int STATE_RETRY = 1;
        public static final int STATE_NONE = 2;
        private final LinearLayout mContainerLoading;
        private final LinearLayout mContainerRetry;

        public Footer(View itemView) {
            super(itemView);
            mContainerLoading = (LinearLayout) itemView.findViewById(R.id.item_loadmore_container_loading);
            mContainerRetry = (LinearLayout) itemView.findViewById(R.id.item_loadmore_container_retry);
            TextView tv_retry = (TextView) mContainerRetry.findViewById(R.id.item_loadmore_tv_retry);
        }
    }

    class LoadMoreTask implements Runnable {
        @Override
        public void run() {
            // 真正开始请求网络加载更多数据
            GankPicBean loadMoreDatas = null;
            /*--------------- 根据加载更多的数据,处理 loadmore的状态 begin---------------*/
            int state = Footer.STATE_LOADING;
            try {
                loadMoreDatas = loadMore(mDatas.size() / Constants.PAGESIZE + 1);
                // 得到返回数据,处理结果
                if (loadMoreDatas == null) {// 没有更多数据
                    state = Footer.STATE_NONE;
                } else {
                    // 假如规定每页返回20
                    // 10<20==>没有加载更多
                    if (loadMoreDatas.results.size() < Constants.PAGESIZE) {// 没有加载更多
                        state = Footer.STATE_NONE;
                    } else {
                        state = Footer.STATE_LOADING;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                state = Footer.STATE_RETRY;
            }

			/*--------------- 定义一个中转的临时变量 ---------------*/
            final int tempState = state;
            final GankPicBean tempLoadMoreDatas = loadMoreDatas;
            /*--------------- 根据加载更多的数据,处理 loadmore的状态 end---------------*/
            UIUtils.postTaskSafely(new Runnable() {

                @Override
                public void run() {
                    // 刷新loadmore视图
                    setDataAndRefreshFooter(mLoadMoreHolder, tempState);
                    // 刷新listview视图 返回加载更多过后得到的数据 mDatas.addAll()
                    if (tempLoadMoreDatas != null) {
                        mDatas.addAll(tempLoadMoreDatas.results);// listview数据源更新
                        notifyDataSetChanged();// 刷新
                    }
                }
            });

            mLoadMoreTask = null;
        }
    }

    public GankPicBean loadMore(int index) throws Exception {
        GankPicProtocol gankPicProtocol = new GankPicProtocol();
		/*=============== 协议简单封装之后 ===============*/
        GankPicBean gankPicBean = gankPicProtocol.loadData(String.valueOf(index));
        if (gankPicBean == null) {
            return null;
        }

        return gankPicBean;
    }

}

