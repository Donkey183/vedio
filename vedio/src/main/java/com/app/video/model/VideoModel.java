package com.app.video.model;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.base.MFBaseModel;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.video.data.VideoData;
import com.app.video.net.VideoNetService;
import com.app.video.net.response.VedioResponse;

import retrofit2.Response;


public class VideoModel extends MFBaseModel {

    public VideoModel(MFBaseActivity activityContext) {
        super(activityContext);
    }

    public VideoData videoData;
    public static final int GET_VIDEO_INFO = 0x50991;

    @Override
    protected void sendHttpRequest(CommonHttpRequest request, int requestCode) {
        MFCall<VedioResponse> call = HttpRequestService.createService(VideoNetService.class).getVideoResources(request.buildParams());
        call.doRequest(new MFCallbackAdapter<VedioResponse>() {
            @Override
            public void onResponse(VedioResponse entity, Response<?> response, Throwable throwable) {
                if (entity == null || !entity.success) {
                    disPatchNetErrorMessage(-1, entity == null ? null : entity.msg);
                    return;
                }
                videoData = entity.list;
                disPatchRequestSuccessMessage(GET_VIDEO_INFO);
            }
        });
    }

    @Override
    public boolean loadModelDataFromCache() {
        return false;
    }
}
