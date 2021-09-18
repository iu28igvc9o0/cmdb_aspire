package com.migu.tsg.microservice.atomicservice.rbac.biz;

import java.util.List;

import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResult;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.DepartmentDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.DepartmentUserDTO;

/**
 * 业务逻辑层接口
 * <p>
 * 项目名称: 微服务运维平台 包: com.migu.tsg.microservice.atomicservice.rbac.biz 类名称: DepartmentBiz 类描述: 业务逻辑层接口 创建人: 曾祥华 创建时间: 2019-03-04
 * 16:04:48
 */
public interface DepartmentBiz {

    /**
     * 新增数据
     *
     * @param departmentDTO
     *            动作DTO对象
     * @return String 数据ID
     */
    String insert(DepartmentDTO departmentDTO);

    void batchInsertOrUpdate(List<DepartmentDTO> departmentDTOList);

    void batchDeleteByPrimaryKey(List<String> departmentIdList);

    /**
     * 根据主键删除数据
     *
     * @param departmentId
     *            主键
     * @return int 删除数据条数
     */
    int deleteByPrimaryKey(String departmentId);

    /**
     * 根据主键更新数据
     *
     * @param departmentDTO
     *            动作DTO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(DepartmentDTO departmentDTO);

    /**
     * 根据主键查询
     *
     * @param departmentId
     *            主键
     * @return DepartmentDTO 返回对象
     */
    DepartmentDTO selectByPrimaryKey(String departmentId);

    /**
     * 根据dto实体查询列表
     *
     * @param departmentDTO
     *            动作DTO对象
     * @return List<Department> 返回集合
     */
    List<DepartmentDTO> select(DepartmentDTO departmentDTO);

    /**
     * 根据主键数组查询
     *
     * @param departmentIdArrays
     *            主键数组
     * @return List<DepartmentDTO> 返回集合对象
     */
    List<DepartmentDTO> selectByPrimaryKeyArrays(String[] departmentIdArrays);

    /**
     * 分页列表查询
     * 
     * @param pageRequest
     * @return
     */
    PageResult<DepartmentDTO> pageList(PageRequest pageRequest);

    DepartmentDTO selectByName(String name);

    List<DepartmentDTO> queryByDeptId(String deptId);

	List<DepartmentDTO> getAll();

    List<DepartmentUserDTO> queryDepartAndUser(String departId);
}
