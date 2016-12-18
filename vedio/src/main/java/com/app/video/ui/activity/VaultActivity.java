package com.app.video.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.INetFinish;
import com.app.basevideo.util.WindowUtil;
import com.app.video.R;
import com.app.video.adaptor.LanZuanFragmentAdaptor;
import com.app.video.config.Constants;
import com.app.video.config.VedioConstant;
import com.app.video.data.VideoData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.model.VideoActivityModel;
import com.app.video.model.VideoModel;
import com.app.video.ui.fragment.BlueFragment;
import com.app.video.ui.widget.CommonAlert;
import com.app.video.util.PlayCountUtil;

public class VaultActivity extends MFBaseActivity implements INetFinish, OnRecyclerViewItemClickListener, View.OnClickListener {

    private RecyclerView vip_recyclerView;
    private LanZuanFragmentAdaptor mAdapter;
    private VideoActivityModel mModel;
    private ImageView vault_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault);
        mModel = new VideoActivityModel(this);
        vault_back = (ImageView) findViewById(R.id.vault_back);
        vault_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final LayoutInflater inflate = LayoutInflater.from(this);
        View view = inflate.inflate(R.layout.activity_vault, null, false);
        vip_recyclerView = (RecyclerView) findViewById(R.id.vip_recycler);
        GridLayoutManager manager = new GridLayoutManager(this, 3) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        vip_recyclerView.setLayoutManager(manager);

        WindowUtil.resizeRecursively(view);


        getVideoInfo();
    }


    private void getVideoInfo() {
        CommonHttpRequest request = new CommonHttpRequest();
        request.addParam(VedioConstant.R_TYPE, "6");
        request.addParam(VedioConstant.PAGE_NO, "1");
        mModel.sendHttpRequest(request, VideoModel.GET_VEDIO_BLUE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getVideoInfo();
    }

    @Override
    public void onHttpResponse(CommonMessage<?> responsedMessage) {
        mAdapter = new LanZuanFragmentAdaptor(this.getApplicationContext());
        mAdapter.setOnItemClickListener(this);
        vip_recyclerView.setAdapter(mAdapter);
        mAdapter.showVIPView(mModel.videoData.page.result, mModel.videoData.page.list1);
    }

    @Override
    public void onItemClick(View view, int position, Object obj) {
        if (Constants.config.getVip_now().equals(Constants.PURPLE)) {
            CommonAlert alert = new CommonAlert(this);
            alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.vip_layout);
        } else {
            if (!PlayCountUtil.hasAuth("BLUE") && Constants.config.getVip_now().equals(Constants.BLUE)) {
                CommonAlert alert = new CommonAlert(this);
                alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.forum_layout);
                return;
            }
            String recourseUrl = "";
            String img = "";
            if (obj instanceof VideoData.Page.Video) {
                recourseUrl = ((VideoData.Page.Video) obj).getDyres();
                img = ((VideoData.Page.Video) obj).getDypic();
            } else if (obj instanceof VideoData.Page.Banner) {
                recourseUrl = ((VideoData.Page.Banner) obj).getDyresource();
                img = ((VideoData.Page.Banner) obj).getDypic();
            }
            Intent intent = new Intent(this, VideoPlayerActivity.class);
            intent.putExtra("path", recourseUrl);
            intent.putExtra("img", img);
            intent.putExtra("parent", "");
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View view) {
        if (!Constants.config.getVip_now().equals(Constants.RED)) {
            CommonAlert alert = new CommonAlert(this);
            alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.vip_layout);
        }
    }
}
