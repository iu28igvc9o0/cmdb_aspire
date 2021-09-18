package com.aspire.ums.cmdb.index;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName IItCloudScreenAPI
 * @Description IT租户大屏API
 * @Author luowenbo
 * @Date 2020/2/26 21:15
 * @Version 1.0
 */
@RequestMapping("/cmdb/index")
public interface IItCloudScreenAPI {

    /*
     *  获取资源分配总量 (计算资源 || 分配资源)
     * */
    @RequestMapping(value = "/allocate/resource", method = RequestMethod.POST)
    @ApiOperation(value = "获取计算资源资源分配总量", notes = "获取计算资源资源分配总量", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> getResourceAllocateList(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取某个月份的CPU和内存的均峰值
     * */
    @RequestMapping(value = "/utilization/max/month", method = RequestMethod.POST)
    @ApiOperation(value = "获取某个月份的CPU和内存的均峰值", notes = "获取某个月份的CPU和内存的均峰值", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> getMaxUtilizationByMonth(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取业务系统的CPU和内存的均峰值
     * */
    @RequestMapping(value = "/utilization/max/biz", method = RequestMethod.POST)
    @ApiOperation(value = "获取业务系统的CPU和内存的均峰值", notes = "获取业务系统的CPU和内存的均峰值", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> getMaxUtilizationByBizSystem(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取某个月份的CPU和内存的平均值
     * */
    @RequestMapping(value = "/utilization/avg/month", method = RequestMethod.POST)
    @ApiOperation(value = "获取某个月份的CPU和内存的平均值", notes = "获取某个月份的CPU和内存的平均值", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> getAvgUtilizationByMonth(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取业务系统的CPU和内存的平均值
     * */
    @RequestMapping(value = "/utilization/avg/biz", method = RequestMethod.POST)
    @ApiOperation(value = "获取业务系统的CPU和内存的平均值", notes = "获取业务系统的CPU和内存的平均值", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> getAvgUtilizationByBizSystem(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取月份对比的均峰值利用率，这个月与上个月的对比
     * */
    @RequestMapping(value = "/utilization/monthContrast/max", method = RequestMethod.POST)
    @ApiOperation(value = "获取业务系统的CPU和内存的平均值", notes = "获取业务系统的CPU和内存的平均值", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> getMonthMaxUtilization(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取月份对比的平均值利用率，这个月与上个月的对比
     * */
    @RequestMapping(value = "/utilization/monthContrast/avg", method = RequestMethod.POST)
    @ApiOperation(value = "获取业务系统的CPU和内存的平均值", notes = "获取业务系统的CPU和内存的平均值", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> getMonthAvgUtilization(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取业务系统的数量总数
     * */
    @RequestMapping(value = "/getBizSystemTotal", method = RequestMethod.POST)
    @ApiOperation(value = "获取业务系统的数量总数", notes = "获取业务系统的数量总数", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Integer.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,String> getBizSystemCount(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取物理机CPU和内存都低于30%的业务系统数量
     * */
    @RequestMapping(value = "/bisSystem/pmCount", method = RequestMethod.POST)
    @ApiOperation(value = "获取物理机CPU和内存都低于30", notes = "获取物理机CPU和内存都低于30", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,String> getPmBizSystemCount(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取低于30%的业务系统列表
     * */
    @RequestMapping(value = "/bisSystem/list/cal", method = RequestMethod.POST)
    @ApiOperation(value = "获取低于30%的业务系统列表", notes = "获取低于30%的业务系统列表", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> getBizSystemListWithIdcType(@RequestBody ItCloudScreenRequest req);


    /*
     *  获取虚拟机CPU或者内存低于30%的业务系统数量
     * */
    @RequestMapping(value = "/bisSystem/vmCount", method = RequestMethod.POST)
    @ApiOperation(value = "获取虚拟机CPU或者内存低于30%的业务系统数量", notes = "获取虚拟机CPU或者内存低于30%的业务系统数量", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,String> getVmBizSystemCount(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取云存储利用率低于40%的业务系统数量
     * */
    @RequestMapping(value = "/bisSystem/storeCount", method = RequestMethod.POST)
    @ApiOperation(value = "获取云存储利用率低于40%的业务系统数量", notes = "获取云存储利用率低于40%的业务系统数量", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,String> getStoreBizSystemCount(@RequestBody ItCloudScreenRequest req);

    /*
     *  获取云存储利用率低于40%的业务系统列表
     * */
    @RequestMapping(value = "/bisSystem/list/store", method = RequestMethod.POST)
    @ApiOperation(value = "获取云存储利用率低于40%的业务系统列表", notes = "获取云存储利用率低于40%的业务系统列表", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> getStoreBizSystemListWithIdcType(@RequestBody ItCloudScreenRequest req);

    /*
     *  判断是否采集正常
     * */
    @RequestMapping(value = "/judge/status", method = RequestMethod.POST)
    @ApiOperation(value = "判断是否采集正常", notes = "判断是否采集正常", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String,Object> judgeStatus(@RequestBody ItCloudScreenRequest req);

    /*
     *  更新扣分项的临时表，按照月份，查询CPU均峰值的所有数据
     * */
    @RequestMapping(value = "/updateCheckScore", method = RequestMethod.GET)
    @ApiOperation(value = "更新扣分项的临时表，按照月份，查询CPU均峰值的所有数据", notes = "更新扣分项的临时表，按照月份，查询CPU均峰值的所有数据", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    JSONObject udpateCpuMaxList(@RequestParam("monthlyTime") String monthlyTime);

    /*
     *  某业务系统下，具体设备的相关信息
     * */
    @RequestMapping(value = "/bisSystem/list/device", method = RequestMethod.POST)
    @ApiOperation(value = "某业务系统下，具体设备的相关信息", notes = "某业务系统下，具体设备的相关信息", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    LinkedHashMap<String, Object> getSpecificDeviceByBz(@RequestBody ItCloudScreenRequest req);
}
