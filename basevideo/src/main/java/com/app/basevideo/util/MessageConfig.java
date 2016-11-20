package com.app.basevideo.util;

import com.app.basevideo.base.MFBaseActivity;

public class MessageConfig {
    /**
     * basefinance 工程内部使用命令号
     */

    private static final int CMD_BASE = 1000;

    /**
     * app全局退出命令号
     * {@link MFBaseActivity#appExitListener}
     */

    public static final int CMD_APP_EXIT = CMD_BASE - 1;

    /**
     * 网络错误号处理
     * {@link MFBaseActivity#processNetErrorCodeListener}
     */

    public static final int CMD_NET_ERROR_CODE_PROCESS = CMD_BASE - 2;


    /**
     * 跳转到登录页面
     */

    public static final int CMD_GOTO_LOGIN_ACTIVITY = CMD_BASE - 3;


    /**
     * Dialog自动消失监听
     */

    public static final int CMD_NET_ERROR = CMD_BASE - 4;

    /**
     * 车险详情卡片保额输入满足条件（在车辆价值的0.7~1.3倍之间）
     */

    public static final int CMD_COVERAGE_INPUT_MATCHED = CMD_BASE - 5;

    /**
     * 跳转到App配置页面
     */

    public static final int CMD_GOTO_APP_CONFIG_ACTIVITY = CMD_BASE - 6;

}
