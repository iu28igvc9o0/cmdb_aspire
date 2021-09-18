package com.aspire.cmdb.agent.schedule;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.v2.index.serivce.CountInstCdtService;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
//import com.xxl.job.core.biz.model.ReturnT;
//import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 说明: Redis缓存数据定时刷新功能
 * 工程: Cmdb
 * 作者: zhujuwang
 * 时间: 2020/10/21 9:32
 */
@Component
@Slf4j
public class RedisRefreshSchedule {

    @Autowired
    private IRedisService redisService;
    @Autowired
    private CountInstCdtService instCdtService;

//    @XxlJob("refreshIndexAssignedInfo")
//    public ReturnT<String> refreshResIndex(String params) {
//        log.info("Begin refresh resource index res assigned info.");
//        redisService.syncRemove(Constants.REDIS_RESOURCE_INDEX_ASSIGNED_INFO);
//        List<Map<String, Object>> dataList = instCdtService.queryOverview();
//        if (dataList != null && dataList.size() > 0) {
//            redisService.set(Constants.REDIS_RESOURCE_INDEX_ASSIGNED_INFO, dataList);
//        }
//        log.info("End refresh resource index res assigned info.");
//        return ReturnT.SUCCESS;
//    }

}
