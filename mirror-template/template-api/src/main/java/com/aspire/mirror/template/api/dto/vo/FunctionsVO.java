package com.aspire.mirror.template.api.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_functions视图层对象
 *
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.api.dto.vo
 * 类名称:   FunctionsVO.java
 * 类描述:   monitor_functions视图层属性，属性范围>=表结构属性.
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class FunctionsVO implements Serializable {
	
	private static final long serialVersionUID = -6211420818735657655L;

    /** 函数ID */
    @JsonProperty("function_id")
    private String functionId;

    /** 监控项ID */
    @JsonProperty("item_id")
    private String itemId;

    /** 触发器ID */
    @JsonProperty("trigger_id")
    private String triggerId;

    /** 函数 */
    private String function;

    /** 参数 */
    private String parameter;

} 
