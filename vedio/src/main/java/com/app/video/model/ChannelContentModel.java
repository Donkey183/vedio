package com.app.video.model;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.base.MFBaseModel;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.video.data.ChannelContentData;
import com.app.video.net.VedioNetService;
import com.app.video.net.response.ChannelContentResponse;

import retrofit2.Response;


public class ChannelContentModel extends MFBaseModel {

    public ChannelContentModel(MFBaseActivity activityContext) {
        super(activityContext);
    }

    public ChannelContentData channelContentData = new ChannelContentData();
    public static final int GET_CHANNEL_CONTENT_INFO = 0x30001;

    @Override
    public void sendHttpRequest(CommonHttpRequest request, int requestCode) {
        MFCall<ChannelContentResponse> call = HttpRequestService.createService(VedioNetService.class).getChannelContentInfo(request.buildParams());
        call.doRequest(new MFCallbackAdapter<ChannelContentResponse>() {
            @Override
            public void onResponse(ChannelContentResponse entity, Response<?> response, Throwable throwable) {
                if (entity == null || !entity.success) {
                    disPatchNetErrorMessage(-1, entity == null ? null : entity.msg);
                    return;
                }
                channelContentData.channelContentList = entity.list;
                disPatchRequestSuccessMessage(GET_CHANNEL_CONTENT_INFO);
            }
        });
    }

    @Override
    public boolean loadModelDataFromCache() {
        return false;
    }
}
