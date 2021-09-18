package com.migu.tsg.microservice.atomicservice.rbac.dao;

import com.migu.tsg.microservice.atomicservice.rbac.dao.po.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SysMenuMapper {
    int deleteByPrimaryKey(String id);

    int insert(SysMenu sysMenu);

    int insertSelective(SysMenu sysMenu);

    int updateByPrimaryKeySelective(SysMenu sysMenu);

    int updateByPrimaryKey(SysMenu sysMenu);

    SysMenu selectById(String id);

    List<SysMenu> selectByParentId(String id);

    List<SysMenu> selectBySysId(String systemId);

    List<SysMenu> selectBySysName(String systemName);

}