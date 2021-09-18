package com.aspire.mirror.template.api.dto;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;
//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 监控项修改对象类
 *
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto
 * 类名称:    ItemsUpdateResponse.java
 * 类描述:    monitor_items修改响应对象
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class ItemsUpdateResponse implements Serializable {
	
	private static final long serialVersionUID = -8860889863482420691L;

    /** 监控项ID */
    @JsonProperty("item_id")
    private String itemId ;

    /** 监控项名称 */
    private String name ;

} 
