package com.migu.tsg.microservice.atomicservice.composite.controller.util.es;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.reflect.TypeToken;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.constant.Enviroment;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.constant.EventConstants;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.constant.LogConstant;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.dto.LogQueryRequest;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.dto.LogTagListRequest;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.engine.EsDslJsonBuilder;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.engine.EsIndexManager;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.engine.EsQueryBuilder;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.service.EsHandleService;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.util.DateUtils;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.util.Pagenation;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.dto.BucketInfoDTO;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.dto.LogInfoDTO;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.payload.LogChartListResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.payload.LogListResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.payload.LogTagListResponse;


/**
 * 日志索引，检索能力(ES)接口 项目名称: 微服务运维平台（monitor-service模块） 包:
 * com.migu.tsg.microservice.atomicservice.monitor.log.biz.impl 类名称:
 * LogInfoBizImpl.java 类描述: 提供日志索引，检索能力(ES) 创建人: sunke 创建时间: 2017年7月26日
 * 下午9:07:00
 */
@Service
public class LogInfoBizImpl {

    private static final Logger LOG = LoggerFactory.getLogger(LogInfoBizImpl.class);
    
    private static final long TIME_MS_BUILDER = 1000;
    private static final String STR_DELIMITE_PARAM = ","; 

    @Autowired
    private EsHandleService service;

    public final int count(final LogQueryRequest request, String regionId) {
        int count = 0;
        try {
            final EsQueryBuilder dslBuilder = initEsQueryBuilder();
            queryRequest2EsBuilder(request, dslBuilder);
            count = service.count(EsDslJsonBuilder.count(dslBuilder),
                    EsIndexManager.getMultiLogIndex(request.getStartTime(),request.getEndTime(), null),
                    EsIndexManager.getLogType(), regionId);
        } catch(Exception e) {
            LOG.error("biz log count error:{}", e);
        }
        return count;
    }

    public final List<LogInfoDTO> search(final LogQueryRequest request, String regionId) {
        List<LogInfoDTO> logs = null;
        try {
            final EsQueryBuilder dslBuilder = initEsQueryBuilder();
            queryRequest2EsBuilder(request, dslBuilder);
            dslBuilder.setFrom(0);
            dslBuilder.setSize(Integer.parseInt(request.getSize()));
            logs = service.list(EsDslJsonBuilder.list(dslBuilder),
                    EsIndexManager.getMultiLogIndex(request.getStartTime(),request.getEndTime(), null),
                    EsIndexManager.getLogType(), new TypeToken<List<LogInfoDTO>>() {
                    }.getType(), regionId);
            for(LogInfoDTO e : logs){
                e.setTime(e.getTime().substring(0,10)+"."+e.getTime().substring(10+1));
            }
        } catch (Exception e) {
            LOG.error("sys log list is error:{}", e);
        }
        return logs;
    }

