package com.aspire.ums.cmdb.v2.index.web;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.index.ICountInstByCdtAPI;
import com.aspire.ums.cmdb.v2.index.serivce.CountInstCdtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CountInstCdtController
 * Author:   hangfang
 * Date:     2019/9/20
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CountInstCdtController implements ICountInstByCdtAPI {

    @Autowired
    private CountInstCdtService instCdtService;
    /**
     * 统计资源池下各设备类型数据
     * @param queryType server
     */
    @Override
    public List<Map<String, Object>> countByIdcDevCT(@RequestParam(value = "idcType", required = false) String idcType,
                                            @RequestParam(value = "queryType", required = false) String queryType) {
        return instCdtService.countByIdcDevCT(idcType, queryType);
    }

    /**
     * 统计该资源池下各项目下设备数据
     */
    @Override
    public List<Map<String, Object>> countByIdcPro(@RequestParam("idcType") String idcType,
                                          @RequestParam(value = "deviceType", required = false) String deviceType,
                                          @RequestParam(value = "queryType", required = false) String queryType) {
        return instCdtService.countByIdcPro(idcType, deviceType,queryType);
    }

    /**
     * 统计该项目下各pod池下设备数据
     * @param idcType 资源池
     * @param deviceType 设备类型
     * @param projectName 项目名
     */
    @Override
    public List<Map<String, Object>> countByIdcDevPro(@RequestParam("idcType") String idcType,
                                                @RequestParam("deviceType") String deviceType,
                                                @RequestParam(value = "projectName",required = false) String projectName){
        return instCdtService.countByIdcDevPro(idcType, deviceType, projectName);
    }

    /**
     * 该一级部门下各资源池业务数量
     */
    @Override
    public List<Map<String, Object>> countBizByDep1(@RequestParam("department1") String department1,
                                           @RequestParam(value = "queryType", required = false) String queryType) {
        return instCdtService.countBizByDep1(department1, queryType);
    }
    /**
     * 统计该资源池下各一级部门下的业务
     */
    @Override

    public List<Map<String, Object>> countBizByIdcDep1(@RequestParam(value = "idcType", required = false) String idcType,
                                              @RequestParam(value = "queryType", required = false) String queryType,
                                                       @RequestParam(value = "top", required = false)Integer top) {
        return instCdtService.countBizByIdcDep1(idcType, queryType, top);
    }


    @Override

    public List<Map<String, Object>> countBizByIdc(@RequestParam(value = "queryType", required = false) String queryType) {
        return instCdtService.countBizByIdc(queryType);
    }

 @Override
    public Map<String, Object> countBizByIdcDep2(@RequestParam(value = "idcType", required = false) String idcType,
                                                 @RequestParam("department1") String department1,
                                                 @RequestParam(value = "queryType", required = false) String queryType) {
         Map<String, Object> resultMap = new HashMap<>();
         List<Map<String, Object>> res = instCdtService.countBizByIdcDep2(idcType, department1, queryType);
         Integer total = 0;
         for(Map<String, Object> count :  res) {
             total += Integer.parseInt(count.get("number").toString());
         }
         resultMap.put("dataList", res);
         resultMap.put("total", total);
        return resultMap;
    }

    @Override
    public List<Map<String, Object>> countByIdcDep2Biz(@RequestParam(value = "idcType", required = false) String idcType,
                                              @RequestParam("department1") String department1,
                                              @RequestParam(value = "department2", required = false) String department2,
                                              @RequestParam(value = "queryType", required = false) String queryType) {
        return instCdtService.countByIdcDep2Biz(idcType, department1, department2, queryType);
    }
    /**
     * 统计设备趋势(日)
     * */
    @Override
    public List<Map<String, Object>> deviceClassTrendWithDay() {
        return instCdtService.deviceClassTrendWithDay();
    }

    /**
     * 统计设备趋势图(月)
     * */
    @Override
    public List<Map<String, Object>> deviceClassTrendWithMonth() {
        return instCdtService.deviceClassTrendWithMonth();
    }

    /**
     * 统计设备趋势图(月)
     * */
    @Override
    public List<Map<String, Object>> deviceClassTrendWithYear() {
        return instCdtService.deviceClassTrendWithYear();
    }

    /**
     *  统计各一级租户设备类型分布
     * */
    @Override
    public List<Map<String, Object>> countDeviceClassByDepartment1() {
        return instCdtService.countDeviceClassByDepartment1();
    }

    /**
     *  统计设备子类型分布
     * */
    @Override
    public List<Map<String, Object>> countDeviceTypeByDeviceClass(@RequestParam(value="deviceClass") String deviceClass) {
        return instCdtService.countDeviceTypeByDeviceClass(deviceClass);
    }

    /**
     * 各一级租户设备量趋势(日)
     * */
    @Override
    public List<Map<String,Object>> countDevClsByDepWithDay() {
        return instCdtService.countDevClsByDepWithDay();
    }

    @Override
    public List<Map<String, Object>> countDevClsByDepWithMonth() {
        return instCdtService.countDevClsByDepWithMonth();
    }

    @Override
    public List<Map<String, Object>> countDevClsByDepWithYear() {
        return instCdtService.countDevClsByDepWithYear();
    }

    /**
     * 统计资源池及POD池数量
     */
    @Override
    public Map<String, Object> countIdcAndPod() {
        return instCdtService.countIdcAndPod();
    }

    /**
     * 统计各设备类型(服务器、网络、存储、安全)设备数量
     */
    @Override
    public Map<String, Object> countDeviceByDeviceClass() {
        return instCdtService.countDeviceByDeviceClass();
 }

    @Override
    public List<Map<String, Object>> countStatusAll() {
        return instCdtService.countStatusAll();
    }

    @Override
    public List<Map<String, Object>> countStatusByIdc() {
        return instCdtService.countStatusByIdc();
    }

    @Override
    public List<Map<String, Object>> countByProduceAll() {
        return instCdtService.countByProduceAll();
    }

    @Override
    public List<Map<String, Object>> countByProduce(@RequestParam("produce") String produce) {
        return instCdtService.countByProduce(produce);
    }

    @Override
    public List<Map<String, Object>> countOverview() {
        return instCdtService.countOverview();
    }

    @Override
    public Map<String, Object> countDiskSize() {
        return instCdtService.countDiskSize();
    }

    @Override
    public List<Map<String, Object>> countList(@RequestBody Map<String, Object> params) {
        if (null == params.get("queryName")) {
            return null;
        }
        return instCdtService.countList(params);
    }

    @Override
    public Map<String, Object> countObject(@RequestBody Map<String, Object> params) {
        if (null == params.get("queryName")) {
            return null;
        }
        return instCdtService.countObject(params);
    }

}
