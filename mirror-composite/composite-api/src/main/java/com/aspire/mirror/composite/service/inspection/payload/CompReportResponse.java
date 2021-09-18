package com.aspire.mirror.composite.service.inspection.payload;

import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 报告详情结果
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    CompReportResponse.java
 * 类描述:    报告详情结果
 * 创建人:    JinSu
 * 创建时间:  2018/8/14 13:44
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
//@ExcelTarget("report")
public class CompReportResponse implements Serializable {
    private static final long serialVersionUID = 5452306817027480878L;
    /**
     * 正常结果集
     */
//    @ExcelCollection(name = "巡检结果正常信息")
    private List<CompInspectionDetail> normal;

    /**
     * 异常结果集
     */
//    @ExcelCollection(name = "巡检结果异常信息")
    private List<CompInspectionDetail> exception;

    /**
     *
     */
    @JsonProperty("noResult")
//    @ExcelCollection(name = "巡检无结果信息")
    private List<CompInspectionDetail> noResult;

    @JsonProperty("artificialJudgment")
//    @ExcelCollection(name = "")
    private List<CompInspectionDetail> artificialJudgment;


    @JsonProperty("report_item_list")
    private List<CompInspectionDetail> reportItemList;

    private String name;

    @JsonProperty("finish_time")
    private Date finishTime;

    @JsonProperty("device_num")
    private Integer deviceNum;

    @JsonProperty("task_type")
    private String taskType;

    @JsonProperty("create_time")
    private Date createTime;

    @JsonProperty("report_id")
    private String reportId;

    @JsonProperty("task_name")
    private String taskName;

    @JsonProperty("item_num")
    private Integer itemNum;

    @JsonProperty("task_id")
    private String taskId;

}
