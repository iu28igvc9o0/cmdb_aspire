package com.aspire.mirror.zabbixintegrate.daoCmdb;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.zabbixintegrate.bean.SuyanMonitorConfig;
import com.aspire.mirror.zabbixintegrate.daoCmdb.po.CmdbInstance;

/**
 * @author baiwp
 * @title: CmdbInstanceDao
 * @projectName zabbix-integrate
 * @description: TODO
 * @date 2019/7/2416:02
 */
@Mapper
public interface CmdbInstanceDao {
     /**
     * 获取所有实例
     * @return 返回所有实例数据
     */
    List<CmdbInstance> list(Map<String, Object> map);

    int count(Map<String, Object> map);
    
    List<CmdbInstance> getDeviceInfo(@Param("suyanResids")List<String> suyanResids);
    
    List<SuyanMonitorConfig> selectSuyanMonitor();
    SuyanMonitorConfig selectSuyanMonitorBySuyanKey(@Param("suyanKey")String suyanKey);
}