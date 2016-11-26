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

import com.app.basevideo.util.WindowUtil;
import com.app.video.R;
import com.app.video.config.Data;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

public class PreListAdapter extends RecyclerView.Adapter<PreListAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private List<Data> data;
    private Context context;

    public PreListAdapter(Context context,List<Data> list){
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.data = list;
    }

    @Override
    public PreListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.preplay_item, parent, false);
        WindowUtil.resizeRecursively(view);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.name.setText(data.get(position).getName());
        holder.info.setText(data.get(position).getInfo());
        holder.time.setText(data.get(position).getTime());
        Glide.with(context).load(data.get(position).getImg()).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.img) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.img.setImageDrawable(circularBitmapDrawable);
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public final class ViewHolder extends RecyclerView.ViewHolder{
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
