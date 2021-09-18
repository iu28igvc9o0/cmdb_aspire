package com.aspire.ums.cmdb.v2.index.mapper;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.index.payload.ScreenBizSystemNotInspect;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenBizSystemMapper
 * @Description  租户利用率大屏——业务系统相关功能的原子层
 * @Author luowenbo
 * @Date 2020/3/22 15:03
 * @Version 1.0
 */
@Mapper
public interface ItCloudScreenBizSystemMapper {
    /*
     *  批量新增业务系统分配免考核资源实体
     * */
    void batchInsertBizSystemNotInpect(List<ScreenBizSystemNotInspect> list);
    /*
    *  依据条件，查询业务系统分配资源实体
    * */
    List<Map<String,Object>> getBizSystemAllocation(ItCloudScreenRequest req);
    /*
     *  依据业务系统，查询免考核资源的详细信息
     * */
    List<Map<String,Object>> getBizSystemNotInpect(ItCloudScreenRequest req);
    /*
     *  全量导出数据
     * */
    List<Map<String,Object>> allDataListExport(ItCloudScreenRequest req);
    /*
     *  导出计算类型资源低于利用率的数据
     * */
    List<Map<String,Object>> calDataListExport(ItCloudScreenRequest req);

    /*
     *  导出存储类型资源低于利用率的数据
     * */
    List<Map<String,Object>> storeDataListExport(ItCloudScreenRequest req);
}
