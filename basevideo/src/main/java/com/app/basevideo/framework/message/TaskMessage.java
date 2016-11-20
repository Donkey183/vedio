package com.app.basevideo.framework.message;


import com.app.basevideo.framework.UniqueId;
import com.app.basevideo.framework.util.MessageHelper;


/**
 * 自定义消息，开发者在开发自定义业务时，自己的消息需要继承此类
 * <p/>
 * <p>
 * 事例：<br>
 * <p/>
 * <pre>
 * public class XXXMessage extends TaskMessage&lt;String&gt; {
 *
 * 	public XXXMessage() {
 * 		super(命令号);
 *    }
 * }
 *
 * XXXMessage message = new XXXMessage();
 * message.setData(&quot;testData&quot;);
 * MessageManager.getInstance().sendMessage(message);
 *
 * </pre>
 * <p/>
 * </p>
 *
 * @param <T>
 */
public class TaskMessage<T> extends AbsTaskMessage<T> {

    private T mData = null;

    /**
     * 构造方法
     *
     * @param cmd
     */
    public TaskMessage(int cmd) {
        super(cmd);
    }

    /**
     * 构造方法
     *
     * @param cmd
     * @param data
     */
    public TaskMessage(int cmd, T data) {
        super(cmd);
        mData = data;
    }

    /**
     * 构造方法
     *
     * @param cmd
     * @param tag
     */
    public TaskMessage(int cmd, UniqueId tag) {
        super(cmd, tag);
    }

    /**
     * 设置自定义数据
     *
     * @param data
     */
    public void setData(T data) {
        this.mData = data;
    }

    /**
     * 获取自定义数据
     *
     * @return
     */
    public T getData() {
        return mData;
    }

    /**
     * 检查cmd合法性
     */
    @Override
    public boolean checkCmd(int cmd) {
        return MessageHelper.checkCustomCmd(cmd);
    }
}
