package com.migu.tsg.microservice.atomicservice.rbac.dao;

import com.migu.tsg.microservice.atomicservice.rbac.dao.po.DepartmentUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.rbac.dao
 * 类名称:    DepartmentUserDao.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/5/21 17:15
 * 版本:      v1.0
 */
@Mapper
public interface DepartmentUserDao {

    int insertBatch(List<DepartmentUser> addList);

    int deleteByUserId(String userId);

    int modifyDeptIdBatchByUserIdArrays(@Param("deptId") String deptId, @Param("userIdList") List<String> userIdList);

    int deleteByUserIdArrays(@Param("userIdList") List<String> userIdList);
}
