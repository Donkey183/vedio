package x;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Keep;
import android.util.AttributeSet;

@Keep
public class ImageButton extends ImageView {

    public ImageButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageButton(Context context) {
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
