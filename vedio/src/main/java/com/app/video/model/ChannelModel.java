package com.app.video.model;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.base.MFBaseModel;
import com.app.basevideo.net.CommonHttpRequest;

/**
 * 频道Model
 */
public class ChannelModel extends MFBaseModel {

    /**
     * 使用BaseModel(Context)，context传入Activity(该Activity必须继承自{@link MFBaseActivity})
     * 在BaseModel中绑定Activity页面唯一标识mUniqueId
     *
     * @param activityContext
     */
    public ChannelModel(MFBaseActivity activityContext) {
        super(activityContext);
    }

    @Override
    protected void sendHttpRequest(CommonHttpRequest request, int requestCode) {

    }

    @Override
    public boolean loadModelDataFromCache() {
        return false;
    }
}
