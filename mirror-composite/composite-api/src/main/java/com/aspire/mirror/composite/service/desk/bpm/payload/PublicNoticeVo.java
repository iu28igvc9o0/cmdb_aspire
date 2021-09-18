package com.aspire.mirror.composite.service.desk.bpm.payload;

import lombok.Data;

import java.io.Serializable;

@Data
public class PublicNoticeVo implements Serializable {
    private String id;

    private String subject;

    private String subjet;

    private String status;

    private String range;

    private String startTime;

    private String endTime;

    private String instId;

    private String content;

    private String noticeType;

    private String operateStatus;

    private Integer pageNo;

    private Integer pageSize;

    private Integer page;

    private static final long serialVersionUID = 1L;

}