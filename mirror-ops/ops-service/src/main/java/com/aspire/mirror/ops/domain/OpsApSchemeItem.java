/**
 *
 * 项目名： ops-service 
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
 * @date	2020年4月8日 
 *
 * @version	V1.0
 * <p/>
 *
 *<b>Copyright(c)</b> 2020 卓望公司-版权所有<br/>
 *   
 */
package com.aspire.mirror.ops.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.aspire.mirror.ops.api.domain.autorepair.OpsApSchemeItemDTO;
import com.aspire.mirror.ops.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.Getter;
import lombok.Setter;

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
@Setter
@Getter
public class OpsApSchemeItem extends OpsApSchemeItemDTO {
	
	@JsonIgnore
	public void setExtendAttrsJson(String extendAttrsJson) {
		if (StringUtils.isBlank(extendAttrsJson)) {
			return;
		}
		TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
		Map<String, Object> map = JsonUtil.jacksonConvert(extendAttrsJson, typeRef);
		getExtendAttrs().clear();
		getExtendAttrs().putAll(map);
	}
	
	@JsonIgnore
	public String getExtendAttrsJson() {
		if (getExtendAttrs().isEmpty()) {
			return null;
		}
		return JsonUtil.toJacksonJson(getExtendAttrs());
	}
	
	public static OpsApSchemeItem parse(OpsApSchemeItemDTO apItemDto) {
		return JsonUtil.jacksonConvert(apItemDto, OpsApSchemeItem.class);
	}
	
	public static List<OpsApSchemeItem> parse(List<OpsApSchemeItemDTO> apSchemeItemList) {
		if (CollectionUtils.isEmpty(apSchemeItemList)) {
			return new ArrayList<OpsApSchemeItem>();
		}
		TypeReference<List<OpsApSchemeItem>> typeRef = new TypeReference<List<OpsApSchemeItem>>() {};
		return JsonUtil.jacksonConvert(apSchemeItemList, typeRef);
	}
	
	public static List<OpsApSchemeItemDTO> reverseParse(List<OpsApSchemeItem> apSchemeItemList) {
		return reverseParse(apSchemeItemList, false);
	}
	
	public static List<OpsApSchemeItemDTO> reverseParse(List<OpsApSchemeItem> apSchemeItemList, boolean clone) {
		if (CollectionUtils.isEmpty(apSchemeItemList)) {
			return new ArrayList<OpsApSchemeItemDTO>();
		}
		if (!clone) {
			return apSchemeItemList.stream().map(item -> {
				return OpsApSchemeItemDTO.class.cast(item);
			}).collect(Collectors.toList());
		}
		TypeReference<List<OpsApSchemeItemDTO>> typeRef = new TypeReference<List<OpsApSchemeItemDTO>>() {};
		return JsonUtil.jacksonConvert(apSchemeItemList, typeRef);
	}
}
