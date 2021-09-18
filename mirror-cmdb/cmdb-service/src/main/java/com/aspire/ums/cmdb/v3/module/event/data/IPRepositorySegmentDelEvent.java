package com.aspire.ums.cmdb.v3.module.event.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import com.aspire.ums.cmdb.IpResource.service.CmdbAddressLibraryService;
import com.aspire.ums.cmdb.dict.mapper.ConfigDictMapper;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v3.module.event.AbstractModuleEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * @author fanwenhui
 * @date 2020-07-15 16:09
 * @description 删除网段时，级联删除对应的IP数据（逻辑删除）
 */
@Slf4j
public class IPRepositorySegmentDelEvent extends AbstractModuleEvent {

    @Autowired
    private ICmdbInstanceService iCmdbInstanceService;

    @Autowired
    private CmdbAddressLibraryService addressLibraryService;

    private ConfigDictMapper configDictMapper;

    @Override
    public void initSpringBeans() {
        // 初始化 service类
        if (this.iCmdbInstanceService == null) {
            this.iCmdbInstanceService = SpringUtils.getBean(ICmdbInstanceService.class);
        }
        if (this.addressLibraryService == null) {
            this.addressLibraryService = SpringUtils.getBean(CmdbAddressLibraryService.class);
        }

        if (this.configDictMapper == null) {
            this.configDictMapper = SpringUtils.getBean(ConfigDictMapper.class);
        }
    }

    @Override
    public Map<String, Object> event(String moduleId, String instanceId, Map<String, Object> handleData) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // 处理数据 插入到内网ip地址的模型中. 且无需入库审核.
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> dataMap = iCmdbInstanceService.getInstanceDetail(moduleId, instanceId);
        if (null == dataMap || dataMap.isEmpty()) {
            returnMap.put("flag", false);
            returnMap.put("msg", "查询不到数据");
            return returnMap;
        }
        Set<String> keyset = dataMap.keySet();
        Map<String, String> delMap = new HashMap<>();
        if (keyset.stream().anyMatch(e -> e.contains("inner"))) {
            delMap.put("ipDelType", "inner");
            delMap.put("segmentAddress", dataMap.get("network_segment_address").toString()); // 内网网段地址
        } else if (keyset.stream().anyMatch(e -> e.contains("ipv6"))) {
            delMap.put("ipDelType", "ipv6");
            delMap.put("segmentAddress", dataMap.get("ipv6_segment_address").toString()); // ipv6网段地址
        } else if (keyset.stream().anyMatch(e -> e.contains("public"))) {
            delMap.put("ipDelType", "public");
            delMap.put("segmentAddress", dataMap.get("public_segment_address").toString()); // 公网网段地址
        } else {
            returnMap.put("flag", false);
            returnMap.put("msg", "更新失败！不存在 inner、ipv6、public");
            return returnMap;
        }
        delMap.put("idcVal", dataMap.get("idcType").toString()); // 资源池
        addressLibraryService.delIpBySegment(delMap);
        stopWatch.stop();
        log.info("<<<<<< 成功删除ip地址库数据,耗时:[{}]ms <<<<<<", stopWatch.getTotalTimeMillis());
        returnMap.put("flag", true);
        returnMap.put("msg", "ip地址库数据删除成功");
        return returnMap;
    }
}
