package com.app.video.adaptor;


import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class BlackGoldAdapter extends PagerAdapter {
    private List<View> mViewList;


    public BlackGoldAdapter(List<View> viewList) {
        this.mViewList = viewList;
    }

    @Override
    public int getCount() {
        return mViewList == null ? 0 : mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private ImageView getItem(int position) {
        int size = getCount();
        return (ImageView) mViewList.get((position >= size ? size - 1 : position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = getItem(position);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.removeView(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.remove(position));
        this.notifyDataSetChanged();
    }
}
