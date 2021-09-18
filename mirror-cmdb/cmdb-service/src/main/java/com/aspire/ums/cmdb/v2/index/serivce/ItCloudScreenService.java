package com.aspire.ums.cmdb.v2.index.serivce;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.index.payload.ScreenAvgUtilization;
import com.aspire.ums.cmdb.index.payload.ScreenMaxUtilization;
import com.aspire.ums.cmdb.index.payload.ScreenResourceAllocation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenService
 * @Description IT云租户大屏——服务类接口
 * @Author luowenbo
 * @Date 2020/2/26 21:43
 * @Version 1.0
 */
public interface ItCloudScreenService {
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
    Map<String,Object> getResourceAllocateList(ItCloudScreenRequest req);

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
     *  获取月份对比的均峰值利用率，这个月与上个月的对比
     * */
    Map<String,Object> getMonthMaxUtilization(ItCloudScreenRequest req);

    /*
     *  获取月份对比的平均值利用率，这个月与上个月的对比
     * */
    Map<String,Object> getMonthAvgUtilization(ItCloudScreenRequest req);

    /*
     *  获取业务系统的数量总数
     * */
    Map<String,String> getBizSystemCount(ItCloudScreenRequest req);

    /*
    *  获取物理机CPU和内存都低于30%的业务系统数量
    * */
    Map<String,String> getPmBizSystemCount(ItCloudScreenRequest req);

    /*
     *  获取低于30%的业务系统列表（区分资源池）
     * */
    List<Map<String,Object>> getBizSystemListWithIdcType(ItCloudScreenRequest req);


    /*
     *  获取虚拟机CPU或者内存低于30%的业务系统数量
     * */
    Map<String,String> getVmBizSystemCount(ItCloudScreenRequest req);

    /*
     *  获取云存储利用率低于40%的业务系统数量（不区分资源池）
     * */
    Map<String,String> getStoreBizSystemCount(ItCloudScreenRequest req);

    /*
     *  获取云存储利用率低于40%的业务系统列表（区分资源池）
     * */
    List<Map<String,Object>> getStoreBizSystemListWithIdcType(ItCloudScreenRequest req);

    /*
     *  获取资源均峰值利用率的业务系统数量
     * */
    Map<String,Object> judgeStatus(ItCloudScreenRequest req);

    /*
     *  依据(业务系统、POD池、资源池)的唯一性，进行删除
     * */
    void deleteOldData(ItCloudScreenRequest req);

    /*
     *  更新扣分项的临时表，按照月份，查询CPU均峰值的所有数据
     * */
    void updateCpuMaxList(String monthlyTime);

    /*
     *  某业务系统下，具体设备的相关信息
     * */
    LinkedHashMap<String, Object> getSpecificDeviceByBz(ItCloudScreenRequest req);
}
