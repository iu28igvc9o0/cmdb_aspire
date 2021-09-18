package com.aspire.mirror.zabbixintegrate.daoAlert;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.zabbixintegrate.daoAlert.po.AlertModelField;

@Mapper
public interface AlertModelFieldMapper {

    List<AlertModelField> getAlertFieldListByTableName(@Param("tableName") String tableName);
    
    //根据proxy_name查询资源池
    List<Map<String,String>> getIdcTypes(List<String> list);
    
    List<Map<String,Object>> getAlertScanComparisionDetailByIpAndPools(List<Map<String, String>> request);

    void insertScanComparision(List<Map<String,Object>> request);

    void batchUpdate(List<Map<String,Object>> request);
    
}
