package com.app.video.config;

/**
 * Created by liubohua on 16/11/26.
 */
public class Payoff {
    private String vip_name;
    private String vip_message;
    private int vip_img;
    private String vip_money;

    public Payoff(String vip_name, String vip_message, int vip_img, String vip_money) {
        this.vip_name = vip_name;
        this.vip_message = vip_message;
        this.vip_img = vip_img;
        this.vip_money = vip_money;
    }

    public String getVip_name() {
        return vip_name;
    }

    public void setVip_name(String vip_name) {
        this.vip_name = vip_name;
    }

    public String getVip_message() {
        return vip_message;
    }

    public void setVip_message(String vip_message) {
        this.vip_message = vip_message;
    }

    public int getVip_img() {
        return vip_img;
    }

    public void setVip_img(int vip_img) {
        this.vip_img = vip_img;
    }

    public String getVip_money() {
        return vip_money;
    }

    public void setVip_money(String vip_money) {
        this.vip_money = vip_money;
    }
}
