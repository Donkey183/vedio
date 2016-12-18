package com.app.video.data;

import java.io.Serializable;

/**
 * 片库内容信息
 */
public class VaultContentData implements Serializable {


    /**
     * id : 3
     * createTime : 2016-12-18 20:49:04
     * seqNo : 0
     * pid : 3
     * dpic :  http://conf.qiumeng88.com:5008/dapp/qd01.jpg
     * psort : 1
     * pname : 第一组
     */

    private int id;
    private String createTime;
    private int seqNo;
    private int pid;
    private String dpic;
    private int psort;
    private String pname;

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

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getDpic() {
        return dpic;
    }

    public void setDpic(String dpic) {
        this.dpic = dpic;
    }

    public int getPsort() {
        return psort;
    }

    public void setPsort(int psort) {
        this.psort = psort;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
