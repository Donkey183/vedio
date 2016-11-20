package com.app.basevideo.util;

import android.content.Context;

public class DensityUtil {
    private static final int ERROR_DEFAULT = 0;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        if (context == null) {
            return ERROR_DEFAULT;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        if (context == null) {
            return ERROR_DEFAULT;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        if (scale == 0) {
            return ERROR_DEFAULT;
        }
        return (int) (pxValue / scale + 0.5f);
    }
}
