package com.app.video.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.video.R;
import com.app.video.adaptor.ChannelAdaptor;
import com.app.video.model.ChannelModel;


public class ChannelFragment extends android.app.Fragment {
    private RecyclerView channel_recycler;
    private ChannelModel mChannelModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//
//     mChannelModel = new ChannelModel(this.getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_channel, container, false);
        channel_recycler = (RecyclerView) view.findViewById(R.id.channel_recycler);
        channel_recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        channel_recycler.setAdapter(new ChannelAdaptor(view.getContext()));
        return view;
    }









}
