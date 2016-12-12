package com.app.video.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.config.VedioCmd;
import com.app.basevideo.framework.listener.MessageListener;
import com.app.basevideo.framework.manager.MessageManager;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.basevideo.util.ChannelUtil;
import com.app.basevideo.util.MD5;
import com.app.video.R;
import com.app.video.config.Constants;
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


public class PayActivity extends MFBaseActivity {

    PayReq req;
    TextView show;
    StringBuffer sb;
    IWXAPI msgApi;
    int recourseId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay);
        msgApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        show = (TextView) findViewById(R.id.editText_prepay_id);
        req = new PayReq();
        sb = new StringBuffer();
        registerListener(payCallBackListener);

        //调起微信支付
        Button appayBtn = (Button) findViewById(R.id.appay_btn);


        final String payAmount = getIntent().getExtras().getString("payAmount");
        recourseId = getIntent().getExtras().getInt("lauout");

        appayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getWechatInfo(payAmount);
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    public void getWechatInfo(String payAmount) {

        CommonHttpRequest request = new CommonHttpRequest();
        request.addParam("pid", ChannelUtil.getChannel(PayActivity.this, "-1"));
        request.addParam("totalMoney", "0.01");//payAmount

        MFCall<WechatPayResponse> call = HttpRequestService.createService(VedioNetService.class).getWechatPayInfo(request.buildParams());
        call.doRequest(new MFCallbackAdapter<WechatPayResponse>() {
            @Override
            public void onResponse(WechatPayResponse entity, Response response, Throwable throwable) {
                if (entity == null || !entity.success || entity.data == null) {
                    return;
                }
                sendPayReq(entity.data);
            }
        });
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes());
        Log.e("orion", "----" + appSign);
        return appSign;
    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


    private void genPayReq(WechatPayData data) {
        req = new PayReq();
        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = data.getPrepayId();
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");

        show.setText(sb.toString());

    }

    private void sendPayReq(WechatPayData data) {
        genPayReq(data);
        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
    }


    MessageListener payCallBackListener = new MessageListener(VedioCmd.CMD_PAY_CALL_BACK) {
        @Override
        public void onMessage(CommonMessage<?> responsedMessage) {
            Toast.makeText(PayActivity.this, "支付回调!", Toast.LENGTH_SHORT).show();
            MessageManager.getInstance().dispatchResponsedMessage(new CommonMessage<String>(VedioCmd.CMD_PAY_SUCCESS, "paysucess" + "*" + getIntent().getIntExtra("layout", recourseId)));
        }
    };

}

