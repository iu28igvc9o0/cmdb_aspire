package com.aspire.mirror.alert.server.dao.kpi;

import com.aspire.mirror.alert.server.dao.kpi.po.KpiResultTransfer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.zabbixintegrate.daoAlert
 * @Author: baiwenping
 * @CreateTime: 2020-04-16 14:48
 * @Description: ${Description}
 */
@Mapper
public interface KpiResultTransferMapper {
    /** 
    * 
    * @auther baiwenping
    * @Description 
    * @Date 10:28 2020/4/20
    * @Param [configId]
    * @return java.util.List<com.aspire.mirror.zabbixintegrate.daoAlert.po.KpiResultTransfer>
    **/
    List<KpiResultTransfer> selectByConfigId(String configId);
}
