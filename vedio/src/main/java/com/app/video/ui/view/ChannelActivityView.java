package com.app.video.ui.view;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.app.basevideo.base.MFBaseMVCView;
import com.app.video.R;
import com.app.video.adaptor.ChannelContentAdaptor;
import com.app.video.data.ChannelContentData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.ui.activity.ChannelActivity;

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
        mAdapter = new ChannelContentAdaptor(mActivity,mClickListener);
        vip_recyclerView.setAdapter(mAdapter);
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
