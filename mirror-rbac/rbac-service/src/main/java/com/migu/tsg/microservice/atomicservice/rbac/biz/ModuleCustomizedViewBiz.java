package com.migu.tsg.microservice.atomicservice.rbac.biz;

import java.util.List;

import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ModuleCustomizedViewDTO;

public interface ModuleCustomizedViewBiz {
	/**
                * 新增数据
     *
     * @param moduleCustomizedViewDTO 动作DTO对象
     * @return String 数据ID
     */
	int insert(ModuleCustomizedViewDTO moduleCustomizedViewDTO);

	
	/**
                  * 新增数据
     *
     * @param moduleCustomizedViewDTO 动作DTO对象
     * @return String 数据ID
     */
	int update(ModuleCustomizedViewDTO moduleCustomizedViewDTO);

	List<ModuleCustomizedViewDTO> select(ModuleCustomizedViewDTO dto);

	int deleteByPrimaryKey(String id);

}
