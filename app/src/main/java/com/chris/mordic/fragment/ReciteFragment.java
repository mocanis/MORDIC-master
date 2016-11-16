package com.chris.mordic.fragment;

import android.view.View;
import android.widget.LinearLayout;

import com.chris.mordic.R;
import com.chris.mordic.base.BaseFragment;
import com.chris.mordic.base.LoadingPager;

/**
 * Created by chris on 16-5-30.
 * Email: soymantag@163.coom
 */
public class ReciteFragment extends BaseFragment {

    /**
     * @des 真正加载数据, 但是BaseFragment不知道具体实现, 必须实现, 定义成为抽象方法, 让子类具体实现
     * @des 它是LoadingPager同名方法
     * @call loadData()方法被调用的时候
     */
    @Override
    public LoadingPager.LoadedResult initData(String date) {
        return LoadingPager.LoadedResult.SUCCESS;
    }

    /**
     * @des 返回成功视图, 但是不知道具体实现, 所以定义成抽象方法
     * @des 它是LoadingPager同名方法
     * @call 正在加载数据完成之后, 并且数据加载成功, 我们必须告知具体的成功视图
     */
    @Override
    public View initSuccessView() {
        LinearLayout ly_recite = (LinearLayout) View.inflate(getActivity(), R.layout.fragment_recite, null);
        return ly_recite;
    }
}
