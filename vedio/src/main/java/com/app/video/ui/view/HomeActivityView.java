package com.app.video.ui.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.basevideo.base.MFBaseMVCView;
import com.app.video.R;
import com.app.video.config.Constants;
import com.app.video.config.Settings;
import com.app.video.ui.activity.HomeActivity;
import com.app.video.ui.fragment.ChannelFragment;
import com.app.video.ui.fragment.ForumFragment;
import com.app.video.ui.fragment.GalleryFragment;
import com.app.video.ui.fragment.RecommendFragment;
import com.app.video.ui.fragment.VIPFragment;
import com.app.video.ui.fragment.VaultFragment;

/**
 * Created by JiangMin.Wei on 16/11/23.
 */
public class HomeActivityView extends MFBaseMVCView {

    private HomeActivity mActivity;
    private View.OnClickListener mOnClickListener;

    private RecommendFragment mRecommendFragment;
    private VIPFragment vipFragment;
    private ChannelFragment channelFragment;
    private VaultFragment vaultFragment;
    private ForumFragment forumFragment;
    private GalleryFragment galleryFragment;

    private ImageView main_user;
    private ImageView main_home;
    private ImageView main_vip;
    private ImageView main_channel;
    private ImageView main_vault;
    private ImageView main_forum;

    private TextView tittle_text;

    public HomeActivityView(HomeActivity activity, View.OnClickListener onClickListener) {
        super(activity);
        mActivity = activity;
        mOnClickListener = onClickListener;
        init();
    }

    private void init() {
        main_user = (ImageView) mActivity.findViewById(R.id.main_user);
        main_home = (ImageView) mActivity.findViewById(R.id.main_home);
        main_vip = (ImageView) mActivity.findViewById(R.id.main_vip);
        main_channel = (ImageView) mActivity.findViewById(R.id.main_channel);
        main_vault = (ImageView) mActivity.findViewById(R.id.main_vault);
        main_forum = (ImageView) mActivity.findViewById(R.id.main_forum);
        tittle_text = (TextView) mActivity.findViewById(R.id.tittle_text);

        main_user.setOnClickListener(mOnClickListener);
        main_vip.setOnClickListener(mOnClickListener);
        main_home.setOnClickListener(mOnClickListener);
        main_channel.setOnClickListener(mOnClickListener);
        main_vault.setOnClickListener(mOnClickListener);
        main_forum.setOnClickListener(mOnClickListener);

        initSetting();
        setDefaultFragment();
    }

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    private void initSetting() {
        settings = mActivity.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString("choose_view", Settings.USESUFACE);
        editor.putString("choose_decode", Settings.USESOFT);
        editor.putString("choose_debug", Settings.DEBUGOFF);
        editor.commit();
    }

    private void setDefaultFragment() {
        main_home.setImageResource(Constants.config.getImg_first2());
        main_vip.setImageResource(Constants.config.getImg_vip1());
        FragmentManager fm = mActivity.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mRecommendFragment = new RecommendFragment();
        vipFragment = new VIPFragment();
        channelFragment = new ChannelFragment();
        vaultFragment = new VaultFragment();
        forumFragment = new ForumFragment();
        galleryFragment = new GalleryFragment();
        transaction.replace(R.id.main_frame, galleryFragment);
        transaction.commit();
    }

    public void clickForum() {
        tittle_text.setText("论坛");
        resetTabUi(main_forum, forumFragment);
    }

    public void clickVault() {
        tittle_text.setText("顶级片库");
        resetTabUi(main_vault, vaultFragment);
    }

    public void clickChannel() {
        tittle_text.setText("频道");
        resetTabUi(main_channel, channelFragment);
    }

    public void clickUser() {

    }

    public void clickVip() {
        tittle_text.setText(Constants.config.getTittle_vip());
        resetTabUi(main_vip, vipFragment);
    }

    public void clickHome() {
        tittle_text.setText(Constants.config.getTittle_first());
        resetTabUi(main_home, galleryFragment);
    }

    private void resetTabUi(View selectedView, Fragment selectedFragMent) {
        main_home.setImageResource(selectedView == main_home ? Constants.config.getImg_first2(): Constants.config.getImg_first1());
        main_vip.setImageResource(selectedView == main_vip ? Constants.config.getImg_vip2() : Constants.config.getImg_vip1());
        main_channel.setImageResource(selectedView == main_channel ? R.drawable.channel2 : R.drawable.channel);
        main_vault.setImageResource(selectedView == main_vault ? R.drawable.vault2 : R.drawable.vault);
        main_forum.setImageResource(selectedView == main_forum ? R.drawable.forum2 : R.drawable.forum);
        FragmentManager fm = mActivity.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_frame, selectedFragMent);
        transaction.commit();
    }

    @Override
    protected int getLayoutRecourseId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onDestroy() {

    }
}
