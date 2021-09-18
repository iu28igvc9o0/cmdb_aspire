package com.aspire.cmdb.agent.schedule;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.aspire.cmdb.agent.client.IAlertClient;
import com.aspire.cmdb.agent.monthReport.mapper.CmdbAddedDepReportMapper;
import com.aspire.cmdb.agent.monthReport.mapper.CmdbCompReportMapper;
import com.aspire.cmdb.agent.monthReport.mapper.CmdbDepInfoReportMapper;
import com.aspire.cmdb.agent.monthReport.mapper.CmdbDistStrUtzLowReportMapper;
import com.aspire.cmdb.agent.monthReport.mapper.CmdbNetworkReportMapper;
import com.aspire.cmdb.agent.monthReport.mapper.CmdbPhyReportMapper;
import com.aspire.cmdb.agent.monthReport.mapper.CmdbPhyUtzLowReportMapper;
import com.aspire.cmdb.agent.monthReport.mapper.CmdbRecycleReportMapper;
import com.aspire.cmdb.agent.monthReport.mapper.CmdbVmCpuUtzLowReportMapper;
import com.aspire.cmdb.agent.monthReport.mapper.CmdbVmMemoryUtzLowReportMapper;
import com.aspire.cmdb.agent.monthReport.mapper.CmdbVmReportMapper;
import com.aspire.ums.cmdb.helper.BpmTokenHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2020/2/13
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Component
@EnableScheduling
@Slf4j
public class CmdbMonthReportSchedule {

    @Autowired
    private CmdbPhyReportMapper phyReportMapper;
    @Autowired
    private CmdbCompReportMapper compReportMapper;
    @Autowired
    private CmdbVmReportMapper vmReportMapper;
    @Autowired
    private CmdbNetworkReportMapper networkReportMapper;
    @Autowired
    private CmdbDepInfoReportMapper depInfoReportMapper;
    @Autowired
    private CmdbAddedDepReportMapper addedDepReportMapper;
    @Autowired
    private CmdbRecycleReportMapper recycleReportMapper;
    @Autowired
    private CmdbVmCpuUtzLowReportMapper vmCpuUtzLowReportMapper;
    @Autowired
    private CmdbVmMemoryUtzLowReportMapper vmMemoryUtzLowReportMapper;
    @Autowired
    private CmdbPhyUtzLowReportMapper phyUtzLowReportMapper;
    @Autowired
    private CmdbDistStrUtzLowReportMapper distStrUtzLowReportMapper;
    @Autowired
    private IAlertClient alertClient;
    @Autowired
    private BpmTokenHelper bpmTokenHelper;
    @Value("${bpm.monthReport.baseUrl:}")
    private String BPM_MONTHREPORT_BASEURL;
    @Value("${bpm.monthReport.resUrl:}")
    private String BPM_MONTHREPORT_RESURL;
    @Value("${bpm.monthReport.depUrl:}")
    private String BPM_MONTHREPORT_DEPURL;
    @Value("${bpm.monthReport.faultUrl:}")
    private String BPM_MONTHREPORT_FAULTURL;

//    @Scheduled(cron = "0 0 8 1 1/1 ?")
//    @Scheduled(cron = "0 0/5 * * * ?")
    @Scheduled(cron = "0 0 8 * * ? ")
    public void countMonthReport() {
        //开始统计月报
        log.info("开始统计月报...");
        // 统计物理设备总量
        countPhyCount();
        // 统计计算节点的设备总量
        countCompCount();
        // 统计云资源总量
        countVmCount();
        // 统计网络设备
        countNetworkCount();
        // 统计租户资源
        countDepInfo();
        // 统计新增租户信息
        countAddedDep();
        // 资源回收和清理
        countRecycle();
        // 租户资源物理计算资源峰值利用率低于30%
        countPhyUtzLow();
        // 各资源池租户虚拟计算资源CPU峰值利用率低于30%
        countVmCpuUtzLow();
        // 各资源池租户虚拟计算资源内存峰值利用率低于30%
        countVmMemoryUtzLow();
        // 各资源池租户分布式存储资源利用率低于40%
        countDistStorageUtzLow();
        log.info("统计月报结束...");

    }

    private void countDistStorageUtzLow() {
        try {
            // 统计租户资源
            log.info("开始各资源池租户分布式存储资源利用率低于40%...");
            List<Map<String, String>> distStrUtzLowReports = distStrUtzLowReportMapper.count();
            if (distStrUtzLowReports != null && distStrUtzLowReports.size() > 0) {
                distStrUtzLowReportMapper.save(distStrUtzLowReports);
            }
            // 从alert获取数据入库
//            List<Map<String, String>> alertValues = alertClient.getReportMonthAllDdata();
//            if (alertValues != null && alertValues.size() > 0) {
//                addedDepReportMapper.saveAlert(alertValues);
//            }
            log.info("各资源池租户分布式存储资源利用率低于40%成功...");

        } catch (Exception e) {
            log.error("各资源池租户分布式存储资源利用率低于40%失败...", e);
        }
    }

