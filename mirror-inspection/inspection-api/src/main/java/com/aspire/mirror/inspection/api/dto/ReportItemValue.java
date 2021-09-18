package com.aspire.mirror.inspection.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 报告Item值扩展
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.api.dto
 * 类名称:    ReportItemValue.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2020/6/6 15:43
 * 版本:      v1.0
 */
@Data
public class ReportItemValue {

    @JsonProperty("report_item_value_id")
    private Long reportItemValueId;

    @JsonProperty("report_item_id")
    private Long reportItemId;
    /**
     * 结果名称
     */
    @JsonProperty("result_name")
    private String resultName;

    /**
     * 结果值
     */
    @JsonProperty("result_value")
    private String resultValue;

    /**
     * 结果状态
     * 0-正常1-异常2-无结果
     */
    @JsonProperty("result_status")
    private String resultStatus;

    /**
     * 结果描述
     */
    @JsonProperty("result_desc")
    private String resultDesc;
}
