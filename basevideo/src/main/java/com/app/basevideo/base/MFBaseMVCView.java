package com.app.basevideo.base;


import com.app.basevideo.framework.UniqueId;
import com.app.basevideo.framework.listener.AbsMessageListener;
import com.app.basevideo.framework.listener.MessageListener;
import com.app.basevideo.framework.manager.MessageManager;
import com.app.basevideo.framework.util.LogUtil;

public abstract class MFBaseMVCView<T extends MFBaseActivity> {

    /**
     * 分配的unique id
     */
    protected UniqueId mUniqueId = null;

    public MFBaseMVCView() {

    }

    public MFBaseMVCView(MFBaseActivity activityContext) {
        if (activityContext == null) {
            LogUtil.e("activityContext is null");
            return;
        }
        activityContext.setContentView(getLayoutRecourseId());
        mUniqueId = UniqueId.gen();
    }

    protected abstract int getLayoutRecourseId();

    protected abstract void onDestroy();

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
        if (listener != null && listener.getTag() == null) {
            listener.setTag(mUniqueId);
        }
        MessageManager.getInstance().registerListener(cmd, listener);
    }


    public void removeListener() {
        MessageManager.getInstance().unRegisterListener(mUniqueId);
        MessageManager.getInstance().removeMessage(mUniqueId);
    }
}
