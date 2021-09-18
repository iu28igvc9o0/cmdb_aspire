package com.aspire.ums.cmdb.v2.restful.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.cmic.util.EventThreadUtils;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.restful.payload.StatisticRequestEntity;
import com.aspire.ums.cmdb.sqlManage.service.CmdbSqlManageService;
import com.aspire.ums.cmdb.util.IpUtils;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceService;
import com.aspire.ums.cmdb.v2.instance.service.ICmdbInstanceV3Service;
import com.aspire.ums.cmdb.v2.restful.service.ICommonRestfulService;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3AccessUser;
import com.aspire.ums.cmdb.v3.condication.service.ICmdbV3AccessUserService;
import com.aspire.ums.cmdb.v3.config.payload.CmdbConfig;
import com.aspire.ums.cmdb.v3.config.service.ICmdbConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CommonRestfulServiceImpl
 * Author:   zhu.juwang
 * Date:     2020/3/13 11:22
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Service
@Slf4j
public class CommonRestfulServiceImpl implements ICommonRestfulService {

    @Autowired
    private ICmdbInstanceService instanceService;
    @Autowired
    private ICmdbInstanceV3Service instanceV3Service;
    @Autowired
    private CmdbSqlManageService sqlManageService;
    @Autowired
    private ICmdbConfigService configService;
    @Autowired
    private ICmdbV3AccessUserService userService;

    @Override
    public Result<Map<String, Object>> getInstanceList(Map<String, Object> params) {
        Long startTime = new Date().getTime();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
//        HttpServletResponse response = servletRequestAttributes.getResponse();
        Map<String, String> requestInfo = toGetRequestInfoFromRequest(request);
        Result<Map<String, Object>> result = null;
        String exceptionInfo = null;
        try {
           result = instanceService.getInstanceList(params, null);
//            result = instanceV3Service.tempQueryInstanceList(params);
            requestInfo.put("response_status", "返回成功");
        } catch (Exception e) {
            exceptionInfo = e.getMessage();
            requestInfo.put("response_status", "返回失败");
            throw new RuntimeException("请求失败！error：" + exceptionInfo);
        } finally {
            requestInfo.put("response_describe", exceptionInfo);
            Integer count = result == null ? 0 : result.getData().size();
            EventThreadUtils.FIXED_POOL.execute(() -> {
                toCreateRequestLog(startTime, requestInfo, params, count);
            });
        }
        return result;
    }

    private Map<String, String> toGetRequestInfoFromRequest(HttpServletRequest request) {
        Map<String, String> info = new HashMap<>();
        String ip = null;
        try {
            ip = IpUtils.getIpAddress(request);
            if (StringUtils.isEmpty(ip)) {
                throw new RuntimeException("获取请求ip失败");
            }
        } catch (UnknownHostException e) {
            log.error("获取请求ip失败! error:{}", e.getMessage());
        }
        info.put("ip", ip);
        info.put("url", request.getRequestURL().toString());
        return info;
    }

    private void toCreateRequestLog(Long startTime, Map<String, String> requestInfo, Map<String, Object> params, Integer resultCount) {
        try {
            CmdbV3AccessUser user = new CmdbV3AccessUser();
            if (params.containsKey("token")) {
                user = userService.getUserByToken(params.get("token").toString());
            }
            boolean needRecord = true;
            //获取日志模型id
            CmdbConfig unNeedLog = configService.getConfigByCode("exclude_record_request_log");
            if (unNeedLog != null && StringUtils.isNotEmpty(unNeedLog.getConfigValue())) {
                List<String> unNeedLogUserList = Arrays.asList(unNeedLog.getConfigValue().split(","));
                if (unNeedLogUserList.contains(user.getUserName())) {
                    needRecord = false;
                }
            }
            if (needRecord) {
                Map<String, Object> instanceData = new HashMap<>();
                //获取日志模型id
                CmdbConfig cmdbConfig = configService.getConfigByCode("cmdb_restful_log");
                instanceData.put("module_id", cmdbConfig.getConfigValue());
                instanceData.put("insert_person", user.getDisplayName());
                instanceData.put("request_params", params);
                instanceData.put("response_time", new Date().getTime() - startTime);
                instanceData.put("response_ip", requestInfo.get("ip"));
                instanceData.put("response_count", resultCount);
                instanceData.put("request_url",  requestInfo.get("url"));
                instanceData.put("response_status", requestInfo.get("response_status"));
                instanceData.put("response_describe", requestInfo.get("response_describe"));
                instanceService.addInstance(user.getUserName(), instanceData, "第三方请求");
            }
        } catch (Exception e) {
            log.error("error: {}", e.getStackTrace());
            log.error("记录请求日志失败！error: {}" , e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getInstanceDetail(Map<String, Object> params) {
        if (!params.containsKey("condicationCode")) {
            params.put("condicationCode", "instance_detail");
        }
        Long startTime = new Date().getTime();
        Map<String, Object> resultMap = new HashMap<>();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Map<String, String> requestInfo = toGetRequestInfoFromRequest(request);
        String exceptionInfo = null;
        try {
            resultMap = instanceService.queryInstanceDetail(params, null);
            requestInfo.put("response_status", "返回成功");
        } catch (Exception e) {
            exceptionInfo = e.getMessage();
            requestInfo.put("response_status", "返回失败");
            throw new RuntimeException("请求失败！error：" + exceptionInfo);
        } finally {
            requestInfo.put("response_describe", exceptionInfo);
            Integer count = resultMap.keySet().size() > 0 ? 1 : 0;
            EventThreadUtils.FIXED_POOL.execute(() -> {
                toCreateRequestLog(startTime, requestInfo, params, count);
            });
        }
        return resultMap;
    }

    @Override
    public Object getInstanceStatistics(StatisticRequestEntity entity) {
        Long startTime = new Date().getTime();
        if (StringUtils.isEmpty(entity.getName())) {
            throw new RuntimeException("Require query params.name is missing. Query failed.");
        }
        if (StringUtils.isEmpty(entity.getResponseType())) {
            entity.setResponseType("list");
//            throw new RuntimeException("Require query params.responseType is missing. Query failed.");
        }
        if (!Arrays.asList("map", "list").contains(entity.getResponseType())) {
            throw new RuntimeException("Don't support response type [" + entity.getResponseType() + "]. Query failed.");
        }
        List resultList = null;
        Map resultMap = null;

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Map<String, String> requestInfo = toGetRequestInfoFromRequest(request);
        String exceptionInfo = null;
        try {
            if (entity.getResponseType().toLowerCase().equals("list")) {
                resultList = sqlManageService.queryList(entity.getName(), entity.getParams());
            } else {
                resultMap = sqlManageService.queryMap(entity.getName(), entity.getParams());
            }
            requestInfo.put("response_status", "返回成功");
        } catch (Exception e) {
            exceptionInfo = e.getMessage();
            requestInfo.put("response_status", "返回失败");
            throw new RuntimeException("请求失败！error：" + exceptionInfo);
        } finally {
            requestInfo.put("response_describe", exceptionInfo);
            Integer count = resultList == null ? resultMap != null && resultMap.keySet().size() > 0 ? 1 : 0 : resultList.size();
            EventThreadUtils.FIXED_POOL.execute(() -> {
                toCreateRequestLog(startTime, requestInfo, JSONObject.parseObject(JSON.toJSONString(entity)), count);
            });
        }
        return resultList == null ? resultMap : resultList;

    }

    @Override
    public Result<Map<String, Object>> getInstanceListByName(Map<String, Object> params) {
        return null;
    }
}
