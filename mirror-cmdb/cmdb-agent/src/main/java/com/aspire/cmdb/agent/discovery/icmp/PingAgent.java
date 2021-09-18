package com.aspire.cmdb.agent.discovery.icmp;

import com.aspire.cmdb.agent.config.Const;
import com.aspire.cmdb.agent.discovery.AbstractAgent;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: PingAgent
 * Author:   zhu.juwang
 * Date:     2019/4/24 19:02
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@NoArgsConstructor
public class PingAgent extends AbstractAgent<PingAgent> {

    public PingAgent(JSONObject agentParams) {
        super(agentParams);
    }
    @Override
    protected void agent() {
        String osName = System.getProperty("os.name").toLowerCase();
        log.info("Ping command run at -> {}", osName);
        for (String ip : scanIpList) {
            boolean isFind = ping(ip);
            if (isFind) {
                log.info("IP -> {} has been find.", ip);
                discoveryIpList.add(ip);
            } else {
                log.warn("IP -> {} can't be find, skipped", ip);
            }
        }
    }

    /**
     * Ping IP是否可通
     * @param ipAddress  ip地址
     * @return 可以Ping通 返回 ture, 反之返回false
     */
    public boolean ping(String ipAddress) {
        boolean returnBoolean = false;
        String pingCommand;
        String osName = System.getProperty("os.name").toLowerCase();
        if(osName.contains("windows")){
            pingCommand = "ping " + ipAddress + " -n 1 -w " + Const.PING_TIME_OUT;
        }else{
            pingCommand = "ping " + " -c 1 -w " + Const.PING_TIME_OUT + " " + ipAddress;
        }
        //log.info("Execute command -> {}", pingCommand);
        try {
            Process p = Runtime.getRuntime().exec(pingCommand);
            StringBuilder sb = new StringBuilder();
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
            Pattern pattern = Pattern.compile("(TTL=\\d+)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(sb.toString().toLowerCase());
            while (matcher.find()) {
                returnBoolean = true;
                break;
            }
        } catch (Exception ex) {
            log.error("Execute Ping command error. ", ex.getMessage(), ex);
        } finally {
            return returnBoolean;
        }
    }
}
