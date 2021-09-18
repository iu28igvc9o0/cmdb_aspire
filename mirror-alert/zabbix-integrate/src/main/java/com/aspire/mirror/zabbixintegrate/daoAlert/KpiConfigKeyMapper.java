package com.aspire.mirror.zabbixintegrate.daoAlert;

import com.aspire.mirror.zabbixintegrate.daoAlert.po.KpiConfigKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

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
