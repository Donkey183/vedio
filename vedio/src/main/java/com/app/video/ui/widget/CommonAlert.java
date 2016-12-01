package com.app.video.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.basevideo.config.VedioCmd;
import com.app.basevideo.framework.listener.MessageListener;
import com.app.basevideo.framework.manager.MessageManager;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.framework.util.LogUtil;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.basevideo.util.WindowUtil;
import com.app.video.R;
import com.app.video.config.Constants;
import com.app.video.config.Payoff;
import com.app.video.data.PayData;
import com.app.video.model.PayModel;
import com.app.video.net.VedioNetService;
import com.app.video.net.response.PayResponse;
import com.app.video.ui.activity.TestActivity;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

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

    private PayModel mPayModel;

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

    public void showAlert(final Payoff pay1, final Payoff pay2, int id, final int num) {

        alert = new AlertDialog.Builder(context).create();

        alert.show();
        Window window = alert.getWindow();
        LayoutInflater inflate = LayoutInflater.from(context);
        View contentView = inflate.inflate(R.layout.dialog_layout, null);
        WindowUtil.resizeRecursively(contentView);
        window.setContentView(contentView);

        dialog_img = (ImageView) window.findViewById(R.id.dialog_img);

        zhifu1_img = (ImageView) window.findViewById(R.id.zhifu1_img);
        zhifu1_text = (TextView) window.findViewById(R.id.zhifu1_text);
        zhifu1_text1 = (TextView) window.findViewById(R.id.zhifu1_text1);
        zhifu1_text2 = (TextView) window.findViewById(R.id.zhifu1_text2);

        dialog_img.setImageResource(id);

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
                if (!check_wechat.isChecked() && !check_zhifu.isChecked()) {
                    Toast.makeText(context, "请选择支付方式", Toast.LENGTH_SHORT).show();
                    return;
                }
                check_packoff(pay1);
//                Intent intent = new Intent(context, TestActivity.class);
//                context.startActivity(intent);
//                Toast.makeText(context, "支付111111", Toast.LENGTH_SHORT).show();
            }
        });
        zhifu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!check_wechat.isChecked() && !check_zhifu.isChecked()) {
                    Toast.makeText(context, "请选择支付方式", Toast.LENGTH_SHORT).show();
                    return;
                }
                check_packoff(pay2);
//                Intent intent = new Intent(context, TestActivity.class);
//                context.startActivity(intent);
//                Toast.makeText(context, "支付222222", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private PayData payData;
    private IWXAPI wxapi;

    private void getPayInfo(String payType, String payAmount) {
        CommonHttpRequest request = new CommonHttpRequest();
        MFCall<PayResponse> call = HttpRequestService.createService(VedioNetService.class).getPayInfo(request.buildParams());
        call.doRequest(new MFCallbackAdapter<PayResponse>() {
            @Override
            public void onResponse(PayResponse entity, Response<?> response, Throwable throwable) {
                if (entity == null || !entity.success) {
                    Toast.makeText(context, "支付失败!", Toast.LENGTH_SHORT).show();
                    return;
                }
                payData = entity.data;
                LogUtil.d("paydata" + entity.data);
                //TODO 发送支付成功消息
                //此处充值成功
                MessageManager.getInstance().dispatchResponsedMessage(new CommonMessage<String>(VedioCmd.CMD_PAY_SUCCESS, "paysucess"));
            }
        });
    }

    private void doWxPay(PayData payData){

//        wxapi = WXAPIFactory.createWXAPI(context,payData.it());
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


}
