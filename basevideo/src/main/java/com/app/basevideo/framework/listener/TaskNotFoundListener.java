package com.app.basevideo.framework.listener;


import com.app.basevideo.framework.message.AbsTaskMessage;


/**
 * 没有找到处理任务的监听
 *
 * @param <T>
 */
public interface TaskNotFoundListener<T extends AbsTaskMessage<?>> {
    /**
     * 回调接口函数
     *
     * @param message
     * @return
     */
    public boolean onNotFindTask(T message);

}
