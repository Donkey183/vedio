package com.app.video.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.config.VedioCmd;
import com.app.basevideo.framework.manager.MessageManager;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.INetFinish;
import com.app.video.data.ChannelContentData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.model.ChannelContentModel;
import com.app.video.ui.view.ChannelActivityView;

public class ChannelActivity extends MFBaseActivity implements INetFinish, OnRecyclerViewItemClickListener {

    private ChannelActivityView mChannelView;
    private ChannelContentModel mChanneModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChannelView = new ChannelActivityView(this, this);
        mChanneModel = new ChannelContentModel(this);
        loadPageData();
    }

    private void loadPageData() {
        CommonHttpRequest request = new CommonHttpRequest();
        String cid = getIntent().getExtras().getString("cid");
        request.addParam("cid", cid);
        mChanneModel.sendHttpRequest(request, ChannelContentModel.GET_CHANNEL_CONTENT_INFO);
    }

    @Override
    public void onHttpResponse(CommonMessage<?> responsedMessage) {
        mChannelView.showPageView(mChanneModel.channelContentDatas);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPageData();
    }

    @Override
    public void onItemClick(View view, int position, Object obj) {
        if (obj != null && obj instanceof ChannelContentData) {
            ChannelContentData content = (ChannelContentData) obj;
            Intent intent = new Intent(ChannelActivity.this, VideoPlayerActivity.class);
            intent.putExtra("path", content.getCresource());
            startActivity(intent);
        }
    }


    @Override
    protected void onDestroy() {
        MessageManager.getInstance().dispatchResponsedMessage(new CommonMessage<String>(VedioCmd.TITLE_CHANGE2, "频道"));
        super.onDestroy();
    }
}
