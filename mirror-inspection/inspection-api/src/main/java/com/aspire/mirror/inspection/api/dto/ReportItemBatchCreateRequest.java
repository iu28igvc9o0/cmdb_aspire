package com.aspire.mirror.inspection.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.api.dto
 * 类名称:    ReportItemBatchCreateRequest.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/4/23 11:12
 * 版本:      v1.0
 */
@Data
public class ReportItemBatchCreateRequest {
    @JsonProperty("report_item_list")
    private List<ReportItemCreateRequest> reportItemList;
}
