package com.aspire.ums.cmdb.serverProject.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.serverProject.mapper.CmdbNetworkLineBillMapper;
import com.aspire.ums.cmdb.serverProject.payload.CmdbNetworkLineBill;
import com.aspire.ums.cmdb.serverProject.service.ICmdbNetworkLineBillService;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.google.common.base.Preconditions;

/**
 * 描述：
 * 
 * @author
 * @date 2021-01-27 17:26:15
 */
@Service
public class CmdbNetworkLineBillServiceImpl implements ICmdbNetworkLineBillService {

    @Autowired
    private CmdbNetworkLineBillMapper mapper;

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbNetworkLineBill> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回指定ID的数据信息
     */
    @Override
    public CmdbNetworkLineBill get(CmdbNetworkLineBill entity) {
        Preconditions.checkArgument(StringUtils.isNotBlank(entity.getId()), "id cannot be null!");
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    public void insert(CmdbNetworkLineBill entity) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity.setId(UUIDUtil.getUUID());
        }
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    public void update(CmdbNetworkLineBill entity) {
        Preconditions.checkArgument(StringUtils.isNotBlank(entity.getId()), "id cannot be null!");
        mapper.update(entity);
    }

    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    @Override
    public void delete(CmdbNetworkLineBill entity) {
        Preconditions.checkArgument(StringUtils.isNotBlank(entity.getId()), "id cannot be null!");
        mapper.delete(entity);
    }
}
