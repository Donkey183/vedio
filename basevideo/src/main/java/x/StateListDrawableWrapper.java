package x;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.Keep;
import android.util.AttributeSet;

import com.app.basevideo.util.XmlAttibuteHelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@Keep
public class StateListDrawableWrapper extends StateListDrawable {

    public void inflate(Resources r, XmlResourceParser parser, AttributeSet attrs) {
        try {
            int type;
            while ((type = parser.next()) != XmlPullParser.END_DOCUMENT) {
                if (type != XmlPullParser.START_TAG) {
                    continue;
                }


                if (!parser.getName().equals("item")) {
                    continue;
                }

                int drawableRes = 0;
                int attrCount = parser.getAttributeCount();
                int states[] = new int[attrCount - 1];
                for (int i = 0; i < attrCount; i++) {

                    String attrName = parser.getAttributeName(i);
                    String attrValue = parser.getAttributeValue(i);

                    if (attrName.equals("drawable")) {
                        drawableRes = parser.getAttributeResourceValue(i, 0);
                        continue;
                    }

                    if (attrValue.equals("false")) {
                        continue;
                    }

                    if (attrName.equals("state_enabled")) {
                        states[i] = android.R.attr.state_enabled;
                    }

                    if (attrName.equals("state_pressed")) {
                        states[i] = android.R.attr.state_pressed;
                    }

                    if (attrName.equals("state_focused")) {
                        states[i] = android.R.attr.state_focused;
                    }

                }

                Drawable dr;
                if (drawableRes != 0) {
                    dr = XmlAttibuteHelper.getDrawable(drawableRes);
                } else {
                    while ((type = parser.next()) == XmlPullParser.TEXT) {
                    }
                    if (type != XmlPullParser.START_TAG) {
                        throw new XmlPullParserException(parser.getPositionDescription()
                                + ": <item> tag requires a 'drawable' attribute or " + "child tag defining a drawable");
                    }
                    dr = Drawable.createFromXmlInner(r, parser, attrs);
                }

                this.addState(states, dr);
            }

            onStateChange(getState());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
