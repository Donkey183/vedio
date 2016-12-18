package com.app.video.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.basevideo.base.MFBaseApplication;
import com.app.basevideo.base.MFBaseFragment;
import com.app.basevideo.cache.MFSimpleCache;
import com.app.basevideo.config.VedioCmd;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.INetFinish;
import com.app.video.R;
import com.app.video.adaptor.BlackGoldAdapter;
import com.app.video.config.VedioConstant;
import com.app.video.data.VideoData;
import com.app.video.model.VideoModel;
import com.app.video.util.GallyPageTransformer;
import com.bumptech.glide.Glide;

import java.util.List;


public class BlackGoldFragment extends MFBaseFragment implements INetFinish {
    private ViewPager gallery_pager;
    private RelativeLayout ll_main;
    private int pagerWidth;
    private VideoModel mVideoModel;
    private ImageView backgroundImg;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoModel = new VideoModel(this);
        getVideoInfo();
    }

    private void getVideoInfo() {
        CommonHttpRequest request = new CommonHttpRequest();
        request.addParam(VedioConstant.R_TYPE, 4);
        request.addParam(VedioConstant.PAGE_NO, 1);
        mVideoModel.sendHttpRequest(request, VideoModel.GET_VEDIO_BLACK_GLOD);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        backgroundImg = (ImageView) view.findViewById(R.id.background_img);
        String url = MFSimpleCache.get(MFBaseApplication.getInstance()).getAsString("PIC_DOMAIN") + "/dapp/heijinbj.jpg";
        Glide.with(MFBaseApplication.getInstance()).load(url).into(backgroundImg);
        Glide.with(BlackGoldFragment.this.getActivity()).load(url).into(backgroundImg);
        gallery_pager = (ViewPager) view.findViewById(R.id.gallery_pager);
        ll_main = (RelativeLayout) view.findViewById(R.id.gallery_layout);
        gallery_pager.setOffscreenPageLimit(3);
        pagerWidth = (int) (getResources().getDisplayMetrics().widthPixels * 3.0f / 3.6f);
        ViewGroup.LayoutParams lp = gallery_pager.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(pagerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            lp.width = pagerWidth;
        }
        gallery_pager.setLayoutParams(lp);
        gallery_pager.setPageMargin(-50);
        ll_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gallery_pager.dispatchTouchEvent(motionEvent);
            }
        });
        gallery_pager.setPageTransformer(true, new GallyPageTransformer());
        return view;
    }

    @Override
    public void onHttpResponse(CommonMessage<?> responsedMessage) {
        List<VideoData.Page.Video> resultBeenList = mVideoModel.videoData.page.result;
        gallery_pager.setAdapter(new BlackGoldAdapter(resultBeenList, getActivity()));
        dispatchMessage(new CommonMessage(VedioCmd.TITLE_CHANGE));
    }


    public String getVideoCount() {
        if (mVideoModel != null && mVideoModel.videoData != null && mVideoModel.videoData.page != null && mVideoModel.videoData.page.getTotalCount() > 0) {
            return "" + mVideoModel.videoData.page.getTotalCount();
        }
        return null;
    }


}
