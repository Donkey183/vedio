package com.app.video.adaptor;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.video.R;
import com.app.video.data.VideoData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class BlackGoldAdapter extends PagerAdapter {
    private List<VideoData.ResultBean> mResultBeanList;
    private List<View> mViewList = new ArrayList<>();

    private Context mContext;

    public BlackGoldAdapter(Context context, List<VideoData.ResultBean> mResultBeanList) {
        this.mResultBeanList = mResultBeanList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mResultBeanList == null ? 0 : mResultBeanList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        Glide.with(mContext).load(mResultBeanList.get(position).getDypic()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imageView);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        container.addView(imageView);
        mViewList.add(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.remove(position));
    }
}
