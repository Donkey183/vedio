package com.app.basevideo.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.TouchDelegate;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.app.basevideo.base.MFBaseApplication;
import com.app.basevideo.framework.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @since 2012-8-7
 */
public class Utils {
    private static long lastClickTime;
    private static long delayTime = 800;
    private static long longDelayTime = 2500;
    /** 圣诞语音时限 **/
    //private static final String CHRISTMAS_START_TIME = "2014-12-22 00:00:00";
    //private static final String CHRISTMAS_END_TIME = "2014-12-25 23:59:59";

    /**
     * 不能通过本工程获取
     * 获取当前版本号
     */
//    public static String getCurrentVersionName() {
//        return SystemUtil.getVersionName();
//    }
//
//    public static String getChannelID() {
//        return SystemUtil.getChannelId();
//    }

    /**
     * 获取当前VersionCode
     */
//    public static int getCurrentVersionCode() {
//        return SystemUtil.getVersionCode();
//    }

    /**
     * 是否展示气泡
     */
    public static boolean isShow() {
        return true;
    }

    /**
     * 获取软件的PackageName
     */
    public static String getPackageName(Context context) {
        String pkg = context.getApplicationContext().getPackageName();
        return pkg;
    }

    /**
     * 得到手机的IMEI号
     *
     * @return
     */
    public static String getIMEI() {
        try {
            TelephonyManager mTelephonyMgr = (TelephonyManager) MFBaseApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            boolean permission = containReadPhoneStatePermission();
            if (permission) {
                if (mTelephonyMgr.getDeviceId() == null) {
                    return "";
                } else
                    return mTelephonyMgr.getDeviceId();
            } else {
                return "";
            }
        } catch (Exception exception) {
            return "";
        }
    }

    /**
     * 检测权限
     *
     * @return
     */
    private static boolean containReadPhoneStatePermission() {
        PackageManager pm = MFBaseApplication.getInstance().getPackageManager();
        //防止在静态加载类库时，权限没有被加载问题
        boolean permission = PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.READ_PHONE_STATE", MFBaseApplication.getInstance().getPackageName());
        return permission;
    }

    /**
     * 获得手机的型号
     */
    public static String getModel() {
        String temp = Build.MODEL;
        if (TextUtils.isEmpty(temp)) {
            return "";
        } else {
            return temp;
        }

    }

    /**
     * 今天 15：40
     *
     * @param millis
     * @return
     */
    public static String getDayOfMillis(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String time = sdf.format(date);

        Calendar curent = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        int days = calendar.get(Calendar.DAY_OF_YEAR) - curent.get(Calendar.DAY_OF_YEAR);
        if (days == 0) {
            time = "今天 " + time;
        } else if (days == 1) {
            time = "明天 " + time;
        } else if (days == 2) {
            time = "后天 " + time;
        }
        return time;
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    public static String getNetworkType() {
        String name = "UNKNOWN";
        Context c = MFBaseApplication.getInstance();

        ConnectivityManager connMgr = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
                return "WIFI";
            }
        }

