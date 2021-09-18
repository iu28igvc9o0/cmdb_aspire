package com.aspire.ums.cmdb.v2.code.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.ums.cmdb.code.ICmdbFieldMappingAPI;
import com.aspire.ums.cmdb.sync.service.ICmdbSyncFieldMappingService;

import lombok.extern.slf4j.Slf4j;

/**
 * cmdb资产同步表字段映射
 *
 * @author jiangxuwen
 * @date 2020/5/28 17:41
 */
@RestController
@Slf4j
public class CmdbFieldMappingController implements ICmdbFieldMappingAPI {

    @Autowired
    private ICmdbSyncFieldMappingService cmdbSyncFieldMappingService;

    @Override
    public Map<String, String> refresh(String mappingType, String mappingKey) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            cmdbSyncFieldMappingService.redisRefresh(mappingType, mappingKey);
            returnMap.put("flag", "true");
            returnMap.put("msg", "操作成功");
        } catch (Exception e) {
            log.error("refresh cmdb sync field mapping failed", e);
            returnMap.put("flag", "error");
            returnMap.put("msg", "刷新cmdb资产同步表字段映射redis失败:" + e.getMessage());
        }
        return returnMap;
    }
}
