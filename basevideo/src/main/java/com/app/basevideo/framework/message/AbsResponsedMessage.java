package com.app.basevideo.framework.message;


/**
 * 响应消息基类
 *
 * @param <T>
 */
public abstract class AbsResponsedMessage<T> {
    private final int mCmd;
    private AbsTaskMessage<?> mOrginalAbsTaskMessage;

    /**
     * 构造函数
     *
     * @param cmd
     */
    public AbsResponsedMessage(int cmd) {
        mCmd = cmd;
    }

    /**
     * 获取cmd
     *
     * @return
     */
    public int getCmd() {
        return mCmd;
    }

    /**
     * 获取源消息
     *
     * @return
     */
    public AbsTaskMessage<?> getOrginalMessage() {
        return mOrginalAbsTaskMessage;
    }

    /**
     * 设置源消息
     *
     * @param orginalAbsTaskMessage
     */
    public void setOrginalMessage(AbsTaskMessage<?> orginalAbsTaskMessage) {
        this.mOrginalAbsTaskMessage = orginalAbsTaskMessage;
    }


}
