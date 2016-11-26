package com.app.video.model;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.base.MFBaseModel;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.video.data.VerifyCodeData;
import com.app.video.net.VedioNetService;
import com.app.video.net.response.VerifyCodeResponse;

import retrofit2.Response;


public class VerifyModel extends MFBaseModel {

    public VerifyModel(MFBaseActivity activityContext) {
        super(activityContext);
    }

    public VerifyCodeData verifyCodeData;
    public static final int GET_VERIFY_CODE = 0x630901;

    @Override
    protected void sendHttpRequest(CommonHttpRequest request, int requestCode) {
        MFCall<VerifyCodeResponse> call = HttpRequestService.createService(VedioNetService.class).getVerifyCode(request.buildParams());
        call.doRequest(new MFCallbackAdapter<VerifyCodeResponse>() {
            @Override
            public void onResponse(VerifyCodeResponse entity, Response<?> response, Throwable throwable) {
                if (entity == null || !entity.success) {
                    disPatchNetErrorMessage(-1, entity == null ? null : entity.msg);
                    return;
                }
                verifyCodeData = entity.data;
                disPatchRequestSuccessMessage(GET_VERIFY_CODE);
            }
        });
    }

    @Override
    public boolean loadModelDataFromCache() {
        return false;
    }
}
