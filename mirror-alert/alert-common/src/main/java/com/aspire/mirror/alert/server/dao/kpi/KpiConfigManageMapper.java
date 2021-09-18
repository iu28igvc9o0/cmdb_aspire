package com.aspire.mirror.alert.server.dao.kpi;

import com.aspire.mirror.alert.server.dao.kpi.po.KpiConfigManage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.zabbixintegrate.daoAlert
 * @Author: baiwenping
 * @CreateTime: 2020-04-15 09:55
 * @Description: ${Description}
 */
@Mapper
public interface KpiConfigManageMapper {

    /**
    * 查询有效的zabbix监控配置
    * @auther baiwenping
    * @Description
    * @Date 14:43 2020/4/17
    * @Param []
    * @return java.util.List<com.aspire.mirror.zabbixintegrate.daoAlert.po.KpiConfigManage>
    **/
    List<KpiConfigManage> selectValidList();

}
