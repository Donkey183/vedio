package com.app.basevideo.util;

import com.app.basevideo.framework.util.LogUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {


    private DateUtil() {

    }

    /**
     * 将字符串转换为时间
     *
     * @return strDate
     */
    public static String createDate(String formatStr) {
        Date date = new Date();
        try {
            DateFormat format = new SimpleDateFormat(formatStr);
            format.setLenient(false);
            return format.format(date);
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
        return null;
    }
}


