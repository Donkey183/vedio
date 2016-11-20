package com.app.basevideo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.app.basevideo.base.MFBaseApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 网络状态管理工具类，单例 {@link #getInstance()}模式，对外提供各种网络状态的方法,
 * <p>
 * <p>
 * <h3>作用</h3>
 * 主要有2方面的作用 *
 * <ul>
 * <li>全局只注册一个监控网络变化的广播接收器（android.net.conn.CONNECTIVITY_CHANGE）；
 * <li>网络状态的所有方法收敛到这个工具类中，
 * </ul>
 * </p>
 * <p>
 * <h3>注意事项</h3>
 * <ul>
 * <li>现有消息机制中，多进程中互相收不到消息，所以对非主进程，需要监控的网络变化的地方需要自己注册广播监听器，不能接收主进程Custom消息；
 * <li>目的是收敛，禁止业务层再自己写网络状态的相关方法，必须统一使用这里的方法
 * </ul>
 * </p>
 */
public class NetTypeUtil {

    public static final String NATION_CODE = "460";

    /**
     * 网络类型枚举：粗略类型，WIFI/2G/3G/4G
     */
    public static final int NET_TPYE_UNAVAILABLE = 0;
    public static final int NET_TYPE_WIFI = 1;
    public static final int NET_TYPE_2G = 2;
    public static final int NET_TYPE_3G = 3;
    public static final int NET_TYPE_4G = 4;

    /**
     * 网络类型名
     */
    public static final String NET_TYPENAME_WIFI = "wifi";
    public static final String NET_TYPENAME_2G = "2g";
    public static final String NET_TYPENAME_3G = "3g";
    public static final String NET_TYPENAME_4G = "4g";
    public static final String NET_TYPENAME_UNAVAILABLE = "unreachable";

    /**
     * 运营商
     */
    public static final int NETWORK_OPERATOR_UNKOWN = 0;
    public static final int NETWORK_OPERATOR_MOBILE = 1; // 移动
    public static final int NETWORK_OPERATOR_UNICOM = 2; // 联通
    public static final int NETWORK_OPERATOR_TELECOM = 3; // 电信

    /**
     * 具体的移动网络分类:Unknown,2G,3G,4G
     */
    public static final int NETWORK_CLASS_UNKNOWN = 0;
    /**
     * Class of broadly defined "2G" networks. {@hide}
     */
    public static final int NETWORK_CLASS_2_G = 1;
    /**
     * Class of broadly defined "3G" networks. {@hide}
     */
    public static final int NETWORK_CLASS_3_G = 2;
    /**
     * Class of broadly defined "4G" networks. {@hide}
     */
    public static final int NETWORK_CLASS_4_G = 3;

    /**
     * 网络具体的子类型
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     */
    public static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    public static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    public static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    public static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    public static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    public static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    public static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    public static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    public static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     */
    public static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    public static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    public static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    public static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     */
    public static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     */
    public static final int NETWORK_TYPE_HSPAP = 15;

    static private Pattern mPattern = Pattern.compile("^[0]{0,1}10\\.[0]{1,3}\\.[0]{1,3}\\.(172|200)$", Pattern.MULTILINE);

    /**
     * 当前的网络状态信息
     */
    private NetworkInfo curNetworkInfo = null;

    /**
     * 是否是Wifi
     */
    private boolean isWifi = true;

    /**
     * 是否是Mobile
     */
    private boolean isMobile = false;

    /**
     * 网络是否可用
     */
    private boolean isNetAvailable = true;

    /**
     * 当前移动网络具体类型
     */
    private int curMobileNetDetailType = NetTypeUtil.NETWORK_TYPE_UNKNOWN;

    /**
     * 移动网络分类:2G/3G/4G
     */
    private int curMobileNetClassify = NetTypeUtil.NETWORK_CLASS_UNKNOWN;

    /**
     * 运营商相关的信息
     */
    private int operatorType = -1;

    /**
     * 移动网络代理Host
     */
    private String mProxyHost = null;

    /**
     * 移动网络代理端口号
     */
    private int mProxyPort = -1;
    /**
     * 是否支持WAP
     */
    private static boolean mSupportWap = true;

    /**
     * 网络改变的时间
     */
    private long mNetChangedTime;

    /**
     * 网络改变消息是否打开
     */
    private boolean isOpenNetChangedMessage = true;

    /**
     * 获取当前的网络状态
     */
    private void getCurNetState() {
        NetworkInfo activeNetInfo = getActiveNetworkInfo();
        if (null != activeNetInfo) {

            curNetworkInfo = activeNetInfo;

            if (activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifi = true;
                isMobile = false;
            } else if (activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isWifi = false;
                isMobile = true;
            } else {
                isWifi = false;
                isMobile = false;
            }

            isNetAvailable = true;
            curMobileNetDetailType = activeNetInfo.getSubtype();
            if (isMobile) {
                curMobileNetClassify = getNetworkClass(curMobileNetDetailType);
            } else {
                curMobileNetClassify = NETWORK_CLASS_UNKNOWN;
            }

        } else {
            isWifi = false;
            isMobile = false;
            isNetAvailable = false;
            curMobileNetDetailType = NetTypeUtil.NETWORK_TYPE_UNKNOWN;
            curMobileNetDetailType = NetTypeUtil.NETWORK_CLASS_UNKNOWN;
        }

        // 运营商
        operatorType = readNetworkOperatorType();

        // 移动网络代理Host
        mProxyHost = android.net.Proxy.getDefaultHost();

        // 移动网络代理Port
        mProxyPort = android.net.Proxy.getDefaultPort();
    }

    /**
     * 获取当前可用的网络信息
     */
    private NetworkInfo getActiveNetworkInfo() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) MFBaseApplication.getInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

            return activeNetInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * <font color=red>立即</font>判断网络是否可用
     * <p>
     * <p>
     * 场景：本类提供的获取网络是否可用依赖网络切换广播的监听，有些情况下广播会延时2s，而业务层某些场景下如用户点击网络某些操作，
     * 需要立即获取网络是否可用
     * </p>
     *
     * @return
     */
    public static boolean isNetworkAvailableForImmediately() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) MFBaseApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

                if (networkInfo != null && networkInfo.length > 0) {
                    for (int i = 0; i < networkInfo.length; i++) {
                        if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNetAvailable() {
        if (curNetworkInfo == null) {
            getCurNetState();
        }

        return isNetAvailable;
    }

    public NetworkInfo getCurNetworkInfo() {
        if (curNetworkInfo == null) {
            getCurNetState();
        }

        return curNetworkInfo;
    }

    public boolean isWifi() {
        if (curNetworkInfo == null) {
            getCurNetState();
        }

        return isWifi;
    }

    public boolean isMobile() {
        if (curNetworkInfo == null) {
            getCurNetState();
        }
        return isMobile;
    }

    public int getCurMobileNetClassify() {
        if (curNetworkInfo == null) {
            getCurNetState();
        }

        return curMobileNetClassify;
    }

    public int getCurMobileNetDetailType() {
        if (curNetworkInfo == null) {
            getCurNetState();
        }

        return curMobileNetDetailType;
    }

    public int getOperatorType() {
        if (operatorType == -1) {
            try {
                operatorType = readNetworkOperatorType();
            } catch (Exception e) {
                operatorType = NetTypeUtil.NETWORK_OPERATOR_UNKOWN;
            }
        }

        return operatorType;
    }

    /**
     * 移动网络代理Host
     */
    public String getProxyHost() {
        if (null == mProxyHost) {
            mProxyHost = android.net.Proxy.getDefaultHost();
        }

        return mProxyHost;
    }

    // 获取网络变化时间
    private long geNetworkChangedTime() {
        return mNetChangedTime;
    }

    private void setNetworkChangedTime(long netChangedTime) {
        mNetChangedTime = netChangedTime;
    }

    /**
     * 在建立连接时是否使用代理(移动网络适用)
     *
     * @return
     */
    public static boolean isPorxyUsed() {
        if (NetTypeUtil.getInstance().isWifi) {
            return false;
        }

        // 不管cmnet和cmwap都选择直连方式
        if (readNetworkOperatorType() == NETWORK_OPERATOR_MOBILE) {
            return false;
        }

        String proxyHost = android.net.Proxy.getDefaultHost();
        if (StringHelper.isEmptyStringAfterTrim(proxyHost)) {
            return false;
        }

        return true;
    }

    /**
     * 读取运营商类型 MCC+MNC(mobile country code + mobile network code)
     * FIXME:CDMA可能会不好使. 参考：http://www.2cto.com/kf/201212/180259.html
     */
    private static int readNetworkOperatorType() {
        TelephonyManager telManager = (TelephonyManager) MFBaseApplication.getInstance()
                .getSystemService(Context.TELEPHONY_SERVICE);
        String operator = telManager.getNetworkOperator();

        if (operator == null || operator.length() < 4 || StringHelper.isEmptyStringAfterTrim(operator)) {
            return NETWORK_OPERATOR_UNKOWN;
        }

        // 非中国运营商
        String mcc = operator.substring(0, 3);
        if (mcc == null || !mcc.equals(NATION_CODE)) {
            return NETWORK_OPERATOR_UNKOWN;
        }

        // operator 由mcc + mnc组成 中国的mcc为460
        // 这里取得mnc来判断是国内的哪个运营商
        String mnc = operator.substring(3);
        int mncIntVar = 0;
        try {
            mncIntVar = Integer.parseInt(mnc);
        } catch (NumberFormatException e) {
        }

        switch (mncIntVar) {
            case 0:
            case 2:
            case 7:
                return NETWORK_OPERATOR_MOBILE;
            case 1:
            case 6:
                return NETWORK_OPERATOR_UNICOM;
            case 3:
            case 5:
                return NETWORK_OPERATOR_TELECOM;
            default:
                break;
        }

        return NETWORK_OPERATOR_UNKOWN;
    }

    /**
     * 根据具体的移动网络类型获取网络分类:2G/3G/4G
     */
    public static int getNetworkClass(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:

                return NETWORK_CLASS_2_G;

            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:

                return NETWORK_CLASS_3_G;

            case NETWORK_TYPE_LTE:

                return NETWORK_CLASS_4_G;

            default:

                return NET_TYPE_4G;
        }
    }

    /**
     * 网络代理Port
     */
    public int getProxyPort() {
        if (-1 == mProxyPort) {
            mProxyPort = android.net.Proxy.getDefaultPort();
        }

        return mProxyPort;
    }

    public boolean isOpenNetChangedMessage() {
        return isOpenNetChangedMessage;
    }

    public void setOpenNetChangedMessage(boolean isOpenNetMessage) {
        this.isOpenNetChangedMessage = isOpenNetMessage;
    }

    private static NetTypeUtil mInstance = null;

    private static synchronized NetTypeUtil getInstance() {
        if (mInstance == null) {
            mInstance = new NetTypeUtil();
        }

        return mInstance;
    }


    /** ~*~*~*~*~*~*~*~*~*~*~*~*~* 以下为向外提供的接口 ~*~*~*~*~*~**~*~**~*~*~*~*~*~ */

    /**
     * 初始化工具类
     */
    public static void init() {
        init(true);

    }

    /**
     * 工具类初始化
     *
     * @param isOpenNetMessage 是否开启分发网络状态变化的Custom消息，默认是开启
     */
    public static void init(boolean isOpenNetMessage) {
        NetTypeUtil.getInstance().setOpenNetChangedMessage(isOpenNetMessage);
        NetTypeUtil.getInstance().getCurNetState();

    }

    // 当前网络是否可用
    public static boolean isNetWorkAvailable() {
        return NetTypeUtil.getInstance().isNetAvailable();
    }

    // 当前是否是WIFI网络
    public static boolean isWifiNet() {
        return NetTypeUtil.getInstance().isWifi();
    }

    // 当前是否是移动网络
    public static boolean isMobileNet() {
        return NetTypeUtil.getInstance().isMobile();
    }

    // 当前是否是4G网络
    public static boolean is4GNet() {
        return NETWORK_CLASS_4_G == NetTypeUtil.getInstance().getCurMobileNetClassify();
    }

    // 当前是否是3G网络
    public static boolean is3GNet() {
        return NETWORK_CLASS_3_G == NetTypeUtil.getInstance().getCurMobileNetClassify();
    }

    // 当前是否是2G网络
    public static boolean is2GNet() {
        return NETWORK_CLASS_2_G == NetTypeUtil.getInstance().getCurMobileNetClassify();
    }

    // 获取网络类型
    public static int netType() {
        if (NetTypeUtil.isWifiNet()) {
            return NetTypeUtil.NET_TYPE_WIFI;
        } else if (NetTypeUtil.is2GNet()) {
            return NetTypeUtil.NET_TYPE_2G;
        } else if (NetTypeUtil.is3GNet()) {
            return NetTypeUtil.NET_TYPE_3G;
        } else if (NetTypeUtil.is4GNet()) {
            return NetTypeUtil.NET_TYPE_4G;
        }

        if (!NetTypeUtil.isNetWorkAvailable()) {
            return NetTypeUtil.NET_TPYE_UNAVAILABLE;
        }

        return NET_TYPE_4G;
    }

    // 小写的网络名称:wifi/2g/3g/4g
    public static String netTypeNameInLowerCase() {
        int netType = NetTypeUtil.netType();
        switch (netType) {
            case NetTypeUtil.NET_TYPE_WIFI:
                return NET_TYPENAME_WIFI;

            case NetTypeUtil.NET_TYPE_4G:
                return NET_TYPENAME_4G;

            case NetTypeUtil.NET_TYPE_3G:
                return NET_TYPENAME_3G;

            case NetTypeUtil.NET_TYPE_2G:
                return NET_TYPENAME_2G;

            default:
                return NET_TYPENAME_UNAVAILABLE;
        }
    }

    /**
     * 大写的网络名称:WIFI,2G,3G,4G
     */
    public static String netTypeNameInUpperCase() {
        String netTypeName = netTypeNameInLowerCase();
        if (null != netTypeName) {
            netTypeName = netTypeName.toUpperCase();
        }

        return netTypeName;
    }

    // 获取运营商类型
    public static int curOperatorType() {
        return NetTypeUtil.getInstance().getOperatorType();
    }

    // 获取移动网络代理Host
    public static String curMobileProxyHost() {
        return NetTypeUtil.getInstance().getProxyHost();
    }

    // 获取移动网络代理端口
    public static int curMobileProxyPort() {
        return NetTypeUtil.getInstance().getProxyPort();
    }

    /**
     * 是否支持wap
     *
     * @return
     */
    public static boolean isSupportWap() {
        return mSupportWap;
    }

    /**
     * 设置是否支持wap
     *
     * @param supportWap
     */
    public static void setSupportWap(boolean supportWap) {
        mSupportWap = supportWap;
    }

    /**
     * 获取是否是Wap网络 移动和联通的wap 10.0.0.172,电信是ctwap 10.0.0.200
     */
    public static boolean isWap(String proxyhost) {

        boolean ret = false;
        Matcher m = mPattern.matcher(proxyhost);
        if (m.find()) {
            ret = true;
        } else {
            ret = false;
        }
        return ret;
    }

    /**
     * 是否是isWap类型
     *
     * @return
     */
    public static boolean isWap() {
        NetworkInfo networkInfo = NetTypeUtil.getInstance().getActiveNetworkInfo();
        if (null == networkInfo) {
            return false;
        }
        if (networkInfo.getExtraInfo() != null && networkInfo.getExtraInfo().contains("wap")) {
            return true;
        }
        return false;
    }

    // 获取网络变化时间
    public static long getNetChangedTime() {
        return NetTypeUtil.getInstance().geNetworkChangedTime();
    }
}
