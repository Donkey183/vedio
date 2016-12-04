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
import com.app.video.R;
import com.app.video.adaptor.ChannelAdaptor;
import com.app.video.data.ChannelData;
import com.app.video.data.VideoData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.model.ChannelModel;
import com.app.video.ui.activity.ChannelActivity;


public class ChannelFragment extends MFBaseFragment implements INetFinish, OnRecyclerViewItemClickListener {
    private RecyclerView channel_recycler;
    private ChannelModel mChannelModel;
    private ChannelAdaptor mChannelAdaptor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChannelModel = new ChannelModel(this);
        getChannelInfo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel, container, false);
        channel_recycler = (RecyclerView) view.findViewById(R.id.channel_recycler);
        channel_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        return view;
    }


    private void getChannelInfo() {
        CommonHttpRequest request = new CommonHttpRequest();
        mChannelModel.sendHttpRequest(request, ChannelModel.GET_CHANNEL_INFO);
    }

    @Override
    public void onHttpResponse(CommonMessage<?> responsedMessage) {
        mChannelAdaptor = new ChannelAdaptor(this.getActivity().getApplicationContext(), this);
        channel_recycler.setAdapter(mChannelAdaptor);
        mChannelAdaptor.showChannelView(mChannelModel.channelList);
    }

    @Override
    public void onItemClick(View view, int position, Object obj) {
        if (!(obj instanceof ChannelData)) {
            return;
        }else{
            ChannelData date = (ChannelData)obj;
            Intent intent = new Intent(ChannelFragment.this.getActivity(), ChannelActivity.class);
            intent.putExtra("name",date.getDname());
            startActivity(intent);
        }

    }
}
