package com.aspire.ums.cmdb.v2.index.serivce.impl;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.sqlManage.service.CmdbSqlManageService;
import com.aspire.ums.cmdb.v2.index.mapper.CmdbIndexInstMapper;
import com.aspire.ums.cmdb.v2.index.serivce.CountInstCdtService;
import com.aspire.ums.cmdb.v2.index.util.SuppleUtil;
import com.aspire.ums.cmdb.v3.redis.service.IRedisService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/9/20
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
public class CountInstCdtServiceImpl implements CountInstCdtService {

    private static final String PHY = "06844828f79041439ba6515c3f8e5201"; // X86服务器

    private static final String VM = "f5f6b15cc48a43a1b02467f0bfcfbeae"; // 云主机
    private static final String TYPE = "device_type";
    private static final String CLASS3 = "device_class_3";
    private static final String CLASS = "device_class";

    @Autowired
    private CmdbIndexInstMapper indexInstMapper;
    @Autowired
    private JDBCHelper jdbcHelper;
    @Autowired
    private CmdbSqlManageService sqlManageService;
    @Autowired
    private IRedisService redisService;

    @Override
    public List<Map<String, Object>> countByIdcDevCT(String idcType, String queryType) {
        // 先去获取 cmdb_sql_source => sql source_owner(主机/IP)
//        return jdbcHelper.getQueryList("", null, true);
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countByIdcDevCT");
        Map<String, Object> params = new HashMap<>();
        params.put("idcType", idcType);
        params.put("deviceClass", queryType);
        return jdbcHelper.getQueryList(sqlSource, null, null, null, params);
    }

    @Override
    public List<Map<String,Object>> countByIdcPro(String idcType, String deviceType,String queryType) {
        String column = "";
        if (PHY.equals(deviceType)) {
            column = CLASS3;
        } else if (VM.equals(deviceType)) {
            column = TYPE;
        } else{
            column = CLASS;
        }
        List<Map<String,Object>> projects = listDistinctPro(idcType, column, deviceType);
        if (projects != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("idcType", idcType);
            params.put("projects", projects);
            params.put("deviceType", deviceType);
            params.put("deviceClass", queryType);
            CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countByIdcPro");
//            return indexInstMapper.countByIdcPro(params);
            return jdbcHelper.getQueryList(sqlSource, null, null, null, params);
        }
       return null;
    }

    @Override
    public List<Map<String, Object>> countByIdcDevPro(String idcType, String deviceType, String projectName) {
        // 判断下设备类型是根据device_type还是device_class筛选
        String column = "";
        if (PHY.equals(deviceType)) {
            column = CLASS3;
        } else if (VM.equals(deviceType)) {
            column = TYPE;
        } else{
            column = CLASS;
        }
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countByIdcDevPro");
        Map<String, Object> params = new HashMap<>();
        params.put("idcType", idcType);
        params.put("column", column);
        params.put("deviceType", deviceType);
        params.put("projectName", projectName);
//        indexInstMapper.countByIdcDevPro(idcType, column, deviceType, projectName);
        return jdbcHelper.getQueryList(sqlSource, null, null, null, params);
    }

    public List<Map<String, Object>> listDistinctPro(String idcType,String column, String deviceType) {
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_listDistinctPro");
        Map<String, Object> params = new HashMap<>();
        params.put("idcType", idcType);
        params.put("column", column);
        params.put("deviceType", deviceType);
//        indexInstMapper.countBizByIdc(queryType);
        return jdbcHelper.getQueryList(sqlSource, null, null, null, params);
    }
    @Override
    public List<Map<String, Object>> countBizByIdc(String queryType) {
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countBizByIdc");
        Map<String, Object> params = new HashMap<>();
        params.put("deviceClass", queryType);
//        indexInstMapper.countBizByIdc(queryType);
        return jdbcHelper.getQueryList(sqlSource, null, null, null, params);
    }

    @Override
    public List<Map<String, Object>> countBizByDep1(String department1, String queryType) {
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countBizByDep1");
        Map<String, Object> params = new HashMap<>();
        params.put("department1", department1);
        params.put("deviceClass", queryType);
//        indexInstMapper.countBizByDep1(department1, queryType);
        return jdbcHelper.getQueryList(sqlSource, null, null, null, params);
    }

    @Override
public List<Map<String, Object>> countBizByIdcDep1(String idcType, String queryType, Integer top) {
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countBizByIdcDep1");
        Map<String, Object> params = new HashMap<>();
        params.put("idcType", idcType);
        params.put("deviceClass", queryType);
        params.put("top", top);
//        indexInstMapper.countBizByIdcDep1(idcType, queryType, top);
        return jdbcHelper.getQueryList(sqlSource, null, null, null, params);
    }

    @Override
   public List<Map<String, Object>> countBizByIdcDep2(String idcType, String department1, String queryType) {
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countBizByIdcDep2");
        Map<String, Object> params = new HashMap<>();
        params.put("idcType", idcType);
        params.put("department1", department1);
        params.put("deviceClass", queryType);
//        indexInstMapper.countBizByIdcDep2(idcType, department1, queryType);
        return jdbcHelper.getQueryList(sqlSource, null, null, null, params);
    }

