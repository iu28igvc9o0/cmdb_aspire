package com.aspire.cmdb.agent.controller;

import com.aspire.cmdb.agent.schedule.CmdbInstanceAgentCheckSchedule;
import com.aspire.cmdb.agent.schedule.ScreenBizSystemDeviceDetailSchedule;
import com.aspire.cmdb.agent.schedule.gvSafe.GvSafeFaultSchedule;
import com.aspire.cmdb.agent.sync.SyncConst;
import com.aspire.cmdb.agent.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ForwardController
 * Author:   zhu.juwang
 * Date:     2019/12/10 10:05
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@RequestMapping("/")
@Slf4j
public class ForwardController {
    // 调用苏研接口认证口令
    @Value("${sync.url.suyan.token}")
    public String token;

    @Autowired
    private ScreenBizSystemDeviceDetailSchedule schedule;

    @Autowired
    private CmdbInstanceAgentCheckSchedule agentCheckSchedule;
    
    @Autowired
    private GvSafeFaultSchedule gvSafeFaultSchedule;
    

    @GetMapping("forward/to/suyan")
    public Object forwardToSuYan(@RequestParam("url") String url,
                                     @RequestParam("currentPage") Integer currentPage) {
        JSONObject header = new JSONObject();
        header.put("Authorization",token);
        String rsUrl = url + "?pageNo=" + currentPage + "&pageSize=" + SyncConst.pageSize;
        return HttpUtil.getMethod(rsUrl, header);
    }

    @GetMapping("sync/bizsystem/detail")
    public void syncBzDetialInfo(@RequestParam("monthParam") String monthParam) {
        log.info("开始————更新ES中业务系统的详细设备");
        schedule.syncDeviceDetailData(monthParam);
        log.info("结束————更新ES中业务系统的详细设备");
    }

    @GetMapping("sync/agent/check")
    public void getAgentCheckData() {
        log.info("开始————更新ES中业务系统的详细设备");
        agentCheckSchedule.checkAgentData();
        log.info("结束————更新ES中业务系统的详细设备");
    }
    
    @GetMapping("sync/agent/testGvSafe")
    public void testGvSafe() throws Exception {
        log.info("开始————扫描sla");
        gvSafeFaultSchedule.scanFault();
        log.info("结束————扫描sla");
    }

}
