package com.aspire.mirror.composite.service.inspection.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 修改请求返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.composite.service.inspection.payload
 * 类名称:    CompTaskUpdateResponse.java
 * 类描述:    修改请求返回
 * 创建人:    JinSu
 * 创建时间:  2018/8/10 14:59
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@ToString
public class CompTaskUpdateResponse implements Serializable {
    /** 任务ID */
    @JsonProperty("task_id")
    private String taskId ;

    /** 任务名 */
    private String name ;
}
