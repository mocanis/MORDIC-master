package com.chris.mordic.utils;

import com.chris.mordic.fragment.HomeFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by chris on 16-6-7.
 * Email: soymantag@163.coom
 */
public class DateUtils {
    public static ArrayList<String> initDate(String currentDate) {

        try {
            ArrayList<String> datas = new ArrayList<String>();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            Date date = simpleDateFormat.parse(currentDate);
            Date date1 = simpleDateFormat.parse(HomeFragment.mToday);

            int dateOffset = differ(date, date1);
            System.out.println("dateOffset：" + dateOffset);
            if (dateOffset >= 5)
                dateOffset = 4;

            for (int i = 1; i <= dateOffset; i++) {
                cal.setTime(date);
                //天数减一
                cal.add(Calendar.DATE, -1);
                date = cal.getTime();
                System.out.println("simpleDateFormat.format(date):" + simpleDateFormat.format(date));
                datas.add(simpleDateFormat.format(date));
            }
            return datas;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getpreDate(String currentDate,int dayOffset) {

        try {
            String predate;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = simpleDateFormat.parse(currentDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            for(int i =0;i<dayOffset;i++){
                cal.add(Calendar.DATE, -1);//日期减一天
            }
            date = cal.getTime();
            predate = simpleDateFormat.format(date);
            return predate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param date1 大日期
     * @param date2 小日期
     * @return
     */
    public static int differ(Date date1, Date date2) {
        //return date1.getTime() / (24*60*60*1000) - date2.getTime() / (24*60*60*1000);
        return (int) (date1.getTime() / 86400000 - date2.getTime() / 86400000);  //用立即数，减少乘法计算的开销
    }
}
