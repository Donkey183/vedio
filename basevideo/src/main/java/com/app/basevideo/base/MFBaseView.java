package com.app.basevideo.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.app.basevideo.util.WindowUtil;

abstract public class MFBaseView extends View {
    public MFBaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public MFBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public MFBaseView(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        resize();
        onInit();
    }

    protected void onInit() {
    }

    public void show() {
        setVisibility(View.VISIBLE);
    }

    public void hide() {
        setVisibility(View.GONE);
    }

    public void invisible() {
        setVisibility(View.INVISIBLE);
    }

    /**
     * 重新计算当前View的宽高、边距
     */
    public void resize() {
        WindowUtil.resize(this);
    }

    public String getLogTag() {
        return this.getClass().getSimpleName();
    }

    public void onDestroy() {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void clear() {
    }

    public void reset() {
    }

    public void onShow() {

    }
}
