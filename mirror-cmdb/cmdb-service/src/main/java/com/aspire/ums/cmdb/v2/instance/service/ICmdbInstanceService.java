package com.aspire.ums.cmdb.v2.instance.service;

import java.util.List;
import java.util.Map;

import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.code.payload.CmdbSimpleCode;
import com.aspire.ums.cmdb.collectApproval.payload.CmdbCollectApproval;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbDeviceTypeByConditonCount;
import com.aspire.ums.cmdb.instance.payload.CmdbDeviceTypeCount;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.aspire.ums.cmdb.instance.payload.CmdbInstanceIpManager;
import com.aspire.ums.cmdb.instance.payload.CmdbQueryInstance;

/**
 * 描述：
 * 
 * @author
 * @date 2019-05-20 20:56:07
 */
public interface ICmdbInstanceService {
     /**
     * 获取所有实例
     * 
     * @return 返回所有实例数据
     */
    List<CmdbInstance> list();

    /**
     * 根据查询条件获取实例列表
     * 
     * @param query
     *            查询实体
     * @return
     */
    List<CmdbInstance> listByEntity(CmdbInstance query);

    /**
     * 根据主键ID 获取数据信息
     * 
     * @param entity
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    CmdbInstance get(CmdbInstance entity);

    Map<String, Object> getByParams(String moduleId, Map<String, Object> params);
    /**
     * 根据主键ID 获取数据信息
     * 
     * @param id
     *            实例信息
     * @return 返回实例信息的数据信息
     */
    List<Map<String, Object>> getById(String id);
    /**
     * 根据主键ID 获取主从表数据信息
     * 
     * @param moduleId
     *            模型ID
     * @param instanceId
     *            实例ID
     * @return 返回实例信息的数据信息
     */
    Map<String, Object> getInstanceDetail(String moduleId, String instanceId);

    /**
     * 新增实例
     *
     * @param instanceData
     *            实例数据
     * @return
     */
    String addInstance(String userName, Map<String, Object> instanceData, String operateType);

    /**
     * 新增实例
     *
     * @param instanceData
     *            实例数据
     * @return
     */
    String addInstanceNoApprove(String userName, Map<String, Object> instanceData, String operateType);

    /**
     * 新增实例
     *
     * @param instanceData
     *            实例数据
     * @return
     */
    CmdbCollectApproval handleAddApproval(String userName, Map<String, Object> instanceData, String operateType);

    /**
     * 新增实例
     *
     * @param approval
     *            实例数据
     * @return
     */
    String insert(String userName, CmdbCollectApproval approval);

    /**
     * 获取IP MANAGER列表
     * @param curValue 配置项值
     * @param codeId 配置项ID
     * @param instanceId 实例ID
     * @return
     */
    List<CmdbInstanceIpManager> handleIpManagerList(String curValue, String codeId, String instanceId);

    /**
     * 更新实例
     *
     * @param instanceData
     *            实例数据
     * @return
     */
    String updateInstance(String id, String userName, Map<String, Object> instanceData, String operateType);

    /**
     * 更新实例
     *
     * @param approvalList
     *            实例数据
     * @return
     */
    String update(String userName, List<CmdbCollectApproval> approvalList);


    /**
     * 网管同步过来的时候，修改实例
     * 
     * @param
     * @return
     */
    void updateInstance(Map<String, Object> instanceData);


    /**
     * 获取批量改审核信息
     * 
     * @param batchUpdate
     *            更新条件数据
     * @return
     */
    List<String> getBatchUpdateApprovals(String username, String moduleId, Map<String, Object> batchUpdate);

    /**
     * 批量修改实例
     * 
     * @param batchUpdate
     *            更新条件数据
     * @return
     */
    Integer batchUpdateCount(String moduleId, Map<String, Object> batchUpdate);


    /**
     * 删除实例
     * 
     * @param entity
     *            实例数据
     * @return
     */
    void delete(CmdbInstance entity);

