package com.app.video.data;

import java.io.Serializable;

/**
 * 片库信息
 */
public class VaultData implements Serializable {

    /**
     * id : 3
     * createTime : 2016-11-22 10:14:08
     * seqNo : 0
     * dpic : https://img.alicdn.com/imgextra/i2/2220771977/TB2iHnlaqi5V1BjSsphXXaEpXXa_!!2220771977.jpg
     * psort : 3
     * dnum : 422
     * updateNum : 12
     * pname : 岛国女友意淫
     */

    private int id;
    private String createTime;
    private int seqNo;
    private String dpic;
    private int psort;
    private int dnum;
    private int updateNum;
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

    public int getDnum() {
        return dnum;
    }

    public void setDnum(int dnum) {
        this.dnum = dnum;
    }

    public int getUpdateNum() {
        return updateNum;
    }

    public void setUpdateNum(int updateNum) {
        this.updateNum = updateNum;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
