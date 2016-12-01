package com.app.video.adaptor;


import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.video.data.VideoData;
import com.app.video.ui.activity.VideoPlayerActivity;

import java.util.List;

public class BlackGoldAdapter extends PagerAdapter {
    private List<View> mViewList;
    private Context context;


    public BlackGoldAdapter(List<View> viewList, Context context) {
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

    private ImageView getItem(int position) {
        int size = getCount();
        return (ImageView) mViewList.get((position >= size ? size - 1 : position));
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        ImageView imageView = getItem(position);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() != null && v.getTag() instanceof VideoData.Page.Video) {
                    VideoData.Page.Video vault = (VideoData.Page.Video) v.getTag();
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("path", vault.getDyres());
                    context.startActivity(intent);
                }
            }
        });
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }
}
