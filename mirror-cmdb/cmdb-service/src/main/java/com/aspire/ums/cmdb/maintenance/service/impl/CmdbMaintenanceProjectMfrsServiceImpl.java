package com.aspire.ums.cmdb.maintenance.service.impl;

import com.aspire.ums.cmdb.maintenance.mapper.CmdbMaintenanceProjectMfrsMapper;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceProjectMfrs;
import com.aspire.ums.cmdb.maintenance.service.ICmdbMaintenanceProjectMfrsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
* 描述：
* @author
* @date 2019-07-29 22:31:45
*/
@Service
public class CmdbMaintenanceProjectMfrsServiceImpl implements ICmdbMaintenanceProjectMfrsService {

    @Autowired
    private CmdbMaintenanceProjectMfrsMapper mapper;

    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbMaintenanceProjectMfrs> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbMaintenanceProjectMfrs get(CmdbMaintenanceProjectMfrs entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void insert(CmdbMaintenanceProjectMfrs entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void update(CmdbMaintenanceProjectMfrs entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void delete(CmdbMaintenanceProjectMfrs entity) {
        mapper.delete(entity);
    }

    /**
     * 删除实例 根据项目id ， 主要用于删除所有关联的厂家联系人
     * @param entity 实例数据
     * @return
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void deleteByProjectId(CmdbMaintenanceProjectMfrs entity) {
        mapper.deleteByProjectId(entity);
    }

    @Override
    public List<CmdbMaintenanceProjectMfrs> getMfrsListByProjectId(String projectId) {
        return mapper.getMfrsListByProjectId(projectId);
    }
}