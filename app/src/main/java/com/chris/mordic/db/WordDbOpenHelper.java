package com.chris.mordic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chris on 16-5-25.
 * Email: soymantag@163.coom
 */
public class WordDbOpenHelper extends SQLiteOpenHelper{

    public WordDbOpenHelper(Context context, String name) {
        super(context, name, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table wordtb(word text primary key,word_bean BLOB,word_notes text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table wordtb");
        onCreate(db);
    }

}
