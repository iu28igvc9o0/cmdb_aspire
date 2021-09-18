package com.aspire.ums.cmdb.v2.index.serivce;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenStoreLowUltRequest;
import com.aspire.ums.cmdb.index.payload.ScreenStoreLowUtilization;

import java.util.Map;

/**
 * @ClassName ItCloudScreenStoreLowUtilizationService
 * @Description 云存储低利用率服务类接口
 * @Author luowenbo
 * @Date 2020/4/24 19:20
 * @Version 1.0
 */
public interface ItCloudScreenStoreLowUtilizationService {

    /*
     *  新增
     * */
    void insert(ScreenStoreLowUtilization obj);

    /*
     *  新增
     * */
    void delete(ItCloudScreenStoreLowUltRequest req);

    /*
     *  统计所有租户的存储设备利用率低于30%的业务系统数量（只要有存在设备类型的利用率低于30%，该业务系统就算低于30%）
     * */
    Map<String, Object> getLowStoreUltCount(ItCloudScreenStoreLowUltRequest req);

    /*
     *  统计所有租户的存储设备利用率低于30%的业务系统列表（只要有存在设备类型的利用率低于30%，该业务系统就算低于30%）
     * */
    Map<String, Object> getLowStoreUltListWithDept(ItCloudScreenStoreLowUltRequest req);
}
