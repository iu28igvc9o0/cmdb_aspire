package com.aspire.ums.cmdb.ipAudit.service.impl;

import com.aspire.ums.cmdb.ipAudit.entity.CmdbIpRepositoryDto;
import com.aspire.ums.cmdb.ipAudit.entity.IpAuditRegistrationExpired;
import com.aspire.ums.cmdb.ipAudit.mapper.IpAuditRegistrationExpiredMapper;
import com.aspire.ums.cmdb.ipAudit.payload.*;
import com.aspire.ums.cmdb.ipAudit.service.IIpAuditRegistrationExpiredService;
import com.aspire.ums.cmdb.util.DateUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.util.UUIDUtil;
import com.google.common.collect.Lists;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 描述： IP稽核：登记已过期IP(内网IP、公网IP、IPv6)
 *
 * @author huanggongrui
 * @date 2020-05-22 14:33:58
 */
@Service
@Slf4j
public class IpAuditRegistrationExpiredServiceImpl implements IIpAuditRegistrationExpiredService {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private IpAuditRegistrationExpiredMapper mapper;

    /**
     * 新增实例
     *
     * @param data 实例数据
     * @return
     */
    @Override
    public void add(IpAuditRegistrationExpired data) {
        mapper.insert(data);
    }

    /**
     * 批量新增实例
     *
     * @param data 实例数据
     * @return
     */
    @Override
    public void batchAdd(List<IpAuditRegistrationExpired> data) {
        mapper.batchInsertIp(data);
    }

    /**
     * 修改实例
     *
     * @param entity 实例数据
     * @return
     */
    public void update(IpAuditRegistrationExpired entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     *
     * @param entity 实例数据
     * @return
     */
    public void delete(IpAuditRegistrationExpired entity) {
        mapper.delete(entity);
    }

    @Override
    public List<RegistrationExpiredIpResp> getList(RegistrationExpiredIpReq data) {
        return mapper.getListByPage(data);
    }

    @Override
    public int getListCount(RegistrationExpiredIpReq data) {
        return mapper.getListCountByPage(data);
    }

    @Override
    public Map<String, Object> getStatisticsData(RegistrationExpiredIpReq req) {
        return mapper.getStatisticsData(req);
    }

    @Override
    public void updateProcess(IpAuditUpdateRequest request) {
        mapper.updateProcess(request);
        // 工单相关
    }

    @Override
    public void generateAuditData() {
    }

    @Override
    public List<RegistrationExpiredPublicIpResp> listByEntity4PublicIp(RegistrationExpiredPublicIpReq req) {
        return mapper.listByEntity4PublicIp(req);
    }

    @Override
    public int getListCount4PublicIp(RegistrationExpiredPublicIpReq data) {
        return mapper.getListCount4PublicIp(data);
    }

    @Override
    public Map<String, Object> getStatisticsData4PublicIp(RegistrationExpiredPublicIpReq req) {
        return mapper.getStatisticsData4PublicIp(req);
    }

    @Override
    public List<RegistrationExpiredIpv6Resp> listByEntity4Ipv6(RegistrationExpiredIpv6Req req) {
        return mapper.listByEntity4Ipv6(req);
    }

    @Override
    public int getListCount4Ipv6(RegistrationExpiredIpv6Req data) {
        return mapper.getListCount4Ipv6(data);
    }

    @Override
    public Map<String, Object> getStatisticsData4Ipv6(RegistrationExpiredIpv6Req req) {
        return mapper.getStatisticsData4Ipv6(req);
    }

    @Override
    public void genRegistrationExpiredIpData(String ipType) {
        if (StringUtils.isEmpty(ipType)) return;
        Long maxCheckNum = mapper.getMaxCheckNum(ipType);
        if (maxCheckNum == null || maxCheckNum < 1) { // 第一次比对
            maxCheckNum = 1l;
        } else {
            maxCheckNum++;
        }
        List<CmdbIpRepositoryDto> ipRepositoryDtos = Lists.newArrayList();
        if (ipType.equalsIgnoreCase("内网IP")) {
            ipRepositoryDtos = mapper.getIpRepositoryInnerIp();
        } else if (ipType.equalsIgnoreCase("公网IP")) {
            ipRepositoryDtos = mapper.getIpRepositoryPublicIp();
        } else if (ipType.equalsIgnoreCase("IPv6")) {
            ipRepositoryDtos = mapper.getIpRepositoryIpv6();
        }
        if (ipRepositoryDtos.size() > 0) {
            List<IpAuditRegistrationExpired> needAddList = Lists.newLinkedList();
            LocalDateTime now = LocalDateTime.now();
            for (int i = 0; i < ipRepositoryDtos.size(); i++) {
                CmdbIpRepositoryDto cmdbIpRepositoryDto = ipRepositoryDtos.get(i);
                Double usefulLife = null;
                try {
                    if (StringUtils.isNotEmpty(cmdbIpRepositoryDto.getUsefulLife()))
                        usefulLife = Double.valueOf(cmdbIpRepositoryDto.getUsefulLife());
                } catch (NumberFormatException e) {
                    log.error("使用期限解析有误", e);
                }
                LocalDateTime requestTime = null;
                try {
                    if (StringUtils.isNotEmpty(cmdbIpRepositoryDto.getRequestTime())) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        requestTime = LocalDateTime.parse(cmdbIpRepositoryDto.getRequestTime(), formatter);
                    }
                } catch (Exception e) {
                    log.error("申请时间解析有误", e);
                }
                if (requestTime != null && usefulLife != null) {
                    usefulLife = usefulLife * 365 * 24; // 小时维度
                    LocalDateTime expiredTime = requestTime.plusHours(new Double(usefulLife).longValue());
                    if (now.isAfter(expiredTime)) { // 过期
                        IpAuditRegistrationExpired ipAuditRegistrationExpired = new IpAuditRegistrationExpired(
                                Date.from(now.atZone(ZoneId.systemDefault()).toInstant()), "未通知",
                                "0", maxCheckNum, 0);
                        ipAuditRegistrationExpired.setDc(cmdbIpRepositoryDto.getIdcType());
                        ipAuditRegistrationExpired.setIp(cmdbIpRepositoryDto.getIp());
                        ipAuditRegistrationExpired.setIpType(cmdbIpRepositoryDto.getIpType());
                        ipAuditRegistrationExpired.setId(UUIDUtil.getUUID());
                        ipAuditRegistrationExpired.setRequestTime(Date.from(requestTime.atZone(ZoneId.systemDefault()).toInstant()));
                        ipAuditRegistrationExpired.setUsefulLife(usefulLife);
                        needAddList.add(ipAuditRegistrationExpired);
                    }
                }
            }
            // batch insert records
            if (!CollectionUtils.isEmpty(needAddList)) {
                if (needAddList.size() <= 2000) {
                    mapper.batchInsertIp(needAddList);
                } else {
                    List<IpAuditRegistrationExpired> tmp = Lists.newArrayList();
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

    public static void main(String[] args) {
        Double usefulLife = 2.3435d;
        usefulLife = usefulLife * 365 * 24; // 小时维度
        LocalDateTime requestDate = LocalDateTime.parse("2020-05-13 23:00:23",
                DateTimeFormatter.ofPattern(DateUtils.DEFAULT_DATETIME_FMT));
        LocalDateTime expiredTime = requestDate.plusHours(new Double(usefulLife).longValue());
        System.out.println(expiredTime);
    }
}