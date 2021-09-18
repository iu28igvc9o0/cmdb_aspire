package com.aspire.mirror.zabbixintegrate.daoAlert;

import com.aspire.mirror.zabbixintegrate.daoAlert.po.CmdbInstance;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

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
    /** 
    * 
    * @auther baiwenping
    * @Description 
    * @Date 17:26 2020/4/17
    * @Param [map]
    * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    **/
    List<Map<String, Object>> selectAllByIdc(Map<String, Object> map);

    /**
     *
     * @auther baiwenping
     * @Description
     * @Date 17:26 2020/4/17
     * @Param [map]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String, Object>> selectAllByField(Map<String, Object> map);
}