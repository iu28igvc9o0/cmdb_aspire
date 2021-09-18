package com.aspire.ums.cmdb.collect.service.impl;

import com.aspire.ums.cmdb.collect.entity.CollectEmployeeEntity;
import com.aspire.ums.cmdb.collect.entity.CollectEntity;
import com.aspire.ums.cmdb.collect.entity.InsertCollectEntity;
import com.aspire.ums.cmdb.collect.mapper.CollectMapper;
import com.aspire.ums.cmdb.collect.service.CollectEmployeeService;
import com.aspire.ums.cmdb.collect.service.CollectOriginalRecordService;
import com.aspire.ums.cmdb.collect.service.CollectService;
import com.aspire.ums.cmdb.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectServiceImpl
 * Author:   zhu.juwang
 * Date:     2019/3/12 14:23
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private CollectOriginalRecordService recordService;
    @Autowired
    private CollectEmployeeService employeeService;

    @Override
    public List<CollectEntity> getCollectsByModuleId(String moduleId) {
        return collectMapper.getCollectsByModuleId(moduleId);
    }

    @Override
    public List<Map> getCollectListMapByModuleId(String moduleId) {
        return collectMapper.getCollectListMapByModuleId(moduleId);
    }

    @Override
    public void insertVO(CollectEntity collectEntity) {
        collectMapper.insertVO(collectEntity);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Map<String, String> deleteVO(CollectEntity deleteEntity) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            //删除人员配置
            employeeService.deleteVOByCollectId(deleteEntity.getId());
            //删除采集记录
            recordService.deleteVOByCollectId(deleteEntity.getId());
            //删除采集配置
            collectMapper.deleteVO(deleteEntity);
            returnMap.put("code", "success");
        } catch (Exception e) {
            returnMap.put("code", "error");
            returnMap.put("msg", e.getMessage());
            throw e;
        }
        return returnMap;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Map<String, String> saveCollect(InsertCollectEntity collectEntity) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            List<CollectEntity> insertCollects = collectEntity.getInsertCollect();
            if (insertCollects != null && insertCollects.size() > 0) {
                for (CollectEntity entity : insertCollects) {
                    String uuid = UUIDUtil.getUUID();
                    entity.setId(uuid);
                    entity.setVisitType(collectEntity.getVisitType());
                    entity.setVisitRequest(collectEntity.getVisitRequest().toJSONString());
                    insertVO(entity);
                    List<CollectEmployeeEntity> employeeList = entity.getEmployeeList();
                    if (employeeList != null && employeeList.size() > 0) {
                        for (CollectEmployeeEntity employeeEntity : employeeList) {
                            employeeEntity.setCollectId(uuid);
                            employeeEntity.setId(UUIDUtil.getUUID());
                            employeeEntity.setOpertype("collect_notice_employee");
                            //todo 处理人员地址
                            employeeService.insertVO(employeeEntity);
                        }
                    }
                }
            }
            //删除已删除的配置
            List<String> deleteCollects = collectEntity.getDeleteCollect();
            if (deleteCollects != null && deleteCollects.size() > 0) {
                for (String collectId : deleteCollects) {
                    //删除人员配置
                    employeeService.deleteVOByCollectId(collectId);
                    //删除采集记录
                    recordService.deleteVOByCollectId(collectId);
                    //删除采集配置
                    CollectEntity deleteEntity = new CollectEntity();
                    deleteEntity.setId(collectId);
                    deleteVO(deleteEntity);
                }
            }
            returnMap.put("code", "success");
        } catch (Exception e) {
            returnMap.put("code", "error");
            returnMap.put("msg", e.getMessage());
            throw e;
        }
        return returnMap;
    }

    @Override
    public List<CollectEntity> getCollectByFrequency(String frequency) {
        return collectMapper.getCollectByFrequency(frequency);
    }

    @Override
    public CollectEntity getCollectByCollectId(String collectId) {
        return collectMapper.getCollectByCollectId(collectId);
    }
}
