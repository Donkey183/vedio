package com.app.video.data;

import java.io.Serializable;
import java.util.List;

/**
 * 频道内容信息
 */
public class ChannelContentData implements Serializable {

    /**
     * pageNo : 1
     * pageSize : 20
     * orderBy : null
     * order : null
     * autoCount : true
     * result : [{"id":4,"createTime":"2016-11-22 10:21:03","seqNo":0,"cid":3,"cname":"434343","cpic":"https://img.alicdn.com/imgextra/i1/2220771977/TB2imU0tVXXXXbHXpXXXXXXXXXX_!!2220771977.jpg","cresource":"http://i.rb8k.com/xsp/mp4/20s_19.mp4","psort":1,"clabel":"32"}]
     * totalCount : 1
     * pageCount : -1
     * totalPages : 1
     * list1 : null
     * orderBySetted : false
     * hasNext : false
     * nextPage : 1
     * hasPre : false
     * prePage : 1
     * first : 0
     * end : 19
     */

    private int pageNo;
    private int pageSize;
    private int totalCount;
    /**
     * id : 4
     * createTime : 2016-11-22 10:21:03
     * seqNo : 0
     * cid : 3
     * cname : 434343
     * cpic : https://img.alicdn.com/imgextra/i1/2220771977/TB2imU0tVXXXXbHXpXXXXXXXXXX_!!2220771977.jpg
     * cresource : http://i.rb8k.com/xsp/mp4/20s_19.mp4
     * psort : 1
     * clabel : 32
     */

    public List<ResultBean> result;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
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
}
