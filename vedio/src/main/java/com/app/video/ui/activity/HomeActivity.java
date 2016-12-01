package com.app.video.ui.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.app.basevideo.util.DesUtil;
import com.app.video.R;
import com.app.video.config.Constants;
import com.app.video.config.VedioConstant;
import com.app.video.model.HomeActivityModel;
import com.app.video.ui.view.HomeActivityView;

public class HomeActivity extends MFBaseActivity implements INetFinish {

    private HomeActivityView mHomeView;
    private HomeActivityModel mHomeModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        sharedPreferences = getSharedPreferences("config", Activity.MODE_PRIVATE);
        checkConfig(sharedPreferences.getString("vip", Constants.NORMAL));
        mHomeView = new HomeActivityView(this, linenter);
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
        }else if (config.equals(Constants.PURPLE)) {
            Constants.config = Constants.purple_config;
        }else if (config.equals(Constants.BLUE)) {
            Constants.config = Constants.blue_config;
        }else if (config.equals(Constants.RED)) {
            Constants.config = Constants.red_config;
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
            Toast.makeText(HomeActivity.this, str , Toast.LENGTH_SHORT).show();
            sharedPreferences = getSharedPreferences("config", Activity.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("vip", Constants.pay_config.getVip_now());
            editor.commit();
            checkConfig(sharedPreferences.getString("vip", Constants.NORMAL));
            int id = Integer.parseInt(str.split("\\*")[1]);

            choseClick(R.id.home_layout);
            Log.d("adasd111","adsdsadas");
            choseClick(R.id.vip_layout);
            choseClick(R.id.channel_layout);
            choseClick(R.id.vault_layout);
            choseClick(R.id.forum_layout);
            choseClick(id);
            //销毁充值对话框
            MessageManager.getInstance().dispatchResponsedMessage(new CommonMessage<Object>(VedioCmd.DISS_MISS_ALERT));
        }
    };


    private void choseClick(int id) {
        switch (id) {
            case R.id.home_layout:
                Log.e("adasd","home");
                mHomeView.clickHome();
                break;
            case R.id.vip_layout:
                mHomeView.clickVip();
                Log.e("adasd","vip");
//                LogUtil.e("=====uuid=====" + AppUtils.getUUID());
//                LogUtil.e(DesUtil.decrypt(AppUtils.getUUID(), "URIW853FKDJAF9363KDJKF7MFSFRTEWE"));
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
                Log.e("adasd","forum");
                mHomeView.clickForum();
                break;
            default:
                break;
        }
    }




}
