package com.migu.tsg.microservice.atomicservice.composite.controller.util.es;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.google.gson.reflect.TypeToken;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.constant.Enviroment;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.constant.EventConstants;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.constant.LogConstant;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.dto.LogTagListRequest;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.engine.EsDslJsonBuilder;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.engine.EsIndexManager;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.engine.EsQueryBuilder;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.service.EsHandleService;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.util.DateUtils;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.es.util.Pagenation;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.dto.BizLogInfoDTO;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.dto.BucketInfoDTO;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.payload.BizLogListResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.payload.BizLogSearchRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.payload.BizLogTagListResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.logs.payload.LogChartListResponse;

/**
 * 日志索引，检索能力(ES)接口 项目名称: 微服务运维平台（monitor-service模块） 包: com.migu.tsg.microservice.atomicservice.monitor.log.biz.impl 类名称:
 * LogInfoBizImpl.java 类描述: 提供日志索引，检索能力(ES) 创建人: jiangfuyi 创建时间: 2017年12月28日
 */
@Service
public class BizLogInfoBizImpl {

    private static final Logger LOG = LoggerFactory.getLogger(BizLogInfoBizImpl.class);

    private static final String STR_DELIMITE_PARAM = ",";

    @Autowired
    private EsHandleService service;

    /**
     * ES查询记录条数
     *
     * @param request
     * @param regionId
     * @return
     */
    public final int count(final BizLogSearchRequest request, String regionId) {
        int count = 0;
        try {
            final EsQueryBuilder dslBuilder = initEsQueryBuilder();
            queryRequest2EsBuilder(request, dslBuilder);
            count = service.count(EsDslJsonBuilder.countBiz(dslBuilder), EsIndexManager.getMultiLogIndex(
                    request.getStartTime(), request.getEndTime(), Enviroment.BIZ_INDEX_PREFIX), EsIndexManager
                    .getBizType(), regionId);
        } catch (Exception e) {
            LOG.error("biz log count error:{}", e);
        }
        return count;
    }

    public final List<BizLogInfoDTO> search(final BizLogSearchRequest request, String regionId) {
        List<BizLogInfoDTO> logs = null;
        try {
            final EsQueryBuilder dslBuilder = initEsQueryBuilder();
            queryRequest2EsBuilder(request, dslBuilder);
            dslBuilder.setFrom(0);
            dslBuilder.setSize(Integer.parseInt(request.getSize()));
            logs = service.list(EsDslJsonBuilder.list(dslBuilder), EsIndexManager
                            .getMultiLogIndex(request.getStartTime(), request.getEndTime(), Enviroment
                                    .BIZ_INDEX_PREFIX),
                    EsIndexManager.getBizType(), new TypeToken<List<BizLogInfoDTO>>() {
                    }.getType(), regionId);
            for (BizLogInfoDTO logInfo : logs) {
                logInfo.setTime(logInfo.getTime().substring(0, 10) + "." + logInfo.getTime().substring(10 + 1
                ));
            }
        } catch (Exception e) {
            LOG.error("biz log list is error:{}", e);
        }
        return logs;
    }

