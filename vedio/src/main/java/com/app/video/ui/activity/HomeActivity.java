package com.app.video.ui.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.config.VedioCmd;
import com.app.basevideo.framework.listener.MessageListener;
import com.app.basevideo.framework.manager.MessageManager;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.framework.util.LogUtil;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.INetFinish;
import com.app.basevideo.util.AppUtils;
import com.app.video.R;
import com.app.video.config.Constants;
import com.app.video.config.VedioConstant;
import com.app.video.model.HomeActivityModel;
import com.app.video.ui.view.HomeActivityView;
import com.app.video.util.DesUtil;

public class HomeActivity extends MFBaseActivity implements View.OnClickListener, INetFinish {

    private HomeActivityView mHomeView;
    private HomeActivityModel mHomeModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        sharedPreferences = getSharedPreferences("config", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("vip", Constants.NORMAL);
        editor.commit();
        checkConfig(sharedPreferences.getString("vip", Constants.NORMAL));
        mHomeView = new HomeActivityView(this, this);
        mHomeModel = new HomeActivityModel(this);
        preLoadPageData();
        registerListener(paySuccessListener);
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
        }
    }

    private void preLoadPageData() {
        CommonHttpRequest request = new CommonHttpRequest();
        request.addParam(VedioConstant.TOKEN, "12345");
        mHomeModel.sendHttpRequest(request, HomeActivityModel.GET_PAGE_DATA);
    }

    @Override
    public void onHttpResponse(CommonMessage<?> responsedMessage) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mHomeView = new HomeActivityView(this, this);
    }

    MessageListener paySuccessListener = new MessageListener(VedioCmd.CMD_PAY_SUCCESS) {
        @Override
        public void onMessage(CommonMessage<?> responsedMessage) {
            //充值成功回调
            Toast.makeText(HomeActivity.this, (String) responsedMessage.getData(), Toast.LENGTH_SHORT).show();
            sharedPreferences = getSharedPreferences("config", Activity.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("vip", Constants.pay_config.getVip_now());
            editor.commit();
            checkConfig(sharedPreferences.getString("vip", Constants.NORMAL));

            //销毁充值对话框
            MessageManager.getInstance().dispatchResponsedMessage(new CommonMessage<Object>(VedioCmd.DISS_MISS_ALERT));
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_layout:
                mHomeView.clickHome();
                break;
            case R.id.vip_layout:
                mHomeView.clickVip();
                LogUtil.e("=====uuid=====" + AppUtils.getUUID());
                LogUtil.e(DesUtil.decrypt(AppUtils.getUUID(), "URIW853FKDJAF9363KDJKF7MFSFRTEWE"));
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

}
