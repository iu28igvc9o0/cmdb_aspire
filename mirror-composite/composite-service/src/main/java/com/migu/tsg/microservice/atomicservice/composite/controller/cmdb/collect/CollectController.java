package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.collect;

import com.aspire.mirror.composite.service.cmdb.collect.ICollectApprovalAPI;
import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbApprovalUpdateReq;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApprovalQuery;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.collect.CmdbCollectClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.process.ApprovalController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class CollectController extends CommonResourceController implements ICollectApprovalAPI {

    @Autowired
    private CmdbCollectClient collectClient;

    @Autowired
    private ApprovalController approvalController;

    @Override
    public List<Map<String, String>> getFiledNameList() {
        return collectClient.getFiledNameList();
    }

    @Override
    public List<Map<String, String>> getOperatorTypeList() {
        return collectClient.getOperatorTypeList();
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public Result<Map<String, Object>> list(@RequestBody CmdbCollectApprovalQuery approvalQuery) {
        return collectClient.list(approvalQuery);
    }

    @Override
    public List<CmdbSimpleCode> getApprovalHeaderCode(@RequestParam("moduleId") String moduleId) {
        return collectClient.getApprovalHeaderCode(moduleId);
    }

    @Override
    public Map<String, Object> update(@RequestBody CmdbApprovalUpdateReq updateReq) {
        return approvalController.approvalProcess(updateReq);
    }
}
