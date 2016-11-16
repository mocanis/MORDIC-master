package com.chris.mordic.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chris.mordic.R;
import com.chris.mordic.activity.FileSelectActivity;
import com.chris.mordic.dao.WordDao;
import com.chris.mordic.data.WordBean;
import com.chris.mordic.data.WordBookBean;
import com.chris.mordic.protocol.WordProtocol;
import com.chris.mordic.utils.UIUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 7/4/16.
 * Email: soymantag@163.coom
 */
public class ImportWordsFragment extends Fragment {

    private ListView mListview;
    private List<WordBookBean> datas = new ArrayList<>();
    private RelativeLayout mLayout_show_words_num;
    private TextView mTv_improtWord;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wordbook, container, false);
        mListview = (ListView) view.findViewById(R.id.listView_wordbook);
        mTv_improtWord = (TextView) view.findViewById(R.id.tv_importWord);

        initData();
        initView();
        return view;
    }

    private void initView() {
        mLayout_show_words_num = (RelativeLayout) View.inflate(getActivity(), R.layout.layout_show_words_num, null);
        final View alphaView = mLayout_show_words_num.findViewById(R.id.alphaView);
/*        RelativeLayout ll1 = (RelativeLayout) mLayout_show_words_num.findViewById(R.id.ll1);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll1.getLayoutParams();
        layoutParams.setMargins(0,getStatusBarHeight()+ UIUtils.dip2Px(5),0,0);
        ll1.setLayoutParams(layoutParams);*/

        mListview.addHeaderView(mLayout_show_words_num);//addHeaderView要在setAdapter前调用,否则会出错
        mListview.setAdapter(new MyAdapter());
        //需要在xml文件中设置属性android:descendantFocusability="blocksDescendants",否则点击事件会被子组件消费
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), position + "", Toast.LENGTH_SHORT).show();
            }
        });
        mListview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                alphaView.setAlpha((float) ((visibleItemCount/10)));
            }
        });
        mTv_improtWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //intent.setDataAndType(Uri.fromFile(new File("/sdcard")),"*/*");
                intent.setDataAndType(Uri.fromFile(Environment.getExternalStorageDirectory().getParentFile()), "*/*");
                intent.setClass(getActivity(), FileSelectActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 40; i++) {
            WordBookBean history = new WordBookBean();
            history.setBookName("生词本" + i);
            history.setWordNum(0);
            datas.add(history);
        }
    }

    private class ViewHolder {
        ImageView im_icon;
        TextView tv_bookName;
        TextView tv_wordsNum;
    }

    private class MyAdapter extends BaseAdapter {

        private RelativeLayout mRl_item_wordbooklist;

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public android.view.View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_wordbooklist, null);
                viewHolder = new ViewHolder();
                mRl_item_wordbooklist = (RelativeLayout) convertView.findViewById(R.id.rl_item_wordbooklist);
                viewHolder.im_icon = (ImageView) convertView.findViewById(R.id.iv_wordBook);
                viewHolder.tv_bookName = (TextView) convertView.findViewById(R.id.tv_wordBookName);
                viewHolder.tv_wordsNum = (TextView) convertView.findViewById(R.id.tv_wordsNum);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            WordBookBean bean = datas.get(position);
            viewHolder.tv_bookName.setText(bean.getBookName());
            viewHolder.tv_wordsNum.setText(String.valueOf(bean.getWordNum()));

            return convertView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                final String path = data.getStringExtra("path");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("导入生词")
                        .setMessage("已选择的生词本路径为" + path + ",是否导入?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "导入中...", Toast.LENGTH_SHORT).show();
                                ImportWordsFromDisk(path);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        }
    }

    private void ImportWordsFromDisk(final String path) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final AlertDialog alertDialog = builder.setTitle("导入生词")
                        .setMessage("正在导入,请勿退出")
                        .setCancelable(false)
                        .create();
                alertDialog.show();
                new Thread(new Runnable() {

                    private Reader mReader;
                    private BufferedReader mBufferedReader;

                    @Override
                    public void run() {
                        try {
                            SystemClock.sleep(2000);
                            File file = new File(path);
                            String fileName = file.getName();
                            mReader = new FileReader(path);
                            mBufferedReader = new BufferedReader(mReader);
                            String line;

                            WordProtocol wordProtocol = new WordProtocol();
                            while ((line = mBufferedReader.readLine()) != null) {
                                System.out.println(line);
                                if (line.contains("&&")) {

                                } else {
                                    String word = line.trim();

                                    //String json = wordProtocol.getJsonFromNet(word);
                                    //System.out.println("jsonstring==>"+json);
                                    //SystemClock.sleep(5000);


                                    WordBean wordBean = wordProtocol.loadData(word);
                                    System.out.println("wordBean.word_name:"+wordBean.word_name);

                                    //序列化一个对象(存储到字节数组)
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                                    oos.writeObject(wordBean);

                                    WordDao wordDao = new WordDao(getActivity(), fileName);
                                    wordDao.replace(word, baos.toByteArray(), "0");
                                    System.out.println("replace into is ok");
                                    WordDao wordDao1 = new WordDao(getActivity(), fileName);
                                    byte[] beanByte = wordDao1.getBean(word);

                                    //反序列化,将该对象恢复(存储到字节数组)
                                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(beanByte));
                                    WordBean p = (WordBean)ois.readObject();
                                    System.out.println("wordname:"+p.word_name);

                                    oos.close();
                                    ois.close();

                                }
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                            //Toast.makeText(UIUtils.getContext(), "错误:文件未找到", Toast.LENGTH_SHORT).show();
                            System.out.println("错误:文件未找到");
                        } catch (IOException e) {
                            //Toast.makeText(UIUtils.getContext(), "导入失败,文件不可读/写", Toast.LENGTH_SHORT).show();
                            System.out.println("导入失败,文件不可读/写");
                            e.printStackTrace();
                        } catch (Exception e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(UIUtils.getContext(), "导入失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                                }
                            });

                            System.out.println("导入失败,请检查网络连接");
                            e.printStackTrace();
                        }finally {
                            try {
                                mReader.close();
                                mBufferedReader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        System.out.println("导入成功.");
                        alertDialog.dismiss();
                    }
                }).start();

            }
        });


    }

}
