package com.migu.tsg.microservice.atomicservice.rbac.biz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.migu.tsg.microservice.atomicservice.common.aspect.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.common.aspect.RequestAuthContext.RequestHeadUser;
import com.migu.tsg.microservice.atomicservice.rbac.cache.CacheBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.ResourceSchemaDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchema;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchemaActions;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchemaConstraints;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.GetRoleDetailDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ResourceSchemaDTO;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.biz <br>
 * 类名称: ResourceSchemaBiz.java <br>
 * 类描述: 【RBAC原子层】资源模式业务层 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月21日上午11:19:59 <br>
 * 版本: v1.0
 */
@Service
@Transactional(readOnly = true)
public class ResourceSchemaBiz {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceSchemaBiz.class);

    @Autowired
    private CacheBiz cacheBiz;
    @Autowired
	private RoleBiz	 roleBiz;
    @Autowired
    private ResourceSchemaDao resSchemaDao;

    /**
     * 获取整个资源模式(资源类型,资源操作,资源约束)的完整列表
     */
    public List<ResourceSchemaDTO> fetchRoleSchemaList() {
        List<ResourceSchema> fetchRoleSchemaList = cacheBiz.listResourceSchema();
        List<ResourceSchemaDTO> dto = new ArrayList<>();
        for (ResourceSchema roleSchema : fetchRoleSchemaList) {
            dto.add(fromResourceSchemaPo(roleSchema));
        }
        LOGGER.info("method[fetchRoleSchemaList] Query all resource schema success");
        return dto;
    }
    
    /**
     * 获取子级资源模式
     */
    public List<ResourceSchemaDTO> listChildrenResourceSchema(String parentResource) {
        List<ResourceSchema> fetchRoleSchemaList = cacheBiz.listChildrenResourceSchema(parentResource);
        return popupReferInfo(fetchRoleSchemaList);
    }
    
    /** 
     * 功能描述: 获取子ResourceSchema, 通过recurseFlag指定是否需要递归子孙节点
     * <p>
     * @param parentResource
     * @param recurseFlag
     * @return
     */
    public List<ResourceSchemaDTO> listChildrenResourceSchema(String parentResource, boolean recurseFlag) {
    	if (!recurseFlag) {
    		return listChildrenResourceSchema(parentResource);
    	}
    	
        List<ResourceSchema> fetchRoleSchemaList = cacheBiz.listChildrenResourceSchema(parentResource, recurseFlag);
        
        // 如果为管理员或者用户信息不存在(为了调试方便，当用户信息不存在时,也认为是管理员)
        RequestHeadUser currUser = RequestAuthContext.currentRequestAuthContext().getUser();
        if (currUser == null || currUser.isAdmin() || currUser.isSuperUser()) {
        	return popupReferInfo(fetchRoleSchemaList);
        }
        
        // 如果为非管理员, 则只返回角色有权限的schema
        String[] roleUuidArr = roleBiz.getOperRoleIdByUserName(currUser.getNamespace(), currUser.getUsername());
        if (roleUuidArr.length == 0) {
        	return new ArrayList<ResourceSchemaDTO>();
        }
        
        Set<String> authedSchemaList = new HashSet<>();
        for (String roleUuid : roleUuidArr) {
        	final GetRoleDetailDTO roleDetail = roleBiz.getRoleDetail(roleUuid);
        	// 如果包含管理角色, 返回所有功能点
        	if (roleDetail.getAdminRole()) {
        		return popupReferInfo(fetchRoleSchemaList);
        	}
        	roleDetail.getPermissions().stream().forEach(p -> {
        		authedSchemaList.addAll(p.getResource());
        	});
        }
        
        final Set<ResourceSchema> resultSet = new HashSet<>();
        recurseLoadParentResSchema(resultSet, fetchRoleSchemaList, new ArrayList<String>(authedSchemaList));
        return popupReferInfo(new ArrayList<>(resultSet));
    }
    
    /** 
     * 功能描述: 递归添加父节点  
     * <p>
     * @param resultSet
     * @param fetchRoleSchemaList
     * @param childSchemaIdList
     */
    private void recurseLoadParentResSchema(final Set<ResourceSchema> resultSet, 
    				final List<ResourceSchema> fetchRoleSchemaList, final List<String> childSchemaIdList) {
    	if (CollectionUtils.isEmpty(childSchemaIdList)) {
    		return;
    	}
    	
    	Set<ResourceSchema> matchChildList = fetchRoleSchemaList.stream().filter(
    			item -> childSchemaIdList.contains(item.getResource())).collect(Collectors.toSet());
    	
    	if (CollectionUtils.isNotEmpty(matchChildList)) {
    		resultSet.addAll(matchChildList);
    		Set<String> parentIdList = matchChildList.stream().map(item -> item.getParentResource()).collect(Collectors.toSet());
    		recurseLoadParentResSchema(resultSet, fetchRoleSchemaList, new ArrayList<String>(parentIdList));
    	}
    }
    
    private List<ResourceSchemaDTO> popupReferInfo(List<ResourceSchema> fetchRoleSchemaList) {
    	List<ResourceSchemaDTO> resultList = new ArrayList<>();
        for (ResourceSchema roleSchema : fetchRoleSchemaList) {
            resultList.add(fromResourceSchemaPo(roleSchema));
        }
        return resultList;
    }

    /**
     * 根据资源类型获取单个资源模式(资源类型,资源操作,资源约束)信息
     * 
     * @param resourceType
     *            资源类型
     * @return
     */
    public ResourceSchemaDTO fetchRoleSchemaDetail(final String resourceType,String actionType) {
        ResourceSchema roleSchema = cacheBiz.getResourceSchema(resourceType,actionType);
        ResourceSchemaDTO fromResourceSchemaPo = fromResourceSchemaPo(roleSchema);
        LOGGER.info("method[fetchRoleSchemaDetail] Query resource schema {} success", resourceType);
        return fromResourceSchemaPo;
    }

    /**
     * 封装ResourceSchemaDTO
     * @param resourceSchema PO Entity
     * @return DTO对象
     */
    private ResourceSchemaDTO fromResourceSchemaPo(final ResourceSchema resourceSchema) {
        ResourceSchemaDTO resourceSchemaDTO = new ResourceSchemaDTO();
    	if(resourceSchema==null) {
    		return resourceSchemaDTO;
    	}
        resourceSchemaDTO.setGeneral("t".equals(resourceSchema.getGeneral()) ? true : false);
        resourceSchemaDTO.setResource(resourceSchema.getResource());
        resourceSchemaDTO.setName(resourceSchema.getName());
        resourceSchemaDTO.setParentResource(resourceSchema.getParentResource());
        List<ResourceSchemaActions> actions = resourceSchema.getActions();
        Map<String, String> dtoActions = new HashMap<>();
        // 封装资源操作
        if (CollectionUtils.isNotEmpty(actions)) {
            for (ResourceSchemaActions resourceSchemaActions : actions) {
                String action = resourceSchemaActions.getAction();
                if(action!=null && resourceSchemaActions.getActionType().equals("0")) {
    				dtoActions.put(action,resourceSchemaActions.getActionName());
                }
            }
        }
        resourceSchemaDTO.setActions(dtoActions);
        List<ResourceSchemaConstraints> constraints = resourceSchema.getConstraints();
        Map<String, String> constrMap = new HashMap<>();
        // 封装资源约束
        if (CollectionUtils.isNotEmpty(actions)) {
            for (ResourceSchemaConstraints resourceSchemaConstraints : constraints) {
                if (resourceSchemaConstraints.getConstKey() != null
                        && resourceSchemaConstraints.getConstValue() != null) {
                    constrMap.put(resourceSchemaConstraints.getConstKey(),
                            resourceSchemaConstraints.getConstValue());
                }
            }
        }
        resourceSchemaDTO.setConstraints(constrMap);
        return resourceSchemaDTO;
    }

}
