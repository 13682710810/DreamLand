package wang.dreamland.www.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    //根据传入的日期字符串和日期格式字符串，返回格式化后的日期
    public static Date StringtoDate(String dateStr, String formatStr){
        DateFormat dd = new SimpleDateFormat(formatStr);
        Date date = null;
        try{
            date = dd.parse(dateStr);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    //将日期根据指定根式转成字符串
    public static String formatDate(Date date,String format){
        if(date == null){
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return dateString;
    }
}
