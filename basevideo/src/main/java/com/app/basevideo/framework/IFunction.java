package com.app.basevideo.framework;

import com.app.basevideo.framework.message.AbsTaskMessage;
import com.app.basevideo.framework.task.AbsMessageTask;

import java.util.LinkedList;

/**
 * 消息处理抽象接口，提供消息的曾删改查
 *
 * @param <M> 消息类型的泛型
 * @param <T> 任务的泛型
 */
public interface IFunction<M extends AbsTaskMessage<?>, T extends AbsMessageTask> {
    /**
     * 发送消息
     *
     * @param message
     * @param task
     */
    public abstract void sendMessage(M message, T task);

    /**
     * 删除消息
     *
     * @param tag
     * @return
     */
    public abstract void removeMessage(UniqueId tag);

    /**
     * 删除消息
     *
     * @param cmd
     * @param tag
     * @return
     */
    public abstract void removeMessage(int cmd, UniqueId tag);

    /**
     * 删掉等待运行的消息
     *
     * @param tag
     * @return
     */
    public abstract void removeWaitingMessage(UniqueId tag);

    /**
     * 删掉等待运行的消息
     *
     * @param cmd
     * @param tag
     * @return
     */
    public abstract void removeWaitingMessage(int cmd, UniqueId tag);

    /**
     * 查找消息
     *
     * @param tag
     * @return
     */
    public abstract LinkedList<M> findMessage(UniqueId tag);

    /**
     * 查找消息
     *
     * @param cmd
     * @param tag
     * @return
     */
    public abstract LinkedList<M> findMessage(int cmd, UniqueId tag);

    /**
     * 是否有tag等于参数的消息
     *
     * @param tag
     * @return true表示有，false表示没有
     */
    public abstract boolean haveMessage(UniqueId tag);

    /**
     * 是否有tag等于参数且命令号等于参数的消息
     *
     * @param cmd 命令号
     * @param tag
     * @return true表示有，false表示没有
     */
    public abstract boolean haveMessage(int cmd, UniqueId tag);

    /**
     * 获取消息tag等于参数的消息个数
     * <p/>
     * <p>
     * 可用于开发者要做一些事情，但是队列事情没有做完，需要做一些处理。如底层消息队列还有任务在执行，切换帐号时可以让用户等待一下
     * </p>
     *
     * @param tag
     * @return 消息个数
     * @see #getMessageNum(int, UniqueId)
     */
    public abstract int getMessageNum(UniqueId tag);

    /**
     * 获取消息tag等于参数的消息个数
     *
     * @param cmd
     * @param tag
     * @return 消息个数
     * @see #getMessageNum(UniqueId)
     */
    public abstract int getMessageNum(int cmd, UniqueId tag);
}
