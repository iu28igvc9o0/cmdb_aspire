package com.migu.tsg.microservice.atomicservice.rbac.controller;

import com.migu.tsg.microservice.atomicservice.rbac.biz.SysManageBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.SysManage;
import com.migu.tsg.microservice.atomicservice.rbac.dto.SysManageReq;
import com.migu.tsg.microservice.atomicservice.rbac.dto.SysManageResp;
import com.migu.tsg.microservice.atomicservice.rbac.service.SysManageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
public class SysManageController implements SysManageService{
    @Autowired
    private SysManageBiz sysManageBiz;

    /**
     *
     * @return
     */
    @Override
    public List<SysManageResp> listAll() {
        List<SysManage> sysManages = sysManageBiz.listAll();
        return sysManages.stream().map(item -> {
            SysManageResp sysManageResp = new SysManageResp();
            BeanUtils.copyProperties(item, sysManageResp);
            return sysManageResp;
        }).collect(Collectors.toList());
    }

    /**
     *
     * @param sysManageReq
     * @return
     */
    @Override
    public SysManageResp insert(@RequestBody SysManageReq sysManageReq) {
        SysManage sysManage = new SysManage();
        BeanUtils.copyProperties(sysManageReq, sysManage);
        SysManage insert = sysManageBiz.insert(sysManage);
        SysManageResp sysManageResp = new SysManageResp();
        BeanUtils.copyProperties(insert, sysManageResp);
        return sysManageResp;
    }

    /**
     *
     * @param name
     * @return
     */
    @Override
    public Map<String, Object> checkName(@PathVariable("name")String name) {
        return sysManageBiz.checkName(name);
    }

    /**
     *
     * @param id
     */
    @Override
    public void delete(@PathVariable("id")String id) {
        sysManageBiz.delete(id);
    }
}
