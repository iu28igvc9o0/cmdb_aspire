package com.aspire.mirror.composite.service.inspection.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 任务列表请求
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    CompTaskPageRequest.java
 * 类描述:    任务列表请求
 * 创建人:    JinSu
 * 创建时间:  2018/8/10 15:58
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompTaskPageRequest implements Serializable {
    private static final long serialVersionUID = 597074649341738847L;

    /**
     * 任务名称
     */
    private String name;

    private String type;
    /**
     * 任务执行运行开始时间
     */
    @JsonProperty("exec_time_start")
    private String execTimeStart;
    /**
     * 任务执行运行结束时间
     */
    @JsonProperty("exec_time_end")
    private String execTimeEnd;

    /**
     * 页面大小
     */
    @JsonProperty("page_size")
    private Integer pageSize;

    /**
     * 第几页
     */
    @JsonProperty("page_no")
    private Integer pageNo;

    @JsonProperty("template_id")
    private String templateId;
}
