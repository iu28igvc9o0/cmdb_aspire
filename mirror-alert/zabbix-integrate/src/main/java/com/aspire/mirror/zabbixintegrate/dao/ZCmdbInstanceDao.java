package com.aspire.mirror.zabbixintegrate.dao;

import com.aspire.mirror.zabbixintegrate.dao.po.ZCmdbInstance;
import com.aspire.mirror.zabbixintegrate.daoAlert.po.CmdbInstance;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author baiwp
 * @title: ZCmdbInstanceDao
 * @projectName zabbix-integrate
 * @description: TODO
 * @date 2019/7/2416:02
 */
@Mapper
public interface ZCmdbInstanceDao {

    /**
     * 新增实例
     * @param entity 实例数据
     * @return
     */
    void insert(CmdbInstance entity);

    /**
     * 批量新增实例
     * @param list 实例数据
     * @return
     */
    void insertBatch(List<ZCmdbInstance> list);

    void insertBatchNew(List<Map<String, Object>> list);

    /**
     * 删除所有实例
     * @return
     */
    void deleteAll();

    int count();
    
    List<Map<String, String>> getCmdbNullDevices();
}