package com.migu.tsg.microservice.atomicservice.rbac.dao;

import java.util.List;
import com.aspire.mirror.common.entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.migu.tsg.microservice.atomicservice.rbac.dao.po.User;

/**
 * 数据访问层接口
 * <p>
 * 项目名称:  mirror平台
 * 包:     com.migu.tsg.microservice.atomicservice.rbac.dao   
 * 类名称:     UserDao 
 * 类描述:     数据访问层接口
 * 创建人:     曾祥华
 * 创建时间:     2019-03-07 16:05:29
 */
 @Mapper
public interface UserDao {

	/**
     * 新增数据
     *
     * @param user 动作PO对象
     * @return int 新增数据条数
     */
    int insert(User user);
    
    /**
     * 根据主键删除数据
     *
     * @param userId 主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value = "userId") String userId);
    
    /**
     * 根据主键更新数据
     *
     * @param user 动作PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(User user);
    
    /**
     * 根据主键查询
     *
     * @param userId 主键
     * @return User 返回对象
     */
    User selectByPrimaryKey(@Param(value = "userId") String userId);

    /**
     * 根据主键数组查询
     *
     * @param userIdArrays 主键数组
     * @return List<User> 返回集合对象
     */
    List<User> selectByPrimaryKeyArrays(String[] userIdArrays);
    
	/**
     * 根据po实体查询列表
     *
     * @param user 动作PO对象
     * @return List<User>  返回集合
     */
    List<User> queryList(User user);
    
	/**
     * 根据po实体查询列表
     *
     * @param user 动作PO对象
     * @return List<User>  返回集合
     */
    List<User> queryListForExcel(User user);

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
    List<User> pageList(Page page);

    List<User> selectByLdapIdAndNamespace(@Param("ldapId") String ldapId, @Param("namespace") String namespace);

    int insertBatch(List<User> addList);

    int modifyBatchByLdapIdAndNamespace(List<User> modifyList);

    int modifyBatchByUserId(List<User> users);

    List<User> getByDefId(@Param("deptId") String deptId);

	List<User> getAll();

    String getUuidBy(String ldapId, String namespace);
    
	void addDefaultRoleForUms();

	void addDefaultUserClassifyAccountForUms();
}
