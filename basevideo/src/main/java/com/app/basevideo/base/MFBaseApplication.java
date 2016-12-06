package com.app.basevideo.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.app.basevideo.cache.MFSimpleCache;
import com.app.basevideo.framework.util.LogUtil;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.basevideo.net.utils.AppVersionConfig;

import retrofit2.Response;


public class MFBaseApplication extends Application {

    private static MFBaseApplication sApp = null;

    private boolean mIsDebugMode = false;

    private Application mContext = null;

    private boolean mIsPluginResourceOpen = true;

    /**
     * 会调用父类的:{@link #onCreate()}，与其类似
     */
    @Override
    public void onCreate() {
        initBaseApp();
        super.onCreate();
//        LeakCanary.install(this);
    }



    private void initBaseApp() {
        sApp = this;
        mContext = this;
        initWorkMode();
        LogUtil.addLogPackage("com.didi.basefinance");
        LogUtil.addLogPackage("com.didi.finance_widget_progress");
    }

    /**
     * 获得BaseApplication的单例，供其他地方希望不传入context的方法和类使用
     *
     * @return
     */
    public static MFBaseApplication getInstance() {
        return sApp;
    }


    /**
     * 获得Application的context
     *
     * @return
     */
    public Context getContext() {
        return mContext;
    }

    private void initWorkMode() {
        if ((mContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) == 0) {
            mIsDebugMode = false;
        } else {
            mIsDebugMode = true;
        }
    }

    /**
     * app是否是debug包
     *
     * @return
     */
    public boolean isDebugMode() {
        return !(AppVersionConfig.VERSION_TYPE_USED == AppVersionConfig.VERSION_TYPE_ONLINE);
    }

    /**
     * 此方法需要在 {@link #onCreate()} 方法执行完毕后调用才能生效。
     */
    public void setDebugMode(boolean mIsDebugMode) {
        this.mIsDebugMode = mIsDebugMode;
    }


    private long lastGcTime = 0L;

    /**
     * 是否可以使用App配置功能,当手机支持多点触控(三点及以上,现在手机基本支持)并且{@link AppVersionConfig#CAN_USE_APP_CONFIG}配置支持使用该功能时返回true
     *
     * @return
     */

    public boolean canUseAppConfig() {
        PackageManager pm = MFBaseApplication.getInstance().getPackageManager();
        boolean isSupportMultiTouch = pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT);
        return isSupportMultiTouch && AppVersionConfig.CAN_USE_APP_CONFIG;
    }

}
