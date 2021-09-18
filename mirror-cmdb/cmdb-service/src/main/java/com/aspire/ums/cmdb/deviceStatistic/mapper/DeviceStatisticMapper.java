package com.aspire.ums.cmdb.deviceStatistic.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 设备实例
 * <p>
 * 项目名称:  mirror平台
 * 包:       com.aspire.ums.cmdb.deviceStatistic.mapper
 * 类名称:    DeviceInstanceMapper.java
 * 类描述:    设备实例
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:16
 * 版本:      v1.0
 */
@Mapper
public interface DeviceStatisticMapper {

    /**
     * 各类型设备的数量统计
     * @param   
     */
    List<Map<String, Object>> selectDeviceBydeviceType(Map<String, Object> hashMap);

    /**
     * 各类型的各品牌设备数量统计
     * @param   
     */
    List<Map<String, Object>> selectDeviceBydeviceTypeDeviceMfrs(Map<String, Object> hashMap);

    /**
     * 各资源池各类型的数量统计
     * @param   
     */
    List<Map<String, Object>> selectDeviceByidcTypeDeviceType(Map<String, Object> hashMap);

    /**
     * 各业务系统各分类数量统计
     * @param   
     */
    List<Map<String, Object>> selectDeviceBybizSystem(Map<String, Object> hashMap);

    /**
     * 各归属部门各分类数量统计
     * @param   
     */
    List<Map<String, Object>> selectDeviceByDepartment(Map<String, Object> hashMap);

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
    List<Map<String, Object>> selectProjectStatistic(@Param("idcType") String idcType);

    /**
     *  指定资源池、工期下的POD池统计
     * @return 模型列表
     */
    List<Map<String, Object>> selectPodStatistic(@Param("idcType") String idcType,
                                                 @Param("projectName") String projectName);

    List<Map<String, Object>> selectMultiProjectStatistic(List<String> idcTypes);
}
