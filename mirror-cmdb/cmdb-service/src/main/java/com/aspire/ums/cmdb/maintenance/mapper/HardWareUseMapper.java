package com.aspire.ums.cmdb.maintenance.mapper;

import com.aspire.ums.cmdb.maintenance.payload.HardwareUse;
import com.aspire.ums.cmdb.maintenance.payload.HardwareUseRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 类名称: HardWareUseMapper
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/7/31 9:57
 * 版本: v1.0
 */
@Mapper
public interface HardWareUseMapper {

    /**
     * 新增
     * @param entity
     */
    void insertHardWareUse(HardwareUse entity);

    /**
     * 批量新增
     * @param list
     */
    void batchInsertHardWareUse(List<HardwareUse> list);

    /**
     * 修改硬件维保使用
     * @param entity 硬件维保使用
     */
    int updateHardWareUse(HardwareUse entity);

    /**
     * 批量修改硬件维保使用
     * @param hashmap 硬件维保使用
     */
    int batchUpdateHardWareUse(Map<String, Object> hashmap);

    /**
     * 查询分页数量
     * @param
     */
    int getHardwareUseCount(HardwareUseRequest request);

    /**
     * 查询分页数据
     * @param
     */
    List<HardwareUse> getHardwareUseByPage(HardwareUseRequest request);

    /**
     * 导出数据
     * @param
     */
    List<LinkedHashMap> queryExportData(Map<String,Object> sendRequest);

    /**
     * 查询可用硬件维保数据
     * @return
     */
    List<Map<String,String>> getHardwareTableList();

    /**
     * 批量删除
     * @param id 多个ID逗号隔开
     */
    void delete(List<String> id);

    /**
     * 根据项目ID 查询硬件维保使用列表
     * @param projectId
     * @return 返回硬件维保List
     */
    List<LinkedHashMap> queryByProjectId(@Param("projectId") String projectId);

    /**
     * 根据项目绑定设备ID 查询硬件维保使用列表
     * @param projectInstanceId
     * @return 返回硬件维保使用记录List
     */
    List<HardwareUse> getHardWareUseByProjectInstanceId(@Param("projectInstanceId") String projectInstanceId);
}
