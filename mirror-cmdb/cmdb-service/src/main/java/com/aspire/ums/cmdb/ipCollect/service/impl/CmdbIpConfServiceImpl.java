package com.aspire.ums.cmdb.ipCollect.service.impl;

import com.aspire.ums.cmdb.ipCollect.mapper.CmdbIpConfPoolMapper;
import com.aspire.ums.cmdb.ipCollect.payload.entity.CmdbIpConfPoolEntity;
import com.aspire.ums.cmdb.ipCollect.service.CmdbIpConfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/5 16:29
 */
@Slf4j
@Service("CmdbIpConfService")
public class CmdbIpConfServiceImpl implements CmdbIpConfService {
    @Autowired
    private CmdbIpConfPoolMapper confPoolMapper;

    @Override
    public void batchAdd(List<CmdbIpConfPoolEntity> data) {
//        List<String> instanceIds = data.stream().map(e -> e.getInstanceId()).collect(Collectors.toList());
//        batchDeleteByInstanceIdw(instanceIds);
        confPoolMapper.batchAdd(data);
    }

    @Override
    public void modify(CmdbIpConfPoolEntity data) {
        confPoolMapper.update(data);
    }

    @Override
    public void updateByInstanceId(Map<String, Object> param) {
        confPoolMapper.updateByInstanceId(param);
    }

    @Override
    public void batchDeleteByInstanceId(List<String> instanceIds) {
        confPoolMapper.batchDeleteByInstanceId(instanceIds);
    }

    @Override
    public void batchDeleteByInstanceIdw(List<String> instanceIds) {
        confPoolMapper.batchDeleteByInstanceIdw(instanceIds);
    }

    @Override
    public List<String> getAllInstanceId() {
        return confPoolMapper.getAllInstanceId();
    }

    @Override
    public List<CmdbIpConfPoolEntity> findByInstanceIdList(List<String> instanceIdList) {
        return confPoolMapper.findByInstanceids(instanceIdList);
    }

    @Override
    public List<CmdbIpConfPoolEntity> findDataByIpList(List<String> ipList) {
        return confPoolMapper.findDataByIpList(ipList);
    }
}
