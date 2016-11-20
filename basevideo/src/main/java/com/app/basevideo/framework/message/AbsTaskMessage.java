package com.app.basevideo.framework.message;

import com.app.basevideo.framework.UniqueId;

import java.io.Serializable;
import java.security.InvalidParameterException;


/**
 * 消息抽象类，定义了cmd和tag，和性能统计数据字段
 * <p/>
 * <p>
 * 构造函数时必须指定命令号，且内部会根据不同的业务类型对命令号区间做校验<br>
 * </p>
 *
 * @param <T>
 */
public abstract class AbsTaskMessage<T> implements Serializable, Cloneable {
    private final int mCmd;
    private UniqueId mTag;
    private Object mExtra = null;

    /**
     * 构造方法
     *
     * @param cmd
     */
    public AbsTaskMessage(int cmd) {
        mCmd = cmd;
        check();
    }

    /**
     * 构造方法
     *
     * @param cmd
     * @param tag
     */
    public AbsTaskMessage(int cmd, UniqueId tag) {
        mCmd = cmd;
        mTag = tag;
        check();
    }

    /**
     * 检查cmd合法性
     */
    private void check() {
        if (checkCmd(mCmd) == false) {
            throw new InvalidParameterException("cmd invalid");
        }
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
        mTag = tag;
    }

    /**
     * 检查cmd合法性接口
     *
     * @param cmd
     * @return
     */
    public abstract boolean checkCmd(int cmd);

    /**
     * 获取扩展参数
     *
     * @return
     */
    public Object getExtra() {
        return mExtra;
    }

    /**
     * 设置扩展参数
     *
     * @param extra
     */
    public void setExtra(Object extra) {
        this.mExtra = extra;
    }

    @Override
    public String toString() {
        return "AbsTaskMessage";
    }

    @Override
    public AbsTaskMessage clone() {
        AbsTaskMessage obj = null;
        try {
            obj = (AbsTaskMessage) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
