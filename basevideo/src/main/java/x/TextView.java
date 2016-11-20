package x;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Keep;
import android.util.AttributeSet;

import com.app.basevideo.util.XmlAttibuteHelper;

@Keep
public class TextView extends android.widget.TextView {

    public TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint("NewApi")
    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable drawable = XmlAttibuteHelper.getBackground(attrs);

        if (drawable != null) {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                setBackgroundDrawable(drawable);
            } else {
                setBackground(drawable);
            }
        }
    }

    public TextView(Context context) {
        super(context);
    }

    @Override
    public void setBackgroundResource(int resid) {
        Drawable drawable = XmlAttibuteHelper.getDrawable(resid);
        setBackgroundDrawable(drawable);
    }
}
