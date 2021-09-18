package com.aspire.ums.cmdb.v2.instance.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.ums.cmdb.instance.payload.CmdbDeviceTypeByConditonCount;
import com.aspire.ums.cmdb.instance.payload.CmdbDeviceTypeCount;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.aspire.ums.cmdb.instance.payload.CmdbQueryInstance;
import com.aspire.ums.cmdb.instance.payload.CmdbUpdateInstance;

/**
* 描述：
* @author
* @date 2019-05-20 20:56:07
*/
@Mapper
public interface CmdbInstanceMapper {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbInstance> list();

    /**
     * 获取业务线下设备数量
     * @return 返回所有实例数据
     */
    List<Map<String, Object>> countByBiz();

    /**
     * 获取所有实例
     * @param entity 实例信息
     * @return 返回所有实例数据
     */
    List<CmdbInstance> listByEntity(CmdbInstance entity);

    /**
     * 根据主键ID 获取数据信息
     * @param entity 实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbInstance get(CmdbInstance entity);
    /**
     * 根据ip和资源池 获取数据信息
     * @param ip 实例信息
     * @return 返回实例信息的数据信息
     */
    List<Map<String, Object>> getByIdcTypeAndIP(@Param("ip")String ip, @Param("idcType") String idcType);

    /**
     * 根据主键ID 获取数据信息
     * @param ip 实例信息
     * @return 返回实例信息的数据信息
     */
    List<Map<String, Object>> queryDeviceByIdcTypeAndIP(@Param("ip")String ip, @Param("idcType") String idcType);

    /**
     * 根据主键ID 获取数据信息
     * @param id 实例信息
     * @return 返回实例信息的数据信息
     */
    List<Map<String, Object>> getByInsId(@Param("id") String id);


