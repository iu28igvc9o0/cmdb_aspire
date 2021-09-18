package com.aspire.ums.cmdb.monthReport.service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbMonthReportService
 * Author:   hangfang
 * Date:     2020/5/9
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface ICmdbMonthReportService {


    /**
     * 计算资源-物理机总量、已建、在建、已分配、已创建、管理节点
     **/
    LinkedHashMap<String, Object> countCompResource(String month, String idcType);

    /**
     * 计算资源-创建率/配额分配率
     **/
    List<LinkedHashMap<String, Object>>  countRateCompResource(String month, String idcType);

    /**
     * 物理机创建率/分配率月度趋势
     **/
    List<LinkedHashMap<String, Object>> countSixMonthTrend(String month, String idcType, Integer monthCount);

    /**
     * 网络资源
     **/
    LinkedHashMap<String, Object> countNetworkResource(String month, String idcType);

    /**
     * 存储资源
     **/
    LinkedHashMap<String, Object> countStorageResource(String month, String idcType, String storageType);
    /**
     * 存储资源
     **/
    LinkedHashMap<String, Object> getIdcTypeStoreUseRate(String month, String idcType, String storageType);

    /**
     * 存储资源
     **/
    List<LinkedHashMap<String, Object>> countStorageSixMonthTrend(String month,Integer monthCount,String type, String idcType, String storageType);


    /**
     * 资源回收 资源清理
     **/
    List<LinkedHashMap<String, Object>> countRecycleResource(String month, String countType, String idcType, String recycleType);

    /**
     * ALERT
     * 裸金属均峰值利用率
     **/
    List<LinkedHashMap<String, Object>> countPhyAvgMaxUtz(String month, String deviceType, String idcType);



    /**
     * ALERT
     * 告警
     **/
    LinkedHashMap<String, Object> countAlert(String month, String idcType);


    /**
     * ALERT
     * 故障
     **/
    LinkedHashMap<String, Object> countTrouble(String month, String idcType);

    /**
     * ALERT
     * 故障
     **/
    LinkedHashMap<String, Object> countOrder(String month, String idcType);

}
