package com.migu.tsg.microservice.atomicservice.rbac.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.migu.tsg.microservice.atomicservice.rbac.dao.po.RoleUsers;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dao <br>
 * 类名称: RoleUsersDao.java <br>
 * 类描述: 一个角色对应多个成员对象的中间表的DAO接口 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年8月7日上午10:39:27 <br>
 * 版本: v1.0
 */
@Mapper
public interface RoleUsersDao {

    /**
     * 查询指定角色的成员集合
     * @param roleUuid 角色uuid
     * @return 指定角色的成员集合
     */
    public List<RoleUsers> fetchRoleUsersList(String roleUuid);

    /**
     * 根据用户名和空间名称获取对象
     * @param username 用户名
     * @param namespace 空间名称
     * @return 集合
     */
    public List<RoleUsers> listOfRoleUsers(@Param("username") String username,
            @Param("namespace") String namespace);

    /**
     * 查询已经分配给用户的角色列表OR已经分配给角色的用户列表
     * @param namespace 空间名称
     * @param user 用户名称
     * @param roleUuid 角色UUID
     * @return 已分配的角色集合
     */
    public List<RoleUsers> fetchRolesAssignedList(@Param("namespace") String namespace,
            @Param("user") String user, @Param("roleUuid") String[] roleUuid);

    /**
     * 查询指定空间中成员名称是否已存在
     * @param roleUuid 角色UUID
     * @param namespace 空间名
     * @param username 成员名称
     * @return 大于0则已存在,反之不存在
     */
    public int hasFetchRoleUsersList(@Param("roleUuid") String roleUuid, @Param("namespace") String namespace,
            @Param("username") String username);
    

    /**
     * <p>
     * 判断是否有管理员角色
     * </p>
     * @author 曾祥华
     * @version 0.1 2019年4月12日
     * @param username
     * @return
     * int
     */
    public int hasAdminRole(@Param("username") String username, @Param("namespace") String namespace);

    /**
     * 新增角色和成员的关联关系
     * @param roleUsers 角色成员对象
     * @return 影响行数
     */
    public int createRoleUsers(RoleUsers roleUsers);

    /**
     * 批量新增角色和成员的关联关系
     * @param roleUsersList 角色成员对象集合
     * @return 影响行数
     */
    public int createRoleUsersBatch(@Param("roleUsersList") List<RoleUsers> roleUsersList);

    /**
     * 删除角色和成员的关联关系
     * @param roleUuid 角色UUID
     * @param namespace 空间名
     * @param username 成员名称
     * @return 影响行数
     */
    public int deleteRoleUsers(@Param("roleUuid") String roleUuid, @Param("namespace") String namespace,
            @Param("username") String username);

    /**
     * 删除角色和成员的关联关系
     * @param roleUuid 角色UUID
     * @return 影响行数
     */
    public int deleteRoleUsersByRoleUuid(String roleUuid);

    /**
     * 撤销用户组的所有角色
     * @param namespace 空间名称
     * @param usernameList 用户名称集合
     * @return 影响的行数
     */
    public int deleteRoleUsersBatch(@Param("namespace") String namespace,
            @Param("usernameList") List<String> usernameList);

    String[] getOperRoleIdByUserName(@Param("namespace") String namespace, @Param("username") String userName);
}
