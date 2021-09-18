package com.aspire.mirror.alert.api.dto.notify;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author menglinjie
 */
@Data
public class SmsRecordReq implements Serializable {
    private String id;

    private String receiverMobile;

    private List<Map<String,Object>> receivers;

    private String receiverName;

    private String receiverUuid;

    private String senderUuid;

    private Date createTime;

    private Integer status;

    private Integer isDelete;

    private String content;

    private String errorLog;
    
    private List<String> idList;

    private static final long serialVersionUID = 1L;

}