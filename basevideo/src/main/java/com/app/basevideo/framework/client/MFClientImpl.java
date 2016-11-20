package com.app.basevideo.framework.client;

import com.app.basevideo.framework.UniqueId;
import com.app.basevideo.framework.async.MFAsyncTask;
import com.app.basevideo.framework.async.MFAsyncTaskPriority;
import com.app.basevideo.framework.manager.MessageManager;
import com.app.basevideo.framework.message.AbsResponsedMessage;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.framework.message.TaskMessage;
import com.app.basevideo.framework.task.MessageTask;
import com.app.basevideo.framework.util.LogUtil;

import java.util.LinkedList;

/**
 * 自定义消息处理实现类
 */
@SuppressWarnings("rawtypes")
public class MFClientImpl extends MFClient<TaskMessage<?>, MessageTask> {

    public MFClientImpl(MessageManager manager) {
        super(manager);
    }

    /**
     * 发送自定义消息，同步的task从task中取得runnable并执行run方法，将执行结果返回并进行分发
     * 异步task则新建异步任务交由线程池管理，执行的返回结果在{@link CustomMFAsyncTask#onPostExecute}方法中分发
     *
     * @param taskMessage
     * @param task
     */
    @SuppressWarnings("unchecked")
    @Override
    public void sendMessage(TaskMessage taskMessage, MessageTask task) {
        if (taskMessage == null || task == null) {
            return;
        }
        if (task.getType() == MessageTask.TASK_TYPE.SYNCHRONIZED) {
            MessageTask.CustomRunnable runnable = task.getRunnable();
            AbsResponsedMessage<?> rsp = null;
            try {
                rsp = runnable.run(taskMessage);
                if (rsp != null) {
                    rsp.setOrginalMessage(taskMessage);
                }
            } catch (Exception e) {
                LogUtil.detailException(e);
            }
            if (rsp != null) {
                mMessageManager.dispatchResponsedMessage(rsp);
            }
        } else {
            CustomMFAsyncTask mTask = new CustomMFAsyncTask(taskMessage, task);
            mTask.execute();
        }
    }

    public <T> CommonMessage<T> runTask(MessageTask task, Class<T> cls) {
        return runTask(null, task, cls);
    }

    @SuppressWarnings("unchecked")
    public <T> CommonMessage<T> runTask(TaskMessage taskMessage, MessageTask task, Class<T> cls) {
        if (task == null) {
            return null;
        }
        CommonMessage<T> rsp = null;
        if (task.getType() == MessageTask.TASK_TYPE.SYNCHRONIZED) {
            MessageTask.CustomRunnable runnable = task.getRunnable();
            try {
                rsp = runnable.run(taskMessage);
            } catch (Exception e) {
                LogUtil.detailException(e);
            }
            if (rsp != null) {
                mMessageManager.dispatchResponsedMessage(rsp);
            }
        } else {
            CustomMFAsyncTask mTask = new CustomMFAsyncTask(taskMessage, task);
            mTask.execute();
        }
        return rsp;
    }

    private class CustomMFAsyncTask extends MFAsyncTask<String, String, CommonMessage<?>> {
        private TaskMessage mRequestMsg = null;
        private MessageTask mMessageTask = null;

        public TaskMessage getMessage() {
            return mRequestMsg;
        }

        public CustomMFAsyncTask(TaskMessage msg, MessageTask task) {
            setPriority(task.getPriority());
            setParallel(task.getParallel());
            setTag(msg.getTag());
            setKey(String.valueOf(task.getCmd()));
            setParallel(task.getTaskParallel());
            if (task.isImme()) {
                setPriority(MFAsyncTaskPriority.SUPER_HIGH);
            }
            mRequestMsg = msg;
            mMessageTask = task;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected CommonMessage doInBackground(String... params) {
            if (mMessageTask == null) {
                return null;
            }
            if (mMessageTask.getRunnable() == null) {
                LogUtil.e("CustomTask :" + mMessageTask.getClass().getName() + "did not contain a runnable!!");
                return null;
            }
            // 线程安全每个task自行处理
            try {
                return mMessageTask.getRunnable().run(mRequestMsg);
            } catch (Exception e) {
                LogUtil.detailException(e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(CommonMessage<?> result) {
            if (result != null) {
                result.setOrginalMessage(mRequestMsg);
                mMessageManager.dispatchResponsedMessage(result);
            } else {
                LogUtil.e("CustomTask :" + mMessageTask.getClass().getName() + "returns a NULL!!");
            }
        }
    }

    @Override
    public void removeMessage(UniqueId tag) {
        removeMessage(0, tag);
    }

    @Override
    public void removeMessage(int cmd, UniqueId tag) {
        String key = null;
        if (cmd != 0) {
            key = String.valueOf(cmd);
        }
        CustomMFAsyncTask.removeAllTask(tag, key);
    }

    @Override
    public void removeWaitingMessage(UniqueId tag) {
        removeWaitingMessage(0, tag);
    }

    @Override
    public void removeWaitingMessage(int cmd, UniqueId tag) {
        String key = null;
        if (cmd != 0) {
            key = String.valueOf(cmd);
        }
        CustomMFAsyncTask.removeAllWaitingTask(tag, key);
    }

    @Override
    public LinkedList<TaskMessage<?>> findMessage(UniqueId tag) {
        return findMessage(0, tag);
    }

    @Override
    public LinkedList<TaskMessage<?>> findMessage(int cmd, UniqueId tag) {
        String key = null;
        if (cmd != 0) {
            key = String.valueOf(cmd);
        }
        LinkedList<MFAsyncTask<?, ?, ?>> list = MFAsyncTask.searchAllTask(tag, key);
        LinkedList<TaskMessage<?>> result = new LinkedList<TaskMessage<?>>();
        for (MFAsyncTask<?, ?, ?> task : list) {
            if (task instanceof CustomMFAsyncTask) {
                result.add(((CustomMFAsyncTask) task).getMessage());
            }
        }
        return result;
    }

    @Override
    public boolean haveMessage(UniqueId tag) {
        return getMessageNum(tag) > 0;
    }

    @Override
    public boolean haveMessage(int cmd, UniqueId tag) {
        return getMessageNum(cmd, tag) > 0;
    }

    @Override
    public int getMessageNum(UniqueId tag) {
        return getMessageNum(0, tag);
    }

    @Override
    public int getMessageNum(int cmd, UniqueId tag) {
        String key = null;
        if (cmd != 0) {
            key = String.valueOf(cmd);
        }
        return CustomMFAsyncTask.getTaskNum(key, tag);
    }

}
