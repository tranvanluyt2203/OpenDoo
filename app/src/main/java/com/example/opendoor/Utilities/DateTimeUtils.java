package com.example.opendoor.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DateTimeUtils {

    public static String getTimeFormat(Calendar calendar,String type,String format,boolean isNow) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return type.equals("hour")?
                isNow?"Now":timeFormat.format(calendar.getTime()).toUpperCase():
                isNow?"Today\n"+timeFormat.format(calendar.getTime()).substring(4):timeFormat.format(calendar.getTime());
    }
    public static List<String> getListTimeInDay(){
        Calendar calendar = Calendar.getInstance();
        List<String> results = new ArrayList<>();
        for (int i=0;i<24;i++){
            results.add(getTimeFormat(calendar,"hour","ha",i==0));
            calendar.add(Calendar.HOUR,1);
        }
        return results;
    }
    public static List<String> getListDateInWeek(){
        Calendar calendar = Calendar.getInstance();
        List<String> results = new ArrayList<>();
        for (int i=0;i<7;i++){
            results.add(getTimeFormat(calendar,"date","EEE\ndd/MM",i==0));
            calendar.add(Calendar.DAY_OF_WEEK,1);
        }
        return results;
    }
}
