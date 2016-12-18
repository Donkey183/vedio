package com.app.video.ui.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.basevideo.base.MFBaseMVCView;
import com.app.basevideo.framework.util.LogUtil;
import com.app.video.R;
import com.app.video.config.Constants;
import com.app.video.config.Settings;
import com.app.video.ui.activity.HomeActivity;
import com.app.video.ui.activity.UserInfoActivity;
import com.app.video.ui.fragment.BlackGoldFragment;
import com.app.video.ui.fragment.BlueFragment;
import com.app.video.ui.fragment.ChannelFragment;
import com.app.video.ui.fragment.CrownFragment;
import com.app.video.ui.fragment.DiamondFragment;
import com.app.video.ui.fragment.ForumFragment;
import com.app.video.ui.fragment.GoldFragment;
import com.app.video.ui.fragment.PurpleFragment;
import com.app.video.ui.fragment.RecommendFragment;
import com.app.video.ui.fragment.VaultFragment;

public class HomeActivityView extends MFBaseMVCView {

    private HomeActivity mActivity;
    private View.OnClickListener mOnClickListener;

    private RecommendFragment mRecommendFragment;
    private ChannelFragment channelFragment;
    private VaultFragment vaultFragment;
    private ForumFragment forumFragment;
    private BlackGoldFragment blackGoldFragment;
    private GoldFragment goldFragment;
    private DiamondFragment diamondFragment;
    private CrownFragment crownFragment;
    private BlueFragment blueFragment;
    private PurpleFragment purpleFragment;

    private FragmentManager fm;
    private FragmentTransaction transaction;

    private ImageView main_user;
    private ImageView main_home;
    private ImageView main_vip;
    private ImageView main_channel;
    private ImageView main_vault;
    private ImageView main_forum;

    private TextView tittle_text;

    private TextView text_home;
    private TextView text_vip;
    private TextView text_channel;
    private TextView text_vault;
    private TextView text_forum;

    private RelativeLayout home_layout;
    private RelativeLayout vip_layout;
    private RelativeLayout channel_layout;
    private RelativeLayout vault_layout;
    private RelativeLayout forum_layout;

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

        text_home = (TextView) mActivity.findViewById(R.id.text_home);
        text_vip = (TextView) mActivity.findViewById(R.id.text_vip);
        text_vault = (TextView) mActivity.findViewById(R.id.text_vault);
        text_forum = (TextView) mActivity.findViewById(R.id.text_forum);
        text_channel = (TextView) mActivity.findViewById(R.id.text_channel);

        home_layout = (RelativeLayout) mActivity.findViewById(R.id.home_layout);
        vip_layout = (RelativeLayout) mActivity.findViewById(R.id.vip_layout);
        channel_layout = (RelativeLayout) mActivity.findViewById(R.id.channel_layout);
        vault_layout = (RelativeLayout) mActivity.findViewById(R.id.vault_layout);
        forum_layout = (RelativeLayout) mActivity.findViewById(R.id.forum_layout);

        text_vip.setText(Constants.config.getTittle_vip());
        text_home.setText(Constants.config.getTittle_first());
        tittle_text.setText(Constants.config.getTittle_first());
        main_home.setImageResource(Constants.config.getImg_first1());
        main_vip.setImageResource(Constants.config.getImg_vip1());

        main_user.setOnClickListener(mOnClickListener);
        vip_layout.setOnClickListener(mOnClickListener);
        home_layout.setOnClickListener(mOnClickListener);
        channel_layout.setOnClickListener(mOnClickListener);
        vault_layout.setOnClickListener(mOnClickListener);
        forum_layout.setOnClickListener(mOnClickListener);

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
        fm = mActivity.getFragmentManager();
        transaction = fm.beginTransaction();
        if (forumFragment == null) {
            forumFragment = new ForumFragment();
            transaction.add(R.id.main_frame, forumFragment);
        }
        if (vaultFragment == null) {
            vaultFragment = new VaultFragment();
            transaction.add(R.id.main_frame, vaultFragment);
        }
        if (channelFragment == null) {
            channelFragment = new ChannelFragment();
            transaction.add(R.id.main_frame, channelFragment);
        }

        if (blackGoldFragment == null) {
            blackGoldFragment = new BlackGoldFragment();
            transaction.add(R.id.main_frame, blackGoldFragment);
        }

        if (mRecommendFragment == null) {
            mRecommendFragment = new RecommendFragment();
            transaction.add(R.id.main_frame, mRecommendFragment);
        }

