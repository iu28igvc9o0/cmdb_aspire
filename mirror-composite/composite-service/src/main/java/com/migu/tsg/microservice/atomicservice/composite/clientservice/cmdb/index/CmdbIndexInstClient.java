package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.index;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbIndexInstClient
 * Author:   hangfang
 * Date:     2019/9/23
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface CmdbIndexInstClient {

    /**
     * 统计资源池下各设备类型数据
     */
    @RequestMapping(value = "/cmdb/index/countByIdcDevCT", method = RequestMethod.GET)
    List<JSONObject> countByIdcDevCT(@RequestParam(value = "idcType", required = false) String idcType,
                                     @RequestParam(value = "queryType", required = false) String queryType);

    /**
     * 统计该资源池下各项目下设备数据
     */
    @RequestMapping(value = "/cmdb/index/countByIdcPro", method = RequestMethod.GET)
    List<JSONObject> countByIdcPro(@RequestParam("idcType") String idcType,
                                   @RequestParam(value = "deviceType", required = false) String deviceType,
                                   @RequestParam(value = "queryType", required = false) String queryType);

    /**
     * 统计该项目下各pod池下设备数据
     */
    @RequestMapping(value = "/cmdb/index/countByIdcDevPro", method = RequestMethod.GET)
    List<JSONObject> countByIdcDevPro(@RequestParam("idcType") String idcType,
                                      @RequestParam("deviceType") String deviceType,
                                      @RequestParam(value = "projectName", required = false) String projectName);

    /**
     * 各资源池下业务数量
     */
    @RequestMapping(value = "/cmdb/index/countBizByIdc", method = RequestMethod.GET)
    List<JSONObject> countBizByIdc(@RequestParam(value = "queryType", required = false) String queryType);

    /**
     * 该一级部门下各资源池业务数量
     */
    @RequestMapping(value = "/cmdb/index/countBizByDep1", method = RequestMethod.GET)
    List<JSONObject> countBizByDep1(@RequestParam("department1") String department1,
                                    @RequestParam(value = "queryType", required = false) String queryType);
    /**
     * 统计该资源池下各一级部门下的业务
     */
    @RequestMapping(value = "/cmdb/index/countBizByIdcDep1", method = RequestMethod.GET)
    List<JSONObject> countBizByIdcDep1(@RequestParam(value = "idcType", required = false) String idcType,
                                       @RequestParam(value = "queryType", required = false) String queryType,
                                       @RequestParam(value = "top", required = false) Integer top);

    /**
     * 统计该一级部门下各二级部门的业务
     */
    @RequestMapping(value = "/cmdb/index/countBizByIdcDep2", method = RequestMethod.GET)
    Map<String, Object> countBizByIdcDep2(@RequestParam(value = "idcType", required = false) String idcType,
                                       @RequestParam("department1") String department1,
                                          @RequestParam(value = "queryType", required = false) String queryType);

    /*
     * 统计设备趋势(日)
     * */
    @RequestMapping(value = "/cmdb/index/countDeviceClassTrend/day")
    List<JSONObject> deviceClassTrendWithDay();

    /*
     * 统计设备趋势图(月)
     * */
    @RequestMapping(value = "/cmdb/index/countDeviceClassTrend/month")
    List<JSONObject> deviceClassTrendWithMonth();

    /*
     * 统计设备趋势图(年)
     * */
    @RequestMapping(value = "/cmdb/index/countDeviceClassTrend/year")
    List<JSONObject> deviceClassTrendWithYear();

    /*
     *  统计各一级租户设备类型分布
     * */
    @RequestMapping(value = "/cmdb/index/countDeviceClassByDep1",method = RequestMethod.GET)
    List<JSONObject> countDeviceClassByDepartment1();

    /*
     *  统计设备子类型分布
     * */
    @RequestMapping(value = "/cmdb/index/countDevTypeByDevClass",method = RequestMethod.GET)
    List<JSONObject> countDeviceTypeByDeviceClass(@RequestParam(value="deviceClass") String deviceClass);

    /*
     * 各一级租户设备量趋势(日)
     * */
    @RequestMapping(value = "/cmdb/index/countDevClsTrendWithDay",method = RequestMethod.GET)
    @ResponseBody
    List<Map<String,Object>> countDevClsByDepWithDay();

    /*
     * 各一级租户设备量趋势(月)
     * */
    @RequestMapping(value = "/cmdb/index/countDevClsTrendWithMonth",method = RequestMethod.GET)
    @ResponseBody
    List<Map<String,Object>> countDevClsByDepWithMonth();
    /**
     * 各一级租户设备量趋势(年)
     * */
    @RequestMapping(value = "/cmdb/index/countDevClsTrendWithYear",method = RequestMethod.GET)
    @ResponseBody
    List<Map<String,Object>> countDevClsByDepWithYear();

    /**
     * 各资源池一级部门下二级部门下各业务系统的设备数量
     */
    @RequestMapping(value = "/cmdb/index/countByIdcDep2Biz", method = RequestMethod.GET)
    List<JSONObject> countByIdcDep2Biz(@RequestParam(value = "idcType", required = false) String idcType,
                                       @RequestParam("department1") String department1,
                                       @RequestParam(value = "department2", required = false) String department2,
                                       @RequestParam(value = "queryType", required = false) String queryType);
  /**
     * 统计资源池及POD池数量
     */
    @RequestMapping(value = "/cmdb/index/countIdcAndPod", method = RequestMethod.GET)
    Map<String, Object> countIdcAndPod();

    /**
     * 统计各设备类型(服务器、网络、存储、安全)设备数量
     */
    @RequestMapping(value = "/cmdb/index/countDeviceByDeviceClass", method = RequestMethod.GET)
    Map<String, Object> countDeviceByDeviceClass();
    
    /**
     * 统计磁盘利用率
     */
    @RequestMapping(value = "/cmdb/index/countDiskSize", method = RequestMethod.GET)
    Map<String, Object> countDiskSize();

    /**
     * 统计各资源池设备分配状态（总）
     */
    @RequestMapping(value = "/cmdb/index/countStatusAll", method = RequestMethod.GET)
    List<Map<String, Object>> countStatusAll();

    /**
     * 统计各资源池设备分配状态
     */
    @RequestMapping(value = "/cmdb/index/countStatusByIdc", method = RequestMethod.GET)
    List<Map<String, Object>> countStatusByIdc();


    /**
     * 统计各品牌设备分布（总）
     */
    @RequestMapping(value = "/cmdb/index/countByProduceAll", method = RequestMethod.GET)
    List<Map<String, Object>> countByProduceAll();

    /**
     * 统计各资源池品牌设备类型分布
     */
    @RequestMapping(value = "/cmdb/index/countByProduce", method = RequestMethod.GET)
    List<Map<String, Object>> countByProduce(@RequestParam(value = "produce") String produce);

    /**
     * 统计资源总览
     */
    @RequestMapping(value = "/cmdb/index/countOverview", method = RequestMethod.GET)
    List<Map<String, Object>> countOverview();

    @PostMapping(value = "/cmdb/index/countList")
    List<Map<String,Object>> countList(@RequestBody Map<String,Object> params);

    @PostMapping (value = "/cmdb/index/countObject")
    Map<String, Object> countObject(@RequestBody Map<String,Object> params);

}
