package com.aspire.ums.cmdb.monthReport;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbMonthReportAPI
 * Author:   hangfang
 * Date:     2020/5/9
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RequestMapping("/cmdb/monthReport/")
public interface ICmdbMonthReportAPI {


    /**
     * CMDB
     * 计算资源-物理机总量、已建、在建、已分配、已创建、管理节点
     **/
    @GetMapping(value = "/countCompResource")
    @ApiOperation(value = "计算资源", notes = "计算资源", tags = {"CMDB MonthReport API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    LinkedHashMap<String, Object> countCompResource(@RequestParam("month") String month,
                                                    @RequestParam(value = "idcType", required = false) String idcType);

    /**
     * CMDB
     * 计算资源-创建率/配额分配率
     **/
    @GetMapping(value = "/countRateCompResource")
    @ApiOperation(value = "创建率/配额分配率", notes = "创建率/配额分配率", tags = {"CMDB MonthReport API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<LinkedHashMap<String, Object>>  countRateCompResource(@RequestParam("month") String month,
                                                    @RequestParam(value = "idcType", required = false) String idcType);

    /**
     * CMDB
     * 物理机创建率/分配率月度趋势
     **/
    @GetMapping(value = "/countSixMonthTrend")
    @ApiOperation(value = "物理机创建率/分配率月度趋势", notes = "物理机创建率/分配率月度趋势", tags = {"CMDB MonthReport API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<LinkedHashMap<String, Object>> countSixMonthTrend(@RequestParam("month") String month,
                                                 @RequestParam(value = "idcType", required = false) String idcType,
                                                 @RequestParam(value = "monthCount", required = false) Integer monthCount);

    /**
     * CMDB
     * 网络资源
     **/
    @GetMapping(value = "/countNetworkResource")
    @ApiOperation(value = "网络资源", notes = "网络资源", tags = {"CMDB MonthReport API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    LinkedHashMap<String, Object> countNetworkResource(@RequestParam("month") String month,
                                          @RequestParam(value = "idcType", required = false) String idcType);

    /**
     * CMDB
     * 存储资源
     **/
    @GetMapping(value = "/countStorageResource")
    @ApiOperation(value = "存储资源", notes = "存储资源", tags = {"CMDB MonthReport API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    LinkedHashMap<String, Object> countStorageResource(@RequestParam("month") String month,
                                             @RequestParam(value = "idcType", required = false) String idcType,
                                             @RequestParam("storageType") String storageType);


    /**
     * CMDB
     * 存储资源
     **/
    @GetMapping(value = "/getIdcTypeStoreUseRate")
    @ApiOperation(value = "存储资源利用率", notes = "存储资源利用率", tags = {"CMDB MonthReport API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    LinkedHashMap<String, Object> getIdcTypeStoreUseRate(@RequestParam("month") String month,
                                                       @RequestParam(value = "idcType", required = false) String idcType,
                                                       @RequestParam("storageType") String storageType);
    /**
     * CMDB
     * 计算资源--存储利用率月度趋势
     **/
    @GetMapping(value = "/countStorageSixMonthTrend")
    @ApiOperation(value = "存储利用率月度趋势", notes = "存储利用率月度趋势", tags = {"CMDB MonthReport API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<LinkedHashMap<String, Object>> countStorageSixMonthTrend(@RequestParam("month") String month,
                                                                  @RequestParam(value = "monthCount", required = false) Integer monthCount,
                                                                  @RequestParam(value = "type") String type,
                                             @RequestParam(value = "idcType", required = false) String idcType,
                                                  @RequestParam("storageType") String storageType);


    /**
     * CMDB
     * 资源回收/清理
     **/
    @GetMapping(value = "/countRecycleResource")
    @ApiOperation(value = "资源回收/清理", notes = "资源回收/清理", tags = {"CMDB MonthReport API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<LinkedHashMap<String, Object>> countRecycleResource(@RequestParam("month") String month,
                                                             @RequestParam("countType") String countType,
                                                             @RequestParam(value = "idcType", required = false) String idcType,
                                                             @RequestParam("recycleType") String recycleType);


    /**
     * ALERT
     * 裸金属均峰值利用率
     **/
    @GetMapping(value = "/ALERT/countPhyAvgMaxUtz")
    @ApiOperation(value = "裸金属均峰值利用率", notes = "裸金属均峰值利用率", tags = {"CMDB MonthReport API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    List<LinkedHashMap<String, Object>> countPhyAvgMaxUtz(@RequestParam("month") String month,
                                                          @RequestParam("deviceType") String deviceType,
                                                                  @RequestParam(value = "idcType", required = false) String idcType);

//
//    /**
//     * ALERT
//     * 裸金属利用率月度趋势
//     **/
//    @GetMapping(value = "/ALERT/countPhyUtzTrend")
//    @ApiOperation(value = "裸金属利用率月度趋势", notes = "裸金属利用率月度趋势", tags = {"CMDB MonthReport API"})
//    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
//            @ApiResponse(code = 500, message = "内部错误")})
//    List<Map<String, String>> countPhyUtzTrend(@RequestParam("month") String month,
//                                                         @RequestParam("countType") String countType,
//                                                          @RequestParam("deviceType") String deviceType,
//                                                          @RequestParam(value = "idcType", required = false) String idcType);

    /**
     * ALERT
     * 告警
     **/
    @GetMapping(value = "/ALERT/countAlert")
    @ApiOperation(value = "告警", notes = "告警", tags = {"CMDB MonthReport API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    LinkedHashMap<String, Object> countAlert(@RequestParam("month") String month,
                                                          @RequestParam(value = "idcType", required = false) String idcType);

    /**
     * BPM
     * 故障
     **/
    @GetMapping(value = "/BPM/countTrouble")
    @ApiOperation(value = "故障", notes = "故障", tags = {"CMDB MonthReport API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    LinkedHashMap<String, Object> countTrouble(@RequestParam("month") String month,
                                             @RequestParam(value = "idcType", required = false) String idcType);


    /**
     * BPM
     * 工单
     **/
    @GetMapping(value = "/BPM/countOrder")
    @ApiOperation(value = "工单", notes = "工单", tags = {"CMDB MonthReport API"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "查询成功", response = Map.class),
            @ApiResponse(code = 500, message = "内部错误")})
    LinkedHashMap<String, Object> countOrder(@RequestParam("month") String month,
                                               @RequestParam(value = "idcType", required = false) String idcType);

}
