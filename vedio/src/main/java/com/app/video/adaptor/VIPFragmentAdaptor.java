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
import com.app.video.data.VideoData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by JiangMin.Wei on 16/11/26.
 */
public class VIPFragmentAdaptor extends RecyclerView.Adapter<VIPFragmentAdaptor.VIPViewHolder> implements View.OnClickListener {

    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private Context mContext;
    private List<VideoData.ResultBean> mVIPList;

    public VIPFragmentAdaptor(Context context) {
        mContext = context;
    }

    @Override
    public VIPViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.item_vip, parent, false);
        VIPViewHolder holder = new VIPViewHolder(view);
        view.setOnClickListener(this);
        WindowUtil.resizeRecursively(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final VIPViewHolder holder, int position) {
        holder.video_name.setText(getItem(position).getDname());
        Glide.with(mContext).load(getItem(position).getDypic()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.video_img);
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
        return mVIPList.size();
    }

    private VideoData.ResultBean getItem(int position) {
        return mVIPList.get(position);
    }

    @Override
    public void onClick(View view) {

    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public void showVIPView(List<VideoData.ResultBean> videoDatas) {
        mVIPList = videoDatas;
        this.notifyDataSetChanged();
    }

    class VIPViewHolder extends RecyclerView.ViewHolder {
        TextView video_name;
        ImageView video_img;

        public VIPViewHolder(View view) {
            super(view);
            video_name = (TextView) view.findViewById(R.id.vip_name);
            video_img = (ImageView) view.findViewById(R.id.vip_image);
        }
    }
}
