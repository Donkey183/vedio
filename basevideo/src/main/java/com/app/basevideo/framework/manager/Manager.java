package com.app.basevideo.framework.manager;

import android.util.SparseArray;
import android.util.SparseIntArray;

import com.app.basevideo.framework.IFunction;
import com.app.basevideo.framework.UniqueId;
import com.app.basevideo.framework.client.MFClient;
import com.app.basevideo.framework.listener.AbsMessageListener;
import com.app.basevideo.framework.listener.TaskNotFoundListener;
import com.app.basevideo.framework.message.AbsResponsedMessage;
import com.app.basevideo.framework.message.AbsTaskMessage;
import com.app.basevideo.framework.task.AbsMessageTask;
import com.app.basevideo.framework.util.LogUtil;
import com.app.basevideo.framework.util.MessageHelper;
import com.app.basevideo.framework.util.ThreadHelper;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 管理器抽象类，主要职责
 * <p/>
 * <ul>
 * <li>完成对{@link MFClient}的封装，消息的增删查找等操作通过{@link MFClient}做中转
 * <li>{@link AbsMessageListener}增删管理
 * <li>{@link AbsMessageTask}的管理
 * <li>消息分发操作
 * </ul>
 *
 * @param <M>
 * @param <T>
 * @param <N>
 */

