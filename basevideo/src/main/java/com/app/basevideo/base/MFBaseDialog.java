package com.app.basevideo.base;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.app.basevideo.util.WindowUtil;


public class MFBaseDialog extends Dialog {

    public MFBaseDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void setContentView(int layoutResID) {
        View view = getLayoutInflater().inflate(layoutResID, null);
        this.setContentView(view);
    }

    @Override
    public void setContentView(View view) {
        resizeView(view);
        super.setContentView(view);
    }

    /**
     * 重新计算View及子View的宽高、边距
     *
     * @param view
     */
    public void resizeView(View view) {
        WindowUtil.resizeRecursively(view);
    }

    public String getLogTag() {
        return this.getClass().getSimpleName();
    }
}
