package com.app.basevideo.framework.util;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

public class LogUtil {
    static private final String LOG_TAG = "DiDiFinanceLog";

    /**
     * 日志的class必须以此为开头才打印。
     */
    private static String LogFilter_classNameStartsWith = null;

    private static ArrayList<String> logPackage = new ArrayList<String>();

    public static void addLogPackage(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        if (logPackage.contains(packageName)) {
            return;
        }
        logPackage.add(packageName);
    }

    /**
     * 设置只打印以某个 class name 开头的日志。
     *
     * @param classNameStartsWith 传入null取消限制。
     */
    public static void setClassNameStartWithLogFilter(String classNameStartsWith) {
        LogFilter_classNameStartsWith = classNameStartsWith;
    }

    /**
     * 是否是debug模式。提前检测有利于减少不必须要的日志参数构造开销。
     */
    public static boolean isDebugMode() {
        //hasChange
//        if (InsuranceApplication.getInstance().getContext() == null) {
//            return false;
//        }
        return true;//InsuranceApplication.getInstance().isDebugMode();
    }

    /**
     * 打印Info信息
     *
     * @param className 类名
     * @param method    方法名
     * @param msg       消息
     * @return
     */
    static private void i(String className, String method, String msg) {
        String log = createMsg(true, className, method, msg);
        if (log != null) {
            Log.i(LOG_TAG, log);
        }
    }

    /**
     * 打印error信息
     *
     * @param className 类名
     * @param method    方法名
     * @param msg       消息
     * @return
     */
    static private void e(String className, String method, String msg) {
        String log = createMsg(false, className, method, msg);
        if (log != null) {
            Log.e(LOG_TAG, log);
        }
    }

    /**
     * 打印Warning信息
     *
     * @param className 类名
     * @param method    方法名
     * @param msg       消息
     * @return
     */
    static private void w(String className, String method, String msg) {
        String log = createMsg(false, className, method, msg);
        if (log != null) {
            Log.w(LOG_TAG, log);
        }
    }

    /**
     * 打印verbose信息
     *
     * @param className 类名
     * @param method    方法名
     * @param msg       消息
     * @return
     */
    static private void v(String className, String method, String msg) {
        String log = createMsg(true, className, method, msg);
        if (log != null) {
            Log.v(LOG_TAG, log);
        }
    }

    /**
     * 打印debug信息
     *
     * @param className 类名
     * @param method    方法名
     * @param msg       消息
     * @return
     */
    static private void d(String className, String method, String msg) {
        String log = createMsg(true, className, method, msg);
        if (log != null) {
            Log.d(LOG_TAG, log);
        }
    }

    static private String createMsg(boolean underPageackContrl, String className, String method, String msg) {
        if (isDebugMode()) {
            if (LogFilter_classNameStartsWith != null && !msg.startsWith(LogFilter_classNameStartsWith)) {
                return null;
            }

            if (underPageackContrl && !isLogable(className)) {
                return null;
            }

            StringBuffer fullMsg = new StringBuffer(100);
            fullMsg.append(className);
            fullMsg.append(":");
            fullMsg.append(method);
            fullMsg.append(":");
            fullMsg.append(msg);
            return fullMsg.toString();
        } else {
            return null;
        }
    }

    static public int printLog(int type, String msg) {
        if (isDebugMode()) {
            StackTraceElement[] elements = Thread.currentThread().getStackTrace();
            if (elements.length < 5) {
                return -1;
            }
            StackTraceElement element = elements[4];
            String methodName = element.getMethodName();
            String className = element.getClassName();
            if (type > 1 && !isLogable(className)) {
                return -1;
            }
            if (type == 0) {
                e(className, methodName, msg);
            } else if (type == 1) {
                w(className, methodName, msg);
            } else if (type == 2) {
                i(className, methodName, msg);
            } else if (type == 3) {
                d(className, methodName, msg);
            } else {
                v(className, methodName, msg);
            }
            return 0;
        } else {
            return -1;
        }
    }

    private static boolean isLogable(String className) {
        if (logPackage.size() == 0) {
            return false;
        }
        boolean isLog = false;
        for (String item : logPackage) {
            if (className.startsWith(item)) {
                isLog = true;
            }
        }
        return isLog;
    }

    static public int detailException(String msg, Throwable t) {
        if (isDebugMode() && t != null) {
            Log.e(LOG_TAG, msg, t);
        }

        return printLog(0, msg);
    }

    static public int detailException(Throwable t) {
        if (isDebugMode() && t != null) {
            Log.e(LOG_TAG, t.getMessage(), t);
            return printLog(0, t.getMessage());
        }

        return -1;
    }

    static public int e(Throwable e) {
        return printLog(0, e.getMessage());
    }

    static public int e(String msg) {
        return printLog(0, msg);
    }

    static public int w(String msg) {
        return printLog(1, msg);
    }

    static public int i(String msg) {
        return printLog(2, msg);
    }

    static public int d(String msg) {
        return printLog(3, msg);
    }

    static public int v(String msg) {
        return printLog(4, msg);
    }
}
