package com.app.video.util;

import com.app.basevideo.base.MFBaseApplication;
import com.app.basevideo.cache.MFSimpleCache;

public class PlayCountUtil {


    public static boolean hasAuth(String curArea) {
        String playCount = MFSimpleCache.get(MFBaseApplication.getInstance()).getAsString(curArea);
        if (playCount == null) {
            MFSimpleCache.get(MFBaseApplication.getInstance()).put(curArea, "1");
            return true;
        }
        int count = Integer.parseInt(playCount) + 1;
        MFSimpleCache.get(MFBaseApplication.getInstance()).put(curArea, "" + count);
        return count < 3;
    }

}
