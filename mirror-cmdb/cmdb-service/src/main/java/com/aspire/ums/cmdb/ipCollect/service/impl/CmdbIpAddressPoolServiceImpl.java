package com.aspire.ums.cmdb.ipCollect.service.impl;

import com.aspire.ums.cmdb.ipCollect.mapper.CmdbIpAddressPoolMapper;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpAddressPoolEntity;
import com.aspire.ums.cmdb.ipCollect.service.CmdbIpAddressPoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/19 18:09
 */
@Slf4j
@Service("CmdbIpConllectService")
@Transactional(rollbackFor = Exception.class)
public class CmdbIpAddressPoolServiceImpl implements CmdbIpAddressPoolService {

    @Autowired
    private CmdbIpAddressPoolMapper cmdbIpAddressPoolMapper;

    @Override
    public void batchAdd(List<CmdbIpAddressPoolEntity> data) {
//        List<String> instanceIds = data.stream().map(e -> e.getInstanceId()).collect(Collectors.toList());
//        batchDeleteByInstanceIdw(instanceIds);
        cmdbIpAddressPoolMapper.batchAdd(data);
    }

    @Override
    public void modify(CmdbIpAddressPoolEntity data) {
        cmdbIpAddressPoolMapper.update(data);
    }

    @Override
    public void updateByInstanceId(Map<String, Object> param) {
        cmdbIpAddressPoolMapper.updateByInstanceId(param);
    }

    @Override
    public void batchDeleteByInstanceId(List<String> instanceId) {
        cmdbIpAddressPoolMapper.batchDeleteByInstanceId(instanceId);
    }

    @Override
    public void batchDeleteByInstanceIdw(List<String> instanceIds) {
        cmdbIpAddressPoolMapper.batchDeleteByInstanceIdw(instanceIds);
    }

    @Override
    public List<String> getAllInstanceId() {
        return cmdbIpAddressPoolMapper.getAllInstanceId();
    }

    @Override
    public List<CmdbIpAddressPoolEntity> findByInstanceIdList(List<String> instanceIdList) {
        return cmdbIpAddressPoolMapper.findByInstanceIdList(instanceIdList);
    }

    @Override
    public List<CmdbIpAddressPoolEntity> findDataByIpList(List<String> ipList) {
        return cmdbIpAddressPoolMapper.findDataByIpList(ipList);
    }
}
