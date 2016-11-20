package com.app.basevideo.framework.async;

/**
 * 异步任务{@link GeneralAsyncTask}的优先级定义。
 * <b>请使用此接口中定义的常量，设置优先级</b>
 * <ul>
 * <li>低优先级 <code>LOW</code></li>
 * <li>中优先级 <code>MIDDLE</code></li>
 * <li>高优先级 <code>HIGH></code></li>
 * <li>越高优先级 <code>SUPER_HIGH</code>
 * <b>在此优先级时，<code>MFAsyncTask</code>将尽最大努力使其执行</b></li>
 * </ul>
 * <p>
 * 使用时，与用户体验、交互相关的任务应设置较高优先级，而长时间任务，如
 * <ul>
 * <li>下载</li>
 * <li>IO操作</li>
 * <li>...</li>
 * </ul>
 * 应使用较低优先级。
 * </p>
 */

public interface MFAsyncTaskPriority {
    public static final int LOW = 1;
    public static final int MIDDLE = 2;
    public static final int HIGH = 3;
    public static final int SUPER_HIGH = 4;// 超高优先级执行，可独立于5个线程之外执行
}