public abstract class Manager<M extends AbsTaskMessage<?>, T extends AbsMessageTask, N extends AbsResponsedMessage<?>>
        implements
        IFunction<M, T> {
    protected MessageManager mMessageManager = null;
    private final SparseArray<T> mTasks = new SparseArray<T>();
    private final SparseArray<N> mStickyResponsedMessage = new SparseArray<N>();
    private final SparseArray<LinkedList<AbsMessageListener<N>>> mListeners = new SparseArray<LinkedList<AbsMessageListener<N>>>();
    private boolean mAborted = false;
    private final SparseIntArray mListenerLocked = new SparseIntArray();

    private static TaskNotFoundListener<AbsTaskMessage<?>> mTaskNotFoundListener = null;

    /**
     * 构造方法，强制传入消息中心管理器
     *
     * @param manager
     */
    public Manager(MessageManager manager) {
        mMessageManager = manager;
    }

    /**
     * 获取消息管理器
     *
     * @return
     */
    public MessageManager getMessageManager() {
        return mMessageManager;
    }

    /**
     * 注册任务
     * <p/>
     * <p>
     * 底层是个hashmap，同一个命令号的任务以最后一次注册的任务会覆盖以前注册的
     * </p>
     *
     * @param task
     */
    public synchronized void registerTask(T task) {
        if (task == null) {
            return;
        }
        int cmd = task.getCmd();
        mTasks.put(cmd, task);
    }

    /**
     * 注销任务
     *
     * @param cmd
     */
    public synchronized void unRegisterTask(int cmd) {
        mTasks.remove(cmd);
    }

    /**
     * 查找任务
     *
     * @param cmd
     * @return
     */
    public synchronized T findTask(int cmd) {
        return mTasks.get(cmd);
    }

    /**
     * 查找全部任务
     * <p/>
     * <p>
     * 外层拿到的是一个新的集合，即使开发者对返回的数组做增删操作，不会影响底层数据集合
     * </p>
     *
     * @return
     */
    public synchronized ArrayList<T> findTasks() {
        ArrayList<T> tasks = new ArrayList<T>();
        int size = mTasks.size();
        for (int i = 0; i < size; i++) {
            T tmp = mTasks.valueAt(i);
            tasks.add(tmp);
        }
        return tasks;
    }

    /**
     * 检查监听注册注销是否被锁
     *
     * @see #lockListener(int)
     * @see #unLockListener(int)
     */
    private void checkListenerLock(int cmd) {
        if (isListenerLocked(cmd) == true) {
            throw new IllegalStateException("AbsMessageListener locked");
        }
    }

    /**
     * 注册监听
     * <p/>
     * <p>
     * <b><font color=red>只允许主线程调用</font><br>
     * <b><font color=red>不能在同一个命令号的消息回调里面执行注册监听操作</font>
     * </p>
     * <p/>
     * <p>
     * 命令号和listener中指定的命令号不能同时为0，或都不为0，要求一个为0，一个不能为0
     * </p>
     *
     * @param cmd      命令号
     * @param listener 监听器
     */
    public void registerListener(int cmd, AbsMessageListener<N> listener) {
        ThreadHelper.checkMainThread();
        if (null == listener) {
            return;
        }
        if ((cmd == 0 && listener.getCmd() == 0) || (cmd != 0 && listener.getCmd() != 0)) {
            throw new InvalidParameterException("registerListener cmd error");
        }
        if (cmd == 0) {
            cmd = listener.getCmd();
        }
        /**
         * 注意此处检测是否加锁
         */
        checkListenerLock(cmd);
        LinkedList<AbsMessageListener<N>> list = mListeners.get(cmd);
        if (list == null) {
            list = new LinkedList<AbsMessageListener<N>>();
            mListeners.put(cmd, list);
        }
        /**
         * 按优先级插入监听器队列
         */
        MessageHelper.insert(list, listener);
        N message = null;
        /**
         * 如果是sticky模式的监听器，则在注册的时候直接触发一次监听响应
         */
        if ((message = mStickyResponsedMessage.get(cmd)) != null) {
            listener.onMessage(message);
        }
    }

    /**
     * 注销监听
     * <p/>
     * <p>
     * <b><font color=red>只允许主线程调用</font><br>
     * <b><font color=red>不能在同一个命令号的消息回调里面执行注销监听操作</font>
     * </p>
     *
     * @param listener 要被注销的监听器
     * @see #unRegisterListener(UniqueId)
     */
    public void unRegisterListener(AbsMessageListener<?> listener) {
        ThreadHelper.checkMainThread();
        if (listener == null) {
            return;
        }
        int cmd = listener.getCmd();
        if (cmd == 0) {
            int size = mListeners.size();
            for (int i = 0; i < size; i++) {
                LinkedList<AbsMessageListener<N>> listeners = mListeners.valueAt(i);
                int key = mListeners.keyAt(i);
                if (listeners.contains(listener)) {
                    checkListenerLock(key);
                    listeners.remove(listener);
                }
            }
        } else {
            checkListenerLock(cmd);
            LinkedList<AbsMessageListener<N>> list = mListeners.get(cmd);
            if (list != null) {
                list.remove(listener);
            }
        }
    }

    /**
     * 按tag注销监听
     * <p/>
     * <p>
     * <b><font color=red>只允许主线程调用</font><br>
     * <b><font color=red>不能在同一个命令号的消息回调里面执行注销监听操作</font>
     * </p>
     *
     * @param tag 同一个类型的标记，由开发者指定
     * @see #unRegisterListener(AbsMessageListener)
     */
    public void unRegisterListener(UniqueId tag) {
        ThreadHelper.checkMainThread();
        if (tag == null) {
            return;
        }
        int size = mListeners.size();
        for (int i = 0; i < size; i++) {
            LinkedList<AbsMessageListener<N>> listeners = mListeners.valueAt(i);
            int cmd = mListeners.keyAt(i);
            Iterator<AbsMessageListener<N>> iterator = listeners.iterator();
            while (iterator.hasNext()) {
                AbsMessageListener<N> listener = iterator.next();
                if (listener != null && listener.getTag() == tag) {
                    checkListenerLock(cmd);
                    iterator.remove();
                }
            }
        }
    }

    /**
     * @param message
     * @param task    可以为空，如果为空，则根据消息命令号查找对应的执行任务，如果任务不为空，之前已经注册过消息命令号的任务，
     *                则此时执行参数task，不执行已经注册的任务
     * @see #sendMessage(AbsTaskMessage, AbsMessageTask)
     * @see #setNotFindTaskListener(TaskNotFoundListener)
     */
    public boolean dispatchMessage(M message, T task) {
        ThreadHelper.checkMainThread();
        if (message == null) {
            return false;
        }
        int cmd = message.getCmd();
        if (task == null) {
            task = findTask(cmd);
        }
        if (task != null) {
            if (message != null) {
                sendMessage(message, task);
                return true;
            } else {
                LogUtil.d("message is trapped:" + cmd);
                return false;
            }
        } else {
            if (mTaskNotFoundListener != null) {
                mTaskNotFoundListener.onNotFindTask(message);
            }
            LogUtil.e("task not register:" + cmd);
            return false;
        }
    }

    /**
     * 注册sticky模式
     *
     * @param cmd 命令号
     */
    public void registerStickyMode(int cmd) {
        if (mStickyResponsedMessage.indexOfKey(cmd) < 0) {
            mStickyResponsedMessage.put(cmd, null);
        }
    }

    /**
     * 注销sticky模式
     *
     * @param cmd 命令号
     */
    public void unRegisterStickyMode(int cmd) {
        mStickyResponsedMessage.remove(cmd);
    }

    /**
     * 终止消息传递，多数可以在某个消息监听器里面调用，终止消息传到其他消息监听器，往往需要和监听器优先级配合使用
     */
    public void abort() {
        mAborted = true;
    }

    /**
     * 分发Responsed消息，响应注册的消息监听器
     *
     * @param responsedMessage 响应消息
     */
    public void dispatchResponsedMessage(N responsedMessage) {
        ThreadHelper.checkMainThread();
        if (responsedMessage == null) {
            return;
        }
        int cmd = responsedMessage.getCmd();
        UniqueId tag = null;
        AbsTaskMessage<?> original = responsedMessage.getOrginalMessage();
        if (original != null) {
            tag = original.getTag();
        }

        /**
         * 对应的命令号，如果是Sticky模式消息，一直以最后一次分发的消息为准
         */
        if (mStickyResponsedMessage.indexOfKey(cmd) >= 0) {
            mStickyResponsedMessage.put(cmd, responsedMessage);
        }
        LinkedList<AbsMessageListener<N>> listeners = mListeners.get(cmd);
        if (listeners == null) {
            return;
        }

        mAborted = false;
        /**
         * 注意此处加锁，只针对当前派发的命令号加锁（做过优化，以前是对所有监听器集合加锁），否则内部会回调监听器处理，
         * 开发者如果在回调处理里面又注册了同一个命令号的监听器或注销了一个监听器
         * ，则框架会抛出异常，原因是当前的监听器集合正在遍历，不同的业务场景需求不同，可能需要立即执行新的监听器
         * ，也可能不执行，如果让开发者指定，也会变的复杂
         */
        lockListener(cmd);
        try {
            Iterator<AbsMessageListener<N>> iterator = listeners.iterator();
            while (iterator.hasNext() && mAborted == false) {
                AbsMessageListener<N> listener = iterator.next();
                if (listener != null) {
                    /**
                     * 对于self的监听器，会继续判断tag是否相等，具体逻辑请参考
                     * {@link AbsMessageListener#setSelfListener}逻辑
                     */
                    if (listener.isSelfListener() == false || (listener.getTag() == tag)) {
                        try {
                            listener.onMessage(responsedMessage);
                        } catch (Exception e) {
                            LogUtil.detailException(responsedMessage.getClass().getName(), e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.detailException(responsedMessage.getClass().getName(), e);
        } finally {
            unLockListener(cmd);
        }
    }

    /**
     * 锁定监听，针对单个命令号的
     *
     * @param cmd 命令号
     */
    private synchronized void lockListener(int cmd) {
        mListenerLocked.put(cmd, mListenerLocked.get(cmd, 0) + 1);
    }

    /**
     * 解锁监听，针对单个命令号的
     *
     * @param cmd 命令号
     */
    private synchronized void unLockListener(int cmd) {
        int num = mListenerLocked.get(cmd, 0);
        if (num <= 1) {
            mListenerLocked.delete(cmd);
        } else {
            mListenerLocked.put(cmd, num - 1);
        }
    }

    /**
     * 判断监听是否被锁定
     *
     * @param cmd 命令号
     * @return
     */
    private synchronized boolean isListenerLocked(int cmd) {
        return mListenerLocked.get(cmd, 0) != 0;
    }

    /**
     * 设置找不到task时的回调
     * <p/>
     * <o> 场景：开放者可以在回调里面打印日志或动态去加载任务所在模块 </p>
     *
     * @param taskNotFoundListener
     */
    public static void setNotFindTaskListener(TaskNotFoundListener<AbsTaskMessage<?>> taskNotFoundListener) {
        mTaskNotFoundListener = taskNotFoundListener;
    }
}