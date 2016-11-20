package com.app.basevideo.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.app.basevideo.base.MFBaseApplication;
import com.app.basevideo.framework.util.LogUtil;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {

    private static final String LOG_FOLDER = ".WL";
    /**
     * 默认保留数值的精度-小数点后1位
     */
    public static final int DEF_SCALE = 1;
    private static long lastClickTime;

    private static Typeface sTypeFace = null;
    /**
     * 字体文件，位于 assets/fonts/
     */
    private static final String FONT_NAME = "";

    private static final int REQ_GPS_SETTING = 100;


    /**
     * 获取当前版本号
     */
    public static String getAppVersion() {
        Context context = MFBaseApplication.getInstance();
        String pkgName = getPackageName(context);
        try {
            PackageInfo pinfo = context.getApplicationContext().getPackageManager()
                    .getPackageInfo(pkgName, PackageManager.GET_CONFIGURATIONS);
            return pinfo.versionName;
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * 从token中获取用户的id
     *
     * @param token
     * @return
     */
    public static String getDriverIdByToken(String token) {
        if (TextUtils.isEmpty(token) || token.length() != 19) {
            return null;
        }
        return token.substring(10);
    }

    /**
     * 获取软件的PackageName
     */
    public static String getPackageName(Context context) {
        return context.getApplicationContext().getPackageName();
    }

    /**
     * 获取内存大小
     *
     * @return
     */
    public static int getMemorySize() {
        File pathFile = Environment.getDataDirectory();
        StatFs statFs = new StatFs(pathFile.getPath());
        long blockSize = statFs.getBlockSize();
        long availableSize = statFs.getAvailableBlocks();
        long memSize = availableSize * blockSize;
        int size = (int) (memSize / 1024 / 1024);
        return size;
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return true:可用 false:不可用
     * @note 这个方法不是百分百可靠，部分机型获取到的NetworkInfo为空。比如HTC的G11
     */
    public static boolean hasInternet(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected()) {
            // 网络不可用
            return false;
        }
        if (info.isRoaming()) {
            return true;
        }
        return true;
    }

    /**
     * 得到手机的IMEI号
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = mTelephonyMgr.getDeviceId();
        if (imei == null) {
            return "";
        }
        return imei;
    }

    /**
     * 得到手机的IMSI号
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = mTelephonyMgr.getSubscriberId();
        if (imsi == null) {
            return "";
        }
        return imsi;
    }

    /**
     * 获得手机的型号
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (TextUtils.isEmpty(model)) {
            return "";
        }
        return model;
    }

    /**
     * 获取手机厂商名字
     */
    public static String getManufacturer() {
        String name = Build.MANUFACTURER;
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        return name;
    }

    /**
     * 得到手机当前网络是否可用
     *
     * @param context
     * @return
     * @note 这个方法不是百分百可靠，部分机型获取到的NetworkInfo为空。比如HTC的G11
     */
    public static boolean getNetworkEnable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        boolean flag = false;
        if (activeNetInfo != null) {
            flag = connectivityManager.getActiveNetworkInfo().isAvailable();
        }
        return flag;
    }

    /**
     * 飞行模式是否打开
     */
    public static boolean isAirPlaneModeOn(Context context) {
        ContentResolver cr = context.getContentResolver();
        String mode = Settings.System.getString(cr, Settings.System.AIRPLANE_MODE_ON);
        if ("0".equals(mode)) {
            return false;
        }
        return true;
    }

    /**
     * 获取手机号
     */
    public static String getMobilePhone(Context context) {
        TelephonyManager telephonyMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyMgr != null) {
            String tel = telephonyMgr.getLine1Number();
            if (!TextUtils.isEmpty(tel) && tel.startsWith("+86")) {
                tel = tel.replace("+86", "");
            }
            return tel;
        } else {
            return null;
        }
    }

    /**
     * 判断手机号是否合法
     *
     * @deprecated
     */
    public static boolean isMobile(String mobiles) {
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        }
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 获取网络状态
     */
    public static boolean getMobileDataStatus(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        Class<?> conMgrClass = null; // ConnectivityManager类
        Field iConMgrField = null; // ConnectivityManager类中的字段
        Object iConMgr = null; // IConnectivityManager类的引用
        Class<?> iConMgrClass = null; // IConnectivityManager类
        Method getMobileDataEnabledMethod = null; // setMobileDataEnabled方法

        try {
            // 取得ConnectivityManager类
            conMgrClass = Class.forName(conMgr.getClass().getName());
            // 取得ConnectivityManager类中的对象mService
            iConMgrField = conMgrClass.getDeclaredField("mService");
            // 设置mService可访问
            iConMgrField.setAccessible(true);
            // 取得mService的实例化类IConnectivityManager
            iConMgr = iConMgrField.get(conMgr);
            // 取得IConnectivityManager类
            iConMgrClass = Class.forName(iConMgr.getClass().getName());
            // 取得IConnectivityManager类中的getMobileDataEnabled(boolean)方法
            getMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod("getMobileDataEnabled");
            // 设置getMobileDataEnabled方法可访问
            getMobileDataEnabledMethod.setAccessible(true);
            // 调用getMobileDataEnabled方法
            return (Boolean) getMobileDataEnabledMethod.invoke(iConMgr);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 手机是否有GPS模块
     *
     * @return
     */
    public static boolean hasGPSDevice() {
        LocationManager mgr = (LocationManager) MFBaseApplication.getInstance().getSystemService(
                Context.LOCATION_SERVICE);
        if (mgr == null) {
            return false;
        }
        List<String> providers = mgr.getAllProviders();
        if (providers == null) {
            return false;
        }
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    /**
     * GPS开关是否打开
     *
     * @return
     */
    public static boolean isGPSEnable() {
        String str = Secure.getString(
                MFBaseApplication.getInstance().getContentResolver(),
                Secure.LOCATION_PROVIDERS_ALLOWED);
        if (str != null) {
            return str.contains("gps");
        } else {
            return false;
        }
    }

    /**
     * 跳转到系统设置GPS页面
     *
     * @param activity
     */
    public static void redirectToGpsSetting(Activity activity) {
        // 设置完成后返回到原来的界面
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        try {
            activity.startActivityForResult(intent, REQ_GPS_SETTING);
        } catch (ActivityNotFoundException ex) {
            // The Android SDK doc says that the location settings activity
            // may not be found. In that case show the general settings.

            // General settings activity
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                activity.startActivityForResult(intent, REQ_GPS_SETTING);
            } catch (Exception e) {
            }
        }
    }

    /**
     * 自动开启GPS
     *
     * @note 仅在Android 2.2及以下好使
     */
    public static void openGPS(Context ctx) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(ctx, 0, GPSIntent, 0).send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }

    public static boolean isWifiConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MFBaseApplication
                .getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetworkInfo.isConnected();
    }

    /**
     * 打开网络
     */
    public static boolean gprsEnabled(Context ctx, boolean bEnable) {
        if (isWifiConnected())
            return false;
        boolean isOpen = gprsIsOpenMethod(ctx, "getMobileDataEnabled");
        if (isOpen == !bEnable) {
            setGprsEnabled(ctx, "setMobileDataEnabled", bEnable);
        }
        return isOpen;
        // }
    }

    private static boolean gprsIsOpenMethod(Context ctx, String methodName) {
        ConnectivityManager mCM = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Class<?> cmClass = mCM.getClass();
        Class<?>[] argClasses = null;
        Object[] argObject = null;

        Boolean isOpen = false;
        try {
            Method method = cmClass.getMethod(methodName, argClasses);

            isOpen = (Boolean) method.invoke(mCM, argObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOpen;
    }

    private static void setGprsEnabled(Context ctx, String methodName, boolean isEnable) {
        ConnectivityManager mCM = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        Class<?> cmClass = mCM.getClass();
        Class<?>[] argClasses = new Class[1];
        argClasses[0] = boolean.class;

        try {
            Method method = cmClass.getMethod(methodName, argClasses);
            method.invoke(mCM, isEnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
     * 获取网络类型
     *
     * @return
     */
    public static String getNetworkType() {
        String name = "UNKNOWN";
        Context c = MFBaseApplication.getInstance();

        ConnectivityManager connMgr = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
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
                name = "GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                name = "EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                name = "UMTS";
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                name = "CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                name = "EVDO_0";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                name = "EVDO_A";
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                name = "1xRTT";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                name = "HSDPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                name = "HSUPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                name = "HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                name = "IDEN";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                name = "EVDO_B";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                name = "LTE";
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                name = "EHRPD";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                name = "HSPAP";
                break;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            default:
                break;
        }
        return name;
    }

    /**
     * 得到手机当前正在使用的网络类型，如果是wifi的话，返回 wifi字符串，否则返回other_类型编码，参考TelephonyManager
     *
     * @return
     */
    public static String getCurrentApnType() {
        Context context = MFBaseApplication.getInstance();
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (ni.isConnected()) { // 如果有wifi连接，则选择wifi，不返回代理
            return "wifi";
        }

        TelephonyManager telmanager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        int typ = telmanager.getNetworkType();

        switch (typ) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:// 电信2G
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_0: // 电信3G
            case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3G
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                return "UNKNOWN";
        }
    }

    /**
     * 获取系统API Level
     *
     * @return
     */
    public static int getSystemVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 返回系统的版本，如“4.0.3”
     */
    public static String getSystemSDKName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 安装apk
     *
     * @param context
     * @param apkPath 安装包的绝对路径
     */
    public static void installApk(Context context, String apkPath) {
        Uri uri = Uri.fromFile(new File(apkPath)); // APK路径
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        context.startActivity(intent);
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
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1)
                * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 距离转经度
     *
     * @param meters   距离
     * @param latitude 当前点的纬度
     * @return 传入距离表示的经度值
     */
    public static double m2L(double meters, double latitude) {
        double r = 20037508.34;
        double l = meters / (r * Math.cos(latitude * Math.PI / 180) / 180);
        try {
            DecimalFormat df = new DecimalFormat("#.0000");
            return Double.valueOf(df.format(l));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    /**
     * 测试时调用
     */
    public static String getRandomHms_Test(int min, int max) {

        String hour = String.valueOf((int) Math.rint(Math.random() * (max - min)) + min);
        String minute = String.valueOf((int) Math.rint(Math.random() * (59 - 0) + 0));
        String second = String.valueOf((int) Math.rint(Math.random() * (59 - 0) + 0));
        String hms = hour + ":" + minute + ":" + second;
        // NqLog.e(new Exception(), "Link Time:" + hms1);
        return hms;
    }

    /**
     * 测试时调用
     */
    public static long getNextLinkMillisecond_Test(int nextDay, String time) {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        c.setTimeInMillis(System.currentTimeMillis());
        c.add(Calendar.DAY_OF_MONTH, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 1);
        c.set(Calendar.SECOND, (int) Math.rint(Math.random() * (59 - 0) + 0));
        // NqLog.e(new Exception(), "Next Link Time:" + nextDay + " " + h + ":"
        // + m + ":" + hms[2]);
        return c.getTimeInMillis();
    }

    /**
     * 根据条件返回一定范围的随机数
     *
     * @param min 随机数的最小值
     * @param max 随机数的最大值
     * @return hms 时分秒
     */
    public static String getRandomHms(int min, int max) {
        String hour = String.valueOf((int) Math.rint(Math.random() * (max - min)) + min);
        String minute = String.valueOf((int) Math.rint(Math.random() * (59 - 0) + 0));
        String second = String.valueOf((int) Math.rint(Math.random() * (59 - 0) + 0));
        String hms = hour + ":" + minute + ":" + second;
        return hms;
    }

    /**
     * 根据条件返回一定范围的随机数
     *
     * @param min 随机数的最小值
     * @param max 随机数的最大值
     * @return hms 时分秒
     */
    public static int[] getRandomHmsInt(int min, int max) {
        int hour = (int) Math.rint(Math.random() * (max - min)) + min;
        int minute = (int) Math.rint(Math.random() * (59 - 0) + 0);
        int second = (int) Math.rint(Math.random() * (59 - 0) + 0);
        int[] times = new int[3];
        times[0] = hour;
        times[1] = minute;
        times[2] = second;
        return times;
    }

    /**
     * 存储卡上是否有足够的空间（20M）
     *
     * @return 如果没有存储卡或存储卡上空间不足返回false
     */
    public static boolean isSpaceEnough() {
        if (!DeviceUtil.isSdcardMounted()) {
            return false;
        }
        boolean ret = false;
        File pathFile = Environment.getExternalStorageDirectory();
        StatFs statfs = new StatFs(pathFile.getPath());
        // 获取SDCard上每个block的SIZE
        long m_nBlocSize = statfs.getBlockSize();
        // 获取可供程序使用的Block的数量
        long m_nAvailaBlock = statfs.getAvailableBlocks();
        long total = m_nAvailaBlock * m_nBlocSize / (1024 * 1024);
        if (total > 20) {
            ret = true;
        }
        return ret;
    }

    /**
     * 手机上获取物理唯一标识码
     */
    public static String getMobileID(Context ctx) {
        return Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);
    }

    /**
     * 创建文件夹
     */
    public static String mkDir(String dirName) {
        String strDir = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
        if (!TextUtils.isEmpty(dirName)) {
            strDir += dirName;
        }
        File dir = new File(strDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath() + File.separator;
    }

    /**
     * 获取SD卡路径，返回格式 path+"/"
     */
    public static String getPropertyPath() {
        return mkDir(null);
    }

    /**
     * 获取SD卡路径，返回格式 path+"/"
     */
    public static String getSDCardPath() {
        return mkDir(LOG_FOLDER);
    }

    /**
     * 获取订单track文件目录
     */
    public static String getOrderTracksPath() {
        return mkDir(LOG_FOLDER + File.separator + ".order");
    }

    /**
     * 获取网络异常日志的路径，返回格式 path+"/"
     */
    public static String getNetExceptionLogPath() {
        return MFBaseApplication.getInstance().getExternalFilesDir("netExceptionLog")
                + File.separator;
    }


    /**
     * @param ctx
     * @return
     * @note 这个方法不是百分百可靠，部分机型获取到的NetworkInfo为空。比如HTC的G11
     */
    public static String getMobileIP(Context ctx) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 实例化mActiveNetInfo对象
        NetworkInfo mActiveNetInfo = mConnectivityManager.getActiveNetworkInfo();// 获取网络连接的信息
        if (mActiveNetInfo == null) {
            return null;
        } else {
            return getIp(mActiveNetInfo);
        }
    }

    // 获取本地IP函数
    private static String getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface
                    .getNetworkInterfaces(); mEnumeration.hasMoreElements(); ) {
                NetworkInterface intf = mEnumeration.nextElement();
                for (Enumeration<InetAddress> enumIPAddr = intf.getInetAddresses(); enumIPAddr
                        .hasMoreElements(); ) {
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


    /**
     * 获取文件名
     */
    public static String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    /**
     * 匹配连续出现的字符串
     */
    public static boolean regxSameString(String str) {
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        int len = str.length() - 1;
        String regx = "^.*(.)\\1{" + len + "}.*$";
        Matcher m1 = null;
        Pattern p1 = null;
        p1 = Pattern.compile(regx);
        m1 = p1.matcher(str);
        if (m1.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 验证密码是否为相同字符
     */
    public static boolean isSamePwd(String str) {
        if (AppUtils.regxSameString(str)) {
            return false;
        }
        return true;
    }

    /**
     * 验证密码是否为123456
     */
    public static boolean isSimplePwd(String str) {
        if (str.equals("123456") || str.equals("654321")) {
            return false;
        }
        return true;
    }


    /**
     * 系统是否已ROOT
     *
     * @return
     */
    public static boolean isRooted() {

        String suFileDir[] = {"/system/xbin/which", "/sbin/", "/system/sbin/", "/system/bin/",
                "/system/xbin/", "/verifyData/local/xbin/", "/verifyData/local/bin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/verifyData/local/", "/vendor/bin/"};

        for (int i = 0; i < suFileDir.length; i++) {
            File f = new File(suFileDir[i] + "su");
            if (f != null && f.exists()) {
                return true;
            }
        }

        return false;
    }


    /**
     * 是否已安装了指定的App
     *
     * @param pkg 要检查的App的包名，有多个用逗号分开
     * @return 是否安装了指定App
     */
    public static boolean isInstalledApp(String pkg) {
        if (TextUtils.isEmpty(pkg)) {
            return false;
        }

        try {
            String[] arr = pkg.split(",");
            PackageManager pm = MFBaseApplication.getInstance().getPackageManager();

            if (arr != null) {
                for (int i = 0; i < arr.length; i++) {
                    try {
                        PackageInfo info = pm.getPackageInfo(arr[i], PackageManager.GET_ACTIVITIES);
                        if (info != null) {
                            return true;
                        }
                    } catch (NameNotFoundException e) {
                        // e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return false;
    }

    /**
     * 是否指定的App正在运行
     *
     * @param pkg 要检查的App的包名，有多个用逗号分开
     * @return 指定的App是否正在运行
     */
    public static boolean isSpecifiedAppRunning(String pkg) {
        if (TextUtils.isEmpty(pkg)) {
            return false;
        }

        String[] arr = pkg.split(",");
        ActivityManager am = (ActivityManager) MFBaseApplication.getInstance().getSystemService(
                Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcessList = am.getRunningAppProcesses();
        try {
            for (int i = 0; i < arr.length; i++) {
                for (RunningAppProcessInfo appProcessInfo : appProcessList) {
                    if (arr[i].equalsIgnoreCase(appProcessInfo.processName)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取指定APP的签名，并计算其md5值
     *
     * @param pack 指定的APP包名
     * @return 该APP签名计算得到的MD5值
     */
    public static String getAppSignitureMd5(String pack) {
        PackageManager pm = MFBaseApplication.getInstance().getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(pack, PackageManager.GET_SIGNATURES);
            if (info != null) {
                Signature sign = info.signatures[0];
                return MD5.md5(sign.toCharsString());
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 跳转到系统设置开发者选项界面
     *
     * @param context
     */
    public static void redirectToDeveloperSetting(Context context) {
        /* 尝试跳转到开发者选项 */
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
            return;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }

        /* 尝试跳转到"应用程序->开发" */
        Intent i = new Intent();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setClassName("com.android.settings", "com.android.settings.DevelopmentSettings");
        try {
            context.startActivity(i);
            return;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前手机上单个App可用的内存大小
     *
     * @return
     */
    public static int getAvailMemory() {
        ActivityManager activityManager = (ActivityManager) MFBaseApplication.getInstance()
                .getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager.getMemoryClass();
    }

    /**
     * 获取当前程序是debug还是releasae
     */
    public static boolean isApkDebugable() {
        try {
            ApplicationInfo info = MFBaseApplication.getInstance().getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 得到手机当前正在使用的网络类型，如果是wifi的话，返回 wifi字符串，否则返回other_类型编码，参考ConnectivityManager.TYPE
     *
     * @return
     * @note 这个方法不是百分百可靠，部分机型获取到的NetworkInfo为空。比如HTC的G11
     */
    public static String getCurrentNetType() {
        String name = "";
        int netType = -1;
        String apnString = "";
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) MFBaseApplication
                    .getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

            if (activeNetInfo != null) {
                netType = activeNetInfo.getType();
                if (null != activeNetInfo.getExtraInfo()) {
                    apnString = activeNetInfo.getExtraInfo().toLowerCase();
                }
            }

            if (netType == ConnectivityManager.TYPE_WIFI) {
                name = "wifi";
            } else {
                name = apnString;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 获得网络类型 netWorkTypes[0] 为运营商 netWorkTypes[1] 为网络类型
     * 如果是wifi情况下，netWorkTypes[0]为空字符串，netWorkTypes[1] 为WIFI 如果是非wifi下，例如：netWorkTypes[0]为cmnet,
     * netWorkTypes[1]为HSDPA
     *
     * @return
     */
    public static String[] getNetWorkName() {
        String[] netWorkTypes = new String[2];
        ConnectivityManager connectMgr = (ConnectivityManager) MFBaseApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectMgr != null) {
            NetworkInfo info = connectMgr.getActiveNetworkInfo();
            if (info != null) {
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {// 用的移动数据
                    netWorkTypes[0] = info.getExtraInfo();
                    netWorkTypes[1] = info.getSubtypeName();
                } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {// 用的wifi
                    netWorkTypes[0] = null;
                    netWorkTypes[1] = info.getTypeName();
                }
            }
        }
        return netWorkTypes;
    }


    /**
     * 提供精确的小数位截断处理
     *
     * @param value 需要处理的数值
     * @param scale 需要截断的小数位数
     * @return
     */
    public static double floor(double value, int scale) {
        if (scale < 0) {
            scale = 0;
        }
        BigDecimal bd = new BigDecimal(value);
        return bd.setScale(scale, RoundingMode.FLOOR).doubleValue();
    }


    /**
     * 将小端字节改为大端字节
     */
    public static void littleEndianToBigEndian(byte[] data) {
        byte tem;
        int byteLength = data.length;
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            for (int i = 0; i < byteLength / 2; i++) {
                tem = data[i];
                data[i] = data[byteLength - 1 - i];
                data[byteLength - 1 - i] = tem;
            }
        }
    }

    /**
     * 将小端字节直接改为大端字节,不用判断本地的字节序，用来处理服务端传来的数据
     */
    public static byte[] littleEndianDirectToBigEndian(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        int byteLength = data.length;
        byte[] result = new byte[byteLength];
        int c = 0;
        for (int i = byteLength - 1; i >= 0; i--, c++) {
            if (c < byteLength) {
                result[i] = data[c];
            } else {
                result[i] = 0;
            }
        }
        return result;
    }

    /**
     * 获得网络类型，如果是wifi情况下，netWorkTypes[0]为空字符串，netWorkTypes[1] 为WIFI;
     * 如果是非wifi下，netWorkTypes[0]为cmnet, netWorkTypes[1]为HSDPA
     */
    public static String[] getNetWorkType() {
        String[] netWorkTypes = new String[2];
        ConnectivityManager connectMgr = (ConnectivityManager) MFBaseApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectMgr != null) {
            NetworkInfo info = connectMgr.getActiveNetworkInfo();
            if (info != null) {
                if (info.getType() == ConnectivityManager.TYPE_MOBILE) {// 用的移动数据
                    netWorkTypes[0] = info.getExtraInfo();
                    netWorkTypes[1] = info.getSubtypeName();
                } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {// 用的wifi
                    netWorkTypes[0] = null;
                    netWorkTypes[1] = info.getTypeName();
                }
            }
        }
        return netWorkTypes;
    }

    /**
     * 获取移动运营商信息
     *
     * @return
     */
    public static String getMobileOperators() {
        Context c = MFBaseApplication.getInstance();
        TelephonyManager telManager = (TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telManager.getSimOperator();
        if (operator != null) {
            // 中国移动
            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {
                return "CMCC";
            } else if (operator.equals("46001")) { // 中国联通
                return "CU";
            } else if (operator.equals("46003")) { // 中国电信
                return "CT";
            }
        }
        return "UNKNOWN";
    }

    /**
     * 获取MCC+MNC
     *
     * @return
     */
    public static String getCarriers() {
        Context c = MFBaseApplication.getInstance();
        TelephonyManager telManager = (TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telManager.getSimOperator();
        if (operator != null) {
            return operator;
        }
        return "";
    }

    public static boolean isIp(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
        Pattern p = Pattern.compile(regex);
        return p.matcher(str).find();
    }


    public static String getHostIpByHostName(String hostName) {
        InetAddress inetAddress = null;
        String ip = null;
        try {
            inetAddress = InetAddress.getByName(hostName);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (inetAddress != null) {
            ip = inetAddress.getHostAddress();
        }
        return ip;
    }

    public static boolean ipAddressValid(String text) {
        if (TextUtils.isEmpty(text)) {
            return false;
        }
        // 定义正则表达式
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        // 判断ip地址是否与正则表达式匹配
        return text.matches(regex);
    }

    public static void hideSoftKeyboard(Context ctx, View v) {
        InputMethodManager imm = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0); // 强制隐藏键盘
    }

    public static Intent getHotlineIntent() {
        Uri uri = Uri.parse("tel:4000000666");
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        return intent;
    }


    /**
     * 拨打电话，从html5跳转
     */
    public static void call(Context context, String action) {
        if (TextUtils.isEmpty(action)) {
            return;
        }
        try {
            Uri uri = Uri.parse(action);
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static SpannableString getColoredString(String string, int start, int end,
                                                   int textColor, float textSize, boolean isBold) {
        SpannableString sp = new SpannableString(string);
        // 设置文字颜色
        sp.setSpan(new ForegroundColorSpan(textColor), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 文字大小
        if (textSize > 0) {
            sp.setSpan(new AbsoluteSizeSpan((int) textSize, true), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (isBold) {
            sp.setSpan(new StyleSpan(Typeface.BOLD), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sp;
    }

    public static SpannableString getColoredString(String string, int start, int end, int textColor) {
        return getColoredString(string, start, end, textColor, 0, false);
    }

    public static SpannableString getColoredString(String string, int[] startArray, int[] endArray,
                                                   int textColor) {
        return getColoredString(string, startArray, endArray, textColor, 0, false);
    }

    public static SpannableString getColoredString(String string, int[] startArray, int[] endArray,
                                                   int textColor, int textSize, boolean isBold) {
        SpannableString sp = new SpannableString(string);
        // 设置文字颜色
        for (int i = 0, size = startArray.length; i < size; i++) {
            int start = startArray[i];
            int end = endArray[i];
            sp.setSpan(new ForegroundColorSpan(textColor), start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 文字大小
            if (textSize > 0) {
                sp.setSpan(new AbsoluteSizeSpan(textSize, true), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (isBold) {
                sp.setSpan(new StyleSpan(Typeface.BOLD), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return sp;
    }


    private static String getCrashInfo(Throwable cause, boolean isMainCrash) {
        if (cause == null) {
            return "";
        }
        StringBuilder buffer = new StringBuilder();
        if (!isMainCrash) {
            buffer.append("Caused by: ");
        }
        buffer.append(cause.getClass().getName()).append(": ").append(cause.getLocalizedMessage())
                .append("\n");
        StackTraceElement[] t = cause.getStackTrace();
        if (t != null && t.length > 0) {
            for (StackTraceElement ste : t) {
                buffer.append("\tat: ").append(ste.getClassName()).append(".")
                        .append(ste.getMethodName()).append("(").append(ste.getFileName())
                        .append(":").append(ste.getLineNumber()).append(")\n");
            }
        }
        return buffer.toString();
    }


    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en
                    .hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr
                        .hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            LogUtil.e("WifiPreference IpAddress");
        }
        return null;
    }

    /**
     * 获取APP安装的时间
     **/
    public static String getAppInstallTime(Context mContext) {
        PackageManager packageManager = mContext.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            return String.valueOf(packageInfo.firstInstallTime);// 应用安装的时间
        } catch (NameNotFoundException e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * afn 文件信息标识
     **/
    public static String getAfnInfo() {
        InputStream localInputStream = null;
        try {
            List<String> mlist = new ArrayList<String>();
            float maxValue = 0; // M级
            Process localprocess = Runtime.getRuntime().exec("df");
            localInputStream = localprocess.getInputStream();
            DataInputStream localDataInputStream = new DataInputStream(localInputStream);
            int count = localInputStream.available();

            do {
                String temp = localDataInputStream.readLine();
                if (temp == null) {
                    break;
                } else {
                    mlist.add(temp);
                }
            } while (true);
            Iterator<String> mIterator = mlist.iterator();
            while (mIterator.hasNext()) {
                String[] split = mIterator.next().replaceAll(" +", "#").split("#"); // 考虑结果中没有‘#’，所以把所有空格替换成#
                if (split == null || split.length < 2) {
                    continue;
                }
                if (split[2].endsWith("K")) {
                    continue; // 考虑到最小也是M级，所以不比较K级的
                } else if (split[2].endsWith("M")) {
                    if (Float.valueOf(split[2].substring(0, split[2].length() - 1)) > maxValue) {
                        maxValue = Float.valueOf(split[2].substring(0, split[2].length() - 1));
                    } else {
                        continue;
                    }
                } else if (split[2].endsWith("G")) {
                    if (1000 * Float.valueOf(split[2].substring(0, split[2].length() - 1)) > maxValue) {
                        maxValue = 1000 * Float
                                .valueOf(split[2].substring(0, split[2].length() - 1));
                    } else {
                        continue;
                    }
                }
            }
            return maxValue + "M";
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != localInputStream) {
                try {
                    localInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取apn 图片信息标识
     **/
    public static String getApnInfo() {
        if (DeviceUtil.isSdcardMounted()) {
            try {
                File dcim = new File(Environment.getExternalStorageDirectory() + "/DCIM");
                return FormetFileSize(getFileSizes(dcim));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        if (flist != null && flist.length > 0) {
            for (File file : flist) {
                if (file == null || !file.exists()) {
                    continue;
                }
                if (file.isDirectory()) {
                    size += getFileSizes(file);
                } else {
                    size += getFileSize(file);
                }
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 获取指定文件大小
     *
     * @param file
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) {
        long size = 0;
        if (file != null && file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        } else {
            // file.createNewFile();
            LogUtil.e("获取文件大小 文件不存在!");
        }
        return size;
    }


    /**
     * 版本号比较
     */
    public static int compareVersion(String s1, String s2) {
        if (s1 == null && s2 == null)
            return 0;
        else if (s1 == null)
            return -1;
        else if (s2 == null)
            return 1;

        String[] arr1 = s1.split("[^a-zA-Z0-9]+"), arr2 = s2.split("[^a-zA-Z0-9]+");

        int i1, i2, i3;

        for (int ii = 0, max = Math.min(arr1.length, arr2.length); ii <= max; ii++) {
            if (ii == arr1.length)
                return ii == arr2.length ? 0 : -1;
            else if (ii == arr2.length)
                return 1;

            try {
                i1 = Integer.parseInt(arr1[ii]);
            } catch (Exception x) {
                i1 = Integer.MAX_VALUE;
            }

            try {
                i2 = Integer.parseInt(arr2[ii]);
            } catch (Exception x) {
                i2 = Integer.MAX_VALUE;
            }

            if (i1 != i2) {
                return i1 - i2;
            }

            i3 = arr1[ii].compareTo(arr2[ii]);

            if (i3 != 0)
                return i3;
        }

        return 0;
    }

    /**
     * 获取安装软件的版本号
     */
    public static String getAppVersionName(String pkg) {
        String versionName = "";
        try {
            PackageManager packageManager = MFBaseApplication.getInstance().getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(pkg, 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;

    }

    /**
     * URLEncoder.encode
     *
     * @param text
     * @return
     */
    public static String urlEncode(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        return URLEncoder.encode(text);
    }

    /**
     * URLEncoder.encode
     *
     * @param text
     * @return
     */
    public static String urlDecode(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        return URLDecoder.decode(text);
    }

    /**
     * 打开手机自动获取网络时间和时区的开关
     */
    public static void setSyncTime() {
        Context context = MFBaseApplication.getInstance();
        /* 设置自动获取网络时间和时区 */
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.AUTO_TIME, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.AUTO_TIME_ZONE, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static String getAndroidID(Context context) {
        try {
            return Secure.getString(context.getContentResolver(),
                    Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getCPUSerialno() {
        String cpuId = "";
        InputStreamReader ir = null;
        LineNumberReader input = null;
        try {
            Process proc = Runtime.getRuntime().exec("cat /proc/cpuinfo");
            if (proc == null) {
                return null;
            }
            ir = new InputStreamReader(proc.getInputStream());
            input = new LineNumberReader(ir);
            String str = null;
            while ((str = input.readLine()) != null) {
                cpuId = str.trim();// 去空格
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (ir != null) {
                try {
                    ir.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return cpuId;
    }

    public static String getUUID() {
        Context context = MFBaseApplication.getInstance();
        return MD5.md5("1_" + getAndroidID(context) + "2_" + getIMEI(context) + "3_"
                + getCPUSerialno());
    }

    public static final boolean isPureUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return url.startsWith("http:") || url.startsWith("https:");
    }


    private static String getUrl(String url, String param) {
        if (url.contains("?")) {
            url += ("&");
        } else {
            url += ("?");
        }
        return url + param;
    }


    /**
     * 对字符串进行Base64加密
     *
     * @param data
     * @return
     */
    public static String encodeByBase64(String data) {
        String base64 = "";
        if (!TextUtils.isEmpty(data)) {
            base64 = encodeByBase64(data.getBytes());
        }
        return base64;
    }

    public static String encodeByBase64(byte[] data) {
        String base64 = "";
        if (data != null && data.length > 0) {
            try {
                base64 = Base64.encodeToString(data, Base64.DEFAULT);
            } catch (Exception e) {
            }
        }
        return base64;
    }

    /**
     * 对字符串进行Base64解密
     *
     * @param data
     * @return
     */
    public static String decodeByBase64(String data) {
        byte[] bytes = decodeBase64(data);
        if (bytes != null && bytes.length > 0) {
            return new String(bytes);
        }
        return "";
    }

    public static byte[] decodeBase64(String data) {
        if (!TextUtils.isEmpty(data)) {
            try {
                return Base64.decode(data, Base64.DEFAULT);
            } catch (Exception e) {
            }
        }
        return null;
    }


    /**
     * 正则表达式验证手机号码
     */
    public static boolean isPhoneNum(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNumatcher = pattern.matcher(str);
        if (!isNumatcher.matches()) {
            return false;
        }
        if (TextUtils.isEmpty(str) || str.length() != 11 || !str.startsWith("1")) {
            return false;
        }
        return true;
    }

    /**
     * 保存资源图片到sd卡
     */
    public static String savePicture(String url, byte[] data) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (data == null || data.length == 0) {
            return null;
        }
        String imgFolder = AppUtils.getSDCardPath() + "imgs/";
        String fileName = MD5.md5(url);
        IOUtil.mkDir(imgFolder);
        IOUtil.saveFile(imgFolder + fileName, new ByteArrayInputStream(data));
        return fileName;
    }

    /**
     * 获取保存在sd卡上的资源图片
     */
    public static Bitmap getPicture(String url) {
        return getPicture(url, false);
    }

    /**
     * 获取保存在sd卡上的资源图片
     */
    public static Bitmap getPicture(String url, boolean isMd5) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        String fileName = isMd5 ? url : MD5.md5(url);
        return BitmapFactory.decodeFile(AppUtils.getSDCardPath() + "imgs/" + fileName);
    }

    /**
     * 获取sd卡上的资源图片是否存在
     */
    public static boolean isPictureExist(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        return IOUtil.isExists(AppUtils.getSDCardPath() + "imgs/" + MD5.md5(url));
    }

    /**
     * 获取sd卡上的资源图片是否存在
     */
    public static void deletePicture(String url) {
        deletePicture(url, false);
    }

    /**
     * 获取sd卡上的资源图片是否存在
     */
    public static void deletePicture(String url, boolean isMd5) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        String fileName = isMd5 ? url : MD5.md5(url);
        IOUtil.delete(AppUtils.getSDCardPath() + "imgs/" + fileName);
    }


    /**
     * 获取传入app的md5值
     *
     * @param apps 待获取md5的应用列表
     * @return 传入的app的md5值
     */
    public static String getAppMd5(List<ApplicationInfo> apps) {
        String result = "";
        if (apps != null && apps.size() > 0) {
            // 将app排序
            Collections.sort(apps, new ApplicationInfo.DisplayNameComparator(
                    MFBaseApplication.getInstance().getPackageManager()));

            // 取出app的package和name拼接并做md5
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < apps.size(); i++) {
                ApplicationInfo app = apps.get(i);
                stringBuilder.append(app.name);
                stringBuilder.append(app.packageName);
            }
            result = MD5.md5(stringBuilder.toString());
        }
        return result;
    }

}
