package com.app.video.ui.view;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.app.basevideo.base.MFBaseMVCView;
import com.app.video.R;
import com.app.video.adaptor.ChannelContentAdaptor;
import com.app.video.config.Constants;
import com.app.video.data.ChannelContentData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.ui.activity.ChannelActivity;
import com.app.video.ui.activity.VideoPlayerActivity;
import com.app.video.ui.widget.CommonAlert;

import java.util.List;

public class ChannelActivityView extends MFBaseMVCView {

    private ChannelActivity mActivity;
    private RecyclerView vip_recyclerView;
    private ChannelContentAdaptor mAdapter;
    private OnRecyclerViewItemClickListener mClickListener;
    private Button btn1;
    private Button btn2;

    public ChannelActivityView(ChannelActivity activity, OnRecyclerViewItemClickListener clickListener) {
        super(activity);
        mActivity = activity;
        mClickListener = clickListener;
        init();
    }

    private void init() {
        vip_recyclerView = (RecyclerView) mActivity.findViewById(R.id.vip_recycler);
        btn1 = (Button) mActivity.findViewById(R.id.btn1);
        btn2 = (Button) mActivity.findViewById(R.id.btn2);
        vip_recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mAdapter = new ChannelContentAdaptor(mActivity, mClickListener);
        vip_recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object object) {
                if (!Constants.config.getVip_now().equals(Constants.RED)) {
                    CommonAlert alert = new CommonAlert(mActivity);
                    alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.forum_layout);
                } else if (object instanceof ChannelContentData.ChannelContent) {
                    ChannelContentData.ChannelContent content = (ChannelContentData.ChannelContent) object;
                    Intent intent = new Intent(mActivity, VideoPlayerActivity.class);
                    intent.putExtra("path", content.getCresource());
                    mActivity.startActivity(intent);
                }
            }
        });
    }

    public void showPageView(List<ChannelContentData.ChannelContent> channelContents) {
        mAdapter.showChannelView(channelContents);
    }

    @Override
    protected int getLayoutRecourseId() {
        return R.layout.channel_activity;
    }

    @Override
    protected void onDestroy() {

    }
}
