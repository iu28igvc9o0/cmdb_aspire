package com.aspire.cmdb.agent.util;

import com.alibaba.fastjson.JSON;
import com.aspire.ums.cmdb.cmic.service.CmdbModuleKafkaEventService;
import com.aspire.ums.cmdb.helper.BpmTokenHelper;
import com.aspire.ums.cmdb.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: BPMUtil
 * Author:   hangfang
 * Date:     2020/12/7
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class BPMUtil {

    @Autowired
    private BpmTokenHelper bpmTokenHelper;

    private BpmTokenHelper getBpmTokenHelper() {
        if (this.bpmTokenHelper == null) {
            this.bpmTokenHelper = SpringUtils.getBean(BpmTokenHelper.class);
        }
        return this.bpmTokenHelper;
    }
    /**form
     * { "rwlx":"1",
     *   "rwclxgfj":"",
     *   "zyc":"",
     *   "rwclnr":"处理内容",
     *   "rwnr":"",
     *   "rwmc":"任务名称"，
     *   "clrID":"处理人账号"
     *     }
     *
     * */
    public void createTask(String url, String username, Map<String,Object> taskForm) {
        Map<String, String> params =  new HashMap<>();
        Map<Object,Object> data = new HashMap<Object, Object>();
        String  token = getBpmTokenHelper().getUserToken("admin");
        data.put(getBpmTokenHelper().getDATAKEY(), taskForm);
        String  formData = JSON.toJSONString(data);
        byte[] dataByte = org.apache.commons.codec.binary.Base64.encodeBase64(formData.getBytes());
        params.put("data", new String(dataByte));
        params.put("account", username);
        params.put("flowKey", getBpmTokenHelper().getFLOWKEY());
        final CloseableHttpClient httpClient = HttpClients.createDefault();
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
                log.info("<<<<<< 发起工单完成 <<<<<<");
            } else{
                log.error("<<<<<< 发起单失败 <<<<<< message:{}" , httpResponse.getEntity());
            }
        } catch (Exception e) {
            log.error("send request error URL: " + url, e);
        }
        finally {
            IOUtils.closeQuietly(httpClient);
        }
    }


}
