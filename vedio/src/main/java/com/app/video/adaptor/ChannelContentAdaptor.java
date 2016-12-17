package com.app.video.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.basevideo.util.WindowUtil;
import com.app.video.R;
import com.app.video.data.ChannelContentData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ChannelContentAdaptor extends RecyclerView.Adapter<ChannelContentAdaptor.ChannelContentViewHolder> implements View.OnClickListener {

    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private Context mContext;
    private List<ChannelContentData.ResultBean> mChannelList = new ArrayList<>();

    public ChannelContentAdaptor(Context context, OnRecyclerViewItemClickListener clickListener) {
        mContext = context;
        mOnItemClickListener = clickListener;
    }

    @Override
    public ChannelContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.item_channel, parent, false);
        WindowUtil.resizeRecursively(view);
        ChannelContentViewHolder holder = new ChannelContentViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }


    @Override
    public void onBindViewHolder(final ChannelContentViewHolder holder, int position) {
        holder.video_name.setText(getItem(position).getCname());
        Glide.with(mContext).load(getItem(position).getCpic()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.video_img);
        if (mOnItemClickListener != null) {
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(holder.itemView, position, getItem(position)); // 2
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mChannelList.size();
    }

    private ChannelContentData.ResultBean getItem(int position) {
        return mChannelList.get(position);
    }

    @Override
    public void onClick(View view) {

    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void showChannelView(List<ChannelContentData.ResultBean> channelDatas) {
        mChannelList = channelDatas;
        this.notifyDataSetChanged();
    }

    class ChannelContentViewHolder extends RecyclerView.ViewHolder {
        TextView video_name;
        ImageView video_img;

        public ChannelContentViewHolder(View view) {
            super(view);
            video_name = (TextView) view.findViewById(R.id.channel_text);
            video_img = (ImageView) view.findViewById(R.id.channel_image);
        }
    }
}
