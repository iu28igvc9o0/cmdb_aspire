package com.migu.tsg.microservice.atomicservice.rbac.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ParentRole;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.RoleParent;

/**
 * 项目名称: rbac-service <br>
 * 包: com.migu.tsg.microservice.atomicservice.rbac.dao <br>
 * 类名称: RoleParentsDao.java <br>
 * 类描述: 一个角色对应多个父角色的中间表的DAO接口 <br>
 * 创建人: WangSheng <br>
 * 创建时间: 2017年7月22日下午4:12:34<br>
 * 版本: v1.0
 */
@Mapper
public interface RoleParentDao {

    /**
     * 查询该角色是否是父角色
     * @param roleUuid 角色uuid
     * @return 大于0则为父角色,否则为非父角色
     */
    public int isParentRole(String roleUuid);

    /**
     * 查询指定角色的父角色集合
     * @param roleUuid 子角色uuid
     * @return 指定角色的父角色集合
     */
    public List<RoleParent> fetchRoleParentList(String roleUuid);

    /**
     * 查询指定父角色的子角色集合
     * @param parentRoleUuid 父角色uuid
     * @return 指定父角色的子角色集合
     */
    public List<RoleParent> fetchRoleChildrenList(String parentRoleUuid);

    /**
     * 查询指定角色的父角色是否存在
     * @param roleUuid 子角色uuid
     * @param parentUuid 父角色uuid
     * @return 大于0则存在,小于等于0则不存在
     */
    public int hasFetchRoleParent(@Param("roleUuid") String roleUuid, @Param("parentUuid") String parentUuid);

    /**
     * 新增角色和单个父角色关联信息
     * @param roleParent 对象
     * @return 影响行数
     */
    public int createRoleParent(RoleParent roleParent);

    /**
     * 批量新增角色和单个父角色关联信息
     * @param roleParentList 对象集合
     * @return 影响行数
     */
    public int createRoleParentBatch(@Param("roleParentList") List<RoleParent> roleParentList);

    /**
     * 删除角色和单个父角色关联信息
     * @param roleUuid 子角色UUID
     * @param parentUuid 父角色UUID
     * @return 影响行数
     */
    public int deleteParentRole(@Param("roleUuid") String roleUuid, @Param("parentUuid") String parentUuid);

    /**
     * 删除角色和所有父角色关联信息
     * @param roleUuid 子角色UUID
     * @return 影响行数
     */
    public int deleteParentRoles(String roleUuid);

    /**
     * 查询角色的父角色(含自身一共4层)
     * @param roleUuidList 子角色UUID集合
     * @return 父角色UUID集合
     */
    public List<ParentRole> listOfParentRoleUuid(@Param("roleUuidList") List<String> roleUuidList);
}
