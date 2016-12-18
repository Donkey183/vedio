package com.app.video.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.basevideo.base.MFBaseFragment;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.INetFinish;
import com.app.basevideo.util.WindowUtil;
import com.app.video.R;
import com.app.video.adaptor.ZiZuanFragmentAdaptor;
import com.app.video.config.Constants;
import com.app.video.config.VedioConstant;
import com.app.video.data.VideoData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.model.VideoModel;
import com.app.video.ui.activity.VideoPlayerActivity;
import com.app.video.ui.widget.CommonAlert;
import com.app.video.util.PlayCountUtil;

public class PurpleFragment extends MFBaseFragment implements INetFinish, OnRecyclerViewItemClickListener, View.OnClickListener {

    private RecyclerView vip_recyclerView;
    private ZiZuanFragmentAdaptor mAdapter;
    private VideoModel mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new VideoModel(this);
        getVideoInfo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vip, container, false);

        vip_recyclerView = (RecyclerView) view.findViewById(R.id.vip_recycler);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 3 : 1;
            }
        });
        vip_recyclerView.setLayoutManager(manager);

        WindowUtil.resizeRecursively(view);
        return view;
    }

    private void getVideoInfo() {
        CommonHttpRequest request = new CommonHttpRequest();
        request.addParam(VedioConstant.R_TYPE, "5");
        request.addParam(VedioConstant.PAGE_NO, "1");
        mModel.sendHttpRequest(request, VideoModel.GET_VEDIO_PURPLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getVideoInfo();
    }

    @Override
    public void onHttpResponse(CommonMessage<?> responsedMessage) {
        mAdapter = new ZiZuanFragmentAdaptor(this.getActivity().getApplicationContext());
        mAdapter.setOnItemClickListener(this);
        vip_recyclerView.setAdapter(mAdapter);
        mAdapter.showVIPView(mModel.videoData.page.result, mModel.videoData.page.list1);
    }

    @Override
    public void onItemClick(View view, int position, Object obj) {
        if (!Constants.config.getVip_now().equals(Constants.PURPLE)) {
            CommonAlert alert = new CommonAlert(getActivity());
            alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.vip_layout);
        } else {
            if (!PlayCountUtil.hasAuth("ZIZUAN")) {
                CommonAlert alert = new CommonAlert(PurpleFragment.this.getActivity());
                alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.forum_layout);
                return;
            }
            String recourseUrl = "";
            String img = "";
            if (obj instanceof VideoData.Page.Video) {
                recourseUrl = ((VideoData.Page.Video) obj).getDyres();
                img = ((VideoData.Page.Video) obj).getDypic();
            } else if (obj instanceof VideoData.Page.Banner) {
                recourseUrl = ((VideoData.Page.Banner) obj).getDyresource();
                img = ((VideoData.Page.Banner) obj).getDypic();
            }
            Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
            intent.putExtra("path", recourseUrl);
            intent.putExtra("img", img);
            intent.putExtra("parent", "");
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View view) {
        if (!Constants.config.getVip_now().equals(Constants.RED)) {
            CommonAlert alert = new CommonAlert(getActivity());
            alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.vip_layout);
        }
    }


}
