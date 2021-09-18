package com.aspire.ums.cmdb.maintenance.mapper;

import com.aspire.ums.cmdb.maintenance.payload.Hardware;
import com.aspire.ums.cmdb.maintenance.payload.HardwareRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface HardWareMapper {

    void insert(@RequestBody Hardware entity);

    void batchInsert(List<Hardware> list);

    /**
     * 根据设备序列号和出保时间查询项目及是否管理设备
     * 如果已关联则返回id 否则为空
     * @param deviceSerialNumber
     * @param warrantyDate
     * @return
     */
    String queryIsExist(@Param("deviceSerialNumber") String deviceSerialNumber,
                     @Param("warrantyDate") String warrantyDate);

    /**
     * 根据projectInstatnceId查询硬件维保管理表是否存在
     * 如果存在则放回id 否则为空
     * @param projectInstanceId
     * @return
     */
    String queryIdByProjectInstanceId(@Param("projectInstanceId") String projectInstanceId);

    /**
     * 修改硬件维保
     * @param entity 硬件维保
     */
    int updateHardware(@RequestBody Hardware entity);


    /**
     * 批量修改硬件维保
     * @param hashmap 硬件维保
     */
    int batchUpdateHardware(Map<String, Object> hashmap);

    /**
     * 查询分页数量
     * @param
     */
    int getHardwareCount(@RequestBody HardwareRequest request);

    /**
     * 查询分页数据
     * @param
     */
    List<Hardware> getHardwareByPage(@RequestBody HardwareRequest request);


    /**
     * 导出数据
     * @param
     */
    List<Map<String, Object>> queryExportData(HardwareRequest queryMap);

    /**
     * 根据项目ID 查询硬件维保列表
     * @param projectId
     * @return 返回硬件维保List
     */
    List<LinkedHashMap> queryByProjectId(@Param("projectId") String projectId);

    /*
    *  根据项目名称 和 序列号 来获取信息
    * */
    Map<String,Object> queryInfoByNameAndDeviceSn(@Param("projectName") String projectName,@Param("deviceSn")String deviceSn);
}
