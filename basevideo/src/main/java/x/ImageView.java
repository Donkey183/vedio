package x;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Keep;
import android.util.AttributeSet;

import com.app.basevideo.util.XmlAttibuteHelper;
import com.bumptech.glide.Glide;


@Keep
public class ImageView extends android.widget.ImageView {

    public ImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable drawable = XmlAttibuteHelper.getBackground(attrs);
        if (drawable != null)
            setBackgroundDrawable(drawable);
        drawable = XmlAttibuteHelper.getImageDrawable(attrs);
        if (drawable != null)
            setImageDrawable(drawable);
    }

    public ImageView(Context context) {
        super(context);
    }

    @Override
    public void setBackgroundResource(int resid) {
        Drawable drawable = XmlAttibuteHelper.getDrawable(resid);
        setBackgroundDrawable(drawable);
    }

    @Override
    public void setImageResource(int resId) {
        Drawable drawable = XmlAttibuteHelper.getDrawable(resId);
        setImageDrawable(drawable);
    }

    public void startLoad(String url) {
        Glide.with(this.getContext()).load(url).into(this);
    }
}
