package com.chris.mordic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chris.mordic.R;
import com.chris.mordic.fragment.HomeFragment;

import java.util.List;

/**
 * Created by chris on 16-6-3.
 * Email: soymantag@163.coom
 */
public class MyStaggeredAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_HEADER = 1;
    Context mContext;
    List<HomeFragment.DataBean> mDatas;
    public MyStaggeredAdapter(Context context, List<HomeFragment.DataBean> datas) {
        this.mContext = context;
        this.mDatas = datas;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View itemView = View.inflate(mContext, R.layout.item_stagger,null);
            return new MyHolder(itemView);
        }else if(viewType == TYPE_FOOTER){
            View item_foot = LayoutInflater.from(mContext).inflate(R.layout.item_foot,parent,false);
            return new FootViewHolder(item_foot);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyHolder){
            ((MyHolder)holder).setDataAndRefreshUI(mDatas.get(position));
        }
    }




    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mDatas.size() == 0 ? 0 : mDatas.size()+1;
    }

    @Override
    public int getItemViewType(int position) {

        if(position+1 == getItemCount()){
            return TYPE_FOOTER;
        }else {
            return TYPE_ITEM;
        }
    }



    public class MyHolder extends RecyclerView.ViewHolder {
        private TextView mTvName;
        private ImageView mIvIcon;

        public MyHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.item_straggered_tv);
            mIvIcon = (ImageView) itemView.findViewById(R.id.item_straggered_iv);
        }

        public void setDataAndRefreshUI(HomeFragment.DataBean dataBean){
            mTvName.setText(dataBean.text);
            mIvIcon.setImageResource(dataBean.iconId);
        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {
        public FootViewHolder(View item_foot) {
            super(item_foot);
        }
    }
}
