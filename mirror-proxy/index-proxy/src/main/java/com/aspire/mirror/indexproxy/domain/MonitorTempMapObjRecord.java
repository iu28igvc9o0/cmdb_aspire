package com.aspire.mirror.indexproxy.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 模板详情对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto
 * 类名称:    TemplateDetailResponse.java
 * 类描述:    监控模板与监控对象映射关系
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(of={"templateId", "objectType", "objectId"}, callSuper=false)
public class MonitorTempMapObjRecord extends BasicDataOperateAware implements Serializable {
	private static final long serialVersionUID = 1887003977443728371L;

	@JsonProperty("template_object_id")
    private String id;
    
    @JsonProperty("template_id")
    private String templateId;
    
    @JsonProperty("object_type")
    private String objectType;
    
    @JsonProperty("object_id")
    private String objectId;
} 
