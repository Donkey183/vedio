package com.app.video.ui.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;

import com.app.basevideo.base.MFBaseMVCView;
import com.app.video.R;
import com.app.video.config.Settings;
import com.app.video.ui.activity.HomeActivity;
import com.app.video.ui.fragment.ChannelFragment;
import com.app.video.ui.fragment.ForumFragment;
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

    private ImageView main_user;

    private ImageView main_home;
    private ImageView main_vip;
    private ImageView main_channel;
    private ImageView main_vault;
    private ImageView main_forum;

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
        main_home.setImageResource(R.drawable.home2);
        main_vip.setImageResource(R.drawable.vip);
        FragmentManager fm = mActivity.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mRecommendFragment = new RecommendFragment();
        vipFragment = new VIPFragment();
        channelFragment = new ChannelFragment();
        vaultFragment = new VaultFragment();
        forumFragment = new ForumFragment();
        transaction.replace(R.id.main_frame, mRecommendFragment);
        transaction.commit();
    }


    public void clickForum() {
        resetTabUi(main_forum, forumFragment);
    }

    public void clickVault() {
        resetTabUi(main_vault, vaultFragment);
    }

    public void clickChannel() {
        resetTabUi(main_channel, channelFragment);
    }

    public void clickUser() {

    }

    public void clickVip() {
        resetTabUi(main_vip, vipFragment);
    }

    public void clickHome() {
        resetTabUi(main_home, mRecommendFragment);
    }

    private void resetTabUi(View selectedView, Fragment selectedFragMent) {
        main_home.setImageResource(selectedView == main_home ? R.drawable.home : R.drawable.home2);
        main_vip.setImageResource(selectedView == main_vip ? R.drawable.vip : R.drawable.vip2);
        main_channel.setImageResource(selectedView == main_channel ? R.drawable.channel : R.drawable.channel2);
        main_vault.setImageResource(selectedView == main_vault ? R.drawable.vault : R.drawable.vault2);
        main_forum.setImageResource(selectedView == main_forum ? R.drawable.forum : R.drawable.forum2);
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
