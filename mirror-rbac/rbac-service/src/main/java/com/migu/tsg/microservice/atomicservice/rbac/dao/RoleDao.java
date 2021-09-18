package com.migu.tsg.microservice.atomicservice.rbac.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.common.entity.Page;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.Role;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ListRolesResponse;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dao <br>
 * 类名称: RoleDao.java <br>
 * 类描述: 角色DAO接口 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月21日上午11:03:31<br>
 * 版本: v1.0
 */
@Mapper
public interface RoleDao {

    /**
     * 查询角色列表
     * @param namespace 空间名称/根账号
     * @param roleNames 角色名称查询条件集合
     * @return 角色集合
     */
    public List<Role> fetchRoleList(@Param("namespace") String namespace,
            @Param("roleNames") String[] roleNames);

    /**
     * 查询对于给定的UUID的角色列表
     * @param uuids 角色UUID集合,必填
     * @param username 用户名称,通过相关的用户名过滤角色列表
     * @return 角色集合
     */
    public List<Role> listRoles(@Param("uuids") String[] uuids, @Param("name") String name,
    		@Param("namespace") String namespace, @Param("roleType") Integer roleType,
    		 @Param("username") String username);

    

    /**
    * 根据page对象查询数量
    * @param page
    * @return 条数
    */
    int pageListCount(Page page);

    /**
    * 根据page对象查询监控项列表
    * @param page
    * @return 监控项列表
    */
    List<ListRolesResponse> pageList(Page page);
    
    /**
     * 查询子级角色列表
     * @param uuids 角色UUID集合,必填
     * @param username 用户名称,通过相关的用户名过滤角色列表
     * @return 角色集合
     */
    public List<Role> childListRoles(@Param("uuids") String[] uuids, @Param("name") String name,
    		@Param("namespace") String namespace, @Param("roleType") Integer roleType);

    /**
     * 查询单个角色详细信息
     * @param namespace 空间名
     * @param roleUuidOrName 角色UUID或角色名
     * @return 角色对象
     */
    public Role fetchRoleDetail(@Param("namespace") String namespace,@Param("roleUuid") String roleUuid,
            @Param("roleUuidOrName") String roleUuidOrName);

    /**
     * 查询单个角色是否存在
     * @param namespace 空间名
     * @param roleUuidOrName 角色UUID或角色名
     * @return 大于0存在,反之不存在
     */
    public int hasFetchRole(@Param("namespace") String namespace,
            @Param("roleUuidOrName") String roleUuidOrName);

    /**
     * 删除单个角色信息
     * @param namespace 空间名称/根账号
     * @param roleUuidOrName 角色UUID或角色名
     * @return 影响行数
     */
    public int deleteRole(@Param("namespace") String namespace,
            @Param("roleUuidOrName") String roleUuidOrName);

    /**
     * 删除单个角色信息
     * @param roleUuid 角色UUID
     * @return 影响行数
     */
    public int deleteRoleByUuid(String roleUuid);

    /**
     * 新增单个角色信息
     * @param role 角色对象
     * @return 影响行数
     */
    public int createRole(Role role);

    /**
     * 批量新增角色
     * @param roleList 角色集合
     * @return 影响行数
     */
    public int createRoleBatch(@Param("roleList") List<Role> roleList);

    /**
     * 修改单个角色信息
     * @param role 角色对象
     * @return 影响行数
     */
    public int modifyRole(Role role);

    /**
     * 根据角色UUID查询对应角色信息
     * @param uuid 角色UUID
     * @return 对象
     */
    public Role getRoleByUUID(String uuid);

    /**
     * 查询是否存在当前角色
     * @param uuid 角色UUID
     * @return 大于0则存在,小于等于0则不存在
     */
    public int hasGetRoleByUUID(String uuid);

}
