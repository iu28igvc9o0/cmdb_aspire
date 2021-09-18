package com.aspire.ums.cmdb.ipAudit.service.impl;

import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpAddressPool;
import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpRepositoryInnerIp;
import com.aspire.ums.cmdb.ipAudit.entity.IpAuditAssignedUnsurvivingIntranetIp;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditAssignedUnsurvivingIntranetIpMapper;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditIntranetIpMapper;
import com.aspire.ums.cmdb.ipAudit.payload.AssignedUnsurvivingIntranetIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.AssignedUnsurvivingIntranetIpResp;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.service.IIpAuditAssignedUnsurvivingIntranetIpService;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 描述：
 *
 * @author
 * @date 2020-05-21 20:22:15
 */
@Service
public class IpAuditAssignedUnsurvivingIntranetIpServiceImpl implements IIpAuditAssignedUnsurvivingIntranetIpService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IpAuditAssignedUnsurvivingIntranetIpMapper mapper;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private IpAuditIntranetIpMapper ipAuditIntranetIpMapper;

    /**
     * 新增实例
     *
     * @param data 实例数据
     * @return
     */
    @Override
    public void add(IpAuditAssignedUnsurvivingIntranetIp data) {
        mapper.insert(data);
    }

    /**
     * 批量新增实例
     *
     * @param data 实例数据
     * @return
     */
    @Override
    public void batchAdd(List<IpAuditAssignedUnsurvivingIntranetIp> data) {
        mapper.batchInsertIp(data);
    }

    /**
     * 修改实例
     *
     * @param entity 实例数据
     * @return
     */
    public void update(IpAuditAssignedUnsurvivingIntranetIp entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     *
     * @param entity 实例数据
     * @return
     */
    public void delete(IpAuditAssignedUnsurvivingIntranetIp entity) {
        mapper.delete(entity);
    }

    @Override
    public List<AssignedUnsurvivingIntranetIpResp> getList(AssignedUnsurvivingIntranetIpReq data) {
        return mapper.getListByPage(data);
    }

    @Override
    public int getListCount(AssignedUnsurvivingIntranetIpReq data) {
        return mapper.getListCountByPage(data);
    }

    /**
     * IP地址库内网IP地址已分配IP与全量存活IP扫描结果进行比对
     */
    @Override
    public void generateAuditData() {
        Long maxCheckNum = mapper.getMaxCheckNum();
        if (maxCheckNum == null || maxCheckNum < 1) { // 第一次比对
            maxCheckNum = 1l;
        } else {
            maxCheckNum++;
        }
        // 获取全量已分配IP地址
        List<CmdbIpRepositoryInnerIp> assignedIntranetIp = ipAuditIntranetIpMapper.getIpRepositoryAssignedIntranetIp();
        // 获取全量存活IP
        List<CmdbIpAddressPool> collectionIp = ipAuditIntranetIpMapper.getCollectionIpEntity("ipv4");
        Map<String, CmdbIpAddressPool> collectionIpHolder = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(collectionIp)) {
            for (int i = 0; i < collectionIp.size(); i++) {
                CmdbIpAddressPool cmdbIpAddressPool = collectionIp.get(i);
                collectionIpHolder.put(cmdbIpAddressPool.getIp(), cmdbIpAddressPool);
            }
        }
        if (!CollectionUtils.isEmpty(assignedIntranetIp)) {
            List<IpAuditAssignedUnsurvivingIntranetIp> needAddList = Lists.newArrayList();
            Date checkDate = new Date();
            for (int i = 0; i < assignedIntranetIp.size(); i++) {
                CmdbIpRepositoryInnerIp ipRepositoryInnerIp = assignedIntranetIp.get(i);
                String ip = ipRepositoryInnerIp.getIp();
                String dc = ipRepositoryInnerIp.getDc();
                IpAuditAssignedUnsurvivingIntranetIp assignedUnsurvivingIntranetIp =
                        new IpAuditAssignedUnsurvivingIntranetIp(checkDate, "未通知",
                                "0", maxCheckNum, 0);
                // assignedUnsurvivingIntranetIp.setDc(null); // ip地址库没有所属资源池
                assignedUnsurvivingIntranetIp.setIp(ip);
                assignedUnsurvivingIntranetIp.setDc(dc);
                assignedUnsurvivingIntranetIp.setId(UUIDUtil.getUUID());
                // IP地址库字段
                assignedUnsurvivingIntranetIp.setInnerIpId(ipRepositoryInnerIp.getId());
                assignedUnsurvivingIntranetIp.setInnerSegmentId(ipRepositoryInnerIp.getSegmentId());
                assignedUnsurvivingIntranetIp.setInnerSegmentType(ipRepositoryInnerIp.getInnerSegmentType());
                assignedUnsurvivingIntranetIp.setInnerSegmentSubType(ipRepositoryInnerIp.getInnerSegmentSubType());
                assignedUnsurvivingIntranetIp.setFirstBusinessSystem(ipRepositoryInnerIp.getFirstBusinessSystem());
                assignedUnsurvivingIntranetIp.setAloneBusinessSystem(ipRepositoryInnerIp.getAloneBusinessSystem());
                assignedUnsurvivingIntranetIp.setInnerIpUse(ipRepositoryInnerIp.getInnerIpUse());
                assignedUnsurvivingIntranetIp.setAssignStatus(ipRepositoryInnerIp.getAssignStatus());
                assignedUnsurvivingIntranetIp.setAssignUser(ipRepositoryInnerIp.getAssignUser());
                assignedUnsurvivingIntranetIp.setAssignTime(ipRepositoryInnerIp.getAssignTime());
                assignedUnsurvivingIntranetIp.setRequestPerson(ipRepositoryInnerIp.getRequestPerson());
                assignedUnsurvivingIntranetIp.setRequestTime(ipRepositoryInnerIp.getRequestTime());
                if (CollectionUtils.isEmpty(collectionIpHolder) || collectionIpHolder.get(ip) == null) { // 全量存活IP中没有记录
                    needAddList.add(assignedUnsurvivingIntranetIp);
                } else {
                    // 全量存活IP中有记录
                    CmdbIpAddressPool cmdbIpAddressPool = collectionIpHolder.get(ip);
                    if (cmdbIpAddressPool != null && cmdbIpAddressPool.getDelFlag().equals("1")) { // 删除了的
                        assignedUnsurvivingIntranetIp.setLastSurviveTime(cmdbIpAddressPool.getTime());
                        needAddList.add(assignedUnsurvivingIntranetIp);
                    }
                }
            }
            // batch insert records
            if (!CollectionUtils.isEmpty(needAddList)) {
                if (needAddList.size() <= 2000) {
                    mapper.batchInsertIp(needAddList);
                } else {
                    List<IpAuditAssignedUnsurvivingIntranetIp> tmp = Lists.newArrayList();
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
    public Map<String, Object> getStatisticsData(AssignedUnsurvivingIntranetIpReq req) {
        return mapper.getStatisticsData(req);
    }

    @Override
    public void updateProcess(IpAuditUpdateRequest request) {
        mapper.updateProcess(request);
        // 工单相关
    }
}