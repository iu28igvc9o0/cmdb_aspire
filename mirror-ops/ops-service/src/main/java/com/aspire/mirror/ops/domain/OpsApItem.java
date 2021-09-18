/**
 *
 * 项目名： ops-service 
 * <p/> 
 *
 * 文件名:  OpsApItem.java 
 * <p/>
 *
 * 功能描述: 自愈指标 
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
package com.aspire.mirror.ops.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.aspire.mirror.ops.api.domain.autorepair.OpsApItemDTO;
import com.aspire.mirror.ops.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.Getter;
import lombok.Setter;

/** 
 *
 * 项目名称: ops-service 
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
@Setter
@Getter
@JsonInclude(Include.NON_EMPTY)
public class OpsApItem extends OpsApItemDTO {
	
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
	
	public static OpsApItem parse(OpsApItemDTO apItemDto) {
		return JsonUtil.jacksonConvert(apItemDto, OpsApItem.class);
	}
	
	public static List<OpsApItem> parse(List<OpsApItemDTO> apItemDtoList) {
		if (CollectionUtils.isEmpty(apItemDtoList)) {
			return new ArrayList<OpsApItem>();
		}
		TypeReference<List<OpsApItem>> typeRef = new TypeReference<List<OpsApItem>>() {};
		return JsonUtil.jacksonConvert(apItemDtoList, typeRef);
	}
}
