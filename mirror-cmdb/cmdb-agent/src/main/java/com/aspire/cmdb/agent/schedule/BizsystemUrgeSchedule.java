package com.aspire.cmdb.agent.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.cmdb.agent.util.StringUtils;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.helper.BpmTokenHelper;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: BizsystemUrgeSchedule
 * Author:   hangfang
 * Date:     2020/11/2
 * Description: 扫描业务系统，根据上线考核时间前15，5天发送催办工单
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@EnableScheduling
@Slf4j
@Component
public class BizsystemUrgeSchedule {

    @Autowired
    ICmdbInstanceService instanceService;

    @Autowired
    ICmdbConfigService configService;

    @Autowired
    private BpmTokenHelper bpmTokenHelper;
    @Scheduled(cron = "0 0 0 * * ?")
    public void scanToUrgeBizsystem () {
        log.info("------scanToUrgeBizsystem start {}------", System.currentTimeMillis());
        // 获取业务系统模型信息
        CmdbConfig config = configService.getConfigByCode("business_list_info");
        // 获取业务催办信息
        CmdbConfig bizUrgeConfig = configService.getConfigByCode("scan_biz_urge_bpm");
        if (config == null || StringUtils.isEmpty(config.getConfigValue())) {
           throw new RuntimeException("未配置业务系统模型信息，code[business_list_info]");
        }
        if (bizUrgeConfig == null || StringUtils.isEmpty(bizUrgeConfig.getConfigValue())) {
            throw new RuntimeException("未配置业务催办信息，code[scan_biz_urge_bpm]");
        }
        JSONObject bizUrgeObj = JSONObject.parseObject(bizUrgeConfig.getConfigValue());
        List<Long> urgeDays = JSONArray.parseArray(JSON.toJSONString(bizUrgeObj.get("urgeDays")), Long.class);
        String rwmc = bizUrgeObj.get("rwmc").toString();
        String rwclnr = bizUrgeObj.get("rwclnr").toString();
        log.info("------业务系统配置信息 {} ------", config.getConfigValue());
        Map bizModuleInfo = JSONObject.parseObject(config.getConfigValue(), Map.class);
        Map<String, Object> params = new HashMap<>();
        params.put("condicationCode", bizModuleInfo.get("condicationCode"));
        params.put("pageSize", 999999);
        params.put("module_id", bizModuleInfo.get("module_id"));
        Result<Map<String, Object>> bizsystemList = instanceService.getInstanceList(params, null);
        log.info("------查询到业务系统 {} 条 ------", bizsystemList.getTotalSize());
        for (Map<String, Object> biz : bizsystemList.getData()) {
            // 联系人账号，上线考核时间，催办时间 都有才创建工单
            if (biz.containsKey("business_concat_account") && !StringUtils.isNotEmpty(biz.get("business_concat_account"))) {
                log.info("业务系统[{}]没有业务联系人账号信息",biz.get("bizSystem"));
                continue;
            }
            if (biz.containsKey("access_online_time") && !StringUtils.isNotEmpty(biz.get("access_online_time"))) {
                log.info("业务系统[{}]没有上线考核时间信息",biz.get("bizSystem"));
                continue;
            }
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date onlineTime = null;
            Date currentTime = null;
            try {
                onlineTime = sdf.parse(biz.get("access_online_time").toString());
                currentTime = sdf.parse(sdf.format(System.currentTimeMillis()));
                Long diff = onlineTime.getTime() - currentTime.getTime();
                Long days = diff / (1000 * 60 * 60 * 24);
                log.info("------------ {}", days);
                if (urgeDays.contains(days)) {
                    log.info("催办工单");
                    rwclnr = rwclnr.replace("[day]", days + "");
                    toBmp(rwmc, rwclnr, biz);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                log.error("扫描业务系统生成催办工单失败");
            }
        }

    }

    private void toBmp(String rwmc, String rwclnr, Map<String, Object> biz) {
        String username = biz.get("business_concat_account").toString();
        String bizsystem = biz.get("bizSystem").toString();
        String  token = bpmTokenHelper.getUserToken("admin");
        if (StringUtils.isEmpty(token)) {
            log.error("获取BPM token异常 token 为空");
            return;
        }
        // 如果已存在业务上线工单则忽略
        if ("true".equals(checkTaskExit(token, bizsystem))) {
            return;
        }
        // 催办工单
        createUrgeTask(rwmc, rwclnr, token, username, bizsystem);
    }

    private String checkTaskExit(String token, String bizsystem) {
        log.info("开始查询bpm是存在[{}]上线工单",bizsystem);
        Map<String, String>  params =  new HashMap<>();
        params.put("sysName", bizsystem);
        String result = bpmTokenHelper.doGet(token, bpmTokenHelper.getBPM_ONLINE_BIZ_Url(), params);
        JSONObject res = JSONObject.parseObject(result);
        log.info("查询bpm是存在[{}]上线工单, 返回数据：{}",bizsystem, result);
        return res.get("value").toString();
    }

    private void createUrgeTask1(String rwmc, String rwclnr, String token, String username, String bizsystem) {
        JSONObject  params =  new JSONObject();
        Map<Object,Object> data = new HashMap<Object, Object>();
        Map<Object,Object> form = new HashMap<Object, Object>();
        form.put("rwlx", "1");
        form.put("rwclxgfj", "");
        form.put("zyc", "");
        form.put("rwclnr", bizsystem + rwclnr);
        form.put("rwnr", "");
        form.put("rwmc", rwmc);
        data.put(bpmTokenHelper.getDATAKEY(), form);
        String  formData = JSON.toJSONString(data);
        byte[] dataByte = org.apache.commons.codec.binary.Base64.encodeBase64(formData.getBytes());
        params.put("data", new String(dataByte));
        params.put("account", username);
        params.put("flowKey", bpmTokenHelper.getFLOWKEY());
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Authorization", "Bearer "+token);
        requestHeaders.add("Content-Type", "application/json;charset=utf-8");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity(params,
                requestHeaders);
        RestTemplate restTemplate = new RestTemplate();
        log.info("<<<<<< 开始发起bpm业务系统催办工单 <<<<<<"+bpmTokenHelper.getBUSINESSSYSTEMCHANGETOBPMURL());
        ResponseEntity<String> result = restTemplate.exchange(bpmTokenHelper.getBUSINESSSYSTEMCHANGETOBPMURL(), HttpMethod.POST, requestEntity,String.class);
        if(result.getStatusCodeValue() == 200) {
            log.info("<<<<<< 发起bpm业务系统催办工单完成 <<<<<<");
        } else{
            log.error("<<<<<< 发起bpm业务系统催办工单失败 <<<<<<"+result.getBody());
        }
    }


    private void createUrgeTask(String rwmc, String rwclnr, String token, String username, String bizsystem) {
        Map<String, String> params =  new HashMap<>();
        Map<Object,Object> data = new HashMap<Object, Object>();
        Map<Object,Object> form = new HashMap<Object, Object>();
        form.put("rwlx", "1");
        form.put("rwclxgfj", "");
        form.put("zyc", "");
        form.put("rwclnr", bizsystem + rwclnr);
        form.put("rwnr", "");
        form.put("rwmc", rwmc);
        form.put("clrID", username);
        data.put(bpmTokenHelper.getDATAKEY(), form);
        String  formData = JSON.toJSONString(data);
        byte[] dataByte = org.apache.commons.codec.binary.Base64.encodeBase64(formData.getBytes());
        params.put("data", new String(dataByte));
        params.put("account", username);
        params.put("flowKey", bpmTokenHelper.getFLOWKEY());
        final CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = bpmTokenHelper.getBUSINESSSYSTEMCHANGETOBPMURL();
        try {
            URIBuilder builder = new URIBuilder(url);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
            URI uri = builder.build();
            HttpPost httpPost = new HttpPost(uri);
            if (StringUtils.isNotEmpty(token)) {
                httpPost.setHeader("Authorization", "Bearer "+token);
                httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
            }
            httpPost.setEntity(new StringEntity(JSON.toJSONString(params)));
            final HttpResponse httpResponse = httpClient.execute(httpPost);
            org.apache.http.HttpEntity responseEntity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            log.info("GET Response Status: {}", statusCode);
            if (statusCode == 200) {
                log.info("<<<<<< 发起bpm业务系统催办工单完成 <<<<<<");
            } else{
                log.error("<<<<<< 发起bpm业务系统催办工单失败 <<<<<< message:{}" , httpResponse.getEntity());
            }
        } catch (Exception e) {
            log.error("send request error URL: " + url, e);
        }
        finally {
            IOUtils.closeQuietly(httpClient);
        }
    }
}
