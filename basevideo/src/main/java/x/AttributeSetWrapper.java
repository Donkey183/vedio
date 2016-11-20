package x;

import android.util.AttributeSet;


public class AttributeSetWrapper implements AttributeSet {
    public static final String NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android";
    public static final String ATTRIBUTE_BACKGROUND = "background";
    private AttributeSet attrs;

    public AttributeSetWrapper() {

    }

    public AttributeSetWrapper(AttributeSet attrs) {
        this.attrs = attrs;
    }

    public int getAttributeCount() {
        return attrs.getAttributeCount();
    }

    public String getAttributeName(int index) {
        return attrs.getAttributeName(index);
    }

    public String getAttributeValue(int index) {
        return attrs.getAttributeValue(index);
    }

    public String getAttributeValue(String namespace, String name) {
        return attrs.getAttributeValue(namespace, name);
    }

    public String getPositionDescription() {
        return attrs.getPositionDescription();
    }

    public int getAttributeNameResource(int index) {
        return attrs.getAttributeNameResource(index);
    }

    public int getAttributeListValue(String namespace, String attribute, String[] options, int defaultValue) {
        return attrs.getAttributeListValue(namespace, attribute, options, defaultValue);
    }

    public boolean getAttributeBooleanValue(String namespace, String attribute, boolean defaultValue) {
        return attrs.getAttributeBooleanValue(namespace, attribute, defaultValue);
    }

    public int getAttributeResourceValue(String namespace, String attribute, int defaultValue) {
        return attrs.getAttributeResourceValue(namespace, attribute, defaultValue);
    }

    public int getAttributeIntValue(String namespace, String attribute, int defaultValue) {
        return attrs.getAttributeIntValue(namespace, attribute, defaultValue);
    }

    public int getAttributeUnsignedIntValue(String namespace, String attribute, int defaultValue) {
        return attrs.getAttributeUnsignedIntValue(namespace, attribute, defaultValue);
    }

    public float getAttributeFloatValue(String namespace, String attribute, float defaultValue) {
        return attrs.getAttributeFloatValue(namespace, attribute, defaultValue);
    }

    public int getAttributeListValue(int index, String[] options, int defaultValue) {
        return attrs.getAttributeListValue(index, options, defaultValue);
    }

    public boolean getAttributeBooleanValue(int index, boolean defaultValue) {
        return attrs.getAttributeBooleanValue(index, defaultValue);
    }

    public int getAttributeResourceValue(int index, int defaultValue) {
        return attrs.getAttributeResourceValue(index, defaultValue);
    }

    public int getAttributeIntValue(int index, int defaultValue) {
        return attrs.getAttributeIntValue(index, defaultValue);
    }

    public int getAttributeUnsignedIntValue(int index, int defaultValue) {
        return attrs.getAttributeUnsignedIntValue(index, defaultValue);
    }

    public float getAttributeFloatValue(int index, float defaultValue) {
        return attrs.getAttributeFloatValue(index, defaultValue);
    }

    public String getIdAttribute() {
        return attrs.getIdAttribute();
    }

    public String getClassAttribute() {
        return attrs.getClassAttribute();
    }

    public int getIdAttributeResourceValue(int defaultValue) {
        return attrs.getIdAttributeResourceValue(defaultValue);
    }

    public int getStyleAttribute() {
        return attrs.getStyleAttribute();
    }

    public int getBackgroundResId() {
        return attrs.getAttributeResourceValue(NAMESPACE_ANDROID, ATTRIBUTE_BACKGROUND, -1);
    }
}
