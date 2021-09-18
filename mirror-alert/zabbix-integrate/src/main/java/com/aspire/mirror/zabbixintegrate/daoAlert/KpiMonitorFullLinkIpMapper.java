package com.aspire.mirror.zabbixintegrate.daoAlert;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.zabbixintegrate.daoAlert
 * @Author: baiwenping
 * @CreateTime: 2020-04-21 20:26
 * @Description: ${Description}
 */
@Mapper
public interface KpiMonitorFullLinkIpMapper {
    /**
     *
     * @param map
     * @return
     */
    int insert (Map<String, Object> map);

    void deleteByPrimary(@Param("tag") String tag, @Param("ip") String ip);


}
