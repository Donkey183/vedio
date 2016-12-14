package com.app.video.ui.widget;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.basevideo.cache.MFSimpleCache;
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
import com.app.basevideo.util.WindowUtil;
import com.app.video.R;
import com.app.video.config.Constants;
import com.app.video.config.Payoff;
import com.app.video.data.WechatPayData;
import com.app.video.net.VedioNetService;
import com.app.video.net.response.WechatPayResponse;
import com.bumptech.glide.Glide;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import retrofit2.Response;

public class CommonAlert {

    private Context context;
    private ImageView alert_cha;
    private CheckBox check_wechat;
    private CheckBox check_zhifu;
    private RelativeLayout zhifu1;
    private RelativeLayout zhifu2;

    private ImageView dialog_img;
    private ImageView zhifu1_img;
    private TextView zhifu1_text1;
    private TextView zhifu1_text2;
    private TextView zhifu1_text;

    private ImageView zhifu2_img;
    private TextView zhifu2_text1;
    private TextView zhifu2_text2;
    private TextView zhifu2_text;

    AlertDialog alert;

    public CommonAlert(Context context) {
        this.context = context;
        MessageManager.getInstance().registerListener(dissmissAlertListener);
    }

    private MessageListener dissmissAlertListener = new MessageListener(VedioCmd.DISS_MISS_ALERT) {
        @Override
        public void onMessage(CommonMessage<?> responsedMessage) {
            if (alert != null) {
                alert.dismiss();
            }
        }
    };

    public void showAlert(final Payoff pay1, final Payoff pay2, final String id, final int num) {

        alert = new AlertDialog.Builder(context).create();

        alert.show();
        Window window = alert.getWindow();
        final LayoutInflater inflate = LayoutInflater.from(context);
        View contentView = inflate.inflate(R.layout.dialog_layout, null);
        WindowUtil.resizeRecursively(contentView);
        window.setContentView(contentView);

        dialog_img = (ImageView) window.findViewById(R.id.dialog_img);
        String domain = MFSimpleCache.get(context).getAsString("PIC_DOMAIN");
        Glide.with(context).load(domain + id).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(dialog_img);

        zhifu1_img = (ImageView) window.findViewById(R.id.zhifu1_img);
        zhifu1_text = (TextView) window.findViewById(R.id.zhifu1_text);
        zhifu1_text1 = (TextView) window.findViewById(R.id.zhifu1_text1);
        zhifu1_text2 = (TextView) window.findViewById(R.id.zhifu1_text2);

        zhifu1_text.setText("￥" + pay1.getVip_money());
        zhifu1_text1.setText(pay1.getVip_name());
        zhifu1_text2.setText(pay1.getVip_message());
        zhifu1_img.setImageResource(pay1.getVip_img());

        zhifu2_img = (ImageView) window.findViewById(R.id.zhifu2_img);
        zhifu2_text = (TextView) window.findViewById(R.id.zhifu2_text);
        zhifu2_text1 = (TextView) window.findViewById(R.id.zhifu2_text1);
        zhifu2_text2 = (TextView) window.findViewById(R.id.zhifu2_text2);

        check_wechat = (CheckBox) window.findViewById(R.id.check_wechat);
        check_zhifu = (CheckBox) window.findViewById(R.id.check_zhifu);

        alert_cha = (ImageView) window.findViewById(R.id.dialog_cha);

        zhifu1 = (RelativeLayout) window.findViewById(R.id.layout_zhifu1);

        zhifu2 = (RelativeLayout) window.findViewById(R.id.layout_zhifu2);


        if (pay2 == null) {
            zhifu2.setVisibility(View.GONE);
        } else {
            zhifu2_text.setText("￥" + pay2.getVip_money());
            zhifu2_text1.setText(pay2.getVip_name());
            zhifu2_text2.setText(pay2.getVip_message());
            zhifu2_img.setImageResource(pay2.getVip_img());
        }

        alert_cha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        check_wechat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_zhifu.setChecked(false);
                }
            }
        });
        check_zhifu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check_wechat.setChecked(false);
                }
            }
        });

        zhifu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check_wechat.isChecked()) {
                    Toast.makeText(context, "请选择支付方式", Toast.LENGTH_SHORT).show();
                    return;
                }
                check_packoff(pay1);
                MessageManager.getInstance().registerListener(payCallBackListener);
                getWechatInfo(pay1.getVip_money());
                Toast.makeText(context, "正在获取支付信息...", Toast.LENGTH_LONG).show();
            }
        });

        zhifu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check_wechat.isChecked()) {
                    Toast.makeText(context, "请选择支付方式", Toast.LENGTH_LONG).show();
                    return;
                }
                check_packoff(pay2);
                MessageManager.getInstance().registerListener(payCallBackListener);
                getWechatInfo(pay2.getVip_money());
                Toast.makeText(context, "正在获取支付信息...", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void check_packoff(Payoff pay) {
        if (pay.getVip_name().equals("黄金会员")) {
            Constants.pay_config = Constants.gold_config;
        } else if (pay.getVip_name().equals("钻石会员")) {
            Constants.pay_config = Constants.diamond_config;
        } else if (pay.getVip_name().equals("黑金会员")) {
            Constants.pay_config = Constants.black_config;
        } else if (pay.getVip_name().equals("皇冠会员")) {
            Constants.pay_config = Constants.crown_config;
        } else if (pay.getVip_name().equals("紫钻会员")) {
            Constants.pay_config = Constants.purple_config;
        } else if (pay.getVip_name().equals("蓝钻会员")) {
            Constants.pay_config = Constants.blue_config;
        } else if (pay.getVip_name().equals("打赏红包")) {
            Constants.pay_config = Constants.red_config;
        }
    }


    PayReq req;
    StringBuffer sb;
    IWXAPI msgApi;
    int recourseId;

    public void getWechatInfo(String payAmount) {

        CommonHttpRequest request = new CommonHttpRequest();
        request.addParam("pid", ChannelUtil.getChannel(context, "-1"));
        request.addParam("totalMoney", payAmount);

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
    }

    private void sendPayReq(WechatPayData data) {
        msgApi = WXAPIFactory.createWXAPI(context, Constants.APP_ID);
        req = new PayReq();
        sb = new StringBuffer();
        genPayReq(data);
        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);
    }


    MessageListener payCallBackListener = new MessageListener(VedioCmd.CMD_PAY_CALL_BACK) {
        @Override
        public void onMessage(CommonMessage<?> responsedMessage) {
            MessageManager.getInstance().dispatchResponsedMessage(new CommonMessage<String>(VedioCmd.CMD_PAY_SUCCESS, "paysucess" + "*" + recourseId));
        }
    };
}
