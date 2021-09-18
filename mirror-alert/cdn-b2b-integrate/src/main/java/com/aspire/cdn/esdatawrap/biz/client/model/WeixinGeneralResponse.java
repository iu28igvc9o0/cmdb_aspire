package com.aspire.cdn.esdatawrap.biz.client.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/** 
 *
 * 项目名称: esdatawrap6 
 * <p/>
 * 
 * 类名: WeixinGeneralResponse
 * <p/>
 *
 * 类功能描述: 微信请求响应类
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年5月19日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
public class WeixinGeneralResponse {
	
	@JsonProperty("errcode")
	protected Integer errcode;
	
	@JsonProperty("errmsg")
	protected String errmsg;
	
	@JsonIgnore
	protected final Map<String, Object> extraAttrMap = new HashMap<>();
	
	@JsonAnyGetter
	public Map<String, Object> getExtraAttrMap() {
		return this.extraAttrMap;
	}
	
	@JsonAnySetter
	public void addExtraAttr(String key, Object value) {
		this.extraAttrMap.put(key, value);
	}
}
