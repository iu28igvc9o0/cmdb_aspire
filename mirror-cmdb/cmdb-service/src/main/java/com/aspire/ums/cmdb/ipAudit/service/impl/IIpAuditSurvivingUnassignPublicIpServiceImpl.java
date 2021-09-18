package com.aspire.ums.cmdb.ipAudit.service.impl;

import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpEntity;
import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnassignPublicIp;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditIntranetIpMapper;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditSurvivingUnassignPublicIpMapper;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditSurvivingUnplanPublicIpMapper;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.payload.UnassignPublicIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.UnassignPublicIpResp;
import com.aspire.ums.cmdb.ipAudit.service.IIpAuditSurvivingUnassignPublicIpService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;

import java.util.*;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fanwenhui
 * @date 2020-05-29 15:57
 * @description
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class IIpAuditSurvivingUnassignPublicIpServiceImpl implements IIpAuditSurvivingUnassignPublicIpService {

    @Resource
    private IpAuditIntranetIpMapper ipAuditMapper;
    @Resource
    private IpAuditSurvivingUnassignPublicIpMapper mapper;

    @Override
    public void add(IpAuditSurvivingUnassignPublicIp data) {
        mapper.insert(data);
    }

    @Override
    public void batchAdd(List<IpAuditSurvivingUnassignPublicIp> data) {
        mapper.batchInsertIp(data);
    }

    @Override
    public void update(IpAuditSurvivingUnassignPublicIp data) {
        mapper.update(data);
    }

    @Override
    public void delete(IpAuditSurvivingUnassignPublicIp data) {
        mapper.delete(data);
    }

    @Override
    public List<UnassignPublicIpResp> getList(UnassignPublicIpReq data) {
        return mapper.listByEntity(data);
    }

    @Override
    public int getListCount(UnassignPublicIpReq data) {
        return mapper.getListCount(data);
    }

    @Override
    public void generateAuditData() {
        Long maxCheckNum = mapper.getMaxCheckNum();
        if (maxCheckNum == null || maxCheckNum < 1) { // 第一次比对
            maxCheckNum = 1l;
        } else {
            maxCheckNum++;
        }
        List<CmdbIpEntity> allIpList = ipAuditMapper.getPublicIpInfoList();
        Map<String,Map<String,String>> unAssignMap = buildUnAssignMap();
        try {
            if (!allIpList.isEmpty() && null != unAssignMap) {
                List<IpAuditSurvivingUnassignPublicIp> publicIpAddList = new ArrayList<>();
                List<String> unAssignIpList = new ArrayList<>();
                for (CmdbIpEntity cmdbIpEntity : allIpList) {
                    String ip = cmdbIpEntity.getIp();
                    String dc = cmdbIpEntity.getIdcType();
                    String ipDcType = ip + "-" + dc;
                    Map<String, String> map = unAssignMap.get(ip);
                    if (null == map) {
                        continue;
                    }
                    // 获取当前公网IP地址库的数据，判断该IP的申请人信息是否为空，若为空则为未分配IP
                    if (StringUtils.isEmpty(map.get("requestPerson"))) {
                        IpAuditSurvivingUnassignPublicIp publicIp = new IpAuditSurvivingUnassignPublicIp();
                        publicIp.setId(UUIDUtil.getUUID());
                        publicIp.setCheckNum(maxCheckNum);
                        publicIp.setIp(ip);
                        publicIp.setIsDelete(0);
                        publicIp.setOperator("admin");
                        publicIp.setProcessingStatus("0");
                        publicIpAddList.add(publicIp);
                        unAssignIpList.add(map.get("ip"));
                    }
                }
                // 分批次插入数据库，每次1000条
                if (!publicIpAddList.isEmpty()) {
                    int totalSize = publicIpAddList.size();
                    int limitSize = 1000;
                    if (totalSize > limitSize) {
                        List<IpAuditSurvivingUnassignPublicIp> list = new ArrayList<>();
                        for (int i=0;i < totalSize;i++) {
                            list.add(publicIpAddList.get(i));
                            if (i % limitSize == 0) {
                                mapper.batchInsertIp(list);
                                list.clear();
                            }
                        }
                        if (!list.isEmpty()) {
                            mapper.batchInsertIp(list);
                        }
                    } else {
                        mapper.batchInsertIp(publicIpAddList);
                    }
                }
                if (!unAssignIpList.isEmpty()) {
                    mapper.batchUpdatePublicIpStatus(unAssignIpList);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getStatisticsData(UnassignPublicIpReq req) {
        return mapper.getStatisticsData(req);
    }

    @Override
    public void updateProcess(IpAuditUpdateRequest request) {
        mapper.updateProcess(request);
    }

    @Override
    public void updatePublicInfo(IpAuditUpdateRequest req) {
        if(StringUtils.isNotEmpty(req.getRequestPerson()) || StringUtils.isNotEmpty(req.getRequestTime()) ||
                StringUtils.isNotEmpty(req.getRequestOrder()) || StringUtils.isNotEmpty(req.getExpirationDate())) {
            mapper.updatePublicIpInfo(req);
        }
        if(StringUtils.isNotEmpty(req.getBusinessName1()) || StringUtils.isNotEmpty(req.getBusinessName2()) ||
                StringUtils.isNotEmpty(req.getIpUse())) {
            mapper.updatePublicSegmentInfo(req);
        }
    }

    public Map<String,Map<String,String>> buildUnAssignMap() {
        Map<String,Map<String,String>> retMap = new HashMap<>();
        List<Map<String,String>> publicIps = mapper.getIpRepositoryPublicIps();
        for (Map<String, String> publicIp : publicIps) {
            retMap.put(publicIp.get("ip"),publicIp);
        }
        return retMap;
    }
}
