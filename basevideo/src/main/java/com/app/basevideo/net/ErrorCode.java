package com.app.basevideo.net;

import com.app.basevideo.base.MFBaseActivity;

public class ErrorCode {

    /**
     * 通用错误码定义,需事先与服务端约定接口返回的错误码
     * 特殊的错误码会被通知到{@link MFBaseActivity#processNetErrorCode(BaseHttpResult)}中做处理
     */

    public static final String ERROR_CODE = "error_code";

    /**
     * 请求成功
     */

    public static final int ERROR_CODE_RESULT_OK = 0;

    /**
     * 登录token过期
     */

    public static final int ERROR_CODE_INVALID_ACCESS_TOKEN = 0x1000;

    /**
     * 没有绑定手机号
     */

    public static final int ERROR_CODE_UNBIND_MOBILE = 0x1001;

}
