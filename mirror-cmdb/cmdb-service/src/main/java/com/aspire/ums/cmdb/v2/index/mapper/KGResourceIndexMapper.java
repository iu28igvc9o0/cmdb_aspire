package com.aspire.ums.cmdb.v2.index.mapper;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface KGResourceIndexMapper {

    /**
     *  统计各品牌设备分布
     */
    List<Map<String, Object>> deviceCountByProduceAll(Map<String, String> param);

    /**
     *  统计品牌下设备型号分布
     */
    List<Map<String, Object>> modelCountByProduce(Map<String, String> param);

    /**
     * 获取网段地址列表
     */
    List<Map<String, Object>> getAllSegmentAddress();

    /**
     *  资源使用状况分布
     */
    Map<String, Object> getDeviceStatusCount(@Param("segmentAddr") String segmentAddr,
                                             @Param("bizSystem") String bizSystem,
                                             @Param("deviceStatus") String deviceStatus,
                                             @Param("deviceType") String deviceType);

    List<Map<String, Object>> getDeviceUseCountByType(@Param("segmentAddr") String segmentAddr,
                                                   @Param("bizSystem") String bizSystem,
                                                   @Param("deviceStatus") String deviceStatus,
                                                   @Param("deviceType") String deviceType);

    /**
     *  网段用途设备总量分布
     */
    List<Map<String, Object>> deviceCountBySegmentUse(@Param("deviceType") String deviceType);

    /**
     *  服务器业务使用量占比
     */
    List<Map<String, Object>> deviceCountBySystem(@Param("deviceType") String deviceType,
                                                  @Param("systemType") String systemType,
                                                  @Param("deviceStatus") String deviceStatus);

    Map<String, Object> deviceUseByClassAndType(@Param("deviceClass") String deviceClass,
                                                @Param("deviceType") String deviceType);
}
