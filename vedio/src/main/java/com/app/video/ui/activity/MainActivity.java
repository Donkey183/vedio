package com.app.video.ui.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.app.video.R;
import com.app.video.config.Settings;
import com.app.video.ui.fragment.ChannelFragment;
import com.app.video.ui.fragment.HomeFragment;
import com.app.video.ui.fragment.VIPFragment;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private HomeFragment homeFragment;
    private VIPFragment vipFragment;
    private ChannelFragment channelFragment;

    private ImageView main_user;

    private ImageView main_home;
    private ImageView main_vip;
    private ImageView main_channel;

    private SharedPreferences settings ;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        main_user = (ImageView) findViewById(R.id.main_user);
        main_home = (ImageView) findViewById(R.id.main_home);
        main_vip = (ImageView) findViewById(R.id.main_vip);
        main_channel = (ImageView) findViewById(R.id.main_channel);


        main_user.setOnClickListener(this);
        main_vip.setOnClickListener(this);
        main_home.setOnClickListener(this);
        main_channel.setOnClickListener(this);

        initSetting();
        setDefaultFragment();
    }

    private void initSetting() {
        settings = getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString("choose_view", Settings.USESUFACE);
        editor.putString("choose_decode",Settings.USESOFT);
        editor.putString("choose_debug",Settings.DEBUGOFF);
        editor.commit();
    }

    private void setDefaultFragment() {

        main_home.setImageResource(R.drawable.home2);
        main_vip.setImageResource(R.drawable.vip);
        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        homeFragment = new HomeFragment();
        vipFragment = new VIPFragment();
        channelFragment = new ChannelFragment();
        transaction.replace(R.id.main_frame,homeFragment);
        transaction.commit();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_home:
                clickHome();
                break;
            case R.id.main_vip:
                clickVip();
                break;
            case R.id.main_user:
                clickUser();
                break;
            case R.id.main_channel:
                clickChannel();
                break;
            default:
                break;
        }
    }

    private void clickChannel() {
        main_home.setImageResource(R.drawable.home);
        main_vip.setImageResource(R.drawable.vip);
        main_channel.setImageResource(R.drawable.channel2);
        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_frame,channelFragment);
        transaction.commit();
    }

    private void clickUser() {

    }

    private void clickVip() {
        main_home.setImageResource(R.drawable.home);
        main_vip.setImageResource(R.drawable.vip2);
        main_channel.setImageResource(R.drawable.channel);
        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_frame,vipFragment);
        transaction.commit();
    }

    private void clickHome() {

        main_home.setImageResource(R.drawable.home2);
        main_vip.setImageResource(R.drawable.vip);
        main_channel.setImageResource(R.drawable.channel);
        FragmentManager fm = this.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_frame,homeFragment);
        transaction.commit();
    }
}