    void delete(String userName, CmdbCollectApproval approval);

    /**
     * 网管同步资产-物理删除.
     * 
     * @param
     * @return
     */
    void deletePhysic(CmdbCollectApproval approval);

    /**
     * 根据查询条件 返回主机实例数据
     * 
     * @param params
     *            查询参数
     * @param moduleType
     *            模型类型
     * @return
     */
    Result<Map<String, Object>> getInstanceList(Map<String, Object> params, String moduleType);

    /**
     * 根据查询条件 返回主机实例数据
     *
     * @param params
     *            查询参数
     * @param moduleType
     *            模型类型
     * @return
     */
    List<Map<String, Object>> getInstanceListData(Map<String, Object> params, String moduleType);

    /**
     * 获取文件下载类型
     * @return
     */
    String getDownloadType();

    /**
     * 根据查询条件 返回主机实例数据
     * 
     * @param params
     *            查询参数
     * @return
     */
    Result<Map<String, Object>> getInstanceListForCommon(Map<String, Object> params);

    /**
     * 根据IP地址及资源池查询设备信息
     * 
     * @param param
     *            {idcType: '', ip: ''}
     * @return
     */
    List<CmdbInstance> getInstanceByIp(Map<String, Object> param);

    /**
     * 删除CI
     * 
     * @param instanceList 需要删除的CI集合
     */
    String deleteInstance(String userName, List<Map<String, Object>> instanceList, String operateType);

    String deleteInstanceNoApprove(String userName, List<Map<String, Object>> instanceList, String operateType);

    /**
     * 网管同步资产--物理删除
     * 
     * @param
     * @return
     */
    String deleteInstancePysicalNoApprove(String userName, List<Map<String, Object>> instanceList, String operateType);

//    /**
//     * 根据模型id和instanceId删除CI.
//     *
//     * @param
//     * @return
//     */
//    void deleteInstance(String moduleId, String instanceId);

    /**
     * 获取资源池树
     */
    List<Map> getIdcTree();

    /**
     * 获取设备类型树
     */
    List<Map> getDeviceClassTree();

    /**
     * 根据资源池获取部门
     */
    List<String> getDepartmentsByIDC(String idcType);

    /**
     * 根据多个资源池ID获取资源池列表
     * 
     * @param ids
     * @return
     */
    List<Map<String, String>> getIdcByIds(String ids);

    /**
     * 根据多个podID获取pod列表
     * 
     * @param ids
     * @return
     */
    List<Map<String, String>> getPodByIds(String ids);

    /**
     * 根据多个机房ID获取机房列表
     * @param ids
     * @return
     */
    List<Map<String, String>> getRoomByIds(String ids);

    /**
     * 查询服务器数量
     * 
     * @param bizSystem
     * @return
     */
    List<CmdbDeviceTypeCount> queryServiceCount(String bizSystem);
    List<CmdbDeviceTypeCount> queryServiceCountForKG();

    /**
     * 查询网络安全设备信息
     * @param cmdbQueryInstance
     * @return
     */
    List<Map<String,Object>> getNetworkAndSafetyDeivce(CmdbQueryInstance cmdbQueryInstance);

    /**
     * 根据资源池获取资源池下的所有工程期数
     * @param idcType 资源池名称
     */
    List<Map<String, String>> getProjectNameByIdcType(String idcType);
    
    List<CmdbDeviceTypeByConditonCount> queryDeviceCountByIdctype(String idcType, String deviceType, String startTime,
			String endTime);

	List<CmdbDeviceTypeByConditonCount> queryDeviceCountByBizsystem(String bizSystem, String idcType, String deviceType,
			String startTime, String endTime, String sourceType);

    Map<String, Map<String, Map<String, Integer>>> filterEmptyCiItem(String ciItem);

    /**
     *
     * @param ipmiIp IPMI地址
     * @param idcType 资源池名称
     * @return
     */
    Map<String, Object> getInstanceByIPMI(String ipmiIp, String idcType);

