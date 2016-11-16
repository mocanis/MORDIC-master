package com.chris.mordic;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.chris.mordic.dao.WordDao;
import com.chris.mordic.protocol.WordProtocol;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    public void test_list() throws Exception {
        WordDao dao = new WordDao(getContext(),"test.db");
        dao.delete("test");
    }
    public void test1() throws Exception {
        WordDao dao = new WordDao(getContext(),"test.db");
        dao.delete("test");
    }
    //以apple为例进行增删改查
    public void test_add() throws Exception {
        WordDao dao = new WordDao(getContext(),"test.db");
        dao.add("apple",null,null);
    }
    public void test_replace() throws Exception {
        WordDao dao = new WordDao(getContext(),"test.db");
        //dao.replace("apple","word_bean","test_replace");
    }
    public void test_query() throws Exception {
        WordDao dao = new WordDao(getContext(),"test.db");

        System.out.println(dao.getBean("apple"));
    }
    public void test_getjosn() throws Exception {
        WordProtocol wordProtocol = new WordProtocol();
        String json = wordProtocol.getJsonFromNet("apple");
        System.out.println("word apple begin");
        //System.out.println(json);
        System.out.println("word apple end");
    }
    public void test_sout() throws Exception {

        System.out.println("json");
    }

}