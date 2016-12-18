package com.app.video.data;


import java.util.List;

public class VideoDetailData {


    /**
     * id : 3
     * createTime : 2016-11-26 19:37:28
     * seqNo : 0
     * dytype : 0
     * content : 体验3
     * psort : 3
     * userName : 用户3
     */

    public List<Detail> videoCommentList;
    /**
     * id : 2
     * createTime : 2016-11-26 20:29:58
     * seqNo : 0
     * dytype : 0
     * username : 用户2
     * userimg : http://p1.qzone.la/upload/0/200ogakq.jpg
     * utime : 1个小时前
     * content : 233232
     * psort : 2
     */

    public List<Detail> pageCommentList;
    /**
     * id : 61
     * createTime : null
     * seqNo : 0
     * dypic : http://img.fcbird.org/20001/upfile/v1/1456041733039a.jpg
     * dyres : http://vod1.fcbird.org/try/20s_4.mp4
     * psort : 5
     * dytype : 0
     * dname : 美女热舞诱惑宅男的世界
     * dlabel : 高清
     */

    public List<Detail> recommendVideoList;


    public static class Detail {


        /**
         * id : 3
         * createTime : 2016-11-26 19:37:28
         * seqNo : 0
         * dytype : 0
         * content : 体验3
         * psort : 3
         * userName : 用户3
         * userimg : http://p1.qzone.la/upload/0/200ogakq.jpg
         * utime : 1个小时前
         * dypic : http://img.fcbird.org/20001/upfile/v1/1456041733039a.jpg
         * dyres : http://vod1.fcbird.org/try/20s_4.mp4
         * dname : 美女热舞诱惑宅男的世界
         * dlabel : 高清
         */

        private int id;
        private String createTime;
        private int seqNo;
        private String dytype;
        private String content;
        private int psort;
        private String username;
        private String userimg;
        private String utime;
        private String dypic;
        private String dyres;
        private String dname;
        private String dlabel;

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

        public String getDytype() {
            return dytype;
        }

        public void setDytype(String dytype) {
            this.dytype = dytype;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getPsort() {
            return psort;
        }

        public void setPsort(int psort) {
            this.psort = psort;
        }

        public String getUserName() {
            return username;
        }

        public void setUserName(String userName) {
            this.username = userName;
        }

        public String getUserimg() {
            return userimg;
        }

        public void setUserimg(String userimg) {
            this.userimg = userimg;
        }

        public String getUtime() {
            return utime;
        }

        public void setUtime(String utime) {
            this.utime = utime;
        }

        public String getDypic() {
            return dypic;
        }

        public void setDypic(String dypic) {
            this.dypic = dypic;
        }

        public String getDyres() {
            return dyres;
        }

        public void setDyres(String dyres) {
            this.dyres = dyres;
        }

        public String getDname() {
            return dname;
        }

        public void setDname(String dname) {
            this.dname = dname;
        }

        public String getDlabel() {
            return dlabel;
        }

        public void setDlabel(String dlabel) {
            this.dlabel = dlabel;
        }
    }
}
