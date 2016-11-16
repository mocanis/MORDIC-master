package com.chris.mordic.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chris.mordic.conf.Constants;
import com.chris.mordic.db.WordDbOpenHelper;

/**
 * Created by chris on 16-5-25.
 * Email: soymantag@163.coom
 */
public class WordDao {
    private WordDbOpenHelper mWordDbOpenHelper;

    public WordDao(Context context,String name){
        this.mWordDbOpenHelper = new WordDbOpenHelper(context,name);
    }


    public void add(String word,byte[] bean,String notes){
        SQLiteDatabase db = mWordDbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word",word);
        values.put("word_bean",bean);
        values.put("word_notes", notes);
        db.insert("wordtb",null,values);
        db.close();
    }
    public void replace(String word,byte[] bean,String notes){
        SQLiteDatabase db = mWordDbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word",word);
        values.put("word_bean",bean);
        values.put("word_notes", notes);
        db.replace("wordtb",null,values);
/*        db.execSQL("replace into wordtb VALUES ("
                +""+word+", "
                +""+json+","
                +""+notes+");");*/
        db.close();
    }

    public boolean delete(String word){
        SQLiteDatabase db = mWordDbOpenHelper.getWritableDatabase();
        db.delete(Constants.Database.wordtb, Constants.Database.word+"=?;",new String[]{word});
        return true;
    }

    public void update (String word,byte[] bean,String notes){
        SQLiteDatabase db = this.mWordDbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.Database.word_bean,bean);
        values.put(Constants.Database.notes,notes);
        db.update(Constants.Database.wordtb,values,Constants.Database.word+"=?",new String[]{word});
        db.close();
    }
    public byte[] getBean(String word){
        SQLiteDatabase db = mWordDbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select "+Constants.Database.word_bean +" from "+ Constants.Database.wordtb+" where "+ Constants.Database.word+" = ?",new String[]{word});
        byte[] bean;
        if(cursor.moveToNext()){//查到此单词
            bean = cursor.getBlob(0);
        }else {//数据库中没有此单词
            bean = null;
        }
        cursor.close();
        return bean;
    }

}
