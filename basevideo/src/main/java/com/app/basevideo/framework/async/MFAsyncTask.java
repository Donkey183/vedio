package com.app.basevideo.framework.async;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.app.basevideo.framework.UniqueId;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
public abstract class MFAsyncTask<Params, Progress, Result> {
    private static final int MESSAGE_POST_RESULT = 0x1;
    private static final int MESSAGE_POST_PROGRESS = 0x2;
    private static final MFAsyncTaskExecutor sDefaultExecutor = MFAsyncTaskExecutor.getInstance();
    private static final InternalHandler sHandler = new InternalHandler(Looper.getMainLooper());

    private final WorkerRunnable<Params, Result> mWorker;
    private final MFAsyncTaskFuture<Result> mFuture;
    private volatile AsyncTaskStatus mStatus = AsyncTaskStatus.PENDING;
    private int mPriority = MFAsyncTaskPriority.LOW;
    private int mTag = 0;
    private String mKey = null;
    private MFAsyncTaskParallel mParallel = null;
    private boolean isSelfExecute = false;
    private final AtomicBoolean mTaskInvoked = new AtomicBoolean(false);
    private final AtomicBoolean mPreCancelInvoked = new AtomicBoolean(false);
    private boolean mIsTimeout = false;

    /**
     * 当前异步任务的状态。
     * <ul>
     * <li><code>PENDING</code> 调度前,已经加入到调度队列中</li>
     * <li><code>RUNNING</code> 正在运行</li>
     * <li><code>FINISHED</code> 运行结束</li>
     * </ul>
     */
    public enum AsyncTaskStatus {
        PENDING, // 调度前,已经加入到调度队列中
        RUNNING, // 正在运行
        FINISHED,// 运行结束
    }

    /**
     * 构造函数
     */
    public MFAsyncTask() {
        mWorker = new WorkerRunnable<Params, Result>() {
            @Override
            public Result call() throws Exception {
                if (mFuture.isCancelled() == false) {
                    return postResult(doInBackground(mParams));
                } else {
                    return postResult(null);
                }
            }
        };

        mFuture = new MFAsyncTaskFuture<Result>(mWorker, this) {
            @Override
            protected void done() {
                try {
                    final Result result = get();
                    postResult(result);
                } catch (InterruptedException e) {

                } catch (ExecutionException e) {
                    postResult(null);
                } catch (CancellationException e) {
                    postResult(null);
                } catch (Throwable t) {
                    throw new RuntimeException(
                            "An error occured while executing "
                                    + "doInBackground()", t);
                }
            }

            @Override
            protected void cancelTask() {
                // TODO Auto-generated method stub
                MFAsyncTask.this.cancel();
            }
        };
    }

    /**
     * 设置该异步任务的优先级
     *
     * @param priority 新的优先级
     *                 <p>该值越大，优先级越高</p>
     *                 <b>请使用接口{@link MFAsyncTaskPriority}中的常量设置</b>
     * @return 返回原来的优先级
     */
    public synchronized int setPriority(int priority) {
        if (mStatus != AsyncTaskStatus.PENDING) {
            throw new IllegalStateException("the task is already running");
        }
        int old = mPriority;
        mPriority = priority;
        return old;
    }

    /**
     * 获得当前任务的优先级
     * 不要加synchronized，会严重影响性能
     *
     * @return 当前任务的优先级
     */
    public int getPriority() {
        return mPriority;
    }

    /**
     * 获取该任务的tag
     * 不要加synchronized，会严重影响性能
     *
     * @return 当前任务的tag
     */
    public int getTag() {
        return mTag;
    }

    /**
     * 设置该任务的tag,唯一标识符
     *
     * @param tag
     * @return 原来tag的id
     */
    public synchronized int setTag(UniqueId tag) {
        if (mStatus != AsyncTaskStatus.PENDING) {
            throw new IllegalStateException("the task is already running");
        }
        int tmp = mTag;
        if (tag != null) {
            mTag = tag.getId();
        }
        return tmp;
    }

    /**
     * 获取该异步任务的key
     * 不要加synchronized，会严重影响性能
     *
     * @return 该异步任务的key
     */
    public String getKey() {
        return mKey;
    }

    /**
     * 设置该异步任务的key
     *
     * @param key 新的key
     * @return 返回旧的key
     */
    public synchronized String setKey(String key) {
        if (mStatus != AsyncTaskStatus.PENDING) {
            throw new IllegalStateException("the task is already running");
        }
        String tmp = mKey;
        mKey = key;
        return tmp;
    }

    /**
     * 获取该异步任务的并行度
     * 不要加synchronized，会严重影响性能
     *
     * @return 该异步任务的并行度, 见{@link MFAsyncTaskParallel}
     */
    public MFAsyncTaskParallel getParallel() {
        return mParallel;
    }

