package com.app.basevideo.util;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.DisplayMetrics;

/**
 * 设备工具类
 */
public class DeviceUtil {

    /**
     * SD卡是否已挂载
     *
     * @return
     */
    public static boolean isSdcardMounted() {
        try {
            String status = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equalsIgnoreCase(status)
                    || Environment.MEDIA_CHECKING.equalsIgnoreCase(status)
                    || Environment.MEDIA_MOUNTED_READ_ONLY.equalsIgnoreCase(status)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取设备是否是小米手机
     *
     * @return
     */
    public static boolean isMIPhone() {
        String manufactrue = android.os.Build.MANUFACTURER;
        String model = android.os.Build.MODEL;
        if ("xiaomi".equalsIgnoreCase(manufactrue)) {
            return true;
        } else {
            if (model.startsWith("MI") || model.startsWith("mi")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取设备定制UA
     *
     * @param context
     * @return
     */
    public static String getUA(Context context) {
        String vn = "";
        String vc = "";
        try {
            PackageManager manager = context.getPackageManager();
            if (manager != null) {
                PackageInfo info;
                info = manager.getPackageInfo(context.getPackageName(), 0);
                if (info != null) {
                    vn = info.versionName;
                    vc = info.versionCode + "";
                }
            }
        } catch (Exception e) {
            if (vn == null) {
                vn = "";
            }
        }
        // ua=bfbsdk_720_1200_15_4.0.4Lenovo-K860-stuttgart_1.0_1
        DisplayMetrics displayer = context.getResources().getDisplayMetrics();
        StringBuilder sb = new StringBuilder();
        sb.append("_didigsui");
        sb.append('_');
        sb.append(displayer.widthPixels);
        sb.append('_');
        sb.append(displayer.heightPixels);
        sb.append('_');
        String str = android.os.Build.MODEL + '-' + android.os.Build.DEVICE;
        str = str.replace(' ', '-');
        str = str.replace('_', '-');
        sb.append(str);
        sb.append('_');
        sb.append(android.os.Build.VERSION.SDK);
        sb.append('_');
        sb.append(android.os.Build.VERSION.RELEASE);
        sb.append('_');
        sb.append(vn);
        sb.append('_');
        sb.append(vc);
        return sb.toString();
    }
}
