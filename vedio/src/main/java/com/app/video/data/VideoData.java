package com.app.video.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 电影列表信息(推荐区、黑金区and so on)
 */
public class VideoData implements Serializable {

    public static final List<Video> videoList = new ArrayList<>();

    public class Video {
        private String id;
        private String createTime;
        private String seqNo;
        private String dpic;  //图片Url
        private String psort;
        private String dyres;//种子Url
        private String dname; //频道名称
        private String dytype; //频道类型(体验区=0  普通会员=1  黄金会员=2  钻石会员=3  黑金会员=4  皇冠会员=5)
        private String dlabel; //分辨率类型(普通、高清等)

        public String getId() {
            return id == null ? "" : id;
        }

        public String getCreateTime() {
            return createTime == null ? "" : createTime;
        }

        public String getSeqNo() {
            return seqNo == null ? "" : seqNo;
        }

        public String getDpic() {
            return dpic == null ? "" : dpic;
        }

        public String getPsort() {
            return psort == null ? "" : psort;
        }

        public String getDyres() {
            return dyres == null ? "" : dyres;
        }

        public String getDname() {
            return dname == null ? "" : dname;
        }

        public String getDytype() {
            return dytype == null ? "" : dytype;
        }

        public String getDlabel() {
            return dlabel == null ? "" : dlabel;
        }
    }
}
