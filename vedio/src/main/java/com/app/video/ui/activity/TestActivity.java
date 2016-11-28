package com.app.video.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.config.VideoCmd;
import com.app.basevideo.framework.manager.MessageManager;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.framework.message.TaskMessage;
import com.app.video.R;
import com.app.video.util.Strings;

public class TestActivity extends MFBaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //此处充值成功
        MessageManager.getInstance().dispatchResponsedMessage(new CommonMessage<String>(VideoCmd.CMD_PAY_SUCCESS, "paysucess"));

    }
}
