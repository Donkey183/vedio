package x;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Keep;
import android.util.AttributeSet;

import com.app.basevideo.util.XmlAttibuteHelper;

@Keep
public class LinearLayout extends android.widget.LinearLayout {

    public LinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable bg = XmlAttibuteHelper.getBackground(attrs);
        if (bg != null)
            setBackgroundDrawable(bg);
    }

    public LinearLayout(Context context) {
        super(context);
    }

    @Override
    public void setBackgroundResource(int resid) {
        super.setBackgroundResource(resid);
    }

    @Override
    public void setBackgroundDrawable(Drawable d) {
        super.setBackgroundDrawable(d);
    }

}
