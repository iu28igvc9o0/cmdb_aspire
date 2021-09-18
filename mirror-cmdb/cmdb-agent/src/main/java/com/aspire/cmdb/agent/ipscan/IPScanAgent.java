package com.aspire.cmdb.agent.ipscan;

import com.aspire.cmdb.agent.config.Const;
import com.aspire.cmdb.agent.ipscan.entity.ScanIpEntity;
import com.aspire.mirror.common.util.DateUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: IPScanAgent
 * Author:   zhu.juwang
 * Date:     2019/9/3 21:26
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
//@Component
@Data
public class IPScanAgent {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Value(value = "${businessMonitor.alert.ip.dir}")
    private String alertIpDir;
    @Value(value = "${businessMonitor.idcType}")
    private String idcType;
    @Value(value = "${businessMonitor.monitor_object}")
    private String monitorObject;
    @Value(value = "${businessMonitor.monitor_level}")
    private String monitorLevel;
    @Value(value = "${businessMonitor.kafka.topic:TOPIC_SYSTEM_ALERTS}")
    private String topic;
    @Value(value = "${businessMonitor.kafka.topic.partition:3}")
    private String partition;
    @Value(value = "${businessMonitor.test.ip:''}")
    private String testIp;
    @Value(value = "${businessMonitor.form:''}")
    private String form;
    @Value(value = "${businessMonitor.needSend:'false'}")
    private String needSend;

    public boolean start(ScanIpEntity scanIpEntity) {
        String osName = System.getProperty("os.name").toLowerCase();
        log.info("Ping command run at -> {}", osName);
        boolean isFind = ping(scanIpEntity);
        if (scanIpEntity.getIp().equals(testIp)) {
            isFind = true;
        }
        // 匹配成功, 判断是否需要消除告警
        if (isFind) {
            boolean fileExists = false;
            File existsFile = null;
            // luowenbo 2020.07.17 路径遍历漏洞修复
            String pattern = "/\" : | * ? < >";
            File alertFile;
            if(Pattern.matches(pattern,alertIpDir)) {
                log.info("Scan ip file -> {} is not safe. ", alertIpDir);
                return false;
            } else {
                alertFile = new File(alertIpDir);
            }
            if (alertFile.exists() && alertFile.isDirectory()) {
                File[] files = alertFile.listFiles();
                for (File file : files) {
                    if (file.getName().equals(scanIpEntity.getIp() + ".txt")) {
                        fileExists = true;
                        existsFile = file;
                        break;
                    }
                }
            }
            if (fileExists) {
                // 取消告警 删除文件
                if (sendAlert("2", scanIpEntity)) {
                    existsFile.delete();
                }
            }
        } else {
            File alertFile = new File(scanIpEntity.getIp() + ".txt");
            if (!alertFile.exists()) {
                // 发送告警 新增文件
                if (sendAlert("1", scanIpEntity)) {
                    try {
                        alertFile.createNewFile();
                    } catch (IOException e) {
                        log.error("Create alert file error.", e);
                    }
                }
            }
        }
        return isFind;
    }

    private boolean sendAlert(String monitorType, ScanIpEntity scanIpEntity) {
        try {
            if (needSend != null && needSend.equals("true")) {
                JSONObject kafkaData = new JSONObject();
                kafkaData.put("moniResult", monitorType);
                kafkaData.put("source", "inside");
                kafkaData.put("deviceIP", scanIpEntity.getIp());
                kafkaData.put("monitorObject", monitorObject);
                kafkaData.put("monitorIndex", "Ping不可达(来自" + form + ")");
                kafkaData.put("alertLevel", monitorLevel);
                kafkaData.put("curMoniTime", DateUtil.format(new Date(), "yyyy.MM.dd HH:mm:ss"));
                kafkaData.put("businessSystem", idcType);
                kafkaData.put("z_itemId", monitorObject);
                log.info("Send kafka message -> {} ", kafkaData.toString());
                kafkaTemplate.send(topic, partition, kafkaData.toString());
            }
            return true;
        } catch (Exception e) {
            log.error("Send kafka message error", e);
            return false;
        }
    }

    /**
     * Ping IP是否可通
     * @param scanIp
     * @return 可以Ping通 返回 ture, 反之返回false
     */
    private boolean ping(ScanIpEntity scanIp) {
        boolean returnBoolean = false;
        String pingCommand;
        String osName = System.getProperty("os.name").toLowerCase();
        if(osName.contains("windows")){
            pingCommand = "ping " + scanIp.getIp() + " -n 3 -w " + Const.PING_TIME_OUT;
        }else{
            pingCommand = "ping " + " -c 3 -i 1 " + scanIp.getIp();
        }
        try {
            String returnValue = execute(pingCommand);
            if (returnValue == null) {
                throw new RuntimeException("Execute ping command error.");
            }
            Pattern pattern = Pattern.compile("(TTL=\\d+)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(returnValue.toUpperCase());
            while (matcher.find()) {
                returnBoolean = true;
                break;
            }
        } catch (Exception ex) {
            log.error("Execute Ping command error. ", ex.getMessage(), ex);
            throw new RuntimeException("Execute ping command error.", ex);
        }
        return returnBoolean;
    }

    public String execute (String command) {
        try {
            Process p = Runtime.getRuntime().exec(command);
            StringBuilder sb = new StringBuilder("");
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    p.getInputStream(), "GB2312"));
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
                sb.append('\n');
            }
            br.close();
            br = new BufferedReader(new InputStreamReader(
                    p.getErrorStream(), "GB2312"));
            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
                sb.append('\n');
            }
            br.close();
            return sb.toString();
        } catch (Exception ex) {
            log.error("Execute command error. ", ex.getMessage(), ex);
            return null;
        }
    }
}
