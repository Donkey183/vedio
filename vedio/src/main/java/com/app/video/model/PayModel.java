package com.app.video.model;

import com.app.basevideo.base.MFBaseFragment;
import com.app.basevideo.base.MFBaseFragmentModel;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.video.data.PayData;
import com.app.video.net.VedioNetService;
import com.app.video.net.response.PayResponse;

import retrofit2.Response;


public class PayModel extends MFBaseFragmentModel {

    public PayModel(MFBaseFragment activityContext) {
        super(activityContext);
    }

    public PayData payData;
    public static final int GET_PAY_INFO = 0x10001;

    @Override
    public void sendHttpRequest(CommonHttpRequest request, int requestCode) {
        MFCall<PayResponse> call = HttpRequestService.createService(VedioNetService.class).getPayInfo(request.buildParams());
        call.doRequest(new MFCallbackAdapter<PayResponse>() {
            @Override
            public void onResponse(PayResponse entity, Response<?> response, Throwable throwable) {
                if (entity == null || !entity.success) {
                    disPatchNetErrorMessage(-1, entity == null ? null : entity.msg);
                    return;
                }
                payData = entity.data;
                disPatchRequestSuccessMessage(GET_PAY_INFO);
            }
        });
    }

    @Override
    public boolean loadModelDataFromCache() {
        return false;
    }
}
