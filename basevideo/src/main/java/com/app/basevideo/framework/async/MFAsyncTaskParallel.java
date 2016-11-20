package com.app.basevideo.framework.async;

import com.app.basevideo.framework.UniqueId;

import java.security.InvalidParameterException;


/**
 * 异步任务的并行度类
 * <ul>
 * <li>串行 <code>SERIAL</code></li>
 * <li>两个任务并行 <code>TWO_PARALLEL</code></li>
 * <li>三个任务并行 <code>THREE_PARALLEL</code></li>
 * <li>四个任务并行 <code>FOUR_PARALLEL</code></li>
 * <li>自定义个数 <code>CUSTOM_PARALLEL</code>
 * </li>
 * <li>最大量并行 <code>MAX_PARALLEL</code></li>
 * </ul>
 */

public class MFAsyncTaskParallel {

    /**
     * 并行类型枚举
     */
    public static enum AsyncTaskParallelType {
        SERIAL, // 串行
        TWO_PARALLEL, // 两个任务并行
        THREE_PARALLEL, // 三个任务并行
        FOUR_PARALLEL, // 四个任务并行
        CUSTOM_PARALLEL, // 自定义个数，需要配置num参数，如果不配置，为串行
        MAX_PARALLEL// 多个任务并行
    }

    private UniqueId mBdAsyncTaskParallelTag = null;
    private AsyncTaskParallelType mAsyncTaskParallelType = AsyncTaskParallelType.MAX_PARALLEL;
    private int mExecuteNum = 1;

    /**
     * 构造函数
     *
     * @param type 类型, 参见{@link # AsyncTaskParallelType}
     * @param tag  唯一标识符
     */
    public MFAsyncTaskParallel(AsyncTaskParallelType type, UniqueId tag) {
        if (type == null || tag == null) {
            throw new InvalidParameterException("MFAsyncTaskParallel parameter null");
        }
        mAsyncTaskParallelType = type;
        mBdAsyncTaskParallelTag = tag;
    }

    /**
     * 构造函数
     *
     * @param tag        唯一标识符
     * @param executeNum 并行执行的数目
     */
    public MFAsyncTaskParallel(UniqueId tag, int executeNum) {
        if (tag == null) {
            throw new InvalidParameterException("MFAsyncTaskParallel parameter null");
        }
        mAsyncTaskParallelType = AsyncTaskParallelType.CUSTOM_PARALLEL;
        mExecuteNum = executeNum;
        mBdAsyncTaskParallelTag = tag;
    }

    /**
     * 获取并行数
     *
     * @return
     */
    public int getExecuteNum() {
        return mExecuteNum;
    }

    /**
     * 获取唯一标识符
     *
     * @return
     */
    public int getTag() {
        return mBdAsyncTaskParallelTag.getId();
    }

    /**
     * 获取类型
     *
     * @return
     */
    public AsyncTaskParallelType getType() {
        return mAsyncTaskParallelType;
    }

    /**
     * 返回唯一标识（请使用@{link #UniqueId}）
     */
    public static final class BdAsyncTaskParallelTag {
        private static final int MAX_BASE_TAG = 1000;
        private static int mBaseTag = MAX_BASE_TAG;
        private int mTag = 0;

        public static synchronized BdAsyncTaskParallelTag gen() {
            BdAsyncTaskParallelTag tag = new BdAsyncTaskParallelTag();
            tag.mTag = mBaseTag;
            mBaseTag++;
            return tag;
        }

        private BdAsyncTaskParallelTag() {

        }

        public int getTag() {
            return mTag;
        }
    }
}
