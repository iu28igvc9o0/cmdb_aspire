package com.aspire.ums.cmdb.ipCollect.service.impl;

import com.aspire.ums.cmdb.ipCollect.mapper.CmdbIpArpPoolMapper;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpArpPoolEntity;
import com.aspire.ums.cmdb.ipCollect.service.CmdbIpArpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/5 17:31
 */
@Slf4j
@Service("CmdbIpArpService")
public class CmdbIpArpServiceImpl implements CmdbIpArpService {
    @Autowired
    private CmdbIpArpPoolMapper arpPoolMapper;

    @Override
    public void batchAdd(List<CmdbIpArpPoolEntity> data) {
//        List<String> instanceIds = data.stream().map(e -> e.getInstanceId()).collect(Collectors.toList());
//        batchDeleteByInstanceIdw(instanceIds);
        arpPoolMapper.batchAdd(data);
    }

    @Override
    public void modify(CmdbIpArpPoolEntity data) {
        arpPoolMapper.update(data);
    }

    @Override
    public void updateByInstanceId(Map<String, Object> param) {
        arpPoolMapper.updateByInstanceId(param);
    }


    @Override
    public void batchDeleteByInstanceId(List<String> instanceIds) {
        arpPoolMapper.batchDeleteByInstanceId(instanceIds);
    }

    @Override
    public void batchDeleteByInstanceIdw(List<String> instanceIds) {
        arpPoolMapper.batchDeleteByInstanceIdw(instanceIds);
    }

    @Override
    public List<String> getAllInstanceId() {
        return arpPoolMapper.getAllInstanceId();
    }

    @Override
    public List<CmdbIpArpPoolEntity> findByInstanceIdList(List<String> instanceIdList) {
        return arpPoolMapper.findByInstanceids(instanceIdList);
    }

    @Override
    public List<CmdbIpArpPoolEntity> findDataByIpList(List<String> ipList) {
        return arpPoolMapper.findDataByIpList(ipList);
    }
}
