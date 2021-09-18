package com.aspire.mirror.composite.service.inspection.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    CompReportItemPageRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/12/26 16:09
 * 版本:      v1.0
 */
@Data
public class CompReportItemPageRequest {
    /** 页面大小*/
    @JsonProperty("page_size")
    private int pageSize;

    /** 第几页*/
    @JsonProperty("page_no")
    private int pageNo;

    @JsonProperty("item_id")
    private String itemId;

    @JsonProperty("report_id")
    private String reportId;

    private String status;

    @JsonProperty("exec_status")
    private String execStatus;

    @JsonProperty("object_id")
    private String objectId;

    @JsonProperty("object_type")
    private String objectType = "3";

    private List<String> logs;

    @JsonProperty("item_group")
    private String itemGroup;
}
