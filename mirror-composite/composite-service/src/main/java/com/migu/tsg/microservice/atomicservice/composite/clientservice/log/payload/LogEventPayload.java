package com.migu.tsg.microservice.atomicservice.composite.clientservice.log.payload;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
* ErrorsResponse类
* Project Name:composite-service
* File Name:LogEventPayload.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.clientService.log.payload
* ClassName: LogEventPayload <br/>
* date: 2017年9月1日 下午1:39:01 <br/>
* 日志事件方法类
* @author pengguihua
* @version
* @since JDK 1.6
*/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogEventPayload {
    private static final Logger LOG = LoggerFactory.getLogger(LogEventPayload.class);

    @JsonProperty("resource_name")
    private String resourceName;

    @JsonProperty("log_level")
    private Integer logLevel;

    @JsonProperty("resource_id")
    private String resourceId;

    @JsonProperty("created_at")
    @JSONField(name = "created_at")
    private String createTime;

    private Long time;

    @JsonProperty("template_id")
    private String templateId;

    @JsonProperty("resource_type")
    private String resourceType;

    private String namespace;

    @JsonProperty("detail")
    @JSONField(name = "detail")
    private Map<String, Object> detailMap;
    
    /**
    * parse2Json:(转成json数组的字符串表示). <br/>
    *
    * 作者： pengguihua
    * @param logEvents
    * @return
    */
    public static String parse2Json(final List<LogEventPayload> logEvents) {
        final ObjectMapper mapper = new ObjectMapper();
        
        try {
            return mapper.writeValueAsString(logEvents);
        } catch (JsonProcessingException e) {
            LOG.error(null, e);
        }
        return null;
    }
}