    private void countVmMemoryUtzLow() {
        try {
            // 统计租户资源
            log.info("开始各资源池租户虚拟计算资源内存峰值利用率低于30%...");
            List<Map<String, String>> vmMemoryUtzLowReports = vmMemoryUtzLowReportMapper.count();
            if (vmMemoryUtzLowReports != null && vmMemoryUtzLowReports.size() > 0) {
                vmMemoryUtzLowReportMapper.save(vmMemoryUtzLowReports);
            }
            // 从alert获取数据入库
//            List<Map<String, String>> alertValues = alertClient.getReportMonthAllDdata();
//            if (alertValues != null && alertValues.size() > 0) {
//                addedDepReportMapper.saveAlert(alertValues);
//            }
            log.info("各资源池租户虚拟计算资源内存峰值利用率低于30%成功...");

        } catch (Exception e) {
            log.error("各资源池租户虚拟计算资源内存峰值利用率低于30%失败...", e);
        }
    }

    private void countVmCpuUtzLow() {
        try {
            // 统计租户资源
            log.info("开始统计各资源池租户虚拟计算资源CPU峰值利用率低于30%...");
            List<Map<String, String>> vmCpuUtzLowReports = vmCpuUtzLowReportMapper.count();
            if (vmCpuUtzLowReports != null && vmCpuUtzLowReports.size() > 0) {
                vmCpuUtzLowReportMapper.save(vmCpuUtzLowReports);
            }
            // 从alert获取数据入库
//            List<Map<String, String>> alertValues = alertClient.getReportMonthAllDdata();
//            if (alertValues != null && alertValues.size() > 0) {
//                addedDepReportMapper.saveAlert(alertValues);
//            }
            log.info("统计各资源池租户虚拟计算资源CPU峰值利用率低于30%成功...");

        } catch (Exception e) {
            log.error("统计各资源池租户虚拟计算资源CPU峰值利用率低于30%失败...", e);
        }
    }

    private void countPhyUtzLow() {
        try {
            // 统计租户资源
            log.info("开始统计租户资源物理计算资源峰值利用率低于30%...");
            List<Map<String, String>> phyUtzLowReports = phyUtzLowReportMapper.count();
            if (phyUtzLowReports != null && phyUtzLowReports.size() > 0) {
                phyUtzLowReportMapper.save(phyUtzLowReports);
            }
            // 从alert获取数据入库
//            List<Map<String, String>> alertValues = alertClient.getReportMonthAllDdata();
//            if (alertValues != null && alertValues.size() > 0) {
//                addedDepReportMapper.saveAlert(alertValues);
//            }
            log.info("统计租户资源物理计算资源峰值利用率低于30%成功...");

        } catch (Exception e) {
            log.error("统计租户资源物理计算资源峰值利用率低于30%失败...", e);
        }
    }

    private void countAddedDep() {
        try {
            // 统计租户资源
            log.info("开始统计新增租户信息...");
            List<Map<String, String>> addedDepReports = addedDepReportMapper.count();
            if (addedDepReports != null && addedDepReports.size() > 0) {
                addedDepReportMapper.save(addedDepReports);
            }
            // 从alert获取数据入库
            List<Map<String, String>> alertValues = alertClient.getReportMonthAllDdata();
            if (alertValues != null && alertValues.size() > 0) {
                addedDepReportMapper.saveAlert(alertValues);
            }
            log.info("统计新增租户信息成功...");

        } catch (Exception e) {
            log.error("统计新增租户信息失败...", e);
        }
    }

    private void countRecycle() {
        try {
            // 资源回收和清理
            log.info("开始统计资源回收和清理...");
            List<Map<String, String>> recycleReports = recycleReportMapper.count();
            if (recycleReports != null && recycleReports.size() > 0) {
                recycleReportMapper.save(recycleReports);
            }
            log.info("统计资源回收和清理成功...");
        } catch (Exception e) {
            log.error("统计资源回收和清理失败...", e);
        }

    }

