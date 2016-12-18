package com.app.video.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.basevideo.util.ListUtil;
import com.app.basevideo.util.WindowUtil;
import com.app.video.R;
import com.app.video.data.VideoData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class LanZuanFragmentAdaptor extends RecyclerView.Adapter<LanZuanFragmentAdaptor.VIPViewHolder> implements View.OnClickListener {

    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener mOnItemClickListener;
    private Context mContext;
    private static final int IS_HEADER = 2;
    private static final int IS_FOOTER = 3;
    private static final int IS_NORMAL = 1;
    private List<VideoData.Page.Video> mVIPList = new ArrayList<>();

    public LanZuanFragmentAdaptor(Context context) {
        mContext = context;
    }

    @Override
    public VIPViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;

        this.mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.item_vip, parent, false);
        view.setOnClickListener(this);
        WindowUtil.resizeRecursively(view);
        VIPViewHolder holder = new VIPViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final VIPViewHolder holder, int position) {

        holder.video_name.setText(getItem(position).getDname());
        holder.video_type.setText(mVIPList.get(position).getDlabel());
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
        return ListUtil.getCount(mVIPList);
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == 0) {
//            return IS_HEADER;
//        }
        return IS_NORMAL;
    }

    private VideoData.Page.Video getItem(int position) {
        return ListUtil.getItem(mVIPList, position);
    }

    private List<String> urlList = new ArrayList<>();

    private List<String> getBannerImageUrlList() {
        return urlList;
    }

    @Override
    public void onClick(View view) {

    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public void showVIPView(List<VideoData.Page.Video> videoDatas, List<VideoData.Page.Banner> bannerList) {
        mVIPList = videoDatas;
        this.notifyDataSetChanged();

    }

    class VIPViewHolder extends RecyclerView.ViewHolder {
        TextView video_name;
        ImageView video_img;
        TextView video_type;
        Banner binner;

        public VIPViewHolder(View view) {
            super(view);
            video_name = (TextView) view.findViewById(R.id.vip_name);
            video_img = (ImageView) view.findViewById(R.id.vip_image);
            video_type = (TextView) view.findViewById(R.id.home_type);
            binner = (Banner) view.findViewById(R.id.home_banner);
        }
    }
}
