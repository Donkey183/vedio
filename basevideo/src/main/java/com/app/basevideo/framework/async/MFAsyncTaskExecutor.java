package com.app.basevideo.framework.async;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.SparseIntArray;

import com.app.basevideo.framework.UniqueId;

import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 调度器。用于将新建的异步任务加入到执行等待队列，并根据各任务的不同属性调度执行。
 * <p/>
 * <h3>实现细节</h3>
 * <ol>
 * <li>当线程池大小小于<code>CORE_POOL_SIZE</code>，则新建线程，并处理请求</li>
 * <li>当线程池大小等于<code>CORE_POOL_SIZE</code>，把请求放入workQueue中，线程池中的空闲线程将从workQueue中取任务并处理</li>
 * <li>当workQueue放不下新入的任务时，新建线程入池，并处理请求，如果线程池大小到达<code>MAXIMUM_POOL_SIZE</code>上限，{@link java.util.concurrent.RejectedExecutionHandler}来做拒绝处理</li>
 * <li>另外，当线程池的线程数大于<code>CORE_POOL_SIZE</code>的时候，多余的线程会等待30秒，如果无请求可处理就自行销毁</li>
 * </ol>
 * <h3>使用方法</h3>
 * 请直接调用{@link com.didi.basefinance.asynctask.GeneralAsyncTask}
 */


public class MFAsyncTaskExecutor implements Executor {
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 256;
    private static final int KEEP_ALIVE = 30;
    private static final int TASK_MAX_TIME = 3 * 60 * 1000;
    private static final int TASK_MAX_TIME_ID = 1;
    private static final int TASK_RUN_NEXT_ID = 2;

