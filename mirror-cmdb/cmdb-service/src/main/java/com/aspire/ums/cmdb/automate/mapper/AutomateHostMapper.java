package com.aspire.ums.cmdb.automate.mapper;

import com.aspire.ums.cmdb.automate.payload.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-09-10 14:45
 * @description 自动化主机模型配置入库
 */
public interface AutomateHostMapper {
    void saveHost(AutomateHostEntity hostEntity);
    void batchSaveCpuList(@Param("list") List<AutomateHostCpuEntity> list);
    void batchSaveDiskList(@Param("list") List<AutomateHostDiskEntity> list);
    void batchSaveEthList(@Param("list") List<AutomateHostEthEntity> list);
    void batchSaveServiceList(@Param("list") List<AutomateHostServiceEntity> list);
    Map<String,Object> getAutomateHostDetail(@Param("ip") String ip);
    List<Map<String,String>> getCpuList(@Param("assetInstanceId") String assetInstanceId);
    List<Map<String,String>> getDiskList(@Param("assetInstanceId") String assetInstanceId);
    List<Map<String,String>> getEthList(@Param("assetInstanceId") String assetInstanceId);
    List<Map<String,String>> getServiceList(@Param("assetInstanceId") String assetInstanceId);
    List<Map<String,String>> getAutomateModuleIds(@Param("list") List<String > list);
    void updateDelAutomate(@Param("instanceId") String instanceId);
    void delAutomateCpu(@Param("assetInstanceId") String assetInstanceId);
    void delAutomateDisk(@Param("assetInstanceId") String assetInstanceId);
    void delAutomateEth(@Param("assetInstanceId") String assetInstanceId);
    void delAutomateService(@Param("assetInstanceId") String assetInstanceId);
    void updateHostByInstanceId(Map param);
    Map<String,String> getAutomateHostInfo(Map<String,String> param);
    void batchSaveHostConfList(@Param("list") List<Map<String,Object>> list);
    List<Map<String,String>> findAutomateConfList(Map<String,Object> param);
    String getAutomateHostPartName(@Param("tableName") String tableName,@Param("partName") String partName);
    void alterAutomateHostPart(Map<String,String> partMap);
}
