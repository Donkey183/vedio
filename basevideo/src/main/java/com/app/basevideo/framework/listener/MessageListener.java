package com.app.basevideo.framework.listener;

import com.app.basevideo.framework.message.CommonMessage;

/**
 * 自定义任务消息回调类
 */
public abstract class MessageListener extends AbsMessageListener<CommonMessage<?>> {

    /**
     * 构造函数
     *
     * @param cmd
     */
    public MessageListener(int cmd) {
        super(cmd);
    }

    /**
     * 构造函数
     *
     * @param cmd
     * @param isSelfListener
     */
    public MessageListener(int cmd, boolean isSelfListener) {
        super(cmd, isSelfListener);
    }
}
