package com.aspire.ums.cmdb.ipAudit.service.impl;

import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpEntity;
import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnassignIpV6;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditSurvivingUnassignIpV6Mapper;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.payload.UnassignIpV6Req;
import com.aspire.ums.cmdb.ipAudit.payload.UnassignIpV6Resp;
import com.aspire.ums.cmdb.ipAudit.service.IIpAuditSurvivingUnassignIpV6Service;
import com.aspire.ums.cmdb.ipAudit.service.IIpAuditSurvivingUnplanIpv6Service;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author fanwenhui
 * @date 2020-05-28 20:16
 * @description ip稽核-存活未分配IP（IPv6） 业务逻辑具体实现
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class IIpAuditSurvivingUnassignIpV6ServiceImpl implements IIpAuditSurvivingUnassignIpV6Service {

    @Resource
    private IpAuditSurvivingUnassignIpV6Mapper mapper;
    @Resource
    private IIpAuditSurvivingUnplanIpv6Service ipv6UnPlanService;

    @Override
    public void add(IpAuditSurvivingUnassignIpV6 data) {
        mapper.insert(data);
    }

    @Override
    public void batchAdd(List<IpAuditSurvivingUnassignIpV6> data) {
        mapper.batchInsertIp(data);
    }

    @Override
    public void update(IpAuditSurvivingUnassignIpV6 data) {
        mapper.update(data);
    }

    @Override
    public void delete(IpAuditSurvivingUnassignIpV6 data) {
        mapper.delete(data);
    }

    @Override
    public List<UnassignIpV6Resp> getList(UnassignIpV6Req data) {
        return mapper.listByEntity(data);
    }

    @Override
    public int getListCount(UnassignIpV6Req data) {
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
        // 获取全量存活IP（IPV6）
        List<CmdbIpEntity> allIpv6List = ipv6UnPlanService.getAllIpv6List();
        // 获取IPv6地址库中未删除的IPv6
        Map<String,Map<String,String>> unAssignMap = buildUnAssignMap();
        try {
            if (!allIpv6List.isEmpty() && null != unAssignMap && !unAssignMap.isEmpty()) {
                List<IpAuditSurvivingUnassignIpV6> ipv6AddList = new ArrayList<>();
                List<String> unAssignIpList = new ArrayList<>();
                for (CmdbIpEntity cmdbIpEntity : allIpv6List) {
                    String ip = cmdbIpEntity.getIp();
                    String dc = cmdbIpEntity.getIdcType();
                    String ipDcType = ip + "-" + dc;
                    Map<String, String> map = unAssignMap.get(ip);
                    if (null == map) {
                        continue;
                    }
                    // 获取当前IPV6地址库的数据，判断该IP的申请人信息是否为空，若为空则为未分配IP
                    if (StringUtils.isEmpty(map.get("requestPerson"))) {
                        // 构建录入IpV6稽核报表的实体
                        IpAuditSurvivingUnassignIpV6 ipv6Add = new IpAuditSurvivingUnassignIpV6();
                        ipv6Add.setId(UUIDUtil.getUUID());
                        ipv6Add.setCheckNum(maxCheckNum);
                        ipv6Add.setIp(ip);
                        ipv6Add.setIsDelete(0);
                        ipv6Add.setOperator("admin");
                        ipv6Add.setProcessingStatus("0");
                        ipv6Add.setIpv6Id(map.get("ipv6Id"));
                        ipv6Add.setIpv6SegmentId(map.get("ipv6SegmentId"));
                        ipv6Add.setIpv6SegmentName(map.get("ipv6SegmentName"));
                        ipv6Add.setDc(map.get("dc"));
                        ipv6Add.setIsPool("否");
                        if (StringUtils.isNotEmpty(map.get("dc"))) {
                            ipv6Add.setIsPool("是");
                        }
                        ipv6Add.setIpv6SegmentType(map.get("ipv6SegmentType"));
                        ipv6Add.setIpv6SegmentSubType(map.get("ipv6SegmentSubType"));
                        ipv6Add.setIpv6SegmentUse(map.get("ipv6SegmentUse"));
                        ipv6Add.setFirstBusinessSystem(map.get("firstBusinessSystem"));
                        ipv6Add.setAloneBusinessSystem(map.get("aloneBusinessSystem"));
                        ipv6Add.setIsCmdbManager(map.get("isCmdbManager"));
                        ipv6Add.setUseBusiness("");
                        ipv6AddList.add(ipv6Add);

                        // 构建更新ipv6地址库分配状态的实体
                        unAssignIpList.add(map.get("ipv6Id"));
                    }
                }
                // 分批次插入数据库，每次1000条
                if (!ipv6AddList.isEmpty()) {
                    int totalSize = ipv6AddList.size();
                    int limitSize = 1000;
                    if (totalSize > limitSize) {
                        List<IpAuditSurvivingUnassignIpV6> list = new ArrayList<>();
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
                if (!unAssignIpList.isEmpty()) {
                    int totalSize = unAssignIpList.size();
                    int limitSize = 3;
                    if (totalSize > limitSize) {
                        List<String> list = new ArrayList<>();
                        for (int i=0;i < totalSize;i++) {
                            list.add(unAssignIpList.get(i));
                            if (i % limitSize == 0) {
                                mapper.batchUpdateIpv6Status(list);
                                list.clear();
                            }
                        }
                        if (!list.isEmpty()) {
                            mapper.batchUpdateIpv6Status(list);
                        }
                    } else {
                        mapper.batchUpdateIpv6Status(unAssignIpList);
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getStatisticsData(UnassignIpV6Req req) {
        return mapper.getStatisticsData(req);
    }

    @Override
    public void updateProcess(IpAuditUpdateRequest request) {
        mapper.updateProcess(request);
    }

    @Override
    public void updateIpv6Info(IpAuditUpdateRequest req) {
        if(StringUtils.isNotEmpty(req.getRequestPerson()) || StringUtils.isNotEmpty(req.getRequestTime()) ||
                StringUtils.isNotEmpty(req.getRequestOrder()) || StringUtils.isNotEmpty(req.getExpirationDate())) {
            mapper.updateIpv6IpInfo(req);
        }

        if(StringUtils.isNotEmpty(req.getBusinessName1()) || StringUtils.isNotEmpty(req.getBusinessName2())) {
            mapper.updateIpv6SegmentInfo(req);
        }
    }

    public Map<String,Map<String,String>> buildUnAssignMap() {
        Map<String,Map<String,String>> retMap = new HashMap<>();
        List<Map<String,String>> ipv6s = mapper.getIpRepositoryIpv6s();
        for (Map<String, String> ipv6 : ipv6s) {
            retMap.put(ipv6.get("ipv6"),ipv6);
        }
        return retMap;
    }
}
