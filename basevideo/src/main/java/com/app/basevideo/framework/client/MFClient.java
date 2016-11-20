package com.app.basevideo.framework.client;


import com.app.basevideo.framework.IFunction;
import com.app.basevideo.framework.manager.MessageManager;
import com.app.basevideo.framework.message.AbsTaskMessage;
import com.app.basevideo.framework.task.AbsMessageTask;

/**
 * 处理基类，供三种业务类型来实现，侧重消息的处理，即底层消息队列的增删查询等操作
 *
 * @param <M> 消息类型泛型
 * @param <T> 任务类型泛型
 */
public abstract class MFClient<M extends AbsTaskMessage<?>, T extends AbsMessageTask> implements IFunction<M, T> {
    protected MessageManager mMessageManager = null;

    /**
     * 构造函数，必须传入消息管理器
     *
     * @param manager
     */
    public MFClient(MessageManager manager) {
        mMessageManager = manager;
    }

    /**
     * 获取消息管理器，继承类可以通过保护类型mMessageManager来获取
     *
     * @return
     */
    public MessageManager getMessageManager() {
        return mMessageManager;
    }

    /**
     * 发送消息去执行任务
     *
     * @param message
     * @param task
     */
    @Override
    public abstract void sendMessage(M message, T task);

}
