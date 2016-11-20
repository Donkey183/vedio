package com.app.basevideo.framework.task;


import com.app.basevideo.framework.async.MFAsyncTaskParallel;

import java.security.InvalidParameterException;


/**
 * 任务抽象类
 * <p/>
 * <p>
 * 任务：就是要处理的一个事情，按业务类型分为http，socket，自定义三种任务。<br>
 * 任务和一个命令号关联，通用属性包括超时，重试次数，优先级，任务并行度，是否加密等配置，和任务处理函数（大多是runnalbe接口）
 * </p>
 */
public abstract class AbsMessageTask {
    protected final int mCmd;
    protected int mRetry;
    protected int mPriority;
    private MFAsyncTaskParallel mParallel = null;
    protected boolean mNeedEncrypt = true;// 默认为加密

    /**
     * 构造方法，任务必须设置cmd，不能改变
     *
     * @param cmd
     */
    public AbsMessageTask(int cmd) {
        mCmd = cmd;
        check();
    }

    /**
     * 检查命令号是否合法，即是否在当前业务类型命令号区间
     */
    private void check() {
        if (checkCmd() == false) {
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
     * 获取优先级
     *
     * @return
     */
    public int getPriority() {
        return mPriority;
    }

    /**
     * 设置优先级
     *
     * @param priority
     */
    public void setPriority(int priority) {
        this.mPriority = priority;
    }

    /**
     * 获取任务并行度
     *
     * @return
     */
    public MFAsyncTaskParallel getParallel() {
        return mParallel;
    }

    /**
     * 设置任务并行度
     *
     * @param parallel
     */
    public void setParallel(MFAsyncTaskParallel parallel) {
        mParallel = parallel;
    }


    /**
     * cmd检查接口
     *
     * @return
     */
    public abstract boolean checkCmd();
}
