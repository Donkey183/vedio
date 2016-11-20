package com.app.basevideo.net.utils;


public class UriManager {
    private static final String TAG = UriManager.class.getSimpleName();

    /**
     * HTTP头,现阶段所有请求均走http协议,使用HTTP_HEAD即可,后续升级再做处理
     */

    public static final String HTTP_HEAD = "http://";
    public static final String HTTPS_HEAD = HTTP_HEAD;


    /**
     * 线上环境,正式发版环境,使用该环境需将{@link AppVersionConfig#VERSION_TYPE_USED}设置为{@link AppVersionConfig#VERSION_TYPE_ONLINE}
     */

    public static final String SERVER_DOMAIN_ONLINE = "www.baidu.com/";

    /**
     * 测试环境,预上线环境,使用该环境需将{@link AppVersionConfig#VERSION_TYPE_USED}设置为{@link AppVersionConfig#VERSION_TYPE_VERIFY}
     */

    public static final String SERVER_DOMAIN_VERIFY_V1 = "127.0.0.1:8090/";//测试环境1

    public static String SERVER_DOMAIN_VERIFY = SERVER_DOMAIN_VERIFY_V1;//在此配置使用的测试环境域名


    /**
     * 个人开发机测试环境,使用该环境需将{@link AppVersionConfig#VERSION_TYPE_USED}设置为{@link AppVersionConfig#VERSION_TYPE_DEBUG}
     */

    public static final String SERVER_DOMAIN_DEBUG_V1 = "127.0.0.1:8090/";//my开发机地址

    public static String SERVER_DOMAIN_DEBUG = SERVER_DOMAIN_DEBUG_V1;//在此配置使用的开发机地址


    /**
     * 获取基本Uri，包含请求头和域名
     * 具体接口地址配置见{InsuranceNetService},在 @GET 或 @POST 方法中添加接口地址
     */
    public static String getUriBase() {
        String uriBase = null;

        switch (AppVersionConfig.VERSION_TYPE_USED) {
            case AppVersionConfig.VERSION_TYPE_ONLINE:
                uriBase = HTTPS_HEAD + SERVER_DOMAIN_ONLINE;
                break;
            case AppVersionConfig.VERSION_TYPE_DEBUG:
                uriBase = HTTP_HEAD + SERVER_DOMAIN_DEBUG;
                break;
            case AppVersionConfig.VERSION_TYPE_VERIFY:
                uriBase = HTTP_HEAD + SERVER_DOMAIN_VERIFY;
                break;
        }
        return uriBase;

    }

    public static void resetAppVersionType(int versionType) {
        switch (versionType) {
            case AppVersionConfig.VERSION_TYPE_ONLINE:
                AppVersionConfig.VERSION_TYPE_USED = AppVersionConfig.VERSION_TYPE_ONLINE;
                break;
            case AppVersionConfig.VERSION_TYPE_DEBUG:
                AppVersionConfig.VERSION_TYPE_USED = AppVersionConfig.VERSION_TYPE_DEBUG;
                break;
            case AppVersionConfig.VERSION_TYPE_VERIFY:
                AppVersionConfig.VERSION_TYPE_USED = AppVersionConfig.VERSION_TYPE_VERIFY;
                break;
        }
    }
}
