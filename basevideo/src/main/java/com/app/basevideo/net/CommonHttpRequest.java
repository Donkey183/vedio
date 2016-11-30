package com.app.basevideo.net;

import com.app.basevideo.config.VideoConstant;
import com.app.basevideo.util.AppUtils;
import com.app.basevideo.util.DateUtil;
import com.app.basevideo.util.DesUtil;

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

        String uuid = AppUtils.getUUID();
        String timeStamp = DateUtil.createDate("ddhhmmss");
        String encyStr = uuid + timeStamp;
        params.put(VideoConstant.PARAM_TIMESTAMP, timeStamp);
        params.put(VideoConstant.PARAM_TOKEN, DesUtil.encrypt(uuid, "URIW853FKDJAF9363KDJKF7MFSFRTEWE"));//"BD9F6B08527789FC"
        params.put(VideoConstant.ENCY_STR, DesUtil.encrypt(encyStr, "URIW853FKDJAF9363KDJKF7MFSFRTEWE"));//D3BF8A21D8AE9941AD4021DB9ABCD50D
    }


    public Map<String, Object> buildParams() {
        if (mIsNeedCommonParam) {
            addCommonParams();
        }
        return params;
    }
}
