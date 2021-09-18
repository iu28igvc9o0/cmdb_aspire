package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.monthReport;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
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
@FeignClient(value = "CMDB")
public interface CmdbMonthReportClient {

    /**
     * 计算资源-物理机总量、已建、在建、已分配、已创建、管理节点
     **/
    @GetMapping(value = "/cmdb/monthReport/countCompResource")
    Map<String, Object> countCompResource(@RequestParam("month") String month,
                                          @RequestParam(value = "idcType", required = false) String idcType);

    /**
     * 计算资源-创建率/配额分配率
     **/
    @GetMapping(value = "/cmdb/monthReport/countRateCompResource")
    List<LinkedHashMap<String, Object>> countRateCompResource(@RequestParam("month") String month,
                                                              @RequestParam(value = "idcType", required = false) String idcType);

    /**
     * 物理机创建率/分配率月度趋势
     **/
    @GetMapping(value = "/cmdb/monthReport/countSixMonthTrend")
    List<Map<String, Object>> countSixMonthTrend(@RequestParam("month") String month,
                                                 @RequestParam(value = "idcType", required = false) String idcType,
                                                 @RequestParam(value = "monthCount", required = false) Integer monthCount);
    /**
     * 存储资源
     **/
    @GetMapping(value = "/cmdb/monthReport/countStorageResource")
    Map<String, Object> countStorageResource(@RequestParam("month") String month,
                                             @RequestParam(value = "idcType", required = false) String idcType,
                                             @RequestParam("storageType") String storageType);


    /**
     * CMDB
     * 存储资源
     **/
    @GetMapping(value = "/cmdb/monthReport/getIdcTypeStoreUseRate")
    LinkedHashMap<String, Object> getIdcTypeStoreUseRate(@RequestParam("month") String month,
                                                         @RequestParam(value = "idcType", required = false) String idcType,
                                                         @RequestParam("storageType") String storageType);

    /**
     * 存储资源--存储利用率月度趋势
     **/
    @GetMapping(value = "/cmdb/monthReport/countStorageSixMonthTrend")
    List<Map<String, Object>> countStorageSixMonthTrend(@RequestParam("month") String month,
                                                        @RequestParam(value = "monthCount", required = false) Integer monthCount,
                                                        @RequestParam(value = "type") String type,
                                                        @RequestParam(value = "idcType", required = false) String idcType,
                                                        @RequestParam("storageType") String storageType);

    /**
     * 网络资源
     **/
    @GetMapping(value = "/cmdb/monthReport/countNetworkResource")
    Map<String, Object> countNetworkResource(@RequestParam("month") String month,
                                             @RequestParam(value = "idcType", required = false) String idcType);


    /**
     * 资源回收/资源清理
     **/
    @GetMapping(value = "/cmdb/monthReport/countRecycleResource")
    List<Map<String, Object>> countRecycleResource(@RequestParam("month") String month,
                                                   @RequestParam("countType") String countType,
                                             @RequestParam(value = "idcType", required = false) String idcType,
                                             @RequestParam("recycleType") String recycleType);


    /**
     * ALERT
     * 裸金属均峰值利用率
     **/
    @GetMapping(value = "/cmdb/monthReport/ALERT/countPhyAvgMaxUtz")
    List<LinkedHashMap<String, Object>> countPhyAvgMaxUtz(@RequestParam("month") String month,
                                                          @RequestParam("deviceType") String deviceType,
                                                          @RequestParam(value = "idcType", required = false) String idcType);


    /**
     * ALERT
     * 告警
     **/
    @GetMapping(value = "/cmdb/monthReport/ALERT/countAlert")
    LinkedHashMap<String, Object> countAlert(@RequestParam("month") String month,
                                             @RequestParam(value = "idcType", required = false) String idcType);

    /**
     * BPM
     * 故障
     **/
    @GetMapping(value = "/cmdb/monthReport/BPM/countTrouble")
    LinkedHashMap<String, Object> countTrouble(@RequestParam("month") String month,
                                               @RequestParam(value = "idcType", required = false) String idcType);


    /**
     * BPM
     * 工单
     **/
    @GetMapping(value = "/cmdb/monthReport/BPM/countOrder")
    LinkedHashMap<String, Object> countOrder(@RequestParam("month") String month,
                                             @RequestParam(value = "idcType", required = false) String idcType);


}
