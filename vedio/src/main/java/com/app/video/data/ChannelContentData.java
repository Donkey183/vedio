package com.app.video.data;

import java.io.Serializable;

/**
 * 频道内容信息
 */
public class ChannelContentData implements Serializable {
    private String id;
    private String createTime;
    private String seqNo;
    private String cname;
    private String cpic;  //图片Url
    private String cresource; //资源Url
    private String psort;
    private String clabel; //标签


    public String getId() {
        return id == null ? "" : id;
    }

    public String getCreateTime() {
        return createTime == null ? "" : createTime;
    }

    public String getSeqNo() {
        return seqNo == null ? "" : seqNo;
    }

    public String getPsort() {
        return psort == null ? "" : psort;
    }

    public String getCname() {
        return cname == null ? "null" : cname;
    }

    public String getCpic() {
        return cpic == null ? "null" : cpic;
    }

    public String getCresource() {
        return cresource == null ? "null" : cresource;
    }

    public String getClabel() {
        return clabel == null ? "null" : clabel;
    }
}
