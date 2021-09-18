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
package com.aspire.mirror.inspection.api.dto.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** 
 *
 * 项目名称: ops-api 
 * <p/>
 * 
 * 类名: OpsApItem
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
public class OpsApItem {
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
	public void setExtendAttr(String key, Object value) {
		this.extendAttrs.put(key, value);
	}

//	@JsonIgnore
//	public String getRecordMd5() {
//		List<String> referAttrs = new ArrayList<>();
//		for (Map.Entry<String, Object> entry : getExtendAttrs().entrySet()) {
//			referAttrs.add(entry.getKey() + "=" + String.valueOf(entry.getValue()));
//		}
//		Collections.sort(referAttrs); // 排序
//
//		referAttrs.addAll(Arrays.asList(new String[]{
//				String.valueOf(apItemTypeId), apItem, apItemName, String.valueOf(apItemValueType)}));
//		return Md5Util.getMD5String(StringUtils.join(referAttrs, "|"));
//	}
}
