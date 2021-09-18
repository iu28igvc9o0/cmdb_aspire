package com.aspire.ums.cmdb.allocate.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aspire.ums.cmdb.allocate.mapper.AllocateIpConfigMapper;
import com.aspire.ums.cmdb.allocate.service.AllocateIpConfigService;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class AllocateIpConfigServiceImpl implements AllocateIpConfigService {

    @Autowired
    private AllocateIpConfigMapper allocateIpConfigMapper;

    @Override
    public Result<AllocateIpConfigRes> getAllocateIpConfigData(int pageNum,
                                                               int startPageNum,
                                                               int pageSize,
                                                               int vpnId,
                                                               int networkId,
                                                               String bizSystem,
                                                               String ip,
                                                               String privateIp,
                                                               boolean isAdd) {
        AllocateIpConfigListReq request = new AllocateIpConfigListReq();
        request.setPageNum(pageNum);
        request.setStartPageNum(startPageNum);
        request.setPageSize(pageSize);
        request.setVpnId(vpnId);
        request.setNetworkId(networkId);
        request.setBizSystem(bizSystem);
        request.setIp(ip);
        request.setPrivateIp(privateIp);
        if (!isAdd) {
            request.setStartPageNum((pageNum - 1) * pageSize);
        } else {
            request.setStartPageNum(-1);
        }
        Result<AllocateIpConfigRes> response = new Result<AllocateIpConfigRes>();
        int allocateIpConfigDataCount = allocateIpConfigMapper.getAllocateIpConfigDataCount(request);
        response.setCount(allocateIpConfigDataCount);
        if (allocateIpConfigDataCount > 0) {
            response.setData(allocateIpConfigMapper.getAllocateIpConfigData(request));
        }
        log.info("response is {}",response);
        return response;
    }

    @Override
    public String insertAllocateIpConfig(List<AllocateIpConfigDetail> request, String name) {
        log.info("request is {}",request);
        if (request.isEmpty()) {
            log.warn("request is null");
            return null;
        }
        try {
            allocateIpConfigMapper.insertAllocateIpConfig(request);
            AllocateIpConfigOperationReq allocateIpConfigOperationReq = new AllocateIpConfigOperationReq();
            allocateIpConfigOperationReq.setId(UUID.randomUUID().toString());
            allocateIpConfigOperationReq.setType(0);
            allocateIpConfigOperationReq.setOperator(name);
            allocateIpConfigOperationReq.setOperationTime(new Date());
            allocateIpConfigMapper.insertAllocateIpConfigOperation(allocateIpConfigOperationReq);
            return "success";
        } catch (Exception e) {
            log.error("[ERROR] >>> ", e);
            return "error";
        }
    }

    @Override
    public String deleteAllocateIpConfigById(String ids, String name) {
        log.info("request is {}",ids);
        if (ids.isEmpty()) {
            log.warn("request is empty");
            return null;
        }
        try {
            String[] aa = ids.split(",");
            List<Long> id = Lists.newArrayList();
            for (String str : aa) {
                id.add(Long.valueOf(str));
            }
            allocateIpConfigMapper.deleteAllocateIpConfigById(id);
            AllocateIpConfigOperationReq allocateIpConfigOperationReq = new AllocateIpConfigOperationReq();
            allocateIpConfigOperationReq.setId(UUID.randomUUID().toString());
            allocateIpConfigOperationReq.setAllocateIpId(ids);
            allocateIpConfigOperationReq.setType(1);
            allocateIpConfigOperationReq.setOperator(name);
            allocateIpConfigOperationReq.setOperationTime(new Date());
            allocateIpConfigMapper.insertAllocateIpConfigOperation(allocateIpConfigOperationReq);
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    @Override
    public List<Map<String, Object>> exportAllocateIpConfig(AllocateIpConfigListReq request) {
        log.info("request is {}",request);
        return allocateIpConfigMapper.exportAllocateIpConfigData(request);
    }

    @Override
    public List<Map<String, Object>> getVpnData() {
        return allocateIpConfigMapper.getVpnData();
    }

    @Override
    public List<Map<String, Object>> getNetworkById(long id) {
        return allocateIpConfigMapper.getNetworkById(id);
    }
}
