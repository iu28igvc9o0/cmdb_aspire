package com.aspire.ums.cmdb.v3.module.event.data;

import com.aspire.ums.cmdb.IpResource.service.CmdbAddressLibraryService;
import com.aspire.ums.cmdb.IpResource.service.CmdbInnerSegmentUpdateService;
import com.aspire.ums.cmdb.IpResource.service.CmdbIpv6SegmentUpdateService;
import com.aspire.ums.cmdb.IpResource.service.CmdbPublicSegmentUpdateService;
import com.aspire.ums.cmdb.util.SpringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v3.module.event.AbstractModuleEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/30 15:29
 */
@Slf4j
public class IPRepositorySegmentUpdateEvent extends AbstractModuleEvent {
    @Autowired
    private ICmdbInstanceService iCmdbInstanceService;
    @Autowired
    private CmdbAddressLibraryService addressLibraryService;
    @Autowired
    private CmdbInnerSegmentUpdateService cmdbInnerSegmentUpdateService;
    @Autowired
    private CmdbIpv6SegmentUpdateService cmdbIpv6SegmentUpdateService;
    @Autowired
    private CmdbPublicSegmentUpdateService cmdbPublicSegmentUpdateService;

    @Override
    public void initSpringBeans() {
        // 初始化 service类
        if (this.iCmdbInstanceService == null) {
            this.iCmdbInstanceService = SpringUtils.getBean(ICmdbInstanceService.class);
        }
        if (this.addressLibraryService == null) {
            this.addressLibraryService = SpringUtils.getBean(CmdbAddressLibraryService.class);
        }
        if (this.cmdbInnerSegmentUpdateService == null) {
            this.cmdbInnerSegmentUpdateService = SpringUtils.getBean(CmdbInnerSegmentUpdateService.class);
        }
        if (this.cmdbIpv6SegmentUpdateService == null) {
            this.cmdbIpv6SegmentUpdateService = SpringUtils.getBean(CmdbIpv6SegmentUpdateService.class);
        }
        if (this.cmdbPublicSegmentUpdateService == null) {
            this.cmdbPublicSegmentUpdateService = SpringUtils.getBean(CmdbPublicSegmentUpdateService.class);
        }
    }

    @Override
    public Map<String, Object> event(String moduleId, String instanceId, Map<String, Object> handleData) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //  处理数据 插入到内网ip地址的模型中. 且无需入库审核.
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> dataMap = iCmdbInstanceService.getInstanceDetail(moduleId, instanceId);
        Set<String> keyset = dataMap.keySet();
        int count;
        if (keyset.stream().anyMatch(e -> e.contains("inner"))) {
            count = cmdbInnerSegmentUpdateService.batchUpdateInnerSegment(moduleId,dataMap);
        } else if (keyset.stream().anyMatch(e -> e.contains("ipv6"))) {
            count = cmdbIpv6SegmentUpdateService.batchUpdateIpv6Segment(moduleId,dataMap);
        } else if (keyset.stream().anyMatch(e -> e.contains("public"))) {
            count = cmdbPublicSegmentUpdateService.batchUpdatePublicSegment(moduleId,dataMap);
        } else {
            returnMap.put("flag", false);
            returnMap.put("msg", "更新失败！不存在 inner、ipv6、public");
            return returnMap;
        }

        stopWatch.stop();
        log.info("<<<<<< 成功修改网段 共[{}]条,耗时:[{}]ms <<<<<<", count, stopWatch.getTotalTimeMillis());
        returnMap.put("flag", true);
        returnMap.put("msg", "更新成功");
        return returnMap;
    }
}
