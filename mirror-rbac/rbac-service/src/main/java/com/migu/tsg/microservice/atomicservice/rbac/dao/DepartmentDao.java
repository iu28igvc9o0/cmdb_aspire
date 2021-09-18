package com.migu.tsg.microservice.atomicservice.rbac.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.common.entity.Page;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.Department;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.DepartmentUserTree;

/**
 * 数据访问层接口
 * <p>
 * 项目名称: mirror平台 包: com.migu.tsg.microservice.atomicservice.rbac.dao 类名称: DepartmentDao 类描述: 数据访问层接口 创建人: 曾祥华 创建时间: 2019-03-04
 * 16:04:48
 */
@Mapper
public interface DepartmentDao {

    /**
     * 新增数据
     *
     * @param department
     *            动作PO对象
     * @return int 新增数据条数
     */
    int insert(Department department);

    /**
     * 新增数据
     *
     * @param departmentList
     *            动作PO对象
     */
    void insertOrUpdateBatchParam(List<Department> departmentList);

    /**
     * 根据主键删除数据
     *
     * @param departmentId
     *            主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(@Param(value = "departmentId") String departmentId);

    void batchDeleteByPrimaryKey(List<String> departmentIdList);

    /**
     * 根据主键更新数据
     *
     * @param department
     *            动作PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(Department department);

    /**
     * 根据主键查询
     *
     * @param departmentId
     *            主键
     * @return Department 返回对象
     */
    Department selectByPrimaryKey(@Param(value = "departmentId") String departmentId);

    /**
     * 根据父部门查找子部门
     */
    List<Department> selectByParentId(@Param(value = "parentId") String parentId);

    /**
     * 根据主键数组查询
     *
     * @param departmentIdArrays
     *            主键数组
     * @return List<Department> 返回集合对象
     */
    List<Department> selectByPrimaryKeyArrays(String[] departmentIdArrays);

    /**
     * 根据po实体查询列表
     *
     * @param department
     *            动作PO对象
     * @return List<Department> 返回集合
     */
    List<Department> queryList(Department department);

    /**
     * 根据page对象查询数量
     * 
     * @param page
     * @return 条数
     */
    int pageListCount(Page page);

    /**
     * 根据page对象查询监控项列表
     * 
     * @param page
     * @return 监控项列表
     */
    List<Department> pageList(Page page);

    List<Department> queryByDeptId(@Param(value = "deptId") String deptId);

	List<Department> getAll();

    /**
     * 根据部门id查询子部门和人员
     * 
     * @param
     * @return
     */
    List<DepartmentUserTree> queryDepartAndUserByDeptId(@Param(value = "deptId") String deptId);
}
