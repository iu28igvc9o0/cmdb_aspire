package com.aspire.mirror.ops.api.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsParamType
 * <p/>
 *
 * 类功能描述: 参数引用
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年12月21日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
public class OpsParamReference {

	@JsonProperty("entity_id")
	private Long		entityId;

	@JsonProperty("entity_param_code")
	private String		entityParamCode;

	@JsonProperty("param_id")
	private Long		paramId;

	@JsonProperty("param_value")
	private String		paramValue;

	@JsonProperty("ref_param")
	private OpsParam	referParam;

	@JsonProperty("last_update_time")
	private Date		lastUpdateTime;
}
