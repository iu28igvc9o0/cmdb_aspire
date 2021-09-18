package com.aspire.ums.cmdb.v2.index.serivce;

import java.util.List;
import java.util.Map;

public interface KGResourceIndexService {

    /**
     *  统计各品牌设备分布
     */
    List<Map<String, Object>> deviceCountByProduceAll(String deviceClass);

    /**
     *  统计品牌下设备型号分布
     */
    List<Map<String, Object>> modelCountByProduce(String deviceClass,String produce);

    /**
     * 获取网段地址列表
     */
    List<Map<String, Object>> getAllSegmentAddress();

    /**
     *  资源使用状况分布
     */
    List<Map<String, Object>> getDeviceUseCount(String segmentAddr,String bizSystem);
    /**
     *  (物理机/虚拟机)资源使用状况分布
     */
    List<Map<String, Object>> getDeviceUseCountByType(String segmentAddr,String bizSystem);

    /**
     *  网段用途设备总量分布
     */
    List<Map<String, Object>> deviceCountBySegmentUse(String deviceType);
    /**
     *  服务器业务使用量占比
     */
    List<Map<String, Object>> deviceCountBySystem(String deviceType,String systemType);
    /**
     *  设备使用性(物理机)
     */
    String deviceUseByClassAndType(String deviceClass,String deviceType);
}
