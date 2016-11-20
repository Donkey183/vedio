package com.app.basevideo.framework;


/**
 * 唯一id生成器
 */
public class UniqueId {
    private volatile static int sBaseId = 10000000;
    private int mId = 0;

    private UniqueId() {

    }

    public static synchronized UniqueId gen() {
        UniqueId uniqueId = new UniqueId();
        uniqueId.mId = sBaseId;
        sBaseId--;
        return uniqueId;
    }

    public int getId() {
        return mId;
    }
}
