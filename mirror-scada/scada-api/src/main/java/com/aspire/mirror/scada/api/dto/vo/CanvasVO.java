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
 * 类名称:     Canvas
 * 类描述:     数据模型
 * 创建人:     pengfeng
 * 创建时间:     2019-05-24 11:32:23
 */
 
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class CanvasVO implements Serializable{
	/**  */
	 @JsonProperty("id")
	private String id;
	
	/** 关联业务Id */
	 @JsonProperty("bizId")
	private String bizId;
	
	/** 画布内容 */
	 @JsonProperty("content")
	private String content;
	
	/** 描述 */
	 @JsonProperty("remark")
	private String remark;
	
}
