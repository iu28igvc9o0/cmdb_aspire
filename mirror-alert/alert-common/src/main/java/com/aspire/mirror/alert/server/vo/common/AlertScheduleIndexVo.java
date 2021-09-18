package com.aspire.mirror.alert.server.vo.common;

import lombok.Data;

@Data
public class AlertScheduleIndexVo {

    private String id;

    private String indexName;

    private String indexValue;

    private String indexType;

    private String remark;
}
