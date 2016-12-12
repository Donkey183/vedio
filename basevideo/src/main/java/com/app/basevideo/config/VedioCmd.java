package com.app.basevideo.config;


import com.app.basevideo.util.MessageConfig;

public class VedioCmd {
    /**
     * 业务层启始命令号,定义为10000
     * 前1000为BaseFinance 包中使用，保留 {@link MessageConfig#CMD_BASE}
     */

    public static final int CMD_START = 10000;


    /**
     * 跳转到登录页面
     */
    public static final int CMD_GOTO_LOGIN_ACTIVITY = CMD_START + 1;

    /**
     * 跳转到启动引导图页面
     */
    public static final int CMD_GOTO_LAUNCH_GUIDE_ACTIVITY = CMD_START + 2;

    /**
     * 跳转到启动欢迎页(广告页)
     */
    public static final int CMD_GOTO_LAUNCH_WELCOME_ACTIVITY = CMD_START + 3;

    /**
     * 跳转App配置页面
     */
    public static final int CMD_GOTO_APP_CONFIG_ACTIVITY = CMD_START + 4;

    /**
     * 充值成功回调
     */
    public static final int CMD_PAY_SUCCESS = CMD_START + 5;

    /**
     * 支付回调
     */
    public static final int CMD_PAY_CALL_BACK = CMD_START + 8;


    /**
     * 销毁支付弹框
     */
    public static final int DISS_MISS_ALERT = CMD_START + 6;

    /**
     * 播放结束
     */

    public static final int VIDEO_PLAY_END = CMD_START + 7;
}
