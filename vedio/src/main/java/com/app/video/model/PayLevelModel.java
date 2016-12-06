package com.app.video.model;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.base.MFBaseModel;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.video.data.PayLevelData;
import com.app.video.net.VedioNetService;
import com.app.video.net.response.PayLevelResponse;

import retrofit2.Response;


public class PayLevelModel extends MFBaseModel {

    public PayLevelModel(MFBaseActivity activityContext) {
        super(activityContext);
    }

    public PayLevelData mPayLevelData = new PayLevelData();
    public static final int GET_PAY_INFO = 91103;

    @Override
    public void sendHttpRequest(CommonHttpRequest request, final int requestCode) {
        MFCall<PayLevelResponse> call = HttpRequestService.createService(VedioNetService.class).getPayInfo(request.buildParams());
        call.doRequest(new MFCallbackAdapter<PayLevelResponse>() {
            @Override
            public void onResponse(PayLevelResponse entity, Response<?> response, Throwable throwable) {
                if (entity == null || !entity.success || entity.data == null) {
                    disPatchNetErrorMessage(-1, entity == null ? null : entity.msg);
                    return;
                }
                mPayLevelData.data = entity.data;
                disPatchRequestSuccessMessage(requestCode);
            }
        });
    }

    @Override
    public boolean loadModelDataFromCache() {
        return false;
    }
}
