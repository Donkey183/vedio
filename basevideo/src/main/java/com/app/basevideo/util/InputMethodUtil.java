package com.app.basevideo.util;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class InputMethodUtil {
    /**
     * 切换软键盘显示状态
     *
     * @param context
     * @param editText
     * @param visible
     */
    public static void setInputMethodVisibility(Context context,
                                                EditText editText, boolean visible) {
        if (context == null || editText == null)
            return;
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (visible) {
            imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_SHOWN,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            imm.hideSoftInputFromWindow(editText.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 软键盘是否已显示
     *
     * @param context
     * @return
     */
    public static boolean isInputMethodVisible(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }

    /**
     * 关闭软键盘
     *
     * @param v
     */
    public static void closeInputMethod(View v) {
        if (v == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    public static void showInputMethod(View v) {
        if (v == null)
            return;
        InputMethodManager imm = (InputMethodManager) v.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void setInputMethodPanel(View view) {
        if (view != null) {
            view.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN)
                        InputMethodUtil.closeInputMethod(v);
                    return false;
                }
            });
        }
    }

    /**
     * 设置EditText的Action
     *
     * @param editText
     */
    public static void setEditTextAction(EditText editText) {
    }
}
