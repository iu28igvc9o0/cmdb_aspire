package com.migu.tsg.microservice.atomicservice.rbac.biz.impl;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.migu.tsg.microservice.atomicservice.rbac.biz.ModuleCustomizedViewBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.ModuleCustomizeViewdDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.ModuleCustomizedDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ModuleCustomized;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ModuleCustomizedView;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.transform.ModuleCustomizedTransformer;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ModuleCustomizedDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ModuleCustomizedViewDTO;
@Service("moduleCustomizedViewBiz")
public class ModuleCustomizedViewBizImpl implements ModuleCustomizedViewBiz {
	/**
	 * 系统日志
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ModuleCustomizedViewBizImpl.class);
	
	@Autowired
	private ModuleCustomizeViewdDao moduleCustomizedViewDao;
	
	@Override
	public List<ModuleCustomizedViewDTO> select(ModuleCustomizedViewDTO dto) {
		  if (null == dto) {
	            LOGGER.warn("select Object moduleCustomizedDTO is null");
	            return Collections.emptyList();
	        }
		    ModuleCustomizedView moduleCustomizedView = this.toPo(dto);
	        List<ModuleCustomizedView> listModuleCustomizedView = moduleCustomizedViewDao.select(moduleCustomizedView);
	        return this.fromListPo(listModuleCustomizedView);
		
	}

	@Override
	public int deleteByPrimaryKey(String id) {
		 if (StringUtils.isEmpty(id)) {
	            LOGGER.error("method[eleteByPrimaryKey] param[id] is null");
	            throw new RuntimeException("param[id] is null");
	        }
	        return moduleCustomizedViewDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(ModuleCustomizedViewDTO moduleCustomizedViewDTO) {
		 if (null == moduleCustomizedViewDTO) {
	            LOGGER.error("method[insert] param[ModuleCustomizedViewDTO] is null");
	            throw new RuntimeException("param[ModuleCustomizedViewDTO] is null");
	        }
		    ModuleCustomizedView moduleCustomized = this.toPo(moduleCustomizedViewDTO);
		    moduleCustomized.setId(UUID.randomUUID().toString());
		    return  moduleCustomizedViewDao.insert(moduleCustomized);
			 
		
	}

	@Override
	public int update(ModuleCustomizedViewDTO moduleCustomizedViewDTO)  {
		if (null == moduleCustomizedViewDTO) {
            LOGGER.error("method[updateByPrimaryKey] param[moduleCustomizedViewDTO] is null");
            throw new RuntimeException("param[moduleCustomizedViewDTO] is null");
        }
        if (StringUtils.isEmpty(moduleCustomizedViewDTO.getId())) {
            LOGGER.warn("method[updateByPrimaryKey] param[Id] is null");
            throw new RuntimeException("param[Id] is null");
        }
        ModuleCustomizedView moduleCustomized = this.toPo(moduleCustomizedViewDTO);
        return moduleCustomizedViewDao.updateByPrimaryKey(moduleCustomized);
		
	}
	
	
	
 
  

	
	
	 /**
     * 将DTO实体转换为动作PO实体
     *
     * @param moduleCustomizedDTO DTO实体类
     * @return ModuleCustomized PO实体
     */
    public  ModuleCustomizedView toPo(final ModuleCustomizedViewDTO moduleCustomizedDTO) {
        if (null == moduleCustomizedDTO) {
            return null;
        }

        ModuleCustomizedView moduleCustomizedView = new ModuleCustomizedView();
        moduleCustomizedView.setId(moduleCustomizedDTO.getId());
        moduleCustomizedView.setUserId(moduleCustomizedDTO.getUserId());
        moduleCustomizedView.setModuleId(moduleCustomizedDTO.getModuleId());
        moduleCustomizedView.setContent(moduleCustomizedDTO.getContent());
        moduleCustomizedView.setCreateTime(moduleCustomizedDTO.getCreateTime());
        moduleCustomizedView.setName(moduleCustomizedDTO.getName());
        moduleCustomizedView.setDescribe(moduleCustomizedDTO.getDescribe());
        moduleCustomizedView.setSystemId(moduleCustomizedDTO.getSystemId());
        moduleCustomizedView.setPageType(moduleCustomizedDTO.getPageType());
        
        return moduleCustomizedView;
    }
    
    
    /**
     * 将PO实体转换为动作DTO实体
     *
     * @param moduleCustomized 动作PO实体
     * @return ModuleCustomizedDTO 动作DTO实体
     */
    public ModuleCustomizedViewDTO fromPo(final ModuleCustomizedView moduleCustomizedView) {
        if (null == moduleCustomizedView) {
            return null;
        }

        ModuleCustomizedViewDTO moduleCustomizedViewDTO = new ModuleCustomizedViewDTO();
        moduleCustomizedViewDTO.setId(moduleCustomizedView.getId());
        moduleCustomizedViewDTO.setUserId(moduleCustomizedView.getUserId());
        moduleCustomizedViewDTO.setModuleId(moduleCustomizedView.getModuleId());
        moduleCustomizedViewDTO.setContent(moduleCustomizedView.getContent());
        moduleCustomizedViewDTO.setCreateTime(moduleCustomizedView.getCreateTime());
        moduleCustomizedViewDTO.setName(moduleCustomizedView.getName());
        moduleCustomizedViewDTO.setDescribe(moduleCustomizedView.getDescribe());
        moduleCustomizedViewDTO.setSystemId(moduleCustomizedView.getSystemId());
        moduleCustomizedViewDTO.setPageType(moduleCustomizedView.getPageType());

        return moduleCustomizedViewDTO;
    }
    
    /**
     * 将业务实体对象集合转换为动作持久化对象集合
     *
     * @param listModuleCustomized 业务实体对象集合
     * @return List<ModuleCustomizedDTO> 持久化对象集合
     */
    public  List<ModuleCustomizedViewDTO> fromListPo(final List<ModuleCustomizedView> listModuleCustomizedView) {
        if (CollectionUtils.isEmpty(listModuleCustomizedView)) {
            return Lists.newArrayList();
        }
        List<ModuleCustomizedViewDTO> listModuleCustomizedViewDTO = Lists.newArrayList();

        for (ModuleCustomizedView moduleCustomizedView : listModuleCustomizedView) {
        	listModuleCustomizedViewDTO.add(this.fromPo(moduleCustomizedView));
        }
        return listModuleCustomizedViewDTO;
    }

}
