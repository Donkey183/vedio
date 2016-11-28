package com.app.video.adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.basevideo.framework.util.LogUtil;
import com.app.basevideo.util.ListUtil;
import com.app.basevideo.util.WindowUtil;
import com.app.video.R;
import com.app.video.data.VaultData;
import com.app.video.data.VideoData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.util.GlideImageLoader;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

public class RecommendAdaptor extends RecyclerView.Adapter<RecommendAdaptor.RecommendViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<VaultData> mVaultList;
    private static final int IS_HEADER = 2;
    private static final int IS_FOOTER = 3;
    private static final int IS_NORMAL = 1;
    private View mHeaderView;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private List<VideoData.Page.Video> mVideoList;

    public RecommendAdaptor(Context context) {
        mContext = context;
    }

    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public void showRecommendView(List<VideoData.Page.Video> videoDataList) {
        if (videoDataList == null) {
            LogUtil.e("showRecommendView failed,videoDataList is null");
            return;
        }
        mVideoList = videoDataList;

        this.notifyDataSetChanged();
    }

    private VideoData.Page.Video getItem(int position) {
        return ListUtil.getItem(mVideoList, position);
    }

    private List<String> getBannerImageUrlList() {
        List<String> urlList = new ArrayList<>();
        for (VideoData.Page.Video video : mVideoList) {
            if (video.getDypic() != null && !"".endsWith(video.getDypic()))
                urlList.add(video.getDypic());
        }
        return urlList;
    }

    @Override
    public RecommendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == IS_HEADER) {
            view = (Banner) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.head_banner_layout, parent, false);
            Banner banner = (Banner) view.findViewById(R.id.home_banner);
            banner.setImageLoader(new GlideImageLoader());
            banner.setImages(getBannerImageUrlList());
            banner.setOnBannerClickListener(new OnBannerClickListener() {
                @Override
                public void OnBannerClick(int position) {
                    Log.d("Banner:", getBannerImageUrlList().get(position - 1));
                }
            });
            banner.start();

        } else {
            this.mInflater = LayoutInflater.from(mContext);
            view = mInflater.inflate(R.layout.item_home, parent, false);
            WindowUtil.resizeRecursively(view);
            view.setOnClickListener(null);
        }
        RecommendViewHolder holder = new RecommendViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecommendViewHolder holder, int position) {
        if (getItemViewType(position) == IS_HEADER) return;

        holder.video_name.setText(getItem(position).getDname());
        holder.video_type.setText(mVideoList.get(position).getDytype());
        Glide.with(mContext)
                .load(mVideoList.get(position).getDypic())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.video_img);
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

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return ListUtil.getCount(mVideoList);
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) return IS_HEADER;
        return IS_NORMAL;
    }

    class RecommendViewHolder extends RecyclerView.ViewHolder {
        TextView video_name;
        ImageView video_img;
        TextView video_type;
        Banner binner;

        public RecommendViewHolder(View view) {
            super(view);
            video_name = (TextView) view.findViewById(R.id.home_name);
            video_img = (ImageView) view.findViewById(R.id.home_image);
            video_type = (TextView) view.findViewById(R.id.home_type);
            binner = (Banner) view.findViewById(R.id.home_banner);
        }
    }
}