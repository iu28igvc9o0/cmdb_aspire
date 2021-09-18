package com.aspire.ums.cmdb.v2.index.mapper;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenOverviewRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenOverviewMapper
 * @Description 总租户利用率大屏——数据层
 * @Author luowenbo
 * @Date 2020/4/15 9:58
 * @Version 1.0
 */
@Mapper
public interface ItCloudScreenOverviewMapper {

    /*
    *  依据资源类型，获取设备类型
    * */
    List<String> getDeviceTypeList(String moduleType);

    /*
    *  依据租户，获取租户、业务系统总数
    * */
    Map<String,Object> getTenantAndBzNumByDep(ItCloudScreenOverviewRequest req);

    /*
     *  依据资源池，获取租户、业务系统总数
     * */
    List<Map<String,Object>> getTenantAndBzNumByRp(ItCloudScreenOverviewRequest req);

    /*
    *  依据设备类型，获取租户资源的分配量和使用量
    * */
    List<Map<String,Object>> getHasAndUseAltNumByDt(ItCloudScreenOverviewRequest req);

    /*
     *  依据设备类型和租户，获取业务系统的资源分配量和使用量
     * */
    List<Map<String,Object>> getHasAndUseAltNumByDevtAndDept(ItCloudScreenOverviewRequest req);

    /*
     *  获取月度的Agent部署的数量
     * */
    List<Map<String,Object>> getAgentNum(ItCloudScreenOverviewRequest req);

    /*
     *  获取业务系统Agent部署的数量
     * */
    List<Map<String,Object>> getAgentNumWithDept(ItCloudScreenOverviewRequest req);

    /*
     *  依据设备类型，获取月度的存储资源利用率
     * */
    List<Map<String,Object>> getStoreUtzByDt(ItCloudScreenOverviewRequest req);

    /*
     *  依据设备类型和租户部门，获取月度业务系统的存储资源利用率
     * */
    List<Map<String,Object>> getStoreUtzByDtAndDept(ItCloudScreenOverviewRequest req);

    /*
     *  获取物理机和GPU的资源回收总数量
     * */
    List<Map<String,Object>> getRecycleNumWithPmAndGpu(ItCloudScreenOverviewRequest req);

    /*
     *  获取虚拟机CPU核数和内存的资源回收总数量
     * */
    List<Map<String,Object>> getRecycleNumWithVm(ItCloudScreenOverviewRequest req);

    /*
     *  获取各个租户物理机和GPU的资源回收的数量
     * */
    List<Map<String,Object>> getPmAndGpuRecycleNumWithDevt(ItCloudScreenOverviewRequest req);

    /*
     *  获取各个租户虚拟机CPU核数的资源回收的数量
     * */
    List<Map<String,Object>> getVmRecycleNumWithDevt(ItCloudScreenOverviewRequest req);

    /*
    *  获取业务系统总数
    * */
    List<Map<String,Object>> getBizSystemTotalCount(ItCloudScreenOverviewRequest req);

    /*
    *  获取均峰值利用率低于30%的数量（不包括云存储）
    * */
    List<Map<String,Object>> getLowUtlCount(ItCloudScreenOverviewRequest req);

    /*
    *  获取均峰值利用率低于30%的租户业务系统数量（不包括云存储）
    * */
    List<Map<String,Object>> getLowUltListWithDept(ItCloudScreenOverviewRequest req);

    /*
    *  各租户设备的CPU和内存的均峰值
    * */
    List<Map<String,Object>> getCpuAndStoreMaxWithDept(ItCloudScreenOverviewRequest req);

    /*
     *  租户设备的CPU和内存均峰值的最大值
     * */
    Map<String,Object> getCpuAndStoreMax(ItCloudScreenOverviewRequest req);

    /*
     *  各租户设备的CPU和内存的均值
     * */
    List<Map<String,Object>> getCpuAndStoreAvgWithDept(ItCloudScreenOverviewRequest req);

    /*
     *  租户设备的CPU和内存均值的平均值
     * */
    Map<String,Object> getCpuAndStoreAvg(ItCloudScreenOverviewRequest req);

    /*
    *  各租户考核项列表
    * */
    List<Map<String,Object>> getScoreWithDept(ItCloudScreenOverviewRequest req);

    /*
     *  获取租户累计扣分项列表
     * */
    List<Map<String,Object>> getTotalMonthCheckScoreList(ItCloudScreenOverviewRequest req);
}
