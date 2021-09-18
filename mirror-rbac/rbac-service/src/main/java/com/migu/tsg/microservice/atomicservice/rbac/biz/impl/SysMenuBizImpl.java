package com.migu.tsg.microservice.atomicservice.rbac.biz.impl;

import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.rbac.biz.SysMenuBiz;
import com.migu.tsg.microservice.atomicservice.rbac.dao.ResourceSchemaDao;
import com.migu.tsg.microservice.atomicservice.rbac.dao.SysManageMapper;
import com.migu.tsg.microservice.atomicservice.rbac.dao.SysMenuMapper;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.ResourceSchema;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.SysManage;
import com.migu.tsg.microservice.atomicservice.rbac.dao.po.SysMenu;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SysMenuBizImpl
 * @Description: TODO
 * @Author baiwenping
 * @Date 2019/11/26
 * @Version V1.0
 **/
@Service
public class SysMenuBizImpl implements SysMenuBiz {
    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Autowired
    private SysManageMapper sysManageMapper;

    @Autowired
    private ResourceSchemaDao resourceSchemaDao;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     *
     * @param sysId
     * @return
     */
    @Override
    public List<SysMenu> listAll(String sysId) {
        List<SysMenu> list = sysMenuMapper.selectBySysId(sysId);
        return list;
    }

    /**
     *
     * @param manageCode
     * @return
     */
    @Override
    public Map listBySysName(String manageCode) {
        Map map = Maps.newHashMap();
        if (StringUtils.isEmpty(manageCode)) {
            return map;
        }
        if (redisTemplate.hasKey(manageCode)) {
            map = (Map)redisTemplate.opsForValue().get(manageCode);
        } else {
            List<SysMenu> sysMenus = sysMenuMapper.selectBySysName(manageCode);
            List<Map> menuList = Lists.newArrayList();
            for (SysMenu sysMenu: sysMenus) {
                menuList.add(generateMenus(sysMenu));
            }
            map.put("homeRouters", "");
            map.put("categories", menuList);
            redisTemplate.opsForValue().set(manageCode, map);
            redisTemplate.expire(manageCode, 24, TimeUnit.HOURS);
        }
        return map;
    }

    /**
     * 根据表结构生成菜单路由
     * @param sysMenu
     * @return
     */
    private Map generateMenus (SysMenu sysMenu) {
        Map map = Maps.newHashMap();
        if (null == sysMenu) {
            return map;
        }
        map.put("id", sysMenu.getId());
        if (!StringUtils.isEmpty(sysMenu.getBase())) {
            map.put("base", sysMenu.getBase());
        }
        if (!StringUtils.isEmpty(sysMenu.getName())) {
            map.put("name", sysMenu.getName());
        }
        if (!StringUtils.isEmpty(sysMenu.getIcon())) {
            map.put("icon", sysMenu.getIcon());
        }
        if (!StringUtils.isEmpty(sysMenu.getPath())) {
            map.put("path", sysMenu.getPath());
        }
        if (!StringUtils.isEmpty(sysMenu.getComponent())) {
            map.put("component", sysMenu.getComponent());
        }
        if (!StringUtils.isEmpty(sysMenu.getUrl())) {
            map.put("url", sysMenu.getUrl());
        }
        if ("0".equals(sysMenu.getIsShow())) {
            map.put("show", false);
        } else {
            map.put("show", true);
        }
        List<SysMenu> childrenList = sysMenu.getChildren();
        if (CollectionUtils.isNotEmpty(childrenList)) {
            List<Map> childrenMenus = Lists.newArrayList();
            for (SysMenu childMenu: childrenList) {
                childrenMenus.add(generateMenus(childMenu));
            }
            map.put(sysMenu.getMenuType(), childrenMenus);
        }

        return map;
    }

    /**
     * 刷新redis数据
     * @param sysId
     */
    private void flushMenu(String sysId) {
        SysManage sysManage = sysManageMapper.selectById(sysId);
        String manageCode = sysManage.getManageCode();
        List<SysMenu> sysMenus = sysMenuMapper.selectBySysName(manageCode);
        Map map = Maps.newHashMap();
        List<Map> menuList = Lists.newArrayList();
        for (SysMenu sysMenu: sysMenus) {
            menuList.add(generateMenus(sysMenu));
        }
        map.put("homeRouters", "");
        map.put("categories", menuList);
        redisTemplate.opsForValue().set(manageCode, map);
        redisTemplate.expire(manageCode, 24, TimeUnit.HOURS);
    }

    /**
     *
     * @param sysMenu
     * @return
     */
    @Override
    @Transactional
    public SysMenu insert(SysMenu sysMenu) {
        sysMenu.setId(UUID.randomUUID().toString());
        sysMenuMapper.insertSelective(sysMenu);
        // 添加Resource schema
        if (!sysMenu.getMenuType().equals("vue")) {
            ResourceSchema resourceSchema = new ResourceSchema();
            resourceSchema.setResource(sysMenu.getId());
            resourceSchema.setName(sysMenu.getName());
            if (sysMenu.getParentId().equals("-1")) {
                resourceSchema.setParentResource("all");
            } else {
                resourceSchema.setParentResource(sysMenu.getParentId());
            }
            resourceSchema.setGeneral("f");
            resourceSchema.setCreatedAt(new Timestamp(new Date().getTime()));
            resourceSchemaDao.insert(resourceSchema);
        }
        flushMenu(sysMenu.getSystemId());
        return sysMenu;
    }

    /**
     *
     * @param sysMenu
     */
    @Override
    public void update(SysMenu sysMenu) {
        sysMenuMapper.updateByPrimaryKey(sysMenu);
        // 修改资源
        if (!sysMenu.getMenuType().equals("vue")) {
            ResourceSchema result = resourceSchemaDao.selectByResource(sysMenu.getId());
            ResourceSchema resourceSchema = new ResourceSchema();
            resourceSchema.setResource(sysMenu.getId());
            resourceSchema.setName(sysMenu.getName());
            if (result == null) {
                if (sysMenu.getParentId().equals("-1")) {
                    resourceSchema.setParentResource("all");
                } else {
                    resourceSchema.setParentResource(sysMenu.getParentId());
                }
//                resourceSchema.setParentResource(sysMenu.getParentId());
                resourceSchema.setGeneral("f");
                resourceSchema.setCreatedAt(new Timestamp(new Date().getTime()));
                resourceSchemaDao.insert(resourceSchema);
            } else {
                resourceSchemaDao.updateName(resourceSchema);
            }
        } else {
            resourceSchemaDao.deleteByResource(sysMenu.getId());
        }
        flushMenu(sysMenu.getSystemId());
    }

    /**
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        SysMenu sysMenu = sysMenuMapper.selectById(id);
        sysMenuMapper.deleteByPrimaryKey(id);
        // 删除资源
        resourceSchemaDao.deleteByResource(id);
        flushMenu(sysMenu.getSystemId());
    }

    /**
     *
     * @param id
     * @return
     */
    public SysMenu selectById(String id) {
        SysMenu sysMenu = sysMenuMapper.selectById(id);
        if (sysMenu == null) {
            return null;
        }
        SysManage sysManage = sysManageMapper.selectById(sysMenu.getSystemId());
        if (sysManage != null) {
            sysMenu.setSystemName(sysManage.getName());
        }
        return sysMenu;
    }
}
