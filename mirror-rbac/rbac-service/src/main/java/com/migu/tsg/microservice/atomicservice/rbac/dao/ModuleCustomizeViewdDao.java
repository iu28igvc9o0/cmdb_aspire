package com.migu.tsg.microservice.atomicservice.rbac.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ModuleCustomizedView;


@Mapper
public interface ModuleCustomizeViewdDao {

	List<ModuleCustomizedView> select(ModuleCustomizedView moduleCustomizedView);

	int deleteByPrimaryKey(@Param(value = "id") String moduleId);

	int insert(ModuleCustomizedView moduleCustomizedView);

	int updateByPrimaryKey(ModuleCustomizedView moduleCustomizedView);

}
