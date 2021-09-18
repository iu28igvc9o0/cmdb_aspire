package com.aspire.ums.cmdb.IpResource.service.impl;

import com.aspire.ums.cmdb.IpResource.mapper.CmdbIpv6SegmentUpdateMapper;
import com.aspire.ums.cmdb.IpResource.service.CmdbIpv6SegmentUpdateService;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.module.payload.Module;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import com.aspire.ums.cmdb.v2.module.service.ModuleService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-12-17 11:48
 * @description
 */
@Slf4j
@Service
public class CmdbIpv6SegmentUpdateServiceImpl implements CmdbIpv6SegmentUpdateService {
    @Resource
    private CmdbIpv6SegmentUpdateMapper mapper;
    @Autowired
    private ICmdbCodeService codeService;

    @Override
    public Integer batchUpdateIpv6Segment(String moduleId,Map<String, Object> dataMap) {
        String tableName = "cmdb_ip_repository_ipv6_segment";
        String address = dataMap.get("ipv6_segment_address").toString();
        String idcType = dataMap.get("idcType").toString();
        int count;
        Map<String, Object> instanceData = new HashMap<>();
        long start = System.currentTimeMillis();
        List<CmdbCode> codeList = codeService.getCodeListByModuleId(moduleId);
        log.info("IPv6网段码表查询耗时：#{}",System.currentTimeMillis() - start);
        for (CmdbCode cmdbCode : codeList) {
            if (null != cmdbCode.getUpdateReadOnly() && 1 == cmdbCode.getUpdateReadOnly()) {
                continue;
            }
            instanceData.put(cmdbCode.getFiledCode(),dataMap.get(cmdbCode.getFiledCode()));
        }
        if (instanceData.isEmpty()) {
            log.info("ipv6网段【{}】,没有需要更新的字段",address);
            return 0;
        }
        count = mapper.batchUpdateIpv6Segment(tableName, address, idcType, instanceData);
        return count;
    }

}
