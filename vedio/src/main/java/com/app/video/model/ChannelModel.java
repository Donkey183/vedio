package com.app.video.model;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.base.MFBaseModel;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.video.data.ChannelData;
import com.app.video.net.VideoNetService;
import com.app.video.net.response.ChannelResponse;

import retrofit2.Response;


public class ChannelModel extends MFBaseModel {

    public ChannelModel(MFBaseActivity activityContext) {
        super(activityContext);
    }

    public ChannelData channelData;
    public static final int GET_CHANNEL_INFO = 0x10001;

    @Override
    protected void sendHttpRequest(CommonHttpRequest request, int requestCode) {
        MFCall<ChannelResponse> call = HttpRequestService.createService(VideoNetService.class).getChannelInfo(request.buildParams());
        call.doRequest(new MFCallbackAdapter<ChannelResponse>() {
            @Override
            public void onResponse(ChannelResponse entity, Response<?> response, Throwable throwable) {
                if (entity == null || !entity.success) {
                    disPatchNetErrorMessage(-1, entity == null ? null : entity.msg);
                    return;
                }
                channelData = entity.list;
                disPatchRequestSuccessMessage(GET_CHANNEL_INFO);
            }
        });
    }

    @Override
    public boolean loadModelDataFromCache() {
        return false;
    }
}
