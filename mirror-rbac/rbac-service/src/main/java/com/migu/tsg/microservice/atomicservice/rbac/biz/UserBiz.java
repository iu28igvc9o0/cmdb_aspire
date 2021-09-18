package com.migu.tsg.microservice.atomicservice.rbac.biz;

import java.util.List;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResult;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.DepartmentUser;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.User;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.UserDTO;

/**
 * 业务逻辑层接口
 * <p>
 * 项目名称: 微服务运维平台 包: com.migu.tsg.microservice.atomicservice.rbac.biz 类名称: UserBiz 类描述: 业务逻辑层接口 创建人: 曾祥华 创建时间: 2019-03-07 16:05:29
 */
public interface UserBiz {

    /**
     * 新增数据
     *
     * @param userDTO
     *            动作DTO对象
     * @return String 数据ID
     */
    String insert(UserDTO userDTO);

    /**
     * 根据主键删除数据
     *
     * @param userId
     *            主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(String userId);

    /**
     * 根据主键更新数据
     *
     * @param userDTO
     *            动作DTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(UserDTO userDTO);

    /**
     * 根据主键查询
     *
     * @param userId
     *            主键
     * @return UserDTO 返回对象
     */
    UserDTO selectByPrimaryKey(String userId);

    /**
     * 根据dto实体查询列表
     *
     * @param userDTO
     *            动作DTO对象
     * @return List<User> 返回集合
     */
    List<UserDTO> select(UserDTO userDTO);

    /**
     * 根据主键数组查询
     *
     * @param userIdArrays
     *            主键数组
     * @return List<UserDTO> 返回集合对象
     */
    List<UserDTO> selectByPrimaryKeyArrays(String[] userIdArrays);

    /**
     * 分页列表查询
     * 
     * @param pageRequest
     * @return
     */
    PageResult<UserDTO> pageList(PageRequest pageRequest);

    int insertBatch(List<UserDTO> userList);

    UserDTO findByCode(String name);

    int modifyDeptIdBatchByUserIdArrays(String deptId, List<String> userIdList);

    UserDTO findByLdapId(String ldapId);

    List<UserDTO> getByDefId(String deptId);

    /**
     * 批量创建部门-用户关联关系.
     * 
     * @param departmentUserList
     *            部门-人员列表
     * @return
     */
    void batchInsertDepartmentUser(List<DepartmentUser> departmentUserList);

    List<User> selectByLdapIdAndNamespace(String ldapId, String namespace);

	List<UserDTO> getAll();

    List<String> findSameDeptUserIdByLdapId(String ldapId);
    
    /**
	 * 创建默认记录，深圳UMS使用。
	 */
	void addDefaultRoleForUms();
	
	/**
	 * 创建默认记录，深圳UMS使用。
	 */
	void addDefaultUserClassifyAccountForUms();
}
