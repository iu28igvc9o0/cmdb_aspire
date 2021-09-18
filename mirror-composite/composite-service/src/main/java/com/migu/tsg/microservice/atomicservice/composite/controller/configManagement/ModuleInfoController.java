package com.migu.tsg.microservice.atomicservice.composite.controller.configManagement;

import com.aspire.mirror.composite.service.configManagement.IModuleInfoService;
import com.aspire.mirror.composite.service.configManagement.payload.CompModuleInfoCreateRequest;
import com.aspire.mirror.composite.service.configManagement.payload.ModuleInfoPayload;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.ModuleInfoClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ModuleInfoCreateRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.vo.ModuleInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

/**
 * TODO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller.configManagement
 * 类名称:    ModuleInfoController.java
 * 类描述:    TODO
 * 创建人:    JinSu
 * 创建时间:  2019/10/17 15:53
 * 版本:      v1.0
 */
@RestController
@Slf4j
public class ModuleInfoController implements IModuleInfoService {
    @Autowired
    private ModuleInfoClient moduleInfoClient;
    @Override
    @ResAction(action = "create", resType = "moduleInfo")
    public ModuleInfoPayload saveModuleInfo(@RequestBody CompModuleInfoCreateRequest moduleInfo) {
        if (moduleInfo == null) {
            throw new RuntimeException("ModuleInfoController[saveModuleInfo] param moduleInfo is null");
        }
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();

        ModuleInfoCreateRequest createRequest = new ModuleInfoCreateRequest();

        if (authCtx != null) {
            createRequest.setCreateUser(authCtx.getUser().getUsername());
            createRequest.setLastUpdateUser(authCtx.getUser().getUsername());
        }
        createRequest.setData(moduleInfo.getData());
        createRequest.setModuleCode(moduleInfo.getModuleCode());
        ModuleInfoVO moduleInfoVO = moduleInfoClient.saveModuleInfo(createRequest);
        return jacksonBaseParse(ModuleInfoPayload.class, moduleInfoVO);
    }

    @Override
    @ResAction(action = "select", resType = "moduleInfo")
    public ModuleInfoPayload findByCode(@RequestParam("moduleCode") String moduleCode) {
        if (StringUtils.isEmpty(moduleCode)) {
            log.warn("ModuleInfoController[findByCode] param moduleCode is empty");
            return null;
        }
        ModuleInfoVO moduleInfoVO = moduleInfoClient.findByCode(moduleCode);
        return jacksonBaseParse(ModuleInfoPayload.class, moduleInfoVO);
    }
}
