package com.aspire.ums.cmdb.v2.index.mapper;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.index.payload.ScreenAvgUtilization;
import com.aspire.ums.cmdb.index.payload.ScreenMaxUtilization;
import com.aspire.ums.cmdb.index.payload.ScreenResourceAllocation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ITCloudScreenMapper
 * @Description IT云租户大屏——原子层
 * @Author luowenbo
 * @Date 2020/2/26 20:59
 * @Version 1.0
 */
@Mapper
public interface ItCloudScreenMapper{
    /*
    *  获取系统标题列表
    * */
    List<Map<String,String>> getSystemTitleList();

    /*
     *  新增资源分配总量
     * */
    void insertResourceAllocate(ScreenResourceAllocation obj);

    /*
     *  批量新增资源分配总量
     * */
    void insertBatchResourceAllocate(List<ScreenResourceAllocation> obj);

    /*
     *  批量新增均峰值利用率实体
     * */
    void insertBatchMaxUtilization(List<ScreenMaxUtilization> obj);

    /*
     *  批量新增平均值利用率实体
     * */
    void insertBatchAvgUtilization(List<ScreenAvgUtilization> obj);

    /*
     *  获取资源分配总量 (计算资源 || 分配资源)
     * */
    List<Map<String,Object>> getResourceAllocateList(ItCloudScreenRequest req);

    /*
    *  获取某个月份的CPU和内存的均峰值
    * */
    List<Map<String,Object>> getMaxUtilizationByMonth(ItCloudScreenRequest req);

    /*
     *  获取业务系统的CPU和内存的均峰值
     * */
    List<Map<String,Object>> getMaxUtilizationByBizSystem(ItCloudScreenRequest req);

    /*
     *  获取某个月份的CPU和内存的平均值
     * */
    List<Map<String,Object>> getAvgUtilizationByMonth(ItCloudScreenRequest req);

    /*
     *  获取业务系统的CPU和内存的平均值
     * */
    List<Map<String,Object>> getAvgUtilizationByBizSystem(ItCloudScreenRequest req);

    /*
    *  月份资源均峰值利用率
    * */
    Map<String,Object> getMonthMaxUtilization(ItCloudScreenRequest req);

    /*
     *  月份资源平均利用率
     * */
    Map<String,Object> getMonthAvgUtilization(ItCloudScreenRequest req);

    /*
    *  获取业务系统的数量总数
    * */
    Integer getBizSystemCount(ItCloudScreenRequest req);

    /*
    *  获取物理机CPU和内存都低于30%的业务系统 数量（不区分资源池）
    * */
    Integer getPmBizSystemCountNotIdcType(ItCloudScreenRequest req);

    /*
    *  获取物理机CPU和内存都低于30%的业务系统 数量（区分资源池）
    * */
    Integer getPmBizSystemCountWithIdcType(ItCloudScreenRequest req);

    /*
     *  获取CPU和内存都低于30%的业务系统 列表（区分资源池）
     * */
    List<Map<String,Object>> getBizSystemListWithIdcType(ItCloudScreenRequest req);

    /*
     *  获取虚拟机CPU或者内存都低于30%的业务系统 数量（不区分资源池）
     * */
    Integer getVmBizSystemCountNotIdcType(ItCloudScreenRequest req);

    /*
     *  获取虚拟机CPU或者内存低于30%的业务系统 数量（区分资源池）
     * */
    Integer getVmBizSystemCountWithIdcType(ItCloudScreenRequest req);

    /*
     *  获取云存储利用率低于40%的业务系统 数量（不区分资源池）
     * */
    Integer getStoreBizSystemCountNotIdcType(ItCloudScreenRequest req);

    /*
     *  获取云存储利用率低于40%的业务系统 数量（区分资源池）
     * */
    Integer getStoreBizSystemCountWithIdcType(ItCloudScreenRequest req);

    /*
     *  获取云存储利用率低于40%的业务系统 列表（区分资源池）
     * */
    List<Map<String,Object>> getStoreBizSystemListWithIdcType(ItCloudScreenRequest req);

    /*
    *  判断租户下均峰值的CPU和内存的数据是否有采集
    * */
    Integer getMaxCpuAndStoreCount(ItCloudScreenRequest req);

    /*
     *  判断租户下平均值的CPU和内存的数据是否有采集
     * */
    Integer getAvgCpuAndStoreCount(ItCloudScreenRequest req);

    /*
     *  判断租户下资源量是否有分配资源
     * */
    Integer getResourceAllocationCount(ItCloudScreenRequest req);

    /*
    *  依据(业务系统、POD池、资源池)的唯一性，进行删除
    * */
    void deleteOldData(ItCloudScreenRequest req);

    /*
     *  更新扣分项的临时表，按照月份，查询CPU均峰值的所有数据(IT公司)
     * */
    List<Map<String,Object>> getCpuMaxListWithIT(@Param("monthlyTime") String monthlyTime);

    /*
     *  更新扣分项的临时表，按照月份，查询CPU均峰值的所有数据(非IT公司)
     * */
    List<Map<String,Object>> getCpuMaxListWithNotIT(@Param("monthlyTime") String monthlyTime);

    /*
    *  某业务系统下，具体设备的相关信息
    * */
    List<LinkedHashMap<String,Object>> getSpecificDeviceByBz(@Param("bizSystem") String bizSystem,
                                                             @Param("deviceType") String deviceType,
                                                             @Param("idcList") List<String> idcList,
                                                             @Param("pageNo") Integer pageNo,
                                                             @Param("pageSize") Integer pageSize);
    /*
     *  某业务系统下，具体设备数量
     * */
    int getSpecificDeviceCountByBz(@Param("bizSystem") String bizSystem,
                                   @Param("deviceType") String deviceType,
                                   @Param("idcList") List<String> idcList);
}
