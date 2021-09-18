package com.migu.tsg.microservice.atomicservice.rbac.dao;

import com.migu.tsg.microservice.atomicservice.rbac.dao.po.SysManage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SysManageMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysManage sysManage);

    int insertSelective(SysManage sysManage);

    int updateByPrimaryKeySelective(SysManage sysManage);

    int updateByPrimaryKey(SysManage sysManage);

    SysManage selectById(String id);

    List<SysManage> selectAll ();
}