    @SuppressWarnings("unchecked")
    public final LogListResponse list(final LogQueryRequest request, String regionId) {

        final LogListResponse response = new LogListResponse();
        try {
            final EsQueryBuilder dslBuilder = initEsQueryBuilder();
            queryRequest2EsBuilder(request, dslBuilder);
            final int count = service.count(EsDslJsonBuilder.count(dslBuilder), 
                    EsIndexManager.getMultiLogIndex(request.getStartTime(),request.getEndTime(), null),
                    EsIndexManager.getLogType(), regionId);

            if(count == 0){
            	return response;
            }
            
            int size = EventConstants.DEFUALT_PAGE_SIZE;
            if (!StringUtils.isEmpty(request.getSize())) {
                size = Integer.parseInt(request.getSize());
            }
            int page = EventConstants.DEFAULT_PAGE;
            if (!StringUtils.isEmpty(request.getPageNo())) {
                page = Integer.parseInt(request.getPageNo());
            }
            // 构建分页查询
            final Pagenation pagenation = new Pagenation(size, page, count);
            response.setTotalItems(String.valueOf(pagenation.getRowCount()));
            response.setTotalPage(String.valueOf(pagenation.getPageCount()));
            dslBuilder.setFrom(pagenation.getStartRow());
            if (count - pagenation.getStartRow() < size) {
                dslBuilder.setSize(count - pagenation.getStartRow());
            } else {
                dslBuilder.setSize(pagenation.getPageSize());
            }
            
            final List<LogInfoDTO> logs = service.list(EsDslJsonBuilder.list(dslBuilder),
                    EsIndexManager.getMultiLogIndex(request.getStartTime(),request.getEndTime(), null),
                    EsIndexManager.getLogType(), new TypeToken<List<LogInfoDTO>>() {
                    }.getType(), regionId);
            for(LogInfoDTO e : logs){
                e.setTime(e.getTime().substring(0,10)+"."+e.getTime().substring(10+1));
            }
            response.setLogs(logs);
            
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("response for list:{}");// response.toString());
        return response;
    }

    public final LogTagListResponse listTags(final LogTagListRequest request, String regionId) {
    	LOG.info("LogInfoBizImpl listTags request:{}", request.toString());
        final LogTagListResponse response = new LogTagListResponse();
        try {
            //  1.后续需要分别中mysql和es中获取数据
            //  2.目前namespace 不能用，后续通过jakrio使用namespace鉴权
            final EsQueryBuilder dslBuilder = initEsQueryBuilder();
//            final List<String> clusters = distinct(dslBuilder, LogConstant.COL_REGION_NAME);
//            final List<String> services = distinct(dslBuilder, LogConstant.COL_SERVICE_NAME);
            final List<String> nodes = distinct(dslBuilder, LogConstant.COL_MACHINE, regionId);
            final List<String> paths = distinct(dslBuilder, LogConstant.COL_PATHS, regionId);
//            response.setClusters(clusters);
//            response.setServices(services);
            response.setNodes(nodes);
            response.setPaths(paths);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("LogInfoBizImpl listTags response for listTags:{}", response.toString());
        return response;
    }

    private List<String> distinct(final EsQueryBuilder dslBuilder, final String colsName, String regionId) {
        dslBuilder.setGroupColsName(colsName);
        //es5.6需要先开启fielddata
        return service.group(EsDslJsonBuilder.group(dslBuilder), EsIndexManager.getIndex(),
                EsIndexManager.getLogType(), regionId);
    }

    public final LogChartListResponse listChart(final LogQueryRequest request, String regionId) {
        final LogChartListResponse response = new LogChartListResponse();
        try {
            final EsQueryBuilder dslBuilder = initEsQueryBuilder();
            dslBuilder.setGroupColsName(LogConstant.COL_LOG_TIME);
            queryRequest2EsBuilder(request, dslBuilder);
            // 计算直方图的间隔
            dslBuilder.setInterval(getInterval(request.getStartTime(), request.getEndTime()));
            LOG.info("listChart getIndex={}, getLogType()={}", 
            		EsIndexManager.getIndex(), EsIndexManager.getLogType());
            final List<BucketInfoDTO> list = service.histogram(EsDslJsonBuilder.histogram(dslBuilder),
                    EsIndexManager.getMultiLogIndex(request.getStartTime(),
                            request.getEndTime(), null), EsIndexManager.getLogType(), regionId);
            response.setBuckets(list);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        LOG.info("response for listChart:{}"/*, response.toString()*/);
        return response;
    }

    /**
     * 计算直方图间隔
     * @param start
     *            开始时间
     * @param end
     *            结束时间
     * @return 返回直方图间隔时间,单位秒
     */
    private String getInterval(long start, long end) {
        if (start == 0) {
            start = DateUtils.getDate(new Date(), 0, 0, LogConstant.DEFAULT_START_DATE, 0, 0, 0).getTime();
        }
        if (end == 0) {
            end = new Date().getTime();
        }
        final long interval = (end - start)  / (LogConstant.HISTOGRAM_COUNT);
        return (interval) + "ms";
    }
    /**
     * 转换请求参数
     * @param request
     *            请求参数
     * @param dslBuilder
     *            dsl builder构建对象
     */
    private void queryRequest2EsBuilder(final LogQueryRequest request, final EsQueryBuilder dslBuilder) {
        final long startTime = request.getStartTime();
        final long endTime = request.getEndTime();
        /*final String logType = request.getPaths();*/
        final String clusters = request.getClusters();
        final String services = request.getServices();
        final String nodes = request.getNodes();
        final String instance = request.getInstance();
        final String paths = request.getPaths();
		final String appId = request.getAppId();
		final String queryString = request.getQueryString();
        
        LOG.info("queryRequest2EsBuilder request:{}， {}, dslBuilder{}, {}", 
        		startTime, endTime, dslBuilder.getStartTime(), dslBuilder.getEndTime());
        
        if (startTime > 0) {
            dslBuilder.setStartTime(startTime);
        }
        if (endTime > 0) {
            dslBuilder.setEndTime(endTime);
        }
        
        final Map<String, List<String>> paramsMultiple = new ConcurrentHashMap<String, List<String>>();
        final Map<String, List<String>> paramsShould = new ConcurrentHashMap<String, List<String>>();
        final Map<String, String> paramsQuery = new ConcurrentHashMap<String, String>();
        
        /*if (!StringUtils.isEmpty(namespace)) {
            params.put(LogConstant.COL_LOG_NAMESPACE, namespace);
        }*/
        /*if (!StringUtils.isEmpty(logType)) {
            params.put(LogConstant.COL_LOG_TYPE, logType);
        }*/
        if (!StringUtils.isEmpty(clusters)) {   
        	paramsMultiple.put(LogConstant.COL_REGION_NAME, 
    				Arrays.asList(clusters.split(STR_DELIMITE_PARAM)));
        }
        if (!StringUtils.isEmpty(services)) {
        	paramsMultiple.put(LogConstant.COL_SERVICE_NAME, 
    				Arrays.asList(services.split(STR_DELIMITE_PARAM)));
        }
        if (!StringUtils.isEmpty(instance)) {
        	paramsShould.put(LogConstant.COL_INSTANCE_ID8, 
    				Arrays.asList(instance.split(STR_DELIMITE_PARAM)));
        }
        if (!StringUtils.isEmpty(nodes)) {
        	paramsMultiple.put(LogConstant.COL_MACHINE, 
    				Arrays.asList(nodes.split(STR_DELIMITE_PARAM)));
        }
        if (!StringUtils.isEmpty(paths)) {
        	paramsMultiple.put(LogConstant.COL_PATHS, 
    				Arrays.asList(paths.split(STR_DELIMITE_PARAM)));       	
        }
		if (!StringUtils.isEmpty(appId)) {
			paramsMultiple.put(LogConstant.COL_APP_ID, 
    				Arrays.asList(appId.split(STR_DELIMITE_PARAM)));
        }
		if (!StringUtils.isEmpty(queryString)) {
            paramsQuery.put(LogConstant.COL_QUERY_STRING, queryString);
        }
		
        if (!paramsMultiple.isEmpty()) {
            dslBuilder.setParamsMultiple(paramsMultiple);
        }
        
        if (!paramsShould.isEmpty()) {
            dslBuilder.setParamsShould(paramsShould);
        }
        
        if(!paramsQuery.isEmpty()){
        	dslBuilder.setParamsQuery(paramsQuery);
        }
        
        LOG.info("queryRequest2EsBuilder dslBuilder=" + dslBuilder.toString());
    }

    /**
     * 构建dsl查询对象
     * @return dsl 查询对象
     */
    private EsQueryBuilder initEsQueryBuilder() {
        final EsQueryBuilder dslBuilder = new EsQueryBuilder();
        final Date startTime = DateUtils.getDate(new Date(), 0, 0, LogConstant.DEFAULT_START_DATE, 0, 0, 0);
        dslBuilder.setEndTime(new Date().getTime() * TIME_MS_BUILDER);
        dslBuilder.setStartTime(startTime.getTime() * TIME_MS_BUILDER);
        dslBuilder.setTimeColsName(LogConstant.COL_LOG_TIME);
        return dslBuilder;
    }
    
    public List<String> getLogSource(final LogQueryRequest request, final String colsName, String regionId) {
        final EsQueryBuilder dslBuilder = initEsQueryBuilder();
        queryRequest2EsBuilder(request, dslBuilder);
        dslBuilder.setGroupColsName(colsName);
        ArrayList<String> indexName = EsIndexManager.getMultiLogIndex(dslBuilder.getStartTime(),
                dslBuilder.getEndTime(),Enviroment.BIZ_INDEX_PREFIX);
        //业务日志事件显示毫秒
        dslBuilder.setStartTime(dslBuilder.getStartTime()/TIME_MS_BUILDER);
        dslBuilder.setEndTime(dslBuilder.getEndTime()/TIME_MS_BUILDER);
        return service.group(EsDslJsonBuilder.group(dslBuilder), indexName,
                EsIndexManager.getBizType(), regionId);
    }
}
