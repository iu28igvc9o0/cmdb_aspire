package com.aspire.ums.cmdb.v2.index.serivce;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CountInstCdtService
 * Author:   hangfang
 * Date:     2019/9/20
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface CountInstCdtService {

    /**
     * 统计资源池下各设备类型数据
     */
    List<Map<String, Object>> countByIdcDevCT(String idcType, String queryType);

    /**
     * 统计该资源池下各项目下设备数据
     */
    List<Map<String, Object>> countByIdcPro(String idcType, String deviceType, String queryType);

    /**
     * 统计该项目下各pod池下设备数据
     */
    List<Map<String, Object>> countByIdcDevPro(String idcType,
                                      String deviceType,
                                     String projectName);
    /**
     * 各资源池下业务数量
     */
    List<Map<String, Object>> countBizByIdc(String queryType);

    /**
     * 该一级部门下各资源池业务数量
     */
    List<Map<String, Object>> countBizByDep1(String department1,String queryType);

    /**
     * 统计该资源池下各一级部门下的业务
     */
    List<Map<String, Object>> countBizByIdcDep1(String idcType,String queryType, Integer top);

    /**
     * 统计该一级部门下各二级部门的业务
     */
    List<Map<String, Object>> countBizByIdcDep2(String idcType,
                                      String department1,
                                                String queryType);

    /**
     * 统计设备趋势(日)
     * */
    List<Map<String, Object>> deviceClassTrendWithDay();

    /**
     * 统计设备趋势图(月)
     * */
    List<Map<String, Object>> deviceClassTrendWithMonth();

    /**
     * 统计设备趋势图(年)
     * */
    List<Map<String, Object>> deviceClassTrendWithYear();

    /**
     *  统计各一级租户设备类型分布
     * */
    List<Map<String, Object>> countDeviceClassByDepartment1();

    /**
     *  统计设备子类型分布
     * */
    List<Map<String, Object>> countDeviceTypeByDeviceClass(String deviceClass);

    /**
     * 各一级租户设备量趋势(日)
     * */
    List<Map<String,Object>> countDevClsByDepWithDay();

    /**
     * 各一级租户设备量趋势(月)
     * */
    List<Map<String,Object>> countDevClsByDepWithMonth();

    /**
     * 各一级租户设备量趋势(年)
     * */
    List<Map<String,Object>> countDevClsByDepWithYear();
    
    /**
     * 各资源池一级部门下二级部门下各业务系统的设备数量
     */
    List<Map<String,Object>> countByIdcDep2Biz(String idcType,
                                       String department1,
                                       String department2,
                                       String queryType);

    /**
     * 统计资源池及POD池数量
     */
    Map<String, Object> countIdcAndPod();

    /**
     * 统计各设备类型(服务器、网络、存储、安全)设备数量
     */
    Map<String, Object> countDeviceByDeviceClass();


    /**
     * 统计各资源池设备分配状态（总）
     */
    List<Map<String, Object>> countStatusAll();

    /**
     * 统计各资源池设备分配状态
     */
    List<Map<String, Object>> countStatusByIdc();
    /**
     * 获取所有设备状态
     */
    List<Map<String,String>> listStatus();

    /**
     * 统计各品牌设备分布（总）
     */
    List<Map<String, Object>> countByProduceAll();

    /**
     * 统计各资源池品牌设备类型分布
     */
    List<Map<String, Object>> countByProduce( String produce);

    /**
     * 统计资源总览
     */
    List<Map<String, Object>> countOverview();

    /**
     * 统计资源总览
     */
    List<Map<String, Object>> queryOverview();

    /**
     * 统计磁盘利用率
     */
    Map<String, Object> countDiskSize();

    List<Map<String, Object>> countList(Map<String, Object> params);
    Map<String, Object> countObject(Map<String, Object> params);

}
