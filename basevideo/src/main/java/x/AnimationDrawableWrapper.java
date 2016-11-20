package x;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Pair;

import com.app.basevideo.util.XmlAttibuteHelper;

import java.util.List;

public class AnimationDrawableWrapper extends AnimationDrawable {
    public static AnimationDrawableWrapper create(List<Pair<Integer, Integer>> pairList) {
        AnimationDrawableWrapper d = new AnimationDrawableWrapper();
        int resId = 0;
        int duration = 0;
        Drawable drawable = null;
        for (Pair<Integer, Integer> pair : pairList) {
            resId = pair.first;
            duration = pair.second;
            drawable = XmlAttibuteHelper.getDrawable(resId);
            d.addFrame(drawable, duration);
        }
        return d;
    }
}
