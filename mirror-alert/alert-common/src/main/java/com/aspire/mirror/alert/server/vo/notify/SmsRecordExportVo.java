package com.aspire.mirror.alert.server.vo.notify;

import lombok.Data;

import java.io.Serializable;

/**
 * @author menglinjie
 */
@Data
public class SmsRecordExportVo implements Serializable {

    private String startTime;

    private String endTime;

    private String receiver;

    private String content;

    private Integer status;

    private static final long serialVersionUID = 1L;

}