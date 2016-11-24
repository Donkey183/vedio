package com.app.video.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.video.R;
import com.app.video.util.GallyPageTransformer;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class GalleryFragment extends android.app.Fragment {
    private ViewPager gallery_pager;
    private LinearLayout ll_main;
    private List<ImageView> imageViews;
    private int pagerWidth;

    private String[] imgurls = {"http://img.taopic.com/uploads/allimg/121017/234940-12101FR22825.jpg",
            "http://pic44.nipic.com/20140721/11624852_001107119409_2.jpg",
            "http://pic47.nipic.com/20140901/6608733_145238341000_2.jpg",
            "http://pic45.nipic.com/20140807/2531170_221641791877_2.jpg",
            "http://pic6.huitu.com/res/20130116/84481_20130116142820494200_1.jpg",
            "http://img05.tooopen.com/images/20140604/sy_62331342149.jpg",
            "http://img03.tooopen.com/images/20131102/sy_45238929299.jpg",
            "http://img01.taopic.com/151227/234973-15122G5550795.jpg",
            "http://img.taopic.com/uploads/allimg/120423/107913-12042323220753.jpg",
            "http://img05.tooopen.com/images/20150531/tooopen_sy_127457023651.jpg",
            "http://pic32.nipic.com/20130815/10675263_110224052319_2.jpg"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        gallery_pager = (ViewPager) view.findViewById(R.id.gallery_pager);
        ll_main = (LinearLayout) view.findViewById(R.id.gallery_layout);
        initdata();
        gallery_pager.setOffscreenPageLimit(3);
        pagerWidth = (int) (getResources().getDisplayMetrics().widthPixels * 3.0f / 5.0f);
        ViewGroup.LayoutParams lp = gallery_pager.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(pagerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            lp.width = pagerWidth;
        }
        gallery_pager.setLayoutParams(lp);
        gallery_pager.setPageMargin(-50);
        ll_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gallery_pager.dispatchTouchEvent(motionEvent);
            }
        });
        gallery_pager.setPageTransformer(true, new GallyPageTransformer());
        gallery_pager.setAdapter(new MyViewPagerAdapter(imageViews));
        return view;
    }

    private void initdata() {
        imageViews = new ArrayList<>();
        ImageView first = new ImageView(getActivity());
        ImageView second = new ImageView(getActivity());
        ImageView third = new ImageView(getActivity());
        ImageView fourth = new ImageView(getActivity());
        ImageView fifth = new ImageView(getActivity());


        Glide.with(GalleryFragment.this).load(imgurls[1]).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(first);
        Glide.with(GalleryFragment.this).load(imgurls[2]).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(second);
        Glide.with(GalleryFragment.this).load(imgurls[3]).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(third);
        Glide.with(GalleryFragment.this).load(imgurls[4]).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(fourth);
        Glide.with(GalleryFragment.this).load(imgurls[5]).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(fifth);


        imageViews.add(first);
        imageViews.add(second);
        imageViews.add(third);
        imageViews.add(fourth);
        imageViews.add(fifth);
    }
}
