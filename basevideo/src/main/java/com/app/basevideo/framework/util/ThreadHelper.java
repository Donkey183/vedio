package com.app.basevideo.framework.util;

import android.os.Looper;

public class ThreadHelper {

    /**
     * 检查是否为主线程
     *
     * @throws Throwable
     */
    public static void checkMainThread() {
        boolean error = false;
        if (!isMainThread()) {
            error = true;
        }

        if (error == true) {
            StringBuilder buidler = new StringBuilder(100);
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            for (int i = 1; i < elements.length; i++) {
                buidler.append(elements[i].getClassName());
                buidler.append(".");
                buidler.append(elements[i].getMethodName());
                buidler.append("  lines = ");
                buidler.append(elements[i].getLineNumber());
                buidler.append("\n");
            }
            LogUtil.e("can not be call not thread! trace = \n" +
                    buidler.toString());
            throw new Error("can not be call not thread! trace = " +
                    buidler.toString());
        }
    }

    /**
     * 判断是否为主线程
     */
    public static boolean isMainThread() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
