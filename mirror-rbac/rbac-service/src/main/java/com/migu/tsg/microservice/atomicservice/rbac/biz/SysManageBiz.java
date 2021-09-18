package com.migu.tsg.microservice.atomicservice.rbac.biz;

import com.migu.tsg.microservice.atomicservice.rbac.dao.po.SysManage;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SysmanageBiz
 * @Description: TODO
 * @Author baiwenping
 * @Date 2019/11/26
 * @Version V1.0
 **/
public interface SysManageBiz {
    /**
     *
     * @return
     */
    List<SysManage> listAll () ;

    /**
     *
     * @param sysManage
     * @return
     */
    SysManage insert (SysManage sysManage);

    /**
     *
     * @param name
     * @return
     */
    Map<String, Object> checkName (String name);

    /**
     * 删除
     *
     * @param id
     */
    void delete(String id);
}
