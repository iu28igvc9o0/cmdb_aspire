package com.aspire.cmdb.agent.schedule;

import com.alibaba.fastjson.JSON;
import com.aspire.ums.cmdb.client.ICmdbToAlterESClient;
import com.aspire.ums.cmdb.dict.payload.ConfigDict;
import com.aspire.ums.cmdb.dict.service.ConfigDictService;
import com.aspire.ums.cmdb.instance.payload.CmdbInstanceAgentCheck;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceAgentCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName CmdbInstanceAgentCheckSchedule
 * @Description 已部署的AGENT，没有性能数据的扫描
 * @Author luowenbo
 * @Date 2020/5/26 10:37
 * @Version 1.0
 */
@Component
@EnableScheduling
@Slf4j
public class CmdbInstanceAgentCheckSchedule {

    @Autowired
    private ICmdbToAlterESClient esClient;
    @Autowired
    private ICmdbInstanceAgentCheckService agentCheckService;
    @Autowired
    private ConfigDictService configDictService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void checkAgentData() {
        // 1、获取所有资源池，并且循环遍历
        List<ConfigDict> dicts =  configDictService.selectDictsByType("idcType",null,null,null);
        for(ConfigDict item : dicts) {
            String idcType = item.getValue();
            log.info("开始-处理"+ idcType +"的设备数据...");
            // 2、依据资源池，获取CI中已经安装Agent的设备，设A集合
            List<CmdbInstanceAgentCheck> agentCheckList = agentCheckService.getCIWithAgent(idcType);
            Map<String,Object> esParam = packageESReqParam(idcType);
            // 3、依据资源池，和昨天的日期获取存在性能数据的设备，设B集合
            List<Map<String,Object>> agentList = esClient.getIpWithHaveData(esParam);
            for(Map<String,Object> agent : agentList) {
                String ip = agent.get("ip").toString();
                // 4、求出A-B的集合差数据
                agentCheckList = agentCheckList.stream().filter(p -> !ip.equals(p.getIp())).collect(Collectors.toList());
            }
            // 5、保存,补充数据
            for(CmdbInstanceAgentCheck obj : agentCheckList) {
                // 状态固定为未采集
                obj.setSyncStatus("未采集");
                obj.setScanTime(getYesterday("yyyy-MM-dd HH:mm:ss"));
            }
            agentCheckService.batchInsert(agentCheckList);
            log.info("结束-处理"+ idcType +"的设备数据...");
        }
    }

    /*
    * 按照指定格式封装ES的请求参数,时间参数，默认取前一天的日期
    * */
    private Map<String,Object> packageESReqParam(String idcType){
        Map<String,Object> rs = new HashMap<>();
        String dslStr = "{\"size\":0,\"query\":{\"bool\":{\"must\":[{\"match_phrase\":{\"idcType\":\""+ idcType +"\"}}]}},\"aggs\":{\"ip\":{\"terms\":{\"field\":\"host.keyword\",\"size\":100000}}}}";
        // 获取昨天日期，拼接ES的时间参数
        String yesterday = getYesterday("yyyyMMdd");
        String indexStr = "kpi-hour_" + yesterday + "*";
        rs.put("dsl",JSON.parse(dslStr));
        rs.put("index",indexStr);
        return rs;
    }

    /*
    *  获取昨天日期
    * */
    private String getYesterday(String pattern){
        Date today = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String yesterday = simpleDateFormat.format(today);
        return yesterday;
    }
}
