package com.aspire.ums.cmdb.v2.index.serivce;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.index.payload.ScreenBizSystemNotInspect;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenBizSystemService
 * @Description
 * @Author luowenbo
 * @Date 2020/3/22 16:59
 * @Version 1.0
 */
public interface ItCloudScreenBizSystemService {
    /*
     *  批量新增业务系统分配资源实体
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
     *  导出部分低于利用率的数据数据
     * */
    List<Map<String,Object>> partDataListExport(ItCloudScreenRequest req);
}
