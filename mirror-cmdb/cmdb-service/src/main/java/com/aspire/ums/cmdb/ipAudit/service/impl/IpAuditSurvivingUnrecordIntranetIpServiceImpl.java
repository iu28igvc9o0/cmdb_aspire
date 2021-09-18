package com.aspire.ums.cmdb.ipAudit.service.impl;

import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpAddressPool;
import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpRepositoryInnerIp;
import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnrecordIntranetIp;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditIntranetIpMapper;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditSurvivingUnrecordIntranetIpMapper;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnrecordIntranetIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnrecordIntranetIpResp;
import com.aspire.ums.cmdb.ipAudit.service.IIpAuditSurvivingUnrecordIntranetIpService;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
* 描述： ip稽核--存活未录入CMDB的IP（全量存活IP与CMDB所有的管理IP、业务IP1、业务IP2、IPV6、其他IP进行比对）
 *              加上CMDB自动采集的IP
* @author huanggongrui
* @date 2020-05-21 11:20:10
*/
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class IpAuditSurvivingUnrecordIntranetIpServiceImpl implements IIpAuditSurvivingUnrecordIntranetIpService {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private IpAuditSurvivingUnrecordIntranetIpMapper mapper;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private IpAuditIntranetIpMapper ipAuditIntranetIpMapper;

    /**
     * 新增实例
     * @param data 实例数据
     */
    @Override
    public void add(IpAuditSurvivingUnrecordIntranetIp data) {
        mapper.insert(data);
    }

    /**
     * 批量新增实例
     * @param data 实例数据
     */
    @Override
    public void batchAdd(List<IpAuditSurvivingUnrecordIntranetIp> data) {
        mapper.batchInsertIp(data);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(IpAuditSurvivingUnrecordIntranetIp entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(IpAuditSurvivingUnrecordIntranetIp entity) {
        mapper.delete(entity);
    }

    @Override
    public List<SurvivingUnrecordIntranetIpResp> getList(SurvivingUnrecordIntranetIpReq data) {
        return mapper.getListByPage(data);
    }

    @Override
    public int getListCount(SurvivingUnrecordIntranetIpReq data) {
        return mapper.getListCountByPage(data);
    }

    /**
     * 生成存活未录入CMDB数据(全量存活内网IP与CMDB所有的管理IP、业务IP1、业务IP2、IPV6、其他IP进行比对)
     */
    @Override
    public void generateAuditData() {
        Long maxCheckNum = mapper.getMaxCheckNum();
        List<String> firstCheckSurvivingUnrecordIntranetIps = Lists.newArrayList();
        if (maxCheckNum == null || maxCheckNum < 1) { // 第一次比对
            maxCheckNum = 1l;
        } else {
            // 获取第一次检测的IPs
            firstCheckSurvivingUnrecordIntranetIps = mapper.getIpDcListByCheckNum(1l);
            maxCheckNum++;

        }
        // 获取全量存活IP
        List<CmdbIpAddressPool> collectionIp = ipAuditIntranetIpMapper.getCollectionIpEntity("ipv4");
        // 获取CMDB所有IP
        List<String> cmdbAllIp = ipAuditIntranetIpMapper.getCmdbAllIp1();
        List<String> cmdbAllIp2 = ipAuditIntranetIpMapper.getCmdbAllIp2();
        List<String> cmdbAllIp3 = ipAuditIntranetIpMapper.getCmdbAllIp3();
        List<String> cmdbAllIp4 = ipAuditIntranetIpMapper.getCmdbAllIp4();
        List<String> cmdbAllIp5 = ipAuditIntranetIpMapper.getCmdbAllIp5();
        List<String> cmdbAllIp6 = ipAuditIntranetIpMapper.getCmdbAllIp6();
        cmdbAllIp.addAll(cmdbAllIp2);
        cmdbAllIp.addAll(cmdbAllIp3);
        cmdbAllIp.addAll(cmdbAllIp4);
        cmdbAllIp.addAll(cmdbAllIp5);
        if (!CollectionUtils.isEmpty(cmdbAllIp6)) {
            for (String ips: cmdbAllIp6) {
                String[] splitIp = ips.split(";");
                cmdbAllIp.addAll(Arrays.asList(splitIp));
            }
        }
        // 获取全量IP地址库内网IP
        List<CmdbIpRepositoryInnerIp> ipRepositoryIp = ipAuditIntranetIpMapper.getIpRepositoryIntranetIpEntity();
        Map<String, CmdbIpRepositoryInnerIp> ipRepositoryInnerIpHolder = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(ipRepositoryIp)) {
            for (int i = 0; i < ipRepositoryIp.size(); i++) {
                CmdbIpRepositoryInnerIp cmdbIpRepositoryInnerIp = ipRepositoryIp.get(i);
                if (cmdbIpRepositoryInnerIp.getIp() != null && cmdbIpRepositoryInnerIp.getDc() != null) {
                    ipRepositoryInnerIpHolder.put(cmdbIpRepositoryInnerIp.getIp(), cmdbIpRepositoryInnerIp);
                }
            }
        }
        // 全量存活ip比对cmdb所有ip
        if (!CollectionUtils.isEmpty(collectionIp)) {
            // ipUseStatusType dict
            List<Map<String, String>> ipUseStatusTypes = ipAuditIntranetIpMapper.getIpUseStatusTypeDict();
            String illegalUse = "非法使用";
            String legalUse = "已使用";
            if (!CollectionUtils.isEmpty(ipUseStatusTypes)) {
                for (Map<String, String> holder: ipUseStatusTypes) {
                    if (holder.get("text").equalsIgnoreCase("非法使用")) {
                        illegalUse = holder.get("id");
                    } else if (holder.get("text").equalsIgnoreCase("已使用")) {
                        legalUse = holder.get("id");
                    }
                }
            }
            List<IpAuditSurvivingUnrecordIntranetIp> needAddList = Lists.newArrayList();
            List<String> needAddIps = Lists.newArrayList(); // 去重
            Date checkDate = new Date();
            for (int i = 0; i < collectionIp.size(); i++) {
                CmdbIpAddressPool cmdbIpAddressPool = collectionIp.get(i);
                String ip = cmdbIpAddressPool.getIp();
                if (CollectionUtils.isEmpty(cmdbAllIp) || !cmdbAllIp.contains(ip)) {
                    if (needAddIps.contains(ip)) continue; // 去重
                    IpAuditSurvivingUnrecordIntranetIp survivingUnrecordIntranetIp =
                            new IpAuditSurvivingUnrecordIntranetIp(checkDate, "未通知",
                                    "0", maxCheckNum, 0);
                    // survivingUnrecordIntranetIp.setDc(cmdbIpAddressPool.getResource());
                    survivingUnrecordIntranetIp.setDeviceIp(cmdbIpAddressPool.getGateway());
                    survivingUnrecordIntranetIp.setIp(ip);
                    survivingUnrecordIntranetIp.setId(UUIDUtil.getUUID());
                    // ip地址库字段
                    if (!CollectionUtils.isEmpty(ipRepositoryInnerIpHolder) && ipRepositoryInnerIpHolder.get(ip) != null) {
                        CmdbIpRepositoryInnerIp innerIp = ipRepositoryInnerIpHolder.get(ip);
                        survivingUnrecordIntranetIp.setDc(innerIp.getDc());
                        survivingUnrecordIntranetIp.setInnerIpId(innerIp.getId());
                        survivingUnrecordIntranetIp.setInnerSegmentId(innerIp.getSegmentId());
                        survivingUnrecordIntranetIp.setInnerSegmentType(innerIp.getInnerSegmentType());
                        survivingUnrecordIntranetIp.setInnerSegmentSubType(innerIp.getInnerSegmentSubType());
                        survivingUnrecordIntranetIp.setFirstBusinessSystem(innerIp.getFirstBusinessSystem());
                        survivingUnrecordIntranetIp.setInnerIpUse(innerIp.getInnerIpUse());
                        survivingUnrecordIntranetIp.setAssignStatus(innerIp.getAssignStatus());
                        survivingUnrecordIntranetIp.setAssignUser(innerIp.getAssignUser());
                        survivingUnrecordIntranetIp.setAssignTime(innerIp.getAssignTime());
                        survivingUnrecordIntranetIp.setRequestPerson(innerIp.getRequestPerson());
                    }
                    // 处理是否合法使用
                    if (maxCheckNum > 1l) {
                        if (!CollectionUtils.isEmpty(ipRepositoryInnerIpHolder) && ipRepositoryInnerIpHolder.get(ip) != null) {
                            CmdbIpRepositoryInnerIp cmdbIpRepositoryInnerIp = ipRepositoryInnerIpHolder.get(ip);
                            if (StringUtils.isEmpty(cmdbIpRepositoryInnerIp.getRequestPerson()) &&
                                    !firstCheckSurvivingUnrecordIntranetIps.contains(ip)) {
                                survivingUnrecordIntranetIp.setUseStatus(illegalUse);
                            } else {
                                survivingUnrecordIntranetIp.setUseStatus(legalUse);
                            }
                        } else if (firstCheckSurvivingUnrecordIntranetIps.contains(ip)) {
                            survivingUnrecordIntranetIp.setUseStatus(legalUse);
                        } else { // 既不存在IP地址库也不存在第一次检测的IP中
                            survivingUnrecordIntranetIp.setUseStatus(illegalUse);
                        }
                        // 与工单相关的字段处理...
                    } else {
                        survivingUnrecordIntranetIp.setUseStatus(legalUse);
                    }
                    needAddList.add(survivingUnrecordIntranetIp);
                    needAddIps.add(ip);
                }
            }
            // batch insert records
            if (!CollectionUtils.isEmpty(needAddList)) {
                if (needAddList.size() <= 2000) {
                    mapper.batchInsertIp(needAddList);
                } else {
                    List<IpAuditSurvivingUnrecordIntranetIp> tmp = Lists.newArrayList();
                    for (int i = 0; i < needAddList.size(); i++) {
                        tmp.add(needAddList.get(i));
                        if (tmp.size() >= 2000) {
                            mapper.batchInsertIp(tmp);
                            tmp.clear();
                        }
                    }
                    if (tmp.size() > 0) {
                        mapper.batchInsertIp(tmp);
                    }
                }
            }
        }
    }

    @Override
    public Map<String, Object> getStatisticsData(SurvivingUnrecordIntranetIpReq req) {
        Map<String, Object> rtn = mapper.getStatisticsData(req);
        return rtn;
    }

    @Override
    public void updateProcess(IpAuditUpdateRequest request) {
        mapper.updateProcess(request);
        // 工单相关
    }

    /**
     * 获取总数，快
     * @param req
     * @param list
     * @return
     */
    @Override
    public Map<String, Integer> getListCount(SurvivingUnrecordIntranetIpReq req, List<SurvivingUnrecordIntranetIpResp> list) {
        Map<String, Integer> rtnMap = Maps.newHashMap();
        int count = 0;
        int numOfToBeProcessedIp = 0;
        int numOfNotProcessIp = 0;
        int numOfUnsuitableUseIp = 0;
        rtnMap.put("count", 0);
        rtnMap.put("numOfToBeProcessedIp", 0);
        rtnMap.put("numOfNotProcessIp", 0);
        rtnMap.put("numOfUnsuitableUseIp", 0);
        if (CollectionUtils.isEmpty(list)) return rtnMap;
        List<String> ipList = Lists.newArrayList();
        for (int i = 0; i < list.size(); i++) {
            if (StringUtils.isNotEmpty(list.get(i).getIp())) {
                ipList.add(list.get(i).getIp());
            }
        }
        List<SurvivingUnrecordIntranetIpResp> ipReporsitoryList = Lists.newArrayList();
        List<String> tmpList = Lists.newArrayList();
        for (int i = 0; i < ipList.size(); i++) {
            if (tmpList.size() >= 2000) {
                req.setIps(tmpList);
                ipReporsitoryList.addAll(mapper.getIpRepositoryList(req));
                tmpList.clear();
            }
            tmpList.add(ipList.get(i));
        }
        if (tmpList.size() > 0) {
            req.setIps(tmpList);
            ipReporsitoryList.addAll(mapper.getIpRepositoryList(req));
        }
        for (int i = 0; i < list.size(); i++) {
            SurvivingUnrecordIntranetIpResp intranetIp = list.get(i);
            for (int j = 0; j < ipReporsitoryList.size(); j++) {
                SurvivingUnrecordIntranetIpResp ipResp = ipReporsitoryList.get(j);
                if (intranetIp.getIp().equals(ipResp.getIp())) {
                    count++;
                    if (intranetIp.getProcessingStatus().equals("0")) numOfToBeProcessedIp++;
                    if (intranetIp.getProcessingStatus().equals("1")) numOfNotProcessIp++;
                    if (intranetIp.getUseStatus().equals("非法使用")) numOfUnsuitableUseIp++;
                    break;
                }
            }
        }
        rtnMap.put("count", count);
        rtnMap.put("numOfToBeProcessedIp", numOfToBeProcessedIp);
        rtnMap.put("numOfNotProcessIp", numOfNotProcessIp);
        rtnMap.put("numOfUnsuitableUseIp", numOfUnsuitableUseIp);
        return rtnMap;
    }
}