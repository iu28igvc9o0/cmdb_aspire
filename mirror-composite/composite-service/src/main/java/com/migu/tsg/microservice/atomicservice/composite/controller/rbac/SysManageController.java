package com.migu.tsg.microservice.atomicservice.composite.controller.rbac;

import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.SysManageServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.ICompSysManageService;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.CompSysManageReq;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.CompSysManageResp;
import com.migu.tsg.microservice.atomicservice.rbac.dto.SysManageReq;
import com.migu.tsg.microservice.atomicservice.rbac.dto.SysManageResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName SysManageController
 * @Description: TODO
 * @Author baiwenping
 * @Date 2019/11/25
 * @Version V1.0
 **/
@RestController
public class SysManageController implements ICompSysManageService {
    @Autowired
    private SysManageServiceClient sysManageServiceClient;

    /**
     *
     * @return
     */
    @Override
    public List<CompSysManageResp> listAll() {
        List<SysManageResp> sysManages = sysManageServiceClient.listAll();
        return PayloadParseUtil.jacksonBaseParse(CompSysManageResp.class, sysManages);
    }

    /**
     *
     * @param sysManageReq
     * @return
     */
    @Override
    public CompSysManageResp insert(@RequestBody CompSysManageReq sysManageReq) {
        SysManageReq sysManage = PayloadParseUtil.jacksonBaseParse(SysManageReq.class, sysManageReq);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        sysManage.setCreater(authCtx.getUser().getUsername());
        sysManage.setCreateTime(new Date());
        SysManageResp insert = sysManageServiceClient.insert(sysManage);
        return PayloadParseUtil.jacksonBaseParse(CompSysManageResp.class, insert);
    }

    /**
     *
     * @param name
     * @return
     */
    @Override
    public Map<String, Object> checkName(@PathVariable("name")String name) {
        return sysManageServiceClient.checkName(name);
    }

    /**
     *
     * @param id
     */
    @Override
    public void delete(@PathVariable("id")String id) {
        sysManageServiceClient.delete(id);
    }
}
