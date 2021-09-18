package com.aspire.mirror.template.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * monitor_functions新增对象类
 *
 * 项目名称: mirror平台
 * 包:      com.aspire.mirror.template.api.dto
 * 类名称:   FunctionsCreateRequest.java
 * 类描述:   monitor_functions创建请求对象
 * 创建人:   JinSu
 * 创建时间: 2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunctionsCreateRequest implements Serializable {
	
	private static final long serialVersionUID = -7279084338806758988L;

    /** 函数 */
    @NotBlank
    private String function;

    /** 参数 */
    @NotBlank
    private String parameter;

} 
