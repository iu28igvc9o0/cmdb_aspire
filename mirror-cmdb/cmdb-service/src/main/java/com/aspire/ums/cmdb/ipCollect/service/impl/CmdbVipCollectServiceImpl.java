package com.aspire.ums.cmdb.ipCollect.service.impl;

import com.aspire.ums.cmdb.ipCollect.mapper.CmdbVipCollectMapper;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbVipCollectEntity;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbVipCollectRequest;
import com.aspire.ums.cmdb.ipCollect.service.CmdbVipCollectService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/19 18:30
 */
@Slf4j
@Service("CmdbVipConllectService")
@Transactional(rollbackFor = Exception.class)
public class CmdbVipCollectServiceImpl implements CmdbVipCollectService {
    @Autowired
    private CmdbVipCollectMapper cmdbVipCollectMapper;

    @Override
    public void batchAdd(List<CmdbVipCollectEntity> data) {
        List<String> delList = data.stream().map(e -> e.getInstanceId()).collect(Collectors.toList());
        cmdbVipCollectMapper.batchDeleteByInstanceId2(delList);
        cmdbVipCollectMapper.batchAdd(data);
    }

    @Override
    public void modify(CmdbVipCollectEntity data) {
        cmdbVipCollectMapper.update(data);
    }

    @Override
    public void updateByInstanceId(Map<String, Object> param) {
        cmdbVipCollectMapper.updateByInstanceId(param);
    }

    @Override
    public void deleteByInstanceId(String instanceId) {
        cmdbVipCollectMapper.deleteByInstanceId(instanceId);
    }

    @Override
    public void batchDeleteByInstanceId(List<String> instanceId) {
        cmdbVipCollectMapper.batchDeleteByInstanceId(instanceId);
    }

    @Override
    public List<CmdbVipCollectEntity> findData(CmdbVipCollectRequest data) {
        return cmdbVipCollectMapper.findPage(data);
    }

    @Override
    public int findDataCount(CmdbVipCollectRequest data) {
        return cmdbVipCollectMapper.findPageCount(data);
    }

    @Override
    public CmdbVipCollectEntity findDataByInstanceId(String instanceId) {
        CmdbVipCollectRequest request = new CmdbVipCollectRequest();
        request.setInstanceId(instanceId);
        List<CmdbVipCollectEntity> list = cmdbVipCollectMapper.findPage(request);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Map<String, String>> getResource() {
        List<Map<String, String>> resultList = Lists.newArrayList();
        List<String> resourceList = cmdbVipCollectMapper.findResourceList();
        resourceList.stream().forEach(e -> {
            Map<String, String> dataMap = Maps.newHashMap();
            dataMap.put("value", e);
            dataMap.put("label", e);
            resultList.add(dataMap);
        });
        return resultList;
    }

    @Override
    public List<String> getAllInstanceId() {
        return cmdbVipCollectMapper.getAllInstanceId();
    }

    @Override
    public List<CmdbVipCollectEntity> findByInstanceIdList(List<String> instanceIdList) {
        return cmdbVipCollectMapper.findByInstanceIdList(instanceIdList);
    }

    @Override
    public List<Map<String, String>> getUseType() {
        List<Map<String, String>> resultList = Lists.newArrayList();
        List<String> useTypeList = cmdbVipCollectMapper.findUseTypeList();
        useTypeList.stream().forEach(e -> {
            Map<String, String> dataMap = Maps.newHashMap();
            dataMap.put("value", e);
            dataMap.put("label", e);
            resultList.add(dataMap);
        });
        return resultList;
    }

    @Override
    public List<String> findAllVip() {
        return cmdbVipCollectMapper.findAllVip();
    }
}
