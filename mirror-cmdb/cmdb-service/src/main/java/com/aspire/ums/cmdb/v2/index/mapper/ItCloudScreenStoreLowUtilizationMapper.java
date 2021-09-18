package com.aspire.ums.cmdb.v2.index.mapper;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenStoreLowUltRequest;
import com.aspire.ums.cmdb.index.payload.ScreenStoreLowUtilization;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenStoreLowUtilizationMapper
 * @Description 数据层接口
 * @Author luowenbo
 * @Date 2020/4/23 10:04
 * @Version 1.0
 */
@Mapper
public interface ItCloudScreenStoreLowUtilizationMapper {

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
    List<Map<String,Object>> getLowStoreUltCount(ItCloudScreenStoreLowUltRequest req);

    /*
     *  统计所有租户的存储设备利用率低于30%的业务系统列表（只要有存在设备类型的利用率低于30%，该业务系统就算低于30%）
     * */
    List<Map<String,Object>> getLowStoreUltListWithDept(ItCloudScreenStoreLowUltRequest req);
}
