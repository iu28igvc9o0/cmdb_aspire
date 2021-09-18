package com.migu.tsg.microservice.atomicservice.rbac.dao;

import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ModuleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.rbac.dao
 * 类名称:    ModuleInfoDao.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/17 16:28
 * 版本:      v1.0
 */
@Mapper
public interface ModuleInfoDao {
    /**
     * 创建模块信息数据
     * @param moduleInfo 模块信息实体
     * @return 受影响数据条数
     */
    int insert(ModuleInfo moduleInfo);

    /**
     * 根据code查找模块数据
     * @param moduleCode 模块code
     * @return 模块信息实体
     */
    ModuleInfo findByCode(@Param("moduleCode") String moduleCode);

    int updateByCode(ModuleInfo moduleInfo);
}
