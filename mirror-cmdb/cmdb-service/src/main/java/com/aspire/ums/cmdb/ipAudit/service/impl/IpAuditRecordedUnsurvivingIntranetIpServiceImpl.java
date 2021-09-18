package com.aspire.ums.cmdb.ipAudit.service.impl;

import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpAddressPool;
import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpEntity;
import com.aspire.ums.cmdb.ipAudit.entity.IpAuditRecordedUnsurvivingIntranetIp;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditIntranetIpMapper;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditRecordedUnsurvivingIntranetIpMapper;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.payload.RecordedUnsurvivingIntranetIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.RecordedUnsurvivingIntranetIpResp;
import com.aspire.ums.cmdb.ipAudit.service.IIpAuditRecordedUnsurvivingIntranetIpService;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 描述：
 *
 * @author
 * @date 2020-05-21 16:49:59
 */
@Service
@Slf4j
public class IpAuditRecordedUnsurvivingIntranetIpServiceImpl implements IIpAuditRecordedUnsurvivingIntranetIpService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IpAuditRecordedUnsurvivingIntranetIpMapper mapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IpAuditIntranetIpMapper ipAuditIntranetIpMapper;
    /**
     * 新增实例
     * @param data 实例数据
     */
    @Override
    public void add(IpAuditRecordedUnsurvivingIntranetIp data) {
        mapper.insert(data);
    }
    /**
     * 批量新增实例
     * @param data 实例数据
     */
    @Override
    public void batchAdd(List<IpAuditRecordedUnsurvivingIntranetIp> data) {
        mapper.batchInsertIp(data);
    }

    /**
     * 修改实例
     *
     * @param entity 实例数据
     * @return
     */
    @Override
    public void update(IpAuditRecordedUnsurvivingIntranetIp entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     *
     * @param entity 实例数据
     * @return
     */
    @Override
    public void delete(IpAuditRecordedUnsurvivingIntranetIp entity) {
        mapper.delete(entity);
    }

    @Override
    public List<RecordedUnsurvivingIntranetIpResp> getList(RecordedUnsurvivingIntranetIpReq data) {
        return mapper.getListByPage(data);
    }

    @Override
    public int getListCount(RecordedUnsurvivingIntranetIpReq data) {
        return mapper.getListCountByPage(data);
    }

    /**
     * CMDB所有的管理IP、业务IP1、业务IP2、IPV6、其他IP与全量IP扫描结果进行比对
     */
    @Override
    public void generateAuditData() {
        Long maxCheckNum = mapper.getMaxCheckNum();
        if (maxCheckNum == null || maxCheckNum < 1) { // 第一次比对
            maxCheckNum = 1l;
        } else {
            maxCheckNum++;
        }
        // 获取CMDB所有IP
        List<CmdbIpEntity> cmdbAllIp = ipAuditIntranetIpMapper.getCmdbAllIpEntity1();
        List<CmdbIpEntity> cmdbAllIp2 = ipAuditIntranetIpMapper.getCmdbAllIpEntity2();
        List<CmdbIpEntity> cmdbAllIp3 = ipAuditIntranetIpMapper.getCmdbAllIpEntity3();
        List<CmdbIpEntity> cmdbAllIp4 = ipAuditIntranetIpMapper.getCmdbAllIpEntity4();
        List<CmdbIpEntity> cmdbAllIp5 = ipAuditIntranetIpMapper.getCmdbAllIpEntity5();
        cmdbAllIp.addAll(cmdbAllIp2);
        cmdbAllIp.addAll(cmdbAllIp3);
        cmdbAllIp.addAll(cmdbAllIp4);
        cmdbAllIp.addAll(cmdbAllIp5);
        // 获取全量存活IP
        List<CmdbIpAddressPool> collectionIp = ipAuditIntranetIpMapper.getCollectionIpEntity("ipv4");
        Map<String, CmdbIpAddressPool> collectionIpHolder = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(collectionIp)) {
            for (int i = 0; i < collectionIp.size(); i++) {
                CmdbIpAddressPool cmdbIpAddressPool = collectionIp.get(i);
                collectionIpHolder.put(cmdbIpAddressPool.getIp(), cmdbIpAddressPool);
            }
        }
        // cmdb所有ip比对全量存活ip
        if (!CollectionUtils.isEmpty(cmdbAllIp)) {
            List<IpAuditRecordedUnsurvivingIntranetIp> needAddList = Lists.newArrayList();
            Date checkDate = new Date();
            for (int i = 0; i < cmdbAllIp.size(); i++) {
                CmdbIpEntity cmdbIpEntity = cmdbAllIp.get(i);
                String ip = cmdbIpEntity.getIp();
                String dc = cmdbIpEntity.getIdcType();
                IpAuditRecordedUnsurvivingIntranetIp recordedUnsurvivingIntranetIp =
                        new IpAuditRecordedUnsurvivingIntranetIp(checkDate,
                                "未通知", "0", maxCheckNum, 0);
                recordedUnsurvivingIntranetIp.setDc(cmdbIpEntity.getIdcType());
                recordedUnsurvivingIntranetIp.setDeviceIp(cmdbIpEntity.getDeviceIp());
                recordedUnsurvivingIntranetIp.setIp(ip);
                recordedUnsurvivingIntranetIp.setIpType(cmdbIpEntity.getIpType());
                recordedUnsurvivingIntranetIp.setId(UUIDUtil.getUUID());
                recordedUnsurvivingIntranetIp.setDeviceStatus(cmdbIpEntity.getDeviceStatus());
                if (CollectionUtils.isEmpty(collectionIpHolder) || collectionIpHolder.get(ip) == null) { // 全量存活IP中没有记录
                    needAddList.add(recordedUnsurvivingIntranetIp);
                } else {
                    // 全量存活IP中有记录
                    CmdbIpAddressPool cmdbIpAddressPool = collectionIpHolder.get(ip);
                    if (cmdbIpAddressPool != null && cmdbIpAddressPool.getDelFlag().equals("1")) { // 删除了的
                        recordedUnsurvivingIntranetIp.setLastSurviveTime(cmdbIpAddressPool.getTime());
                        needAddList.add(recordedUnsurvivingIntranetIp);
                    }
                }
            }
            // batch insert records
            if (!CollectionUtils.isEmpty(needAddList)) {
                if (needAddList.size() <= 2000) {
                    mapper.batchInsertIp(needAddList);
                } else {
                    List<IpAuditRecordedUnsurvivingIntranetIp> tmp = Lists.newArrayList();
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
    public Map<String, Object> getStatisticsData(RecordedUnsurvivingIntranetIpReq req) {
        return mapper.getStatisticsData(req);
    }

    @Override
    public void updateProcess(IpAuditUpdateRequest request) {
        mapper.updateProcess(request);
        // 工单相关
    }
}