package com.aspire.ums.cmdb.v2.index.mapper;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbIndexInstMapper
 * Author:   hangfang
 * Date:     2019/9/20
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface CmdbIndexInstMapper {
    /**
     * 统计资源池下各设备类型数据
     */
    List<JSONObject> countByIdcDevCT(@Param("idcType") String idcType,
                                     @Param("deviceClass") String deviceClass);

    /**
     * 统计该资源池下各设备下项目数据
     */
    List<JSONObject> countByIdcPro(Map<String, Object> params);

    /**
     * 统计该项目下各pod池下设备数据
     */
    List<JSONObject> countByIdcDevPro(@Param("idcType") String idcType,
                                      @Param("column") String column,
                                      @Param("deviceType") String deviceType,
                                      @Param("projectName") String projectName);

    /**
     * 该资源池下所有项目
     */
    List<Map<String,String>> listDistinctPro(@Param("idcType") String idcType,
                                 @Param("column") String column,
                                 @Param("deviceType") String deviceType);


    /**
     * 各资源池下业务数量
     */
    List<JSONObject> countBizByIdc(@Param("deviceClass") String deviceClass);

    /**
     * 该一级部门下各资源池业务数量
     */
    List<JSONObject> countBizByDep1(@Param("department1") String department1,
                                    @Param("deviceClass") String deviceClass);
    /**
     * 统计该资源池下各一级部门下的业务
     */
    List<JSONObject> countBizByIdcDep1(@Param("idcType") String idcType,
                                       @Param("deviceClass") String deviceClass);

    /**
     * 统计该一级部门下各二级部门的业务
     */
    List<Map<String, Object>> countBizByIdcDep2(@Param("idcType") String idcType,
                                       @Param("department1") String department1,
                                                @Param("deviceClass") String deviceClass);
 /**
     * 各资源池一级部门下二级部门下各业务系统的设备数量
     */
    List<JSONObject> countByIdcDep2Biz(@Param("idcType") String idcType,
                                       @Param("department1") String department1,
                                       @Param("department2") String department2,
                                       @Param("deviceClass") String deviceClass);

/*
     * 统计设备趋势(日)
     * */
    List<JSONObject> deviceClassTrendWithDay();

    /*
     * 统计设备趋势图(月)
     * */
    List<JSONObject> deviceClassTrendWithMonth();

    /*
     * 统计设备趋势图(年)
     * */
    List<JSONObject> deviceClassTrendWithYear();

    /*
     *  统计各一级租户设备类型分布
     * */
    List<JSONObject> countDeviceClassByDepartment1();

    /*
     *  统计设备子类型分布
     * */
    List<JSONObject> countDeviceTypeByDeviceClass(String deviceClass);

    /*
    * 各一级租户设备量趋势(日)
    * */
    List<Map<String,Object>> countDevClsByDepWithDay();

    /*
     * 各一级租户设备量趋势(月)
     * */
    List<Map<String,Object>> countDevClsByDepWithMonth();

    /*
     * 各一级租户设备量趋势(年)
     * */
    List<Map<String,Object>> countDevClsByDepWithYear();
/**
     * 统计资源池及POD池数量
     */
    Map<String, Object> countIdcAndPod();

    /**
     * 统计各设备类型(服务器、网络、存储、安全)设备数量
     */
    Map<String, Object> countDeviceByDeviceClass();

    /**
     * 获取所有设备状态
     */
    List<Map<String,String>> listStatus();

    /**
     * 统计各资源池设备分配状态（总）
     */
    List<Map<String, Object>> countStatusAll(@Param(value = "statusList") List<String> statusList);

    /**
     * 统计各资源池设备分配状态
     */
    List<Map<String, Object>> countStatusByIdc(@Param(value = "statusList") List<String> statusList);


    /**
     * 统计各品牌设备分布（总）
     */
    List<Map<String, Object>> countByProduceAll();

    /**
     * 统计各资源池品牌设备类型分布
     */
    List<Map<String, Object>> countByProduce(@Param(value = "produce") String produce);

    /**
     * 统计资源总览
     */
    List<Map<String, Object>> countOverview();


    /**
     * 统计磁盘利用率
     */
    Map<String, Object> countDiskSize();
}
