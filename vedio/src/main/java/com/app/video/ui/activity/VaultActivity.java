package com.app.video.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.config.VedioCmd;
import com.app.basevideo.framework.manager.MessageManager;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.INetFinish;
import com.app.video.R;
import com.app.video.adaptor.BlackGoldAdapter2;
import com.app.video.config.VedioConstant;
import com.app.video.data.VaultContentData;
import com.app.video.model.VaultContentModel;
import com.app.video.util.GallyPageTransformer;

import java.util.List;

public class VaultActivity extends MFBaseActivity implements INetFinish {

    private RecyclerView vip_recyclerView;
    private BlackGoldAdapter2 mAdapter;
    private VaultContentModel mModel;
    private ImageView vault_back;
    private TextView vault_text;

    private ViewPager gallery_pager;
    private RelativeLayout ll_main;
    private int pagerWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new VaultContentModel(VaultActivity.this);
        setContentView(R.layout.vault_activity);
        gallery_pager = (ViewPager) findViewById(R.id.gallery_pager);
        ll_main = (RelativeLayout) findViewById(R.id.gallery_layout);
        vault_back = (ImageView) findViewById(R.id.vault_back);
        vault_text = (TextView) findViewById(R.id.vault_text);
        vault_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        vault_text.setText(getIntent().getExtras().getString("tittle"));

        gallery_pager.setOffscreenPageLimit(3);
        pagerWidth = (int) (getResources().getDisplayMetrics().widthPixels * 3.0f / 3.6f);
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
        String pid = getIntent().getExtras().getString("pid");
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
        List<VaultContentData> resultBeenList = mModel.vaultContentDatas;
        gallery_pager.setAdapter(new BlackGoldAdapter2(resultBeenList, VaultActivity.this));
    }


    @Override
    protected void onDestroy() {
        MessageManager.getInstance().dispatchResponsedMessage(new CommonMessage<String>(VedioCmd.TITLE_CHANGE2, "顶级片库"));
        super.onDestroy();
    }
}
