package com.aspire.ums.cmdb.IpResource.service.impl;

import com.aspire.ums.cmdb.IpResource.mapper.CmdbPublicSegmentUpdateMapper;
import com.aspire.ums.cmdb.IpResource.service.CmdbPublicSegmentUpdateService;
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
 * @date 2020-12-17 15:08
 * @description
 */
@Slf4j
@Service
public class CmdbPublicSegmentUpdateServiceImpl implements CmdbPublicSegmentUpdateService {
    @Autowired
    private ICmdbCodeService codeService;
    @Resource
    private CmdbPublicSegmentUpdateMapper mapper;
    @Override
    public Integer batchUpdatePublicSegment(String moduleId, Map<String, Object> dataMap) {
        String tableName = "cmdb_ip_repository_public_segment";
        String address = dataMap.get("public_segment_address").toString();
        String idcType = dataMap.get("idcType").toString();
        int count = 0;
        Map<String, Object> instanceData = new HashMap<>();
        long start = System.currentTimeMillis();
        List<CmdbCode> codeList = codeService.getCodeListByModuleId(moduleId);
        log.info("公网网段码表查询耗时：#{}",System.currentTimeMillis() - start);
        for (CmdbCode cmdbCode : codeList) {
            if (null != cmdbCode.getUpdateReadOnly() && 1 == cmdbCode.getUpdateReadOnly()) {
                continue;
            }
            instanceData.put(cmdbCode.getFiledCode(),dataMap.get(cmdbCode.getFiledCode()));
        }
        if (instanceData.isEmpty()) {
            log.info("公网网段【{}】,没有需要更新的字段",address);
            return 0;
        }
        count = mapper.batchUpdatePublicSegment(tableName, address, idcType, instanceData);
        return count;
    }
}
