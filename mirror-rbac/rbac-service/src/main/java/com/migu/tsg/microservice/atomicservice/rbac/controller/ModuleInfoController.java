package com.migu.tsg.microservice.atomicservice.rbac.controller;

import com.migu.tsg.microservice.atomicservice.rbac.biz.ModuleInfoBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleInfoCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.ModuleInfoDTO;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.ModuleInfoVO;
import com.migu.tsg.microservice.atomicservice.rbac.service.ModuleInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.rbac.controller
 * 类名称:    ModuleInfoController.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/17 16:07
 * 版本:      v1.0
 */
@RestController
@Slf4j
public class ModuleInfoController implements ModuleInfoService {

    @Autowired
    public ModuleInfoBiz moduleInfoBiz;
    @Override
    public ModuleInfoVO saveModuleInfo(@RequestBody ModuleInfoCreateRequest moduleInfo) {
        if (moduleInfo == null) {
            log.error("ModuleInfoController[saveModuleCustomized] param moduleInfo is null");
            throw new RuntimeException("ModuleInfoController[saveModuleCustomized] param moduleInfo is null");
        }
        if (StringUtils.isEmpty(moduleInfo.getModuleCode())) {
            log.error("ModuleInfoController[saveModuleCustomized] param moduleInfo.moduleCode is empty");
            throw new RuntimeException("ModuleInfoController[saveModuleCustomized] param moduleInfo.moduleCode is empty");
        }
        ModuleInfoDTO moduleInfoDTO = new ModuleInfoDTO();
        BeanUtils.copyProperties(moduleInfo, moduleInfoDTO);
        ModuleInfoDTO result = moduleInfoBiz.insert(moduleInfoDTO);
        ModuleInfoVO response = new ModuleInfoVO();
        BeanUtils.copyProperties(result, response);
        return response;
    }

    @Override
    public ModuleInfoVO findByCode(@RequestParam("moduleCode") String moduleCode) {
        if (StringUtils.isEmpty(moduleCode)) {
            log.warn("ModuleInfoController[findByCode] param moduleCode is empty");
            return null;
        }
        ModuleInfoDTO moduleInfoDTO = moduleInfoBiz.findByCode(moduleCode);
        if (moduleInfoDTO != null) {
            ModuleInfoVO moduleInfoVO = new ModuleInfoVO();
            BeanUtils.copyProperties(moduleInfoDTO, moduleInfoVO);
            return moduleInfoVO;
        }
        return null;
    }
}