        TelephonyManager tm = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return name;
        }

        int type = tm.getNetworkType();
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                name = "2G";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                name = "3G";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                name = "4G";
                break;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                name = "UNKNOWN";
                break;
            default:
                name = "UNKNOWN";
                break;
        }
        return name;
    }

    /**
     * 是否开启了wifi
     *
     * @return
     */
    public static boolean isWifi(Context context) {
        return TextUtils.equals("wifi", getCurrentApnType());
    }

    /**
     * 得到手机当前正在使用的网络类型，如果是wifi的话，返回 wifi字符串，否则返回other_类型编码，参考TelephonyManager
     *
     * @return
     */
    public static String getCurrentApnType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MFBaseApplication.getInstance().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (ni.isConnected()) { // 如果有wifi连接，则选择wifi，不返回代理
            return "wifi";
        }

        TelephonyManager telmanager = (TelephonyManager) MFBaseApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
        int typ = telmanager.getNetworkType();

        String type = "NULL";
        if (typ == TelephonyManager.NETWORK_TYPE_EDGE) {
            type = "EDGE"; // 2.75G
        }
        if (typ == TelephonyManager.NETWORK_TYPE_GPRS) {
            type = "GPRS"; // 2G
        }
        if (typ == TelephonyManager.NETWORK_TYPE_UMTS) {
            type = "UTMS"; // 3G
        }
        if (typ == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
            type = "UNKNOWN";
        }
        return type;
    }

    /**
     * 判断软件主界面是否在前台
     *
     * @param ctx
     * @param pkgName
     * @return
     */
    public static boolean isAppTopFront(Context ctx, String pkgName) {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = am.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            if (pkgName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
                return true;
            }
        }
        return false;

    }


    /**
     * 获取字节数
     *
     * @param inStream
     * @return 字节数
     * @throws Exception
     */
    public static long read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] b = outStream.toByteArray();
        return b.length;
    }


    /**
     * 保存文件
     *
     * @param fileName 文件名
     */
    public static boolean saveFile(final Context ctx, InputStream isInputStream, final String fileName) {
        int len = 0;
        try {
            OutputStream os = new FileOutputStream(fileName);
            byte[] buf = new byte[1024];

            while ((len = isInputStream.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            isInputStream.close();

            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 计算经纬度距离
     */
    public static double distance(double lat_a, double lng_a, double lat_b, double lng_b) {
        final double EARTH_RADIUS = 6378137.0;
        double radLat1 = (lat_a * Math.PI / 180.0);
        double radLat2 = (lat_b * Math.PI / 180.0);
        double a = radLat1 - radLat2;
        double b = (lng_a - lng_b) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 日志输出
     */
    public static void writeLog(String phone, String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd HH:mm:ss");
        String d = sdf.format(new Date());
        String string = d + "||" + s;
        FileOutputStream fos;
        File saveDir = Environment.getExternalStorageDirectory();
        String p = saveDir.getAbsolutePath() + File.separator;
        try {
            fos = new FileOutputStream(new File(p + phone + ".txt"), true);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
            osw.write(string);
            osw.write("\n");
            osw.flush();
            osw.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /**
     * 向SD卡写日志
     *
     * @param phone   司机电话
     * @param clz     所在的类文件
     * @param content 日志内容
     */
    public static void debugLog(Context context, String phone, String clz, String content) {
        if (isAvailableSpace()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd HH:mm:ss");
            String d = sdf.format(new Date());
            String resut = d + ":" + clz + ":" + content;
            writeLog(phone, resut);
        }
    }

    /**
     * 判断SDCard是否存在
     */
    public static boolean haveSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * SDCard是否有剩余空间
     */
    public static boolean isAvailableSpace() {
        if (!haveSDCard()) {
            return false;
        }
        boolean flag = false;
        int bytelength = 1024;
        File pathFile = Environment.getExternalStorageDirectory();
        StatFs statfs = new StatFs(pathFile.getPath());
        // 获取SDCard上每个block的SIZE
        long m_nBlocSize = statfs.getBlockSize();
        // 获取可供程序使用的Block的数量
        long m_nAvailaBlock = statfs.getAvailableBlocks();
        long total = m_nAvailaBlock * m_nBlocSize / bytelength;
        if (total > 5120) {
            flag = true;
        }
        return flag;
    }

    private static byte[] readInStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inStream.close();
        return outputStream.toByteArray();
    }

    // 获取本地IP函数
    private static String getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface.getNetworkInterfaces(); mEnumeration.hasMoreElements(); ) {
                NetworkInterface intf = mEnumeration.nextElement();
                for (Enumeration<InetAddress> enumIPAddr = intf.getInetAddresses(); enumIPAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIPAddr.nextElement();
                    // 如果不是回环地址
                    if (!inetAddress.isLoopbackAddress()) {
                        // 直接返回本地IP地址
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // 显示IP信息
    private static String getIp(NetworkInfo mActiveNetInfo) {
        // 如果是WIFI网络
        if (mActiveNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return getLocalIPAddress();
        }
        // 如果是手机网络
        else if (mActiveNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return getLocalIPAddress();
        } else {
            return null;
        }

    }

    private static void recursion(String root, Vector<File> vecFile) {
        File file = new File(root);
        File[] subFile = file.listFiles();
        if (subFile != null) {
            for (int i = 0; i < subFile.length; i++) {
                if (subFile[i].isDirectory()) {
                    recursion(subFile[i].getAbsolutePath(), vecFile);
                } else {
                    vecFile.add(subFile[i]);
                }
            }
        }
    }

    /**
     * 得到手机屏幕宽
     *
     * @return
     */
    public static int getWindowWidth(Activity window) {
        DisplayMetrics dm = new DisplayMetrics();
        window.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }


    /**
     * 得到手机上面当前默认使用的接入点名称及端口
     *
     * @param context
     * @return K:V proxy,port,user,pwd
     */
    public static HashMap<String, String> getCurrentApn(Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (Build.VERSION.RELEASE.startsWith("4")
                || Build.VERSION.RELEASE.startsWith("5") || Build.VERSION.RELEASE.startsWith("6")) {
            String proxy = android.net.Proxy.getDefaultHost();
            int port = android.net.Proxy.getDefaultPort();
            if (!TextUtils.isEmpty(proxy)) {
                map.put("proxy", proxy);
                map.put("port", port + "");
            }
            return map;
        }

        Cursor cursor = context.getContentResolver().query(Uri.parse("content://telephony/carriers/preferapn"), null, null, null, null);

        String proxy = null;
        String port = null;
        String userName = null;
        String pwd = null;
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToNext()) {
                proxy = cursor.getString(cursor.getColumnIndex("proxy"));
                port = cursor.getString(cursor.getColumnIndex("port"));
                userName = cursor.getString(cursor.getColumnIndex("user"));
                pwd = cursor.getString(cursor.getColumnIndex("password"));

                if (TextUtils.isEmpty(proxy) || "null".equals(proxy)) {

                } else {
                    map.put("proxy", proxy);
                    if (TextUtils.isEmpty(port) || "null".equals(port)) {
                    } else {
                        map.put("port", port);
                    }
                    if ("10.0.0.200".equals(proxy)) {
                        if (TextUtils.isEmpty(userName) || "null".equals(userName)) {
                        } else {
                            map.put("user", userName);
                        }
                        if (TextUtils.isEmpty(pwd) || "null".equals(pwd)) {
                        } else {
                            map.put("pwd", pwd);
                        }
                    }
                }
            }
            cursor.close();
        }
        return map;
    }


    /**
     * 判断当前应用是否正在运行
     */
    public static boolean isMyAppRunning(Context c) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = manager.getRunningTasks(Integer.MAX_VALUE);
        for (RunningTaskInfo info : list) {
            String packageName = info.topActivity.getPackageName();
            if (getPackageName(c).equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0;
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isTextEmpty(String text) {
        if (TextUtils.isEmpty(text))
            return true;
        return "null".equalsIgnoreCase(text);
    }


    /**
     * 网络是否可用
     *
     * @return
     */
    public static boolean isNetworkConnected() {
        Context context = MFBaseApplication.getInstance().getContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo network = cm.getActiveNetworkInfo();
        if (network != null) {
            return network.isConnected() || network.isAvailable();
        } else {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            State wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            return wifiManager.isWifiEnabled() && wifiState.equals(State.CONNECTED);
        }
    }


    /**
     * 将日期转成毫秒
     *
     */
    public static long converDateToMillisecond(String date) {
        if (Utils.isTextEmpty(date))
            return 0L;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long mills = 0;
        try {
            Date d = sdf.parse(date);
            mills = d.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mills;
    }

    public static String trimText(String str) {
        if (isTextEmpty(str))
            return "";
        return str.trim();
    }


    /**
     * 扩大view控件的点击判断范围,用于解决控件较小时，不易点中的问题
     *
     * @param delegateView 需要改变点击判断区域的view
     * @param scaleRatio   ：内部将处理为scaleRatio/100 ：四个方向扩大的尺寸比例(相对于delegateView的本地坐标系)
     */
    public static void enlargeHitRect(final View delegateView, final int scaleRatio) {
        if (delegateView == null) {
            return;
        }
        final View parentView = View.class.isInstance(delegateView.getParent()) ? (View) delegateView.getParent() : null;
        if (delegateView != null && parentView != null && scaleRatio > 0) {
            delegateView.post(new Runnable() {
                @Override
                public void run() {
                    Rect newBounds = new Rect();
                    delegateView.getHitRect(newBounds);
                    int xOffset = Math.round((float) Math.abs(newBounds.left - newBounds.right) * scaleRatio / 100) / 2;// 水平方向单边偏移的大小
                    int yOffset = Math.round((float) Math.abs(newBounds.bottom - newBounds.top) * scaleRatio / 100) / 2;// 水平方向单边偏移的大小

                    newBounds.left -= xOffset;
                    newBounds.right += xOffset;
                    newBounds.top -= yOffset;
                    newBounds.bottom += yOffset;

                    parentView.setTouchDelegate(new TouchDelegate(newBounds, delegateView));
                }
            });
        }
    }

//    /**
//     * 将毫秒转为日期-时间字符串
//     *
//     * @param millisec
//     * @return
//     */
//    public static String[] convertDateTime(long millisec) {
//        Context context = MFBaseApplication.getAppContext();
//        String dateString = context.getString(R.string.unknown);
//        String timeString = context.getString(R.string.unknown);
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(millisec);
//        int month = c.get(Calendar.MONTH) + 1;
//        dateString = month + context.getString(R.string.month) + c.get(Calendar.DAY_OF_MONTH) + context.getString(R.string.day);
//        if (0 <= c.get(Calendar.MINUTE) && c.get(Calendar.MINUTE) < 10) {
//            timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + "0" + c.get(Calendar.MINUTE);
//        } else {
//            timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
//        }
//        long nowTime = System.currentTimeMillis();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(nowTime);
//        if (c.get(Calendar.YEAR) - calendar.get(Calendar.YEAR) == 0) {
//            int number = c.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR);
//            switch (number) {
//                case 0:
//                    dateString = context.getString(R.string.today);
//                    if (0 <= c.get(Calendar.MINUTE) && c.get(Calendar.MINUTE) < 10) {
//                        timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + "0" + c.get(Calendar.MINUTE);
//                    } else {
//                        timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
//                    }
//                    break;
//                case 1:
//                    dateString = context.getString(R.string.tomorrow);
//                    if (0 <= c.get(Calendar.MINUTE) && c.get(Calendar.MINUTE) < 10) {
//                        timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + "0" + c.get(Calendar.MINUTE);
//                    } else {
//                        timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
//                    }
//                    break;
//                case 2:
//                    dateString = context.getString(R.string.acquired);
//                    if (0 <= c.get(Calendar.MINUTE) && c.get(Calendar.MINUTE) < 10) {
//                        timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + "0" + c.get(Calendar.MINUTE);
//                    } else {
//                        timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
//                    }
//                    break;
//                case -1:
//                    dateString = context.getString(R.string.yesterday);
//                    if (0 <= c.get(Calendar.MINUTE) && c.get(Calendar.MINUTE) < 10) {
//                        timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + "0" + c.get(Calendar.MINUTE);
//                    } else {
//                        timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//        String[] str = new String[]{dateString, timeString};
//        return str;
//    }

    /**
     * 将千毫秒格式化为yyyy-MM-dd HH:mm:ss格式
     */
    public static String formatDate(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 将千毫秒格式化为yyyy-MM-dd HH:mm格式
     */
    public static String btsformatDate(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

    // string类型转换为date类型
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }


    // date类型转换为long类型
    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
    }


    public static String sha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes());

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 检查当前应用是否在前台运行
     *
     * @return 在前台运行时返回true
     */
    public static boolean isRunningForeground() {
        String packageName = MFBaseApplication.getInstance().getPackageName();
        String topAppPackageName = getTopActivityPackegeName();

        if (TextUtils.isEmpty(packageName))
            return false;
        if (TextUtils.isEmpty(topAppPackageName))
            return false;
        return topAppPackageName.equals(packageName);
    }

    /**
     * 获取处于系统前台应用packageName
     *
     * @return 处于手机屏幕前台的Activity的包名
     */
    public static String getTopActivityPackegeName() {
        String topActivityPackeageName = null;
        ActivityManager activityManager = (ActivityManager) (MFBaseApplication.getInstance()
                .getSystemService(Context.ACTIVITY_SERVICE));
        List<RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            if (f != null)
                topActivityPackeageName = f.getPackageName();
        }
        return topActivityPackeageName;
    }


    /**
     * 获取SD卡路径，返回格式 path+"/"
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    }

    /**
     * sd卡是否可用
     */
    public static boolean isSDCardAvailble() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
//
//    /**
//     * 获取SD卡上的Didi目录，返回格式 path+"/"
//     */
//    public static String getDidiPath() {
//        return getSDCardPath() + VideoConstant.SDCARD_FILE_DIR + File.separator;
//    }


    public static String getAppPath() {
        return MFBaseApplication.getInstance().getFilesDir().getAbsolutePath() + File.separator;
    }


    /**
     * 将大等于60的描述转换为min:sec格式
     */
    public static String second2Min(int second) {
        int minute = 60;
        if (second < 60) {
            String secd = String.format("%1$02d", second);
            return "00:" + secd;
        }
        int minInt = second / minute;
        int secInt = second % minute;
        if (minInt >= 1) {
            String mins = String.format("%1$02d", minInt);
            String secd = String.format("%1$02d", secInt);
            return mins + ":" + secd;
        }
        return second + "";
    }


    /**
     * 获取小时数
     */
    public static int getHour(String str, int blankCount) {
        int ret = 0;
        if (str.split(" ").length < (blankCount + 1)) {
            return ret;
        }
        String hh_mm = str.split(" ")[blankCount];
        String s = hh_mm.substring(0, 2);
        try {
            ret = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 获取分钟数
     */
    private static int getMinutes(String str, int blankCount) {
        int ret = 0;
        if (str.split(" ").length < (blankCount + 1)) {
            return ret;
        }
        String hh_mm = str.split(" ")[blankCount];

        String s = hh_mm.substring(3);
        try {
            ret = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return ret;
    }


    /**
     * 正则表达式验证
     *
     * @param str 验证字符串
     * @return
     */
    public static boolean isNum(String str) {

        if (TextUtils.isEmpty(str))
            return false;

        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNumatcher = pattern.matcher(str);
        if (!isNumatcher.matches()) {
            return false;
        }
        return true;
    }


    /**
     * 显示输入法
     */
    public static void showInputMethod(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏输入法
     */
    public static void hideInputMethod(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInputFromWindow(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }


    /**
     * 格式化推送时间
     */
    public static String getFormattedPushTime(String time) {
        int pushTime = Integer.parseInt(time);
        if (pushTime < 1) {
            return "1";
        }
        return time;
    }

    /**
     * 防止快速重复点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < delayTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    /**
     * 获取已安装的应用列表
     **/
    public static String getAllInstalledPkg() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PackageManager pm = MFBaseApplication.getInstance().getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, 0);
        StringBuilder sb = new StringBuilder();
        if (resolveInfos != null && !resolveInfos.isEmpty()) {

            for (int i = 0; i < resolveInfos.size(); i++) {
                ResolveInfo ri = resolveInfos.get(i);
                String pkgName = ri.activityInfo.packageName;
                sb.append(pkgName).append("*");
            }

        }
        return sb.toString();
    }


    /**
     * 获取屏幕分辨率
     *
     * @param context
     */
    public static DisplayMetrics getDisplayPixels(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * Fetch screen height and width, to use as our max size when loading images as this activity
     * runs full screen
     *
     * @return
     */
    public static int getLongestDisplay(Context context) {
        // Fetch screen height and width, to use as our max size when loading images as this
        // activity runs full screen
        final DisplayMetrics displayMetrics = getDisplayPixels(context);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;

        // For this sample we'll use half of the longest width to resize our images. As the
        // image scaling ensures the image is larger than this, we should be left with a
        // resolution that is appropriate for both portrait and landscape. For best image quality
        // we shouldn't divide by 2, but this will use more memory and require a larger memory
        // cache.
        final int longest = (height > width ? height : width);
        return longest;
    }

//    /**
//     * 将毫秒转为日期-时间字符串
//     *
//     * @param millisec
//     * @return
//     */
//    public static String[] convertIMDateTime(long millisec) {
//        Context context = MFBaseApplication.getInstance();
//        String dateString = context.getString(R.string.unknown);
//        String timeString = context.getString(R.string.unknown);
//        Calendar c = Calendar.getInstance();
//        c.setTimeInMillis(millisec);
//        int month = c.get(Calendar.MONTH) + 1;
//        dateString = month + context.getString(R.string.month) + c.get(Calendar.DAY_OF_MONTH) + context.getString(R.string.day);
//        if (0 <= c.get(Calendar.MINUTE) && c.get(Calendar.MINUTE) < 10) {
//            timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + "0" + c.get(Calendar.MINUTE);
//        } else {
//            timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
//        }
//        long nowTime = System.currentTimeMillis();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(nowTime);
//        if (c.get(Calendar.YEAR) - calendar.get(Calendar.YEAR) == 0) {
//            timeString = c.get(Calendar.HOUR_OF_DAY) + ":" + "0" + c.get(Calendar.MINUTE);
//        }
//        String[] str = new String[]{dateString, timeString};
//        return str;
//    }

    public static boolean isActivityInRunningList(Context context, Class activityClazz) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = manager.getRunningTasks(Integer.MAX_VALUE);

        for (RunningTaskInfo taskInfo : list) {
            String className = taskInfo.topActivity.getClassName();
            if (className.equals(activityClazz.getName())) {
                return true;
            }
        }
        return false;
    }

    public static String getMacSerialno() {
        String cpuSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
            if (pp == null) {
                return null;
            }
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    cpuSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return cpuSerial;
    }

    public static String getCPUSerialno() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            if (pp == null) {
                return null;
            }
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return macSerial;
    }

    public static String getAndroidID() {
        return Secure.getString(MFBaseApplication.getInstance().getContentResolver(), Secure.ANDROID_ID);
    }


    /**
     * 是否在圣诞节期间
     *
     * @return
     */
//    public static boolean isInChristmasDay() {
//        long currentTime = System.currentTimeMillis();
//        long startTime = converDateToMillisecond(CHRISTMAS_START_TIME);
//        long endTime = converDateToMillisecond(CHRISTMAS_END_TIME);
//        return currentTime >= startTime && currentTime <= endTime;
//    }

    /**
     * 获取URL地址，不含?部分
     */
    public static String getUrlWithOutParam(String url) {
        if (TextUtils.isEmpty(url) || url.indexOf("?") == -1) {
            return url;
        }
        return url.split("\\?")[0];
    }

    public static boolean isAppFront(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasksInfo = am.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            if (tasksInfo.get(0).topActivity.getClassName().contains("com.didi")) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取签名
     *
     * @param paramContext
     * @param paramString  包名
     * @return
     */
    private static Signature[] getRawSignatures(Context paramContext, String paramString) {
        if ((paramString == null) || (paramString.length() == 0)) {
            LogUtil.d("getRawSignatures packageName is Null");
            return null;
        }
        PackageManager pm = paramContext.getPackageManager();
        PackageInfo packageInfo;
        try {
            packageInfo = pm.getPackageInfo(paramString, 64);
            if (packageInfo == null) {
                LogUtil.d("packageInfo is Null, packageName = " + paramString);
                return null;
            }
        } catch (NameNotFoundException e) {
            return null;
        }
        return packageInfo.signatures;

    }


    public static String convertToChineseWeekNumber(int number) {
        switch (number) {
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            case 0:
                return "日";
            default:
                return "";
        }
    }

    public static int getDayDiff(long time1, long time2) {
        Date dateA = new Date(time1);
        Date dateB = new Date(time2);
        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(dateA);
        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dateB);

        if (calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)) {
            return calDateB.get(Calendar.DAY_OF_MONTH) - calDateA.get(Calendar.DAY_OF_MONTH);
        } else if (calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR) && ((calDateB.get(Calendar.MONTH) - calDateA.get(Calendar.MONTH)) == 1
                || (calDateB.get(Calendar.MONTH) - calDateA.get(Calendar.MONTH)) == -11)) {//处理跨年情况
            return calDateB.get(Calendar.DAY_OF_MONTH) + (getCurrentMonthLastDay() - calDateA.get(Calendar.DAY_OF_MONTH));
        }
        return 0;
    }

    public static int getCurrentMonthLastDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }


    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 判断当前手机是否有ROOT权限
     *
     * @return
     */
    public static boolean isRoot() {
        boolean bool = false;
        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                bool = false;
            } else {
                bool = true;
            }
        } catch (Exception e) {
        }
        return bool;
    }


    /**
     * 扩大View的触摸和点击响应范围,最大不超过其父View范围
     *
     * @param view
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    public static void expandViewTouchDelegate(final View view, final int top,
                                               final int bottom, final int left, final int right) {

        ((View) view.getParent()).post(new Runnable() {
            @Override
            public void run() {
                Rect bounds = new Rect();
                view.setEnabled(true);
                view.getHitRect(bounds);

                bounds.top -= top;
                bounds.bottom += bottom;
                bounds.left -= left;
                bounds.right += right;

                TouchDelegate touchDelegate = new TouchDelegate(bounds, view);

                if (View.class.isInstance(view.getParent())) {
                    ((View) view.getParent()).setTouchDelegate(touchDelegate);
                }
            }
        });
    }


    public static String getCityName(String city) {
        return null;
    }


    public static String formatePrice(double price) {
        if (price == 0) {
            return "0";
        } else {
            DecimalFormat df = new DecimalFormat("#.0");
            return subZeroAndDot(df.format(price));
        }
    }

    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 应答气泡数字格式化
     *
     * @param d
     * @return
     */
    public static String formateDouble(double d) {
        if (d == 0) {
            return "0";
        } else {
            DecimalFormat df = new DecimalFormat("#.0");
            String string = df.format(d);
            if (string.startsWith(".")) {
                return "0" + string;
            } else {
                return string;
            }
        }
    }

    @SuppressLint("NewApi")
    public static void setImportantForAccessibilityNo(View view) {
        if (view == null) {
            return;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 16) {
            view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
        }
    }


    @SuppressLint("NewApi")
    public static void setImportantForAccessibilityYES(View view) {
        if (view == null) {
            return;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 16) {
            view.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private boolean hasSoftKeys(WindowManager windowManager) {
        Display d = windowManager.getDefaultDisplay();


        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 17) {
            d.getRealMetrics(realDisplayMetrics);
        }


        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;


        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);


        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;


        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    public static String getWeekName(Calendar calendar) {
        String string = "(周一)";

        int week = calendar.get(Calendar.DAY_OF_WEEK);
        if (week == 1) {
            string = "(周日)";
        } else if (week == 2) {
            string = "(周一)";
        } else if (week == 3) {
            string = "(周二)";
        } else if (week == 4) {
            string = "(周三)";
        } else if (week == 5) {
            string = "(周四)";
        } else if (week == 6) {
            string = "(周五)";
        } else if (week == 7) {
            string = "(周六)";
        }

        return string;
    }

}
