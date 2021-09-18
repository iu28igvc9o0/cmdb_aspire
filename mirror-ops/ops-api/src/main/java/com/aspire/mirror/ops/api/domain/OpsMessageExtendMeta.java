package com.aspire.mirror.ops.api.domain;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsMessageExtendMeta
 * <p/>
 *
 * 类功能描述: OpsMessageMeta对象  内部 扩展meta
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2019年11月19日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2019 卓望公司-版权所有 
 *
 */
@Getter
@NoArgsConstructor
public class OpsMessageExtendMeta {
	// 业务分类
	private Integer bizClassify;														
	
	// 其它属性
	@JsonIgnore
	protected final Map<String, Object>	extendAttrs	 = new HashMap<String, Object>();	
	
	public OpsMessageExtendMeta(Integer bizClassify) { 
		this.bizClassify = bizClassify;
	}
	
	@JsonAnyGetter
	public Map<String, Object> getExtendAttrs() {
		return extendAttrs;
	}
	
	@JsonAnySetter
	public void addExtendAttr(String key, Object value) {
		this.extendAttrs.put(key, value);
	}
	
	public Object getAttrValByKey(String attrKey) {
		return extendAttrs.get(attrKey);
	}
	
	public <T> T getAttrValByKey(String attrKey, Class<T> clazz) {
		Object attrVal = extendAttrs.get(attrKey);
		return attrVal == null ? null : clazz.cast(attrVal);
	}
}
