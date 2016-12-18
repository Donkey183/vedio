package com.app.video.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.config.VedioCmd;
import com.app.basevideo.framework.manager.MessageManager;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.INetFinish;
import com.app.basevideo.util.WindowUtil;
import com.app.video.R;
import com.app.video.adaptor.VaultContentAdaptor;
import com.app.video.config.Constants;
import com.app.video.config.VedioConstant;
import com.app.video.data.VideoData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.model.VaultContentModel;
import com.app.video.ui.widget.CommonAlert;
import com.app.video.util.PlayCountUtil;

public class VaultActivity extends MFBaseActivity implements INetFinish, OnRecyclerViewItemClickListener, View.OnClickListener {

    private RecyclerView vip_recyclerView;
    private VaultContentAdaptor mAdapter;
    private VaultContentModel mModel;
    private ImageView vault_back;
    private TextView vault_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault);
        mModel = new VaultContentModel(this);
        vault_back = (ImageView) findViewById(R.id.vault_back);
        vault_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        vault_text = (TextView) findViewById(R.id.vault_text);
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

        String pid = getIntent().getExtras().getString("pid");
        vault_text.setText(getIntent().getExtras().getString("tittle"));
        getVideoInfo(pid);
    }


    private void getVideoInfo(String pid) {
        CommonHttpRequest request = new CommonHttpRequest();
        request.addParam(VedioConstant.PID, pid);
        mModel.sendHttpRequest(request, VaultContentModel.GET_VAULT_CONTENT);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHttpResponse(CommonMessage<?> responsedMessage) {
        mAdapter = new VaultContentAdaptor(this.getApplicationContext());
        mAdapter.setOnItemClickListener(this);
        vip_recyclerView.setAdapter(mAdapter);
        mAdapter.showVIPView(mModel.vaultContentDatas);
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


    @Override
    protected void onDestroy() {
        MessageManager.getInstance().dispatchResponsedMessage(new CommonMessage<String>(VedioCmd.TITLE_CHANGE2, "顶级片库"));
        super.onDestroy();
    }
}
