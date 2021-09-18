/**
 *
 * 项目名： ops-api 
 * <p/> 
 *
 * 文件名:  OpsAutoRepairItem.java 
 * <p/>
 *
 * 功能描述: TODO 
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年3月5日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2020 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.api.domain.autorepair;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsApItemDTO
 * <p/>
 *
 * 类功能描述: 自愈指标
 * <p/>
 *
 * @author	pengguihua
 *
 * @date	2020年3月5日  
 *
 * @version	V1.0 
 * <br/>
 *
 * <b>Copyright(c)</b> 2020 卓望公司-版权所有 
 *
 */
@Data
@EqualsAndHashCode(of= {"sourceMark", "apItemType", "apItem"})
@JsonInclude(Include.NON_EMPTY)
public class OpsApItemDTO {
	public static final Integer			VALUE_TYPE_STRING	= 1;							// 字符串
	public static final Integer			VALUE_TYPE_INTEGER	= 2;							// 整数
	public static final Integer			VALUE_TYPE_FLOAT	= 3;							// 浮点数
	protected Long						id;
	protected Long						apItemTypeId;
	protected String					sourceMark;
	protected String					apItemType;
	protected String					apItemTypeName;
	protected String					apItem;
	protected String					apItemName;
	protected Integer					apItemValueType;									// 1 字符串  2 整数  3 浮点数
	protected String					apItemValueUnit;									// 单位
	protected Integer					referBySchemeFlag;									// 是否已经被自愈规则引用
	@JsonProperty("manage_type")
	protected String					manageType;											// U|D  U:更新   D：删除
	@JsonIgnore
	protected final Map<String, Object>	extendAttrs			= new HashMap<String, Object>();

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date						firstSyncTime;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date						lastUpdateTime;
	
	
	@JsonAnyGetter
	public Map<String, Object> getExtendAttrs() {
		return extendAttrs;
	}
	
	@JsonAnySetter
	public void addExtendAttr(String key, Object value) {
		this.extendAttrs.put(key, value);
	}
}
