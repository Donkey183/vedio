package com.app.basevideo.framework;


import com.app.basevideo.framework.listener.AbsMessageListener;

/**
 * 优先级抽象类，只有监听器{@link AbsMessageListener}有优先级策略
 */
public abstract class Priority {
    private int mPriority = 0;

    /**
     * 获取优先级
     *
     * @return
     */
    public int getPriority() {
        return mPriority;
    }

    /**
     * 设置优先级，数值越小，优先级越高
     *
     * @param priority
     */
    public void setPriority(int priority) {
        this.mPriority = priority;
    }

}
