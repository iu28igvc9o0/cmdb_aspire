package com.aspire.cmdb.agent.schedule;

import com.aspire.cmdb.agent.ipscan.IPScanAgent;
import com.aspire.cmdb.agent.ipscan.entity.ScanIpEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: BusinessScanSchedule
 * Author:   zhu.juwang
 * Date:     2019/9/4 15:46
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
//@EnableScheduling
@Slf4j
//@Component
public class BusinessScanSchedule {

    @Autowired
    private IPScanAgent scanAgent;
    @Value(value = "${businessMonitor.scan.ip.file}")
    private String scanIpFile;
    private AtomicInteger maxThread = new AtomicInteger(0);
    private List<ScanIpEntity> scanList;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void startScan() {
        log.info("Begin schedule ... ");
        initScanIPList();
        log.info("scanList size -> {}", scanList.size());
        for (ScanIpEntity scanIp : scanList) {
            if (maxThread.get() > 20) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    log.error("Sleep 1s error.", e);
                }
            }
            new Thread(() -> {
                maxThread.incrementAndGet();
                log.info("Thread -> {} Begin scan ip -> {}", maxThread.get(), scanIp.getIp());
                boolean isFind = scanAgent.start(scanIp);
                if (!isFind) {
                    log.info("Scan ip -> {} is not receive. ", scanIp.getIp());
                }
                maxThread.decrementAndGet();
            }).start();
        }
    }
    private void initScanIPList() {
        BufferedReader bufferedReader = null;
        try {
            log.info("scan ip file -> {}", scanIpFile);
            scanList = new ArrayList<>();
            // luowenbo 2020.07.16 路径遍历缺陷修复
            String pattern = "/\" : | * ? < >";
            File file;
            if(Pattern.matches(pattern,scanIpFile)) {
                log.info("Scan ip file -> {} is not safe. ", scanIpFile);
                return;
            } else {
                file = new File(scanIpFile);
            }
            bufferedReader = new BufferedReader(new FileReader(file));
            String strLine;
            while(null != (strLine = bufferedReader.readLine())){
                if (StringUtils.isEmpty(strLine.trim())) {
                    continue;
                }
                log.info("strLine -> {}", strLine);
                String[] ipInfo = strLine.split(" ");
                ScanIpEntity scanIpEntity = new ScanIpEntity();
                scanIpEntity.setIp(ipInfo[0]);
                scanList.add(scanIpEntity);
            }
        } catch(Exception e){
            log.error("Parse scan ip file error.", e);
        } finally {
            if (null != bufferedReader) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    log.error("stream close fail.", e);
                    e.printStackTrace();
                }
            }
        }
    }
}