    private void countDepInfo() {
        try {
            // 统计租户资源
            log.info("开始统计租户资源...");
            List<Map<String, String>> depInfoReports = depInfoReportMapper.count();
            if (depInfoReports != null && depInfoReports.size() > 0) {
                depInfoReportMapper.save(depInfoReports);
            }
            // 从bpm获取工单数据入库
            List<Map<String, String>> bpmValues = getFromBpmUrl(BPM_MONTHREPORT_BASEURL + BPM_MONTHREPORT_DEPURL, null);
            if (bpmValues != null && bpmValues.size() > 0) {
                depInfoReportMapper.saveBpm(bpmValues);
            }
            // 从bpm获取工单数据入库
            List<Map<String, String>> faultValues = getFromBpmUrl(BPM_MONTHREPORT_BASEURL + BPM_MONTHREPORT_FAULTURL, null);
            if (faultValues != null && faultValues.size() > 0) {
                depInfoReportMapper.saveBpmFault(faultValues);
            }
            // 从alert获取数据入库
            List<Map<String, String>> alertValues = alertClient.getReportMonthAllDdata();
            if (alertValues != null && alertValues.size() > 0) {
                depInfoReportMapper.saveAlert(alertValues);
            }
            log.info("统计租户资源成功...");

        } catch (Exception e) {
            log.error("统计租户资源失败...", e);
        }
    }

    private void countNetworkCount() {
        try {
            // 统计网络设备
            log.info("开始统计网络设备...");
            List<Map<String, String>> networkReportList = networkReportMapper.count();
            if (networkReportList != null && networkReportList.size() > 0) {
                networkReportMapper.save(networkReportList);
            }
            log.info("统计网络设备成功...");
        } catch (Exception e) {
            log.error("统计网络设备成功...", e);
        }
    }

    private void countVmCount() {
        try {
            // 统计云资源总量
            log.info("开始统计云资源总量...");
            List<Map<String, String>> vmReportList = vmReportMapper.count();
            if (vmReportList != null && vmReportList.size() > 0) {
                vmReportMapper.save(vmReportList);
            }
            // 从bpm获取数据
            List<Map<String, String>> bpmValues = getFromBpmUrl(BPM_MONTHREPORT_BASEURL + BPM_MONTHREPORT_RESURL, "云主机");
            if (bpmValues != null && bpmValues.size() > 0) {
                vmReportMapper.saveBpm(bpmValues);
            }
            log.info("统计云资源总量成功...");
        } catch (Exception e) {
            log.error("统计云资源总量成功...", e);
        }
    }

    private void countCompCount() {
        try {
            // 统计计算节点的设备总量
            log.info("开始统计计算节点的设备总量...");
            List<Map<String, String>> compReportList = compReportMapper.count();
            if (compReportList != null && compReportList.size() > 0) {
                compReportMapper.save(compReportList);
            }
            // 从bpm获取数据
            List<Map<String, String>> bpmValues = getFromBpmUrl(BPM_MONTHREPORT_BASEURL + BPM_MONTHREPORT_RESURL, "裸金属服务器");
            if (bpmValues != null && bpmValues.size() > 0) {
                compReportMapper.saveBpm(bpmValues);
            }
            log.info("统计计算节点的设备总量成功...");
        } catch (Exception e) {
            log.error("统计计算节点的设备总量失败...", e);
        }

    }

    private void countPhyCount() {
        try{
            // 统计物理设备总量
            log.info("开始统计物理设备总量...");
            List<Map<String, String>> phyReportList = phyReportMapper.count();
            if (phyReportList != null && phyReportList.size() > 0) {
                phyReportMapper.save(phyReportList);
            }
            log.info("统计物理设备总量成功...");
        } catch (Exception e) {
            log.error("统计物理设备总量失败...", e);
        }
    }



    private List<Map<String, String>> getFromBpmUrl (String url, String type) {
        String token = null;
        try {
            token = bpmTokenHelper.getToken();
            if (StringUtils.isEmpty(token)) {
                log.error("获取BPM token异常 token 为空");
                throw new RuntimeException("获取token异常");
            }
        } catch (Exception e) {
            throw new RuntimeException("获取token异常" + e.getMessage());
        }
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        params.put("month", format.format(c.getTime()));
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        requestHeaders.add("Authorization", "Bearer " + token);
        requestHeaders.add("Content-Type", "application/json;charset=utf-8");
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        params.entrySet().stream().forEach(o -> builder.queryParam(o.getKey(),o.getValue()));
        URI httpUrl = builder.build().encode().toUri();
        log.info("get bpm data url: {}" + httpUrl.toString());
        HttpEntity<Map> formEntity = new HttpEntity<>( requestHeaders);
        ResponseEntity response = restTemplate.exchange(httpUrl, HttpMethod.GET, formEntity, Object.class);
        if (response.getStatusCodeValue() != HttpStatus.OK.value()) {
            throw new RuntimeException("从bpm获取计算节点的设备数据失败..." + response.getBody());
        }
        Map<String, Object> respBody = (Map<String, Object>)response.getBody();
        List<Map<String, String>> resultList = (List<Map<String, String>>)respBody.get("value");
        log.info("get bpm data success,dataSize: {}" + resultList.size());
        return resultList;
    }
}
