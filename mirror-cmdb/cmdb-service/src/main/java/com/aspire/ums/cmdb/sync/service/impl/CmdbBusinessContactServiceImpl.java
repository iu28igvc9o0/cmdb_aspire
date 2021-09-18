package com.aspire.ums.cmdb.sync.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aspire.ums.cmdb.sync.entity.CmdbBusinessContact;
import com.aspire.ums.cmdb.sync.mapper.CmdbBusinessContactMapper;
import com.aspire.ums.cmdb.sync.service.ICmdbBusinessContactService;
import com.aspire.ums.cmdb.util.UUIDUtil;

/**
 * 描述：
 * 
 * @author
 * @date 2020-05-20 09:16:01
 */
@Service
public class CmdbBusinessContactServiceImpl implements ICmdbBusinessContactService {

    @Autowired
    private CmdbBusinessContactMapper mapper;

    /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    @Override
    public List<CmdbBusinessContact> list() {
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
    public CmdbBusinessContact get(CmdbBusinessContact entity) {
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
    public void insert(CmdbBusinessContact entity) {
        if (StringUtils.isBlank(entity.getId())) {
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
    public void update(CmdbBusinessContact entity) {
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
    public void delete(CmdbBusinessContact entity) {
        mapper.delete(entity);
    }
}
