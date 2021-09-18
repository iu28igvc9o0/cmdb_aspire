package com.aspire.ums.cmdb.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-08-27 10:56
 * @description
 */
@Component
public class RestTemplateUtils {

    @Autowired
    private RestTemplate restTemplate;

    public String postJson(String url, String json, Map<String, String> header) {
        HttpHeaders headers = buildCommonHeaders(header);
        HttpEntity<String> request = new HttpEntity<>(json,headers);
        try {
            return restTemplate.postForObject(url, request, String.class);
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new RestClientException("restTemplate post exception", e);
        }
    }

    private static HttpHeaders buildCommonHeaders(Map<String, String> header) {
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        if (header != null) {
            for (Map.Entry<String, String> m : header.entrySet()) {
                headers.add(m.getKey(), m.getValue());
            }
        }
        return headers;
    }

    public static Map<String, String> buildAutomateHeaders() {
        Map<String, String> retMap = new HashMap<>();
        retMap.put("Content-Type","application/json");
        retMap.put("host","tool.easyops-only.com");
        retMap.put("user","easyops");
        retMap.put("org","130667");
        return retMap;
    }

    /**
     * {
     *  "inputs": {
     *   "@agents": [{
     *    "ip": "172.16.108.101"
     *   }],
     *   "ip": "10.2.0.249",
     *   "gateway": "10.2.224.6",
     *   "resource": "呼和浩特资源池"
     *  },
     *  "toolId": "42a2e8119fe6ce245d6c2419b31e9462",
     *  "execUser": "ampmon",
     *  "vId": "e2eb78a1782b9eab867393275528ea8d"
     * }
     * @return
     */
    public static String buildAutomateBody(Map<String,String> inputParam,String idcIp,String vid) {
        Map<String,Object> paramMap = new HashMap<>();
        // 1.第三层，inputs -> @agents -> ip
        Map<String,String> param3 = new HashMap<>();
        param3.put("ip",idcIp);
        List<Map<String,String>> list3 = new ArrayList<>();
        list3.add(param3);
        // 2.第二层，inputs -> @agents
        Map<String,Object> param2 = new HashMap<>();
        param2.put("@agents",list3);
        if (null != inputParam) {
            param2.putAll(inputParam);
        }
        // 3.第一层，inputs
        paramMap.put("inputs",param2);
        paramMap.put("toolId","42a2e8119fe6ce245d6c2419b31e9462");
//        paramMap.put("execUser","ampmon");
//        paramMap.put("vId","e2eb78a1782b9eab867393275528ea8d");
        paramMap.put("vId",vid);
        return JSONObject.toJSONString(paramMap);
    }
}
