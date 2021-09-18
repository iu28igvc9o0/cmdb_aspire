package com.aspire.mirror.template.api.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * monitor_items新增对象类
 *
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.api.dto
 * 类名称:   ItemsCreateResponse.java
 * 类描述:   monitor_items创建响应对象
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemsCreateResponse implements Serializable {
	
	private static final long serialVersionUID = -5945445783330259350L;

    /** 监控项ID */
    @JsonProperty("item_id")
    private String itemId;

    /** 监控项名称 */
    private String name;

    private String key;

} 
