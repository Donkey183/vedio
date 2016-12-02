package com.app.video.ui.wxapi;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.app.basevideo.framework.util.LogUtil;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.basevideo.util.DesUtil;
import com.app.basevideo.util.MD5;
import com.app.video.R;
import com.app.video.data.WechatPayData;
import com.app.video.net.VedioNetService;
import com.app.video.net.response.WechatPayResponse;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import retrofit2.Response;

public class WXPayTestActivity extends Activity {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    //appid 微信分配的公众账号ID
    public static final String APP_ID = "wx310fce993d9b2cc7";

    //商户号 微信分配的公众账号ID
    public static final String MCH_ID = "1412344602";

    //  API密钥，在商户平台设置
    public static final String API_KEY = "004859871f07c245cc39829471201101";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_test);
    }


    public void getWechatInfo() {

        CommonHttpRequest request = new CommonHttpRequest();
        request.addParam("pid", "90");
        request.addParam("totalMoney", "21");

        MFCall<WechatPayResponse> call = HttpRequestService.createService(VedioNetService.class).getWechatPayInfo(request.buildParams());
        call.doRequest(new MFCallbackAdapter<WechatPayResponse>() {
            @Override
            public void onResponse(WechatPayResponse entity, Response response, Throwable throwable) {
                if (entity == null || !entity.success || entity.data == null) {
                    return;
                }
                doWechatPay(entity.data);
            }
        });
    }

    private PayReq mPayReq = new PayReq();

    IWXAPI wechatApi;

    public void doWechatPay(WechatPayData payData) {
        wechatApi = WXAPIFactory.createWXAPI(WXPayTestActivity.this, APP_ID);
        wechatApi.registerApp(APP_ID);
        mPayReq.appId = APP_ID;//DesUtil.decrypt(payData.getAppid(), "URIW853FKDJAF9363KDJKF7MFSFRTEWE");
        mPayReq.prepayId = payData.getPrepayId();
        mPayReq.sign = payData.getSign();
        mPayReq.timeStamp = genTimeStamp();
        mPayReq.partnerId = MCH_ID;//DesUtil.decrypt(payData.getMchid(), "URIW853FKDJAF9363KDJKF7MFSFRTEWE");
        mPayReq.nonceStr = genNonceStr();
        mPayReq.packageValue = "Sign=WXPay";
       //DesUtil.decrypt(payData.getAppid(), "URIW853FKDJAF9363KDJKF7MFSFRTEWE")
        wechatApi.sendReq(mPayReq);
        LogUtil.e("==========appId============" + DesUtil.decrypt(payData.getAppid(), "URIW853FKDJAF9363KDJKF7MFSFRTEWE"));
        LogUtil.e("==========商户号============" + DesUtil.decrypt(payData.getMchid(), "URIW853FKDJAF9363KDJKF7MFSFRTEWE"));
        LogUtil.e("==========key============" + DesUtil.decrypt(payData.getKey(), "URIW853FKDJAF9363KDJKF7MFSFRTEWE"));
    }

    public void onBtnClick(View view) {
        getWechatInfo();
    }

    private void genSign() {
        List<NameValuePair> signParams = new LinkedList();
        signParams.add(new BasicNameValuePair("appid", mPayReq.appId));
        signParams.add(new BasicNameValuePair("noncestr", mPayReq.nonceStr));
        signParams.add(new BasicNameValuePair("package", mPayReq.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", mPayReq.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", mPayReq.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", mPayReq.timeStamp));
    }


    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private String genTimeStamp() {
        return "" + (System.currentTimeMillis() / 1000);
    }

}