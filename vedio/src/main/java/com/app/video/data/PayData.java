package com.app.video.data;

import java.io.Serializable;

/**
 * 支付
 */
public class PayData implements Serializable {
    private String uuid;
    private String createTime;
    private String seqNo;
    private String memberPay;
    private String goldPay;
    private String zuanshiPay;
    private String huangPay;
    private String fenZuanPay;
    private String lanzuanPay;
    private String redbagPay;
    private String alipayMechid;
    private String alipayAppid;
    private String alipaySecret;
    private String wxMchid;
    private String wxAppid;
    private String wxSecret;
    private String alipayApikey;
    private String wxapiKey;
    private String ifVerify;
    private String ifpay;

    public String getUuid() {
        return uuid == null ? "" : uuid;
    }

    public String getCreateTime() {
        return createTime == null ? "" : createTime;
    }

    public String getSeqNo() {
        return seqNo == null ? "" : seqNo;
    }

    public String getMemberPay() {
        return memberPay == null ? "" : memberPay;
    }

    public String getGoldPay() {
        return goldPay == null ? "" : goldPay;
    }

    public String getZuanshiPay() {
        return zuanshiPay == null ? "" : zuanshiPay;
    }

    public String getHuangPay() {
        return huangPay == null ? "" : huangPay;
    }

    public String getFenZuanPay() {
        return fenZuanPay == null ? "" : fenZuanPay;
    }

    public String getLanzuanPay() {
        return lanzuanPay == null ? "" : lanzuanPay;
    }

    public String getRedbagPay() {
        return redbagPay == null ? "" : redbagPay;
    }

    public String getAlipayMechid() {
        return alipayMechid == null ? "" : alipayMechid;
    }

    public String getAlipayAppid() {
        return alipayAppid == null ? "" : alipayAppid;
    }

    public String getAlipaySecret() {
        return alipaySecret == null ? "" : alipaySecret;
    }

    public String getWxMchid() {
        return wxMchid == null ? "" : wxMchid;
    }

    public String getWxAppid() {
        return wxAppid == null ? "" : wxAppid;
    }

    public String getWxSecret() {
        return wxSecret == null ? "" : wxSecret;
    }

    public String getAlipayApikey() {
        return alipayApikey == null ? "" : alipayApikey;
    }

    public String getWxapiKey() {
        return wxapiKey == null ? "" : wxapiKey;
    }

    public String getIfVerify() {
        return ifVerify == null ? "" : ifVerify;
    }

    public String getIfpay() {
        return ifpay == null ? "" : ifpay;
    }
}
