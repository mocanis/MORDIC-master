package com.chris.mordic.factory;

import android.support.v4.app.Fragment;
import android.support.v4.util.SparseArrayCompat;

import com.chris.mordic.fragment.HomeFragment;
import com.chris.mordic.fragment.ReciteFragment;
import com.chris.mordic.fragment.SquareFragment;
import com.chris.mordic.fragment.WordListFragment;

/**
 * Created by chris on 16-5-30.
 * Email: soymantag@163.coom
 */
public class FragmentFactory {
    /**
     <string-array name="main_titles">
     <item>首页</item>
     <item>单词复习</item>
     <item>生词本</item>
     <item>词悬浮</item>
     </string-array>
     */
    public static final int					FRAGMENT_HOME		= 0;
    public static final int					FRAGMENT_RECITE		= 1;
    public static final int					FRAGMENT_STUDYLIST	= 2;
    public static final int					FRAGMENT_SQUARE 	= 3;
    static SparseArrayCompat<Fragment> cachesFragment		= new SparseArrayCompat<>();

    public static Fragment getFragment(int position){
        Fragment fragment = null;
        // 如果缓存里面有对应的fragment,就直接取出返回

        Fragment tmpFragment = cachesFragment.get(position);
        if (tmpFragment != null) {
            fragment = tmpFragment;
            return fragment;
        }
        switch (position) {
            case FRAGMENT_HOME:// 主页
                fragment = new HomeFragment();
                break;
            case FRAGMENT_RECITE:
                fragment = new ReciteFragment();
                break;
            case FRAGMENT_STUDYLIST:
                fragment = new WordListFragment();
                break;
            case FRAGMENT_SQUARE:
                fragment = new SquareFragment();
                break;
            default:
                break;

        }
        // 保存对应的fragment
        cachesFragment.put(position, fragment);
        return fragment;
    }
}
