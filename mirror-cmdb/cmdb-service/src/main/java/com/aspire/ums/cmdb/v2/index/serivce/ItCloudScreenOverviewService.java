package com.aspire.ums.cmdb.v2.index.serivce;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenOverviewRequest;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenOverviewService
 * @Description 总租户利用率大屏——服务层
 * @Author luowenbo
 * @Date 2020/4/15 10:00
 * @Version 1.0
 */
public interface ItCloudScreenOverviewService {
    /*
     *  依据资源类型，获取设备类型
     * */
    Map<String,Object> getDeviceTypeList(String moduleType,String exclude);

    /*
     *  依据租户，获取租户、业务系统总数
     * */
    Map<String,Object> getTenantAndBzNumByDep(ItCloudScreenOverviewRequest req);

    /*
     *  依据资源池，获取租户、业务系统总数
     * */
    Map<String, Object> getTenantAndBzNumByRp(ItCloudScreenOverviewRequest req);

    /*
     *  依据设备类型，获取租户资源的分配量和使用量
     * */
    Map<String, Object> getHasAndUseAltNumByDt(ItCloudScreenOverviewRequest req);

    /*
     *  依据设备类型和租户类型，获取业务系统的资源分配量和使用量
     * */
    Map<String, Object> getHasAndUseAltNumByDevtAndDept(ItCloudScreenOverviewRequest req);

    /*
     *  获取月度的Agent部署的数量
     * */
    Map<String,Object> getAgentNum(ItCloudScreenOverviewRequest req);

    /*
     *  获取业务系统Agent部署的数量
     * */
    Map<String, Object> getAgentNumWithDept(ItCloudScreenOverviewRequest req);

    /*
     *  依据设备类型，获取月度的存储资源利用率
     * */
    Map<String, Object> getStoreUtzByDt(ItCloudScreenOverviewRequest req);

    /*
     *  依据设备类型和租户部门，获取月度业务系统的存储资源利用率
     * */
    Map<String, Object> getStoreUtzByDtAndDept(ItCloudScreenOverviewRequest req);

    /*
     *  获取物理机和GPU的资源回收数量
     * */
    Map<String, Object> getRecycleNum(ItCloudScreenOverviewRequest req);

    /*
     *  获取各租户资源回收的数量
     * */
    Map<String, Object> getRecycleNumWithDevt(ItCloudScreenOverviewRequest req);

    /*
     *  获取业务系统总数
     * */
    Map<String,Object> getBizSystemTotalCount(ItCloudScreenOverviewRequest req);

    /*
     *  获取均峰值利用率低于30%的利用率（不包括云存储）
     * */
    Map<String, Object> getLowUtlCount(ItCloudScreenOverviewRequest req);

    /*
     *  获取均峰值利用率低于30%的租户业务系统数量（不包括云存储）
     * */
    Map<String, Object> getLowUltListWithDept(ItCloudScreenOverviewRequest req);

    /*
     *  各租户设备的CPU和内存的均峰值
     * */
    Map<String, Object> getCpuAndStoreMaxWithDept(ItCloudScreenOverviewRequest req);

    /*
     *  租户设备的CPU和内存均峰值的最大值
     * */
    Map<String,Object> getCpuAndStoreMax(ItCloudScreenOverviewRequest req);

    /*
     *  各租户设备的CPU和内存的均峰值
     * */
    Map<String,Object> getCpuAndStoreAvgWithDept(ItCloudScreenOverviewRequest req);

    /*
     *  租户设备的CPU和内存均峰值的最大值
     * */
    Map<String,Object> getCpuAndStoreAvg(ItCloudScreenOverviewRequest req);

    /*
     *  各租户考核项列表
     * */
    Map<String, Object> getScoreWithDept(ItCloudScreenOverviewRequest req);

    /*
     *  获取租户累计扣分项列表
     * */
    Map<String, Object> getTotalMonthCheckScoreList(ItCloudScreenOverviewRequest req);
}
