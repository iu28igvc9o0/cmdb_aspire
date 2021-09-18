package com.aspire.ums.cmdb.monthReport.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbMonthReportMapper
 * Author:   hangfang
 * Date:     2020/5/9
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Mapper
public interface CmdbMonthReportMapper {
    /*计算资源*/
    LinkedHashMap<String, Object> countCompResource(@Param("month") String month,
                                          @Param("idcType") String idcType);

//    LinkedHashMap<String, Object> countDistRateCompare(@Param("month") String month,
//                                                   @Param("months") List<String> months,
//                                          @Param("idcType") String idcType);
//    LinkedHashMap<String, Object> countUseRateCompare(@Param("month") String month,
//                                                   @Param("months") List<String> months,
//                                                   @Param("idcType") String idcType);
    LinkedHashMap<String, Object> countDistRateCompare(@Param("month") String month,
                                                   @Param("idcType") String idcType);
    LinkedHashMap<String, Object> countUseRateCompare(@Param("month") String month,
                                                      @Param("idcType") String idcType);

    LinkedHashMap<String, Object> countRate(@Param("month") String month,
                                                  @Param("idcType") String idcType);
    LinkedHashMap<String, Object> countRateIdc(@Param("month") String month,
                                            @Param("idcType") String idcType);

    LinkedHashMap<String, Object> countNetworkResource(@Param("month") String month,
                                             @Param("idcType") String idcType);
/*存储资源*/
    LinkedHashMap<String, Object> countStorageResource(@Param("month") String month,
                                             @Param("idcType") String idcType,
                                             @Param("storageType") String storageType);
    LinkedHashMap<String, Object> getIdcTypeStoreUseRate(@Param("month") String month,
                                                       @Param("idcType") String idcType,
                                                       @Param("storageType") String storageType);


    List<LinkedHashMap<String, Object>> countStorageSixMonthTrend(@Param("month") String month,
                                                                  @Param("months") List<String> months,
                                                                  @Param("type") String type,
                                                                  @Param("idcType") String idcType,
                                                                  @Param("storageType") String storageType);


    List<LinkedHashMap<String, Object>> countStorageSixMonthTrendIdc(@Param("month") String month,
                                                                  @Param("months") List<String> months,
                                                                  @Param("type") String type,
                                                                  @Param("idcType") String idcType,
                                                                  @Param("storageType") String storageType);

    /*资源回收/清理*/
    List<LinkedHashMap<String, Object>> countRecycleResource(@Param("month") String month,
                                                             @Param("countType") String countType,
                                          @Param("idcType") String idcType,
                                             @Param("recycleType") String recycleType);

    /** ALERT
     * 裸金属均峰值利用率
     */
    LinkedHashMap<String, Object> countPhyAvgMaxUtz(@Param("month") String month,
                                                          @Param("deviceType") String deviceType);

    /** ALERT
     * 裸金属均峰值利用率-有资源池
     */
    LinkedHashMap<String, Object> countPhyAvgMaxUtzIdc(@Param("month") String month,
                                                          @Param("deviceType") String deviceType,
                                                          @Param("idcType") String idcType);

    /**
     * ALERT
     * 告警
     */
    LinkedHashMap<String, Object> countAlert(@Param("month") String month,
                                                          @Param("idcType") String idcType);

    /**
     * ALERT
     * 故障
     */
    LinkedHashMap<String, Object> countTrouble(@Param("month") String month,
                                             @Param("idcType") String idcType);
    /**
     * ALERT
     * 故障
     */
    LinkedHashMap<String, Object> countTroubleIdc(@Param("month") String month,
                                               @Param("idcType") String idcType);
    /**
     * ALERT
     * 工单
     */
    LinkedHashMap<String, Object> countOrder(@Param("month") String month,
                                               @Param("idcType") String idcType);

}
