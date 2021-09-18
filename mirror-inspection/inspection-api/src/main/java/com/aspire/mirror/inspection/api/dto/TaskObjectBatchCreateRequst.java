package com.aspire.mirror.inspection.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * 任务设备批量创建请求
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.api.dto
 * 类名称:    TaskObjectBatchCreateRequst.java
 * 类描述:    任务设备批量创建请求
 * 创建人:    JinSu
 * 创建时间:  2018/8/11 13:37
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
public class TaskObjectBatchCreateRequst implements Serializable {
    private static final long serialVersionUID = 6311884597098588381L;
    /**
     * 任务设备列表
     */
    @JsonProperty("object_list")
    @NotEmpty
    private List<TaskObjectCreateRequest> objectList;
}
