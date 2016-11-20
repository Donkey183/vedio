package com.app.basevideo.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.TouchDelegate;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.app.basevideo.R;
import com.app.basevideo.base.MFBaseApplication;
import com.app.basevideo.framework.util.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UtilHelper {

    static boolean deviceDataInited = false;

    private static float displayMetricsDensity;
    static int displayMetricsWidthPixels;
    static int displayMetricsHeightPixels;

    public static void initDeviceData(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metric);
        int orientation = manager.getDefaultDisplay().getOrientation();
        if (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270) {
            displayMetricsWidthPixels = metric.heightPixels; // 屏幕宽度（像素）
            displayMetricsHeightPixels = metric.widthPixels; // 屏幕高度（像素）
        } else {
            displayMetricsWidthPixels = metric.widthPixels; // 屏幕宽度（像素）
            displayMetricsHeightPixels = metric.heightPixels; // 屏幕高度（像素）
        }
        displayMetricsDensity = metric.density; // 屏幕密度（0.75 / 1.0 / 1.5）
        deviceDataInited = true;
    }

    public static int getEquipmentWidth(Context context) {
        if (!UtilHelper.deviceDataInited) {
            UtilHelper.initDeviceData(context);
        }

        return UtilHelper.displayMetricsWidthPixels;
    }

    public static int getEquipmentHeight(Context context) {
        if (!UtilHelper.deviceDataInited) {
            UtilHelper.initDeviceData(context);
        }

        return UtilHelper.displayMetricsHeightPixels;
    }

    public static int dip2px(Context context, float dipValue) {
        if (!deviceDataInited) {
            initDeviceData(context);
        }

        return (int) (dipValue * displayMetricsDensity + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        if (!deviceDataInited) {
            initDeviceData(context);
        }

        return (int) (pxValue / displayMetricsDensity + 0.5f);
    }

    public static float getEquipmentDensity(Context context) {
        if (!deviceDataInited) {
            initDeviceData(context);
        }

        return displayMetricsDensity;
    }

    /**
     * 全局只保留一个Toast对象，要不然多个Toast会叠在一起
     */
    private static Toast mToast = null;
    private static ICustomToastView mToastView = null;
    private static String mOldMsg;
    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mToast != null) {
                mToast.cancel();
            }
        }
    };
    private static final int TOAST_SHORT = 2000;
    private static final int TOAST_LONG = 3500;

    /**
     * 把Toast置为null,不然引起Activity内存泄露
     */
    public static void clearToast() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        mToast = null;
    }

    /**
     * 自定义toast
     *
     */
    public interface ICustomToastView {
        /**
         * 设置string到自定义的toastview中去。
         *
         * @param str
         */
        public void setToastString(String str);

        /**
         * 返回自定义的toastView
         *
         * @return
         */
        public View getToastContentView();

    }

    /**
     * 显示一个Toast，可自定义时间
     *
     * @param context
     * @param str
     * @param duration
     */
    public static void showToast(Context context, String str, int duration) {
        if (!TextUtils.isEmpty(str)) {
            mHandler.removeCallbacks(mRunnable);
            if (mToast == null) {
                if (mToastView == null || mToastView.getToastContentView() == null) {
                    mToast = Toast.makeText(MFBaseApplication.getInstance(),
                            str, Toast.LENGTH_SHORT);
                } else {
                    mToast = new Toast(MFBaseApplication.getInstance());
                    mToast.setDuration(Toast.LENGTH_SHORT);
                    mToastView.setToastString(str);
                    mToast.setView(mToastView.getToastContentView());
                }

                int y_offset = dip2px(MFBaseApplication.getInstance(), 100);
                mToast.setGravity(Gravity.CENTER, 0, y_offset);
            } else {
                if (!str.equals(mOldMsg)) {
                    if (mToastView == null || mToastView.getToastContentView() == null) {
                        mToast.setText(str);
                    } else {
                        mToastView.setToastString(str);
                    }
                }
            }
            mOldMsg = str;
            mHandler.postDelayed(mRunnable, duration);
            mToast.show();
        }
    }

    public static void showToast(Context context, String str) {
        showToast(context, str, TOAST_SHORT);
    }

    public static void showToast(Context context, int stringId) {
        String str = context.getResources().getString(stringId);
        showToast(context, str);
    }

    public static void showLongToast(Context context, String str) {
        showToast(context, str, TOAST_LONG);
    }

    public static void showLongToast(Context context, int stringId) {
        String str = context.getResources().getString(stringId);
        showLongToast(context, str);
    }

    /**
     * 显示等待对话框
     *
     * @param string 对话框文字
     */
    public static ProgressDialog showLoadingDialog(Context context, String string) {
        ProgressDialog mWaitingDialog = null;
        OnCancelListener mDialogListener = new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }

        };
        if (string != null) {
            mWaitingDialog = ProgressDialog.show(context, "", string, true, false,
                    mDialogListener);
        } else {
            mWaitingDialog = ProgressDialog.show(context, "", context.getResources()
                    .getString(R.string.loading), true, false, mDialogListener);
        }

        return mWaitingDialog;
    }

    /**
     * 显示等待对话框
     *
     * @param string   对话框文字
     * @param listener 监听取消事件
     */
    public static ProgressDialog showLoadingDialog(Context context, String string, OnCancelListener listener) {
        ProgressDialog mWaitingDialog = null;
        if (string != null) {
            mWaitingDialog = ProgressDialog.show(context, "", string, true, true,
                    listener);
        } else {
            mWaitingDialog = ProgressDialog.show(context, "", context.getResources()
                    .getString(R.string.loading), true, true, listener);
        }

        return mWaitingDialog;
    }

    /**
     * 关闭当前等待对话空
     */
    public static void closeLoadingDialog(ProgressDialog mWaitingDialog) {
        if (mWaitingDialog != null) {
            try {
                if (mWaitingDialog.isShowing()) {
                    mWaitingDialog.dismiss();
                }
            } catch (Exception ex) {
                LogUtil.e(ex.getMessage());
            }
            mWaitingDialog = null;
        }
    }

    public static void hideSoftKeyPad(Context context, View view) {
        try {
            if (view == null) {
                return;
            } else if (view.getWindowToken() == null) {
                return;
            }

            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Throwable ex) {
            LogUtil.e(ex.getMessage());
        }
    }

    public static void showSoftKeyPad(Context context, View view) {
        try {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        } catch (Throwable ex) {
            LogUtil.e(ex.getMessage());
        }
    }

    public static int getStatusBarHeight(Activity activity) {
        int height = 0;
        Rect rect = new Rect();
        Window window = activity.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rect);
        height = rect.top;
        if (height == 0) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass
                        .getField("status_bar_height").get(localObject)
                        .toString());
                height = activity.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return height;
    }

    public static int[] getScreenDimensions(Context context) {
        int[] dimensions = new int[2];
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        dimensions[0] = display.getWidth();
        dimensions[1] = display.getHeight();
        return dimensions;
    }

    /**
     * 获取一个对象隐藏的属性，并设置属性为public属性允许直接访问�?
     *
     * @return {@link Field} 如果无法读取，返回null；返回的Field需要使用者自己缓存，本方法不做缓存�?
     */
    public static Field getDeclaredField(Object object, String field_name) {
        Class<?> cla = object.getClass();
        Field field = null;
        for (; cla != Object.class; cla = cla.getSuperclass()) {
            try {
                field = cla.getDeclaredField(field_name);
                field.setAccessible(true);
                return field;
            } catch (Exception e) {

            }
        }
        return null;
    }

    public static boolean isGif(byte[] data) {
        boolean isGif = false;
        if (data == null || data.length < 3) {
            return false;
        }
        if (data[0] == 'G' && data[1] == 'I' && data[2] == 'F') {
            isGif = true;
        }

        return isGif;
    }

    /**
     * Check if the byte verifyData is webp format or not.
     *
     * @param data byte verifyData to be checked
     * @return true for webp
     */
    public static boolean isDataWebpFormat(byte[] data) {
        boolean ret = false;
        if (data == null) {
            return false;
        }
        try {
            // The first 16 bytes
            final String tag = new String(data, 0, 16, "UTF-8");
            if ((tag != null) && (0 == tag.indexOf("RIFF")) && (8 == tag.indexOf("WEBPVP8 "))) {
                ret = true;
            }
        } catch (Exception ex) {
            ret = false;
        }
        return ret;
    }

    public static DisplayMetrics getScreenSize(Activity activity) {
        DisplayMetrics result = null;
        try {
            result = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(result);
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
        return result;
    }

    // 1. 粗略计算文字宽度
    public static float measureTextWidth(Paint paint, String str) {
        if (paint == null || str == null) {
            return 0;
        }
        return paint.measureText(str);
    }

    // 2. 计算文字所在矩形，可以得到宽高
    public static Rect measureText(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect;
    }

    // 3. 精确计算文字宽度
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    public static String getTextOmit(TextPaint paint, String str, int width) {
        String des = null;
        CharSequence sequence = TextUtils.ellipsize(str, paint, width, TruncateAt.END);
        if (sequence != null) {
            des = sequence.toString();
        }
        return des;
    }

    public static TextPaint setTextSize(Context c, TextPaint paint, float size) {
        Resources r;
        if (c == null) {
            r = Resources.getSystem();
        } else {
            r = c.getResources();
        }
        if (r != null) {
            paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, r.getDisplayMetrics()));
        }
        return paint;
    }

    public static int getFontHeight(Context context, float fontSize) {
        TextPaint paint = new TextPaint();
        setTextSize(context, paint, fontSize);
        FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    public static void setWindowAlpha(Activity activity, float alpha) {
        LayoutParams params = activity.getWindow().getAttributes();
        // params.alpha = alpha;
        params.screenBrightness = alpha;
        activity.getWindow().setAttributes(params);
    }

    public static int getScreenBrightness(Activity activity) {
        int screenBrightness = 0;
        try {
            screenBrightness = android.provider.Settings.System.getInt(activity.getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception ex) {
            LogUtil.e(ex.getMessage());
        }
        return screenBrightness;
    }

    public static String urlAddParam(String url, String param) {
        if (url == null || param == null) {
            return url;
        }
        if (url.indexOf("?") < 0) {
            url += "?";
        } else if (!url.endsWith("?") && !url.endsWith("&")) {
            url += "&";
        }
        url += param;
        return url;
    }

    public static void share(Context context, String st_type, String content, File image) {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND, null);
            intent.addCategory(Intent.CATEGORY_DEFAULT);

            if (content.length() > 140) {
                content = content.substring(0, 140);
            }

            intent.putExtra(Intent.EXTRA_TEXT, content);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setType("text/plain");

            // 添加图片
            if (image != null && image.exists()) {
                Uri u = Uri.fromFile(image);
                intent.putExtra(Intent.EXTRA_STREAM, u);
                intent.setType("image/*");
            }
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
    }

    public static int[] getImageResize(int width, int height, int maxWidth, int maxHeight) {
        if (width <= 0 || height <= 0 || maxWidth <= 0 || maxHeight <= 0) {
            return null;
        }
        int[] size = new int[2];

        if (height > maxHeight) {
            width = width * maxHeight / height;
            height = maxHeight;
        }
        if (width > maxWidth) {
            height = height * maxWidth / width;
            width = maxWidth;
        }
        size[0] = width;
        size[1] = height;
        return size;
    }

    /* begin 获取两个经纬度间的距离，单位公里 */
    private final static double EARTH_RADIUS = 6378.137;// 地球半径

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 获取当前天为一年中的某一天
     *
     * @return
     */
    public static int getCurrentDay() {

        Calendar c = Calendar.getInstance(Locale.CHINA);
        int curDay = c.get(Calendar.DAY_OF_YEAR);

        return curDay;
    }

    public static int getDimens(Context context, int dimenId) {

        return context.getResources().getDimensionPixelSize(dimenId);

    }

    // 获取两个经纬度间的距离，单位公里
    public static double GetDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2.0), 2) + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000.0;
        return s;
    }

	/* end 获取两个经纬度间的距离，单位公里 */

    // 检测客户端是否安装某个app
    public static boolean hasInstallApp(Context context, String packageName) {
        if (packageName == null || packageName.length() == 0) {
            return false;
        }

        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        // 从pinfo中将包名字逐一取出，压入pName list中
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void addShortcut(Context context, String appName, String packageName, String className, int iconResId) {
        Intent target = new Intent();
        target.addCategory(Intent.CATEGORY_LAUNCHER);
        target.setAction(Intent.ACTION_MAIN);
        ComponentName comp = new ComponentName(packageName, className);
        target.setComponent(comp);

        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 快捷方式的名称
        shortcut.putExtra("duplicate", false);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, target);

        // 快捷方式的图标
        ShortcutIconResource iconRes = ShortcutIconResource.fromContext(context, iconResId);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        context.sendBroadcast(shortcut);
    }

    /**
     * 检查是否为主线程
     *
     * @throws Throwable
     */
    public static void checkMainThread() {
        if (MFBaseApplication.getInstance().isDebugMode()) {
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

    /**
     * 判断当前网络是否可用
     *
     * @return
     */
    public static boolean isNetOk() {
        return NetTypeUtil.isNetWorkAvailable();
    }

    /**
     * 扩大view的点击区域，不会超过父view
     *
     * @param context
     * @param view
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public static void addToParentArea(Context context, final View view, int l, int t, int r, int b) {

        final int pl = UtilHelper.dip2px(context, l);
        final int pt = UtilHelper.dip2px(context, t);
        final int pr = UtilHelper.dip2px(context, r);
        final int pb = UtilHelper.dip2px(context, b);

        final View parent = (View) view.getParent();
        parent.post(new Runnable() {
            @Override
            public void run() {
                final Rect rect = new Rect();
                view.getHitRect(rect);
                rect.right += pr;
                rect.left -= pl;
                rect.bottom += pb;
                rect.top -= pt;
                parent.setTouchDelegate(new TouchDelegate(rect, view));
            }
        });
    }

    public static String getLocalDns() {
        BufferedReader in = null;
        try {
            Process process = Runtime.getRuntime().exec("getprop net.dns1");
            in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine();
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        } finally {
            CloseUtil.close(in);
        }
        return null;
    }

    public static String getLocalDnsBak() {
        BufferedReader in = null;
        try {
            Process process = Runtime.getRuntime().exec("getprop net.dns2");
            in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine();
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        } finally {
            CloseUtil.close(in);
        }
        return null;
    }

    /**
     * 判断当前系统版本是否大于2.3
     *
     * @return
     */
    public static boolean isSDKVersionUp23() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断当前手机的ROM是否为Flyme，并且版本大于3.5
     *
     * @return
     */
    public static boolean isFlymeOsAbove35() {
        String flymeOs = android.os.Build.DISPLAY;
        if (flymeOs != null && flymeOs.contains("Flyme")) {
            String numStr = getNumFromStr(flymeOs);
            if (numStr != null) {
                if (numStr.length() >= 3) {
                    int firstNum = JavaTypesHelper.toInt(getNumFromStr(numStr.substring(0, 1)), 0);
                    int secondNum = JavaTypesHelper.toInt(getNumFromStr(numStr.substring(1, 2)), 0);
                    if (firstNum > 3 || (firstNum == 3 && secondNum >= 5)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 从字符串中提取所有的数字
     *
     * @param str
     * @return
     */
    public static String getNumFromStr(String str) {
        if (str == null) {
            return null;
        }
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 获取协议头
     *
     * @param key
     * @return
     */
    private static List<String> getHeaderValueList(Map<String, List<String>> mHeader, String key) {
        if (mHeader != null && key != null) {
            return Collections.unmodifiableList(mHeader.get(key));
        } else {
            return null;
        }
    }

    /**
     * 得到Http头 Header中的某个Field的Value
     *
     * @param mHeader
     * @param key
     * @return
     */
    public static String getHeaderValueString(Map<String, List<String>> mHeader, String key) {
        if (mHeader != null && key != null) {
            List<String> strList = getHeaderValueList(mHeader, key);
            StringBuilder sb = new StringBuilder();
            if (strList != null) {
                for (String str : strList) {
                    sb.append(str);
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * @return the mToastView
     */
    public static ICustomToastView getToastView() {
        return mToastView;
    }

    /**
     * @param mToastView the mToastView to set
     */
    public static void setToastView(ICustomToastView mToastView) {
        UtilHelper.mToastView = mToastView;
    }
}
