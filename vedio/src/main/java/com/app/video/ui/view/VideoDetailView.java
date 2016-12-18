package com.app.video.ui.view;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.basevideo.base.MFBaseApplication;
import com.app.basevideo.base.MFBaseMVCView;
import com.app.video.R;
import com.app.video.adaptor.VideoDetailAdapter;
import com.app.video.config.Constants;
import com.app.video.data.VideoDetailData;
import com.app.video.ui.activity.VedioDetailActivity;
import com.app.video.ui.activity.VideoPlayerActivity;
import com.app.video.ui.widget.CommonAlert;
import com.app.video.util.PlayCountUtil;
import com.bumptech.glide.Glide;

import java.util.List;

public class VideoDetailView extends MFBaseMVCView {

    private VedioDetailActivity mActivity;
    private View.OnClickListener mOnClickListener;
    private ImageView mplayerImg;
    private ScrollView mScrollView;
    private RelativeLayout player_layout;
    private ImageView back;

    private ImageView img1;
    private ImageView img2;
    private ImageView img3;

    private TextView text1;
    private TextView text2;
    private TextView text3;

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
        player_layout = (RelativeLayout) mActivity.findViewById(R.id.player_layout);

        img1 = (ImageView) mActivity.findViewById(R.id.img1);
        img2 = (ImageView) mActivity.findViewById(R.id.img2);
        img3 = (ImageView) mActivity.findViewById(R.id.img3);

        text1 = (TextView) mActivity.findViewById(R.id.text1);
        text2 = (TextView) mActivity.findViewById(R.id.text2);
        text3 = (TextView) mActivity.findViewById(R.id.text3);

        back = (ImageView) mActivity.findViewById(R.id.detial_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });

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
        final String img = intent.getStringExtra("img");
        Glide.with(mActivity).load(img).error(R.drawable.cha).into(mplayerImg);

        player_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PlayCountUtil.hasAuth("DETAIL_VIEW")) {
                    Toast.makeText(MFBaseApplication.getInstance(),"您的播放次数已超过五次!",Toast.LENGTH_LONG).show();
                    CommonAlert alert = new CommonAlert(mActivity);
                    alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.forum_layout);
                    return;
                }

                Intent intent = new Intent(mActivity, VideoPlayerActivity.class);
                intent.putExtra("path", url);
                intent.putExtra("parent", "detail");
                mActivity.startActivityForResult(intent, 0);
            }
        });
    }


    public void showVideoDetail(VideoDetailData detailData) {

        mDetailAdapter = new VideoDetailAdapter(mActivity, mOnClickListener);
        mListView.setAdapter(mDetailAdapter);
        mDetailAdapter.showVideoDetail(detailData);
        showRecom(detailData);

    }

    private void showRecom(VideoDetailData detailData) {
        List<VideoDetailData.Detail> datalist = detailData.recommendVideoList;
        Glide.with(mActivity).load(datalist.get(0).getDypic()).into(img1);
        Glide.with(mActivity).load(datalist.get(1).getDypic()).into(img2);
        Glide.with(mActivity).load(datalist.get(2).getDypic()).into(img3);
        text1.setText(datalist.get(0).getDname());
        text2.setText(datalist.get(1).getDname());
        text3.setText(datalist.get(2).getDname());

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Constants.config.getVip_now().equals(Constants.RED)) {
                    CommonAlert alert = new CommonAlert(mActivity);
                    alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.forum_layout);
                }
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Constants.config.getVip_now().equals(Constants.RED)) {
                    CommonAlert alert = new CommonAlert(mActivity);
                    alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.forum_layout);
                }
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Constants.config.getVip_now().equals(Constants.RED)) {
                    CommonAlert alert = new CommonAlert(mActivity);
                    alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.forum_layout);
                }
            }
        });

    }

    @Override
    protected int getLayoutRecourseId() {
        return R.layout.activity_preplay;
    }

    @Override
    protected void onDestroy() {

    }
}
