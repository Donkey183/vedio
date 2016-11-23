package com.app.basevideo.net;

public class BaseHttpResult<T> {

    /**
     * 错误码
     */
    public int errno = -1;

    /**
     * 错误码
     */
    public int errorCode = -1;

    /**
     * 错误原因
     */
    public String msg;


    /**
     * 错误原因
     */
    public String errorMsg;


    /**
     * 返回数据对象
     */
    public T data;


    /**
     * 返回数据对象
     */
    public T result;


    /**
     * 返回数据对象
     */
    public T list;

    /**
     * 是否成功
     */
    public boolean success;

}
