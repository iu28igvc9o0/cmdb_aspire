package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.v3.module;

import com.aspire.mirror.composite.service.cmdb.v3.module.ICmdbV3ModuleCiRelationAPI;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelation;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelationDetail;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelationDetailResponse;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.module.ICmdbV3ModuleCiRelationClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbV3ModuleCiRelationController
 * Author:   hangfang
 * Date:     2020/4/27
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbV3ModuleCiRelationController implements ICmdbV3ModuleCiRelationAPI {

    @Autowired
    private ICmdbV3ModuleCiRelationClient ciRelationClient;

    @Override
    public List<CmdbV3ModuleCiRelation> listByModuleId(@RequestParam("moduleId") String moduleId) {
        return ciRelationClient.listByModuleId(moduleId);
    }

    @Override
    public List<CmdbV3ModuleCiRelationDetail> listRDetailByModuleId(@RequestParam("moduleId") String moduleId) {
        return ciRelationClient.listRDetailByModuleId(moduleId);
    }

    @Override
    public Map<String, Object> save(@RequestBody CmdbV3ModuleCiRelation relation) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String username = authCtx.getUser().getUsername();
        relation.setCreatePerson(username);
        return ciRelationClient.save(username, relation);
    }

    @Override
    public Map<String, Object> update(@RequestBody CmdbV3ModuleCiRelation relation) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String username = authCtx.getUser().getUsername();
        relation.setUpdatePerson(username);
        return ciRelationClient.update(username, relation);
    }

    @Override
    public Map<String, Object> deleteById(@RequestParam("id") String id) {
        return ciRelationClient.deleteById(id);
    }

    @Override
    public CmdbV3ModuleCiRelationDetailResponse getCiRelationDetail(@RequestParam("relationId") String relationId,
                                                                    @RequestParam("instanceId") String instanceId) {
        return ciRelationClient.getCiRelationDetail(relationId, instanceId);
    }
}
