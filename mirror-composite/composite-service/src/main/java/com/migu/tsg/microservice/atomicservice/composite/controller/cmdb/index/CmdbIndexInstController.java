package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.index;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.index.ICountInstByCdtAPI;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.index.CmdbIndexInstClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import groovy.util.logging.Slf4j;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbIndexInstController
 * Author:   hangfang
 * Date:     2019/9/23
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbIndexInstController  implements ICountInstByCdtAPI {

    @Autowired
    private CmdbIndexInstClient indexInstClient;

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<JSONObject> countByIdcDevCT(@RequestParam(value = "idcType", required = false) String idcType,
                                            @RequestParam(value = "queryType", required = false) String queryType) {
        return indexInstClient.countByIdcDevCT(idcType, queryType);
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<JSONObject> countByIdcPro(@RequestParam("idcType") String idcType,
                                          @RequestParam(value = "deviceType", required = false) String deviceType,
                                          @RequestParam(value = "queryType", required = false) String queryType) {
        return indexInstClient.countByIdcPro(idcType, deviceType, queryType);
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<JSONObject> countByIdcDevPro(@RequestParam("idcType") String idcType,
                                             @RequestParam("deviceType") String deviceType,
                                             @RequestParam(value = "projectName",required = false) String projectName) {
        return indexInstClient.countByIdcDevPro(idcType, deviceType, projectName);
    }

    /**
     * 各资源池下业务数量
     */
    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<JSONObject> countBizByIdc(@RequestParam(value = "queryType", required = false) String queryType){
        return indexInstClient.countBizByIdc(queryType);
    }

    /**
     * 该一级部门下各资源池业务数量
     */
    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<JSONObject> countBizByDep1(@RequestParam("department1") String department1,
                                           @RequestParam(value = "queryType", required = false) String queryType) {
        return indexInstClient.countBizByDep1(department1, queryType);
    }
    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<JSONObject> countBizByIdcDep1(@RequestParam(value = "idcType", required = false) String idcType,
                                              @RequestParam(value = "queryType", required = false) String queryType,
                                              @RequestParam(value = "top", required = false) Integer top) {
        return indexInstClient.countBizByIdcDep1(idcType, queryType, top);
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public Map<String, Object> countBizByIdcDep2(@RequestParam(value = "idcType", required = false) String idcType,
                                                 @RequestParam("department1") String department1,
                                                 @RequestParam(value = "queryType", required = false) String queryType) {
        return indexInstClient.countBizByIdcDep2(idcType, department1, queryType);
    }
    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<JSONObject> countByIdcDep2Biz(@RequestParam(value = "idcType", required = false) String idcType,
                                              @RequestParam("department1") String department1,
                                              @RequestParam(value = "department2", required = false) String department2,
                                              @RequestParam(value = "queryType", required = false) String queryType){
        return indexInstClient.countByIdcDep2Biz(idcType, department1, department2, queryType);
    } 
    @Override
    @ResAction(action = "view", resType = "script", loadResFilter=true)
    public List<JSONObject> deviceClassTrendWithDay() {
        return indexInstClient.deviceClassTrendWithDay();
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<JSONObject> deviceClassTrendWithMonth() {
        return indexInstClient.deviceClassTrendWithMonth();
    }

    /*
     * 统计设备趋势图(年)
     * */
    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<JSONObject> deviceClassTrendWithYear() {
        return indexInstClient.deviceClassTrendWithYear();
    }
    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<JSONObject> countDeviceClassByDepartment1() {
        return indexInstClient.countDeviceClassByDepartment1();
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<JSONObject> countDeviceTypeByDeviceClass(@RequestParam(value="deviceClass") String deviceClass) {
        return indexInstClient.countDeviceTypeByDeviceClass(deviceClass);
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<Map<String, Object>> countDevClsByDepWithDay() {
        return indexInstClient.countDevClsByDepWithDay();
    }

    @Override
    @ResAction(action = "view", resType = "script", loadResFilter=true)
    public List<Map<String, Object>> countDevClsByDepWithMonth() {
        return indexInstClient.countDevClsByDepWithMonth();
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<Map<String, Object>> countDevClsByDepWithYear() {
        return indexInstClient.countDevClsByDepWithYear();
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public  Map<String, Object> countIdcAndPod() {
        return indexInstClient.countIdcAndPod();
    }

    @Override
    @ResAction(action = "view", resType = "script", loadResFilter=true)
    public Map<String, Object> countDeviceByDeviceClass() {
        return indexInstClient.countDeviceByDeviceClass();
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<Map<String, Object>>countStatusAll() {
        return indexInstClient.countStatusAll();
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<Map<String, Object>> countStatusByIdc() {
        return indexInstClient.countStatusByIdc();
    }

    @Override
    @ResAction(action = "view", resType = "script", loadResFilter=true)
    public List<Map<String, Object>> countByProduceAll() {
        return indexInstClient.countByProduceAll();
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<Map<String, Object>> countByProduce(@RequestParam(value = "produce") String produce) {
        return indexInstClient.countByProduce(produce);
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<Map<String, Object>> countOverview() {
        return indexInstClient.countOverview();
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public Map<String, Object> countDiskSize() {
        return indexInstClient.countDiskSize();
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public List<Map<String, Object>> countList(@RequestBody Map<String, Object> params) {
        return indexInstClient.countList(params);
    }

    @Override
    @ResAction(action = "view", resType = "cmdb", loadResFilter=true)
    public Map<String, Object> countObject(@RequestBody Map<String, Object> params) {
        return indexInstClient.countObject(params);
    }
}
