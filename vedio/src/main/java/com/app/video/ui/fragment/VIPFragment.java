package com.app.video.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.video.R;
import com.app.video.config.Video;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.ui.activity.VideoPlayerActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class VIPFragment extends android.app.Fragment {
    private RecyclerView vip_recyclerView;
    private VIPAdapter mAdapter;

    private String[] imgurls = {"http://img.taopic.com/uploads/allimg/121017/234940-12101FR22825.jpg",
            "http://pic44.nipic.com/20140721/11624852_001107119409_2.jpg",
            "http://pic47.nipic.com/20140901/6608733_145238341000_2.jpg",
            "http://pic45.nipic.com/20140807/2531170_221641791877_2.jpg",
            "http://pic6.huitu.com/res/20130116/84481_20130116142820494200_1.jpg",
            "http://img05.tooopen.com/images/20140604/sy_62331342149.jpg",
            "http://img03.tooopen.com/images/20131102/sy_45238929299.jpg",
            "http://img01.taopic.com/151227/234973-15122G5550795.jpg",
            "http://img.taopic.com/uploads/allimg/120423/107913-12042323220753.jpg",
            "http://img05.tooopen.com/images/20150531/tooopen_sy_127457023651.jpg",
            "http://pic32.nipic.com/20130815/10675263_110224052319_2.jpg"};

    private List<Video> videoList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vip, container, false);
        initdata();
        vip_recyclerView = (RecyclerView) view.findViewById(R.id.vip_recycler);
        vip_recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        vip_recyclerView.setAdapter(mAdapter = new VIPAdapter());
        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Video v = videoList.get(position);
                Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
                intent.putExtra("path", v.getURL());
                startActivity(intent);
            }
        });

        return view;
    }


    private void initdata() {

        videoList = new ArrayList<Video>();

        for (int i = 0; i < 30; i++) {
            Video video = new Video();
            video.setName("testname");
            video.setImageurl(imgurls[i % 11]);
            video.setType("超清");
            video.setURL("rtmp://live.hkstv.hk.lxdns.com/live/hks");
            videoList.add(video);
        }

        for (int i = 0; i < 30; i++) {
            Video video = new Video();
            video.setName("testVIP");
            video.setImageurl(imgurls[i % 11]);
            video.setType("VIP");
            video.setURL("rtmp://live.hkstv.hk.lxdns.com/live/hks");
            videoList.add(video);
        }
    }


    class VIPAdapter extends RecyclerView.Adapter<VIPAdapter.MyViewHolder> implements View.OnClickListener {

        private LayoutInflater mInflater;
        private OnRecyclerViewItemClickListener mOnItemClickListener = null;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            this.mInflater = LayoutInflater.from(getActivity());
            View view = mInflater.inflate(R.layout.item_vip, parent, false);

            MyViewHolder holder = new MyViewHolder(view);

            view.setOnClickListener(this);
            return holder;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.video_name.setText(videoList.get(position).getName());
            Glide.with(VIPFragment.this).load(videoList.get(position).getImageurl()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.video_img);
            if (mOnItemClickListener != null) {
                //为ItemView设置监听器
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition(); // 1
                        mOnItemClickListener.onItemClick(holder.itemView, position); // 2
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return videoList.size();
        }

        @Override
        public void onClick(View view) {

        }

        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView video_name;
            ImageView video_img;

            public MyViewHolder(View view) {
                super(view);
                video_name = (TextView) view.findViewById(R.id.vip_name);
                video_img = (ImageView) view.findViewById(R.id.vip_image);
            }
        }
    }
}
