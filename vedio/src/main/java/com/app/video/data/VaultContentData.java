package com.app.video.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 片库内容信息
 */
public class VaultContentData implements Serializable {

    public static final List<VaultContent> vaultContentList = new ArrayList<>();

    public class VaultContent implements Serializable{
        private String id;
        private String createTime;
        private String seqNo;
        private String dpic;  //图片Url
        private String psort;
        private String pname;//大标题


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

        public String getPname() {
            return pname == null ? "" : pname;
        }
    }

}
