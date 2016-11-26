package com.app.video.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.util.WindowUtil;
import com.app.video.R;
import com.app.video.adaptor.PreListAdapter;
import com.app.video.config.Data;

import java.util.ArrayList;
import java.util.List;

public class PreplayActivity extends MFBaseActivity {

    private ImageView player_img;
    private ScrollView scroll;

    private RecyclerView listView;
    private PreListAdapter adapter;
    private List<Data> datalist;

    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preplay);
        player_img = (ImageView) findViewById(R.id.player_img);
        listView = (RecyclerView) findViewById(R.id.pre_list);
        scroll = (ScrollView) findViewById(R.id.scroll);

        LayoutInflater inflate = LayoutInflater.from(this);
        View contentView = inflate.inflate(R.layout.activity_preplay, null);
        WindowUtil.resizeRecursively(contentView);

        initdata();
        adapter = new PreListAdapter(this,datalist);

        scroll.setFocusable(true);
        scroll.setFocusableInTouchMode(true);
        scroll.requestFocus();

        listView.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        listView.setAdapter(adapter);


        Intent intent = getIntent();
        url = intent.getStringExtra("path");

        player_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreplayActivity.this,VideoPlayerActivity.class);
                intent.putExtra("path", url);
                startActivity(intent);
            }
        });

    }

    private void initdata() {
        datalist = new ArrayList<Data>();
        for(int i=0;i<10;i++){
            Data data = new Data();
            data.setImg("http://img.taopic.com/uploads/allimg/121017/234940-12101FR22825.jpg");
            data.setName("日我啊");
            data.setInfo("不日白不日.....");
            data.setTime("1分钟之前");
            datalist.add(data);
        }
    }
}