    /**
     * 修改资源池的监控状态
     * @param monitorPools 监控资源池
     * @param flag 是 否
     */
    void updateAllPool(List<String> monitorPools, String flag);

    /**
     * 修改Zabbix监控状态
     * @param instanceId 实例ID
     * @param flag 是 否
     */
    void updateZbxMonitorStatus(String instanceId, String flag);

    /**
     * 修改Promethus监控状态
     * @param instanceId 实例ID
     * @param flag 是 否
     */
    void updateProMonitorStatus(String instanceId, String flag);

    /**
     * 验证码表
     * @param code 码表
     * @param codeValue 码表值
     */
    void validCode(CmdbCode code, String codeValue);

    /**
     * 获取CI详细信息
     * 
     * @param params
     *            入参数据
     * @return
     */
    Map<String, Object> queryInstanceDetail(Map<String, Object> params, String moduleType);

    /**
     * 获取CI详细信息
     * @param params 查询设置 包含:
     * {
     * 	"params": [{  // 查询参数设置
     * 		"operator": "操作符",
     * 		"filed": "字段名称",
     * 		"value": "字段值",
     * 	    "filed_type": "字段控件类型"
     * 	}],
     * 	"query_module_id": "查询的模型ID",
     * 	"result": [] //返回结果
     * 	"index": "", //索引名称
     * 	"type": "",  //类型
     * }
     * @return 具体实例数据
     */
    Map<String, Object> getInstanceByPrimaryKey(Map<String, Object> params);

    /**
     * 根据资源池查询设备详细信息
     * @param params
     * @return
     */
    Map<String, Object> queryDeviceByIdcTypeAndIP(Map<String, Object> params);

    /**
     * 获取模型的码表信息
     * @param moduleId 模型ID
     * @param moduleType 模型类型
     * @return
     */
    List<CmdbSimpleCode> getInstanceHeader(String moduleId, String moduleType);
    /**
     * 导出实例
     * @param params 查询参数
     * @param moduleType 导出数据类型
     * @return
     */
    List<Map<String, Object>> exportInstanceList(Map<String, Object> params, String moduleType);
    /*
     * 查询CI基本属性信息列表
     * @param param 查询参数
     * */
    List<Map<String,Object>> getInstanceBaseInfo(Map<String, Object> param);

    /**
     * 获取所有的设备列表, 将所有IP地址作为一条主机记录
     * @param params 查询条件信息
     */
    Result<Map<String, Object>> getAllIPInstance(Map<String, Object> params);

    /*
     * 根据设备分类获取
     */
    List<Map<String, Object>> deviceCountByDeviceClass(String deviceClass);
    /*
     * 根据设备分类获取
     */
    Map<String, Object> deviceCountByDeviceType(String deviceClass,String deviceType);
    /**
     * 获取块存储SIZE
     * */
    Object getBlockSize();
  /**
     * 统计各网段ip状态数量
     * @param segmentTableName 查询参数
     * @param ipTableName 查询参数
     * */
    void countSegmentIp(String segmentTableName, String segmentAddress, String ipTableName, String ipSegment);

    /**
     * 统计各网段ip状态数量
     * 
     * @param segmentTableName
     *            查询参数
     * @param ipTableName
     *            查询参数
     */
    void countSegmentIp4Segment(String segmentTableName, String segmentAddress, String ipTableName, String ipSegment,
            String segmentAddressValue);

    /**
     * 同步cmdb设备业务模型和业务子模块到ip详情
     * @param ipTableName 查询参数
     * */
    void syncIpBussiness(String ipCode, String segmentTableName, String segmentAddress, String ipTableName, String ipSegment);

    /**
     * 处理模型数据
     * @param instanceId 实例ID
     * @param moduleId 模型ID
     * @param instanceData 实例数据
     * @return
     */
    Map<String, Object> handleModuleData(String instanceId, String moduleId, Map<String, Object> instanceData);

    Integer listV3Count(Map<String, Object> params, String moduleType);
}
