package com.app.video.config;

import com.app.video.R;

/**
 * Created by liubohua on 16/11/26.
 */
public class Constants {
    public static Payoff vip_gold  = new Payoff("黄金会员","约百部成人体验电影大片", R.drawable.gold,"39");
    public static Payoff vip_diamond1  = new Payoff("钻石会员","海量成人电影大片(定期更新)", R.drawable.diamond,"68");
    public static Payoff vip_diamond2  = new Payoff("钻石会员","海量成人电影大片(定期更新)", R.drawable.diamond,"29");
    public static Payoff vip_black  = new Payoff("黑金会员","高清大片任你选", R.drawable.black_diamond,"30");
    public static Payoff vip_king  = new Payoff("皇冠会员","每天3000佳片看到爽", R.drawable.crown,"30");

    public static Config nomor_config = new Config("normal","体验区",R.drawable.home,R.drawable.home2,"VIP",R.drawable.VIP,R.drawable.VIP2,vip_gold,vip_diamond1,R.drawable.pay1);
    public static Config gold_config = new Config("gold","黄金区",R.drawable.VIP,R.drawable.VIP2,"钻石区",R.drawable.zuanshi,R.drawable.zuanshi2,vip_diamond2,null,R.drawable.pay2);
    public static Config diamond_config = new Config("deamond","钻石区",R.drawable.zuanshi,R.drawable.zuanshi2,"黑金区",R.drawable.black,R.drawable.black2,vip_black,null,R.drawable.pay3);
    public static Config black_config = new Config("black","黑金区",R.drawable.black,R.drawable.black2,"皇冠区",R.drawable.crown1,R.drawable.crown2,vip_king,null,R.drawable.pay4);
    public static Config crown_config = new Config("crown","黑金区",R.drawable.black,R.drawable.black2,"皇冠区",R.drawable.crown1,R.drawable.crown2,vip_king,null,R.drawable.pay4);


}