    /**
     * 设置该异步任务的并行度
     *
     * @param parallel 新的并行度
     *                 <p>参考{@link MFAsyncTaskParallel}</p>
     */
    public synchronized void setParallel(MFAsyncTaskParallel parallel) {
        if (mStatus != AsyncTaskStatus.PENDING) {
            throw new IllegalStateException("the task is already running");
        }
        mParallel = parallel;
    }

    /**
     * 该任务是否为自运行
     *
     * @return true表示自运行，false表示非自运行
     */
    public boolean isSelfExecute() {
        return isSelfExecute;
    }

    /**
     * 设置该任务是否自运行的任务
     * 如果isSelfExecute为true，<b>该任务在调度时将被直接运行。</b>
     *
     * @param isSelfExecute true | false
     */
    public synchronized void setSelfExecute(boolean isSelfExecute) {
        if (mStatus != AsyncTaskStatus.PENDING) {
            throw new IllegalStateException("the task is already running");
        }
        this.isSelfExecute = isSelfExecute;
    }

    synchronized void setTimeout(boolean isTimeout) {
        mIsTimeout = isTimeout;
    }

    /**
     * 判断该异步任务是否已经超时
     *
     * @return
     */
    public boolean isTimeout() {
        return mIsTimeout;
    }

    @SuppressWarnings("unchecked")
    private Result postResult(Result result) {
        if (mTaskInvoked.compareAndSet(false, true)) {
            Message message = sHandler.obtainMessage(MESSAGE_POST_RESULT,
                    new BdAsyncTaskResult<Result>(this, result));
            message.sendToTarget();
            return result;
        } else {
            return null;
        }
    }

    /**
     * 获取当前任务的调度状态
     *
     * @return 返回当前任务的调度状态 参考{@link AsyncTaskStatus}
     */
    public final AsyncTaskStatus getStatus() {
        return mStatus;
    }

    protected abstract Result doInBackground(Params... params);

    /**
     * 取消任务
     */
    public void cancel() {
        cancel(true);
    }

    protected void onPreCancel() {
    }

    protected void onPreExecute() {
    }

    protected void onPostExecute(Result result) {
    }

    protected void onProgressUpdate(Progress... values) {
    }

    protected void onCancelled(Result result) {
        onCancelled();
    }

    protected void onCancelled() {
    }

    /**
     * 该任务是否被取消
     *
     * @return true表示被取消，false表示未被取消
     */
    public final boolean isCancelled() {
        return mFuture.isCancelled();
    }

    /**
     * <h3>尝试取消任务</h3>
     * <p>该方法有可能失败，如任务已经完成、任务已经被取消、或因其它原因无法被取消（通常为IO操作）.</p>
     * <p>如果成功，则没有被调度的任务将不被执行。而正在执行的任务的情况由mayInterruptIfRunning变量决定。</p>
     * <p>如果 mayInterruptIfRunning==true, 将中止该任务的执行。</p>
     *
     * @param mayInterruptIfRunning 如果任务正在执行，是否中断
     * @return 取消成功与否
     */
    public synchronized final boolean cancel(boolean mayInterruptIfRunning) {
        if (isSelfExecute == false) {
            sDefaultExecutor.removeWaitingTask(this);
        }
        boolean ret = mFuture.cancel(mayInterruptIfRunning);
        if (mPreCancelInvoked.compareAndSet(false, true)) {
            onPreCancel();
        }
        return ret;
    }

    /**
     * 获取任务的执行结果。如果任务未完成，将会等待
     *
     * @return 任务的执行结果
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public final Result get() throws InterruptedException, ExecutionException {
        return mFuture.get();
    }

    /**
     * 获取任务的执行结果，并设置最长等待时间
     *
     * @param timeout 最长等待时间
     * @param unit    等待时间的单位,见{@link TimeUnit}
     * @return 返回任务执行的结果
     * @throws ExecutionException {@link ExecutionException}
     * @throws TimeoutException   {@link TimeoutException}
     */
    public final Result get(long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        return mFuture.get(timeout, unit);
    }

    /**
     * 调度任务，任务具体执行的时间根据调度策略而定。
     *
     * @param params 输入的参数
     * @return 返回本身
     */
    public final MFAsyncTask<Params, Progress, Result> execute(Params... params) {
        return executeOnExecutor(sDefaultExecutor, params);
    }

    /**
     * 调度任务。
     *
     * @param exec   指定一个调度器,见{@link Executor},{@link MFAsyncTaskExecutor}
     * @param params 输入的不定参数
     * @return 任务本身
     */
    public synchronized final MFAsyncTask<Params, Progress, Result> executeOnExecutor(
            Executor exec, Params... params) {
        if (mStatus != AsyncTaskStatus.PENDING) {
            switch (mStatus) {
                case RUNNING:
                    throw new IllegalStateException("Cannot execute task:"
                            + " the task is already running.");
                case FINISHED:
                    throw new IllegalStateException("Cannot execute task:"
                            + " the task has already been executed "
                            + "(a task can be executed only once)");
                default:
                    break;
            }
        }

        mStatus = AsyncTaskStatus.RUNNING;

        onPreExecute();

        mWorker.mParams = params;
        exec.execute(mFuture);

        return this;
    }

