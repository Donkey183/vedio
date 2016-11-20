package com.app.basevideo.util;

import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.app.basevideo.R;
import com.app.basevideo.base.MFBaseActivity;

public class PopUpWindowUtil {
    public static void showPopUpWindow(final MFBaseActivity activity, final PopupWindow popUpWindow, final View baseView, boolean outsideTouchable) {
        popUpWindow.setBackgroundDrawable(new ColorDrawable(0xffffffff));//必须添加BackgroundDrawable,否则点击popupWindow外部或返回键popupWindow不会消失(系统Bug)
        /**
         * popupWindow弹出动画,注意，动画中定义了弹出时间为500毫秒,与Activity背景透明度渐变时间一致，若改动请同时更新二者
         * {@link FinanceChooseProtectionView#setActivityAlpha(boolean)}
         */
        popUpWindow.setAnimationStyle(R.style.finance_popupwindow_anim);
        popUpWindow.setTouchable(true);
        popUpWindow.setOutsideTouchable(outsideTouchable);
        popUpWindow.setFocusable(false);
        activity.setIsDisPatchTouchEvent(false);
        setActivityAlpha(activity, true);

        popUpWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    return true;
                }
                return false;//事件拦截,popupWindow将捕获事件,返回true则popupWindow内部无法接受事件,此时点击外围也无响应
            }
        });

        popUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                activity.setIsDisPatchTouchEvent(true);
                setActivityAlpha(activity, false);
            }
        });

        popUpWindow.showAtLocation(baseView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * ****************************************************  更新页面的透明度分割线  **************************************************************
     */

    private static Handler mHandler = new Handler();
    private static float mAlpha;
    private static int mUpdateTimes;
    private static final int UPDATE_FREQUENCY = 25;//更新频率,popupWindow弹出时间为500毫秒（XML动画中设定）,共分20次刷新,因此每次25毫秒
    private static final int UPDATE_TIMES = 20;//更新次数,popupWindow弹出时间为500毫秒（XML动画中设定）,每25毫秒刷新一次,因此共刷新20次

    /**
     * 设置Activity页面透明度
     *
     * @param isShowPopupWindow
     */
    private static void setActivityAlpha(final MFBaseActivity activity, final boolean isShowPopupWindow) {
        mAlpha = isShowPopupWindow ? 1.0f : 0.3f; //alpha的默认初始值,弹出popupwindow时从全透明到半透明（alpha值从1.0~0.3）,popupwindow消失时从半透明到全透明(alpha值从0.3~1.0),共分20次渐变
        mUpdateTimes = UPDATE_TIMES; //500毫秒内刷新20次UI
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = mAlpha;
                activity.getWindow().setAttributes(lp);
                mAlpha = isShowPopupWindow ? mAlpha - 0.035f : mAlpha + 0.035f;//每次渐变alpha值得变化, 20次 * 0.035每次 = 0.7
                mUpdateTimes--;
                if (mUpdateTimes > 0) {
                    mHandler.postDelayed(this, UPDATE_FREQUENCY);//移除runnable
                } else {
                    mHandler.removeCallbacks(this);
                    mUpdateTimes = UPDATE_TIMES;
                }
            }
        };
        mHandler.postDelayed(runnable, UPDATE_FREQUENCY);
    }
}
