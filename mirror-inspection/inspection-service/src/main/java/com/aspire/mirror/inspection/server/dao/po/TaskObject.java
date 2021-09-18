package com.aspire.mirror.inspection.server.dao.po;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 任务设备PO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.server.dao.po
 * 类名称:    TaskDevice.java
 * 类描述:    任务设备PO
 * 创建人:    JinSu
 * 创建时间:  2018/8/11 14:03
 * 版本:      v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskObject {
    /**
     * id
     */
    private String taskObjectId;
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 模板ID
     */
    private String templateId;
    /**
     * 设备类型
     */
    private String objectType;
    
    /**
     * 设备id或业务id
     */
    private String objectId;
    
    
}
