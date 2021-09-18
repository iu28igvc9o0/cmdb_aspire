package com.aspire.mirror.composite.service.desk.bpm.payload;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author menglinjie
 */
@Data
public class BpmRemindRecordVo implements Serializable {
    private String id;

    private String senderAccount;

    private String senderName;

    private String content;

    private String procInstId;

    private String procDefKey;

    private String nodeId;

    private String receiverAccount;

    private String receiverName;

    private String type;

    private String receiverMobile;

    private String receiverEmail;

    private Date createTime;

    private List<ProcInstVo> procInstVoList;

    private static final long serialVersionUID = 1L;

}