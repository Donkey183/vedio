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
import com.app.video.data.ChannelData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.bumptech.glide.Glide;

public class ChannelAdaptor extends RecyclerView.Adapter<ChannelAdaptor.ChannelViewHolder> implements View.OnClickListener {

    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private Context mContext;
    private ChannelData mChannelData;

    public ChannelAdaptor(Context context) {
        mContext = context;
    }

    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.item_channel, parent, false);
        WindowUtil.resizeRecursively(view);
        ChannelViewHolder holder = new ChannelViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }


    @Override
    public void onBindViewHolder(final ChannelViewHolder holder, int position) {
        holder.video_name.setText(getItem(position).getDname());
        Glide.with(mContext).load(getItem(position).getDpic()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.video_img);
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
        return mChannelData.channelList.size();
    }

    private ChannelData.Channel getItem(int position) {
        return mChannelData.channelList.get(position);
    }

    @Override
    public void onClick(View view) {

    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setChannelData(ChannelData channelData) {
        mChannelData = channelData;
    }

    class ChannelViewHolder extends RecyclerView.ViewHolder {
        TextView video_name;
        ImageView video_img;

        public ChannelViewHolder(View view) {
            super(view);
            video_name = (TextView) view.findViewById(R.id.channel_text);
            video_img = (ImageView) view.findViewById(R.id.channel_image);
        }
    }


}
