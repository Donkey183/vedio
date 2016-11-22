package com.app.video.config;

/**
 * Created by liubohua on 16/11/22.
 */
public class Vault {
    private int visirors;
    private int updates;
    private String valutimg;
    private String valuttype;

    public String getValuttype() {
        return valuttype;
    }

    public void setValuttype(String valuttype) {
        this.valuttype = valuttype;
    }

    public int getVisirors() {
        return visirors;
    }

    public void setVisirors(int visirors) {
        this.visirors = visirors;
    }

    public int getUpdates() {
        return updates;
    }

    public void setUpdates(int updates) {
        this.updates = updates;
    }

    public String getValutimg() {
        return valutimg;
    }

    public void setValutimg(String valutimg) {
        this.valutimg = valutimg;
    }
}
