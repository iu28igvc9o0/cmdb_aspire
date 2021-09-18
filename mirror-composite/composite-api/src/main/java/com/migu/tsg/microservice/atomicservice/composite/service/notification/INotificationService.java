package com.migu.tsg.microservice.atomicservice.composite.service.notification;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.migu.tsg.microservice.atomicservice.composite.service.notification.payload.JakiroNotificationCreateRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.notification.payload.JakiroNotificationCreateResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.notification.payload.NotificationSMSRequestBody;
import com.migu.tsg.microservice.monitor.notice.api.dto.NotificationCreateRequest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface INotificationService {
	/**
     * Create notifications <br/>
     *
     * 作者： lifei
     * @param namespace
     * @return   
     */
    @ApiOperation(value = "创建通知", notes = "创建通知",
            response = JakiroNotificationCreateResponse.class, tags = {"Notification API"})
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "${version}/notifications/{namespace}", method = RequestMethod.POST)
    JakiroNotificationCreateResponse create(@PathVariable("namespace") String namespace,
    		@RequestParam(name = "project_name", required = false) String projectName,
    		@RequestBody JakiroNotificationCreateRequest data);
    
    /**
     * get list of notifications <br/>
     *
     * 作者： lifei
     * @param namespace
     * @return   
     */
    @ApiOperation(value = "查询通知列表", notes = "查询通知列表",
            response = JakiroNotificationCreateResponse.class, tags = {"Notification API"})
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "${version}/notifications/{namespace}", method = RequestMethod.GET)
    List<JakiroNotificationCreateResponse> list(@PathVariable("namespace") String namespace,
    		@RequestParam(name = "project_name", required = false) String projectName);
    
    
    
    /**
     * delete notification identified by uuid <br/>
     *
     * 作者： lifei
     * @param namespace
     * @return   
     */
    @ApiOperation(value = "删除由uuid标识的通知", notes = "删除由uuid标识的通知",
            response = JakiroNotificationCreateResponse.class, tags = {"Notification API"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "${version}/notifications/{namespace}/{notification_name}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("namespace") String namespace,
			@PathVariable("notification_name") String notification_name);
    
    /**
     * Create notifications <br/>
     *
     * 作者： lifei
     * @param namespace
     * @return   
     */
    @ApiOperation(value = "创建通知通过名称", notes = "创建通知通过名称",tags = {"Notification API"})
    @ApiResponses(value = { @ApiResponse(code = 200, message = "返回"),
            @ApiResponse(code =500, message = "内部错误") })
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "${version}/notifications/{namespace}/{notification_name}", method = RequestMethod.PUT)
	public String update(@PathVariable("namespace") String namespace,
			@PathVariable("notification_name") String notification_name,
			@RequestBody NotificationCreateRequest request);
    
    /**
     * get notification identified by uuid <br/>
     *
     * 作者： lifei
     * @param namespace
     * @return   
     */
    @ApiOperation(value = "通过uuid查询的通知", notes = "通过uuid查询的通知",
            tags = {"Notification API"})
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "${version}/notifications/{namespace}/{notification_name}", method = RequestMethod.GET)
	public JakiroNotificationCreateResponse retrieve(@PathVariable("namespace") String namespace,
			@PathVariable("notification_name") String notification_name);
    
    /**
     * get collection of subscribers in this namespace <br/>
     *
     * 作者： lifei
     * @param namespace
     * @return   
     */
    @ApiOperation(value = "在这个命名空间中获取订阅者的集合", 
    		notes = "在这个命名空间中获取订阅者的集合",
            tags = {"Notification API"})
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "${version}/notifications/{namespace}/subscribers", method = RequestMethod.GET)
	public String getSubscriptionInfo(@PathVariable("namespace") String namespace);
    
    /**
     * send message  <br/>
     *
     * 作者： lifei
     * @param namespace
     * @return   
     */
    @ApiOperation(value = "发送消息以适应其他服务", 
    		notes = "发送消息以适应其他服务",
            tags = {"Notification API"})
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "${version}/notifications/{namespace}/{notification_name}/send",
    					method = RequestMethod.POST)
	public void sendMessage(@PathVariable("namespace") String namespace,
			@PathVariable("notification_name") String notificationName,
			@RequestBody NotificationSMSRequestBody request,
			@RequestParam(name = "project_name", required = false) String projectName);
    

}