package com.app.video.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.basevideo.base.MFBaseFragment;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.INetFinish;
import com.app.video.R;
import com.app.video.adaptor.VIPFragmentAdaptor;
import com.app.video.config.VedioConstant;
import com.app.video.data.VideoData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.model.VideoModel;
import com.app.video.ui.activity.VideoPlayerActivity;

public class VIPFragment extends MFBaseFragment implements INetFinish, OnRecyclerViewItemClickListener {
    private RecyclerView vip_recyclerView;
    private VIPFragmentAdaptor mAdapter;
    private VideoModel mModel;
    private Button btn1;
    private Button btn2;

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
        btn1 = (Button) view.findViewById(R.id.btn1);
        btn2 = (Button) view.findViewById(R.id.btn2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        vip_recyclerView = (RecyclerView) view.findViewById(R.id.vip_recycler);
        vip_recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        return view;
    }

    private void getVideoInfo() {
        CommonHttpRequest request = new CommonHttpRequest();
        request.addParam(VedioConstant.R_TYPE, 0);
        request.addParam(VedioConstant.PAGE_NO, 1);
        mModel.sendHttpRequest(request, VideoModel.GET_VIDEO_INFO);
    }

    @Override
    public void onHttpResponse(CommonMessage<?> responsedMessage) {
        mAdapter = new VIPFragmentAdaptor(this.getActivity().getApplicationContext());
        mAdapter.setOnItemClickListener(this);
        vip_recyclerView.setAdapter(mAdapter);
        mAdapter.showVIPView(mModel.videoData.getResult());
    }

    @Override
    public void onItemClick(View view, int position, Object obj) {
        if (!(obj instanceof VideoData.ResultBean)) {
            return;
        }
        VideoData.ResultBean resultBean = (VideoData.ResultBean) obj;
        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
        intent.putExtra("path", resultBean.getDyres());
        startActivity(intent);
    }
}
