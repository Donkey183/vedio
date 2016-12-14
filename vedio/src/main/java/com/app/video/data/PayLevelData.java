package com.app.video.data;

import java.io.Serializable;

/**
 * 支付
 */
public class PayLevelData implements Serializable {


    /**
     * id : null
     * success : true
     * resp : 2
     * msg : 操作成功!
     * ERROR : 操作失败!
     * list : null
     * data : {"uuid":"f9220d90-7ece-4bde-b232-19f7dbf337e9","createTime":"2016-12-04 13:56:56","seqNo":0,"kuaijinpay":"1","memberPay":"1","goldPay":"2","zuanshiPay":"3","balckPay":"4","huangPay":"5","fenZuanPay":"6","lanzuanPay":"7","redbagPay":"3","alipayMechid":"78BE0A743657D213CA773A4E7822328AAD4021DB9ABCD50D","alipayAppid":"78BE0A743657D213CA773A4E7822328AAD4021DB9ABCD50D","alipaySecret":"7B318D678B1E3416CC6EE391D2F2C8B1AD4021DB9ABCD50D","wxMchid":"7B318D678B1E3416CC6EE391D2F2C8B1AD4021DB9ABCD50D","wxAppid":"7B318D678B1E3416CC6EE391D2F2C8B1AD4021DB9ABCD50D","wxSecret":"7B318D678B1E3416CC6EE391D2F2C8B1AD4021DB9ABCD50D","alipayApikey":"7B318D678B1E3416CC6EE391D2F2C8B1AD4021DB9ABCD50D","wxapiKey":"7B318D678B1E3416CC6EE391D2F2C8B1AD4021DB9ABCD50D","ifVerify":"0","ifpay":"1"}
     * pageCount : 0
     * total : 0
     * data1 : null
     * data2 : null
     * more : 0
     * page : null
     * list1 : null
     * list2 : null
     */

    private boolean success;
    private String resp;
    /**
     * uuid : f9220d90-7ece-4bde-b232-19f7dbf337e9
     * createTime : 2016-12-04 13:56:56
     * seqNo : 0
     * kuaijinpay : 1
     * memberPay : 1
     * goldPay : 2
     * zuanshiPay : 3
     * balckPay : 4
     * huangPay : 5
     * fenZuanPay : 6
     * lanzuanPay : 7
     * redbagPay : 3
     * alipayMechid : 78BE0A743657D213CA773A4E7822328AAD4021DB9ABCD50D
     * alipayAppid : 78BE0A743657D213CA773A4E7822328AAD4021DB9ABCD50D
     * alipaySecret : 7B318D678B1E3416CC6EE391D2F2C8B1AD4021DB9ABCD50D
     * wxMchid : 7B318D678B1E3416CC6EE391D2F2C8B1AD4021DB9ABCD50D
     * wxAppid : 7B318D678B1E3416CC6EE391D2F2C8B1AD4021DB9ABCD50D
     * wxSecret : 7B318D678B1E3416CC6EE391D2F2C8B1AD4021DB9ABCD50D
     * alipayApikey : 7B318D678B1E3416CC6EE391D2F2C8B1AD4021DB9ABCD50D
     * wxapiKey : 7B318D678B1E3416CC6EE391D2F2C8B1AD4021DB9ABCD50D
     * ifVerify : 0
     * ifpay : 1
     */

    public DataBean data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String uuid;
        private String createTime;
        private int seqNo;
        private String kuaijinpay;
        private String memberPay;
        private String goldPay;
        private String zuanshiPay;
        private String balckPay;
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
        private String picdomain;

        public String getPicdomain() {
            return picdomain;
        }

        public void setPicdomain(String picdomain) {
            this.picdomain = picdomain;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getSeqNo() {
            return seqNo;
        }

        public void setSeqNo(int seqNo) {
            this.seqNo = seqNo;
        }

        public String getKuaijinpay() {
            return kuaijinpay;
        }

        public void setKuaijinpay(String kuaijinpay) {
            this.kuaijinpay = kuaijinpay;
        }

        public String getMemberPay() {
            return memberPay;
        }

        public void setMemberPay(String memberPay) {
            this.memberPay = memberPay;
        }

        public String getGoldPay() {
            return goldPay;
        }

        public void setGoldPay(String goldPay) {
            this.goldPay = goldPay;
        }

        public String getZuanshiPay() {
            return zuanshiPay;
        }

        public void setZuanshiPay(String zuanshiPay) {
            this.zuanshiPay = zuanshiPay;
        }

        public String getBalckPay() {
            return balckPay;
        }

        public void setBalckPay(String balckPay) {
            this.balckPay = balckPay;
        }

        public String getHuangPay() {
            return huangPay;
        }

        public void setHuangPay(String huangPay) {
            this.huangPay = huangPay;
        }

        public String getFenZuanPay() {
            return fenZuanPay;
        }

        public void setFenZuanPay(String fenZuanPay) {
            this.fenZuanPay = fenZuanPay;
        }

        public String getLanzuanPay() {
            return lanzuanPay;
        }

        public void setLanzuanPay(String lanzuanPay) {
            this.lanzuanPay = lanzuanPay;
        }

        public String getRedbagPay() {
            return redbagPay;
        }

        public void setRedbagPay(String redbagPay) {
            this.redbagPay = redbagPay;
        }

        public String getAlipayMechid() {
            return alipayMechid;
        }

        public void setAlipayMechid(String alipayMechid) {
            this.alipayMechid = alipayMechid;
        }

        public String getAlipayAppid() {
            return alipayAppid;
        }

        public void setAlipayAppid(String alipayAppid) {
            this.alipayAppid = alipayAppid;
        }

        public String getAlipaySecret() {
            return alipaySecret;
        }

        public void setAlipaySecret(String alipaySecret) {
            this.alipaySecret = alipaySecret;
        }

        public String getWxMchid() {
            return wxMchid;
        }

        public void setWxMchid(String wxMchid) {
            this.wxMchid = wxMchid;
        }

        public String getWxAppid() {
            return wxAppid;
        }

        public void setWxAppid(String wxAppid) {
            this.wxAppid = wxAppid;
        }

        public String getWxSecret() {
            return wxSecret;
        }

        public void setWxSecret(String wxSecret) {
            this.wxSecret = wxSecret;
        }

        public String getAlipayApikey() {
            return alipayApikey;
        }

        public void setAlipayApikey(String alipayApikey) {
            this.alipayApikey = alipayApikey;
        }

        public String getWxapiKey() {
            return wxapiKey;
        }

        public void setWxapiKey(String wxapiKey) {
            this.wxapiKey = wxapiKey;
        }

        public String getIfVerify() {
            return ifVerify;
        }

        public void setIfVerify(String ifVerify) {
            this.ifVerify = ifVerify;
        }

        public String getIfpay() {
            return ifpay;
        }

        public void setIfpay(String ifpay) {
            this.ifpay = ifpay;
        }
    }
}
