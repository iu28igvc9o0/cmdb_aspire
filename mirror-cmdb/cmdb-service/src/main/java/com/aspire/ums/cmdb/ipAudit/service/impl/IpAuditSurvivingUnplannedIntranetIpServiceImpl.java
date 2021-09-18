package com.aspire.ums.cmdb.ipAudit.service.impl;

import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpEntity;
import com.aspire.ums.cmdb.ipAudit.entity.IpAuditSurvivingUnplannedIntranetIp;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditIntranetIpMapper;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditSurvivingUnplannedIntranetIpMapper;
import com.aspire.ums.cmdb.ipAudit.payload.IpAuditUpdateRequest;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnplannedIntranetIpReq;
import com.aspire.ums.cmdb.ipAudit.payload.SurvivingUnplannedIntranetIpResp;
import com.aspire.ums.cmdb.ipAudit.service.IIpAuditSurvivingUnplannedIntranetIpService;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
* 描述：
* @author
* @date 2020-05-15 16:53:39
*/
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class IpAuditSurvivingUnplannedIntranetIpServiceImpl implements IIpAuditSurvivingUnplannedIntranetIpService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private IpAuditSurvivingUnplannedIntranetIpMapper mapper;
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private IpAuditIntranetIpMapper ipAuditIntranetIpMapper;

    /**
     * 新增实例
     * @param data 实例数据
     * @return
     */
    @Override
    public void add(IpAuditSurvivingUnplannedIntranetIp data) {
        mapper.insert(data);
    }
    /**
     * 批量新增实例
     * @param data 实例数据
     * @return
     */
    @Override
    public void batchAdd(List<IpAuditSurvivingUnplannedIntranetIp> data) {
        mapper.batchInsertIp(data);
    }

    /**
     * 修改实例
     * @param entity 实例数据
     * @return
     */
    public void update(IpAuditSurvivingUnplannedIntranetIp entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    public void delete(IpAuditSurvivingUnplannedIntranetIp entity) {
        mapper.delete(entity);
    }

    @Override
    public List<SurvivingUnplannedIntranetIpResp> getList(SurvivingUnplannedIntranetIpReq data) {
        return mapper.getListByPage(data);
    }

    @Override
    public int getListCount(SurvivingUnplannedIntranetIpReq data) {
        return mapper.getListCountByPage(data);
    }

    /**
     * 生成存活未规划IP数据
     */
    @Override
    public void generateAuditData() {
        Long maxCheckNum = mapper.getMaxCheckNum();
        if (maxCheckNum == null || maxCheckNum < 1) { // 第一次比对
            maxCheckNum = 1l;
        } else {
            maxCheckNum++;
        }
        // 获取CMDB所有IP（不比对） 和 获取全量存活IP
        List<CmdbIpEntity> allSurvivingIpEntity = ipAuditIntranetIpMapper.getAllSurvivingIpEntity1();
        // 获取全量IP地址库
        List<String> intranetIp = ipAuditIntranetIpMapper.getIpRepositoryIntranetIp();
        // cmdb所有ip和全量存活ip比对全量ip地址库
        if (!CollectionUtils.isEmpty(allSurvivingIpEntity)) {
            List<String> needAddIps = Lists.newArrayList(); // 去重
            List<IpAuditSurvivingUnplannedIntranetIp> needAddList = Lists.newArrayList();
            Date checkDate = new Date();
            for (int i = 0; i < allSurvivingIpEntity.size(); i++) {
                CmdbIpEntity cmdbIpEntity = allSurvivingIpEntity.get(i);
                String ip = cmdbIpEntity.getIp();
                String dc = cmdbIpEntity.getIdcType();
                if (CollectionUtils.isEmpty(intranetIp) || !intranetIp.contains(ip)) {
                    if (needAddIps.contains(ip)) continue; // 去重
                    IpAuditSurvivingUnplannedIntranetIp survivingUnplannedIntranetIp =
                            new IpAuditSurvivingUnplannedIntranetIp(checkDate, cmdbIpEntity.getIpType(),
                                    "未通知", "0", maxCheckNum, 0);
                    survivingUnplannedIntranetIp.setDc(cmdbIpEntity.getIdcType());
                    survivingUnplannedIntranetIp.setDeviceIp(cmdbIpEntity.getDeviceIp());
                    survivingUnplannedIntranetIp.setIp(ip);
                    survivingUnplannedIntranetIp.setId(UUIDUtil.getUUID());
                    needAddList.add(survivingUnplannedIntranetIp);
                    needAddIps.add(ip);
                    if (maxCheckNum > 1l) { // 与工单相关的字段处理
                        // 保存最近一次的同个IP的暂不处理状态、原因以及操作人信息
                        HashMap<String, String> param = Maps.newHashMap();
                        param.put("ip", ip);
                        param.put("dc", dc);
                        IpAuditSurvivingUnplannedIntranetIp entity = mapper.getEntityByIpAndDc(param);
                        if (entity != null) {
                            survivingUnplannedIntranetIp.setProcessingStatus(entity.getProcessingStatus());
                            survivingUnplannedIntranetIp.setOperator(entity.getOperator());
                            survivingUnplannedIntranetIp.setReason(entity.getReason());
                            survivingUnplannedIntranetIp.setOperatingTime(entity.getOperatingTime());
                        }
                    }
                }
            }
            // batch insert records
            if (!CollectionUtils.isEmpty(needAddList)) {
                if (needAddList.size() <= 2000) {
                    mapper.batchInsertIp(needAddList);
                } else {
                    List<IpAuditSurvivingUnplannedIntranetIp> tmp = Lists.newArrayList();
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
    public Map<String, Object> getStatisticsData(SurvivingUnplannedIntranetIpReq req) {
        Map<String, Object> rtn = mapper.getStatisticsData(req);
        return rtn;
    }

    @Override
    public void updateProcess(IpAuditUpdateRequest request) {
        mapper.updateProcess(request);
        // 工单相关
    }
}