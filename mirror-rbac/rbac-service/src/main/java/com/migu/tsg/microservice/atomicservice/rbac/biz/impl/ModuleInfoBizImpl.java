package com.migu.tsg.microservice.atomicservice.rbac.biz.impl;

import com.aspire.mirror.common.util.BeanUtil;
import com.migu.tsg.microservice.atomicservice.rbac.biz.ModuleInfoBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.ModuleInfoDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ModuleInfo;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ModuleInfoDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.rbac.biz.impl
 * 类名称:    ModuleInfoBizImpl.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/17 16:26
 * 版本:      v1.0
 */
@Service
public class ModuleInfoBizImpl implements ModuleInfoBiz {
    @Autowired
    private ModuleInfoDao moduleInfoDao;

    @Override
    public ModuleInfoDTO insert(ModuleInfoDTO moduleInfoDTO) {
        Date now = new Date();
        moduleInfoDTO.setLastUpdateUser(moduleInfoDTO.getLastUpdateUser());
        moduleInfoDTO.setUpdateTime(now);

        ModuleInfo result = moduleInfoDao.findByCode(moduleInfoDTO.getModuleCode());
        if (result == null) {
            moduleInfoDTO.setId(UUID.randomUUID().toString());
            moduleInfoDTO.setCreateTime(now);
            moduleInfoDTO.setCreateUser(moduleInfoDTO.getCreateUser());
            ModuleInfo moduleInfo = new ModuleInfo();
            BeanUtils.copyProperties(moduleInfoDTO, moduleInfo);
            moduleInfoDao.insert(moduleInfo);
        } else {
            moduleInfoDTO.setId(result.getId());
            ModuleInfo moduleInfo = new ModuleInfo();
            BeanUtils.copyProperties(moduleInfoDTO, moduleInfo);
            moduleInfoDao.updateByCode(moduleInfo);
        }

        return moduleInfoDTO;
    }

    @Override
    public ModuleInfoDTO findByCode(String moduleCode) {
        ModuleInfo moduleInfo = moduleInfoDao.findByCode(moduleCode);
        if (moduleInfo != null) {
            ModuleInfoDTO moduleInfoDTO = new ModuleInfoDTO();
            BeanUtils.copyProperties(moduleInfo, moduleInfoDTO);
            return moduleInfoDTO;
        }
        return null;
    }
}
