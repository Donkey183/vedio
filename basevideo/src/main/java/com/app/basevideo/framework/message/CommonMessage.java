package com.app.basevideo.framework.message;

/**
 * 自定义responsedMessage，开发者在开发需要自定义业务时，自己的响应消息需要继承此类
 *
 * @param <T>
 */
public class CommonMessage<T> extends AbsResponsedMessage<T> {
    private T mData = null;

    /**
     * 构造函数
     *
     * @param cmd
     */
    public CommonMessage(int cmd) {
        super(cmd);
    }

    /**
     * 构造函数
     *
     * @param cmd
     * @param data
     */
    public CommonMessage(int cmd, T data) {
        super(cmd);
        mData = data;
    }

    /**
     * 获取数据
     *
     * @return
     */
    public T getData() {
        return mData;
    }

}
