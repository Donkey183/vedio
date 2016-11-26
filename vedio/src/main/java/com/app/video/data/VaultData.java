package com.app.video.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 片库信息
 */
public class VaultData implements Serializable {

    public static final List<Vault> vaultList = new ArrayList<>();

    public class Vault implements Serializable{
        private String id;
        private String createTime;
        private String seqNo;
        private String dpic;  //图片Url
        private String psort;
        private String dnum;//点击人数
        private String updateNum;//更新部数
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

        public String getDnum() {
            return dnum == null ? "" : dnum;
        }

        public String getUpdateNum() {
            return updateNum == null ? "" : updateNum;
        }
    }
}
