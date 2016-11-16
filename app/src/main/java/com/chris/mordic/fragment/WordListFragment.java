package com.chris.mordic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.chris.mordic.R;

/**
 * Created by chris on 16-5-30.
 * Email: soymantag@163.coom
 */
public class WordListFragment extends Fragment {

    private int isWordListImported = 0;
    private FrameLayout mFl_wordlist;
    private LinearLayout mLl_improtWordList;
    private FrameLayout mFrameLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFl_wordlist = (FrameLayout) inflater.inflate(R.layout.fragment_wordlist, container, false);
        mFrameLayout = (FrameLayout) mFl_wordlist.findViewById(R.id.framelayout);
        initView(inflater, container);
        return mFl_wordlist;
    }

    private void initView(LayoutInflater inflater, @Nullable ViewGroup container) {

/*        //判断是否已经导入过生词本
        if (SpTools.getBoolean(UIUtils.getContext(), Constants.IsWordListImported, false)) {
            loadMain();
        } else {
            loadImportView(inflater,container);
        }*/
        loadImportView(inflater, container);
    }

    private void loadImportView(LayoutInflater inflater, @Nullable ViewGroup container) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        ImportWordsFragment importWordsFragment = new ImportWordsFragment();
        transaction.replace(R.id.framelayout, importWordsFragment);
        transaction.commit();
    }

    private void loadMain() {

    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
