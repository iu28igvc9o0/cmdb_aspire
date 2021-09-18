package com.aspire.ums.cmdb.index;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Api("科管资源首页")
@RequestMapping("/cmdb/kg/index")
public interface KGResourceIndexAPI {

    /**
     * 统计各品牌设备分布（总）
     */
    @RequestMapping(value = "/deviceCountByProduceAll", method = RequestMethod.GET)
    @ApiOperation(value = "统计各品牌设备分布", notes = "统计各品牌设备分布", tags = {"KG CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> deviceCountByProduceAll(@RequestParam(value = "deviceClass",required = false) String deviceClass);

    /**
     * 统计品牌下设备型号分布
     */
    @RequestMapping(value = "/modelCountByProduce", method = RequestMethod.GET)
    @ApiOperation(value = "统计品牌下设备型号分布", notes = "统计品牌下设备型号分布",tags = {"KG CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> modelCountByProduce(@RequestParam(value = "deviceClass",required = false) String deviceClass,
                                                  @RequestParam(value = "produce") String produce);

    /**
     * 获取网段地址列表
     */
    @RequestMapping(value = "/getAllSegmentAddress", method = RequestMethod.GET)
    @ApiOperation(value = "获取网段地址列表", notes = "获取网段地址列表",tags = {"KG CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getAllSegmentAddress();

    /**
     * 资源使用状况分布
     */
    @RequestMapping(value = "/getDeviceUseCount", method = RequestMethod.GET)
    @ApiOperation(value = "资源使用状况分布", notes = "资源使用状况分布",tags = {"KG CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getDeviceUseCount(@RequestParam(value = "segmentAddr",required = false) String segmentAddr,
                                                @RequestParam(value = "bizSystem",required = false) String bizSystem);

    /**
     * (物理机/虚拟机)资源使用状况分布
     */
    @RequestMapping(value = "/getDeviceUseCountByType", method = RequestMethod.GET)
    @ApiOperation(value = "资源使用状况分布", notes = "资源使用状况分布",tags = {"KG CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> getDeviceUseCountByType(@RequestParam(value = "segmentAddr",required = false) String segmentAddr,
                                                      @RequestParam(value = "bizSystem",required = false) String bizSystem);

    /**
     * 网段用途设备总量分布
     */
    @RequestMapping(value = "/deviceCountBySegmentUse", method = RequestMethod.GET)
    @ApiOperation(value = "网段用途设备总量分布", notes = "网段用途设备总量分布",tags = {"KG CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> deviceCountBySegmentUse(@RequestParam(value = "deviceType",required = false) String deviceType);

    /**
     *  服务器业务使用量占比
     */
    @RequestMapping(value = "/deviceCountBySystem", method = RequestMethod.GET)
    @ApiOperation(value = "服务器业务使用量占比", notes = "服务器业务使用量占比",tags = {"KG CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> deviceCountBySystem(@RequestParam(value = "deviceType",required = false) String deviceType,
                                                  @RequestParam(value = "systemType") String systemType);

    @RequestMapping(value = "/deviceUseByPhy", method = RequestMethod.GET)
    @ApiOperation(value = "设备使用性(物理机)", notes = "设备使用性(物理机)",tags = {"KG CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = String.class),
            @ApiResponse(code = 500, message = "内部错误")})
    String deviceUseByClassAndType(@RequestParam(value = "deviceClass") String deviceClass,
            @RequestParam(value = "deviceType") String deviceType);
}
