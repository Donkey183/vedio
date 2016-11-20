package com.app.basevideo.framework.task;

import com.app.basevideo.framework.async.MFAsyncTaskParallel;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.framework.message.TaskMessage;
import com.app.basevideo.framework.util.MessageHelper;

/**
 * 自定义任务
 * <p/>
 * <p>
 * 事例：
 * <p/>
 * <pre>
 * // 注册Custom任务
 * MessageTask.CustomRunnable<String> runnable = new MessageTask.CustomRunnable<String>() {
 * 	\@Override
 * 	public CommonMessage<?> run(TaskMessage<String> message) {
 * 		if (message == null || message.getData() == null) {
 * 			return null;
 *        }
 *
 * 		... // 消息处理，具体任务编写
 * 		return null;
 *    }
 * };
 *
 * MessageTask task = new MessageTask(自定义任务命令号, runnable);
 * task.setType(MessageTask.TASK_TYPE.SYNCHRONIZED); // 此处设理为同步任务，即调用处线程同步执行task的runnable，对于耗时任务，如果是在主线程调用，则需要设置为异步
 * MessageManager.getInstance().registerTask(task);
 * </pre>
 * <p/>
 * </p>
 */
public class MessageTask extends AbsMessageTask {
    public enum TASK_TYPE {
        SYNCHRONIZED,
        ASYNCHRONIZED
    }

    ;

    private CustomRunnable<?> mRunnable = null;
    private TASK_TYPE mType = TASK_TYPE.ASYNCHRONIZED;
    private boolean isImme = false;
    private MFAsyncTaskParallel mBdMFAsyncTaskParallel = null;

    /**
     * 构造方法，需要cmd和runnable
     *
     * @param cmd
     * @param runnable
     */
    public MessageTask(int cmd, CustomRunnable<?> runnable) {
        super(cmd);
        mRunnable = runnable;
    }

    /**
     * 获取runnable
     *
     * @return
     */
    public CustomRunnable<?> getRunnable() {
        return mRunnable;
    }

    /**
     * 处理函数接口
     *
     * @param <T>
     */
    public static interface CustomRunnable<T> {
        /**
         * 执行处理
         *
         * @param taskMessage
         * @return
         */
        public abstract CommonMessage<?> run(TaskMessage<T> taskMessage);
    }

    /**
     * 检查cmd合法性
     */
    @Override
    public boolean checkCmd() {
        return MessageHelper.checkCustomCmd(mCmd);
    }

    /**
     * 返回任务类型，同步或异步
     *
     * @return
     */
    public TASK_TYPE getType() {
        return mType;
    }

    /**
     * 设置任务类型，同步或异步，默认为异步，调用地方sendMessage触发任务执行，如果是在主线程调用且任务是耗时操作，则需要设置为异步
     *
     * @param type
     */
    public void setType(TASK_TYPE type) {
        this.mType = type;
    }

    /**
     * 是否立即执行任务，若设置为立即执行，底层异步线程池会立即开启一个最高优先级线程
     *
     * @return
     */
    public boolean isImme() {
        return isImme;
    }

    /**
     * 设置是否立即执行，若设置为立即执行，底层异步线程池会立即开启一个最高优先级线程
     *
     * @param isImme
     */
    public void setImme(boolean isImme) {
        this.isImme = isImme;
    }

    /**
     * 获取任务并行度
     *
     * @return
     */
    public MFAsyncTaskParallel getTaskParallel() {
        return mBdMFAsyncTaskParallel;
    }

    /**
     * 设置任务并行度
     *
     * @param bdMFAsyncTaskParallel
     */
    public void setTaskParallel(MFAsyncTaskParallel bdMFAsyncTaskParallel) {
        this.mBdMFAsyncTaskParallel = bdMFAsyncTaskParallel;
    }
}
