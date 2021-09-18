package com.aspire.ums.cmdb.v2.instance.entity;

import lombok.Data;

import java.util.List;

/**
 * excel表头列
 * 
 * @author lupeng
 * 
 */
@Data
public class PoiTitle implements Cloneable {

	private Integer rowNum; // 行编号

	private Integer columnNum; // 列编号

	private String field; // 字段名

	private String fieldValue; // 字段值

	private String fieldType; // 字段类别+source,+target
	
	private String formId; 

	private String comment; // 字段注释

	private String format; // 字段格式

	private String def; // 默认值

	private String restriction; // 关系类型

	private Integer minLength; // 最小长度

	private Integer maxLength; // 最大长度

	private String validation; // 格式验证

	private Integer min; // 最小值

	private Integer max; // 最大值

	private Integer precision; // 小数位数

	private Boolean formatDate; // 日期格式化

	private List<String> options; // 可选项

	private String formCode;
	
	private String moduleId;
	
	private String formName;
}
