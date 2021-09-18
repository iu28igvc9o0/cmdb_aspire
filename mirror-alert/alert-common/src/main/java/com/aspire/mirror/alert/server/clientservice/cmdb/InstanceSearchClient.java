package com.aspire.mirror.alert.server.clientservice.cmdb;

import com.aspire.ums.cmdb.deviceStatistic.IInstanceSearchAPI;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @author baiwenping
 * @Title: InstanceSearchClient
 * @Package com.aspire.mirror.alert.server.clientservice.cmdb
 * @Description: TODO
 * @date 2021/3/11 11:46
 */
@FeignClient(value = "CMDB")
public interface InstanceSearchClient extends IInstanceSearchAPI {
}
