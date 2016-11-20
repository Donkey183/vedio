package x;

import android.content.Context;
import android.support.annotation.Keep;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;

@Keep
public class WebImageView extends android.widget.ImageView {

    public WebImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public WebImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebImageView(Context context) {
        super(context);
    }

    public void startLoad(String url) {
        Glide.with(this.getContext()).load(url).into(this);
    }
}