    String queryModuleIdByTableNameAndId(@Param("tableName") String tableName, @Param("id") String id);

/**
     * 根据主键ID 获取主从表数据信息
     * @param id 实例信息
     * @return 返回实例信息的数据信息
     */
    Map<String, Object> getFullInstance(@Param("id") String id,
                                        @Param("tableName") String tableName);

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbInstance entity);

    /**
     * 修改实例
     * @param tableName 实例数据
     * @return
     */
    void update(@Param("tableName") String tableName,
                @Param("instanceId") String instanceId,
                @Param("dataMap") List<Map<String, String>> dataMap);

    /**
     * 删除实例
     * @param entity 实例数据
     * @return
     */
    void delete(CmdbInstance entity);

    /**
     * 根据查询条件 返回主机实例数据
     * @param params
     * @return
     */
    List<Map<String, Object>> getInstanceList(Map<String, Object> params);

    /**
     * 根据查询条件 返回主机实例数量
     * @param params
     * @return
     */
    Integer getInstanceListCount(Map<String, Object> params);

    /**
     * 修改CI cmdb_instance表数据
     * @param instanceId CI实例ID
     * @param instanceTableList 需要修改的列与修改值
     */
    void updatePrimaryTable(@Param("instanceId") String instanceId,
                            @Param("filedList") List<CmdbUpdateInstance.CmdbInstancePrimaryTable> instanceTableList);

    /**
     * 修改CI 附属表数据
     * @param instanceId CI实例ID
     * @param moduleCode 模型编码
     * @param otherTableList 需要修改的列与修改值
     */
    void updateOtherTable(@Param("instanceId") String instanceId,
                          @Param("moduleCode") String moduleCode,
                          @Param("filedList") List<CmdbUpdateInstance.CmdbInstanceOtherTable> otherTableList);

    /**
     * 修改CI cmdb_instance表数据
     * @param primaryTableList 需要修改的列与修改值
     */
    void insertPrimaryTable(@Param("filedList") List<CmdbUpdateInstance.CmdbInstancePrimaryTable> primaryTableList);

    /**
     * 修改CI 附属表数据
     * @param moduleCode 模型编码
     * @param otherTableList 需要修改的列与修改值
     */
    void insertOtherTable(@Param("moduleCode") String moduleCode,
                          @Param("filedList") List<CmdbUpdateInstance.CmdbInstanceOtherTable> otherTableList);

    /**
     * 根据IP地址及资源池查询设备信息
     * @param param {idcType: '', ip: ''}
     * @return
     */
    List<CmdbInstance> getInstanceByIp(Map<String, Object> param);

    /**
     * 根据资源池获取部门
     */
    List<String> getDepartmentsByIDC(@Param("idcType") String idcType);

    /**
     * 根据多个资源池ID获取资源池列表
     * @param id
     * @return
     */
    Map<String, String> getIdcById(@Param("id") String id);

    /**
     * 根据多个podID获取pod列表
     * @param id
     * @return
     */
    Map<String, String> getPodById(@Param("id") String id);

    /**
     * 根据多个机房ID获取机房列表
     * @param id
     * @return
     */
    Map<String, String> getRoomById(@Param("id") String id);

    /**
     * 按设备分类查询服务器数量
     * @param bizSystem
     * @return
     */
    List<CmdbDeviceTypeCount> queryServiceCount(@Param("bizSystemList") List<String> bizSystemList);
    List<CmdbDeviceTypeCount> queryServiceCountForKG();

    List<Map<String,Object>> getNetworkAndSafetyDeivce(CmdbQueryInstance cmdbQueryInstance);

    /**
     * 根据资源池获取资源池下的所有工程期数
     * @param idcType 资源池名称
     */
    List<Map<String, String>> getProjectNameByIdcType(@Param("idcType") String idcType);
    
    List<CmdbDeviceTypeByConditonCount> queryDeviceCountByIdctype(@Param("idcType")String idcType
    		,@Param("deviceType")String deviceType,@Param("startTime")String startTime
    		,@Param("endTime")String endTime);
    
    List<CmdbDeviceTypeByConditonCount> queryDeviceCountByBizsystem(@Param("bizSystemList") List<String> bizSystemList,
    		@Param("idcType")String idcType
    		,@Param("deviceType")String deviceType,@Param("startTime")String startTime
    		,@Param("endTime")String endTime,@Param("sourceType")String sourceType);

    /**
     * 查询指定字段为空的设备数量
     * @param moduleTableName 从表名称
     * @param filedCode 字段编码
     * @return
     */
    int queryEmptyCiItemCount(@Param("moduleTableName") String moduleTableName, @Param("filedCode") String filedCode,
                              @Param("idcType") String idcType, @Param("deviceType") String deviceType);


    /**
     * 修改资源池的监控状态
     * @param monitorPools 监控资源池
     * @param flag 是 否
     */
    void updateAllPool(@Param("pools")List<String> monitorPools,@Param("status") String flag);

    /**
     * 修改Zabbix监控状态
     * @param instanceId 实例ID
     * @param flag 是 否
     */
    void updateZbxMonitorStatus(@Param("id")String instanceId,@Param("status") String flag);

    /**
     * 修改Promethus监控状态
     * @param instanceId 实例ID
     * @param flag 是 否
     */
    void updateProMonitorStatus(@Param("id")String instanceId,@Param("status") String flag);

    /**
     * 查询CI基本属性信息列表
     * @param param 查询参数
     * */
    List<Map<String,Object>> getInstanceBaseInfo(Map<String, Object> param);

    /**
     * 获取所有的设备数量, 将所有IP地址作为一条主机记录
     * @param params 查询条件信息
     */
    int getAllIPInstanceCount(Map<String, Object> params);

    /**

     * 获取所有的设备列表, 将所有IP地址作为一条主机记录
     * @param params 查询条件信息
     */
    List<Map<String, Object>> getAllIPInstance(Map<String, Object> params);

    /*
     * 根据设备分类获取
     */
    List<Map<String, Object>> deviceCountByDeviceClass(@Param("deviceClass") String deviceClass);

    Map<String, Object> deviceCountByDeviceType(@Param("deviceClass") String deviceClass,
                                                       @Param("deviceType") String deviceType);

    /**
     * 获取块存储SIZE
     * */
    Map<String,Object> getBlockSize();

   /**
     * 统计各网段ip状态数量
     * @param segmentTableName 查询参数
     * @param ipTableName 查询参数
     * */
    void countSegmentIp(@Param("segmentTableName") String segmentTableName,
                        @Param("segmentAddress") String segmentAddress,
                        @Param("ipTableName") String ipTableName,
                        @Param("ipSegment") String ipSegment);

    /**
     * 统计各网段ip状态数量
     * 
     * @param segmentTableName
     *            查询参数
     * @param ipTableName
     *            查询参数
     */
    void countSegmentIp4Segment(@Param("segmentTableName") String segmentTableName, @Param("segmentAddress") String segmentAddress,
            @Param("ipTableName") String ipTableName, @Param("ipSegment") String ipSegment,
            @Param("segmentAddressValue") String segmentAddressValue);

    /**
     * 同步cmdb设备业务模型和业务子模块到ip详情
     * @param ipTableName 查询参数
     * */
    void syncIpBussiness(@Param("ipCode") String ipCode,
                         @Param("segmentTableName") String segmentTableName,
                         @Param("segmentAddress")String segmentAddress,
                         @Param("ipTableName") String ipTableName,
                         @Param("ipSegment") String ipSegment);

}
