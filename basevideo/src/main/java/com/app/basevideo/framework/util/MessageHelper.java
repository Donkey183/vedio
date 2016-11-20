package com.app.basevideo.framework.util;

import com.app.basevideo.framework.Priority;

import java.util.LinkedList;


/**
 * 框架辅助类，提供命令号类型查找，命令号是否合法判断，根据优先级操作队列等方法
 * 包内使用
 */
public class MessageHelper {
    public static final int BASE_SEGMENT_LENGTH = 1000000000;
    public static final int BASE_CUSTOM_CMD = 0;

    public static enum TYPE {
        CUSTOM
    }

    /**
     * 根据cmd返回消息类型，不支持的返回null
     * 自定义类型：命令号区间 [1000000-2000000]
     *
     * @param cmd
     * @return
     */
    public static TYPE getMessageType(int cmd) {
        if (cmd >= BASE_CUSTOM_CMD
                && cmd < BASE_CUSTOM_CMD + BASE_SEGMENT_LENGTH) {
            return TYPE.CUSTOM;
        } else {
            LogUtil.e("cmd invalid:cmd=" + cmd);
            return null;
        }
    }


    /**
     * 检查cmd是否是custom消息
     *
     * @param cmd
     * @return
     */
    public static boolean checkCustomCmd(int cmd) {
        if (cmd >= BASE_CUSTOM_CMD
                && cmd < BASE_CUSTOM_CMD + BASE_SEGMENT_LENGTH) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据优先级查找插入list中的位置
     *
     * @param queue
     * @param priority
     * @return
     */
    public static <T extends Priority> int getInsertIndex(LinkedList<T> queue, int priority) {
        int index = 0;
        int num = queue.size();
        for (index = 0; index < num; index++) {
            Priority p = queue.get(index);
            if (p.getPriority() > priority) {
                break;
            }
        }
        return index;
    }

    /**
     * 优先级列表中根据优先级插入数据
     *
     * @param queue
     * @param data
     */
    public static <T extends Priority> void insert(LinkedList<T> queue, T data) {
        if (data == null) {
            return;
        }
        if (queue.contains(data) == true) {
            return;
        }
        int index = getInsertIndex(queue, data.getPriority());
        queue.add(index, data);
    }
}
