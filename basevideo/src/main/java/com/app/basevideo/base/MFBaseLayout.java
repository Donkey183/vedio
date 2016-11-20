package com.app.basevideo.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.app.basevideo.util.WindowUtil;

abstract public class MFBaseLayout extends RelativeLayout {

    public MFBaseLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public MFBaseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public MFBaseLayout(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        setContentView(onInitLayoutResId());
        onInit();
    }

    private void setContentView(int layoutResId) {
        if (layoutResId == 0)
            return;
        LayoutInflater.from(getContext()).inflate(layoutResId, this, true);
    }

    abstract protected int onInitLayoutResId();

    protected void onInit() {
    }

    public void show() {
        show(this);
    }

    public void hide() {
        hide(this);
    }

    public void enable(boolean enable) {
        enable(this, enable);
    }

    public void invisible() {
        setVisibility(View.INVISIBLE);
    }

    public void show(View v) {
        v.setVisibility(View.VISIBLE);
    }

    public void enable(View v, boolean enable) {
        v.setEnabled(enable);
    }

    public void hide(View v) {
        v.setVisibility(View.GONE);
    }

    public void invisible(View v) {
        v.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
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

    public void onDestroy() {
        this.removeAllViews();
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
