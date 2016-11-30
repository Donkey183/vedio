package com.app.video.ui.view;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.app.basevideo.base.MFBaseMVCView;
import com.app.video.R;
import com.app.video.adaptor.VideoDetailAdapter;
import com.app.video.data.VideoDetailData;
import com.app.video.ui.activity.VedioDetailActivity;
import com.app.video.ui.activity.VideoPlayerActivity;

public class VideoDetailView extends MFBaseMVCView {

    private VedioDetailActivity mActivity;
    private View.OnClickListener mOnClickListener;
    private ImageView mplayerImg;
    private ScrollView mScrollView;

    private RecyclerView mListView;
    private VideoDetailAdapter mDetailAdapter;

    public VideoDetailView(VedioDetailActivity activity, View.OnClickListener onClickListener) {
        super(activity);
        mActivity = activity;
        mOnClickListener = onClickListener;
        init();
    }

    private void init() {
        mplayerImg = (ImageView) mActivity.findViewById(R.id.player_img);
        mListView = (RecyclerView) mActivity.findViewById(R.id.pre_list);
        mScrollView = (ScrollView) mActivity.findViewById(R.id.scroll);
        mScrollView.setFocusable(true);
        mScrollView.setFocusableInTouchMode(true);
        mScrollView.requestFocus();
        mListView.setLayoutManager(new LinearLayoutManager(mActivity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        Intent intent = mActivity.getIntent();
        final String url = intent.getStringExtra("path");

        mplayerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, VideoPlayerActivity.class);
                intent.putExtra("path", url);
                mActivity.startActivityForResult(intent, 0);
            }
        });
    }

    public void showVideoDetail(VideoDetailData detailData) {

        mDetailAdapter = new VideoDetailAdapter(mActivity, mOnClickListener);
        mDetailAdapter.showVideoDetail(detailData);
        mListView.setAdapter(mDetailAdapter);

    }

    @Override
    protected int getLayoutRecourseId() {
        return R.layout.activity_preplay;
    }

    @Override
    protected void onDestroy() {

    }
}
