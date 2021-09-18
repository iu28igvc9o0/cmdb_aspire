package com.aspire.mirror.zabbixintegrate.biz.monitor;

import com.aspire.mirror.zabbixintegrate.dao.HistoryMapper;
import com.aspire.mirror.zabbixintegrate.daoAlert.KpiConfigKeyMapper;
import com.aspire.mirror.zabbixintegrate.daoAlert.KpiMonitorFullLinkIpMapper;
import com.aspire.mirror.zabbixintegrate.daoAlert.po.KpiConfigKey;
import com.aspire.mirror.zabbixintegrate.util.CommonUtils;
import com.aspire.mirror.zabbixintegrate.util.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author baiwenping
 * @Title: ZabbixCheckFullLinkIpJob
 * @Package com.aspire.mirror.zabbixintegrate.biz.monitor
 * @Description: TODO
 * @date 2021/1/26 16:48
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "monitor.kpi.open", havingValue = "true")
public class ZabbixCheckFullLinkIpJob {
    @Value("${local.alertIndex}")
    private String alertIndex;
    @Value("${monitor.kpi.iplist:}")
    private String ipList;
    @Autowired
    private KpiConfigKeyMapper kpiConfigKeyMapper;
    @Autowired
    private HistoryMapper historyMapper;
    @Autowired
    private KpiMonitorFullLinkIpMapper kpiMonitorFullLinkIpMapper;
    /**
     *
     * @auther baiwenping
     * @Description
     * @Date 16:09 2020/4/17
     * @Param []
     * @return void
     **/
    @Scheduled(cron = "0 0 1 * * ?")
    public void run() {
        List<KpiConfigKey> kpiConfigKeyList = kpiConfigKeyMapper.selectByConfigId("full_link_key");
        if (CollectionUtils.isEmpty(kpiConfigKeyList)) {
            return;
        }
        String keySql = CommonUtils.generateKeysDb(kpiConfigKeyList);
        String sql = "SELECT DISTINCT it.ip ip FROM items i INNER JOIN interface it ON it.hostid=i.hostid where " + keySql;
        log.info("check full link ip, sql is : {}", sql);
        List<Map<String, Object>> mapList = historyMapper.selectBySql(sql);
        if (CollectionUtils.isEmpty(mapList)) {
            return;
        }
        for (Map<String, Object> map : mapList) {
            String ip = MapUtils.getString(map, "ip");
            if (StringUtils.isEmpty(ip)) {
                continue;
            }

            // 删除
            if (!StringUtils.isEmpty(ipList) && ipList.indexOf(ip) > -1) {
                kpiMonitorFullLinkIpMapper.deleteByPrimary(alertIndex, ip);
            } else {
                // 新增
                map.put("tag", alertIndex);
                kpiMonitorFullLinkIpMapper.insert(map);
            }
        }
    }
}
