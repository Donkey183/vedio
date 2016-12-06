package com.app.video.model;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.base.MFBaseModel;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.video.data.WechatPayData;
import com.app.video.net.VedioNetService;
import com.app.video.net.response.WechatPayResponse;

import retrofit2.Response;


public class PayModel extends MFBaseModel {

    public PayModel(MFBaseActivity activityContext) {
        super(activityContext);
    }

    public WechatPayData mWechatPayData;
    public static final int GET_PAY_INFO = 90003;

    @Override
    public void sendHttpRequest(CommonHttpRequest request, final int requestCode) {
//        MFCall<WechatPayResponse> call = HttpRequestService.createService(VedioNetService.class).getPayInfo(request.buildParams());
//        call.doRequest(new MFCallbackAdapter<WechatPayResponse>() {
//            @Override
//            public void onResponse(WechatPayResponse entity, Response<?> response, Throwable throwable) {
//                if (entity == null || !entity.success) {
//                    disPatchNetErrorMessage(-1, entity == null ? null : entity.msg);
//                    return;
//                }
//                mWechatPayData = entity.data;
//                disPatchRequestSuccessMessage(requestCode);
//            }
//        });
    }

    @Override
    public boolean loadModelDataFromCache() {
        return false;
    }
}
