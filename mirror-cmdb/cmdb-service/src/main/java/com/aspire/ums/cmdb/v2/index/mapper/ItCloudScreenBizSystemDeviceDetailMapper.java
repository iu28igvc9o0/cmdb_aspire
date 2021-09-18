package com.aspire.ums.cmdb.v2.index.mapper;

import com.aspire.ums.cmdb.index.payload.ItCloudScreenBizSystemDeviceDetailRequest;
import com.aspire.ums.cmdb.index.payload.ScreenBizSystemDeviceDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenBizSystemDeviceDetailMapper
 * @Description 租户大屏业务系统具体设备数据层
 * @Author luowenbo
 * @Date 2020/5/13 13:42
 * @Version 1.0
 */
@Mapper
public interface ItCloudScreenBizSystemDeviceDetailMapper {

    /*
    *  批量新增
    * */
    void batchInsert(List<ScreenBizSystemDeviceDetail> list);

    /*
    *  删除
    * */
    void delete(ItCloudScreenBizSystemDeviceDetailRequest req);

    /*
    *  查询
    * */
    List<ScreenBizSystemDeviceDetail> list(ItCloudScreenBizSystemDeviceDetailRequest req);

    /*
    *  查询所有物理机和虚拟机的业务系统（设备类型+业务系统）唯一
    * */
    List<Map<String,String>> getDevTypeAndBizSystemList(String monthlyTime);
}
