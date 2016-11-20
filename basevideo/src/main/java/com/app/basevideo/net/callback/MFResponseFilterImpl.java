package com.app.basevideo.net.callback;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.framework.manager.MessageManager;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.BaseHttpResult;
import com.app.basevideo.net.ErrorCode;
import com.app.basevideo.util.MessageConfig;

public class MFResponseFilterImpl implements MFResponseFilter<BaseHttpResult> {

    @Override
    public void doFilter(BaseHttpResult baseHttpResult) {
        /**
         * 将所有网络请求错误码用消息框架通知到Baseactivity去处理{@link MFBaseActivity#processNetErrorCode(BaseHttpResult)}
         * 无需处理的错误码请添加拦截case
         */
        if (baseHttpResult == null) {
            return;
        }
        /**
         * 请求成功时不做处理，若想进行成功提示请在具体Activity 的 onHttpResponse()方法中进行处理
         */
        if (baseHttpResult.errno == ErrorCode.ERROR_CODE_RESULT_OK || baseHttpResult.errorCode == ErrorCode.ERROR_CODE_RESULT_OK || baseHttpResult.success) {
            return;
        }
        switch (baseHttpResult.errorCode) {

            default:
                MessageManager.getInstance().dispatchResponsedMessage(new CommonMessage<BaseHttpResult>(MessageConfig.CMD_NET_ERROR_CODE_PROCESS, baseHttpResult));
                break;
        }
    }
}
