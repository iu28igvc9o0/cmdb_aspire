package com.migu.tsg.microservice.atomicservice.rbac.biz.impl;

import com.migu.tsg.microservice.atomicservice.rbac.biz.SysManageBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.SysManageMapper;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.SysManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @ClassName SysManageBizImpl
 * @Description: TODO
 * @Author baiwenping
 * @Date 2019/11/26
 * @Version V1.0
 **/
@Service
public class SysManageBizImpl implements SysManageBiz {
    @Autowired
    private SysManageMapper sysManageMapper;

    /**
     *
     * @return
     */
    @Override
    public List<SysManage> listAll() {
        return sysManageMapper.selectAll();
    }

    /**
     *
     * @param sysManage
     * @return
     */
    @Override
    public SysManage insert(SysManage sysManage) {
        sysManage.setId(UUID.randomUUID().toString());
        sysManageMapper.insertSelective(sysManage);
        return sysManage;
    }

    /**
     *
     * @param name
     * @return
     */
    @Override
    public Map<String, Object> checkName(String name) {
        return null;
    }

    /**
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        sysManageMapper.deleteByPrimaryKey(id);
    }
}
