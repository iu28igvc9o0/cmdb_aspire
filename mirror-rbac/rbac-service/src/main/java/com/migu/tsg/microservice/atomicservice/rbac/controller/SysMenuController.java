package com.migu.tsg.microservice.atomicservice.rbac.controller;

import com.migu.tsg.microservice.atomicservice.rbac.biz.SysManageBiz;
import com.migu.tsg.microservice.atomicservice.rbac.biz.SysMenuBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.SysMenu;
import com.migu.tsg.microservice.atomicservice.rbac.dto.SysMenuReq;
import com.migu.tsg.microservice.atomicservice.rbac.dto.SysMenuResp;
import com.migu.tsg.microservice.atomicservice.rbac.service.SysMenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName SysMenuController
 * @Description: TODO
 * @Author baiwenping
 * @Date 2019/11/25
 * @Version V1.0
 **/
@RestController
public class SysMenuController implements SysMenuService{
    @Autowired
    private SysMenuBiz sysMenuBiz;
    /**
     *
     * @param sysId
     * @return
     */
    @Override
    public List<SysMenuResp> listAll(@PathVariable("sys_id")String sysId) {
        List<SysMenu> sysMenus = sysMenuBiz.listAll(sysId);
        return sysMenus.stream().map(item -> {
            SysMenuResp sysMenuResp = new SysMenuResp();
            BeanUtils.copyProperties(item, sysMenuResp);
            return sysMenuResp;
        }).collect(Collectors.toList());
    }

    /**
     *
     * @param sysName
     * @return
     */
    @Override
    public Map listBySysName(@PathVariable("sys_name")String sysName) {
        return sysMenuBiz.listBySysName(sysName);
    }

    /**
     *
     * @param sysMenuReq
     * @return
     */
    @Override
    public SysMenuResp insert(@RequestBody SysMenuReq sysMenuReq) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(sysMenuReq, sysMenu);
        SysMenu insert = sysMenuBiz.insert(sysMenu);
        SysMenuResp sysMenuResp = new SysMenuResp();
        BeanUtils.copyProperties(insert, sysMenuResp);
        return sysMenuResp;
    }

    /**
     *
     * @param sysMenuReq
     * @return
     */
    @Override
    public SysMenuResp update(@RequestBody SysMenuReq sysMenuReq) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(sysMenuReq, sysMenu);
        sysMenuBiz.update(sysMenu);
        SysMenuResp sysMenuResp = new SysMenuResp();
        BeanUtils.copyProperties(sysMenuReq, sysMenuResp);
        return sysMenuResp;
    }

    /**
     *
     * @param id
     */
    @Override
    public void delete(@PathVariable("id")String id) {
        sysMenuBiz.delete(id);
    }

    /**
     *
     * @param id
     * @return
     */
    public SysMenuResp selectById(@PathVariable("id") String id) {
        SysMenu sysMenu = sysMenuBiz.selectById(id);
        SysMenuResp sysMenuResp = new SysMenuResp();
        BeanUtils.copyProperties(sysMenu, sysMenuResp);
        return sysMenuResp;
    }

}
