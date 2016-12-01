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
import com.app.video.data.VaultData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.bumptech.glide.Glide;

import java.util.List;

public class VaultAdaptor extends RecyclerView.Adapter<VaultAdaptor.MyViewHolder> {

    private LayoutInflater mInflater;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private Context mContext;
    private List<VaultData> mVaultList;

    public VaultAdaptor(Context context, OnRecyclerViewItemClickListener onItemClickListener) {
        mContext = context;
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.mInflater = LayoutInflater.from(mContext.getApplicationContext());
        View view = mInflater.inflate(R.layout.item_vault, parent, false);
        WindowUtil.resizeRecursively(view);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final VaultData vault = getItem(position);
        holder.video_visitor.setText(vault.getDnum() + "人点击");
        Glide.with(mContext).load(vault.getDpic()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(holder.video_img);
        holder.video_type.setText(vault.getPname());
        holder.video_updates.setText("更新" + vault.getUpdateNum() + "部");

        if (mOnItemClickListener != null) {
            //为ItemView设置监听器
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition(); // 1
                    mOnItemClickListener.onItemClick(holder.itemView, position, vault); // 2
                }
            });
        }
    }

    public void showVaultView(List<VaultData> vaultList) {
        mVaultList = vaultList;
    }

    private VaultData getItem(int position) {
        return mVaultList.get(position);
    }

    @Override
    public int getItemCount() {
        return mVaultList.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView video_visitor;
        ImageView video_img;
        TextView video_type;
        TextView video_updates;

        public MyViewHolder(View view) {
            super(view);
            video_visitor = (TextView) view.findViewById(R.id.vault_visitor);
            video_img = (ImageView) view.findViewById(R.id.vault_img);
            video_type = (TextView) view.findViewById(R.id.vault_type);
            video_updates = (TextView) view.findViewById(R.id.vault_updatas);
        }
    }
}