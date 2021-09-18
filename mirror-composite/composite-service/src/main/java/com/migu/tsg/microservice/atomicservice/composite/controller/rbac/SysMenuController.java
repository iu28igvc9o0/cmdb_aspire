package com.migu.tsg.microservice.atomicservice.composite.controller.rbac;

import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.SysMenuServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.ICompSysMenuService;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.CompSysMenuReq;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.CompSysMenuResp;
import com.migu.tsg.microservice.atomicservice.rbac.dto.SysMenuReq;
import com.migu.tsg.microservice.atomicservice.rbac.dto.SysMenuResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName SysMenuController
 * @Description: TODO
 * @Author baiwenping
 * @Date 2019/11/25
 * @Version V1.0
 **/
@RestController
public class SysMenuController implements ICompSysMenuService {
    @Autowired
    private SysMenuServiceClient sysMenuServiceClient;

    /**
     *
     * @param sysId
     * @return
     */
    @Override
    public List<CompSysMenuResp> listAll(@PathVariable("sys_id")String sysId) {
        List<SysMenuResp> sysMenus = sysMenuServiceClient.listAll(sysId);
        return PayloadParseUtil.jacksonBaseParse(CompSysMenuResp.class, sysMenus);
    }

    /**
     *
     * @param sysName
     * @return
     */
    @Override
    public Map listBySysName(@PathVariable("sys_name")String sysName) {
        return sysMenuServiceClient.listBySysName(sysName);
    }

    /**
     *
     * @param sysMenuReq
     * @return
     */
    @Override
    public CompSysMenuResp insert(@RequestBody CompSysMenuReq sysMenuReq) {
        SysMenuReq sysMenu = PayloadParseUtil.jacksonBaseParse(SysMenuReq.class, sysMenuReq);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        sysMenu.setCreater(authCtx.getUser().getUsername());
        sysMenu.setCreateTime(new Date());
        SysMenuResp insert = sysMenuServiceClient.insert(sysMenu);
        return  PayloadParseUtil.jacksonBaseParse(CompSysMenuResp.class, insert);
    }

    /**
     *
     * @param sysMenuReq
     * @return
     */
    @Override
    public CompSysMenuResp update(@RequestBody CompSysMenuReq sysMenuReq) {
        SysMenuReq sysMenu = PayloadParseUtil.jacksonBaseParse(SysMenuReq.class, sysMenuReq);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        sysMenu.setEditer(authCtx.getUser().getUsername());
        sysMenu.setUpdateTime(new Date());
        sysMenuServiceClient.update(sysMenu);
        return PayloadParseUtil.jacksonBaseParse(CompSysMenuResp.class, sysMenuReq);
    }

    /**
     *
     * @param id
     */
    @Override
    public void delete(@PathVariable("id")String id) {
        sysMenuServiceClient.delete(id);
    }

    /**
     *
     * @param id
     * @return
     */
    public CompSysMenuResp selectById(@PathVariable("id") String id) {
        SysMenuResp sysMenuResp = sysMenuServiceClient.selectById(id);
        if (sysMenuResp == null) {
            return null;
        }
        return PayloadParseUtil.jacksonBaseParse(CompSysMenuResp.class, sysMenuResp);
    }
}
