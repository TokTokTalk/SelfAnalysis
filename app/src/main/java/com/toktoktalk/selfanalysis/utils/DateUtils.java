package com.toktoktalk.selfanalysis.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by seogangmin on 2015. 9. 29..
 */
public class DateUtils {

    public static String formatter(String format){
        SimpleDateFormat form = new SimpleDateFormat(format);
        Date now = new Date();

        return form.format(now);
    }

    public static Long getTimeStamp(){
        Long tsLong = System.currentTimeMillis()/1000;
        return tsLong;
    }


    public static ArrayList getBetWeenDateArray(String start, String end) throws ParseException{

        ArrayList retArray = new ArrayList();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

        Date d1 = df.parse( start );
        Date d2 = df.parse( end );

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime( d1 );
        c2.setTime( d2 );

        while( c1.compareTo( c2 ) !=1 ){
            retArray.add(df.format(c1.getTime()));
            c1.add(Calendar.DATE, 1);
        }

        return retArray;
    }
}
