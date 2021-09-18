package com.aspire.ums.cmdb.ipAudit.service.impl;

import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpEntity;
import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnplanPublicIp;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditIntranetIpMapper;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditSurvivingUnplanPublicIpMapper;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditCommonReq;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditCommonResp;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.service.IIpAuditSurvivingUnplanPublicIpService;
import com.aspire.ums.cmdb.util.UUIDUtil;

import java.util.*;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fanwenhui
 * @date 2020-05-29 16:00
 * @description
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class IIpAuditSurvivingUnplanPublicIpServiceImpl implements IIpAuditSurvivingUnplanPublicIpService {

    @Resource
    private IpAuditIntranetIpMapper ipAuditMapper;
    @Resource
    private IpAuditSurvivingUnplanPublicIpMapper mapper;

    @Override
    public void add(IpAuditSurvivingUnplanPublicIp data) {
        mapper.insert(data);
    }

    @Override
    public void batchAdd(List<IpAuditSurvivingUnplanPublicIp> data) {
        mapper.batchInsertIp(data);
    }

    @Override
    public void update(IpAuditSurvivingUnplanPublicIp data) {
        mapper.update(data);
    }

    @Override
    public void delete(IpAuditSurvivingUnplanPublicIp data) {
        mapper.delete(data);
    }

    @Override
    public List<IpAuditCommonResp> getList(IpAuditCommonReq data) {
        return mapper.listByEntity(data);
    }

    @Override
    public int getListCount(IpAuditCommonReq data) {
        return mapper.getListCount(data);
    }

    @Override
    public void generateAuditData() {
        Long maxCheckNum = mapper.getMaxCheckNum();
        Map<String, Map<String, String>> unPlanListMap = new HashMap<>();
        if (maxCheckNum == null || maxCheckNum < 1) { // 第一次比对
            maxCheckNum = 1l;
            unPlanListMap = buildUnPlanListMap(maxCheckNum);
        } else {
            unPlanListMap = buildUnPlanListMap(maxCheckNum);
            maxCheckNum++;
        }
        List<CmdbIpEntity> AllIpList = ipAuditMapper.getPublicIpInfoList();
        List<String> publicIps = mapper.getIpRepositoryPublicIps();
        if (!AllIpList.isEmpty()) {
            List<IpAuditSurvivingUnplanPublicIp> addList = new ArrayList<>();
            for (CmdbIpEntity cmdbIpEntity : AllIpList) {
                String ip = cmdbIpEntity.getIp();
                String dc = cmdbIpEntity.getIdcType();
                String ipDcType = ip + "-" + dc;
                if (!publicIps.contains(ip)) {
                    IpAuditSurvivingUnplanPublicIp addIp = new IpAuditSurvivingUnplanPublicIp();
                    addIp.setId(UUIDUtil.getUUID());
                    addIp.setCheckNum(maxCheckNum);
                    addIp.setDc(dc);
                    addIp.setIp(ip);
                    addIp.setIsDelete(0);
                    addIp.setIsNotify("未通知");
                    addIp.setNetworkIp(cmdbIpEntity.getDeviceIp());
                    addIp.setOperator("admin");
                    addIp.setProcessingStatus("0");
                    Map<String, String> map = unPlanListMap.get(ip);
                    if (null != map) { // 如果包含有上一次暂不处理的IP则本次检测保持和上次一样
                        addIp.setOperator(map.get("operator"));
                        addIp.setReason(map.get("reason"));
                        addIp.setProcessingStatus("1");
                    }
                    addList.add(addIp);
                }
            }
            // 分批次插入数据库，每次1000条
            if (!addList.isEmpty()) {
                int totalSize = addList.size();
                int limitSize = 1000;
                if (totalSize > limitSize) {
                    List<IpAuditSurvivingUnplanPublicIp> list = new ArrayList<>();
                    for (int i=0;i < totalSize;i++) {
                        list.add(addList.get(i));
                        if (i % limitSize == 0) {
                            mapper.batchInsertIp(list);
                            list.clear();
                        }
                    }
                    if (!list.isEmpty()) {
                        mapper.batchInsertIp(list);
                    }
                } else {
                    mapper.batchInsertIp(addList);
                }
            }
        }
    }

    @Override
    public Map<String, Object> getStatisticsData(IpAuditCommonReq req) {
        return mapper.getStatisticsData(req);
    }

    @Override
    public void updateProcess(IpAuditUpdateRequest request) {
    }

    public Map<String,Map<String,String>> buildUnPlanListMap(long maxCheckNum) {
        List<Map<String,String>> unPlanIpList = mapper.getUnPublicIpsByUnStreated(maxCheckNum);
        Map<String,Map<String,String>> retMap = new HashMap<>();
        for (Map<String, String> unPlanMap : unPlanIpList) {
            retMap.put(unPlanMap.get("ip"),unPlanMap);
        }
        return retMap;
    }
}
