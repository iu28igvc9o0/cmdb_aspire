package com.migu.tsg.microservice.atomicservice.composite.controller.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.log.payload.LogEventPayload;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext.RequestHeadUser;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestConstraintEnum;
import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LastLogCodeEnum;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.CompositeResource;
import com.migu.tsg.microservice.atomicservice.composite.exception.BaseException;
import com.migu.tsg.microservice.atomicservice.composite.exception.ResultErrorEnum;


/**
* 日志model工具类
* Project Name:composite-service2
* File Name:LogEventUtil.java
* Package Name:com.migu.tsg.microservice.atomicservice.composite.controller.util
* ClassName: LogEventUtil <br/>
* date: 2017年8月31日 下午1:02:25 <br/>
* 日志model工具类
* @author pengguihua
* @version
* @since JDK 1.6
*/
public class LogEventUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogEventUtil.class);
    
    private final static int EVENT_LEVEL_TRIVIAL = 0;
    private final static int EVENT_LEVEL_ATTENTION = 1;
    private final static int EVENT_LEVEL_IMPORTANT = 2;
    
    private static final Map<String, Integer> OPERATION_LEVEL_MAPPING = new HashMap<>();
    private static final Map<String, Integer> STATUS_LEVEL_MAPPING = new HashMap<>();
    
    static {
        OPERATION_LEVEL_MAPPING.put("add", EVENT_LEVEL_TRIVIAL);
        OPERATION_LEVEL_MAPPING.put("create", EVENT_LEVEL_TRIVIAL);
        OPERATION_LEVEL_MAPPING.put("delete", EVENT_LEVEL_TRIVIAL);
        OPERATION_LEVEL_MAPPING.put("update", EVENT_LEVEL_ATTENTION);
        OPERATION_LEVEL_MAPPING.put("destroy", EVENT_LEVEL_ATTENTION);
        OPERATION_LEVEL_MAPPING.put("start", EVENT_LEVEL_TRIVIAL);
        OPERATION_LEVEL_MAPPING.put("stop", EVENT_LEVEL_ATTENTION);
        OPERATION_LEVEL_MAPPING.put("destroy_build", EVENT_LEVEL_ATTENTION);
        OPERATION_LEVEL_MAPPING.put("added", EVENT_LEVEL_TRIVIAL);
        OPERATION_LEVEL_MAPPING.put("left", EVENT_LEVEL_TRIVIAL);
        OPERATION_LEVEL_MAPPING.put("permission_grant", EVENT_LEVEL_TRIVIAL);
        OPERATION_LEVEL_MAPPING.put("permission_revoke", EVENT_LEVEL_TRIVIAL);
        OPERATION_LEVEL_MAPPING.put("remove", EVENT_LEVEL_ATTENTION);
        OPERATION_LEVEL_MAPPING.put("upgrade", EVENT_LEVEL_ATTENTION);
        OPERATION_LEVEL_MAPPING.put("trigger", EVENT_LEVEL_TRIVIAL);
        OPERATION_LEVEL_MAPPING.put("retry", EVENT_LEVEL_ATTENTION);
        OPERATION_LEVEL_MAPPING.put("is_service_ready", EVENT_LEVEL_ATTENTION);
        
        OPERATION_LEVEL_MAPPING.put("start", EVENT_LEVEL_TRIVIAL);
        OPERATION_LEVEL_MAPPING.put("stopped", EVENT_LEVEL_ATTENTION);
        OPERATION_LEVEL_MAPPING.put("error", EVENT_LEVEL_IMPORTANT);
        OPERATION_LEVEL_MAPPING.put("succeeded", EVENT_LEVEL_TRIVIAL);
        OPERATION_LEVEL_MAPPING.put("failed", EVENT_LEVEL_ATTENTION);
        OPERATION_LEVEL_MAPPING.put("complete", EVENT_LEVEL_TRIVIAL);
        OPERATION_LEVEL_MAPPING.put("failure", EVENT_LEVEL_IMPORTANT);
    }
    

    /**
    * 构造基本日志请求对象，基本日志对象会填充以下属性：
    *   resourceId
    *   resourceType
    *   resourceName
    *   createTime
    *   time
    *   templdateId
    *   logLevel
    *   namespace
    *   operator
    *   operation
    *   detail
    *
    * .<br/>
    *
    * 作者： pengguihua
    * @param authCtx
    * @param compRes
    * @param operation
    * @param logLevel
    * @param templateId
    * @return
    */
    public static LogEventPayload buildBasicLogEvent(
            RequestAuthContext authCtx, String logResType, String logResUuid, String logResName,
            String operation, int logLevel, String templateId,CompositeResource composite) {
        RequestHeadUser currUser = authCtx.getUser();
        LogEventPayload logEvent = new LogEventPayload();
        logResType = logResType == null ? authCtx.getResType() : logResType;
        logEvent.setResourceType(logResType);
        logEvent.setResourceId(logResUuid);
        logEvent.setResourceName(logResName);
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSS'Z'");
        String formatTime = simpleDateFormat.format(now);
        logEvent.setCreateTime(formatTime);
        logEvent.setTime(now.getTime() * 1000);
        logEvent.setTemplateId(templateId);
        logEvent.setLogLevel(mappingOperationToLogLevel(operation));
        logEvent.setNamespace(currUser.getNamespace());
        Map<String, Object> detailMap = new HashMap<String, Object>();
        detailMap.put("operator", currUser.getUsername());
        operation = operation == null ? authCtx.getRawAction() : operation;
        detailMap.put("operation", operation);
        logEvent.setDetailMap(detailMap);
        String projectConst = authCtx.getRawConstraints().get(RequestConstraintEnum.project_name.name());
        if (StringUtils.isNotEmpty(projectConst)) {
            detailMap.put("project_name", projectConst);
        }
        return logEvent;
    }
    
    public static LogEventPayload buildBasicLogEvent(String namespace, String username,
            String logResType, String logResUuid, String logResName,
            String operation, int logLevel, String templateId, Map<String, Object> detailMap) {
        LogEventPayload logEvent = new LogEventPayload();
        logEvent.setResourceType(logResType);
        logEvent.setResourceId(logResUuid);
        logEvent.setResourceName(logResName);
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSS'Z'");
        String formatTime = simpleDateFormat.format(now);
        logEvent.setCreateTime(formatTime);
        logEvent.setTime(now.getTime() * 1000);
        logEvent.setTemplateId(templateId);
        logEvent.setLogLevel(mappingOperationToLogLevel(operation));
        logEvent.setNamespace(namespace);
        detailMap.put("operator", username);
        detailMap.put("operation", operation);
        logEvent.setDetailMap(detailMap);
        return logEvent;
    }

    /**
    * 为detail的Map中添加键值对.<br/>
    *
    * 作者： pengguihua
    * @param logEvent
    * @param mapKey
    * @param subKey
    * @param subVal
    * @return
    */
    public static LogEventPayload popupDetail(final LogEventPayload logEvent, String attrName, Object attrVal) {
        if (logEvent == null) {
            return null;
        }
        if (null == attrVal) {
            return logEvent;
        }
        Map<String, Object> detailMap = logEvent.getDetailMap();
        if (detailMap == null) {
            detailMap = new HashMap<String, Object>();
            logEvent.setDetailMap(detailMap);
        }
        detailMap.put(attrName, PayloadParseUtil.jacksonBase2JsonStr(attrVal));
        LOGGER.info("进入事件额外属性添加方法");
        return logEvent;
    }
    
    public static LogEventPayload popupParent(final LogEventPayload logEvent, String attrName, Object attrVal) {
        if (logEvent == null) {
            return null;
        }
        Map<String, Object> detailMap = logEvent.getDetailMap();
        if (detailMap == null) {
            detailMap = new HashMap<String, Object>();
            logEvent.setDetailMap(detailMap);
        }
        detailMap.put(attrName, attrVal);
        return logEvent;
    }

    /**
    * 日志detail中添加Map的键值对.<br/>
    *
    * {
    *   mapKey: {
    *       subKey1: subVal1,
    *       subKey2: subVal2,
    *       subKey3: subVal3
    *   }
    * }
    *
    * 作者： pengguihua
    * @param mapKey
    * @param subKey
    * @param sub
    * @return
    */
    @SuppressWarnings("unchecked")
    public static LogEventPayload popupDetailInnerMap(final LogEventPayload logEvent,
            String mapKey, String subKey, Object subVal) {
        if (logEvent == null) {
            return null;
        }
        Map<String, Object> detailMap = logEvent.getDetailMap();
        if (detailMap == null) {
            detailMap = new HashMap<String, Object>();
            logEvent.setDetailMap(detailMap);
        }

        Map<String, Object> innerKeyMap = (Map<String, Object>) detailMap.get(mapKey);
        if (innerKeyMap == null) {
            innerKeyMap = new HashMap<String, Object>();
            detailMap.put(mapKey, innerKeyMap);
        }

        innerKeyMap.put(subKey, subVal);
        return logEvent;
    }

    /**
    * 把一个或多个LogEventPayload包装成List中,并通过JACKSON转成JSON字符串.<br/>
    *
    * 作者： pengguihua
    * @param eventArr
    * @return
    */
    public static String wrapLogEvents2Json(LogEventPayload... eventArr) {
        List<LogEventPayload> asList = Arrays.asList(eventArr);
        return wrapLogEvents2Json(asList);
    }
    
    public static String wrapLogEvents2Json(List<LogEventPayload> logList) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(logList);
        } catch (JsonProcessingException e) {
            LOGGER.error(null, e);
            throw new BaseException(LastLogCodeEnum.GENERAL_ERROR, ResultErrorEnum.BIZ_OBJECT_PARSE_JSON_FAIL);
        }
    }
    
    /**
     * 查询日志级别
     * mappingOperationToLogLevel:(这里用一句话描述这个方法的作用). <br/>
     * 作者： baiwp
     * @param operation
     * @return
     */
    private static int mappingOperationToLogLevel (String operation) {
        operation = operation.toLowerCase(Locale.getDefault());
        if (OPERATION_LEVEL_MAPPING.containsKey(operation)) {
            return OPERATION_LEVEL_MAPPING.get(operation);
        }
        return 0;
               
    }
}
