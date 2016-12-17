package com.app.video.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.basevideo.framework.util.LogUtil;
import com.app.basevideo.util.ListUtil;
import com.app.basevideo.util.WindowUtil;
import com.app.video.R;
import com.app.video.data.VideoData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.util.GlideImageLoader;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

public class ZiZuanFragmentAdaptor extends RecyclerView.Adapter<ZiZuanFragmentAdaptor.VIPViewHolder> implements View.OnClickListener {

    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener mOnItemClickListener;
    private Context mContext;
    private static final int IS_HEADER = 2;
    private static final int IS_FOOTER = 3;
    private static final int IS_NORMAL = 1;
    private List<VideoData.Page.Video> mVIPList = new ArrayList<>();

    private List<VideoData.Page.Banner> mBannerList;


    public ZiZuanFragmentAdaptor(Context context) {
        mContext = context;
    }

    @Override
    public VIPViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view;


        if (viewType == IS_HEADER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.head_banner_layout, parent, false);
            WindowUtil.resizeRecursively(view);
            Banner banner = (Banner) view.findViewById(R.id.home_banner);
            banner.setImageLoader(new GlideImageLoader());
            banner.setImages(getBannerImageUrlList());
            banner.setOnBannerClickListener(new OnBannerClickListener() {
                @Override
                public void OnBannerClick(int position) {
                    mOnItemClickListener.onItemClick(view, position, getBanner(position));
                }
            });
            banner.start();

        } else {
            this.mInflater = LayoutInflater.from(mContext);
            view = mInflater.inflate(R.layout.item_vip, parent, false);
            view.setOnClickListener(this);
            WindowUtil.resizeRecursively(view);
        }
        VIPViewHolder holder = new VIPViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final VIPViewHolder holder, int position) {
        if (getItemViewType(position) == IS_HEADER) return;

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
        return ListUtil.getCount(mVIPList);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return IS_HEADER;
        }
        return IS_NORMAL;
    }

    private VideoData.Page.Video getItem(int position) {
        return ListUtil.getItem(mVIPList, position);
    }

    private VideoData.Page.Banner getBanner(int position) {
        return ListUtil.getItem(mBannerList, position - 1);
    }

    private List<String> getBannerImageUrlList() {

        List<String> urlList = new ArrayList<>();

        for (VideoData.Page.Banner banner : mBannerList) {
            if (banner.getDypic() != null && !"".endsWith(banner.getDypic()))
                urlList.add(banner.getDypic());
        }

        return urlList;
    }

    @Override
    public void onClick(View view) {

    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public void showVIPView(List<VideoData.Page.Video> videoDatas, List<VideoData.Page.Banner> bannerList) {
        try {
            mVIPList = videoDatas;
            this.mBannerList = bannerList;
            this.notifyDataSetChanged();
        } catch (Exception e) {
            LogUtil.e("showVIPView" + e.getMessage());
        }

    }

    class VIPViewHolder extends RecyclerView.ViewHolder {
        TextView video_name;
        ImageView video_img;
        Banner binner;

        public VIPViewHolder(View view) {
            super(view);
            video_name = (TextView) view.findViewById(R.id.vip_name);
            video_img = (ImageView) view.findViewById(R.id.vip_image);
            binner = (Banner) view.findViewById(R.id.home_banner);
        }
    }
}
