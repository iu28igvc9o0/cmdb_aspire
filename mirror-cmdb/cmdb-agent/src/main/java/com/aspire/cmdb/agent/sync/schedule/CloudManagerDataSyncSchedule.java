package com.aspire.cmdb.agent.sync.schedule;

import com.aspire.cmdb.agent.sync.SyncConst;
import com.aspire.cmdb.agent.sync.service.ICloudSyncService;
import com.aspire.cmdb.agent.util.HttpUtil;
import com.aspire.ums.cmdb.code.payload.CmdbCode;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

/**
 * 云管系统物理机、虚拟机数据同步任务
 */
@Component
@Configurable
@EnableScheduling
@Slf4j
public class CloudManagerDataSyncSchedule {
    // 苏研获取虚拟机的URL
    @Value("${sync.url.suyan.vmUrl}")
    public String syVmUrl;
    // 苏研获取物理机的URL
    @Value("${sync.url.suyan.pmUrl}")
    public String syPmUrl;
    // 调用苏研接口认证口令
    @Value("${sync.url.suyan.token}")
    public String token;
    @Value("${kafka.topic.suyan_physical_sync:SUYAN_PHYSICAL_SYNC}")
    public String physicalTopic;
    @Value("${kafka.topic.suyan_vm_sync:SUYAN_VM_SYNC}")
    public String vmTopic;
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private ICmdbCodeService codeService;
    private boolean flag = true;

    @Scheduled(cron = "0 0 0 1/2 * ?")
    public void syncPhysicalData() {
        //开始同步物理机数据
        log.info("Begin sync Su Yan physical machine data ...");
        this.syncData("X86服务器");
        log.info("End sync Su Yan physical machine data.");
    }

    @Scheduled(cron = "0 0 0 1/2 * ?")
    public void syncVmData() {
        //开始同步云主机数据
        log.info("Begin sync Su Yan Vm machine data ...");
        this.syncData("云主机");
        log.info("End sync Su Yan Vm machine data.");
    }

    public void syncData(String deviceType) {
        if (!deviceType.equals("X86服务器") && !deviceType.equals("云主机")) {
            throw new RuntimeException("Not support device type " + deviceType + " operator.");
        }
        this.flag = true;
        String httpUrl = "", topic = "";
        if (deviceType.equals("X86服务器")) {
            httpUrl = syPmUrl;
            topic = physicalTopic;
        }
        if (deviceType.equals("云主机")) {
            httpUrl = syVmUrl;
            topic = vmTopic;
        }
        final String requestHttpUrl = httpUrl;
        int currentPage = 0, totalPage = 1;
        // 一直循环，直到所有分页数据都更新完
        while(currentPage <= totalPage && flag) {
            //发送到kafka
            try {
                JSONObject returnData = getSyncData(requestHttpUrl, String.valueOf(currentPage));
                JSONObject data = returnData.getJSONObject("data");
                if (!data.isEmpty()) {
                    if (currentPage == 0) {
                        totalPage = data.getInt("pages");
                    }
                    log.info("{} -> 正在处理苏研云管数据对比, 共{}页, 当前处理第{}页.", deviceType, totalPage, currentPage);
                    JSONObject sendObject = new JSONObject();
                    sendObject.put("device_type", deviceType);
                    sendObject.put("data", data.getJSONArray("list"));
                    sendObject.put("page", totalPage);
                    sendObject.put("currentpage", currentPage);

                    //cloudSyncService.syncData(sendObject);
                    // 向kafka发送消息
                    kafkaTemplate.send(topic, sendObject.toString());
                    log.info("Send topic page -> data success.", currentPage);
                }
            } catch (Exception e) {
                log.info("数据处理异常: {}", e.getMessage(), e);
            }
            currentPage ++;
        }
        log.info("苏研云管数据对比结束.");
    }

    /*
    *   根据URL 和 当前页数 获得数据
    * */
    public JSONObject getSyncData(String url,String currentPage) {
        JSONObject header = new JSONObject();
        header.put("Authorization",token);
        String rsUrl = url + "?pageNo=" + currentPage + "&pageSize=" + SyncConst.pageSize;
        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        Long startTime = start.getTimeInMillis();
        Calendar end = Calendar.getInstance();
        Long endTime = end.getTimeInMillis();
        String updateAt = "gte:"+startTime+",lte:"+endTime;
        rsUrl += "&updatedAt=" + updateAt;
        return JSONObject.fromObject(HttpUtil.getMethod(rsUrl, header).toString());
    }

    public void stop() {
        this.flag = false;
    }
}
