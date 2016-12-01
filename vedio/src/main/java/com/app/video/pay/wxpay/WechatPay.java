package com.app.video.pay.wxpay;

import android.content.Context;

import com.app.basevideo.framework.util.LogUtil;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.basevideo.util.DesUtil;
import com.app.video.data.WechatPayData;
import com.app.video.net.VedioNetService;
import com.app.video.net.response.WechatPayResponse;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import retrofit2.Response;

public class WechatPay {


    public void getWechatInfo(final Context context) {

        CommonHttpRequest request = new CommonHttpRequest();
        request.addParam("pid", "100");
        request.addParam("totalMoney", "0.1");

        MFCall<WechatPayResponse> call = HttpRequestService.createService(VedioNetService.class).getWechatPayInfo(request.buildParams());
        call.doRequest(new MFCallbackAdapter<WechatPayResponse>() {
            @Override
            public void onResponse(WechatPayResponse entity, Response response, Throwable throwable) {
                if (entity == null || !entity.success || entity.data == null) {
                    return;
                }
                doWechatPay(context, entity.data);
            }
        });
    }

    private PayReq mPayReq = new PayReq();

    IWXAPI wechatApi;

    public void doWechatPay(Context context, WechatPayData payData) {
        mPayReq.appId = DesUtil.decrypt(payData.getAppid(),"URIW853FKDJAF9363KDJKF7MFSFRTEWE");
        mPayReq.prepayId = payData.getPrepayId();
        mPayReq.sign = payData.getSign();
        mPayReq.timeStamp = "" + (System.currentTimeMillis() / 1000);
        mPayReq.partnerId =  DesUtil.decrypt(payData.getMchid(),"URIW853FKDJAF9363KDJKF7MFSFRTEWE");
        mPayReq.nonceStr = "" + ((int) Math.random() * 100 + 109086);
        mPayReq.packageValue = "Sign=WXPay";
        wechatApi = WXAPIFactory.createWXAPI(context, null);
        wechatApi.registerApp(DesUtil.decrypt(payData.getAppid(),"URIW853FKDJAF9363KDJKF7MFSFRTEWE"));
        wechatApi.sendReq(mPayReq);
        LogUtil.e("==========appId============" + DesUtil.decrypt(payData.getAppid(),"URIW853FKDJAF9363KDJKF7MFSFRTEWE"));
        LogUtil.e("==========商户号============" + DesUtil.decrypt(payData.getMchid(),"URIW853FKDJAF9363KDJKF7MFSFRTEWE"));
        LogUtil.e("==========key============" + DesUtil.decrypt(payData.getKey(),"URIW853FKDJAF9363KDJKF7MFSFRTEWE"));
    }

}
