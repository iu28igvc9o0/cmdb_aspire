package com.aspire.ums.cmdb.deviceStatistic.service;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.deviceStatistic.payload.DeviceStatisticRequest;
import com.aspire.ums.cmdb.deviceStatistic.payload.DeviceStatisticResp;
 


/**
 * 设备统计
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.ums.cmdb.deviceStatistic.service
 * 类名称:    DeviceStatisticService.java
 * 类描述:    设备统计业务层接口
 * 创建人:    JinSu
 * 创建时间:  2018/9/14 15:55
 * 版本:      v1.0
 */
public interface DeviceStatisticService {
	
    
    /**
     * 各类型设备的数量统计
     */
    List<DeviceStatisticResp> selectDeviceBydeviceType( DeviceStatisticRequest deviceStatisticRequest);
   
   
    /**
    * 各类型的各品牌设备数量统计
    */
    List<DeviceStatisticResp> selectDeviceBydeviceTypeDeviceMfrs( DeviceStatisticRequest deviceStatisticRequest);
    
    
    /**
     * 各资源池各类型的数量统计
     */
    List<DeviceStatisticResp> selectDeviceByidcTypeDeviceType( DeviceStatisticRequest deviceStatisticRequest);
   
    
    /**
     * 各业务系统各分类数量统计
     */
    List<DeviceStatisticResp> selectDeviceBybizSystem( DeviceStatisticRequest deviceStatisticRequest);
    
  
    /**
     * 各归属部门各分类数量统计
     */
    List<DeviceStatisticResp> selectDeviceByDepartment( DeviceStatisticRequest deviceStatisticRequest);

    /**
     *  各资源池业务统计
     * @return 模型列表
     */
    List<Map<String, Object>> selectIdcTypeStatistic();

    /**
     *  资源池下工期统计
     * @param idcType 所属资源池
     * @return 模型列表
     */
    List<Map<String, Object>> selectProjectStatistic(String idcType);

    /**
     *  指定资源池、工期下的POD池统计
     * @return 模型列表
     */
    List<Map<String, Object>> selectPodStatistic(String idcType, String projectName);

    /*
    * 资源池多选，获取对应工期统计数据
    * */
    List<Map<String, Object>> selectMultiProjectStatistic(List<String> idcTypes);
}
