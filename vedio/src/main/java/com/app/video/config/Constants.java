package com.app.video.config;

import com.app.video.R;


public class Constants {
    public static String NORMAL = "normal";
    public static String GOLD = "gold";
    public static String DIAMOND = "deamond";
    public static String BLACK = "black";
    public static String CROWN = "crown";

    public static String PURPLE = "purple";
    public static String BLUE = "blue";
    public static String RED = "red";


    public static Payoff vip_gold  = new Payoff("黄金会员","约百部成人体验电影大片", R.drawable.gold,"39");
    public static Payoff vip_diamond1  = new Payoff("钻石会员","海量成人电影大片(定期更新)", R.drawable.diamond,"68");
    public static Payoff vip_diamond2  = new Payoff("钻石会员","海量成人电影大片(定期更新)", R.drawable.diamond,"29");
    public static Payoff vip_black  = new Payoff("黑金会员","高清大片任你选", R.drawable.black_diamond,"30");
    public static Payoff vip_king  = new Payoff("皇冠会员","每天3000佳片看到爽", R.drawable.crown,"30");

    public static Payoff vip_purple  = new Payoff("紫钻会员","高清大片任你选", R.drawable.purple_diamond,"10");
    public static Payoff vip_blue  = new Payoff("蓝钻会员","高清大片任你选", R.drawable.blue_diamond,"10");
    public static Payoff vip_red  = new Payoff("打赏红包","高清大片任你选", R.drawable.red,"10");


    public static Config nomor_config = new Config(NORMAL,"体验区",R.drawable.home,R.drawable.home2,"vip",R.drawable.vip,R.drawable.vip2,vip_gold,vip_diamond1,R.drawable.pay1);
    public static Config gold_config = new Config(GOLD,"黄金区",R.drawable.vip,R.drawable.vip2,"钻石区",R.drawable.zuanshi,R.drawable.zuanshi2,vip_diamond2,null,R.drawable.pay2);
    public static Config diamond_config = new Config(DIAMOND,"钻石区",R.drawable.zuanshi,R.drawable.zuanshi2,"黑金区",R.drawable.black,R.drawable.black2,vip_black,null,R.drawable.pay3);
    public static Config black_config = new Config(BLACK,"黑金区",R.drawable.black,R.drawable.black2,"皇冠区",R.drawable.crown1,R.drawable.crown2,vip_king,null,R.drawable.pay4);
    public static Config crown_config = new Config(CROWN,"黑金区",R.drawable.black,R.drawable.black2,"皇冠区",R.drawable.crown1,R.drawable.crown2,vip_purple,null,R.drawable.pay4);

    public static Config purple_config = new Config(PURPLE,"黑金区",R.drawable.black,R.drawable.black2,"皇冠区",R.drawable.crown1,R.drawable.crown2,vip_blue,null,R.drawable.pay4);
    public static Config blue_config = new Config(BLUE,"黑金区",R.drawable.black,R.drawable.black2,"皇冠区",R.drawable.crown1,R.drawable.crown2,vip_red,null,R.drawable.pay4);
    public static Config red_config = new Config(RED,"黑金区",R.drawable.black,R.drawable.black2,"皇冠区",R.drawable.crown1,R.drawable.crown2,null,null,R.drawable.pay4);



    public static Config config;

    public static Config pay_config;
}
