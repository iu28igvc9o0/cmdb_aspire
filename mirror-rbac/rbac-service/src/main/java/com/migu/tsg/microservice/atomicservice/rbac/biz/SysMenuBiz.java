package com.migu.tsg.microservice.atomicservice.rbac.biz;

import com.migu.tsg.microservice.atomicservice.rbac.dao.po.SysMenu;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SysMenuBiz
 * @Description: TODO
 * @Author baiwenping
 * @Date 2019/11/26
 * @Version V1.0
 **/
public interface SysMenuBiz {

    /**
     *
     * @return
     */
    List<SysMenu> listAll (String sysId) ;

    /**
     *
     * @return
     */
    Map listBySysName (String manageCode) ;

    /**
     *
     * @param sysMenu
     * @return
     */
    SysMenu insert (SysMenu sysMenu);

    /**
     *
     * @param sysMenu
     * @return
     */
    void update (SysMenu sysMenu);

    /**
     * 删除
     *
     * @param id
     */
    void delete(String id);

    /**
     *
     * @param id
     * @return
     */
    SysMenu selectById(String id);
}
