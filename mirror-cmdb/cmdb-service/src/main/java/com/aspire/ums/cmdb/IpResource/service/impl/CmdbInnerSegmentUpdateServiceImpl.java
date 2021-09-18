package com.aspire.ums.cmdb.IpResource.service.impl;

import com.aspire.ums.cmdb.IpResource.mapper.CmdbInnerSegmentUpdateMapper;
import com.aspire.ums.cmdb.IpResource.service.CmdbInnerSegmentUpdateService;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-12-14 17:26
 * @description
 */
@Slf4j
@Service
public class CmdbInnerSegmentUpdateServiceImpl implements CmdbInnerSegmentUpdateService {

    @Resource
    private CmdbInnerSegmentUpdateMapper mapper;
    @Autowired
    private ICmdbCodeService codeService;

    @Override
    public Integer batchUpdateInnerSegment(String moduleId,Map<String, Object> dataMap) {
        String tableName = "cmdb_ip_repository_inner_segment";
        String address = dataMap.get("network_segment_address").toString();
        String idcType = dataMap.get("idcType").toString();
        Map<String, Object> instanceData = new HashMap<>();
        int count;
        long start = System.currentTimeMillis();
        List<CmdbCode> codeList = codeService.getCodeListByModuleId(moduleId);
        log.info("内网网段码表查询耗时：#{}",System.currentTimeMillis() - start);
        for (CmdbCode cmdbCode : codeList) {
            if (null != cmdbCode.getUpdateReadOnly() && 1 == cmdbCode.getUpdateReadOnly()) {
                continue;
            }
            instanceData.put(cmdbCode.getFiledCode(),dataMap.get(cmdbCode.getFiledCode()));
        }
        if (instanceData.isEmpty()) {
            log.info("内网网段【{}】,没有需要更新的字段",address);
            return 0;
        }
        count = mapper.batchUpdateInnerSegment(tableName, address, idcType, instanceData);
        return count;
    }
}
