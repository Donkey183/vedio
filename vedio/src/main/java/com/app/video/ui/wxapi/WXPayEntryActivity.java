package com.app.video.ui.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.app.video.R;
import com.app.video.config.Constants;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    private TextView tv_payresult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(final BaseResp resp) {
        // TODO Auto-generated method stub
        Intent intent = new Intent();
        intent.setAction("com.hzy.WxpayResult");
        intent.putExtra("errCode", resp.errCode);
        sendBroadcast(intent);

        Toast.makeText(WXPayEntryActivity.this, "微信支付返回成功", Toast.LENGTH_SHORT).show();
        Log.e("pay", "微信支付返回成功");
        System.out.println("微信返回结果：****************");
        finish();
//		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			switch (resp.errCode) {
//			case -2:
//				AlertDialog.Builder builder = new AlertDialog.Builder(this);
//				builder.setTitle(R.string.app_tip);
//				builder.setMessage(getString(R.string.pay_result_callback_msg, resp.errStr +"取消支付"));
//				builder.show();
//
//				break;
//			case 0:
//				break;
//			default:
//				break;
//			}
//		}
    }
}