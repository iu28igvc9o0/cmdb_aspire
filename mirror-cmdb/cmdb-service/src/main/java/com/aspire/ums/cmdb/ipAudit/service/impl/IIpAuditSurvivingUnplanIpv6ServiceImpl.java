package com.aspire.ums.cmdb.ipAudit.service.impl;

import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpEntity;
import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnplanIpV6;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditIntranetIpMapper;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditSurvivingUnplanIpV6Mapper;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditCommonReq;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditCommonResp;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.service.IIpAuditSurvivingUnplanIpv6Service;
import com.aspire.ums.cmdb.util.IpUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author fanwenhui
 * @date 2020-05-27 11:19
 * @description ip稽核-存活未规划IP（IPv6） 业务逻辑具体实现
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class IIpAuditSurvivingUnplanIpv6ServiceImpl implements IIpAuditSurvivingUnplanIpv6Service {

    @Resource
    private IpAuditSurvivingUnplanIpV6Mapper mapper;
    @Resource
    private IpAuditIntranetIpMapper ipAuditMapper;


    @Override
    public void add(IpAuditSurvivingUnplanIpV6 data) {
        mapper.insert(data);
    }

    @Override
    public void batchAdd(List<IpAuditSurvivingUnplanIpV6> data) {
        mapper.batchInsertIp(data);
    }

    @Override
    public void update(IpAuditSurvivingUnplanIpV6 data) {
        mapper.update(data);
    }

    @Override
    public void delete(IpAuditSurvivingUnplanIpV6 data) {
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
    public Map<String, Object> getStatisticsData(IpAuditCommonReq  req) {
        return mapper.getStatisticsData(req);
    }

    @Override
    public void updateProcess(IpAuditUpdateRequest request) {
    }

    @Override
    public void generateAuditData() {
        Long maxCheckNum = mapper.getMaxCheckNum();
        Map<String, Map<String, String>> unPlanListMap = new HashMap<>();
        if (maxCheckNum == null || maxCheckNum < 1) { // 第一次比对
            maxCheckNum = 1l;
            unPlanListMap = buildIpv6Map(maxCheckNum);
        } else {
            unPlanListMap = buildIpv6Map(maxCheckNum);
            maxCheckNum++;
        }
        List<CmdbIpEntity> allIpv6List = getAllIpv6List();
        // 获取IPv6地址库中未删除的IPv6
        List<String> exitIpv6s = mapper.getIpRepositoryIpv6s();
        // 开始比对全量存活IP和IPv6地址库，并将比对结果录入 IPV6存活未规划IP 数据库
        if (!allIpv6List.isEmpty()) {
            List<IpAuditSurvivingUnplanIpV6> ipv6AddList = new ArrayList<>();
            for (CmdbIpEntity cmdbIpEntity : allIpv6List) {
                String ip = cmdbIpEntity.getIp();
                String dc = cmdbIpEntity.getIdcType();
                String ipIdc = ip + "-" + dc;
                if (!exitIpv6s.contains(ip)) {
                    IpAuditSurvivingUnplanIpV6 ipv6Add = new IpAuditSurvivingUnplanIpV6();
                    ipv6Add.setId(UUIDUtil.getUUID());
                    ipv6Add.setDc(dc);
                    ipv6Add.setIp(ip);
                    ipv6Add.setCheckNum(maxCheckNum);
                    ipv6Add.setIsDelete(0);
                    ipv6Add.setIsNotify("未通知");
                    ipv6Add.setNetworkIp(cmdbIpEntity.getDeviceIp());
                    ipv6Add.setOperator("admin");
                    ipv6Add.setProcessingStatus("0");
                    Map<String, String> map = unPlanListMap.get(ip);
                    if (null != map) { // 如果包含有上一次暂不处理的IP则本次检测保持和上次一样
                        ipv6Add.setOperator(map.get("operator"));
                        ipv6Add.setReason(map.get("reason"));
                        ipv6Add.setProcessingStatus("1");
                    }
                    ipv6AddList.add(ipv6Add);
                }
            }
            // 分批次插入数据库，每次1000条
            if (!ipv6AddList.isEmpty()) {
                int totalSize = ipv6AddList.size();
                int limitSize = 1000;
                if (totalSize > limitSize) {
                    List<IpAuditSurvivingUnplanIpV6> list = new ArrayList<>();
                    for (int i=0;i < totalSize;i++) {
                        list.add(ipv6AddList.get(i));
                        if (i % limitSize == 0) {
                            mapper.batchInsertIp(list);
                            list.clear();
                        }
                    }
                    if (!list.isEmpty()) {
                        mapper.batchInsertIp(list);
                    }
                } else {
                    mapper.batchInsertIp(ipv6AddList);
                }
            }
        }
    }

    public Map<String,Map<String,String>> buildIpv6Map(long maxCheckNum) {
        List<Map<String, String>> unPlanIpList = mapper.getUnIpv6sByUnStreated(maxCheckNum);
        Map<String,Map<String,String>> retMap = new HashMap<>();
        for (Map<String, String> unPlanMap : unPlanIpList) {
            retMap.put(unPlanMap.get("ip"),unPlanMap);
        }
        return retMap;
    }

    @Override
    public List<CmdbIpEntity> getAllIpv6List() {
        // 获取全量存活IP（IPV6）
        Map<String,String> IpParam = new HashMap<>();
        IpParam.put("ipTypeName","存活IP扫描");
        IpParam.put("ipType","ipv6");
        List<CmdbIpEntity> AllIpList = ipAuditMapper.getCmdbAllIp4Ipv6();
        List<CmdbIpEntity> AllIpv6List = new ArrayList<>();
        // 判断查询的稽核IP是否为IPv6
        for (CmdbIpEntity ipMap : AllIpList) {
            String tempIpv6 = ipMap.getIp();
            Object[] ipv6Flag = IpUtils.isIPV6Address(tempIpv6);
            boolean checkFlag = (Boolean) ipv6Flag[0];
            if (checkFlag) {
                AllIpv6List.add(ipMap);
            }
        }
        List<CmdbIpEntity> collectionIp = ipAuditMapper.getAutoIpv6(IpParam);
        AllIpv6List.addAll(collectionIp);
        return AllIpv6List;
    }
}
