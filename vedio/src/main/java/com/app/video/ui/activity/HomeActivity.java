package com.app.video.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.cache.MFSimpleCache;
import com.app.basevideo.config.VedioCmd;
import com.app.basevideo.framework.listener.MessageListener;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.INetFinish;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.basevideo.util.ChannelUtil;
import com.app.video.R;
import com.app.video.config.Constants;
import com.app.video.data.PayLevelData;
import com.app.video.model.PayLevelModel;
import com.app.video.net.VedioNetService;
import com.app.video.net.response.InitAppResponse;
import com.app.video.ui.view.HomeActivityView;
import com.app.video.ui.widget.CommonAlert;
import com.ta.utdid2.android.utils.StringUtils;

import retrofit2.Response;

public class HomeActivity extends MFBaseActivity implements INetFinish {

    private HomeActivityView mHomeView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private PayLevelModel mPayModel;
    private boolean update = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        sharedPreferences = getSharedPreferences("config", Activity.MODE_PRIVATE);
        checkConfig(sharedPreferences.getString("vip", Constants.NORMAL));
        mHomeView = new HomeActivityView(this, linenter);
        mPayModel = new PayLevelModel(this);
        getPayInfos();
        registerListener(paySuccessListener);
        checkFirstInstall();
    }

    private void checkConfig(String config) {
        if (config.equals(Constants.NORMAL)) {
            Constants.config = Constants.nomor_config;
        } else if (config.equals(Constants.GOLD)) {
            Constants.config = Constants.gold_config;
        } else if (config.equals(Constants.DIAMOND)) {
            Constants.config = Constants.diamond_config;
        } else if (config.equals(Constants.BLACK)) {
            Constants.config = Constants.black_config;
        } else if (config.equals(Constants.CROWN)) {
            Constants.config = Constants.crown_config;
        } else if (config.equals(Constants.PURPLE)) {
            Constants.config = Constants.purple_config;
        } else if (config.equals(Constants.BLUE)) {
            Constants.config = Constants.blue_config;
        } else if (config.equals(Constants.RED)) {
            Constants.config = Constants.red_config;
        }
    }

    private void checkFirstInstall() {
        String firstInstall = MFSimpleCache.get(HomeActivity.this).getAsString("FIRST_INSTALL");
        if (StringUtils.isEmpty(firstInstall)) {
            CommonHttpRequest request = new CommonHttpRequest();
            request.addParam("proxyid", ChannelUtil.getChannel(HomeActivity.this));
            request.addParam("ptype", "0");
            MFCall<InitAppResponse> call = HttpRequestService.createService(VedioNetService.class).initApp(request.buildParams());
            call.doRequest(new MFCallbackAdapter<InitAppResponse>() {
                @Override
                public void onResponse(InitAppResponse entity, Response<?> response, Throwable throwable) {
                    if (entity == null || !entity.success) {
                        return;
                    }
//                    Toast.makeText(HomeActivity.this, "第一次安装调用成功，代理号:" + ChannelUtil.getChannel(HomeActivity.this), Toast.LENGTH_LONG).show();
                    MFSimpleCache.get(HomeActivity.this).put("FIRST_INSTALL", "FIRST_INSTALL");
                }
            });
        }
    }

    @Override
    public void onHttpResponse(CommonMessage<?> responsedMessage) {
        if (((Integer) responsedMessage.getData() == PayLevelModel.GET_PAY_INFO)) {
            PayLevelData payLevelData = mPayModel.mPayLevelData;
            Constants.upDatePayOff(payLevelData);
            MFSimpleCache.get(HomeActivity.this).put("PIC_DOMAIN", mPayModel.mPayLevelData.data.getPicdomain());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPayInfos();
        if(update){
            choseClick(R.id.vip_layout);
            choseClick(R.id.home_layout);
            update = false;
        }

    }

    View.OnClickListener linenter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            choseClick(id);
        }
    };


    MessageListener paySuccessListener = new MessageListener(VedioCmd.CMD_PAY_SUCCESS) {
        @Override
        public void onMessage(CommonMessage<?> responsedMessage) {
            //充值成功回调

            String str = (String) responsedMessage.getData();
            if ("videoplayend".equals(str)) {
                CommonAlert alert = new CommonAlert(HomeActivity.this);
                alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.home_layout);

            } else {
                sharedPreferences = getSharedPreferences("config", Activity.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("vip", Constants.pay_config.getVip_now());
                editor.commit();
                checkConfig(sharedPreferences.getString("vip", Constants.NORMAL));
                update = true;
            }
        }
    };

    private void getPayInfos() {
        CommonHttpRequest request = new CommonHttpRequest();
        mPayModel.sendHttpRequest(request, PayLevelModel.GET_PAY_INFO);
    }

    private void choseClick(int id) {
        switch (id) {
            case R.id.home_layout:
                mHomeView.clickHome();
                break;
            case R.id.vip_layout:
                mHomeView.clickVip();
                break;
            case R.id.main_user:
                mHomeView.clickUser();
                break;
            case R.id.channel_layout:
                mHomeView.clickChannel();
                break;
            case R.id.vault_layout:
                mHomeView.clickVault();
                break;
            case R.id.forum_layout:
                mHomeView.clickForum();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 99:
                CommonAlert alert = new CommonAlert(this);
                alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.home_layout);
                break;
            default:
                break;
        }
    }

}
