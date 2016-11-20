package com.app.basevideo.framework.listener;

import com.app.basevideo.framework.Priority;
import com.app.basevideo.framework.UniqueId;
import com.app.basevideo.framework.message.AbsResponsedMessage;

/**
 * 监听抽象类
 *
 * @param <T>
 */
public abstract class AbsMessageListener<T extends AbsResponsedMessage<?>> extends Priority {
    private int mCmd = 0;
    private UniqueId mTag = null;

    private boolean mSelfListener = false;

    /**
     * 构造函数
     *
     * @param cmd
     */
    public AbsMessageListener(int cmd) {
        mCmd = cmd;
    }

    /**
     * 构造函数
     *
     * @param cmd
     * @param isSelfListener
     */
    public AbsMessageListener(int cmd, boolean isSelfListener) {
        mCmd = cmd;
        mSelfListener = isSelfListener;
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
     * 获取tag
     *
     * @return
     */
    public UniqueId getTag() {
        return mTag;
    }

    /**
     * 设置tag
     *
     * @param tag
     */
    public void setTag(UniqueId tag) {
        this.mTag = tag;
    }

    /**
     * 回调接口
     *
     * @param responsedMessage
     */
    public abstract void onMessage(T responsedMessage);

    /**
     * 是否自监听
     *
     * @return
     */
    public boolean isSelfListener() {
        return mSelfListener;
    }

    /**
     * 设置是否自监听，需要和tag一起使用
     * <p/>
     * <p>
     * 使用场景描述，如XXActivity可能会有多个实例，每个实例都会发起一个网络请求，那么开发者为这个请求消息定义一个命令号，发起消息请求网络，
     * 同时实现这个命令号的监听器
     * ，如果多个XXActivity实例发起的请求参数不同，即获取到不同的网络数据，那么所有XXActivity的监听都拿到返回消息刷新自己的ui
     * ，就乱掉了
     * ，有一种变通方法，可以拿到响应消息后，从响应消息获取原始请求数据，和自己发起的请求消息相比，是否为同一个消息，如果是则刷新ui，否则不处理
     * 。这样比较麻烦，开发者可能会忘记在响应消息里面做这个比较。{@link #setSelfListener(boolean)}
     * 就是为解决这个问题，开放者有此需求时只需设置为true，同时需要设定tag和发消息tag一样即可。
     * </p>
     *
     * @param mSelfListener 是否监听自己页面
     * @see #setTag(UniqueId)
     */
    public void setSelfListener(boolean mSelfListener) {
        this.mSelfListener = mSelfListener;
    }

}
