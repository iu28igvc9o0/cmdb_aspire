package com.aspire.mirror.alert.server.dao.cmdbInstance;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.common.entity.Page;

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
public interface CmdbInstanceMapper {

    /**
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @auther baiwenping
     * @Description
     * @Date 17:26 2020/4/17
     * @Param [map]
     **/
    List<Map<String, Object>> selectAllByField(Map<String, Object> map);

    /**
     * @param map
     */
    void insert(Map<String, Object> map);

    /**
     *
     */
    void deleteAll();

    /**
     * 根据id删除
     * @param id
     */
    void deleteById(@Param("id") String id);

    /**
     * 根据ip和资源池查询设备信息
     * @param ip
     * @param idcType
     * @return
     */
    List<Map<String, Object>> selectByIPAndIdcType(@Param("ip") String ip, @Param("idcType") String idcType);

    /**
     * 根据id查询设备
     * @param id
     * @return
     */
    List<Map<String, Object>> detailsById(@Param("id") String id);


    List<Map<String, Object>> pageList(Page page);
    
    int pageCount(Page page);
    
    List<Map<String, Object>> getMonitorDeviceList(Page page);
    
    int getMonitorDeviceCount(Page page);

    /**
     * 查询所有设备的id和ip
     * @return
     */
    List<Map<String, String>> getAllIdAndIp();

    /**
     * 根据id和ip删除设备信息
     * @param id
     * @param ip
     */
    void deleteByIdAndIp(@Param("id") String id, @Param("ip") String ip);
}