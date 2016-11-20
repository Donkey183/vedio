package com.app.basevideo.util;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.util.Xml;
import android.widget.ImageView.ScaleType;

import com.app.basevideo.R;
import com.app.basevideo.base.MFBaseApplication;
import com.app.basevideo.framework.util.LogUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import x.StateListDrawableWrapper;


/**
 * 布局文件Xml属性读取、修改帮助类
 */
public class XmlAttibuteHelper {
    public static final String NAMESPACE_X_VIEW = "http://schemas.android.com/apk/res/com.sdu.didi.psnger";
    public static final String ATTRIBUTE_BACKGROUND = "background";
    public static final String ATTRIBUTE_SRC = "src";

    public static Drawable getBackground(AttributeSet attrs) {
        TypedArray typedArray = MFBaseApplication.getInstance().obtainStyledAttributes(attrs, R.styleable.x);
        TypedValue typedValue = typedArray.peekValue(R.styleable.x_backgrounds);
        if (typedValue == null || typedValue.string == null)
            return null;
        String name = typedValue.string.toString();
        int resId = typedValue.resourceId;
        // int resId = getBackgroundResourceId(attrs);
        typedArray.recycle();

        if (resId == 0x0)
            return null;

        if (name.endsWith(".9.png")) {
            Drawable d = getNinePatchDrawable(typedValue);

            if (d != null) {
                return d;
            }
        } else if (name.endsWith(".png") || name.endsWith(".jpg")) {
            // LogUtil.trace();
            // Bitmap bitmap = ImageUtil.createSuitableDrawable(resId);
            // return new BitmapDrawable(bitmap);
            Bitmap bitmap = getResource(typedValue);
            LogUtil.d("getBackground ： " + name + " resId : " + resId + " bitmap : " + bitmap);
            if (bitmap != null)
                return new BitmapDrawable(bitmap);
        }

        Resources r = MFBaseApplication.getInstance().getResources();
        if (name.endsWith(".xml")) {
            XmlResourceParser parser = r.getXml(resId);

            LogUtil.d("parser.getName() : "
                    + parser.getName() + " name : " + name + " resId : " + resId + "  parser.getAttributeCount() : "
                    + parser.getAttributeCount());
            boolean isSelector = parseSelector(resId, name);
            LogUtil.d("isSelector : " + isSelector + " name : " + name);
            if (isSelector) {
                StateListDrawableWrapper d = new StateListDrawableWrapper();
                d.inflate(r, parser, attrs);

                LogUtil.d("isSelector : " + isSelector + " name : " + name + " drawable : " + d.getCurrent());
                return d;
            }
            try {
                return Drawable.createFromXml(r, parser);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return r.getDrawable(resId);
    }

    public static boolean parseSelector(int xmlId, String name) {
        XmlResourceParser xml = ResourcesHelper.getXml(xmlId);
        int eventType = -1;
        try {
            xml.next();
            eventType = xml.getEventType();
        } catch (XmlPullParserException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        while (eventType != XmlPullParser.END_DOCUMENT) {

            // 到达title节点时标记一下
            if (eventType == XmlPullParser.START_TAG) {
                if (xml.getName().equals("selector")) {
                    return true;
                }
            }

            // 如过到达标记的节点则取出内容
            // if (eventType == XmlPullParser.TEXT && inTitle) {
            // ((TextView) findViewById(R.id.txXml)).setText(xml.getText());
            // }

            try {
                xml.next();
                eventType = xml.getEventType();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Drawable getImageDrawable(AttributeSet attrs) {
        TypedArray typedArray = MFBaseApplication.getInstance().obtainStyledAttributes(attrs, R.styleable.x);
        TypedValue typedValue = typedArray.peekValue(R.styleable.x_src_x);
        if (typedValue == null)
            return null;
        String name = typedValue.string.toString();
        int resId = typedValue.resourceId;
        typedArray.recycle();

        if (resId == 0x0)
            return null;

        if (name.endsWith(".9.png")) {
            LogUtil.d(".9.png------------" + name);

            Drawable d = getNinePatchDrawable(typedValue);

            if (d != null) {
                LogUtil.d("get--------ninie--------");
                return d;
            }
        } else if (name.endsWith(".png") || name.endsWith(".jpg")) {
            Bitmap bitmap = getResource(typedValue);
            return new BitmapDrawable(bitmap);
        }
        Resources r = MFBaseApplication.getInstance().getResources();
        if (name.endsWith(".xml")) {
            XmlResourceParser parser = r.getXml(resId);
            if ("selector".equals(parser.getName())) {
                StateListDrawableWrapper d = new StateListDrawableWrapper();
                d.inflate(r, parser, attrs);
                return d;
            }
            try {
                return Drawable.createFromXml(r, parser);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return r.getDrawable(resId);
    }

    public static Drawable getDrawable(int resId) {
        TypedValue typedValue = new TypedValue();
        Resources r = MFBaseApplication.getInstance().getResources();
        r.getValue(resId, typedValue, true);
        if (typedValue.string == null)
            return null;
        String name = typedValue.string.toString();

        if (resId == 0x0)
            return null;

        if (name.endsWith(".9.png")) {
            Drawable d = getNinePatchDrawable(typedValue);

            if (d != null) {
                return d;
            }
        } else if (name.endsWith(".png") || name.endsWith(".jpg")) {
            Bitmap bitmap = getResource(typedValue);
            return new BitmapDrawable(bitmap);
        }

        if (name.endsWith(".xml")) {
            XmlResourceParser parser = r.getXml(resId);

            boolean isSelector = parseSelector(resId, name);

            if (isSelector) {
                StateListDrawableWrapper d = new StateListDrawableWrapper();
                AttributeSet attrX = Xml.asAttributeSet(parser);
                d.inflate(r, parser, attrX);
                return d;
            }
            try {
                return Drawable.createFromXml(r, parser);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return r.getDrawable(resId);
    }

    private static Bitmap getResource(TypedValue typedValue) {
        String name = typedValue.string.toString();
        int resId = typedValue.resourceId;
        InputStream is = getImageInputStream(resId, name);
        Bitmap bitmap = ImageUtil.createSuitableDrawable(is);
        return bitmap;
    }

    private static InputStream getImageInputStream(int resId, String name) {
        Resources resources = MFBaseApplication.getInstance().getResources();
        AssetManager assetManager = resources.getAssets();
        InputStream is = null;
        try {
            is = assetManager.open(name);
        } catch (IOException e) {
            try {
                is = resources.openRawResource(resId);
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            } catch (NotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return is;
    }

    @SuppressWarnings("unused")
    private static int getBackgroundResourceId(AttributeSet attrs) {
        return attrs.getAttributeResourceValue(NAMESPACE_X_VIEW, ATTRIBUTE_BACKGROUND, -1);
    }

    @SuppressLint("NewApi")
    private static NinePatchDrawable getNinePatchDrawable(TypedValue typedValue) {

        Resources r = MFBaseApplication.getInstance().getResources();
        int rid = typedValue.resourceId;

        Bitmap srcNinePatchBmp = BitmapFactory.decodeResource(r, rid);

        if (srcNinePatchBmp == null) {
            return null;
        }

        Bitmap tarNinePatchBmp = null;
        NinePatchDrawable tarNinePatchDra = null;

        float ratio = WindowUtil.getScaleRatio();
        float tarWidth = srcNinePatchBmp.getWidth() * ratio;
        float tarHeight = srcNinePatchBmp.getHeight() * ratio;

        // LogUtil.d("tarWidth: " + tarWidth + " tarHeight: " + tarHeight);

        // Config config = srcNinePatchBmp.getConfig();

        // int sdk = Build.VERSION.SDK_INT;
        // if (sdk >= Build.VERSION_CODES.KITKAT) {

        // if (srcNinePatchBmp.isMutable()) {
        // srcNinePatchBmp.reconfigure((int) tarWidth, (int) tarHeight, config);
        // tarNinePatchBmp = srcNinePatchBmp;
        //
        // } else {
        // tarNinePatchBmp = ImageUtil.scale(srcNinePatchBmp, tarWidth,
        // tarHeight, ScaleType.FIT_XY, true);
        // }
        //
        // //byte[] chunk = tarNinePatchBmp.getNinePatchChunk();
        // byte[] chunk = srcNinePatchBmp.getNinePatchChunk();
        // chunk = newChunk(chunk, ratio);
        //

        // NinePatch ninePatch = new NinePatch(tarNinePatchBmp, chunk);
        //
        // tarNinePatchDra = new NinePatchDrawable(r, ninePatch);

        // } else {
        LogUtil.d("src chunk size: " + srcNinePatchBmp.getNinePatchChunk().length);
        LogUtil.d("src w: " + srcNinePatchBmp.getWidth() + " h: " + srcNinePatchBmp.getHeight());

        byte[] chunk = srcNinePatchBmp.getNinePatchChunk();
        NinePatchChunk patchChunk = newChunk(chunk, ratio);

        tarNinePatchBmp = ImageUtil.scale(srcNinePatchBmp, tarWidth, tarHeight, ScaleType.FIT_XY, true);
        // tarNinePatchBmp = srcNinePatchBmp;

        LogUtil.d("tar w: " + tarNinePatchBmp.getWidth() + " h: " + tarNinePatchBmp.getHeight());
        LogUtil.d("tar chunk size: " + chunk.length);

        chunk = patchChunk.chunk;
        Rect padding = patchChunk.padding;
        tarNinePatchDra = new NinePatchDrawable(r, tarNinePatchBmp, chunk, padding, null);
        // }

        return tarNinePatchDra;
    }

    private static NinePatchChunk newChunk(byte[] chunk, float ratio) {

        int xDivs[];
        int yDivs[];

        final ByteBuffer byteBuffer = ByteBuffer.wrap(chunk).order(ByteOrder.nativeOrder());

        byte serialized = byteBuffer.get();
        if (serialized == 0)
            return null;

        NinePatchChunk patchChunk = new NinePatchChunk();
        ByteBuffer tarByteBuffer = ByteBuffer.allocate(chunk.length).order(ByteOrder.nativeOrder());

        tarByteBuffer.put(serialized);

        xDivs = new int[byteBuffer.get()];
        yDivs = new int[byteBuffer.get()];

        tarByteBuffer.put((byte) xDivs.length);
        tarByteBuffer.put((byte) yDivs.length);

        tarByteBuffer.put(byteBuffer.get());

        tarByteBuffer.putInt(byteBuffer.getInt());
        tarByteBuffer.putInt(byteBuffer.getInt());

        ////////////////////////
        patchChunk.padding.left = (int) (byteBuffer.getInt() * ratio);
        patchChunk.padding.top = (int) (byteBuffer.getInt() * ratio);
        patchChunk.padding.right = (int) (byteBuffer.getInt() * ratio);
        patchChunk.padding.bottom = (int) (byteBuffer.getInt() * ratio);
        ////////////////////////

        tarByteBuffer.putInt(patchChunk.padding.left);
        tarByteBuffer.putInt(patchChunk.padding.top);
        tarByteBuffer.putInt(patchChunk.padding.right);
        tarByteBuffer.putInt(patchChunk.padding.bottom);
        tarByteBuffer.putInt(byteBuffer.getInt());

        newDivs(xDivs, byteBuffer, ratio);
        newDivs(yDivs, byteBuffer, ratio);

        // LogUtil.d("-------------");
        // for (int i : xDivs) {
        // LogUtil.d(i);
        // }
        //
        // LogUtil.d("=============");
        // for (int i : yDivs) {
        // LogUtil.d(i);
        // }

        putDivs(xDivs, tarByteBuffer);
        putDivs(yDivs, tarByteBuffer);

        while (byteBuffer.hasRemaining()) {
            tarByteBuffer.put(byteBuffer.get());
        }

        patchChunk.chunk = tarByteBuffer.array();
        return patchChunk;
    }

    private static void putDivs(int[] divs, ByteBuffer byteBuffer) {
        for (int index = 0; index < divs.length; index++) {
            byteBuffer.putInt(divs[index]);
        }
    }

    private static void newDivs(int[] divs, ByteBuffer byteBuffer, float ratio) {
        int temp, space;
        for (int index = 0; index < divs.length; index += 2) {
            temp = byteBuffer.getInt();
            divs[index] = (int) (temp * ratio);

            space = divs[index] - temp;
            temp = byteBuffer.getInt();
            divs[index + 1] += temp + space;
        }
    }

    private final static class NinePatchChunk {
        public byte[] chunk;
        public Rect padding = new Rect();
    }

}
