package com.app.basevideo.framework.manager;

import com.app.basevideo.framework.UniqueId;
import com.app.basevideo.framework.client.MFClientImpl;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.framework.message.TaskMessage;
import com.app.basevideo.framework.task.MessageTask;

import java.util.LinkedList;


/**
 * 自定义任务管理器
 * <p/>
 * <p>
 * 创建自己的消息处理的CustomClient，指定自定义任务的默认的超时重试配置
 * </p>
 */
public class ManagerImpl extends Manager<TaskMessage<?>, MessageTask, CommonMessage<?>> {
    private MFClientImpl mClient = null;

    /**
     * 构造函数
     *
     * @param manager
     */
    public ManagerImpl(MessageManager manager) {
        super(manager);
        mClient = new MFClientImpl(manager);
    }

    /**
     * 发送消息
     */
    @Override
    public void sendMessage(TaskMessage<?> taskMessage, MessageTask task) {
        mClient.sendMessage(taskMessage, task);
    }

    /**
     * 执行任务
     *
     * @param taskMessage
     * @param task
     * @param cls
     * @return
     */
    public <T> CommonMessage<T> runTask(TaskMessage<?> taskMessage, MessageTask task, Class<T> cls) {
        return mClient.runTask(taskMessage, task, cls);
    }

    /**
     * 删除消息
     */
    @Override
    public void removeMessage(UniqueId tag) {
        mClient.removeMessage(tag);
    }

    /**
     * 删除消息
     */
    @Override
    public void removeMessage(int cmd, UniqueId tag) {
        mClient.removeMessage(cmd, tag);
    }

    /**
     * 查询消息
     */
    @Override
    public LinkedList<TaskMessage<?>> findMessage(UniqueId tag) {
        return mClient.findMessage(tag);
    }

    /**
     * 查询消息
     */
    @Override
    public LinkedList<TaskMessage<?>> findMessage(int cmd, UniqueId tag) {
        return mClient.findMessage(cmd, tag);
    }

    /**
     * 移除等待队列消息
     */
    @Override
    public void removeWaitingMessage(UniqueId tag) {
        mClient.removeWaitingMessage(tag);
    }

    /**
     * 移除等待队列消息
     */
    @Override
    public void removeWaitingMessage(int cmd, UniqueId tag) {
        mClient.removeWaitingMessage(cmd, tag);
    }

    /**
     * 是否有消息
     */
    @Override
    public boolean haveMessage(UniqueId tag) {
        return mClient.haveMessage(tag);
    }

    /**
     * 是否有消息
     */
    @Override
    public boolean haveMessage(int cmd, UniqueId tag) {
        return mClient.haveMessage(cmd, tag);
    }

    /**
     * 获取消息数目
     */
    @Override
    public int getMessageNum(UniqueId tag) {
        return mClient.getMessageNum(tag);
    }

    /**
     * 获取消息数目
     */
    @Override
    public int getMessageNum(int cmd, UniqueId tag) {
        return mClient.getMessageNum(cmd, tag);
    }

}
