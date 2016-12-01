package com.app.video.data;

import java.io.Serializable;

/**
 * 支付
 */
public class WechatPayData implements Serializable {


    /**
     * prepayId : wx20161201222102c5feea38260393054801
     * sign : CCA5C64FF58098BFAF23B37D8E0F6E5A
     * appid : DEF5AC9EF41B6087B0FF3AD76957141A9B5343568BA3896D
     * mchid : D098FFF76B682C4A248F86A55E889579
     * key : B6C1A32B72A42BF14A6F8ED472BEA0BB63C5963E60A0494027222124DCA34139AD4021DB9ABCD50D
     */

    private String prepayId;
    private String sign;
    private String appid;
    private String mchid;
    private String key;

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
