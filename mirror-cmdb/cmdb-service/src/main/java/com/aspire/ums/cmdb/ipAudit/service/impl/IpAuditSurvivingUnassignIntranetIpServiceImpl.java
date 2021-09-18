package com.aspire.ums.cmdb.ipAudit.service.impl;

import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpAddressPool;
import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpRepositoryInnerIp;
import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnassignIntranetIp;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditIntranetIpMapper;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditSurvivingUnassignIntranetIpMapper;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnassignIntranetIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnassignIntranetIpResp;
import com.aspire.ums.cmdb.ipAudit.service.IIpAuditSurvivingUnassignIntranetIpService;
import com.aspire.ums.cmdb.util.StringUtils;
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
 * @date 2020-05-22 10:49:26
 */
@Service
public class IpAuditSurvivingUnassignIntranetIpServiceImpl implements IIpAuditSurvivingUnassignIntranetIpService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IpAuditSurvivingUnassignIntranetIpMapper mapper;

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
    public void add(IpAuditSurvivingUnassignIntranetIp data) {
        mapper.insert(data);
    }

    /**
     * 批量新增实例
     *
     * @param data 实例数据
     * @return
     */
    @Override
    public void batchAdd(List<IpAuditSurvivingUnassignIntranetIp> data) {
        mapper.batchInsertIp(data);
    }

    /**
     * 修改实例
     *
     * @param entity 实例数据
     * @return
     */
    public void update(IpAuditSurvivingUnassignIntranetIp entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     *
     * @param entity 实例数据
     * @return
     */
    public void delete(IpAuditSurvivingUnassignIntranetIp entity) {
        mapper.delete(entity);
    }

    @Override
    public List<SurvivingUnassignIntranetIpResp> getList(SurvivingUnassignIntranetIpReq data) {
        return mapper.getListByPage(data);
    }

    @Override
    public int getListCount(SurvivingUnassignIntranetIpReq data) {
        return mapper.getListCountByPage(data);
    }


    @Override
    public Map<String, Object> getStatisticsData(SurvivingUnassignIntranetIpReq req) {
        return mapper.getStatisticsData(req);
    }

    @Override
    public void updateProcess(IpAuditUpdateRequest request) {
        mapper.updateProcess(request);
        // 工单相关
    }

    /**
     * 生成存活未分配IP数据
     */
    @Override
    public void generateAuditData() {
        Long maxCheckNum = mapper.getMaxCheckNum();
        if (maxCheckNum == null || maxCheckNum < 1) { // 第一次比对
            maxCheckNum = 1l;
        } else {
            maxCheckNum++;
        }
        String assignedId = ipAuditIntranetIpMapper.getDictOfAssigned();
        // 获取全量存活IP
        List<CmdbIpAddressPool> collectionIp = ipAuditIntranetIpMapper.getCollectionIpEntity("ipv4");
        // 获取全量IP地址库
        List<CmdbIpRepositoryInnerIp> ipRepositoryIp = ipAuditIntranetIpMapper.getIpRepositoryIntranetIpEntity();
        Map<String, CmdbIpRepositoryInnerIp> ipRepositoryIpHolder = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(ipRepositoryIp)) {
            for (int i = 0; i < ipRepositoryIp.size(); i++) {
                CmdbIpRepositoryInnerIp cmdbIpRepositoryInnerIp = ipRepositoryIp.get(i);
                if (cmdbIpRepositoryInnerIp.getIp() != null)
                    ipRepositoryIpHolder.put(cmdbIpRepositoryInnerIp.getIp(),
                            cmdbIpRepositoryInnerIp);
            }
        }
        // 全量存活ip比对IP地址库中全量内网IP
        if (!CollectionUtils.isEmpty(collectionIp)) {
            List<IpAuditSurvivingUnassignIntranetIp> needAddList = Lists.newArrayList();
            Date checkDate = new Date();
            for (int i = 0; i < collectionIp.size(); i++) {
                CmdbIpAddressPool cmdbIpAddressPool = collectionIp.get(i);
                String ip = cmdbIpAddressPool.getIp();
                String dc = cmdbIpAddressPool.getResource();
                if (!CollectionUtils.isEmpty(ipRepositoryIpHolder) && ipRepositoryIpHolder.get(ip) != null &&
                        StringUtils.isEmpty(ipRepositoryIpHolder.get(ip).getRequestPerson())) {
                    IpAuditSurvivingUnassignIntranetIp survivingUnassignIntranetIp =
                            new IpAuditSurvivingUnassignIntranetIp(checkDate, "未通知",
                                    "0", maxCheckNum, 0);
                    // survivingUnassignIntranetIp.setDc(cmdbIpAddressPool.getResource());
                    survivingUnassignIntranetIp.setIp(ip);
                    survivingUnassignIntranetIp.setId(UUIDUtil.getUUID());
                    CmdbIpRepositoryInnerIp innerIp = ipRepositoryIpHolder.get(ip);
                    survivingUnassignIntranetIp.setDc(innerIp.getDc());
                    survivingUnassignIntranetIp.setInnerIpId(innerIp.getId());
                    survivingUnassignIntranetIp.setInnerSegmentId(innerIp.getSegmentId());
                    survivingUnassignIntranetIp.setInnerSegmentType(innerIp.getInnerSegmentType());
                    survivingUnassignIntranetIp.setInnerSegmentSubType(innerIp.getInnerSegmentSubType());
                    survivingUnassignIntranetIp.setFirstBusinessSystem(innerIp.getFirstBusinessSystem());
                    survivingUnassignIntranetIp.setAloneBusinessSystem(innerIp.getAloneBusinessSystem());
                    survivingUnassignIntranetIp.setInnerIpUse(innerIp.getInnerIpUse());
                    survivingUnassignIntranetIp.setVlanNumber(innerIp.getVlanNumber());
                    survivingUnassignIntranetIp.setIsPool(innerIp.getIsPool());
                    survivingUnassignIntranetIp.setSafeRegion(innerIp.getSafeRegion());
                    survivingUnassignIntranetIp.setIsCmdbManager(innerIp.getIsCmdbManager());
                    survivingUnassignIntranetIp.setOnlineBusiness(innerIp.getOnlineBusiness());

                    needAddList.add(survivingUnassignIntranetIp);
                    CmdbIpRepositoryInnerIp cmdbIpRepositoryInnerIp = ipRepositoryIpHolder.get(ip);
                    cmdbIpRepositoryInnerIp.setAssignStatus(assignedId);
                    cmdbIpRepositoryInnerIp.setAssignUser("IP稽核");
                    // 更新ip地址库的分配信息
                    mapper.updateIpRepository(cmdbIpRepositoryInnerIp);
                }
            }
            // batch insert records
            if (!CollectionUtils.isEmpty(needAddList)) {
                if (needAddList.size() <= 2000) {
                    mapper.batchInsertIp(needAddList);
                } else {
                    List<IpAuditSurvivingUnassignIntranetIp> tmp = Lists.newArrayList();
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
}