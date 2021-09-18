package com.aspire.ums.cmdb.index;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICountInstByCdtAPI
 * Author:   hangfang
 * Date:     2019/9/19
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/index")
public interface ICountInstByCdtAPI {



    /**
     * 统计资源池下各设备类型数据
     */
    @RequestMapping(value = "/countByIdcDevCT", method = RequestMethod.GET)
    @ApiOperation(value = "统计资源池下各设备类型数据", notes = "统计资源池下各设备类型数据", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countByIdcDevCT(@RequestParam(value = "idcType", required = false) String idcType,
                                     @RequestParam(value = "queryType", required = false) String queryType);

    /**
     * 统计该资源池下各项目下设备数据
     */
    @RequestMapping(value = "/countByIdcPro", method = RequestMethod.GET)
    @ApiOperation(value = "统计该资源池下各项目下设备数据", notes = "统计该资源池下各项目下设备数据", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countByIdcPro(@RequestParam("idcType") String idcType,
                                   @RequestParam(value = "deviceType", required = false) String deviceType,
                                   @RequestParam(value = "queryType", required = false) String queryType);

    /**
     * 统计该项目下各pod池下设备数据
     */
    @RequestMapping(value = "/countByIdcDevPro", method = RequestMethod.GET)
    @ApiOperation(value = "统计该项目下各pod池下设备数据", notes = "统计该项目下各pod池下设备数据", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countByIdcDevPro(@RequestParam("idcType") String idcType,
                                      @RequestParam("deviceType") String deviceType,
                                      @RequestParam(value = "projectName", required = false) String projectName);

    /**
     * 该一级部门下各资源池业务数量
     */
    @RequestMapping(value = "/countBizByDep1", method = RequestMethod.GET)
    @ApiOperation(value = "该一级部门下各资源池业务数量", notes = "该一级部门下各资源池业务数量", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countBizByDep1(@RequestParam("department1") String department1,
                                    @RequestParam(value = "queryType", required = false) String queryType);

    /**
     * 统计该资源池下各一级部门下的业务
     */
    @RequestMapping(value = "/countBizByIdcDep1", method = RequestMethod.GET)
    @ApiOperation(value = "统计该资源池下各一级部门下的业务", notes = "统计该资源池下各一级部门下的业务", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countBizByIdcDep1(@RequestParam(value = "idcType", required = false) String idcType,
                                       @RequestParam(value = "queryType", required = false) String queryType,
                                                @RequestParam(value = "top", required = false) Integer top);

    /**
     * 各资源池下业务数量
     */
    @RequestMapping(value = "/countBizByIdc", method = RequestMethod.GET)
    @ApiOperation(value = "各资源池下业务数量", notes = "各资源池下业务数量", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countBizByIdc(@RequestParam(value = "queryType", required = false) String queryType);


    /**
     * 统计该一级部门下各二级部门的业务
     */
    @RequestMapping(value = "/countBizByIdcDep2", method = RequestMethod.GET)
    @ApiOperation(value = "统计该一级部门下各二级部门的业务", notes = "统计该一级部门下各二级部门的业务", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> countBizByIdcDep2(@RequestParam(value = "idcType", required = false) String idcType,
                                       @RequestParam("department1") String department1,
                                          @RequestParam(value = "queryType", required = false) String queryType);

    /**
     * 各资源池一级部门下二级部门下各业务系统的设备数量
     */
    @RequestMapping(value = "/countByIdcDep2Biz", method = RequestMethod.GET)
    @ApiOperation(value = "各资源池一级部门下二级部门下各业务系统的设备数量", notes = "各资源池一级部门下二级部门下各业务系统的设备数量", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countByIdcDep2Biz(@RequestParam(value = "idcType", required = false) String idcType,
                                       @RequestParam("department1") String department1,
                                       @RequestParam(value = "department2", required = false) String department2,
                                       @RequestParam(value = "queryType", required = false) String queryType);

    /**
     * 统计设备趋势(日)
     * */
    @RequestMapping(value = "/countDeviceClassTrend/day", method = RequestMethod.GET)
    @ApiOperation(value = "以天为间隔，统计设备趋势", notes = "以天为间隔，统计设备趋势", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> deviceClassTrendWithDay();

    /**
     * 统计设备趋势图(月)
     * */
    @RequestMapping(value = "/countDeviceClassTrend/month", method = RequestMethod.GET)
    @ApiOperation(value = "以月为间隔，统计设备趋势", notes = "以月为间隔，统计设备趋势", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> deviceClassTrendWithMonth();

    /**
     * 统计设备趋势图(年)
     * */
    @RequestMapping(value = "/countDeviceClassTrend/year", method = RequestMethod.GET)
    @ApiOperation(value = "以年为间隔，统计设备趋势", notes = "以月为间隔，统计设备趋势", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> deviceClassTrendWithYear();

    /**
     *  统计各一级租户服务器分布
     * */
    @RequestMapping(value = "/countDeviceClassByDep1", method = RequestMethod.GET)
    @ApiOperation(value = "统计各一级租户设备类型分布", notes = "统计各一级租户服务器分布", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countDeviceClassByDepartment1();

    /**
     *  统计设备子类型分布
     * */
    @RequestMapping(value = "/countDevTypeByDevClass", method = RequestMethod.GET)
    @ApiOperation(value = "统计设备子类型分布", notes = "统计设备子类型分布", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countDeviceTypeByDeviceClass(@RequestParam(value = "deviceClass") String deviceClass);

    /**
     * 各一级租户设备量趋势(日)
     * */
    @RequestMapping(value = "/countDevClsTrendWithDay", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "各一级租户设备量趋势(日)", notes = "各一级租户设备量趋势(日)", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countDevClsByDepWithDay();

    /**
     * 各一级租户设备量趋势(月)
     * */
    @RequestMapping(value = "/countDevClsTrendWithMonth", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "各一级租户设备量趋势(月)", notes = "各一级租户设备量趋势(月)", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countDevClsByDepWithMonth();

    /**
     * 各一级租户设备量趋势(年)
     * */
    @RequestMapping(value = "/countDevClsTrendWithYear", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "各一级租户设备量趋势(年)", notes = "各一级租户设备量趋势(年)", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countDevClsByDepWithYear();

    /**
     * 统计资源池及POD池数量
     */
    @RequestMapping(value = "/countIdcAndPod", method = RequestMethod.GET)
    @ApiOperation(value = "统计资源池及POD池数量", notes = "统计资源池及POD池数量", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> countIdcAndPod();

    /**
     * 统计各设备类型(服务器、网络、存储、安全)设备数量
     */
    @RequestMapping(value = "/countDeviceByDeviceClass", method = RequestMethod.GET)
    @ApiOperation(value = "统计各设备类型(服务器、网络、存储、安全)设备数量", notes = "统计各设备类型(服务器、网络、存储、安全)设备数量",
            tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> countDeviceByDeviceClass();

    /**
     * 统计各资源池设备分配状态（总）
     */
    @RequestMapping(value = "/countStatusAll", method = RequestMethod.GET)
    @ApiOperation(value = "统计各资源池设备分配状态（总）", notes = "统计各资源池设备分配状态（总）",
            tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countStatusAll();

    /**
     * 统计各资源池设备分配状态
     */
    @RequestMapping(value = "/countStatusByIdc", method = RequestMethod.GET)
    @ApiOperation(value = "统计各资源池设备分配状态", notes = "统计各资源池设备分配状态",
            tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countStatusByIdc();

    /**
     * 统计各品牌设备分布（总）
     */
    @RequestMapping(value = "/countByProduceAll", method = RequestMethod.GET)
    @ApiOperation(value = "统计各品牌设备分布", notes = "统计各品牌设备分布",
            tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countByProduceAll();

    /**
     * 统计各资源池品牌设备类型分布
     */
    @RequestMapping(value = "/countByProduce", method = RequestMethod.GET)
    @ApiOperation(value = "统计各资源池品牌设备类型分布", notes = "统计各资源池品牌设备类型分布",
            tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countByProduce(@RequestParam(value = "produce") String produce);

    /**
     * 统计资源总览
     */
    @RequestMapping(value = "/countOverview", method = RequestMethod.GET)
    @ApiOperation(value = "统计资源总览", notes = "统计资源总览",
            tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<Map<String, Object>> countOverview();

    /**
     * 统计磁盘利用率
     */
    @RequestMapping(value = "/countDiskSize", method = RequestMethod.GET)
    @ApiOperation(value = "统计磁盘利用率", notes = "统计磁盘利用率",
            tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class),
            @ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> countDiskSize();

    @PostMapping(value = "/countList")
    @ApiOperation(value = "统计柱形图数据", notes = "统计柱形图数据", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = List.class)
            ,@ApiResponse(code = 500, message = "内部错误")})
    List<Map<String,Object>> countList(@RequestBody Map<String, Object> params);

    @PostMapping(value = "/countObject")
    @ApiOperation(value = "统计非列表数据", notes = "统计非列表数据", tags = {"CMDB Index API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = JSONObject.class)
            ,@ApiResponse(code = 500, message = "内部错误")})
    Map<String, Object> countObject(@RequestBody Map<String, Object> params);
}
