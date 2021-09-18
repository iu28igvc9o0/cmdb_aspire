package com.aspire.mirror.indexproxy.domain;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.aspire.mirror.indexproxy.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * monitor_object详情对象类
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.mirror.template.api.dto
 * 类名称:    ItemsDetailResponse.java
 * 类描述:    monitor_items创建响应对象
 * 创建人:    JinSu
 * 创建时间:  2018-07-27 13:48:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of={"objectType", "objectId"}, callSuper=false)
public class MonitorObjectRecord extends BasicDataOperateAware implements Serializable {
    private static final long serialVersionUID = -6367448910493063682L;
    private static final TypeReference<Map<String, Object>> TYPE_REF = new TypeReference<Map<String,Object>>() {};

    public static final String Object_TYPE_SELF_MONITOR = "3";

    @JsonProperty("object_type")
    private String objectType;

    @JsonProperty("object_id")
    private String objectId;

    private String name;

    private String description;

    @JsonProperty("extend_obj")
    private String extendObj;
    
    @JsonIgnore
    private Map<String, Object> extendAttrMap;

    @JsonProperty("sharding_base")
    public Integer getShardingBase() {
    	return objectId == null ? 0 : Math.abs(objectId.hashCode());
    }
    
    public void setExtendObj(String extendObj) {
    	this.extendObj = extendObj;
    	if (StringUtils.isNotBlank(extendObj)) {
    		extendAttrMap = JsonUtil.jacksonConvert(extendObj, TYPE_REF);
    	}
    }
    
    @JsonIgnore
    public <T> T retriveExtendAttr(String attrKey, Class<T> attrValClazz) {
    	Object attrVal = null;
    	if (extendAttrMap == null || (attrVal = extendAttrMap.get(attrKey)) == null) {
    		return null;
    	}
    	if (attrValClazz.isInstance(attrVal)) {
    		return attrValClazz.cast(attrVal);
    	}
    	return JsonUtil.jacksonConvert(attrVal, attrValClazz);
    }
} 
