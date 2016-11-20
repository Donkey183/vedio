package com.app.basevideo.framework.async;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 将GeneralAsyncTask封装为FutrueTask，用于调度器调度
 *
 * @param <V> 返回的类类型
 */

public abstract class MFAsyncTaskFuture<V> extends FutureTask<V> {
    private MFAsyncTask<?, ?, ?> mTask = null;

    public MFAsyncTask<?, ?, ?> getTask() {
        return mTask;
    }

    /**
     * 构造函数
     *
     * @param callable
     * @param task
     */
    public MFAsyncTaskFuture(Callable<V> callable, MFAsyncTask<?, ?, ?> task) {
        super(callable);
        mTask = task;
    }

    /**
     * 构造函数
     *
     * @param runnable
     * @param result   返回的类类型
     * @param task
     */
    public MFAsyncTaskFuture(Runnable runnable, V result, MFAsyncTask<?, ?, ?> task) {
        super(runnable, result);
        mTask = task;
    }

    protected abstract void cancelTask();

}
