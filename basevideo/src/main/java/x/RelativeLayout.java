package x;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Keep;
import android.util.AttributeSet;

import com.app.basevideo.util.XmlAttibuteHelper;

@Keep
public class RelativeLayout extends android.widget.RelativeLayout {

    public RelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable bg = XmlAttibuteHelper.getBackground(attrs);
        if (bg != null)
            setBackgroundDrawable(bg);
    }

    public RelativeLayout(Context context) {
        super(context);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
    }

    public void setXBackgroundResource(int resid) {
        Drawable bg = XmlAttibuteHelper.getDrawable(resid);
        setBackgroundDrawable(bg);
    }

    @Override
    public void setBackgroundDrawable(Drawable d) {
        super.setBackgroundDrawable(d);
    }
}
