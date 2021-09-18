package com.migu.tsg.microservice.atomicservice.composite.controller.event;

import com.aspire.mirror.log.api.dto.EventLogListRequest;
import com.google.gson.Gson;
import com.migu.tsg.microservice.atomicservice.composite.Constants;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.event.EventAtomicClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.log.LogClientService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.log.payload.LogEventPayload;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext.RequestHeadUser;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeDefine;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.LogEventUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ResourceAuthHelper;
import com.migu.tsg.microservice.atomicservice.composite.dao.CompositeResourceDao;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.CompositeResource;
import com.migu.tsg.microservice.atomicservice.composite.exception.ResourceActionAuthException;
import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.event.IEventJakiroService;
import com.migu.tsg.microservice.atomicservice.composite.service.event.dto.EventInfo;
import com.migu.tsg.microservice.atomicservice.composite.service.event.dto.EventLogList;
import com.migu.tsg.microservice.atomicservice.composite.service.event.dto.RequestEventInfo;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@LogCodeDefine("1050114")
public class EventJakiroController implements IEventJakiroService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventJakiroController.class);

    /**
     * 事件原子服务
     */
    @Autowired
    private EventAtomicClient client;

    /**
     * 数据库操作类
     */
    @Autowired
    private CompositeResourceDao compositeResDao;

    /**
     * 日志工具类
     */
    @Autowired
    private LogClientService logClient;

    /**
     * 鉴权工具类
     */
    @Autowired
    private ResourceAuthHelper resAuthHelper;

    private final Gson gson = new Gson();

    /**
     * 
     * 接口:GET /events/{namespace} 功能:列表指定数量的事件
     * 
     * @author zhangqing Date:2017年10月9日下午4:21:16
     * @see com.migu.tsg.microservice.atomicservice.composite.service.event.
     *      IEventJakiroService#listEvents(java.lang.String, long, long, int)
     */
    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(resType = "event", action = "view")
    @LogCodeDefine("01")
    public String listEvents(@PathVariable("namespace") String namespace,
            @RequestParam(value = "project_name", required = false) String projectName,
            @RequestParam(value = "start_time") String startTime, @RequestParam(value = "end_time") String endTime,
            @RequestParam(value = "size") String size, @RequestParam(value = "pageno") String pageno,
            @RequestParam(value = "resource_type", required = false) String resourceType,
            @RequestParam(value = "resource_id", required = false) String resourceId,
            @RequestParam(value = "query_string", required = false) String queryString) {
        // 1.获取环境变量
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
        RequestHeadUser requestHeader = reqCtx.getUser();
        String org = requestHeader.getNamespace();

        //鉴权
        CompositeResource resource = new CompositeResource();
        resource.setNamespace(namespace);
        resource.setUuid(namespace);
        resource.setName(namespace);
        try {
            resAuthHelper.resourceActionVerify(reqCtx.getUser(), resource, reqCtx.getResAction(),
                    reqCtx.getFlattenConstraints());
        }catch (ResourceActionAuthException e) {
            // 如果用户没有权限，直接返回空字符串
            return "";
        }

        
        // 2.组织原子层入参
        EventLogListRequest request = new EventLogListRequest();
        // startTime入参，例如：1508256000秒；需转成纳秒
        long startTimeSecs = Long.parseLong(startTime);
        // endTime入参，例如：1510816209.486毫秒；需转成纳秒
        Double endTimeDouble = Double.valueOf(endTime) * Math.pow(10, 6);
        Long endTimeSecs = endTimeDouble.longValue();
        request.setEndTime(endTimeSecs);
        request.setNamespace(org);
        request.setPageNo(pageno);
        request.setResourceType(resourceType);
        request.setResourceUuid(resourceId);
        request.setQueryString(queryString);
        request.setSize(size);
        request.setStartTime(startTimeSecs * 1000 * 1000);
        request.setProjectName(projectName);
        LOGGER.info("1.*******原子层入参request是{}", request);

        // 3.原子层调用
        String result = client.listEventLogs(gson.toJson(request));
        if(null == result){
            result = "";
        }
        LOGGER.info("2.*******原子层入调用成功，返回值result是：{}", result);

        JSONObject respObj = JSONObject.fromObject(result);
        JSONObject eventsObj = respObj.getJSONObject("events");
        EventLogList eventLogList = PayloadParseUtil.jacksonBaseParse(EventLogList.class, eventsObj);
        int totalPages = eventLogList.getTotalPages();
        eventLogList.setTotalPages(totalPages);
        LOGGER.info("3.*******转成封装层的eventLogList是：{}", eventLogList);
        for (EventInfo info : eventLogList.getResults()) {
            String time = info.getTime();
            StringBuilder sb = new StringBuilder(time);
            StringBuilder insert = sb.insert(time.length() - 6, ".");
            info.setTime(insert.toString());
        }
        return PayloadParseUtil.jacksonBase2JsonStr(eventLogList);
    }

    /**
     * 
     * 接口:GET /events/{namespace}/{resource_type} 功能:对某种资源类型，查询对其操作的事件，并按照指定数量显示
     * 
     * @author zhangqing Date:2017年10月9日下午4:21:16
     * @see com.migu.tsg.microservice.atomicservice.composite.service.event.
     *      IEventJakiroService#listEvents(java.lang.String, long, long, int)
     */
    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(resType = "event", action = "view")
    @LogCodeDefine("02")
    public String listEventsByResourceType(@PathVariable("namespace") String namespace,
            @PathVariable("resource_type") String resourceType,
            @RequestParam(value = "project_name", required = false) String projectName,
            @RequestParam(value = "start_time") String startTime, @RequestParam(value = "end_time") String endTime,
            @RequestParam(value = "size", required = false) String size, @RequestParam(value = "pageno") String pageno,
            @RequestParam(value = "query_string", required = false) String queryString) {
        // 1.获取环境变量
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
        RequestHeadUser requestHeader = reqCtx.getUser();
        String org = requestHeader.getNamespace();

        //鉴权
        CompositeResource resource = new CompositeResource();
        resource.setNamespace(namespace);
        resource.setUuid(namespace);
        resource.setName(namespace);
        try {
            resAuthHelper.resourceActionVerify(reqCtx.getUser(), resource, reqCtx.getResAction(),
                    reqCtx.getFlattenConstraints());
        }catch (ResourceActionAuthException e) {
            // 如果用户没有权限，直接返回空字符串
            return "";
        }
        
        // 2.组织原子层入参
        EventLogListRequest request = new EventLogListRequest();
        // startTime入参，例如：1508256000秒；需转成纳秒
        long startTimeSecs = Long.parseLong(startTime);
        // endTime入参，例如：1510816209.486毫秒；需转成纳秒
        Double endTimeDouble = Double.valueOf(endTime) * Math.pow(10, 6);
        Long endTimeSecs = endTimeDouble.longValue();
        request.setEndTime(endTimeSecs);
        request.setNamespace(org);
        request.setPageNo(pageno);
        request.setQueryString(queryString);
        request.setSize(size);
        request.setStartTime(startTimeSecs * 1000 * 1000);
        request.setResourceType(resourceType);
        request.setProjectName(projectName);
        LOGGER.info("1.原子层入参request是{}", request);

        // 3.原子层调用
        String result = client.listEventLogs(gson.toJson(request));
        if(null == result){
            result = "";
        }
        LOGGER.info("2.原子层入调用成功，返回值result是：{}", result);

        JSONObject respObj = JSONObject.fromObject(result);
        JSONObject eventsObj = respObj.getJSONObject("events");
        EventLogList eventLogList = PayloadParseUtil.jacksonBaseParse(EventLogList.class, eventsObj);
        int totalPages = eventLogList.getTotalPages() + 1;
        eventLogList.setTotalPages(totalPages);
        LOGGER.info("3.转成封装层的eventLogList是：{}", eventLogList);
        for (EventInfo info : eventLogList.getResults()) {
            String time = info.getTime();
            StringBuilder sb = new StringBuilder(time);
            StringBuilder insert = sb.insert(time.length() - 6, ".");
            info.setTime(insert.toString());
        }
        return PayloadParseUtil.jacksonBase2JsonStr(eventLogList);
    }

    /**
     * 
     * 接口:GET /events/{namespace}/{resource_type}/{resource_uuid}
     * 功能:对某个资源上的指定数量的操作记录进行查询并列表显示。
     * 
     * @author zhangqing Date:2017年10月9日下午4:21:16
     * @see com.migu.tsg.microservice.atomicservice.composite.service.event.
     *      IEventJakiroService#listEvents(java.lang.String, long, long, int)
     */
    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(resType = "event", action = "view")
    @LogCodeDefine("03")
    public String listEventsByResourceTypeAndUuid(@PathVariable("namespace") String namespace,
            @PathVariable("resource_type") String resourceType, 
            @PathVariable("resource_uuid") String resourceUuid,
            @RequestParam(value = "start_time") String startTime, 
            @RequestParam(value = "end_time") String endTime,
            @RequestParam(value = "size") String size, 
            @RequestParam(value = "pageno") String pageno,
            @RequestParam(value = "query_string", required = false) String queryString) {
        // 1.获取环境变量
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
        RequestHeadUser requestHeader = reqCtx.getUser();
        String org = requestHeader.getNamespace();

        //鉴权
        CompositeResource resource = new CompositeResource();
        resource.setNamespace(namespace);
        resource.setUuid(namespace);
        resource.setName(namespace);
        try {
            resAuthHelper.resourceActionVerify(reqCtx.getUser(), resource, reqCtx.getResAction(),
                    reqCtx.getFlattenConstraints());
        }catch (ResourceActionAuthException e) {
            // 如果用户没有权限，直接返回空字符串
            return "";
        }
        
        // 2.组织原子层入参
        EventLogListRequest request = new EventLogListRequest();
        // startTime入参，例如：1508256000秒；需转成纳秒
        long startTimeSecs = Long.parseLong(startTime);
        // endTime入参，例如：1510816209.486毫秒；需转成纳秒
        Double endTimeDouble = Double.valueOf(endTime) * Math.pow(10, 6);
        Long endTimeSecs = endTimeDouble.longValue();
        request.setEndTime(endTimeSecs);
        request.setNamespace(org);
        request.setPageNo(pageno);
        request.setQueryString(queryString);
        request.setSize(size);
        request.setStartTime(startTimeSecs * 1000 * 1000);
        request.setResourceType(resourceType);
        request.setResourceUuid(resourceUuid);
        LOGGER.info("1.原子层入参request是{}", request);

        // 3.原子层调用
        String result = client.listEventLogs(gson.toJson(request));
        if(null == result){
            result = "";
        }
        LOGGER.info("2.原子层入调用成功，返回值result是：{}", result);

        JSONObject respObj = JSONObject.fromObject(result);
        JSONObject eventsObj = respObj.getJSONObject("events");
        EventLogList eventLogList = PayloadParseUtil.jacksonBaseParse(EventLogList.class, eventsObj);
        int totalPages = eventLogList.getTotalPages() + 1;
        eventLogList.setTotalPages(totalPages);
        LOGGER.info("3.转成封装层的eventLogList是：{}", eventLogList);
        for (EventInfo info : eventLogList.getResults()) {
            String time = info.getTime();
            StringBuilder sb = new StringBuilder(time);
            StringBuilder insert = sb.insert(time.length() - 6, ".");
            info.setTime(insert.toString());
        }
        return PayloadParseUtil.jacksonBase2JsonStr(eventLogList);
    }

    @Override
    @LogCodeDefine("04")
    public BaseResponse envetInfo(@PathVariable(name = "resource_name") String resource_name,
            @PathVariable("namespace") String namespace,
            @RequestBody RequestEventInfo request) {

        String resourceType = request.getResource_type();
        String operation = request.getOperation();
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
        CompositeResource accountResource = null;
        if(reqCtx.getUser().getNamespace().equals(resource_name)) {
            accountResource = compositeResDao.queryResourceByName(
                    reqCtx.getUser().getNamespace(), resourceType, resource_name);
         // 添加日志
            LogEventPayload logEvent = LogEventUtil.buildBasicLogEvent(reqCtx, resourceType, accountResource.getUuid(),
                    resource_name, operation, 1, "generic", accountResource);
            String logJson = LogEventUtil.wrapLogEvents2Json(logEvent);
            logClient.saveEventsLogInfo(logJson);
        }
        if(reqCtx.getUser().getUsername().equals(resource_name)) {
            accountResource = compositeResDao.queryResourceByName(
                    reqCtx.getUser().getNamespace(), Constants.RbacResource.SUBACCOUNT, resource_name);
         // 添加日志
            LogEventPayload logEvent = LogEventUtil.buildBasicLogEvent(reqCtx, Constants.RbacResource.SUBACCOUNT, 
                    accountResource.getUuid(), resource_name, operation, 1, "generic", accountResource);
            String logJson = LogEventUtil.wrapLogEvents2Json(logEvent);
            logClient.saveEventsLogInfo(logJson);
        }

        return new BaseResponse();
    }

}
