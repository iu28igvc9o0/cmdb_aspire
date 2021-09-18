package com.aspire.ums.cmdb.sync.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.sync.entity.CmdbZabbixMaintenance;
import com.aspire.ums.cmdb.sync.mapper.CmdbZabbixMaintenanceMapper;
import com.aspire.ums.cmdb.sync.service.CmdbZabbixMaintenanceService;
import com.google.common.base.Preconditions;

/**
 * @since 2020年08月27日 14:51:44
 * @author jiangxuwen
 * @version v1.0
 */
@Transactional
@Service("cmdbZabbixMaintenanceService")
public class CmdbZabbixMaintenanceServiceImpl implements CmdbZabbixMaintenanceService {

    @Autowired
    private CmdbZabbixMaintenanceMapper cmdbZabbixMaintenanceMapper;

    /**
     * 根据主键查询对象
     * 
     * @param id
     * @return
     * @since 2020年08月27日 14:51:44
     * @author jiangxuwen
     * @version v1.0
     */
    @Transactional(readOnly = true)
    @Override
    public CmdbZabbixMaintenance findById(String id) {
        return cmdbZabbixMaintenanceMapper.findById(id);
    }

    /**
     * 分页查询
     * 
     * @param cmdbZabbixMaintenance
     * @return
     * @since 2020年08月27日 14:51:44
     * @author jiangxuwen
     * @version v1.0
     */
    @Transactional(readOnly = true)
    @Override
    public List<CmdbZabbixMaintenance> findPage(CmdbZabbixMaintenance cmdbZabbixMaintenance) {
        return cmdbZabbixMaintenanceMapper.findPage(cmdbZabbixMaintenance);
    }

    /**
     * 新增
     * 
     * @param cmdbZabbixMaintenance
     * @since 2020年08月27日 14:51:44
     * @author jiangxuwen
     * @version v1.0
     */
    @Override
    public void add(CmdbZabbixMaintenance cmdbZabbixMaintenance) {
        Preconditions.checkArgument(StringUtils.isNotBlank(cmdbZabbixMaintenance.getId()), "id 不能为空");
        cmdbZabbixMaintenanceMapper.insert(cmdbZabbixMaintenance);

    }

    /**
     * 更新
     * 
     * @param cmdbZabbixMaintenance
     * @since 2020年08月27日 14:51:44
     * @author jiangxuwen
     * @version v1.0
     */
    @Override
    public void modify(CmdbZabbixMaintenance cmdbZabbixMaintenance) {
        Preconditions.checkArgument(StringUtils.isNotBlank(cmdbZabbixMaintenance.getId()), "id 不能为空");
        CmdbZabbixMaintenance entity = cmdbZabbixMaintenanceMapper.findById(cmdbZabbixMaintenance.getId());
        if (entity == null) {
            cmdbZabbixMaintenanceMapper.insert(cmdbZabbixMaintenance);
        } else {
            cmdbZabbixMaintenanceMapper.update(cmdbZabbixMaintenance);
        }
    }

    /**
     * 删除
     * 
     * @param id
     * @since 2020年08月27日 14:51:44
     * @author jiangxuwen
     * @version v1.0
     */
    @Override
    public void delete(String id) {
        cmdbZabbixMaintenanceMapper.delete(id);
    }

}