    private static MFAsyncTaskExecutor sInstance = null;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            String log = "MFAsyncTask #"
                    + String.valueOf(mCount.getAndIncrement());
            Log.i("MFAsyncTask", log);
            return new Thread(r, log);
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue = new SynchronousQueue<Runnable>();
    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS,
            sPoolWorkQueue, sThreadFactory,
            new ThreadPoolExecutor.DiscardPolicy());

    private volatile int mRunningSuperHightTaskNum = 0;
    private volatile int mRunningHightTaskNum = 0;
    private volatile int mRunningMiddleTaskNum = 0;
    private volatile int mRunningLowTaskNum = 0;
    private final SparseIntArray mParallelMap = new SparseIntArray();
    private final LinkedList<AsyncTaskRunnable> mWaitingTasks = new LinkedList<AsyncTaskRunnable>();
    private final LinkedList<AsyncTaskRunnable> mRunningTasks = new LinkedList<AsyncTaskRunnable>();
    private final LinkedList<AsyncTaskRunnable> mTimeOutTasks = new LinkedList<AsyncTaskRunnable>();
    private HandlerThread sHandlerThread = null;
    private Handler mHandler = null;

    MFAsyncTaskExecutor() {
        sHandlerThread = new HandlerThread("MFAsyncTaskExecutor");
        sHandlerThread.start();
        mHandler = new Handler(sHandlerThread.getLooper()) {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                if (msg.what == TASK_MAX_TIME_ID) {
                    if (msg.obj != null && msg.obj instanceof AsyncTaskRunnable) {
                        taskTimeOut((AsyncTaskRunnable) (msg.obj));
                    }
                } else if (msg.what == TASK_RUN_NEXT_ID) {
                    if (msg.obj != null && msg.obj instanceof AsyncTaskRunnable) {
                        scheduleNext((AsyncTaskRunnable) (msg.obj));
                    }
                }
            }
        };
    }

    @Override
    public String toString() {
        return "mWaitingTasks = " + mWaitingTasks.size() + " mRunningTasks = " + mRunningTasks.size()
                + " mTimeOutTasks = " + mTimeOutTasks.size();
    }

    /**
     * 转化成日志字符串
     *
     * @return
     */
    public String toLogString() {
        return mWaitingTasks.size() + "/" + mRunningTasks.size() + "/" + mTimeOutTasks.size();
    }

    /**
     * 清除Instance，避免内存泄露
     */
    public static void clearInstance() {
        sInstance = null;
    }

    /**
     * 获取{@link MFAsyncTaskExecutor}单例
     *
     * @return
     */
    public static MFAsyncTaskExecutor getInstance() {
        if (null == sInstance) {
            synchronized (MFAsyncTaskExecutor.class) {
                if (null == sInstance) {
                    sInstance = new MFAsyncTaskExecutor();
                }
            }
        }
        return sInstance;
    }

    @Override
    public synchronized void execute(Runnable r) {
        if (!(r instanceof MFAsyncTaskFuture)) {
            return;
        }
        AsyncTaskRunnable runnable = new AsyncTaskRunnable((MFAsyncTaskFuture<?>) r) {
            @Override
            public void run() {
                try {
                    try {
                        if (getPriority() == MFAsyncTaskPriority.SUPER_HIGH) {
                            android.os.Process
                                    .setThreadPriority(android.os.Process.THREAD_PRIORITY_DEFAULT - 2);
                        } else if (getPriority() == MFAsyncTaskPriority.HIGH) {
                            android.os.Process
                                    .setThreadPriority(android.os.Process.THREAD_PRIORITY_DEFAULT - 1);
                        } else if (getPriority() == MFAsyncTaskPriority.MIDDLE) {
                            android.os.Process
                                    .setThreadPriority(android.os.Process.THREAD_PRIORITY_DEFAULT);
                        } else {
                            android.os.Process
                                    .setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                        }
                    } catch (Exception ex) {
                        Log.e("", ex.getMessage());
                    }
                    runTask();
                } finally {
                    if (isSelfExecute() == false) {
                        mHandler.sendMessageDelayed(
                                mHandler.obtainMessage(TASK_RUN_NEXT_ID, this), 1);
                    }
                }
            }
        };

        if (runnable.isSelfExecute() == true) {
            new Thread(runnable).start();
            return;
        }
        insertTask(runnable);
        scheduleNext(null);
    }

    /**
     * 按照优先级插入任务
     *
     * @param runnable
     */
    private synchronized void insertTask(AsyncTaskRunnable runnable) {
        if (runnable == null) {
            return;
        }
        int num = mWaitingTasks.size();
        int index = 0;
        for (index = 0; index < num; index++) {
            if (mWaitingTasks.get(index).getPriority() < runnable.getPriority()) {
                break;
            }
        }
        mWaitingTasks.add(index, runnable);
    }

    /**
     * 任务运行超时
     *
     * @param task
     */
    private synchronized void taskTimeOut(AsyncTaskRunnable task) {
        removeRunningTask(task);
        if (task.isCancelled() == false) {
            task.setTimeout(true);
            mTimeOutTasks.add(task);
            // 终止任务需要时间，所以提前终止线程
            if (mTimeOutTasks.size() > MAXIMUM_POOL_SIZE - CORE_POOL_SIZE * 2) {
                AsyncTaskRunnable runnable = mTimeOutTasks.poll();
                if (runnable != null) {
                    runnable.cancelTask();
                }
            }
        } else {
            Log.e("", "task TimeOut but it's cancelled()");
        }
        scheduleNext(null);
    }

    /**
     * 删除一个正在运行的任务
     *
     * @param task
     */
    private synchronized void removeRunningTask(AsyncTaskRunnable task) {
        if (task == null) {
            return;
        }
        if (task.IsTimeout() == true) {
            // 如果是超时运行的任务，直接在超时队列中移除
            mTimeOutTasks.remove(task);
        } else {
            // 如果是正在运行的任务，从运行队列删除，并且更新数量
            mRunningTasks.remove(task);
            mHandler.removeMessages(TASK_MAX_TIME_ID, task);
            switch (task.getPriority()) {
                case MFAsyncTaskPriority.SUPER_HIGH:
                    mRunningSuperHightTaskNum--;
                    break;
                case MFAsyncTaskPriority.HIGH:
                    mRunningHightTaskNum--;
                    break;
                case MFAsyncTaskPriority.MIDDLE:
                    mRunningMiddleTaskNum--;
                    break;
                case MFAsyncTaskPriority.LOW:
                    mRunningLowTaskNum--;
                    break;
                default:
                    break;
            }
            int tag = task.getParallelTag();
            if (tag != 0) {
                int num = mParallelMap.get(tag) - 1;
                if (num <= 0) {
                    mParallelMap.delete(tag);
                } else {
                    mParallelMap.put(tag, num);
                }
                if (num < 0) {
                    Log.e("", "removeTask error < 0");
                }
            }
        }
    }

    /**
     * 执行队列中的一个任务，变为将使其状态{@link MFAsyncTask.AsyncTaskStatus}执行态
     *
     * @param task
     */
    private synchronized void executeTask(AsyncTaskRunnable task) {
        if (task == null) {
            return;
        }
        mRunningTasks.add(task);
        mWaitingTasks.remove(task);
        THREAD_POOL_EXECUTOR.execute(task);
        mHandler.sendMessageDelayed(
                mHandler.obtainMessage(TASK_MAX_TIME_ID, task), TASK_MAX_TIME);
        switch (task.getPriority()) {
            case MFAsyncTaskPriority.SUPER_HIGH:
                mRunningSuperHightTaskNum++;
                if (mRunningSuperHightTaskNum >= CORE_POOL_SIZE) {
                    Log.e("", "SuperHight Task too much num = " + mRunningSuperHightTaskNum);
                }
                break;
            case MFAsyncTaskPriority.HIGH:
                mRunningHightTaskNum++;
                break;
            case MFAsyncTaskPriority.MIDDLE:
                mRunningMiddleTaskNum++;
                break;
            case MFAsyncTaskPriority.LOW:
                mRunningLowTaskNum++;
                break;
            default:
                break;
        }
        int tag = task.getParallelTag();
        if (tag != 0) {
            int num = mParallelMap.get(tag, 0) + 1;
            mParallelMap.put(tag, num);
        }
    }

    /**
     * 任务是否能并发运行
     *
     * @param activeNum
     * @param task
     * @return
     */
    private boolean canParallelExecute(int activeNum, AsyncTaskRunnable task) {
        if (task == null) {
            return false;
        }
        MFAsyncTaskParallel.AsyncTaskParallelType type = task.getParallelType();
        if (type == MFAsyncTaskParallel.AsyncTaskParallelType.SERIAL) {
            if (activeNum < 1) {
                return true;
            }
        } else if (type == MFAsyncTaskParallel.AsyncTaskParallelType.TWO_PARALLEL) {
            if (activeNum < 2) {
                return true;
            }
        } else if (type == MFAsyncTaskParallel.AsyncTaskParallelType.THREE_PARALLEL) {
            if (activeNum < 3) {
                return true;
            }
        } else if (type == MFAsyncTaskParallel.AsyncTaskParallelType.FOUR_PARALLEL) {
            if (activeNum < 4) {
                return true;
            }
        } else if (type == MFAsyncTaskParallel.AsyncTaskParallelType.CUSTOM_PARALLEL) {
            if (activeNum < task.getExcuteNum()) {
                return true;
            }
        } else {
            return true;
        }
        return false;
    }

    protected synchronized void scheduleNext(AsyncTaskRunnable current) {
        removeRunningTask(current);
        // 等待队列是优先级排好序的
        for (int i = 0; i < mWaitingTasks.size(); i++) {
            AsyncTaskRunnable task = mWaitingTasks.get(i);
            int parallelTag = task.getParallelTag();
            switch (task.getPriority()) {
                case MFAsyncTaskPriority.SUPER_HIGH:
                    // super优先级，如果并发度为最大，则立刻执行
                    if (parallelTag == 0) {
                        executeTask(task);
                        return;
                    }
                    break;
                case MFAsyncTaskPriority.HIGH:
                    // 如果遍历到高优先级任务，正在运行的任务数量超过CORE_POOL_SIZE，直接退出循环
                    if (mRunningHightTaskNum + mRunningMiddleTaskNum + mRunningLowTaskNum >= CORE_POOL_SIZE) {
                        return;
                    }
                    break;
                case MFAsyncTaskPriority.MIDDLE:
                    // 如果遍历到中优先级任务，正在运行的任务数量超过CORE_POOL_SIZE-1，直接退出循环
                    if (mRunningHightTaskNum + mRunningMiddleTaskNum + mRunningLowTaskNum >= CORE_POOL_SIZE - 1) {
                        return;
                    }
                    break;
                case MFAsyncTaskPriority.LOW:
                    // 如果遍历到底优先级任务，正在运行的任务数量超过CORE_POOL_SIZE-2，直接退出循环
                    if (mRunningHightTaskNum + mRunningMiddleTaskNum + mRunningLowTaskNum >= CORE_POOL_SIZE - 2) {
                        return;
                    }
                    break;
                default:
                    break;
            }

            int activeNum = mParallelMap.get(parallelTag);
            if (canParallelExecute(activeNum, task)) {
                executeTask(task);
                return;
            }
        }
    }

    /**
     * 参见{@link MFAsyncTask}中的同名方法
     *
     * @param tag
     */
    public synchronized void removeAllTask(UniqueId tag) {
        removeAllTask(tag, null);
    }

    /**
     * 参见{@link MFAsyncTask}中的同名方法
     *
     * @param tag
     * @param key
     */
    public synchronized void removeAllTask(UniqueId tag, String key) {
        removeAllWaitingTask(tag, key);
        removeTask(mRunningTasks, false, tag, key);
        removeTask(mTimeOutTasks, false, tag, key);
    }

    /**
     * 参见{@link MFAsyncTask}中的同名方法
     *
     * @param tag
     */
    public synchronized void removeAllWaitingTask(UniqueId tag) {
        removeAllWaitingTask(tag, null);
    }

    /**
     * 参见{@link MFAsyncTask}中的同名方法
     *
     * @param tag
     * @param key
     */
    public synchronized void removeAllWaitingTask(UniqueId tag, String key) {
        removeTask(mWaitingTasks, true, tag, key);
    }

    private synchronized void removeTask(LinkedList<AsyncTaskRunnable> tasks,
                                         boolean remove, UniqueId tagData, String key) {
        if (tagData == null) {
            return;
        }
        int tag = tagData.getId();
        Iterator<AsyncTaskRunnable> iterator = tasks.iterator();
        while (iterator.hasNext()) {
            AsyncTaskRunnable next = iterator.next();
            final int tmpTag = next.getTag();
            final String tmpKey = next.getKey();
            if ((key != null && tmpTag == tag && key.equals(tmpKey))
                    || (key == null && tag != 0 && tmpTag == tag)) {
                if (remove == true) {
                    iterator.remove();
                }
                next.cancelTask();
            }
        }
    }

    /**
     * 参见{@link MFAsyncTask}中的同名方法
     *
     * @param task
     */
    public synchronized void removeWaitingTask(MFAsyncTask<?, ?, ?> task) {
        Iterator<AsyncTaskRunnable> iterator = mWaitingTasks.iterator();
        while (iterator.hasNext()) {
            AsyncTaskRunnable next = iterator.next();
            if (next != null && next.getTask() == task) {
                iterator.remove();
                break;
            }
        }
        // LogUtil.d(MFAsyncTaskExecutor.getInstance().toString());
    }

    /**
     * 参见{@link MFAsyncTask}中的同名方法
     *
     * @param tag
     * @return
     */
    public int getTaskNum(UniqueId tag) {
        return getTaskNum(null, tag);
    }

    /**
     * 参见{@link MFAsyncTask}中的同名方法
     *
     * @param key
     * @param tag
     * @return
     */
    public int getTaskNum(String key, UniqueId tag) {
        return getQueueTaskNum(mWaitingTasks, key, tag)
                + getQueueTaskNum(mRunningTasks, key, tag)
                + getQueueTaskNum(mTimeOutTasks, key, tag);
    }

    private synchronized int getQueueTaskNum(LinkedList<AsyncTaskRunnable> list, String key, UniqueId tagData) {
        if (list == null || tagData == null) {
            return 0;
        }
        int tag = tagData.getId();
        int num = 0;
        Iterator<AsyncTaskRunnable> iterator = list.iterator();
        while (iterator.hasNext()) {
            AsyncTaskRunnable next = iterator.next();
            final int tmpTag = next.getTag();
            final String tmpKey = next.getKey();
            if ((key != null && tmpTag == tag && key.equals(tmpKey))
                    || (key == null && tag != 0 && tmpTag == tag)) {
                if (next.getTask() != null && next.getTask().isCancelled() == false) {
                    num++;
                }
            }
        }
        return num;
    }

    /**
     * 参见{@link MFAsyncTask}中的同名方法
     *
     * @param key
     * @return
     */
    public synchronized MFAsyncTask<?, ?, ?> searchTask(String key) {
        MFAsyncTask<?, ?, ?> tmp = null;
        tmp = searchTask(mWaitingTasks, key);
        if (tmp == null) {
            tmp = searchTask(mRunningTasks, key);
        }
        if (tmp == null) {
            tmp = searchTask(mTimeOutTasks, key);
        }
        return tmp;
    }

    /**
     * 参见{@link MFAsyncTask}中的同名方法
     *
     * @param tag
     * @return
     */
    public synchronized LinkedList<MFAsyncTask<?, ?, ?>> searchAllTask(UniqueId tag) {
        return searchAllTask(tag, null);
    }

    /**
     * 参见{@link MFAsyncTask}中的同名方法
     *
     * @param tag
     * @param key
     * @return
     */
    public synchronized LinkedList<MFAsyncTask<?, ?, ?>> searchAllTask(UniqueId tag, String key) {
        LinkedList<MFAsyncTask<?, ?, ?>> ret = new LinkedList<MFAsyncTask<?, ?, ?>>();
        LinkedList<MFAsyncTask<?, ?, ?>> tmp = null;
        tmp = searchAllTask(mWaitingTasks, tag, key);
        if (tmp != null) {
            ret.addAll(tmp);
        }
        tmp = searchAllTask(mRunningTasks, tag, key);
        if (tmp != null) {
            ret.addAll(tmp);
        }
        tmp = searchAllTask(mTimeOutTasks, tag, key);
        if (tmp != null) {
            ret.addAll(tmp);
        }
        return ret;
    }

    /**
     * 参见{@link MFAsyncTask}中的同名方法
     *
     * @param key
     * @return
     */
    public synchronized MFAsyncTask<?, ?, ?> searchWaitingTask(String key) {
        return searchTask(mWaitingTasks, key);
    }

    /**
     * 参见{@link MFAsyncTask}中的同名方法
     *
     * @param tag
     * @return
     */
    public synchronized LinkedList<MFAsyncTask<?, ?, ?>> searchWaitingTask(UniqueId tag) {
        LinkedList<MFAsyncTask<?, ?, ?>> ret = new LinkedList<MFAsyncTask<?, ?, ?>>();
        LinkedList<MFAsyncTask<?, ?, ?>> tmp = searchAllTask(mWaitingTasks, tag, null);
        if (tmp != null) {
            ret.addAll(tmp);
        }
        return ret;
    }

    /**
     * 参见{@link MFAsyncTask}中的同名方法
     *
     * @param key
     * @return
     */
    public synchronized MFAsyncTask<?, ?, ?> searchActivTask(String key) {
        return searchTask(mRunningTasks, key);
    }

    // end

    /**
     * 查找队列list中的关键字为key的任务
     *
     * @param list
     * @param key
     * @return 任务列表
     */
    public synchronized MFAsyncTask<?, ?, ?> searchTask(
            LinkedList<AsyncTaskRunnable> list, String key) {
        if (list == null || key == null) {
            return null;
        }
        Iterator<AsyncTaskRunnable> iterator = list.iterator();
        while (iterator.hasNext()) {
            AsyncTaskRunnable next = iterator.next();
            final String tmp = next.getKey();
            if (tmp != null && tmp.equals(key)
                    && next.getTask().isCancelled() == false) {
                return next.getTask();
            }
        }
        return null;
    }

    /**
     * 查找队列list中的标识符为tagData,关键字为key的所有任务
     *
     * @param list
     * @param tagData
     * @param key
     * @return 任务列表
     */
    public synchronized LinkedList<MFAsyncTask<?, ?, ?>> searchAllTask(
            LinkedList<AsyncTaskRunnable> list, UniqueId tagData, String key) {
        if (list == null || tagData == null) {
            return null;
        }
        int tag = tagData.getId();
        LinkedList<MFAsyncTask<?, ?, ?>> result = new LinkedList<MFAsyncTask<?, ?, ?>>();
        Iterator<AsyncTaskRunnable> iterator = list.iterator();
        while (iterator.hasNext()) {
            AsyncTaskRunnable next = iterator.next();
            final int tmpTag = next.getTag();
            final String tmpKey = next.getKey();
            if ((key != null && tmpTag == tag && key.equals(tmpKey))
                    || (key == null && tag != 0 && tmpTag == tag)) {
                if (next.getTask() != null && next.getTask().isCancelled() == false) {
                    result.add(next.getTask());
                }
            }
        }
        return result;
    }

    private static abstract class AsyncTaskRunnable implements Runnable {
        private MFAsyncTaskFuture<?> mMFAsyncTaskFuture = null;

        public AsyncTaskRunnable(MFAsyncTaskFuture<?> task) {
            if (task == null || task.getTask() == null) {
                throw new InvalidParameterException("parameter is null");
            }
            mMFAsyncTaskFuture = task;
        }

        public void runTask() {
            try {
                mMFAsyncTaskFuture.run();
            } catch (OutOfMemoryError oom) {
                // onAppMemoryLow();
//				MFBaseApplication.getInst().onAppMemoryLow();
            }
        }

        public void cancelTask() {
            mMFAsyncTaskFuture.cancelTask();
        }

        public boolean isCancelled() {
            return mMFAsyncTaskFuture.isCancelled();
        }

        public MFAsyncTask<?, ?, ?> getTask() {
            return mMFAsyncTaskFuture.getTask();
        }

        public int getPriority() {
            return mMFAsyncTaskFuture.getTask().getPriority();
        }

        /**
         * 設置是否爲超時
         *
         * @param isTimeout
         */
        public void setTimeout(boolean isTimeout) {
            mMFAsyncTaskFuture.getTask().setTimeout(isTimeout);
        }

        public boolean IsTimeout() {
            return mMFAsyncTaskFuture.getTask().isTimeout();
        }

        public int getTag() {
            return mMFAsyncTaskFuture.getTask().getTag();
        }

        public int getParallelTag() {
            if (mMFAsyncTaskFuture.getTask().getParallel() != null) {
                return mMFAsyncTaskFuture.getTask().getParallel().getTag();
            } else {
                return 0;
            }
        }

        public String getKey() {
            return mMFAsyncTaskFuture.getTask().getKey();
        }

        public MFAsyncTaskParallel.AsyncTaskParallelType getParallelType() {
            if (mMFAsyncTaskFuture.getTask().getParallel() != null) {
                return mMFAsyncTaskFuture.getTask().getParallel().getType();
            } else {
                return MFAsyncTaskParallel.AsyncTaskParallelType.MAX_PARALLEL;
            }
        }

        public int getExcuteNum() {
            if (mMFAsyncTaskFuture.getTask().getParallel() != null) {
                return mMFAsyncTaskFuture.getTask().getParallel().getExecuteNum();
            } else {
                return 1;
            }
        }

        public boolean isSelfExecute() {
            return mMFAsyncTaskFuture.getTask().isSelfExecute();
        }
    }

}
