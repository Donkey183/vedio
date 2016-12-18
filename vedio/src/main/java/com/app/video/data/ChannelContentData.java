package com.app.video.data;

import java.io.Serializable;

/**
 * 频道内容信息
 */
public class ChannelContentData implements Serializable {

    /**
     * id : 34
     * createTime : 2016-12-18 12:31:34
     * seqNo : 0
     * cid : 5
     * cname : 极品美女丝袜诱惑
     * cpic : http://newpic.wantaico.com/wp-content/uploads/2016/11/mt30-300x169.jpg
     * cresource : http://su.cywl5.com/spd/p180.mp4
     * psort : 29
     * clabel : 高清
     */

    private int id;
    private String createTime;
    private int seqNo;
    private int cid;
    private String cname;
    private String cpic;
    private String cresource;
    private int psort;
    private String clabel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCpic() {
        return cpic;
    }

    public void setCpic(String cpic) {
        this.cpic = cpic;
    }

    public String getCresource() {
        return cresource;
    }

    public void setCresource(String cresource) {
        this.cresource = cresource;
    }

    public int getPsort() {
        return psort;
    }

    public void setPsort(int psort) {
        this.psort = psort;
    }

    public String getClabel() {
        return clabel;
    }

    public void setClabel(String clabel) {
        this.clabel = clabel;
    }
}
