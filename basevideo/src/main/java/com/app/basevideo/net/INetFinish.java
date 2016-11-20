package com.app.basevideo.net;

import com.app.basevideo.framework.message.CommonMessage;

public interface INetFinish {
    public void onHttpResponse(CommonMessage<?> responsedMessage);
}
