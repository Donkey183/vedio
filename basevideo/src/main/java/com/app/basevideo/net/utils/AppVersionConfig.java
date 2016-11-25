package com.app.basevideo.net.utils;


public class AppVersionConfig {

    /**
     * 是否可以使用App配置功能
     */

    public final static boolean CAN_USE_APP_CONFIG = true;

    /**
     * App运行环境的定义,用于配置服务器地址
     * 详见{@link UriManager#getUriBase()}
     */

    public final static int VERSION_TYPE_ONLINE = 0x000001; // 正式版本，线上环境
    public final static int VERSION_TYPE_VERIFY = 0x000010; // 测试环境，预上线环境
    public final static int VERSION_TYPE_DEBUG = 0x000100; // 测试,用于个人开发机调试

    /**
     * App使用的环境
     */

    public static int VERSION_TYPE_USED = VERSION_TYPE_ONLINE;

    /**
     * 设置app使用的环境(线上、测试、开发机)
     *
     * @param versionType
     */
    public static void setVersionType(int versionType) {
        if (versionType == VERSION_TYPE_ONLINE || versionType == VERSION_TYPE_VERIFY || versionType == VERSION_TYPE_DEBUG) {
            VERSION_TYPE_USED = versionType;
        }
    }
}
