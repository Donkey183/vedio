package com.app.video.ui.fragment;

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
import com.app.video.R;
//import com.app.video.adaptor.RecommendAdaptor;
import com.app.video.config.VedioConstant;
import com.app.video.model.ChannelModel;
import com.app.video.model.VideoModel;

public class RecommendFragment extends MFBaseFragment implements INetFinish {
    private RecyclerView my_recyclerView;
    //private RecommendAdaptor mAdapter;
    private VideoModel mVideoModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoModel = new VideoModel(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        my_recyclerView = (RecyclerView) view.findViewById(R.id.main_recycler);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });
        my_recyclerView.setLayoutManager(manager);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getRecommendInfo();
    }

    private void getRecommendInfo() {
        CommonHttpRequest request = new CommonHttpRequest();
        request.addParam(VedioConstant.PAGE_NO, 1);
        request.addParam(VedioConstant.R_TYPE, 0);
        mVideoModel.sendHttpRequest(request, ChannelModel.GET_CHANNEL_INFO);
    }

    @Override
    public void onHttpResponse(CommonMessage<?> responsedMessage) {
 //       my_recyclerView.setAdapter(mAdapter = new RecommendAdaptor(RecommendFragment.this.getActivity()));
//        mAdapter.showRecommendView(mVideoModel.videoData.getVideoList());
//        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position, Object object) {
//                VideoData.Video video = (VideoData.Video) object;
//                Intent intent = new Intent(getActivity(), PreplayActivity.class);
//                intent.putExtra("path", video.getDyres());
//                startActivity(intent);
//            }
//        });
    }
}
