package com.app.video.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 频道信息
 */
public class ChannelData implements Serializable {
    public static final List<Channel> channelList = new ArrayList<>();

    public class Channel implements Serializable{
        private String id;
        private String createTime;
        private String seqNo;
        private String dpic;  //图片Url
        private String dname; //频道名称
        private String psort;

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

        public String getDname() {
            return dname == null ? "" : dname;
        }

        public String getPsort() {
            return psort == null ? "" : psort;
        }
    }
}
