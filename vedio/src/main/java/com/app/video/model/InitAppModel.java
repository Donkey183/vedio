package com.app.video.model;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.base.MFBaseModel;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.video.data.InitAppData;
import com.app.video.net.VideoNetService;
import com.app.video.net.response.InitAppResponse;

import retrofit2.Response;


public class InitAppModel extends MFBaseModel {

    public InitAppModel(MFBaseActivity activityContext) {
        super(activityContext);
    }

    public InitAppData initAppData;
    public static final int INIT_APP = 0x678901;

    @Override
    protected void sendHttpRequest(CommonHttpRequest request, int requestCode) {
        MFCall<InitAppResponse> call = HttpRequestService.createService(VideoNetService.class).initApp(request.buildParams());
        call.doRequest(new MFCallbackAdapter<InitAppResponse>() {
            @Override
            public void onResponse(InitAppResponse entity, Response<?> response, Throwable throwable) {
                if (entity == null || !entity.success) {
                    disPatchNetErrorMessage(-1, entity == null ? null : entity.msg);
                    return;
                }
                initAppData = entity.data;
                disPatchRequestSuccessMessage(INIT_APP);
            }
        });
    }

    @Override
    public boolean loadModelDataFromCache() {
        return false;
    }
}
