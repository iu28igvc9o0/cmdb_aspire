package com.aspire.mirror.inspection.api.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.inspection.api.dto.model
 * 类名称:    TaskDeviceDTO.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2018/8/11 13:49
 * 版本:      v1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class TaskObjectDTO implements Serializable {
	public static final String	OBJECT_TYPE_DEVICE	= "1";
	public static final String	OBJECT_TYPE_BIZ		= "2";
	private static final long	serialVersionUID	= -4013852348733393673L;
    /**
     * id
     */
    @ApiModelProperty("id")
    private String taskObjectId;
    /**
     * 任务ID
     */
    @ApiModelProperty("任务ID")
    private String taskId;
    /**
     * 模板ID
     */
    @ApiModelProperty("模板ID")
    private String templateId;
    /**
     * 设备ID
     */
    @ApiModelProperty("设备类型")
    private String objectType;
    
    @ApiModelProperty("设备id或业务id")
    private String objectId;
    
    
}
