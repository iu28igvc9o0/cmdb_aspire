package com.migu.tsg.microservice.atomicservice.rbac.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.Permission;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dao <br>
 * 类名称: PermissionsDao.java <br>
 * 类描述: 权限DAO接口 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月24日下午4:17:43<br>
 * 版本: v1.0
 */
@Mapper
public interface PermissionDao {

    /**
     * 查询权限列表
     * @param roleUuid 角色UUID
     * @return Permission集合
     */
    public List<Permission> fetchPermissionList(String roleUuid);

    /**
     * 查询权限列表
     * @param roleUuidList 角色UUID集合
     * @return Permission集合
     */
    public List<Permission> listOfPermission(@Param("roleUuidList") List<String> roleUuidList);
    
    /**
     * 根据ids、roleType查询权限列表
     * @param roleUuidList 角色UUID集合
     * @return Permission集合
     */
    public List<Permission> listOfPermissionByIdsAndType(@Param("roleType")String roleType,@Param("roleUuidList") List<String> roleUuidList);

    /**
     * 删除角色对应的一个或者多个权限
     * @param roleUuid 角色UUID
     * @return 影响行数
     */
    public int deletePermissionsByRoleUuid(String roleUuid);

    /**
     * 删除角色对应的某个权限
     * @param uuid 权限UUID
     * @param roleUuid 角色UUID
     * @return 影响行数
     */
    public int deletePermissionsByUuid(@Param("uuid") String uuid, @Param("roleUuid") String roleUuid);

    /**
     * 创建一个权限信息
     * @param permission 权限对象
     * @return 影响行数
     */
    public int createPermission(Permission permission);

    /**
     * 批量创建权限信息
     * @param permissionList 权限对象集合
     * @return 影响行数
     */
    public int createPermissionBatch(@Param("permissionList") List<Permission> permissionList);

    /**
     * 查询权限是否存在
     * @param uuid 权限UUID
     * @return 大于0则存在,否则不存在
     */
    public int hasFetchPermissionByUuid(String uuid);

    /**
     * 根据UUID更改权限
     * @param permission 权限对象
     * @return 影响行数
     */
    public int updatePermissionByUuid(Permission permission);

}
