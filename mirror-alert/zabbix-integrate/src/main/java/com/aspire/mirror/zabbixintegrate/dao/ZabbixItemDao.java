package com.aspire.mirror.zabbixintegrate.dao;

import com.aspire.mirror.zabbixintegrate.domain.RawAlert;
import com.aspire.mirror.zabbixintegrate.domain.ZabbixItem;
import com.aspire.mirror.zabbixintegrate.domain.ZabbixTrends;
import com.aspire.mirror.zabbixintegrate.domain.ZabbixTrigger;

import java.util.List;
import java.util.Map;

/**
 * @author baiwp
 * @title: ZabbixItemDao
 * @projectName zabbix-integrate
 * @description: TODO
 * @date 2019/6/2416:02
 */
public interface ZabbixItemDao {
    List<ZabbixItem> fetchItemList(Map<String, Object> params);

    List<ZabbixTrigger> fetchTriggerList(Map<String, Object> params);

    List<ZabbixTrends> fetchTrendList(Map<String, Object> params);

    Long fetchMinClock(Map<String, Object> params);
}
