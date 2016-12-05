package com.app.video.ui.fragment;

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
import com.app.video.ui.widget.CommonAlert;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class ForumFragment extends android.app.Fragment {

    private RecyclerView forum_recyclerView;

    private ForumAdapter mAdapter;

    private List<Forum> forumList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        forum_recyclerView = (RecyclerView) view.findViewById(R.id.forum_recycler);
        forum_recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        initdata();
        mAdapter = new ForumAdapter();
        forum_recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object object) {
                if (!Constants.config.getVip_now().equals(Constants.RED)) {
                    CommonAlert alert = new CommonAlert(getActivity());
                    alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.forum_layout);
                }
            }
        });

        return view;
    }

    private void initdata() {
        forumList = new ArrayList<Forum>();
        forumList.add(generateForum("求片交流区", "各种影片需求，都可以寻求帮助", 8089, 28090, "http://d.hiphotos.baidu.com/image/pic/item/63d9f2d3572c11dfeac16bd9672762d0f703c23c.jpg"));
        forumList.add(generateForum("自拍偷拍区", "各种事件，各种自拍偷拍", 5098, 17809, "http://g.hiphotos.baidu.com/image/pic/item/08f790529822720efa4690fb79cb0a46f21fab23.jpg"));
        forumList.add(generateForum("童颜巨乳区", "不解释，你懂得", 3809, 7950, "http://h.hiphotos.baidu.com/image/pic/item/b7003af33a87e95042d6236512385343faf2b4f2.jpg"));
        forumList.add(generateForum("制服诱惑区", "职业支付，满足你所有需求", 2016, 3990, "http://b.hiphotos.baidu.com/image/pic/item/8d5494eef01f3a29b41f18fa9c25bc315c607c2b.jpg"));
        forumList.add(generateForum("高清素颜区", "各种颜值爆表，高清震慑眼球", 8990, 18900, "http://e.hiphotos.baidu.com/image/pic/item/622762d0f703918ff453d0ce533d269759eec430.jpg"));
        forumList.add(generateForum("岛国明星区", "各种番号大聚集", 14095, 25693, "http://c.hiphotos.baidu.com/image/pic/item/9d82d158ccbf6c814259ffe1be3eb13533fa4049.jpg"));
        forumList.add(generateForum("成人动漫区", "各种h动漫大聚集", 7893, 9760, "http://e.hiphotos.baidu.com/image/pic/item/37d12f2eb9389b50e3ae035e8735e5dde7116e6d.jpg"));
        forumList.add(generateForum("迅雷下载区", "找种子，来下载", 9989, 30998, "http://d.hiphotos.baidu.com/image/pic/item/dcc451da81cb39db4555b75fd2160924ab183026.jpg"));
        forumList.add(generateForum("网盘分享区", "云时代，共分享", 5638, 6660, "http://e.hiphotos.baidu.com/image/pic/item/5243fbf2b211931344ffa5bc67380cd791238d7a.jpg"));
        forumList.add(generateForum("磁力链接区", "不解释，你懂得", 2455, 8996, "http://e.hiphotos.baidu.com/image/pic/item/472309f7905298221645d4c7d5ca7bcb0a46d444.jpg"));
        forumList.add(generateForum("贴图分享区", "高清图片，推女郎系列", 3889, 9677, "http://e.hiphotos.baidu.com/image/pic/item/d8f9d72a6059252db0f7c355369b033b5ab5b9fb.jpg"));
    }

    private Forum generateForum(String title, String describe, int send, int accept, String url) {
        Forum forum = new Forum();
        forum.setName(title);
        forum.setImage(url);
        forum.setSends(send);
        forum.setAccept(accept);
        forum.setDescribe(describe);
        return forum;
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
            holder.video_send.setText("发帖数: " + forumList.get(position).getSends());
            holder.video_accept.setText("回帖数: " + forumList.get(position).getAccept());
            holder.video_descript.setText(forumList.get(position).getDescribe());

            if (mOnItemClickListener != null) {
                //为ItemView设置监听器
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition(); // 1
                        mOnItemClickListener.onItemClick(holder.itemView, position, null); // 2
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
