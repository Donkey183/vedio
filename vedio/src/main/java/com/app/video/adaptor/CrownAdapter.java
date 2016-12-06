package com.app.video.adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.video.R;
import com.app.video.config.Constants;
import com.app.video.data.VideoData;
import com.app.video.ui.activity.VideoPlayerActivity;
import com.app.video.ui.widget.CommonAlert;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class CrownAdapter extends PagerAdapter {
    private List<VideoData.Page.Banner> mViewList;
    private Context context;
    private List<View> viewList;


    public CrownAdapter(List<VideoData.Page.Banner> viewList, Context context) {
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

    private VideoData.Page.Banner getItem(int position) {
        int size = getCount();
        return (VideoData.Page.Banner) mViewList.get((position >= size ? size - 1 : position));
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final VideoData.Page.Banner video = getItem(position);
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context).load(video.getDypic()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Constants.config.getVip_now().equals(Constants.CROWN)&&!Constants.config.getVip_now().equals(Constants.RED)) {
                    CommonAlert alert = new CommonAlert(context);
                    alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.vip_layout);
                } else {
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("path", video.getDyresource());
                    intent.putExtra("parent", "");
                    context.startActivity(intent);
                }
            }
        });
        viewList.add(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }
}
