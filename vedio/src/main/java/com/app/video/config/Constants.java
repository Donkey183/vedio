package com.app.video.config;

import com.app.video.R;


public class Constants {

    //appid 微信分配的公众账号ID
    public static final String APP_ID = "wx310fce993d9b2cc7";

    //商户号 微信分配的公众账号ID
    public static final String MCH_ID = "1412344602";

    //  API密钥，在商户平台设置
    public static final String API_KEY = "004859871f07c245cc39829471201101";

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
    public static Payoff vip_purple  = new Payoff("紫钻会员","高清大片任你选", R.drawable.purple_diamond,"15");
    public static Payoff vip_blue  = new Payoff("蓝钻会员","高清大片任你选", R.drawable.blue_diamond,"10");
    public static Payoff vip_red  = new Payoff("打赏红包","发个红包吧", R.drawable.red,"10");
    public static Payoff vip_king = new Payoff("皇冠会员", "最后一次会员升级", R.drawable.crown, "10");

    public static Config nomor_config = new Config(NORMAL,"体验区",R.drawable.home,R.drawable.home2,"黄金区",R.drawable.vip,R.drawable.vip2,vip_gold,vip_diamond1,R.drawable.pay1);
    public static Config gold_config = new Config(GOLD,"黄金区",R.drawable.vip,R.drawable.vip2,"钻石区",R.drawable.zuanshi,R.drawable.zuanshi2,vip_diamond2,null,R.drawable.pay2);
    public static Config diamond_config = new Config(DIAMOND,"钻石区",R.drawable.zuanshi,R.drawable.zuanshi2,"黑金区",R.drawable.black,R.drawable.black2,vip_black,null,R.drawable.pay3);
    public static Config black_config = new Config(BLACK,"黑金区",R.drawable.black,R.drawable.black2,"紫钻区",R.drawable.zuanshi,R.drawable.zuanshi2,vip_purple,null,R.drawable.pay4);

    public static Config purple_config = new Config(PURPLE,"紫钻区",R.drawable.zuanshi,R.drawable.zuanshi2,"蓝钻区",R.drawable.zuanshi,R.drawable.zuanshi2,vip_blue,null,R.drawable.pay5);
    public static Config blue_config = new Config(BLUE,"蓝钻区",R.drawable.zuanshi,R.drawable.zuanshi2,"皇冠区",R.drawable.crown1,R.drawable.crown2,vip_king,null,R.drawable.pay7);
    public static Config red_config = new Config(RED,"蓝钻区",R.drawable.zuanshi,R.drawable.zuanshi2,"皇冠区",R.drawable.crown1,R.drawable.crown2,null,null,R.drawable.pay6);
    public static Config crown_config = new Config(CROWN,"蓝钻区",R.drawable.black,R.drawable.black2,"皇冠区",R.drawable.crown1,R.drawable.crown2,vip_red,null,R.drawable.pay7);

    public static Config config;

    public static Config pay_config;
}