    @SuppressWarnings("unchecked")
    public final BizLogListResponse list(final BizLogSearchRequest request, String regionId) {

        final BizLogListResponse response = new BizLogListResponse();
        try {
            final EsQueryBuilder dslBuilder = initEsQueryBuilder();
            queryRequest2EsBuilder(request, dslBuilder);
            final int count = count(request, regionId);
            if (count == 0) {
                return response;
            }

            int size = EventConstants.DEFUALT_PAGE_SIZE;
            if (!StringUtils.isEmpty(request.getSize())) {
                size = Integer.parseInt(request.getSize());
            }
            int page = EventConstants.DEFAULT_PAGE;
            if (!StringUtils.isEmpty(request.getPageno())) {
                page = Integer.parseInt(request.getPageno());
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

            final List<BizLogInfoDTO> logs = service.list(EsDslJsonBuilder.listBiz(dslBuilder), EsIndexManager
                            .getMultiLogIndex(request.getStartTime(), request.getEndTime(), Enviroment
                                    .BIZ_INDEX_PREFIX),
                    EsIndexManager.getBizType(), new TypeToken<List<BizLogInfoDTO>>() {
                    }.getType(), regionId);
            for (BizLogInfoDTO logInfo : logs) {
                logInfo.setTime(logInfo.getTime().substring(0, 10) + "." + logInfo.getTime().substring(10 + 1
                ));
            }
            response.setLogs(logs);

        } catch (Exception e) {
            LOG.error("biz log list is error:{}", e);
        }
        LOG.info("response for list size:{}", response.getTotalItems());// response.toString());
        return response;
    }

    public final BizLogTagListResponse listTags(final LogTagListRequest request, String regionId) {
        LOG.info("LogInfoBizImpl listTags request:{}", request.toString());
        final BizLogTagListResponse response = new BizLogTagListResponse();
        try {
            final EsQueryBuilder dslBuilder = initEsQueryBuilder();

            final List<String> nodes = distinct(dslBuilder, LogConstant.COL_MACHINE, regionId);
            final List<String> levels = distinct(dslBuilder, LogConstant.COL_LOG_LEVEL, regionId);
            if (!CollectionUtils.isEmpty(levels)) {
                List<String> levelList = levels.stream().filter(level -> {
                    return LogConstant.LOG_LEVEL_INFO.equalsIgnoreCase(level) || LogConstant.LOG_LEVEL_ERROR
                            .equalsIgnoreCase(level);
                }).collect(Collectors.toList());
                response.setLevels(levelList);
            } else {
                List<String> levelList = new ArrayList<>();
                levelList.add(LogConstant.LOG_LEVEL_INFO);
                levelList.add(LogConstant.LOG_LEVEL_ERROR);
                response.setLevels(levelList);
            }
            response.setNodes(nodes);
        } catch (Exception e) {
            LOG.error("list tag is error:{}", e);
        }
        LOG.info("LogInfoBizImpl listTags response for listTags:{}", response.toString());
        return response;
    }

    private List<String> distinct(final EsQueryBuilder dslBuilder, final String colsName, String regionId) {
        dslBuilder.setGroupColsName(colsName);
        // es5.6需要先开启fielddata
        return service.group(EsDslJsonBuilder.group(dslBuilder), EsIndexManager.getBizIndex(),
                EsIndexManager.getBizType(), regionId);
    }

    public final LogChartListResponse listChart(final BizLogSearchRequest request, String regionId) {
        final LogChartListResponse response = new LogChartListResponse();
        try {
            final EsQueryBuilder dslBuilder = initEsQueryBuilder();
            dslBuilder.setGroupColsName(LogConstant.COL_LOG_TIME);
            queryRequest2EsBuilder(request, dslBuilder);
            // 计算直方图的间隔
            dslBuilder.setInterval(getInterval(request.getStartTime(), request.getEndTime()));
            LOG.info("listChart getIndex={}, getLogType()={}", EsIndexManager.getBizIndex(),
                    EsIndexManager.getBizType());
            final List<BucketInfoDTO> list = service.histogramForBiz(EsDslJsonBuilder.histogramBiz(dslBuilder),
                    EsIndexManager.getMultiLogIndex(request.getStartTime(),
                            request.getEndTime(), Enviroment.BIZ_INDEX_PREFIX),
                    EsIndexManager.getBizType(), regionId);
            response.setBuckets(list);
        } catch (Exception e) {
            LOG.error("listChart is error:{}", e);
        }
        return response;
    }

    /**
     * 计算直方图间隔
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 返回直方图间隔时间, 单位秒
     */
    private String getInterval(long start, long end) {
        if (start == 0) {
            start = DateUtils.getDate(new Date(), 0, 0, LogConstant.DEFAULT_START_DATE, 0, 0, 0).getTime();
        }
        if (end == 0) {
            end = new Date().getTime();
        }
        final long interval = (end - start) / (LogConstant.HISTOGRAM_COUNT);
        return (interval) + "ms";
    }

    /**
     * 转换请求参数
     *
     * @param request    请求参数
     * @param dslBuilder dsl builder构建对象
     */
    private void queryRequest2EsBuilder(final BizLogSearchRequest request, final EsQueryBuilder dslBuilder) {
        final long startTime = request.getStartTime();
        final long endTime = request.getEndTime();
        final String clusters = request.getClusters();
        final String services = request.getServices();
        final String nodes = request.getNodes();
        final String instance = request.getInstances();
        final String logLevel = request.getLogLevel();
        final String appId = request.getAppId();
        final String traceId = request.getTraceId();
        final String queryString = request.getQueryString();

        LOG.info("queryRequest2EsBuilder request:{}， {}, dslBuilder{}, {}", startTime, endTime,
                dslBuilder.getStartTime(), dslBuilder.getEndTime());

        if (startTime > 0) {
            dslBuilder.setStartTime(startTime);
        }
        if (endTime > 0) {
            dslBuilder.setEndTime(endTime);
        }

        final Map<String, List<String>> paramsMultiple = new ConcurrentHashMap<String, List<String>>();
        final Map<String, List<String>> paramsShould = new ConcurrentHashMap<String, List<String>>();
        final Map<String, String> paramsQuery = new ConcurrentHashMap<String, String>();

        if (!StringUtils.isEmpty(clusters)) {
            paramsMultiple.put(LogConstant.COL_REGION_NAME, Arrays.asList(clusters.split(STR_DELIMITE_PARAM)));
        }
        if (!StringUtils.isEmpty(services)) {
            paramsMultiple.put(LogConstant.COL_SERVICE_NAME, Arrays.asList(services.split(STR_DELIMITE_PARAM)));
        }
        if (!StringUtils.isEmpty(instance)) {
            paramsShould.put(LogConstant.COL_INSTANCE_ID8, Arrays.asList(instance.split(STR_DELIMITE_PARAM)));
        }
        if (!StringUtils.isEmpty(nodes)) {
            paramsMultiple.put(LogConstant.COL_MACHINE, Arrays.asList(nodes.split(STR_DELIMITE_PARAM)));
        }
        if (!StringUtils.isEmpty(logLevel)) {
            // 含有info则查询info 和error级别的日志
            if (logLevel.indexOf(LogConstant.LOG_LEVEL_INFO) >= 0) {
                List<String> levels = new ArrayList<String>();
                levels.add(LogConstant.LOG_LEVEL_INFO);
                levels.add(LogConstant.LOG_LEVEL_ERROR);
                paramsMultiple.put(LogConstant.COL_LOG_LEVEL, levels);
            } else {
                paramsMultiple.put(LogConstant.COL_LOG_LEVEL, Arrays.asList(logLevel.split(STR_DELIMITE_PARAM)));
            }
        }
        if (!StringUtils.isEmpty(appId)) {
            paramsMultiple.put(LogConstant.COL_APP_ID, Arrays.asList(appId.split(STR_DELIMITE_PARAM)));
        }
        if (!StringUtils.isEmpty(traceId)) {
            paramsMultiple.put(LogConstant.COL_BIZ_ID, Arrays.asList(traceId.split(STR_DELIMITE_PARAM)));
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

        if (!paramsQuery.isEmpty()) {
            dslBuilder.setParamsQuery(paramsQuery);
        }

        LOG.info("queryRequest2EsBuilder dslBuilder=" + dslBuilder.toString());
    }

    /**
     * 构建dsl查询对象
     *
     * @return dsl 查询对象
     */
    private EsQueryBuilder initEsQueryBuilder() {
        final EsQueryBuilder dslBuilder = new EsQueryBuilder();
        final Date startTime = DateUtils.getDate(new Date(), 0, 0, LogConstant.DEFAULT_START_DATE, 0, 0, 0);
        dslBuilder.setEndTime(new Date().getTime());
        dslBuilder.setStartTime(startTime.getTime());
        dslBuilder.setTimeColsName(LogConstant.COL_LOG_TIME);
        return dslBuilder;
    }
}