        if (goldFragment == null) {
            goldFragment = new GoldFragment();
            transaction.add(R.id.main_frame, goldFragment);
        }
        if (diamondFragment == null) {
            diamondFragment = new DiamondFragment();
            transaction.add(R.id.main_frame, diamondFragment);
        }
        if (crownFragment == null) {
            crownFragment = new CrownFragment();
            transaction.add(R.id.main_frame, crownFragment);
        }
        if (purpleFragment == null) {
            purpleFragment = new PurpleFragment();
            transaction.add(R.id.main_frame, purpleFragment);
        }
        if (blueFragment == null) {
            blueFragment = new BlueFragment();
            transaction.add(R.id.main_frame, blueFragment);
        }
        transaction.commit();
        resetTabUi(main_home, checkFragment(Constants.config.getTittle_first()));
    }

    public void clickForum() {
        tittle_text.setText("论坛");
        text_forum.setText("论坛");
        resetTabUi(main_forum, forumFragment);
    }

    public void clickVault() {
        tittle_text.setText("顶级片库");
        text_vault.setText("片库");
        resetTabUi(main_vault, vaultFragment);
    }

    public void clickChannel() {
        tittle_text.setText("频道");
        text_channel.setText("频道");
        resetTabUi(main_channel, channelFragment);
    }

    public void clickUser() {
        Intent intent = new Intent(mActivity, UserInfoActivity.class);
        mActivity.startActivity(intent);
    }

    public void clickVip() {
        text_vip.setText(Constants.config.getTittle_vip());
        tittle_text.setText(Constants.config.getTittle_vip());
        resetTabUi(main_vip, checkFragment(Constants.config.getTittle_vip()));
    }

    public void clickHome() {
        text_home.setText(Constants.config.getTittle_first());
        tittle_text.setText(Constants.config.getTittle_first());
        resetTabUi(main_home, checkFragment(Constants.config.getTittle_first()));
    }

    private void resetTabUi(View selectedView, Fragment selectedFragMent) {
        main_home.setImageResource(selectedView == main_home ? Constants.config.getImg_first2() : Constants.config.getImg_first1());
        text_home.setTextColor(selectedView == main_home ? mActivity.getResources().getColor(R.color.finance_fd6b6b) : mActivity.getResources().getColor(R.color.finance_515151));

        main_vip.setImageResource(selectedView == main_vip ? Constants.config.getImg_vip2() : Constants.config.getImg_vip1());
        text_vip.setTextColor(selectedView == main_vip ? mActivity.getResources().getColor(R.color.finance_fd6b6b) : mActivity.getResources().getColor(R.color.finance_515151));

        main_channel.setImageResource(selectedView == main_channel ? R.drawable.channel2 : R.drawable.channel);
        text_channel.setTextColor(selectedView == main_channel ? mActivity.getResources().getColor(R.color.finance_fd6b6b) : mActivity.getResources().getColor(R.color.finance_515151));

        main_vault.setImageResource(selectedView == main_vault ? R.drawable.vault2 : R.drawable.vault);
        text_vault.setTextColor(selectedView == main_vault ? mActivity.getResources().getColor(R.color.finance_fd6b6b) : mActivity.getResources().getColor(R.color.finance_515151));

        main_forum.setImageResource(selectedView == main_forum ? R.drawable.forum2 : R.drawable.forum);
        text_forum.setTextColor(selectedView == main_forum ? mActivity.getResources().getColor(R.color.finance_fd6b6b) : mActivity.getResources().getColor(R.color.finance_515151));

        fm = mActivity.getFragmentManager();
        transaction = fm.beginTransaction();
        try {
            hideFragment(selectedFragMent);
            transaction.commit();
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }

    }

    private Fragment checkFragment(String str) {
        if (str.equals("体验区")) {
            return mRecommendFragment;
        } else if (str.equals("黄金区")) {
            return goldFragment;
        } else if (str.equals("钻石区")) {
            return diamondFragment;
        } else if (str.equals("黑金区")) {
            return blackGoldFragment;
        } else if (str.equals("皇冠区")) {
            return crownFragment;
        } else if (str.equals("紫钻区")) {
            return purpleFragment;
        } else if (str.equals("蓝钻区")) {
            return blueFragment;
        } else {
            return crownFragment;
        }
    }

    private void hideFragment(Fragment fragment) {
        if (channelFragment != null) {
            if (fragment instanceof ChannelFragment){
                transaction.show(channelFragment);
            }else{
                transaction.hide(channelFragment);
            }
        }
        if (vaultFragment != null) {
            if (fragment instanceof VaultFragment){
                transaction.show(vaultFragment);
            }else{
                transaction.hide(vaultFragment);
            }
        }
        if (forumFragment != null) {
            if (fragment instanceof ForumFragment){
                transaction.show(forumFragment);
            }else{
                transaction.hide(forumFragment);
            }
        }
        if (blackGoldFragment != null) {
            if (fragment instanceof BlackGoldFragment){
                transaction.show(blackGoldFragment);
            }else{
                transaction.hide(blackGoldFragment);
            }
        }
        if (mRecommendFragment != null) {
            if (fragment instanceof RecommendFragment){
                transaction.show(mRecommendFragment);
            }else{
                transaction.hide(mRecommendFragment);
            }
        }
        if (goldFragment != null) {
            if (fragment instanceof GoldFragment){
                transaction.show(goldFragment);
            }else{
                transaction.hide(goldFragment);
            }
        }
        if (diamondFragment != null) {
            if (fragment instanceof DiamondFragment){
                transaction.show(diamondFragment);
            }else{
                transaction.hide(diamondFragment);
            }
        }
        if (crownFragment != null) {
            if (fragment instanceof CrownFragment){
                transaction.show(crownFragment);
            }else{
                transaction.hide(crownFragment);
            }
        }
        if (blueFragment != null) {
            if (fragment instanceof BlueFragment){
                transaction.show(blueFragment);
            }else{
                transaction.hide(blueFragment);
            }
        }
        if (purpleFragment != null) {
            if (fragment instanceof PurpleFragment){
                transaction.show(purpleFragment);
            }else{
                transaction.hide(purpleFragment);
            }
        }
    }


    @Override
    protected int getLayoutRecourseId() {
        return R.layout.activity_home;
    }

    @Override
    protected void onDestroy() {

    }
}
