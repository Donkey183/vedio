package com.app.basevideo.base;


import com.app.basevideo.config.VideoErrorData;
import com.app.basevideo.framework.UniqueId;
import com.app.basevideo.framework.listener.AbsMessageListener;
import com.app.basevideo.framework.listener.MessageListener;
import com.app.basevideo.framework.manager.MessageManager;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.framework.message.TaskMessage;
import com.app.basevideo.framework.util.LogUtil;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.util.MessageConfig;


public abstract class MFBaseFragmentModel {

    public static final int MODE_INVALID = 0;

    /**
     * 操作标记
     */
    protected int mLoadDataMode = MODE_INVALID;

    /**
     * 分配的unique id
     */
    protected UniqueId mUniqueId = null;

    /**
     * 使用BaseModel(Context)，context传入Activity(该Activity必须继承自{@link MFBaseActivity})
     * 在BaseModel中绑定Activity页面唯一标识mUniqueId
     */
    public MFBaseFragmentModel(MFBaseFragment activityContext) {
        if (activityContext == null) {
            return;
        }
        this.mUniqueId = activityContext.getUniqueId();
    }


    /**
     * 得到操作标记
     *
     * @return
     */
    public int getLoadDataMode() {
        return mLoadDataMode;
    }


    /**
     * 设置页面唯一id
     *
     * @param id
     */
    public void setUniqueId(UniqueId id) {
        this.mUniqueId = id;
    }

    /**
     * 获得页面唯一id
     *
     * @return
     */
    public UniqueId getUniqueId() {
        return mUniqueId;
    }

    /**
     * 发送消息，不支持的消息类型无法发送，调试模式打印error日志
     * <p/>
     * <p>
     * <b><font color=red>只允许主线程调用</font>
     * </p>
     *
     * @param taskMessage 只允许是{@link TaskMessage}
     */
    public void sendMessage(TaskMessage<?> taskMessage) {
        if (taskMessage == null) {
            return;
        }
        if (taskMessage.getTag() == null) {
            taskMessage.setTag(mUniqueId);
        }
        MessageManager.getInstance().sendMessage(taskMessage);
    }


    /**
     * 取消已经发送过的Message，依赖于当期的页面id：{@link #mUniqueId}
     *
     * @see TaskMessage
     * @see MessageManager#removeMessage(UniqueId)
     */
    public void cancelMessage() {
        check();
        MessageManager.getInstance().removeMessage(mUniqueId);
    }

    /**
     * 注册responsedMessage的事件监听
     * <p/>
     * <p>
     * <b><font color=red>禁止在事件回调里面调用此方法，否则抛异常</font><br>
     * <b><font color=red>只允许主线程调用</font>
     * </p>
     * <p>
     * 注册的消息监听如果没有全局性需求，需要使用者调用{@link MessageManager#unRegisterListener}来注销监听<br>
     * 场景：比如一个activity注册了一个自己发起的http任务返回的消息监听，页面销毁时需要注销此消息监听，否则会<b><font
     * color=red>引起内存泄露</font>
     * </p>
     *
     * @param listener 监听器
     * @see #registerListener(int, MessageListener)
     * @see MessageManager#unRegisterListener(AbsMessageListener)
     */
    public void registerListener(MessageListener listener) {
        check();
        if (listener != null && listener.getTag() == null) {
            listener.setTag(mUniqueId);
        }
        MessageManager.getInstance().registerListener(listener);
    }

    /**
     * 注册事件监听，带有命令号和监听器注册监听重载
     * <p/>
     * <p>
     * <b><font color=red>禁止在事件回调里面调用此方法，否则抛异常</font><br>
     * <b><font color=red>只允许主线程调用</font>
     * </p>
     *
     * @param cmd      不能是零
     * @param listener listener中的域cmd必须是0
     * @see #registerListener(MessageListener)
     * @see MessageManager#unRegisterListener(AbsMessageListener)
     */
    public void registerListener(int cmd, MessageListener listener) {
        check();
        if (listener != null && listener.getTag() == null) {
            listener.setTag(mUniqueId);
        }
        MessageManager.getInstance().registerListener(cmd, listener);
    }


    private void check() {
        if (mUniqueId == null) {
            LogUtil.e(getClass().getName() + "'s mUniqueId wasn't seted!");
        }
    }

    /**
     * 发送网络请求
     */
    protected abstract void sendHttpRequest(CommonHttpRequest request, int requestCode);

    /**
     * 加载已缓存的数据填充Model
     *
     * @return
     */
    public abstract boolean loadModelDataFromCache();

    /**
     * 分发网络请求数据到{@link MFBaseActivity#registerNetResponsedListener()}
     * 由{@link MFBaseActivity}回调数据给发起请求的的Activity
     * 在每个具体的Model中调用{@link MFBaseFragmentModel#disPatchRequestSuccessMessage(Integer)}方法即可在发起请求的Activity收到请求数据
     */
    public void disPatchRequestSuccessMessage(Integer requestCode) {
        if (mUniqueId == null) {
            LogUtil.e("mUniqueId is null, please initialize your model on activity's onCreate() method ");
            return;
        }
        MessageManager.getInstance().dispatchResponsedMessage(new CommonMessage<Integer>(mUniqueId.getId(), requestCode));
    }


    public void disPatchNetErrorMessage(int errorCode, String errorMessage) {
        VideoErrorData error = new VideoErrorData();
        error.setErrorCode(errorCode);
        error.setErrorMessage(errorMessage);
        MessageManager.getInstance().dispatchResponsedMessage(new CommonMessage<VideoErrorData>(MessageConfig.CMD_NET_ERROR, error));
    }

}

