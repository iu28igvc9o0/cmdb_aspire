/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  OpsApSchemeItem.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年3月6日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2020 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain.autorepair;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsApSchemeItemDTO
 * <p/>
 *
 * 类功能描述: 自愈方案应用的指标
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年3月6日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@JsonInclude(Include.NON_NULL)
@EqualsAndHashCode(of={"schemeId", "apItemTypeId", "apItem"})
public class OpsApSchemeItemDTO {
	public static final String	SYMBOL_CONTAIN		= "::contains::";
	public static final String	SYMBOL_SINGLE_EQUAL	= "=";
	public static final String	SYMBOL_EQUAL		= "==";
	public static final String	SYMBOL_NOT_EQUAL	= "!=";
	protected Long				id;
	protected Long				schemeId;
	protected String			schemeName;
	protected Long				apItemTypeId;
	protected String			sourceMark;
	protected String			apItemType;
	protected String			apItemTypeName;
	protected String			apItem;
	protected String			apItemName;
	protected String			judgeSymbol;				// <, <=, ==, >=, >, contain
	protected String			judgeValue;
	protected Integer			order;
	
	@JsonIgnore
	protected final Map<String, Object>	extendAttrs	= new HashMap<String, Object>();
	
	@JsonAnyGetter
	public Map<String, Object> getExtendAttrs() {
		return extendAttrs;
	}
	
	@JsonAnySetter
	public void addExtendAttr(String key, Object value) {
		this.extendAttrs.put(key, value);
	}
}