    protected final void publishProgress(Progress... values) {
        if (!isCancelled()) {
            sHandler.obtainMessage(MESSAGE_POST_PROGRESS,
                    new BdAsyncTaskResult<Progress>(this, values))
                    .sendToTarget();
        }
    }

    private void finish(Result result) {
        if (isCancelled()) {
            onCancelled(result);
        } else {
            onPostExecute(result);
        }
        mStatus = AsyncTaskStatus.FINISHED;
    }

    private static class InternalHandler extends Handler {
        public InternalHandler(Looper looper) {
            super(looper);
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public void handleMessage(Message msg) {
            BdAsyncTaskResult result = (BdAsyncTaskResult) msg.obj;
            switch (msg.what) {
                case MESSAGE_POST_RESULT:
                    // There is only one result
                    result.mTask.finish(result.mData[0]);
                    break;
                case MESSAGE_POST_PROGRESS:
                    result.mTask.onProgressUpdate(result.mData);
                    break;
            }
        }
    }

    private static abstract class WorkerRunnable<Params, Result> implements
            Callable<Result> {
        Params[] mParams;
    }

    private static class BdAsyncTaskResult<Data> {
        @SuppressWarnings("rawtypes")
        final MFAsyncTask mTask;
        final Data[] mData;

        @SuppressWarnings("rawtypes")
        BdAsyncTaskResult(MFAsyncTask task, Data... data) {
            mTask = task;
            mData = data;
        }
    }

    /**
     * 根据唯一标识符移除任务.如果任务正在执行，将尝试取消任务。
     *
     * @param tag 唯一标识符
     */
    public static void removeAllTask(UniqueId tag) {
        sDefaultExecutor.removeAllTask(tag);
    }

    /**
     * 根据 tag 和 key，移除任务
     *
     * @param tag
     * @param key
     */
    public static void removeAllTask(UniqueId tag, String key) {
        sDefaultExecutor.removeAllTask(tag, key);
    }

    /**
     * 移除正在等待的标识为tag的任务
     *
     * @param tag
     */
    public static void removeAllWaitingTask(UniqueId tag) {
        sDefaultExecutor.removeAllWaitingTask(tag);
    }

    /**
     * 移除正在等待调度的标识为tag 关键字为key的任务
     *
     * @param tag
     * @param key
     */
    public static void removeAllWaitingTask(UniqueId tag, String key) {
        sDefaultExecutor.removeAllWaitingTask(tag, key);
    }

    /**
     * 查找标识符为tag的任务
     *
     * @param tag
     * @return 标识符为tag的任务列表
     */
    public static LinkedList<MFAsyncTask<?, ?, ?>> searchAllTask(UniqueId tag) {
        return sDefaultExecutor.searchAllTask(tag);
    }

    /**
     * 查找标识符为tag,关键字为key的任务
     *
     * @param tag
     * @param key
     * @return 查找到的任务列表
     */
    public static LinkedList<MFAsyncTask<?, ?, ?>> searchAllTask(UniqueId tag, String key) {
        return sDefaultExecutor.searchAllTask(tag, key);
    }

    /**
     * 查找关键字为key的任务
     *
     * @param key
     * @return 查找到的任务列表
     */
    public static MFAsyncTask<?, ?, ?> searchTask(String key) {
        return sDefaultExecutor.searchTask(key);
    }

    /**
     * 查找正在等待的关键字为key的任务
     *
     * @param key
     * @return 返回查找到的任务列表
     */
    public static MFAsyncTask<?, ?, ?> searchWaitingTask(String key) {
        return sDefaultExecutor.searchWaitingTask(key);
    }

    /**
     * 查找正在等待的标识符为tag的任务
     *
     * @param tag
     * @return
     */
    public static LinkedList<MFAsyncTask<?, ?, ?>> searchWaitingTask(UniqueId tag) {
        return sDefaultExecutor.searchWaitingTask(tag);
    }

    /**
     * 查找正在执行的关键字为key的任务
     *
     * @param key
     * @return 返回查找到的任务列表
     */
    public static MFAsyncTask<?, ?, ?> searchActivTask(String key) {
        return sDefaultExecutor.searchActivTask(key);
    }

    /**
     * 获取标识符为tag的任务数量
     *
     * @param tag
     * @return 任务数量
     */
    public static int getTaskNum(UniqueId tag) {
        return getTaskNum(null, tag);
    }

    /**
     * 获取标识符为tag，关键字为key的任务数量
     *
     * @param key
     * @param tag
     * @return 任务数量
     */
    public static int getTaskNum(String key, UniqueId tag) {
        return sDefaultExecutor.getTaskNum(key, tag);
    }
}
