package com.aspire.ums.cmdb.ipCollect.entity;

import lombok.Data;

/**
 * BPM派发工单请求参数.
 *
 * Created by zhuhenghui on 2020/11/30.
 *
 */
@Data
public class BpmAutoStartTaskOrderParam {

    public BpmAutoStartTaskOrderParam(String sqrID, String gdbt, String slfyjywxtID, String slfejywxtID, String ydjkrRoleID,
                                      String roleID, String xqms, String sfxysjlsp, String sfxybmldsp) {
        this.sqrID = sqrID;
        this.gdbt = gdbt;
        this.slfyjywxtID = slfyjywxtID;
        this.slfejywxtID = slfejywxtID;
        this.ydjkrRoleID = ydjkrRoleID;
        this.roleID = roleID;
        this.xqms = xqms;
        this.sfxysjlsp = sfxysjlsp;
        this.sfxybmldsp = sfxybmldsp;
    }

    private String sqrID;

    private String gdbt;

    private String slfyjywxtID;

    private String slfejywxtID;

    private String ydjkrRoleID;

    private String roleID;

    private String xqms;

    // private String xqfj;

    private String csrID;

    private String sfxysjlsp;

    private String sjlID;

    private String sfxybmldsp;

    private String bmldID;
}
