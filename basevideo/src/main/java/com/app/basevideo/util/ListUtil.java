package com.app.basevideo.util;

import java.util.List;


/**
 * 常用List操作
 * <p/>
 * <b><font color="red">注意线程安全！</font></b>
 */
public class ListUtil {

    public static <T> void clear(List<T> list) {
        if (null == list) {
            return;
        }
        list.clear();
    }

    public static <T> int getCount(List<T> list) {
        if (null == list || list.isEmpty()) {
            return 0;
        }
        return list.size();
    }

    public static <T> T getItem(List<T> list, int position) {
        if (null == list || list.isEmpty()) {
            return null;
        }
        if (position < 0 || position >= list.size()) {
            return null;
        }
        return list.get(position);
    }

    public static <T> int getPosition(List<T> list, T itemData) {
        if (null == list || list.isEmpty() || itemData == null) {
            return -1;
        }
        return list.indexOf(itemData);
    }

    public static List<String> subList(List<String> list, int start, int end) {
        int listSize = getCount(list);
        if (listSize <= 0) {
            return null;
        }
        if (start < 0 || end > listSize) {
            return null;
        }
        return list.subList(start, end);
    }

    public static <T> boolean isEmpty(List<T> list) {
        return (getCount(list) <= 0);
    }

    public static <T> T remove(List<T> list, int position) {
        if (null == list || list.isEmpty()) {
            return null;
        }
        if (position < 0 || position >= list.size()) {
            return null;
        }
        return list.remove(position);
    }
}
