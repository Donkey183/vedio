package com.app.video.adaptor;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.video.R;
import com.app.video.data.VaultContentData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class BlackGoldAdapter2 extends PagerAdapter {
    private List<VaultContentData> mViewList;
    private Context context;
    private List<View> viewList;


    public BlackGoldAdapter2(List<VaultContentData> viewList, Context context) {
        this.viewList = new ArrayList<View>();
        this.mViewList = viewList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mViewList == null ? 0 : mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private VaultContentData getItem(int position) {
        int size = getCount();
        return (VaultContentData) mViewList.get((position >= size ? size - 1 : position));
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final VaultContentData contentData = getItem(position);
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context).load(contentData.getDpic()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imageView);
        viewList.add(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }
}
