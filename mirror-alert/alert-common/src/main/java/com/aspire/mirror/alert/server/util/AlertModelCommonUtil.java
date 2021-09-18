package com.aspire.mirror.alert.server.util;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.util
 * @Author: baiwenping
 * @CreateTime: 2020-03-16 14:45
 * @Description: ${Description}
 */
public class AlertModelCommonUtil {

    /**
    * 获取存取数据的map
    * @auther baiwenping
    * @Description
    * @Date 14:46 2020/3/16
    * @Param [alertJson, alertFieldList]
    * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
    public static  <T> Map<String, Object> generateAlerts(T alertJson, List<AlertFieldVo> alertFieldList) {
        JSONObject json = null;
        if (alertJson instanceof JSONObject) {
            json = (JSONObject) alertJson;
        } else {
            String jsonString = "{}";
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                jsonString = objectMapper.writeValueAsString(alertJson);
            } catch (JsonProcessingException e) {
            }
            json = JSONObject.parseObject(jsonString);
        }
        Map<String, Object> map = Maps.newHashMap();
        for (AlertFieldVo alertFieldVo : alertFieldList) {
            String fieldCode = alertFieldVo.getFieldCode();
            for (Map.Entry<String, Object> entry: json.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (key.equalsIgnoreCase(fieldCode) && value != null) {
                    map.put(fieldCode, value);
                    break;
                }
            }
        }
        return map;
    }
}
