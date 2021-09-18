package com.aspire.mirror.template.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 触发器新增对象类
 *
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.api.dto
 * 类名称:   TriggersCreateResponse.java
 * 类描述:   触发器创建响应对象
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TriggersCreateResponse implements Serializable {
	
	private static final long serialVersionUID = -8491391742619288729L;

    /** 触发器ID */
    @JsonProperty("trigger_id")
    private String triggerId;

    /** 触发器名称 */
    private String name;

} 
