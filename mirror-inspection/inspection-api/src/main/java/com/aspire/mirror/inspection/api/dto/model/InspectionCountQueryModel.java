package com.aspire.mirror.inspection.api.dto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * 巡检数量查询
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.api.dto.model
 * 类名称:    InspectionCountQueryModel.java
 * 类描述:    巡检数量查询
 * 创建人:    JinSu
 * 创建时间:  2020/4/3 14:57
 * 版本:      v1.0
 */
@Data
public class InspectionCountQueryModel {
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("create_time_start")
    private Date createTimeStart;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("create_time_end")
    private Date createTimeEnd;
}
