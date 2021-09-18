package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.index;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;

/**
 * @ClassName ItCloudScreenClient
 * @Description CRT远程调用it云大屏的接口
 * @Author luowenbo
 * @Date 2020/2/27 14:09
 * @Version 1.0
 */
@FeignClient(value = "CMDB")
public interface ItCloudScreenClient {

    /*
     *  获取资源分配总量 (计算资源 || 分配资源)
     * */
    @RequestMapping(value = "/cmdb/index/allocate/resource", method = RequestMethod.POST)
    Map<String,Object> getResourceAllocateList(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取资源分配总量，依据业务系统分
     * */
    @RequestMapping(value = "/cmdb/index/allocate/bizSystem", method = RequestMethod.POST)
    List<Map<String,Object>> getResourceAllocateByBizSystem(@RequestBody ItCloudScreenRequest req);

    /*
     *  依据业务系统，查询免考核资源的详细信息
     * */
    @RequestMapping(value = "/cmdb/index/bizSystem/notInspect", method = RequestMethod.POST)
    List<Map<String,Object>> getBizSystemNotInpect(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取某个月份的CPU和内存的均峰值
     * */
    @RequestMapping(value = "/cmdb/index/utilization/max/month", method = RequestMethod.POST)
    List<Map<String,Object>> getMaxUtilizationByMonth(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取业务系统的CPU和内存的均峰值
     * */
    @RequestMapping(value = "/cmdb/index/utilization/max/biz", method = RequestMethod.POST)
    List<Map<String,Object>> getMaxUtilizationByBizSystem(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取某个月份的CPU和内存的平均值
     * */
    @RequestMapping(value = "/cmdb/index/utilization/avg/month", method = RequestMethod.POST)
    List<Map<String,Object>> getAvgUtilizationByMonth(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取业务系统的CPU和内存的平均值
     * */
    @RequestMapping(value = "/cmdb/index/utilization/avg/biz", method = RequestMethod.POST)
    List<Map<String,Object>> getAvgUtilizationByBizSystem(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取月份对比的均峰值利用率，这个月与上个月的对比
     * */
    @RequestMapping(value = "/cmdb/index/utilization/monthContrast/max", method = RequestMethod.POST)
    Map<String,Object> getMonthMaxUtilization(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取月份对比的平均值利用率，这个月与上个月的对比
     * */
    @RequestMapping(value = "/cmdb/index/utilization/monthContrast/avg", method = RequestMethod.POST)
    Map<String,Object> getMonthAvgUtilization(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取业务系统的数量总数
     * */
    @RequestMapping(value = "/cmdb/index/getBizSystemTotal", method = RequestMethod.POST)
    Map<String,String> getBizSystemCount(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取物理机CPU和内存都低于30%的业务系统数量
     * */
    @RequestMapping(value = "/cmdb/index/bisSystem/pmCount", method = RequestMethod.POST)
    Map<String,String> getPmBizSystemCount(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取低于30%的业务系统列表
     * */
    @RequestMapping(value = "/cmdb/index/bisSystem/list/cal", method = RequestMethod.POST)
    List<Map<String,Object>> getBizSystemListWithIdcType(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取虚拟机CPU或者内存低于30%的业务系统数量
     * */
    @RequestMapping(value = "/cmdb/index/bisSystem/vmCount", method = RequestMethod.POST)
    Map<String,String> getVmBizSystemCount(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取云存储利用率低于40%的业务系统数量
     * */
    @RequestMapping(value = "/cmdb/index/bisSystem/storeCount", method = RequestMethod.POST)
    Map<String,String> getStoreBizSystemCount(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取云存储利用率低于40%的业务系统列表
     * */
    @RequestMapping(value = "/cmdb/index/bisSystem/list/store", method = RequestMethod.POST)
    List<Map<String,Object>> getStoreBizSystemListWithIdcType(@RequestBody ItCloudScreenRequest req);

    /*
     *  判断是否采集正常
     * */
    @RequestMapping(value = "/cmdb/index/judge/status", method = RequestMethod.POST)
    Map<String,Object> judgeStatus(@RequestBody ItCloudScreenRequest req);

    /*
     *  导出全量数据
     * */
    @RequestMapping(value = "/cmdb/index/export/all", method = RequestMethod.POST)
    List<Map<String,Object>> exportAllInstanceData(@RequestBody ItCloudScreenRequest req);

    /*
     *  导出部分低于利用率的数据数据
     * */
    @RequestMapping(value = "/cmdb/index/export/part", method = RequestMethod.POST)
    List<Map<String,Object>> exportPartInstanceData(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取租户扣分项列表
     * */
    @RequestMapping(value = "/cmdb/index/score/list", method = RequestMethod.POST)
    Map<String, Object> getCheckScoreList(@RequestBody ItCloudScreenRequest req);

    /*
     *  某业务系统下，具体设备的相关信息
     * */
    @RequestMapping(value = "/cmdb/index/bisSystem/list/device", method = RequestMethod.POST)
    LinkedHashMap<String, Object> getSpecificDeviceByBz(@RequestBody ItCloudScreenRequest req);
}
