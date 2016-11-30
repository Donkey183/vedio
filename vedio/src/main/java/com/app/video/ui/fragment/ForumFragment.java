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

import com.app.basevideo.util.WindowUtil;
import com.app.video.R;
import com.app.video.config.Constants;
import com.app.video.config.Forum;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.ui.activity.TestActivity;
import com.app.video.ui.widget.CommonAlert;
import com.bumptech.glide.Glide;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;


public class ForumFragment extends android.app.Fragment {

    private RecyclerView forum_recyclerView;

    private ForumAdapter mAdapter;

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

    private List<Forum> forumList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum,container, false);
        forum_recyclerView = (RecyclerView) view.findViewById(R.id.forum_recycler);
        forum_recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1));
        initdata();
        mAdapter = new ForumAdapter();
        forum_recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position,Object object) {
                if(!Constants.config.getVip_now().equals(Constants.CROWN)){
                    CommonAlert alert = new CommonAlert(getActivity());
                    alert.showAlert(Constants.config.getPay1(),Constants.config.getPay2(),Constants.config.getPay_img(),R.id.forum_layout);
                }
            }
        });

        return view;
    }

    private void initdata() {
        forumList = new ArrayList<Forum>();

        for (int i = 0; i < 12; i++) {
            Forum forum = new Forum();
            forum.setName("迅雷下载区");
            forum.setImage(imgurls[i % 11]);
            forum.setSends(245);
            forum.setAccept(17);
            forum.setDescribe("玩蛋去吧");
            forumList.add(forum);
        }
    }


    class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.MyViewHolder> implements View.OnClickListener {

        private LayoutInflater mInflater;
        private OnRecyclerViewItemClickListener mOnItemClickListener = null;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            this.mInflater = LayoutInflater.from(getActivity());
            View view = mInflater.inflate(R.layout.item_forum, parent, false);
            WindowUtil.resizeRecursively(view);
            MyViewHolder holder = new MyViewHolder(view);

            view.setOnClickListener(this);
            return holder;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            holder.video_name.setText(forumList.get(position).getName());
            Glide.with(ForumFragment.this).load(forumList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.video_img);
            holder.video_send.setText("发帖数: "+forumList.get(position).getSends());
            holder.video_accept.setText("回帖数: "+forumList.get(position).getAccept());
            holder.video_descript.setText(forumList.get(position).getDescribe());

            if (mOnItemClickListener != null) {
                //为ItemView设置监听器
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition(); // 1
                        mOnItemClickListener.onItemClick(holder.itemView, position,null); // 2
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return forumList.size();
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
            TextView video_send;
            TextView video_accept;
            TextView video_descript;

            public MyViewHolder(View view) {
                super(view);
                video_name = (TextView) view.findViewById(R.id.forum_name);
                video_img = (ImageView) view.findViewById(R.id.forum_image);
                video_send = (TextView) view.findViewById(R.id.forum_send);
                video_accept = (TextView) view.findViewById(R.id.forum_accept);
                video_descript = (TextView) view.findViewById(R.id.forum_descript);
            }
        }
    }
}
