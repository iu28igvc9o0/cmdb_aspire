package com.aspire.cmdb.agent.discovery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: AbstractAgent
 * Author:   zhu.juwang
 * Date:     2019/4/24 19:01
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractAgent<T> {

    /**
     *  Agent参数集合
     */
    public JSONObject agentParams;
    /**
     * 需要扫描的IP集合
     */
    public List<String>  scanIpList;

    /**
     * 检测到存在的IP集合
     */
    public List<String> discoveryIpList = new ArrayList<>();

    public AbstractAgent(JSONObject agentParams) {
        this.agentParams = agentParams;
    }

    /**
     * 启动方法
     */
    public void start() {
        initIpList();
        agent();
    }

    /**
     * 子类实现具体操作
     */
    protected abstract void agent();

    public void initIpList() {
        scanIpList = new ArrayList<>();
        String startScanIp = "";
        String endScanIp = "";
        if (this.agentParams != null && this.agentParams.containsKey("start_scan_ip")) {
            startScanIp = this.agentParams.getString("start_scan_ip");
        }
        if (this.agentParams != null && this.agentParams.containsKey("end_scan_ip")) {
            endScanIp = this.agentParams.getString("end_scan_ip");
        }
        if (StringUtils.isNotEmpty(startScanIp) && StringUtils.isNotEmpty(endScanIp)) {
            String prefix = startScanIp.substring(0, startScanIp.lastIndexOf("."));
            int start = Integer.parseInt(startScanIp.substring(startScanIp.lastIndexOf(".") + 1));
            int step = Integer.parseInt(endScanIp.substring(endScanIp.lastIndexOf(".") + 1));
            while (step - start >= 0) {
                scanIpList.add(prefix + "." + (start ++));
            }
        }
    }
}
