package com.app.basevideo.util;

import android.os.Build;

public class InsuranceVersionHelper {
    public static boolean hasThanVersion19() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasThanVersion21() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
