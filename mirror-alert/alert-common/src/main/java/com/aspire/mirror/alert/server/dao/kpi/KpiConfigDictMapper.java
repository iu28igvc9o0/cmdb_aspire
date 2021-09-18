package com.aspire.mirror.alert.server.dao.kpi;

import com.aspire.mirror.alert.server.dao.kpi.po.KpiConfigDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.zabbixintegrate.daoAlert
 * @Author: baiwenping
 * @CreateTime: 2020-04-20 19:01
 * @Description: ${Description}
 */
@Mapper
public interface KpiConfigDictMapper {
    /** 
    * 
    * @auther baiwenping
    * @Description 
    * @Date 19:04 2020/4/20
    * @Param [dictType]
    * @return java.util.List<com.aspire.mirror.zabbixintegrate.daoAlert.po.KpiConfigDict>
    **/
    List<KpiConfigDict> selectByType(@Param("dictType") String dictType);
}