    @Override
    public List<Map<String, Object>> countByIdcDep2Biz(String idcType, String department1, String department2, String queryType) {
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countByIdcDep2Biz");
        Map<String, Object> params = new HashMap<>();
        params.put("idcType", idcType);
        params.put("department1", department1);
        params.put("department2", department2);
        params.put("deviceClass", queryType);
//        indexInstMapper.countByIdcDep2Biz(idcType, department1, department2, queryType);
        return jdbcHelper.getQueryList(sqlSource, null, null, null, params);
    }

 @Override
    public List<Map<String, Object>> deviceClassTrendWithDay() {
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_deviceClassTrendWithDay");
//        indexInstMapper.deviceClassTrendWithDay();
        return jdbcHelper.getQueryList(sqlSource, null, null, null, new HashMap<>());
    }

    @Override
    public List<Map<String, Object>> deviceClassTrendWithMonth() {
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_deviceClassTrendWithMonth");
//        indexInstMapper.deviceColassTrendWithMonth();
        return jdbcHelper.getQueryList(sqlSource, null, null, null, new HashMap<>());
    }

    @Override
    public List<Map<String, Object>> deviceClassTrendWithYear() {
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_deviceClassTrendWithYear");
//        indexInstMapper.deviceClassTrendWithYear();
        return jdbcHelper.getQueryList(sqlSource, null, null, null, new HashMap<>());
    }

    @Override
    public List<Map<String, Object>> countDeviceClassByDepartment1() {
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countDeviceClassByDepartment1");
//        indexInstMapper.countDeviceClassByDepartment1();
        return jdbcHelper.getQueryList(sqlSource, null, null, null, new HashMap<>());
    }

    @Override
    public List<Map<String, Object>> countDeviceTypeByDeviceClass(String deviceClass) {
//        return indexInstMapper.countDeviceTypeByDeviceClass(deviceClass);
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countDeviceTypeByDeviceClass");
        Map<String, Object> params = new HashMap<>();
        params.put("deviceClass", deviceClass);
        return jdbcHelper.getQueryList(sqlSource, null, null, null, params);
    }

    @Override
    public List<Map<String,Object>> countDevClsByDepWithDay() {
//        List<Map<String,Object>> rsList = indexInstMapper.countDevClsByDepWithDay();
//        return
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countDevClsByDepWithDay");
        List<Map<String,Object>> rsList = jdbcHelper.getQueryList(sqlSource, null, null, null, new HashMap<>());
        return SuppleUtil.packageData("DAY",rsList);
    }

    @Override
    public List<Map<String, Object>> countDevClsByDepWithMonth() {
//        List<Map<String,Object>> rsList = indexInstMapper.countDevClsByDepWithMonth();
//        return SuppleUtil.packageData("MONTH",rsList);
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countDevClsByDepWithMonth");
        List<Map<String,Object>> rsList = jdbcHelper.getQueryList(sqlSource, null, null, null, new HashMap<>());
        return SuppleUtil.packageData("MONTH",rsList);
    }

    @Override
    public List<Map<String, Object>> countDevClsByDepWithYear() {
//        List<Map<String,Object>> rsList = indexInstMapper.countDevClsByDepWithYear();
//        return  SuppleUtil.packageData("YEAR",rsList);
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countDevClsByDepWithYear");
        List<Map<String,Object>> rsList = jdbcHelper.getQueryList(sqlSource, null, null, null, new HashMap<>());
        return SuppleUtil.packageData("YEAR",rsList);
    }
    @Override
    public Map<String, Object> countIdcAndPod() {
//        return indexInstMapper.countIdcAndPod();
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countIdcAndPod");
        return jdbcHelper.getQueryMap(sqlSource, null, new HashMap<>());
        }
    @Override
    public Map<String, Object> countDeviceByDeviceClass() {
//        return indexInstMapper.countDeviceByDeviceClass();
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countDeviceByDeviceClass");
        return jdbcHelper.getQueryMap(sqlSource, null, new HashMap<>());
    }

    @Override
    public List<Map<String, Object>> countStatusAll() {
//        List<String> statusList = indexInstMapper.listStatus();
//        return indexInstMapper.countStatusAll(statusList);
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countStatusAll");
        Map<String, Object> params = new HashMap<>();
//        params.put("statusList", statusList);
        return jdbcHelper.getQueryList(sqlSource, null, null, null, params);
    }

    @Override
    public List<Map<String, Object>> countStatusByIdc() {
        List<Map<String,String>> statusList = this.listStatus();
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countStatusByIdc");
        Map<String, Object> params = new HashMap<>();
        params.put("statusList", statusList);
        return jdbcHelper.getQueryList(sqlSource, null, null, null, params);
    }

    /**
     * 获取所有设备状态
     */
    @Override
    public List<Map<String,String>> listStatus() {
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_listStatus");
        return jdbcHelper.getQueryList(sqlSource, null, null, null, null);
    }

