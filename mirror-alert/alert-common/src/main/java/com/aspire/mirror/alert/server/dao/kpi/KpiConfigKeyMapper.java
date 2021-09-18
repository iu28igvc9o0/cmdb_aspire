package com.aspire.mirror.alert.server.dao.kpi;

import com.aspire.mirror.alert.server.dao.kpi.po.KpiConfigKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.zabbixintegrate.daoAlert
 * @Author: baiwenping
 * @CreateTime: 2020-04-15 09:56
 * @Description: ${Description}
 */
@Mapper
public interface KpiConfigKeyMapper {

    List<KpiConfigKey> selectByConfigId(String configId);
}
