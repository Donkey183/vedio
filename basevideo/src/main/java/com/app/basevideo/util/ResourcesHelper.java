package com.app.basevideo.util;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.app.basevideo.base.MFBaseApplication;


/**
 * 获取资源
 * 
 */

public class ResourcesHelper {
    public static int getColor(int rid) {
        return MFBaseApplication.getInstance().getResources().getColor(rid);
    }

    public static ColorStateList getColorStateList(int rid) {
        return MFBaseApplication.getInstance().getResources().getColorStateList(rid);
    }

    public static String getString(int rid) {
        return MFBaseApplication.getInstance().getResources().getString(rid);
    }

    public static String getString(int rid, int param1, int param2) {
        return MFBaseApplication.getInstance().getResources().getString(rid, param1, param2);
    }

    public static String getString(int rid, String str) {
        return MFBaseApplication.getInstance().getResources().getString(rid, str);
    }

    public static String[] getStringArray(int rid) {
        return MFBaseApplication.getInstance().getResources().getStringArray(rid);
    }

    public static Drawable getDrawable(int rid) {
        return MFBaseApplication.getInstance().getResources().getDrawable(rid);
    }

    public static float getDimension(int rid) {
        return MFBaseApplication.getInstance().getResources().getDimension(rid);
    }

    public static DisplayMetrics getDisplayMetrics() {
        return MFBaseApplication.getInstance().getResources().getDisplayMetrics();
    }

    public static int getDisplayMetrics(int x) {
        return MFBaseApplication.getInstance().getResources().getDimensionPixelSize(x);
    }

    public static int getDimensionPixelSize(int x) {
        return MFBaseApplication.getInstance().getResources().getDimensionPixelSize(x);
    }

    public static XmlResourceParser getXml(int rid) {
        return MFBaseApplication.getInstance().getResources().getXml(rid);
    }

    public static Configuration getConfiguration() {
        return MFBaseApplication.getInstance().getResources().getConfiguration();
    }
}
