package com.app.basevideo.util;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.app.basevideo.framework.util.LogUtil;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * 字符串辅助类
 */
@SuppressLint("SimpleDateFormat")
public class StringHelper {
    protected static SimpleDateFormat FORMATE_DATE_ALL = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm");
    protected static SimpleDateFormat FORMATE_DATE_YEAR = new SimpleDateFormat(
            "yyyy年");
    protected static SimpleDateFormat FORMATE_DATE_TIME = new SimpleDateFormat(
            "HH:mm");
    protected static SimpleDateFormat FORMATE_DATE_MOUTH = new SimpleDateFormat(
            "M月d日");
    protected static SimpleDateFormat FORMATE_DATE_MOUTH_TIME = new SimpleDateFormat(
            "M月d日 HH:mm");
    protected static SimpleDateFormat FORMATE_DATE_DAY = new SimpleDateFormat(
            "yyyy-MM-dd");
    protected static SimpleDateFormat FORMATE_DATE_DAY_WEEK = new SimpleDateFormat("yyyy-MM-dd E");
    protected static SimpleDateFormat FORMATE_DATE_DAY_1 = new SimpleDateFormat("yy-M-d");
    protected static SimpleDateFormat FORMATE_DATE_MS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    protected static SimpleDateFormat FORMATE_DATE_DAY_NO_YEAR = new SimpleDateFormat(
            "MM-dd");

    /**
     * 将 CharSequence 转成 String 。 不能强转：
     */
    public static String charSequence2String(CharSequence m_text, String defaultValue) {
        if (m_text instanceof String) {
            return (String) m_text;
        } else if (m_text != null) {
            return m_text.toString();
        } else {
            return defaultValue;
        }
    }

    /**
     * 返回时间字符串，格式HH:mm
     */
    public static String getDateStringMdHm(Date date) {
        synchronized (FORMATE_DATE_MOUTH_TIME) {
            return FORMATE_DATE_MOUTH_TIME.format(date);
        }
    }

    /**
     * 返回时间字符串，格式HH:mm
     */
    public static String getDateStringHm(Date date) {
        synchronized (FORMATE_DATE_TIME) {
            return FORMATE_DATE_TIME.format(date);
        }
    }

    public static String getDateStringYear(Date date) {
        synchronized (FORMATE_DATE_YEAR) {
            return FORMATE_DATE_YEAR.format(date);
        }
    }

    public static String getDateStringMouth(Date date) {
        synchronized (FORMATE_DATE_MOUTH) {
            return FORMATE_DATE_MOUTH.format(date);
        }
    }

    public static String getDateStringDay(Date date) {
        synchronized (FORMATE_DATE_DAY) {
            return FORMATE_DATE_DAY.format(date);
        }
    }

    /**
     * 返回时间字符串，格式yyyy-mm-dd hh:mm:ss
     *
     * @return 当前的时间字符串
     */
    public static String getTimeString(long time) {
        Date date = new Date(time);
        synchronized (FORMATE_DATE_ALL) {
            return FORMATE_DATE_ALL.format(date);
        }
    }

    public static String getCurrentString() {
        Date date = new Date();
        synchronized (FORMATE_DATE_ALL) {
            return FORMATE_DATE_ALL.format(date);
        }
    }

    private static String getHourShow(int hour) {
        String m = null;
        if (hour < 10) {
            m = "0" + hour;
        } else {
            m = String.valueOf(hour);
        }

        String h = null;
        if (hour >= 0 && hour < 6) {
            h = "凌晨";
        } else if (hour >= 6 && hour < 9) {
            h = "早晨";
        } else if (hour >= 9 && hour < 12) {
            h = "上午";
        } else if (hour >= 12 && hour < 14) {
            h = "中午";
        } else if (hour >= 14 && hour < 18) {
            h = "下午";
        } else if (hour >= 18 && hour < 24) {
            h = "晚上";
        } else {
            h = "";
        }
        return h + m;
    }

    private static String getMinuteShow(int minute) {
        String m = null;
        if (minute < 10) {
            m = "0" + minute;
        } else {
            m = String.valueOf(minute);
        }
        return m;
    }

    private static String getWeekShow(int week) {
        String show;
        switch (week) {
            case Calendar.MONDAY:
                show = "周一";
                break;
            case Calendar.TUESDAY:
                show = "周二";
                break;
            case Calendar.WEDNESDAY:
                show = "周三";
                break;
            case Calendar.THURSDAY:
                show = "周四";
                break;
            case Calendar.FRIDAY:
                show = "周五";
                break;
            case Calendar.SATURDAY:
                show = "周六";
                break;
            case Calendar.SUNDAY:
                show = "周日";
                break;
            default:
                show = "";
        }
        return show;
    }

