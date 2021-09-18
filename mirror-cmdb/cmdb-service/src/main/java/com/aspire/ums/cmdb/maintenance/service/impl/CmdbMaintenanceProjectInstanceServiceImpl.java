package com.aspire.ums.cmdb.maintenance.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.mapper.CmdbMaintenanceProjectInstanceMapper;
import com.aspire.ums.cmdb.maintenance.payload.*;
import com.aspire.ums.cmdb.maintenance.service.ICmdbMaintenanceProjectInstanceService;
import com.aspire.ums.cmdb.maintenance.service.ICmdbMaintenanceProjectService;
import com.aspire.ums.cmdb.maintenance.service.IHardWareService;
import com.aspire.ums.cmdb.maintenance.service.IHardWareUseService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.netflix.discovery.converters.Auto;
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
public class CmdbMaintenanceProjectInstanceServiceImpl implements ICmdbMaintenanceProjectInstanceService {

    @Autowired
    private CmdbMaintenanceProjectInstanceMapper mapper;
    @Autowired
    private IHardWareService hardWareService;
    @Autowired
    private IHardWareUseService hardWareUseService;


    /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    public List<CmdbMaintenanceProjectInstance> list() {
        return mapper.list();
    }

    @Override
    public Result<CmdbMaintenanceProjectBindInstance> getProjectBindInstanceList(CmdbMaintenanceProjectBindInstanceQuery query) {
        if (StringUtils.isNotEmpty(query.getPageNo()) && StringUtils.isNotEmpty(query.getPageSize())) {

            query.setPageNo((query.getPageNo() - 1) * query.getPageSize());
        }
        List<CmdbMaintenanceProjectBindInstance> list = mapper.getProjectBindInstanceList(query);
        Integer totalCount = mapper.getProjectBindInstanceListCount(query);
        return new Result<>(totalCount, list);
    }

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回指定ID的数据信息
     */
    public CmdbMaintenanceProjectInstance get(CmdbMaintenanceProjectInstance entity) {
        return mapper.get(entity);
    }

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void insert(CmdbMaintenanceProjectInstance entity) {
        entity.setId(UUIDUtil.getUUID());
        mapper.insert(entity);
        Hardware hardware = new Hardware();
        hardware.setProjectInstanceId(entity.getId());
        hardWareService.update(hardware);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void batchInsert(List<CmdbMaintenanceProjectInstance> listEntity, String projectId) {
        if(listEntity != null && listEntity.size() > 0) {
            listEntity.forEach((entity) -> {
                entity.setProjectId(projectId);
                this.insert(entity);
            });
        }
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void update(CmdbMaintenanceProjectInstance entity) {
        mapper.update(entity);
    }

    @Override
    public List<CmdbMaintenanceProjectInstance> getProjectInstanceListByProjectId(String projectId) {
        return mapper.getProjectInstanceListByProjectId(projectId);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public JSONObject delete(String projectInstanceId, String projectId) {
        JSONObject returnJson = new JSONObject();
        // 判断设备序列号是否被硬件维保使用记录使用
        List<HardwareUse> hardwareUseList = hardWareUseService.getHardWareUseByProjectInstanceId(projectInstanceId);
        if (hardwareUseList != null && hardwareUseList.size() > 0) {
            returnJson.put("flag", "error");
            returnJson.put("msg", "该设备序列号已被硬件维保记录使用, 无法删除.");
            return returnJson;
        }
        CmdbMaintenanceProjectInstance instance = new CmdbMaintenanceProjectInstance();
        instance.setProjectId(projectId);
        instance.setId(projectInstanceId);
        mapper.delete(instance);
        returnJson.put("flag", "success");
        return returnJson;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class, SQLException.class})
    public void batchRemove(List<CmdbMaintenanceProjectInstance> projectInstanceList, String projectId) {
        if (projectInstanceList != null && projectInstanceList.size() > 0) {
            projectInstanceList.forEach((instance) -> {
                instance.setProjectId(projectId);
                mapper.delete(instance);
            });
        }
    }
}