package com.aspire.mirror.template.api.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_functions修改对象类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto
 * 类名称:    FunctionsUpdateResponse.java
 * 类描述:    monitor_functions修改响应对象
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class FunctionsUpdateResponse implements Serializable {
	
	private static final long serialVersionUID = -5033845198904941911L;

    /** 函数ID */
    @JsonProperty("function_id")
    private String functionId ;

    /** 监控项ID */
    @JsonProperty("item_id")
    private String itemId ;

    /** 触发器ID */
    @JsonProperty("trigger_id")
    private String triggerId ;

    /** 函数 */
    private String function ;

    /** 参数 */
    private String parameter ;

} 
