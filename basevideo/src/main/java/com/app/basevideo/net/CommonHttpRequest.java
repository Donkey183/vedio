package com.app.basevideo.net;

import com.app.basevideo.config.VideoConstant;
import com.app.basevideo.util.DateUtil;

import java.util.HashMap;
import java.util.Map;

public class CommonHttpRequest {
    private Map<String, Object> params = new HashMap<>();
    private boolean mIsNeedCommonParam = true;

    public CommonHttpRequest addParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    public CommonHttpRequest addParam(String key, int value) {
        params.put(key, value);
        return this;
    }

    public CommonHttpRequest addParam(String key, boolean value) {
        params.put(key, value);
        return this;
    }

    public CommonHttpRequest needCommonParam(boolean needCommonParam) {
        this.mIsNeedCommonParam = needCommonParam;
        return this;
    }

    private void addCommonParams() {
//        params.put(VideoConstant.PARAM_IMEI, AppUtils.getIMEI(MFBaseApplication.getInstance()));
//        params.put(VideoConstant.PARAM_APP_VERSION, AppUtils.getAppVersion());
//        params.put(VideoConstant.PARAM_NET_TYPE, AppUtils.getNetworkType());
//        params.put(VideoConstant.PARAM_PHONE_MODEL, AppUtils.getModel());
//        params.put(VideoConstant.PARAM_OS_TYPE, "android");
//        params.put(VideoConstant.PARAM_OS_VERSION, AppUtils.getSystemSDKName());
//        params.put(VideoConstant.PARAM_MOBILE_BRAND, AppUtils.getManufacturer());
//        params.put(VideoConstant.PARAM_MOBILE_OPERATORS, AppUtils.getMobileOperators());
        params.put(VideoConstant.PARAM_TIMESTAMP, "1234");//DateUtil.createDate("ddhhmmss")
        params.put(VideoConstant.PARAM_TOKEN, "BD9F6B08527789FC");
        params.put(VideoConstant.ENCY_STR, "D3BF8A21D8AE9941AD4021DB9ABCD50D");
    }


    public Map<String, Object> buildParams() {
        if (mIsNeedCommonParam) {
            addCommonParams();
        }
        return params;
    }
}
