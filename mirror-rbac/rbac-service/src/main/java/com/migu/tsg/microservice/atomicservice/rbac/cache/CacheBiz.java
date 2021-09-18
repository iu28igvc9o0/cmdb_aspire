/**
 * 
 */
package com.migu.tsg.microservice.atomicservice.rbac.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.migu.tsg.microservice.atomicservice.common.config.RedisProperties;
import com.migu.tsg.microservice.atomicservice.common.enums.BadRequestFieldMessageEnum;
import com.migu.tsg.microservice.atomicservice.common.enums.ResultErrorEnum;
import com.migu.tsg.microservice.atomicservice.common.exception.BadRequestFieldException;
import com.migu.tsg.microservice.atomicservice.common.helper.RedisCacheHelper;
import com.migu.tsg.microservice.atomicservice.rbac.dao.PermissionDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.ResourceSchemaDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.RoleParentDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.RoleUsersDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ParentRole;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.Permission;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchema;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.RoleUsers;

/**
* 项目名称: rbac-service <br>
* 包: com.migu.tsg.microservice.atomicservice.rbac.cache <br>
* 类名称: CacheBiz.java <br>
* 类描述: 缓存业务层<br>
* 创建人: WangSheng <br>
* 创建时间: 2017年11月30日下午9:22:04 <br>
* 版本: v1.0
*/
@Component
@EnableConfigurationProperties(RedisProperties.class)
public class CacheBiz implements CommandLineRunner {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheBiz.class);

    @Autowired
    private RedisProperties redisProperties;

    @Autowired
    private RoleParentDao roleParentDao;

    @Autowired
    private RoleUsersDao roleUsersDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private ResourceSchemaDao resourceSchemaDao;

    @Autowired
    private RedisCacheHelper redisCacheHelper;

    /**
     * 根据资源类型获取单个资源模式(资源类型,资源操作,资源约束)信息
     * @param resourceType 资源类型
     * @return 单个资源模式(资源类型,资源操作,资源约束)信息
     */
    public ResourceSchema getResourceSchema(final String resourceType,final String actionType) {
        validateParameterResourceType(resourceType);
        String key = redisProperties.getRedisKeyPrefixResourceSchemaHash()+actionType;
        String hashKey = resourceType;
        if (redisProperties.getUsable()) {
            // 读取Hash缓存
            if (redisCacheHelper.hasKey(key)) {
                if (redisCacheHelper.hasKey(key, hashKey)) {
                    Object object = redisCacheHelper.get(key, hashKey);
                    if (object instanceof ResourceSchema) {
                        LOGGER.info("method[getResourceSchema] Query resource schema {} success from Redis",
                                resourceType);
                        return (ResourceSchema) object;
                    }
                }
            }
        }
        // 读取数据库
        // 根据资源类型获取单个资源模式(资源类型,资源操作,资源约束)信息
        ResourceSchema resourceSchemaDetail = resourceSchemaDao.fetchResourceSchemaDetail(resourceType,actionType);
       
        LOGGER.info("method[getResourceSchema] Query resource schema {} success from DB", resourceType);
        if (redisProperties.getUsable()) {
            // 写入Hash缓存
            if (redisCacheHelper.put(key, hashKey, resourceSchemaDetail)) {
                LOGGER.info("method[getResourceSchema] Put cache success, Key : {}, HashKey : {}", key,
                        hashKey);
            } else {
                LOGGER.warn("method[getResourceSchema] Put cache fail, Key : {}, HashKey : {}", key, hashKey);
            }
        }
        return resourceSchemaDetail;
    }

    /**
     * 获取整个资源模式(资源类型,资源操作,资源约束)的完整列表
     * @return 资源模式集合
     */
    public List<ResourceSchema> listResourceSchema() {
        String key = redisProperties.getRedisKeyPrefixResourceSchemaList();
        if (redisProperties.getUsable()) {
            // 读取List缓存
            if (redisCacheHelper.hasKey(key)) {
                List<Object> list = redisCacheHelper.range(key, 0, -1);
                if (CollectionUtils.isNotEmpty(list)) {
                    LOGGER.info("method[listResourceSchema] Query resource schema list success from Redis");
                    return list.stream().distinct().map(object -> {
                        if (object instanceof ResourceSchema) {
                            return (ResourceSchema) object;
                        }
                        return null;
                    }).collect(Collectors.toList());
                }
            }
        }
        // 读取数据库
        // 获取整个资源模式(资源类型,资源操作,资源约束)的完整列表
        List<ResourceSchema> fetchResourceSchemaList = resourceSchemaDao.fetchResourceSchemaList();
        if (CollectionUtils.isEmpty(fetchResourceSchemaList)) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "",
                    new String[] { BadRequestFieldMessageEnum
                            .dynamicMessage(BadRequestFieldMessageEnum.RESOURCE_SCHEMA_NOT_FOUND, "all") });
        }
        LOGGER.info("method[getResourceSchema] Query resource schema list success from DB");
        if (redisProperties.getUsable()) {
            if (redisCacheHelper.hasKey(key)) {
                redisCacheHelper.delete(key);
            }
            for (ResourceSchema resourceSchema : fetchResourceSchemaList) {
                // 写入List缓存
                if (redisCacheHelper.push(key, resourceSchema)) {
                    LOGGER.info("method[listResourceSchema] Push cache success, Key : {}, Resource : {}", key,
                            resourceSchema.getResource());
                } else {
                    LOGGER.warn("method[listResourceSchema] Push cache fail, Key : {}, Resource : {}", key,
                            resourceSchema.getResource());
                }
            }
        }
        return fetchResourceSchemaList;
    }
    /**
     * 获取整个资源模式(资源类型,资源操作,资源约束)的完整列表
     * @return 资源模式集合
     */
    public List<ResourceSchema> listChildrenResourceSchema(String parentResource) {
    	String split = redisProperties.getRedisKeySplit();
        String key = redisProperties.getRedisKeyPrefixResourceSchemaList()+split+parentResource;
        if (redisProperties.getUsable()) {
            // 读取List缓存
            if (redisCacheHelper.hasKey(key)) {
                List<Object> list = redisCacheHelper.range(key, 0, -1);
                if (CollectionUtils.isNotEmpty(list)) {
                    LOGGER.info("method[listResourceSchema] Query resource schema list success from Redis");
                    return list.stream().distinct().map(object -> {
                        if (object instanceof ResourceSchema) {
                            return (ResourceSchema) object;
                        }
                        return null;
                    }).collect(Collectors.toList());
                }
            }
        }
        // 读取数据库
        // 获取整个资源模式(资源类型,资源操作,资源约束)的完整列表
        List<ResourceSchema> fetchResourceSchemaList = resourceSchemaDao.fetchChildrenResourceSchemaList(parentResource);
//        if (CollectionUtils.isEmpty(fetchResourceSchemaList)) {
//            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "",
//                    new String[] { BadRequestFieldMessageEnum
//                            .dynamicMessage(BadRequestFieldMessageEnum.RESOURCE_SCHEMA_NOT_FOUND, "all") });
//        }
        LOGGER.info("method[getResourceSchema] Query resource schema list success from DB");
        if (redisProperties.getUsable()) {
            if (redisCacheHelper.hasKey(key)) {
                redisCacheHelper.delete(key);
            }
            for (ResourceSchema resourceSchema : fetchResourceSchemaList) {
                // 写入List缓存
                if (redisCacheHelper.push(key, resourceSchema)) {
                    LOGGER.info("method[listResourceSchema] Push cache success, Key : {}, Resource : {}", key,
                            resourceSchema.getResource());
                } else {
                    LOGGER.warn("method[listResourceSchema] Push cache fail, Key : {}, Resource : {}", key,
                            resourceSchema.getResource());
                }
            }
        }
        return fetchResourceSchemaList;
    }
    
    public List<ResourceSchema> listChildrenResourceSchema(String parentResource, boolean recurseFlag) {
    	if (!recurseFlag) {
    		return listChildrenResourceSchema(parentResource);
    	}
    	
    	List<ResourceSchema> wholeResSchemaList = resourceSchemaDao.fetchResourceSchemaList();
    	if (StringUtils.isBlank(parentResource)) {
    		return wholeResSchemaList;
    	}
    	
    	final List<ResourceSchema> resultList = new ArrayList<>();
    	Optional<ResourceSchema> matchParent 
    		= wholeResSchemaList.stream().filter(item -> parentResource.equals(item.getResource())).findFirst();
    	if (!matchParent.isPresent()) {
    		return resultList;
    	}
    	
    	recurseAppendChildren(resultList, wholeResSchemaList, Collections.singletonList(parentResource));
    	return resultList;
    }
    
    private void recurseAppendChildren(
    		final List<ResourceSchema> resultList, final List<ResourceSchema> wholeResSchemaList, final List<String> parentList) {
    	List<ResourceSchema> childList = wholeResSchemaList.stream().filter(
    			item -> parentList.contains(item.getParentResource())).collect(Collectors.toList());
    	if (!childList.isEmpty()) {
    		resultList.addAll(childList);
    		List<String> subParentIdList = childList.stream().map(item -> item.getResource()).collect(Collectors.toList());
    		recurseAppendChildren(resultList, wholeResSchemaList, subParentIdList);
    	}
    }

    /**
     * 根据用户名称和空间名称获取对应的角色权限
     * @param username 用户名称
     * @param namespace 空间名称
     * @return 权限集合
     */
    public List<Permission> listOfPermissionForParentRoleUuid(final String username, final String namespace) {
        String split = redisProperties.getRedisKeySplit();
        String key = redisProperties.getRedisKeyPrefixPermission() + split + namespace + split + username;
        if (redisProperties.getUsable()) {
            // 读取Hash缓存
            if (redisCacheHelper.hasKey(key)) {
                List<Object> values = redisCacheHelper.values(key);
                if (values != null) {
                    List<Permission> collect = values.stream().map(value -> {
                        if (value instanceof Permission) {
                            return (Permission) value;
                        }
                        return null;
                    }).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(collect)) {
                        LOGGER.info(
                                "method[listOfPermissionForParentRoleUuid] Query {} of {} permissions success from Redis",
                                username, namespace);
                        return collect;
                    }
                }
            }
        }
        // 读取数据库
        if (StringUtils.isBlank(username) || StringUtils.isBlank(namespace)) {
            LOGGER.info("method[listOfPermissionForParentRoleUuid] Query {} of {} permissions fail", username,
                    namespace);
            return new ArrayList<>();
        }
        List<RoleUsers> listOfRoleUsers = roleUsersDao.listOfRoleUsers(username, namespace);
        if (CollectionUtils.isNotEmpty(listOfRoleUsers)) {
            List<String> roleUuidList = listOfRoleUsers.stream().map(RoleUsers::getRoleUuid).distinct()
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(roleUuidList)) {
                List<ParentRole> listOfParentRoleUuid = roleParentDao.listOfParentRoleUuid(roleUuidList);
                if (CollectionUtils.isNotEmpty(listOfParentRoleUuid)) {
                    List<String> roleUuids = listOfParentRoleUuid.stream().flatMap(parentRole -> {
                        List<String> uuids = new ArrayList<>();
                        uuids.add(parentRole.getRoleUuid());
                        uuids.add(parentRole.getParentUuid());
                        uuids.add(parentRole.getGrandParentUuid());
                        uuids.add(parentRole.getGreatGrandUuid());// 最多4层
                        return uuids.stream();
                    }).filter(uuid -> StringUtils.isNotBlank(uuid)).distinct().collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(roleUuids)) {
                        List<Permission> listOfPermission = permissionDao.listOfPermission(roleUuids);
                        LOGGER.info(
                                "method[listOfPermissionForParentRoleUuid] Query {} of {} permissions success from DB",
                                username, namespace);
                        if (redisProperties.getUsable()) {
                            // 写入Hash缓存
                            for (Permission permission : listOfPermission) {
                                String hashKey = permission.getUuid();
                                //checkstyle最多只能嵌套4层，此处嵌套5层了
                                if (redisCacheHelper.put(key, hashKey, permission)) {
                                    LOGGER.info(
                                            "method[listOfPermissionForParentRoleUuid] Put cache success, Key : {}, HashKey : {}",
                                            key, hashKey);
                                } else {
                                    LOGGER.warn(
                                            "method[listOfPermissionForParentRoleUuid] Put cache fail, Key : {}, HashKey : {}",
                                            key, hashKey);
                                }
                            }
                        }
                        return listOfPermission;
                    }
                    LOGGER.info("method[listOfPermissionForParentRoleUuid] Query {} of {} no permissions",
                            username, namespace);
                }
            }
        }
        LOGGER.info("method[listOfPermissionForParentRoleUuid] Query {} of {} no roles", username, namespace);
        return new ArrayList<>();
    }

    /**
     * 根据用户名称和空间名称获取对应的角色权限
     * @param username 用户名称
     * @param namespace 空间名称
     * @return 权限集合
     */
    public List<Permission> listOfPermissionForParentRoleUuid(final String username, final String namespace,final String roleType) {
        String split = redisProperties.getRedisKeySplit();
        String key = redisProperties.getRedisKeyPrefixPermission() + split + namespace + split + username+ split + roleType;
        if (redisProperties.getUsable()) {
            // 读取Hash缓存
            if (redisCacheHelper.hasKey(key)) {
                List<Object> values = redisCacheHelper.values(key);
                if (values != null) {
                    List<Permission> collect = values.stream().map(value -> {
                        if (value instanceof Permission) {
                            return (Permission) value;
                        }
                        return null;
                    }).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(collect)) {
                        LOGGER.info(
                                "method[listOfPermissionForParentRoleUuid] Query {} of {} permissions success from Redis",
                                username, namespace);
                        return collect;
                    }
                }
            }
        }
        // 读取数据库
        if (StringUtils.isBlank(username) || StringUtils.isBlank(namespace)) {
            LOGGER.info("method[listOfPermissionForParentRoleUuid] Query {} of {} permissions fail", username,
                    namespace);
            return new ArrayList<>();
        }
        List<RoleUsers> listOfRoleUsers = roleUsersDao.listOfRoleUsers(username, namespace);
        if (CollectionUtils.isNotEmpty(listOfRoleUsers)) {
            List<String> roleUuidList = listOfRoleUsers.stream().map(RoleUsers::getRoleUuid).distinct()
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(roleUuidList)) {
                List<ParentRole> listOfParentRoleUuid = roleParentDao.listOfParentRoleUuid(roleUuidList);
                if (CollectionUtils.isNotEmpty(listOfParentRoleUuid)) {
                    List<String> roleUuids = listOfParentRoleUuid.stream().flatMap(parentRole -> {
                        List<String> uuids = new ArrayList<>();
                        uuids.add(parentRole.getRoleUuid());
                        uuids.add(parentRole.getParentUuid());
                        uuids.add(parentRole.getGrandParentUuid());
                        uuids.add(parentRole.getGreatGrandUuid());// 最多4层
                        return uuids.stream();
                    }).filter(uuid -> StringUtils.isNotBlank(uuid)).distinct().collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(roleUuids)) {
                        List<Permission> listOfPermission = permissionDao.listOfPermissionByIdsAndType(roleType, roleUuids);
                        LOGGER.info(
                                "method[listOfPermissionForParentRoleUuid] Query {} of {} permissions success from DB",
                                username, namespace);
                        if (redisProperties.getUsable()) {
                            // 写入Hash缓存
                            for (Permission permission : listOfPermission) {
                                String hashKey = permission.getUuid();
                                //checkstyle最多只能嵌套4层，此处嵌套5层了
                                if (redisCacheHelper.put(key, hashKey, permission)) {
                                    LOGGER.info(
                                            "method[listOfPermissionForParentRoleUuid] Put cache success, Key : {}, HashKey : {}",
                                            key, hashKey);
                                } else {
                                    LOGGER.warn(
                                            "method[listOfPermissionForParentRoleUuid] Put cache fail, Key : {}, HashKey : {}",
                                            key, hashKey);
                                }
                            }
                        }
                        return listOfPermission;
                    }
                    LOGGER.info("method[listOfPermissionForParentRoleUuid] Query {} of {} no permissions",
                            username, namespace);
                }
            }
        }
        LOGGER.info("method[listOfPermissionForParentRoleUuid] Query {} of {} no roles", username, namespace);
        return new ArrayList<>();
    }

    /**
     * 验证resourceType请求参数
     * @param resourceType 资源类型
     * @throws BadRequestFieldException 请求字段不合法的异常
     */
    private void validateParameterResourceType(final String resourceType) throws BadRequestFieldException {
        if (StringUtils.isBlank(resourceType)) {
            throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "resource_type",
                    new String[] { BadRequestFieldMessageEnum.RESOURCE_TYPE_CANNOT_BE_EMPTY.getMessage() });
        }
    }

    /**
     * 初始化字典表数据写入缓存
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        if (redisProperties.getUsable()) {
            String key = redisProperties.getRedisKeyPrefixResourceSchemaHash();
            String listKey = redisProperties.getRedisKeyPrefixResourceSchemaList();
            // 读取数据库
            // 获取整个资源模式(资源类型,资源操作,资源约束)的完整列表
            List<ResourceSchema> fetchResourceSchemaList = resourceSchemaDao.fetchResourceSchemaList();
            if (CollectionUtils.isEmpty(fetchResourceSchemaList)) {
                LOGGER.info(
                        "Initialization all resource schema fail to Redis, Resource schema not found from DB");
                throw new BadRequestFieldException(ResultErrorEnum.INVALID_ARGS, "",
                        new String[] { BadRequestFieldMessageEnum.dynamicMessage(
                                BadRequestFieldMessageEnum.RESOURCE_SCHEMA_NOT_FOUND, "all") });
            }
            // 写入Hash缓存
            for (ResourceSchema resourceSchema : fetchResourceSchemaList) {
                String hashKey = resourceSchema.getResource();
                if (redisCacheHelper.put(key, hashKey, resourceSchema)) {
                    LOGGER.info("Initialization Put cache success from DB to Redis, Key : {}, HashKey : {}",
                            key, hashKey);
                } else {
                    LOGGER.info("Initialization Put cache fail from DB to Redis, Key : {}, HashKey : {}", key,
                            hashKey);
                }
            }
            if (redisCacheHelper.hasKey(listKey)) {
                redisCacheHelper.delete(listKey);
            }
            for (ResourceSchema resourceSchema : fetchResourceSchemaList) {
                // 写入List缓存
                if (redisCacheHelper.push(listKey, resourceSchema)) {
                    LOGGER.info("method[listResourceSchema] Push cache success, Key : {}, Resource : {}",
                            listKey, resourceSchema.getResource());
                } else {
                    LOGGER.warn("method[listResourceSchema] Push cache fail, Key : {}, Resource : {}",
                            listKey, resourceSchema.getResource());
                }
            }
        }
    }

}
