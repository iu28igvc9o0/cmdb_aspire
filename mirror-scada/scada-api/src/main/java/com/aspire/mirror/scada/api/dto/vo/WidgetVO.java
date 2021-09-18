package com.aspire.mirror.scada.api.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

//import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * 视图层对象
 * <p>
 * 项目名称: mirror平台
 * 包:     com.aspire.mirror.scada.api.dto.vo   
 * 类名称:     Widget
 * 类描述:     数据模型
 * 创建人:     pengfeng
 * 创建时间:     2019-05-24 11:31:42
 */
 
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class WidgetVO implements Serializable{
	/**  */
	 @JsonProperty("id")
	private String id;
	
	/** 展示名称 */
	 @JsonProperty("label")
	private String label;
	
	/** 图片大小 */
	 @JsonProperty("size")
	private Integer size;
	
	/** 图片url */
	 @JsonProperty("image")
	private String image;
	
	/** 形状类型 */
	 @JsonProperty("shape")
	private String shape;
	
	/** 节点类型 */
	 @JsonProperty("type")
	private String type;
	
	/** 分组类型 */
	 @JsonProperty("classType")
	private String classType;
	
}
