package com.app.video.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.basevideo.util.WindowUtil;
import com.app.video.R;
import com.app.video.config.Video;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.ui.activity.PreplayActivity;
import com.app.video.ui.activity.VideoPlayerActivity;
import com.app.video.util.GlideImageLoader;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;
import java.util.ArrayList;
import java.util.List;

public class RecommendFragment extends android.app.Fragment {
  private RecyclerView my_recyclerView;
  private HomeAdapter mAdapter;

  private String[] imgurls = {
      "http://img.taopic.com/uploads/allimg/121017/234940-12101FR22825.jpg",
      "http://pic44.nipic.com/20140721/11624852_001107119409_2.jpg",
      "http://pic47.nipic.com/20140901/6608733_145238341000_2.jpg",
      "http://pic45.nipic.com/20140807/2531170_221641791877_2.jpg",
      "http://pic6.huitu.com/res/20130116/84481_20130116142820494200_1.jpg",
      "http://img05.tooopen.com/images/20140604/sy_62331342149.jpg",
      "http://img03.tooopen.com/images/20131102/sy_45238929299.jpg",
      "http://img01.taopic.com/151227/234973-15122G5550795.jpg",
      "http://img.taopic.com/uploads/allimg/120423/107913-12042323220753.jpg",
      "http://img05.tooopen.com/images/20150531/tooopen_sy_127457023651.jpg",
      "http://pic32.nipic.com/20130815/10675263_110224052319_2.jpg"
  };

  private List<Video> videoList;
  private List<String> imageList;

  public RecommendFragment() {

  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_recommend, container, false);

    initdata();
    my_recyclerView = (RecyclerView) view.findViewById(R.id.main_recycler);
    GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
    manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
      @Override public int getSpanSize(int position) {
        return position == 0 ? 2 : 1;
      }
    });
    my_recyclerView.setLayoutManager(manager);
    my_recyclerView.setAdapter(mAdapter = new HomeAdapter());
    mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
      @Override public void onItemClick(View view, int position, Object object) {
        Video v = videoList.get(position);
        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
        intent.putExtra("path", v.getURL());
        startActivity(intent);
      }
    });

    return view;
  }

  private void initdata() {

    videoList = new ArrayList<Video>();
    imageList = new ArrayList<String>();
    for (int i = 0; i < 30; i++) {
      Video video = new Video();
      video.setName("testname");
      video.setImageurl(imgurls[i % 11]);
      video.setType("超清");
      video.setURL("rtmp://live.hkstv.hk.lxdns.com/live/hks");
      videoList.add(video);
    }

    for (int i = 0; i < 30; i++) {
      Video video = new Video();
      video.setName("testVIP");
      video.setImageurl(imgurls[i % 11]);
      video.setType("vip");
      video.setURL("rtmp://live.hkstv.hk.lxdns.com/live/hks");
      videoList.add(video);
    }
    for (int i = 0; i < 4; i++) {
      imageList.add(videoList.get(i).getImageurl());
    }
  }

  class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
      implements View.OnClickListener {

    private static final int IS_HEADER = 2;
    private static final int IS_FOOTER = 3;
    private static final int IS_NORMAL = 1;

    private LayoutInflater mInflater;
    private View mHeaderView;

    public void setHeaderView(View headerView) {
      mHeaderView = headerView;
      notifyItemInserted(0);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    @Override public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view;
      if (viewType == IS_HEADER) {
        view = (Banner) LayoutInflater.from(parent.getContext())
            .inflate(R.layout.head_banner_layout, parent, false);
        Banner banner = (Banner) view.findViewById(R.id.home_banner);
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(imageList);
        banner.setOnBannerClickListener(new OnBannerClickListener() {
          @Override public void OnBannerClick(int position) {
            Log.d("Banner:", imageList.get(position - 1));
          }
        });
        banner.start();
<<<<<<< HEAD
        my_recyclerView = (RecyclerView) view.findViewById(R.id.main_recycler);
        my_recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        my_recyclerView.setAdapter(mAdapter = new HomeAdapter());

        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object object) {
                Video v = videoList.get(position);
                Intent intent = new Intent(getActivity(),PreplayActivity.class);
                intent.putExtra("path", v.getURL());
                startActivity(intent);
            }
        });

        return view;
=======
      } else {
        this.mInflater = LayoutInflater.from(getActivity());
        view = mInflater.inflate(R.layout.item_home, parent, false);
        WindowUtil.resizeRecursively(view);
        view.setOnClickListener(this);
      }
      MyViewHolder holder = new MyViewHolder(view);
      return holder;
    }
>>>>>>> 053a39537a72102c5c08ea0f2f71a3505357fc8b

    @Override public void onBindViewHolder(final MyViewHolder holder, int position) {
      if (getItemViewType(position) == IS_HEADER) return;

      holder.video_name.setText(videoList.get(position).getName());
      holder.video_type.setText(videoList.get(position).getType());
      Glide.with(RecommendFragment.this)
          .load(videoList.get(position).getImageurl())
          .placeholder(R.mipmap.ic_launcher)
          .error(R.mipmap.ic_launcher)
          .into(holder.video_img);
      if (mOnItemClickListener != null) {
        //为ItemView设置监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            int position = holder.getLayoutPosition(); // 1
            mOnItemClickListener.onItemClick(holder.itemView, position, null); // 2
          }
        });
      }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
      this.mOnItemClickListener = listener;
    }

    @Override public int getItemCount() {
      return videoList.size();
    }

    @Override public void onClick(View view) {

    }

    @Override public int getItemViewType(int position) {
      if (position == 0) return IS_HEADER;
      return IS_NORMAL;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
      TextView video_name;
      ImageView video_img;
      TextView video_type;
      Banner binner;

      public MyViewHolder(View view) {
        super(view);
        video_name = (TextView) view.findViewById(R.id.home_name);
        video_img = (ImageView) view.findViewById(R.id.home_image);
        video_type = (TextView) view.findViewById(R.id.home_type);
        binner = (Banner) view.findViewById(R.id.home_banner);
      }
    }
  }
}
