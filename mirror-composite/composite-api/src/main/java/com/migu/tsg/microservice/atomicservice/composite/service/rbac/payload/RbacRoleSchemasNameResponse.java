package com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
* RbacRoleSchemasResponse类
* Project Name:composite-api
* File Name:RbacRoleSchemasResponse.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload
* ClassName: RbacRoleSchemasResponse <br/>
* date: 2017年9月1日 下午12:34:43 <br/>
* @author yangshilei
* @version
* @since JDK 1.6
*/
@NoArgsConstructor
@Data
@JsonInclude(Include.NON_NULL)
public class RbacRoleSchemasNameResponse {

    private String resource;

    private Boolean general;

    /** 
     * 资源名称
     */ 
    private String name;
    
    /** 
     * 父资源O
     */ 
    private String parentResource;
    
    private Map<String, String> actions;
    
    private final List<RbacRoleSchemasNameResponse> childList = new ArrayList<>();
 
    public void replaceChildList(List<RbacRoleSchemasNameResponse> childList) {
    	if (CollectionUtils.isNotEmpty(childList)) {
    		this.childList.clear();
    		this.childList.addAll(childList);
    	}
    }
    
    public void addChildItem(RbacRoleSchemasNameResponse ... childItem) {
    	this.childList.addAll(Arrays.asList(childItem));
    }
}