    /**
     * 获取微信消息列表页那种时间显示
     *
     * @param tarTime ：需要展现的消息时间
     * @param nowTime ：当前时间（可能是服务端时间_准，可能是客户端时间_不准）
     * @return 具体展现
     */
    public static String getMicroMsgTime(long tarTime, long nowTime) {
        // 异常情况
        if (nowTime == 0) {
            nowTime = System.currentTimeMillis() / 1000;
        }

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(tarTime * 1000);
        int tarYear = calendar.get(Calendar.YEAR);
        int tarMon = calendar.get(Calendar.MONTH) + 1;
        int tarDay = calendar.get(Calendar.DAY_OF_MONTH);
        int tarHour = calendar.get(Calendar.HOUR_OF_DAY);
        int tarMinu = calendar.get(Calendar.MINUTE);
        int tarWeOfY = calendar.get(Calendar.WEEK_OF_YEAR);
        int tarWeek = calendar.get(Calendar.DAY_OF_WEEK);

        // LogUtil.e("======="+tarYear+" "+tarMon+" "+ tarDay + " " + tarWeOfY +
        // " " + tarWeek);

        calendar.setTimeInMillis(nowTime * 1000);
        int nowYear = calendar.get(Calendar.YEAR);
        int nowMon = calendar.get(Calendar.MONTH) + 1;
        int nowDay = calendar.get(Calendar.DAY_OF_MONTH);
        int nowWeOfY = calendar.get(Calendar.WEEK_OF_YEAR);

        // LogUtil.e("======="+nowYear+" "+nowMon+" "+ nowDay + " " + nowWeOfY);

        String show = "";
        String hourShow = getHourShow(tarHour);
        String minuteShow = getMinuteShow(tarMinu);

        if (tarTime > nowTime) { // nowTime不准确的情况，对用户来说，这是将来的时间
            if (tarDay == nowDay) { // 在一天
                show = hourShow + ":" + minuteShow;
            } else {
                show = tarMon + "月" + tarDay + "日" + " " + hourShow + ":" + minuteShow;
            }
            return show;
        }

        if (tarYear < nowYear) { // 不在一年
            show = tarYear + "年" + tarMon + "月" + tarDay + "日" + " " + hourShow + ":" + minuteShow;
        } else if (tarMon < nowMon) { // 不在一月
            show = tarMon + "月" + tarDay + "日" + " " + hourShow + ":" + minuteShow;
        } else if (tarDay < nowDay) { // 不在一天
            if (tarWeOfY < nowWeOfY) { // 不在本周
                show = tarMon + "月" + tarDay + "日" + " " + hourShow + ":" + minuteShow;
            } else {
                String weekShow = getWeekShow(tarWeek);
                show = weekShow + " " + hourShow + ":" + minuteShow;
            }
        } else { // 当天
            show = hourShow + ":" + minuteShow;
        }

        return show;
    }


    public static String getTimeStringWithinMonth(Date tObj) {
        Date tClient = new Date();
        int day = tClient.getDay() - tObj.getDay();
        long ts = tClient.getTime() - tObj.getTime();
        long base = 30 * 1000; // 半分钟，30s

        // 此种情况是服务端时间大于当前客户端时间，对客户端来说，是服务端是未来时间，此时就显示刚刚就好
        // if (ts < 0) {
        // return getDateStringDay(tObj);
        // }

        if (ts < base) {
            return "刚刚";
        }

        base *= 2;// 1分钟，60s

        if (ts < base) {
            return "半分钟前";
        }

        base *= 60;// 1小时

        if (ts < base) {
            return String.valueOf(ts * 60 / base) + "分钟前";
        }

        base *= 24; // 24小时

        if (ts < base) {
            if (day == 0) {
                return getDateStringHm(tObj);
            } else {
                return "1天前";
            }
        }

        base *= 31; // 31天

        if (ts < base) {
            return String.valueOf(ts * 31 / base) + "天前";
        }

        base += 24 * 60 * 60 * 1000; // 32天

        if (ts < base) {
            return "1个月前";
        }
        return null;
    }

