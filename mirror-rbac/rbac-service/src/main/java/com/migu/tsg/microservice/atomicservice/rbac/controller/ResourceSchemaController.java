package com.migu.tsg.microservice.atomicservice.rbac.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.migu.tsg.microservice.atomicservice.common.annotation.ResultCode;
import com.migu.tsg.microservice.atomicservice.rbac.biz.ResourceSchemaBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dto.FetchResourceSchemaDetailResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ResourceSchemaDTO;
import com.migu.tsg.microservice.atomicservice.rbac.service.ResourceSchemaService;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.controller <br>
 * 类名称: ResourceSchemaController.java <br>
 * 类描述: 【RBAC原子层】资源模式控制层 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月21日上午11:19:59 <br>
 * 版本: v1.0
 */
@RestController
public class ResourceSchemaController implements ResourceSchemaService {

    @Autowired
    private ResourceSchemaBiz resourceSchemaBiz;

    /**
     * 获取整个资源模式(资源类型,资源操作,资源约束)的完整列表
     * @return 响应参数
     */
    @ResultCode("105010301")
    public List<FetchResourceSchemaDetailResponse> fetchRoleSchemaList() {
        List<FetchResourceSchemaDetailResponse> response = new ArrayList<>();
        List<ResourceSchemaDTO> fetchRoleSchemaList = resourceSchemaBiz.fetchRoleSchemaList();
        if (CollectionUtils.isNotEmpty(fetchRoleSchemaList)) {
            for (ResourceSchemaDTO resourceSchemaDTO : fetchRoleSchemaList) {
                FetchResourceSchemaDetailResponse fetchRoleSchemaListResponse = new FetchResourceSchemaDetailResponse();
                BeanUtils.copyProperties(resourceSchemaDTO, fetchRoleSchemaListResponse);
                response.add(fetchRoleSchemaListResponse);
            }
        }
        return response;
    }

    /**
     * 根据资源类型获取单个资源模式(资源类型,资源操作,资源约束)信息
     * @param resourceType 资源类型
     * @return 响应参数
     */
    @ResultCode("105010302")
    public FetchResourceSchemaDetailResponse fetchRoleSchemaDetail(
            @PathVariable("resource_type") final String resourceType) {
        ResourceSchemaDTO dto = resourceSchemaBiz.fetchRoleSchemaDetail(resourceType,"0");
        FetchResourceSchemaDetailResponse response = new FetchResourceSchemaDetailResponse();
        BeanUtils.copyProperties(dto, response);
        return response;
    }
    
    @ResultCode("105010303")
    public List<FetchResourceSchemaDetailResponse> fetchChildrenRoleSchemaList(@RequestParam(value="id", required=false) String id) {
        List<ResourceSchemaDTO> fetchRoleSchemaList = resourceSchemaBiz.listChildrenResourceSchema(id);
        return parseByCopyProperties(fetchRoleSchemaList, FetchResourceSchemaDetailResponse.class);
    }
    
    @ResultCode("105010304")
    public List<FetchResourceSchemaDetailResponse> fetchFullChildrenRoleSchemaList(@RequestParam(value="id", required=false) String id) {
    	List<ResourceSchemaDTO> fetchRoleSchemaList = resourceSchemaBiz.listChildrenResourceSchema(id, true);
    	return parseByCopyProperties(fetchRoleSchemaList, FetchResourceSchemaDetailResponse.class);
    }
    
    private <S, T> List<T>  parseByCopyProperties(List<S> sourceList, Class<T> targetClazz) {
    	List<T> resultList = new ArrayList<>();
    	if (CollectionUtils.isNotEmpty(sourceList)) {
            for (S source : sourceList) {
            	try {
            		T target = targetClazz.newInstance();
            		BeanUtils.copyProperties(source, target);
            		resultList.add(target);
            	} catch (Exception e) {
            		throw new RuntimeException(e);
            	}
            }
        }
    	return resultList;
    }
}
