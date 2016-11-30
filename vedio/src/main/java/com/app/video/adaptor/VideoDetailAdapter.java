package com.app.video.adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.basevideo.util.ListUtil;
import com.app.basevideo.util.WindowUtil;
import com.app.video.R;
import com.app.video.data.VideoDetailData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

public class VideoDetailAdapter extends RecyclerView.Adapter<VideoDetailAdapter.ViewHolder> {
    private List<VideoDetailData.Detail> mRecommendVideoList;
    private List<VideoDetailData.Detail> mVideoCommentList;
    private List<VideoDetailData.Detail> mPageCommentList;
    private Context context;
    private View.OnClickListener mOnClickListener;

    public VideoDetailAdapter(Context context, View.OnClickListener onClickListener) {
        this.context = context;
        mOnClickListener = onClickListener;
    }

    @Override
    public VideoDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.preplay_item, parent, false);
        WindowUtil.resizeRecursively(view);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.name.setText(getItem(position).getUserName() == null ? ("用户" + (position / 2 * 3 + 7)) : getItem(position).getUserName());
        holder.info.setText(getItem(position).getContent());
        holder.time.setText(getItem(position).getUtime());
        Glide.with(context).load(getItem(position).getUserimg()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.img) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.img.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    private VideoDetailData.Detail getItem(int position) {
        return ListUtil.getItem(mPageCommentList, position);
    }


    public void showVideoDetail(VideoDetailData detailData) {
        mPageCommentList = detailData.pageCommentList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ListUtil.getCount(mPageCommentList);
    }


    public final class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView name;
        public TextView info;
        public TextView time;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.item_name);
            img = (ImageView) view.findViewById(R.id.item_img);
            info = (TextView) view.findViewById(R.id.item_mesg);
            time = (TextView) view.findViewById(R.id.item_time);
        }
    }
}
