package com.app.video.config;

import android.support.annotation.DrawableRes;

public class Config {
    private String vip_now;
    private String tittle_first;
    private @DrawableRes int img_first1;
    private @DrawableRes int img_first2;
    private String tittle_vip;
    private @DrawableRes int img_vip1;
    private @DrawableRes int img_vip2;
    private Payoff pay1;
    private Payoff pay2;
    private @DrawableRes int pay_img;
    private int playtime;

    public int getPlaytime() {
        return playtime;
    }

    public void setPlaytime(int playtime) {
        this.playtime = playtime;
    }

    public Config(String vip_now, String tittle_first, int img_first1, int img_first2, String tittle_vip, int img_vip1, int img_vip2, Payoff pay1, Payoff pay2, int pay_img,int playtime) {
        this.vip_now = vip_now;
        this.tittle_first = tittle_first;
        this.img_first1 = img_first1;
        this.img_first2 = img_first2;
        this.tittle_vip = tittle_vip;
        this.img_vip1 = img_vip1;
        this.img_vip2 = img_vip2;
        this.pay1 = pay1;
        this.pay2 = pay2;
        this.pay_img = pay_img;
        this.playtime = playtime;
    }

    public String getVip_now() {
        return vip_now;
    }

    public void setVip_now(String vip_now) {
        this.vip_now = vip_now;
    }

    public String getTittle_first() {
        return tittle_first;
    }

    public void setTittle_first(String tittle_first) {
        this.tittle_first = tittle_first;
    }

    public @DrawableRes int getImg_first1() {
        return img_first1;
    }

    public void setImg_first1(int img_first1) {
        this.img_first1 = img_first1;
    }

    public @DrawableRes int getImg_first2() {
        return img_first2;
    }

    public void setImg_first2(int img_first2) {
        this.img_first2 = img_first2;
    }

    public String getTittle_vip() {
        return tittle_vip;
    }

    public void setTittle_vip(String tittle_vip) {
        this.tittle_vip = tittle_vip;
    }

    public @DrawableRes int getImg_vip1() {
        return img_vip1;
    }

    public void setImg_vip1(int img_vip1) {
        this.img_vip1 = img_vip1;
    }

    public @DrawableRes int getImg_vip2() {
        return img_vip2;
    }

    public void setImg_vip2(int img_vip2) {
        this.img_vip2 = img_vip2;
    }

    public Payoff getPay1() {
        return pay1;
    }

    public void setPay1(Payoff pay1) {
        this.pay1 = pay1;
    }

    public Payoff getPay2() {
        return pay2;
    }

    public void setPay2(Payoff pay2) {
        this.pay2 = pay2;
    }

    public @DrawableRes int getPay_img() {
        return pay_img;
    }

    public @DrawableRes void setPay_img(int pay_img) {
        this.pay_img = pay_img;
    }
}
