package com.app.video.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.cache.MFSimpleCache;
import com.app.video.R;
import com.bumptech.glide.Glide;

public class SpalshActivity extends MFBaseActivity {
    ImageView spalshImg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spalsh_activity);
        spalshImg = (ImageView) findViewById(R.id.spalsh_img);
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SpalshActivity.this, HomeActivity.class);
                startActivity(intent);
                handler.removeCallbacks(this);
                SpalshActivity.this.finish();
            }
        };
        String domain = MFSimpleCache.get(SpalshActivity.this).getAsString("PIC_DOMAIN");
        String url = domain == null ? "http://conf.qiumeng88.com/dapp/qd01.jpg" : domain + "/dapp/qd01.jpg";
        Glide.with(SpalshActivity.this).load(url).into(spalshImg);
        handler.postDelayed(runnable, 2000);
    }
}
