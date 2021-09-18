package com.aspire.ums.cmdb.v2.index.mapper;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.index.payload.ScreenOnlineInfo;

import java.util.List;

/**
 * @ClassName ItCloudScreenOnlineInfoMapper
 * @Description 业务系统上线信息——数据层
 * @Author luowenbo
 * @Date 2020/4/23 17:03
 * @Version 1.0
 */
public interface ItCloudScreenOnlineInfoMapper {
    /*
    *  批量新增
    * */
    void batchInsert(List<ScreenOnlineInfo> list);

    /*
     *  新增
     * */
    void insert(ScreenOnlineInfo obj);

    /*
    *   依据部门信息和业务系统查询进行删除
    * */
    void delete(ItCloudScreenRequest req);
}