    /**
     * 判断字符串是否是纯数字
     *
     * @param value
     * @return
     */
    public static boolean isNumeric(String value) {
        if (StringHelper.isEmpty(value)) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    /**
     * 字符串中是否包含汉字
     *
     * @param str 字符串
     * @return
     */
    public static boolean ContentChinese(String str) {
        boolean ret = false;
        if (str == null || str.length() < 1) {
            return ret;
        }
        for (int i = 0; i < str.length(); i++) {
            if (isChinese(str.charAt(i))) {
                return true;
            }
        }
        return ret;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }


    /**
     * @brief 判断是有效的密码
     */
    public static boolean isPassword(String password) {
        // 先判断长度
        int len = password.length();
        if (len < 6 || len > 14) {
            return false;
        }
        int byteLen = password.getBytes().length;
        if (byteLen > len) {
            return false;
        }
        return true;
    }

    /**
     * 是否是电话号码
     *
     * @param phone
     * @return
     */
    public static boolean isMobileNo(String phone) {
        Pattern p = Pattern.compile("1\\d{10}");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 字符是否为空
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if ((s == null) || (s.length() == 0) || s.equals("null")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字符串是否为空。
     *
     * @param src 字符串
     * @return 是否为空
     */
    public static boolean isEmptyStringAfterTrim(String src) {
        return src == null || src.trim().length() == 0;
    }

    /**
     * 获取URLencode编码
     *
     * @param s
     * @return
     */
    public static String getUrlEncode(String s) {
        if (s == null) {
            return null;
        }
        String result = "";
        try {
            result = URLEncoder.encode(s, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 解码URLencode
     *
     * @param s
     * @return 解码后的字符串
     */
    public static String getUrlDecode(String s) {
        String result = null;
        try {
            result = URLDecoder.decode(s, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
     * 计算字符串长度
     */
    public static int byteLength(String string) {
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            if (Integer.toHexString(string.charAt(i)).length() == 4) {
                count += 2;
            } else {
                count++;
            }
        }
        return count;
    }

    public static String cutString(String string, int length) {
        if (string == null || length <= 0) {
            return String.valueOf("");
        }
        int len = string.length();
        int count = 0;
        int i = 0;
        for (i = 0; i < len; i++) {
            char c = string.charAt(i);
            if (isChinese(c)) {
                count += 2;
            } else {
                count++;
            }
            if (count >= length) {
                break;
            }
        }
        if (i < len) {
            return string.substring(0, i + 1) + "...";
        } else {
            return string;
        }
    }

    static public String getNameFromUrl(String url) {
        String name = null;
        try {
            int start = url.lastIndexOf("/");
            int end = url.lastIndexOf(".");
            if (start == -1) {
                name = url;
            } else if (start < end) {
                name = url.substring(start, end);
            } else {
                name = url.substring(start);
            }
        } catch (Exception ex) {
            LogUtil.e(ex.getMessage());
        }
        return name;
    }

    static public String getHighLightString(String src, Color color) {
        if (src == null) {
            return "";
        }
        String highLightString = null;
        try {
            highLightString = src
                    .replaceAll("<em>", "<font color=\'#007bd1\'>");
            highLightString = highLightString.replaceAll("</em>", "</font>");
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
        return highLightString;
    }

    private static long[] parseVersion(String ver) {
        long result[] = new long[3];
        if (ver != null) {
            ver = ver.replace(".", "#");
            String[] strarr = ver.split("#");
            result[0] = Long.parseLong(strarr[0]);
            result[1] = Long.parseLong(strarr[1]);
            result[2] = Long.parseLong(strarr[2]);
        }
        return result;
    }

    public static int compareVersion(String ver1, String ver2) {
        if (ver1 == null) {
            return -1;
        }
        if (ver2 == null) {
            return 1;
        }

        long[] ver1arr = parseVersion(ver1);
        long[] ver2arr = parseVersion(ver2);
        long ver1Result = 0, ver2Result = 0;
        for (int i = 0; i < 3; i++) {
            ver1Result += (ver1arr[i] << (24 - i * 8));
        }
        for (int i = 0; i < 3; i++) {
            ver2Result += (ver2arr[i] << (24 - i * 8));
        }
        if (ver1Result > ver2Result) {
            return 1;
        } else if (ver1Result == ver2Result) {
            return 0;
        } else {
            return -1;
        }
    }

    public static String StringFilter(String str) throws PatternSyntaxException {
        String regEx = "[/\\:*?<>|\"\n\t]"; // 要过滤掉的字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 连接字符串
     *
     * @param strs
     * @return
     */
    public static String join(String... strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        for (String str : strs) {
            buf.append(str);
        }
        return buf.toString();
    }

    /**
     * 将给定的字符串格式化成保留小数点后一位的 价钱 例如 186000 => 18.6万
     *
     * @param str
     * @return
     */
    public static String priceFormat(String str) {
        if (!isEmpty(str)) {
            if (isNumeric(str)) {
                int wan = Integer.parseInt(str) / 10000;
                int qian = Integer.parseInt(str) % 10000 / 1000;
                return String.valueOf(wan + "." + Math.round(qian) + "万");
            }
        }
        return "";
    }


    public static boolean isBetweenTwoInteger(String inputValue, String vehiclePrice) {
        if (StringHelper.isEmpty(inputValue) || StringHelper.isEmpty(vehiclePrice)) {
            return false;
        }
        int input = 0;
        int price = 0;
        try {
            input = Integer.parseInt(inputValue);
            price = Integer.parseInt(vehiclePrice);
        } catch (NumberFormatException e) {
            LogUtil.e(e.toString());
        }
        return (input > price * 0.7 && input < price * 1.3);
    }

    public static boolean isGreaterThanZero(String value) {
        if (StringHelper.isEmpty(value)) {
            return false;
        }
        int tmp = -1;
        try {
            tmp = (int) Float.parseFloat(value);
        } catch (NumberFormatException e) {
            LogUtil.e(e.toString());
        }
        return tmp > 0;
    }
}
