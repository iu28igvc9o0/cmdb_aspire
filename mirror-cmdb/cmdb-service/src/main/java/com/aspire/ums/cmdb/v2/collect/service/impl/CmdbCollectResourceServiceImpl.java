package com.aspire.ums.cmdb.v2.collect.service.impl;

import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectResource;
import com.aspire.ums.cmdb.v2.collect.mapper.CmdbCollectResourceMapper;
import com.aspire.ums.cmdb.v2.collect.service.CmdbCollectResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* 描述：
* @author
* @date 2019-06-18 20:55:57
*/
@Service
public class CmdbCollectResourceServiceImpl implements CmdbCollectResourceService {

    @Autowired
    private CmdbCollectResourceMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbCollectResource> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbCollectResource get(CmdbCollectResource entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    public void insert(CmdbCollectResource entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(CmdbCollectResource entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(CmdbCollectResource entity) {
        mapper.delete(entity);
    }
}