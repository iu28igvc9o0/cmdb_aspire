package com.aspire.mirror.zabbixintegrate.daoCmdb;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.zabbixintegrate.bean.HwSyncLog;
import com.aspire.mirror.zabbixintegrate.bean.IndicatorInfoHw;
import com.aspire.mirror.zabbixintegrate.bean.InstanceHw;

/**
 * @author baiwp
 * @title: CmdbInstanceDao
 * @projectName zabbix-integrate
 * @description: TODO
 * @date 2019/7/2416:02
 */
@Mapper
public interface AlertRestfulDao {
	HwSyncLog getHwSyncLog(@Param("idcTypeTag")String idcTypeTag,@Param("configType")String configType);
    //插入标准化日志
    int insertHwSyncLog(HwSyncLog log);
    
    
    int insertIndicatorInfoHw(List<IndicatorInfoHw>  list);
    int insertInstanceHw(List<InstanceHw>  list);
    int getInstanceHwPageListCount(Map<String,Object> params);
    
    List<InstanceHw> getInstanceHwPageList(Map<String,Object> params);
    List<IndicatorInfoHw> getIndicatorList(Map<String,Object> params);
    List<String> getIndicatorObjTypeIdList(Map<String,Object> params);
    
}