    @Override
    public List<Map<String, Object>> countByProduceAll() {
//        return indexInstMapper.countByProduceAll();
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countByProduceAll");
        return jdbcHelper.getQueryList(sqlSource, null, null, null, new HashMap<>());
    }

    @Override
    public List<Map<String, Object>> countByProduce(String produce) {
//        return indexInstMapper.countByProduce(produce);
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countByProduce");
        Map<String, Object> params = new HashMap<>();
        params.put("produce", produce);
        return jdbcHelper.getQueryList(sqlSource, null, null, null, params);
    }

    @Override
    public List<Map<String, Object>> countOverview() {
        Object object = redisService.get(Constants.REDIS_RESOURCE_INDEX_ASSIGNED_INFO);
        if (object == null) {
            return queryOverview();
        }
        return (new ObjectMapper().convertValue(object, new TypeReference<List<Map<String, String>>>(){}));
    }

    public List<Map<String, Object>> queryOverview() {
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countOverview");
        return jdbcHelper.getQueryList(sqlSource, null, null, null, new HashMap<>());
    }

    @Override
    public Map<String, Object> countDiskSize() {
//        return indexInstMapper.countDiskSize();
        CmdbSqlManage sqlSource = sqlManageService.getByName("ICountInstByCdtAPI_countDiskSize");
        return jdbcHelper.getQueryMap(sqlSource, null, new HashMap<>());
    }

    @Override
    public List<Map<String, Object>> countList(Map<String, Object> params) {
        CmdbSqlManage sqlSource = sqlManageService.getByName(params.get("queryName").toString());
        System.out.println(sqlSource.getChartSql());
        List<Map<String, Object>> queryList = jdbcHelper.getQueryList(sqlSource, null, null, null,params);
        if (null != params.get("queryType")) {
            String queryType = params.get("queryType").toString();
            if ("newTrendMonth".equals(queryType)) { // 独立业务新增设备趋势单独处理
                return SuppleUtil.packageData("MONTH",queryList);
            } else if ("newTrendDay".equals(queryType)){
                return SuppleUtil.packageData("DAY",queryList);
            } else if ("newTrendYear".equals(queryType)) {
                return SuppleUtil.packageData("YEAR",queryList);
            } else if ("code".equals(queryType)) {
                return rebuildList(queryList);
            }
        }
        return queryList;
    }

    @Override
    public Map<String, Object> countObject(Map<String, Object> params) {
        CmdbSqlManage sqlSource = sqlManageService.getByName(params.get("queryName").toString());
        System.out.println(sqlSource.getChartSql());
        if (null != params.get("queryType")) {
            String queryType = params.get("queryType").toString();
            if ("code".equals(queryType)) {
                List<Map<String, Object>> queryList = jdbcHelper.getQueryList(sqlSource, null, null, null,params);
                return rebuildPieChartList(queryList);
            }
        }
        return jdbcHelper.getQueryMap(sqlSource,null, params);
    }

    private List<Map<String, Object>> rebuildList(List<Map<String, Object>> queryList) {
        List<Map<String,Object>> retList = new ArrayList<>();
        if (queryList.isEmpty()) {
            return retList;
        }
        Map<String,Map<String,Object>> mapList = new HashMap<>();
        queryList.forEach(map -> {
            String idcType = map.get("idcType").toString();
            String businessName = map.get("businessName").toString();
            Object businessCount = map.get("businessCount");
            Map<String, Object> mapTemp = mapList.get(idcType);
            List<Object> businessNames;
            List<Object> businessCounts;
            if (null == mapTemp || mapTemp.isEmpty()) {
                mapTemp = new LinkedHashMap<>();
                mapTemp.put("idcType",idcType);
                businessNames = new ArrayList<>();
                businessCounts = new ArrayList<>();
                businessNames.add(businessName);
                businessCounts.add(businessCount);
            } else {
                businessNames = (List<Object>)mapTemp.get("businessName");
                businessCounts = (List<Object>)mapTemp.get("businessCount");
                if ("汇总".equals(businessName)) {
                    mapTemp.put("total",businessCount);
                } else {
                    businessNames.add(businessName);
                    businessCounts.add(businessCount);
                }
            }
            mapTemp.put("businessName",businessNames);
            mapTemp.put("businessCount",businessCounts);
            mapList.put(idcType,mapTemp);
        });
        retList.addAll(mapList.values());
        return retList;
    }

    /**
     * 修改独立业务子模块的数据返回类型
     * @param queryList 初始sql查询数据
     */
    private Map<String,Object> rebuildPieChartList(List<Map<String, Object>> queryList) {
        Map<String,Object> retMap = new HashMap<>();
        if (queryList.isEmpty()) {
            retMap.put("dataList",new ArrayList<>());
            retMap.put("total",0);
            return retMap;
        }
        AtomicInteger total = new AtomicInteger();
        queryList.forEach(map -> {
            Object idcCount = map.get("idcCount");
            int oneIdc = 0;
            if (null != idcCount) {
                oneIdc = Integer.parseInt(idcCount.toString());
            }
            total.addAndGet(oneIdc);
        });
        retMap.put("dataList",queryList);
        retMap.put("total",total);
        return retMap;
    }
}
