package com.app.basevideo.config;

public class VideoConstant {
    /**
     * 通用网络请求参数
     */

    public static final String PARAM_TOKEN = "token";
    public static final String PARAM_APP_VERSION = "appVersion";//当前App版本号
    public static final String PARAM_IMEI = "imei";
    public static final String PARAM_LAT = "lat";
    public static final String PARAM_LNG = "lng";
    public static final String PARAM_OS_TYPE = "osType";//操作系统类型（Android、iOS）
    public static final String PARAM_OS_VERSION = "osVersion";//操作系统版本号
    public static final String PARAM_MOBILE_BRAND = "mobileBrand";//手机品牌
    public static final String PARAM_PHONE_MODEL = "phoneModel";//手机型号（例如：魅族MX6）
    public static final String PARAM_NET_TYPE = "netType";//网络类型（2G/3G/4G/WIFI）
    public static final String PARAM_CLIENT_TYPE = "clientType";//客户端类型
    public static final String PARAM_SCREEN_WIDTH = "screenWidth";//屏幕宽度
    public static final String PARAM_SCREEN_HEIGHT = "screenHeight";//屏幕高度
    public static final String PARAM_TIMESTAMP = "stamp";//时间戳
    public static final String PARAM_USER_ID = "userId";//用户Id
    public static final String PARAM_SIGN = "sign";//签名
    public static final String PARAM_MOBILE_OPERATORS = "mobileOperators";//运营商
    public static final String ENCY_STR = "encyStr";//加密串

    /**
     * 网络异常状态
     */
    public static final int NET_ERROR_TOKEN_INVAID = 999996;
    public static final int NET_ERROR_TOKEN_INVAID_LOGIN = 400;

    /**
     * App 配置信息
     */
    public static final String CONFIG_IS_ONLY_GET_TOKEN = "config_is_only_get_token";

    /**
     * 用户信息
     */
    public static final String BEAN_USER_INFO = "bean_user_info";//用户信息

}
