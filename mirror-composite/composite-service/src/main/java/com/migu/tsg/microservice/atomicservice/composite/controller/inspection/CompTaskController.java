package com.migu.tsg.microservice.atomicservice.composite.controller.inspection;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.inspection.ICompTaskService;
import com.aspire.mirror.composite.service.inspection.payload.CompItemData;
import com.aspire.mirror.composite.service.inspection.payload.CompTaskCreateRequest;
import com.aspire.mirror.composite.service.inspection.payload.CompTaskCreateResponse;
import com.aspire.mirror.composite.service.inspection.payload.CompTaskDetailResponse;
import com.aspire.mirror.composite.service.inspection.payload.CompTaskListResponse;
import com.aspire.mirror.composite.service.inspection.payload.CompTaskObjectCreateReuqest;
import com.aspire.mirror.composite.service.inspection.payload.CompTaskObjectDetailResponse;
import com.aspire.mirror.composite.service.inspection.payload.CompTaskPageRequest;
import com.aspire.mirror.composite.service.inspection.payload.CompTaskUpdateRequest;
import com.aspire.mirror.composite.service.inspection.payload.CompTaskUpdateResponse;
import com.aspire.mirror.composite.service.inspection.payload.CompUserDetailResponse;
import com.aspire.mirror.inspection.api.dto.TaskCreateRequest;
import com.aspire.mirror.inspection.api.dto.TaskCreateResponse;
import com.aspire.mirror.inspection.api.dto.TaskDetailResponse;
import com.aspire.mirror.inspection.api.dto.TaskObjectCreateRequest;
import com.aspire.mirror.inspection.api.dto.TaskObjectDetailResponse;
import com.aspire.mirror.inspection.api.dto.TaskPageRequest;
import com.aspire.mirror.inspection.api.dto.TaskUpdateRequest;
import com.aspire.mirror.inspection.api.dto.TaskUpdateResponse;
import com.aspire.mirror.template.api.dto.ItemsDetailResponse;
import com.aspire.mirror.template.api.dto.TemplateDetailResponse;
import com.aspire.mirror.template.api.dto.TemplateObjectDetailResponse;
import com.aspire.ums.cmdb.instance.payload.CmdbInstance;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.Constants;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.CmdbServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection.TaskObjectServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection.TaskScheduleServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection.TaskServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.inspection.UserServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.template.ItemsServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.template.TemplateObjectServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.template.TemplateServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeDefine;
import com.migu.tsg.microservice.atomicservice.composite.helper.CmdbHelper;
import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import com.migu.tsg.microservice.atomicservice.rbac.dto.ListOrgAccountsResponse;
import com.migu.tsg.microservice.atomicservice.rbac.dto.model.AccountDTO;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

/**
 * 任务管理实现
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller.inspection
 * 类名称:    CompTaskController.java
 * 类描述:    任务管理实现
 * 创建人:    JinSu
 * 创建时间:  2018/8/11 10:38
 * 版本:      v1.0
 */
@RestController
@LogCodeDefine("1050911")
public class CompTaskController extends CommonResourceController implements ICompTaskService {
    Logger logger = LoggerFactory.getLogger(CompTaskController.class);

    @Autowired
    private TaskServiceClient taskService;

//    @Autowired
//    private CompositeResourceDao compositeResDao;

    @Autowired
    private TaskObjectServiceClient taskDeviceService;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private TaskScheduleServiceClient taskScheduleService;

    @Autowired
    private TemplateServiceClient templateService;

    @Autowired
    private CmdbServiceClient cmdbService;

    @Autowired
    private CmdbHelper cmdbHelper;

    @Autowired
    private TemplateObjectServiceClient templateObjectService;

    @Autowired
    private ItemsServiceClient itemsService;

//    @Value("${deviceListUrl}")
//    private String devceListUrl;
//
//    @Value("${moduleTreeUrl}")
//    private String moduleTreeUrl;
//
//    @Value("${formUrl}")
//    private String formUrl;
//
//    @Value("${instancesUrl}")
//    private String instancesUrl;

//    private String[] receiverList;

    /**
     * 根据主键ID删除
     *
     * @param taskId 任务Id
     * @return BaseResponse 删除返回
     */
    @Override
    @ResAction(action = "delete", resType = "task")
    public BaseResponse deleteByPrimaryKey(@PathVariable("task_id") String taskId) {
        logger.info("method[deleteByPrimaryKey] taskId is {}.", taskId);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[deleteByPrimaryKey]Username is {} namespace is {}.", authCtx.getUser().getUsername(), authCtx
                .getUser().getNamespace());
//        CompositeResource resource = this.compositeResDao.queryResourceByUuid(authCtx.getUser().getNamespace(),
//                authCtx.getResType(), taskId);
//        logger.info("[deleteByPrimaryKey] CompositeResource result is {}", resource);
//        // 鉴权
//        resAuthHelper.resourceActionVerify(authCtx.getUser(), resource, authCtx.getResAction(),
//                authCtx.getFlattenConstraints());

        //调原子层删除接口
        taskDeviceService.deleteByTaskId(taskId);
        taskService.deleteByPrimaryKey(taskId);

        //调资源接口做资源删除操作
//        compositeResDao.removeCompositeResource(authCtx.getUser().getNamespace(), Constants.Resource.RES_TASK, taskId);
        return new BaseResponse();
    }

    /**
     * 巡检任务修改
     *
     * @param taskId
     * @param taskUpdateRequest inspection_task修改请求对象
     * @return
     */
    @Override
    @ResAction(action = "update", resType = "task")
    public CompTaskUpdateResponse modifyByPrimaryKey(@PathVariable("task_id") String taskId, @RequestBody @Validated
            CompTaskUpdateRequest taskUpdateRequest) {
        logger.info("method[modifyByPrimaryKey] taskId is {},  updateRequest body is {}", taskId, taskUpdateRequest);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[modifyByPrimaryKey]Username is {} namesapce is {}.", authCtx.getUser().getUsername(), authCtx
                .getUser().getNamespace());
//        CompositeResource compositeResource = new CompositeResource();
//        compositeResource.setUuid(taskId);
//        compositeResource.setNamespace(authCtx.getUser().getNamespace());
//        CompositeResource resource = this.compositeResDao.queryByUuidAndNamespace(taskId, authCtx.getUser()
//                .getNamespace());
//        resource.setName(taskUpdateRequest.getName());
//        logger.info("[modifyByPrimaryKey] CompositeResource result is {}", resource);
//        // 修改权限鉴权
//        resAuthHelper.resourceActionVerify(authCtx.getUser(), resource, authCtx.getResAction(),
//                authCtx.getFlattenConstraints());
//        CompositeResource existProjectRes = compositeResDao.queryResourceByName(authCtx.getUser().getNamespace(),
//                authCtx.getResType(), taskUpdateRequest.getName());
//        if (existProjectRes != null && !existProjectRes.getUuid().equals(taskId)) {
//            String tipMsg = "The config with name '{}' for namespace {} is already exists.";
//            throw new BaseException(tipMsg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_ALREADY_EXIST,
//                    taskUpdateRequest.getName(), authCtx.getUser().getNamespace());
//        }
        //调原子层做修改操作
        TaskUpdateRequest request =
                jacksonBaseParse(TaskUpdateRequest.class, taskUpdateRequest);
        if (StringUtils.isEmpty(request.getStatus())) {
            request.setStatus(Constants.Task.STATUS);
        }
        if (null == request.getExecTime()) {
            request.setExecTime("");
        }
        if (null == request.getCycle()) {
            request.setCycle("");
        }
        if (!StringUtils.isEmpty(request.getReceivers())) {
            String[] receiverList = request.getReceivers().split(",");
            for (int i = 0; i < receiverList.length; i++) {
                receiverList[i] = authCtx.getUser().getNamespace().concat("/").concat(receiverList[i]);
            }
            request.setReceivers(Joiner.on(",").join(receiverList));
        }
        List<TaskObjectCreateRequest> taskObjectList = parseTaskObject(taskUpdateRequest.getObjectList());
        request.setObjectList(taskObjectList);
        TaskUpdateResponse taskUpdateResponse = taskService.modifyByPrimaryKey(taskId, request);

        //处理任务设备列表
//        taskDeviceService.deleteByTaskId(taskId);
//        insertTaskObject(taskId, taskUpdateRequest.getObjectList());
        //修改资源名称
//        compositeResDao.updateResourceNameById(resource.getId(), resource.getName());
        return jacksonBaseParse(CompTaskUpdateResponse.class, taskUpdateResponse);
    }


//    private void insertTaskObject(String taskId, List<CompTaskObjectCreateReuqest> taskDeviceCreateReuqests) {
//        if (!CollectionUtils.isEmpty(taskDeviceCreateReuqests)) {
//            TaskObjectBatchCreateRequst batchCreateRequst = new TaskObjectBatchCreateRequst();
//            List<TaskObjectCreateRequest> listTaskObject = Lists.newArrayList();
//
//            batchCreateRequst.setObjectList(listTaskObject);
//            taskDeviceService.batchCreate(batchCreateRequst);
//        }
//    }

    /**
     * 巡检任务创建
     *
     * @param taskCreateRequest task创建请求对象
     * @return CompTaskCreateResponse 巡检任务创建返回
     */
    @Override
    @ResAction(action = "create", resType = "task")
    public CompTaskCreateResponse createdTask(@RequestBody CompTaskCreateRequest taskCreateRequest) {
        logger.info("method[createdTask] body is {}.", taskCreateRequest);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[createdTask]Username is {} namespace{}.", authCtx.getUser().getUsername(), authCtx.getUser()
                .getNamespace());
        RbacResource rbacData = jacksonBaseParse(RbacResource.class, taskCreateRequest);
        rbacData.setResTypeAction(authCtx.getResAction());
        logger.info("[createdTask]The rbacResource is {}.", rbacData);
        // 鉴权
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());
        logger.info("user {} is trying to create template with name {}.",
                authCtx.getUser().getUsername(), taskCreateRequest.getName());
//        CompositeResource existProjectRes = compositeResDao.queryResourceByName(authCtx.getUser().getNamespace(),
//                authCtx.getResType(), taskCreateRequest.getName());
//        if (existProjectRes != null) {
//            String tipMsg = "The config with name '{}' for namespace {} is already exists.";
//            throw new BaseException(tipMsg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_ALREADY_EXIST,
//                    taskCreateRequest.getName(), authCtx.getUser().getNamespace());
//        }
        //调原子层做创建操作
        TaskCreateRequest createRequest = jacksonBaseParse(TaskCreateRequest.class, taskCreateRequest);
        if (StringUtils.isEmpty(createRequest.getStatus())) {
            createRequest.setStatus(Constants.Task.STATUS);
        }
        if (!StringUtils.isEmpty(createRequest.getReceivers())) {
            String[] receiverList = createRequest.getReceivers().split(",");
            for (int i = 0; i < receiverList.length; i++) {
                receiverList[i] = authCtx.getUser().getNamespace().concat("/").concat(receiverList[i]);
            }
            createRequest.setReceivers(Joiner.on(",").join(receiverList));
        }
        if (null == createRequest.getExecTime()) {
            createRequest.setExecTime("");
        }
        if (null == createRequest.getCycle()) {
            createRequest.setCycle("");
        }
        List<TaskObjectCreateRequest> taskObjectList = parseTaskObject(taskCreateRequest.getObjectList());
        createRequest.setObjectList(taskObjectList);
        TaskCreateResponse createResponse = taskService.createdTask(createRequest);
        //插入任务设备
//        insertTaskObject(createResponse.getTaskId(), taskCreateRequest.getObjectList());
//
        // composite数据库中插入集群记录
//        CompositeResource compTemplate = new CompositeResource();
//        compTemplate.setUuid(createResponse.getTaskId());
//        compTemplate.setName(createResponse.getName());
//        compTemplate.setType(Constants.Resource.RES_TASK);
//        compTemplate.setNamespace(authCtx.getUser().getNamespace());
//        compTemplate.setCreatedBy(authCtx.getUser().getUsername());
//        this.compositeResDao.insertCompositeResource(compTemplate);
        return jacksonBaseParse(CompTaskCreateResponse.class, createResponse);
    }

    private List<TaskObjectCreateRequest> parseTaskObject(List<CompTaskObjectCreateReuqest> objectList) {
        List<TaskObjectCreateRequest> listTaskObject = Lists.newArrayList();
        for (CompTaskObjectCreateReuqest compObjectCreateReuqest : objectList) {
            if (StringUtils.isEmpty(compObjectCreateReuqest.getObjectIds())) {
                List<TemplateObjectDetailResponse> templateObjectList = templateObjectService.findByTemplateId(compObjectCreateReuqest.getTemplateId());
                for (TemplateObjectDetailResponse templateObj : templateObjectList) {
                    TaskObjectCreateRequest deviceCreateRequest = new TaskObjectCreateRequest();
                    deviceCreateRequest.setObjectId(templateObj.getObjectId());
                    deviceCreateRequest.setObjectType(templateObj.getObjectType());
                    deviceCreateRequest.setTemplateId(templateObj.getTemplateId());
                    listTaskObject.add(deviceCreateRequest);
                }
            } else {
                String[] objects = compObjectCreateReuqest.getObjectIds().split(",");
                for (String objectId : objects) {
                    TaskObjectCreateRequest deviceCreateRequest = new TaskObjectCreateRequest();
                    deviceCreateRequest.setObjectId(objectId);
                    deviceCreateRequest.setObjectType(compObjectCreateReuqest.getObejctType());
                    deviceCreateRequest.setTemplateId(compObjectCreateReuqest.getTemplateId());
                    listTaskObject.add(deviceCreateRequest);
                }
            }
        }
        return listTaskObject;
    }

    /**
     * 根据任务Id查询数据
     *
     * @param taskId inspection_task主键
     * @return CompTaskDetailResponse 任务详情返回对象
     */
    @Override
    @ResAction(action = "view", resType = "task")
    public CompTaskDetailResponse findByPrimaryKey(@PathVariable("task_id") String taskId) {
        logger.info("method[findByPrimaryKey] taskId is {}", taskId);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[findByPrimaryKey]Username is {}.", authCtx.getUser().getUsername());
//        CompositeResource resource = resDao.queryResourceByUuid(authCtx.getUser().getNamespace(), authCtx.getResType
//                (), taskId);
//        if (null == resource) {
//            String tipMsg = "The template with uuid '{}' for namespace {} is not exist.";
//            throw new BaseException(tipMsg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_NOT_EXIST,
//                    taskId, authCtx.getUser().getNamespace());
//        }
        // 鉴权
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), resource, authCtx.getResAction(), authCtx.getFlattenConstraints());
        //调原子层获取数据
        TaskDetailResponse taskDetailResponse = taskService.findByPrimaryKey(taskId);
        CompTaskDetailResponse compTaskDetailResponse = jacksonBaseParse(CompTaskDetailResponse.class,
                taskDetailResponse);
        List<TaskObjectDetailResponse> taskDeviceList = taskDeviceService.findByTaskId(taskId);

        Map<String, List<TaskObjectDetailResponse>> taskObjectMaps = toMap(taskDeviceList);

        if (!CollectionUtils.isEmpty(taskObjectMaps)) {
            List<CompTaskObjectDetailResponse> taskDeviceDetailResponseList = Lists.newArrayList();
            String templateIds = Joiner.on(",").join(taskObjectMaps.keySet());
            List<TemplateDetailResponse> templateList = templateService.listByPrimaryKeyArrays(templateIds);
            Map<String, TemplateDetailResponse> templateMap = toTemplateMap(templateList);
            for (String templateId : taskObjectMaps.keySet()) {
//                TemplateDetailResponse templateDetailResponse = templateMap.get(templateId);
                CompTaskObjectDetailResponse compTaskObjectDetailResponse = new CompTaskObjectDetailResponse();
                compTaskObjectDetailResponse.setTemplateId(templateId);
                if (templateMap.get(templateId) != null) {
                    compTaskObjectDetailResponse.setTemplateName(templateMap.get(templateId).getName());
                }
                List<TaskObjectDetailResponse> taskObjectListToMap = taskObjectMaps.get(templateId);
                List<String> objectIdList = Lists.newArrayList();
                for (TaskObjectDetailResponse taskDevice : taskObjectListToMap) {
//                    DeviceDetail device = new DeviceDetail();
//                    device.setDeviceId();
//                    if (!objectIdList.contains(taskDevice.getObjectId()) && taskDevice.getObjectType().equals
//                            (Constants.Task.DEVICE_TYPE)) {
                    objectIdList.add(taskDevice.getObjectId());
//                    }
                }

//                compTaskObjectDetailResponse.setDevices(deviceIds);
                if (!CollectionUtils.isEmpty(objectIdList)) {
                    List<Map<String, String>> objectDetails = Lists.newArrayList();
                    if (templateMap.get(templateId).getMonType().equals(Constants.Template.MON_TYPE_SYS)) {
                        String objectIds = Joiner.on(",").join(objectIdList);
                        List<CmdbInstance> mapList = getInstanceName(objectIds);
                        if (!CollectionUtils.isEmpty(mapList)) {
                            for (CmdbInstance map : mapList) {
                                Map<String, String> deviceDetail = new HashMap<>();
                                deviceDetail.put("device_id", (String) map.getId());
                                deviceDetail.put("device_name", (String) map.getDeviceName());
                                objectDetails.add(deviceDetail);
                            }
                        }
                    } else if(templateMap.get(templateId).getMonType().equals(Constants.Template.MON_TYPE_BIZ)) {
                        for (String objectId : objectIdList) {
                            String bizName = cmdbHelper.getBizSysName(objectId);
                            Map<String, String> map = new HashMap<>();
                            map.put("biz_code", objectId);
                            map.put("biz_name", bizName);
                            objectDetails.add(map);
                        }
                    } else {
                        for (String objectId : objectIdList) {
                            Map<String, String> map = new HashMap<>();
                            map.put("object_id", objectId);
                            objectDetails.add(map);
                        }
                    }
                    compTaskObjectDetailResponse.setDeviceInstanceList(objectDetails);
                }
                taskDeviceDetailResponseList.add(compTaskObjectDetailResponse);
            }
            compTaskDetailResponse.setDeviceList(taskDeviceDetailResponseList);
        }
        return compTaskDetailResponse;
    }

    private List<CmdbInstance> getInstanceName(String deviceIds) {
//        RestTemplate template = new RestTemplate();
//        logger.info("connect cmdb server get instance url is {}", instancesUrl + "/" + deviceIds);
//        ResponseEntity<Object> response = template.getForEntity(instancesUrl + "/" + deviceIds, Object.class);
        List<CmdbInstance> mapList = cmdbService.getInstanceByIds(deviceIds);
//        if (response.getStatusCode().value() == 200) {
//            Object obj = response.getBody();
//            mapList = jacksonBaseParse(List.class, obj);
//        }
        return mapList;
    }

    /**
     * 将模板列表转换成Map
     *
     * @param templateList 模板列表
     * @return
     */
    public static Map<String, TemplateDetailResponse> toTemplateMap(final List<TemplateDetailResponse> templateList) {
        if (CollectionUtils.isEmpty(templateList)) {
            return Maps.newHashMap();
        }
        Map<String, TemplateDetailResponse> templateMaps = Maps.newHashMap();

        for (TemplateDetailResponse template : templateList) {
            templateMaps.put(template.getTemplateId(), template);
        }
        return templateMaps;
    }

    /**
     * 将任务设备列表转换成Map
     *
     * @param listTaskObject 任务设备列表
     * @return
     */
    public static Map<String, List<TaskObjectDetailResponse>> toMap(final List<TaskObjectDetailResponse>
                                                                            listTaskObject) {
        if (CollectionUtils.isEmpty(listTaskObject)) {
            return Maps.newHashMap();
        }
        Map<String, List<TaskObjectDetailResponse>> taskObjectMaps = Maps.newHashMap();

        for (TaskObjectDetailResponse taskDevice : listTaskObject) {
            if (null == taskObjectMaps.get(taskDevice.getTemplateId())) {
                List<TaskObjectDetailResponse> listItem = new ArrayList<TaskObjectDetailResponse>();
                listItem.add(taskDevice);
                taskObjectMaps.put(taskDevice.getTemplateId(), listItem);
            } else {
                List<TaskObjectDetailResponse> listItem = taskObjectMaps.get(taskDevice.getTemplateId());
                listItem.add(taskDevice);
            }
        }
        return taskObjectMaps;
    }

    /**
     * 根据page对象查询数据
     *
     * @param taskPageRequest:分页查询参数封装对象
     * @return PageResponse 返回page对象
     */
    @Override
    @ResAction(action = "view", resType = "task", loadResFilter = true)
    public PageResponse<CompTaskListResponse> list(@RequestBody CompTaskPageRequest taskPageRequest) {
        logger.info("method[list] request body is {}", taskPageRequest);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[list]Username is {}.", authCtx.getUser().getUsername());
        RbacResource rbacData = jacksonBaseParse(RbacResource.class, taskPageRequest);
        //查询鉴权
        rbacData.setResTypeAction(authCtx.getResAction());
        logger.info("[list]The rbacResource is {}.", rbacData);
        resAuthHelper.resourceActionVerify(
                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());
        //调原子层获取数据
        TaskPageRequest pageRequest = jacksonBaseParse(TaskPageRequest.class, taskPageRequest);
        if (pageRequest.getPageNo() == null) {
            pageRequest.setPageNo(1);
        }
        if (pageRequest.getPageSize() == null) {
            pageRequest.setPageSize(1000);
        }
        PageResponse<TaskDetailResponse> pageResponse = taskService.list(pageRequest);
        List<CompTaskListResponse> listTask = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageResponse.getResult())) {
            List<String> taskIdArray = Lists.newArrayList();
            for (TaskDetailResponse taskDetailResponse : pageResponse.getResult()) {
                taskIdArray.add(taskDetailResponse.getTaskId());
            }
            //设置模板数据
            List<TaskObjectDetailResponse> deviceList = taskDeviceService.findByTaskIds(Joiner.on(",").join
                    (taskIdArray));
            //模板ID集合
            List<String> templateIdList = Lists.newArrayList();
            Map<String, List<String>> taskMap = Maps.newHashMap();
            //设备ID集合
            Map<String, Map<String, List<String>>> deviceIdMap = Maps.newHashMap();
            for (TaskObjectDetailResponse taskDeviceDetailResponse : deviceList) {
                if (!templateIdList.contains(taskDeviceDetailResponse.getTemplateId())) {
                    templateIdList.add(taskDeviceDetailResponse.getTemplateId());
                }
                if (taskMap.get(taskDeviceDetailResponse.getTaskId()) == null) {
                    List<String> templateIdList1 = Lists.newArrayList();
                    templateIdList1.add(taskDeviceDetailResponse.getTemplateId());
                    taskMap.put(taskDeviceDetailResponse.getTaskId(), templateIdList1);
                } else {
                    List<String> templateIdList1 = taskMap.get(taskDeviceDetailResponse.getTaskId());
                    if (!templateIdList1.contains(taskDeviceDetailResponse.getTemplateId())) {
                        templateIdList1.add(taskDeviceDetailResponse.getTemplateId());
                    }
                }
                if (deviceIdMap.get(taskDeviceDetailResponse.getTaskId()) == null) {
                    List<String> deviceIdList = Lists.newArrayList();
                    List<String> bizCodeList = Lists.newArrayList();
                    List<String> scriptList = Lists.newArrayList();
                    if (taskDeviceDetailResponse.getObjectType().equals(Constants.Template.DEVICE_TYPE)) {
                        deviceIdList.add(taskDeviceDetailResponse.getObjectId());
                    } else if (taskDeviceDetailResponse.getObjectType().equals(Constants.Template.SYS_TYPE)) {
                        bizCodeList.add(taskDeviceDetailResponse.getObjectId());
                    } else if (taskDeviceDetailResponse.getObjectType().equals(Constants.Template.OPS_TYPE)) {
                        scriptList.add(taskDeviceDetailResponse.getObjectId());
                    }
                    Map map = Maps.newHashMap();
                    map.put("bizCodeList", bizCodeList);
                    map.put("deviceIdList", deviceIdList);
                    map.put("scriptList", scriptList);
                    deviceIdMap.put(taskDeviceDetailResponse.getTaskId(), map);
                } else {
                    List<String> deviceIdList = deviceIdMap.get(taskDeviceDetailResponse.getTaskId()).get
                            ("deviceIdList");
                    List<String> bizCodeList = deviceIdMap.get(taskDeviceDetailResponse.getTaskId()).get("bizCodeList");
                    List<String> scriptList = deviceIdMap.get(taskDeviceDetailResponse.getTaskId()).get("scriptList");
                    if (!deviceIdList.contains(taskDeviceDetailResponse.getObjectId()) && taskDeviceDetailResponse
                            .getObjectType().equals(Constants.Template.DEVICE_TYPE)) {
                        deviceIdList.add(taskDeviceDetailResponse.getObjectId());
                    }
                    if (!bizCodeList.contains(taskDeviceDetailResponse.getObjectId()) && taskDeviceDetailResponse
                            .getObjectType().equals(Constants.Template.SYS_TYPE)) {
                        bizCodeList.add(taskDeviceDetailResponse.getObjectId());
                    }
                    if (!bizCodeList.contains(taskDeviceDetailResponse.getObjectId()) && taskDeviceDetailResponse
                            .getObjectType().equals(Constants.Template.OPS_TYPE)) {
                        scriptList.add(taskDeviceDetailResponse.getObjectId());
                    }
                }
            }

            Map<String, List<String>> taskTemplateMap = Maps.newHashMap();
            if (!CollectionUtils.isEmpty(templateIdList)) {
                List<TemplateDetailResponse> listTemplate = templateService.listByPrimaryKeyArrays(Joiner.on(",")
                        .join(templateIdList));

//                    if (taskTemplateMap.get(taskId) == null) {
//                        List<String> templateNames = Lists.newArrayList();
//                        templateNames.add(templateDetailResponse.getName());
//                        taskTemplateMap.put(taskId, templateNames);
//                    } else {
//                        List<String> templateNames = taskTemplateMap.get(taskId);
//                        templateNames.add(templateDetailResponse.getName());
//                    }
                Map<String, TemplateDetailResponse> templateMap = toTemplateMap(listTemplate);
                for (String taskId : taskMap.keySet()) {
                    List<String> templateNames = Lists.newArrayList();
                    List<String> templateIds = taskMap.get(taskId);
                    for (String templateId : templateIds) {
                        if (templateMap.get(templateId) != null) {
                            templateNames.add(templateMap.get(templateId).getName());
                        }
                    }
                    taskTemplateMap.put(taskId, templateNames);
                }
            }
            for (TaskDetailResponse taskDetailResponse : pageResponse.getResult()) {
                CompTaskListResponse taskListResponse = jacksonBaseParse(CompTaskListResponse.class,
                        taskDetailResponse);
                //设备模板名称
                if (!CollectionUtils.isEmpty(taskTemplateMap.get(taskListResponse.getTaskId()))) {
                    taskListResponse.setTemplateNames(Joiner.on(",").join(taskTemplateMap.get(taskListResponse
                            .getTaskId())));
                }
                //设置设备
                if (!CollectionUtils.isEmpty(deviceIdMap.get(taskListResponse.getTaskId()))) {
                    List<String> deviceIdList = deviceIdMap.get(taskListResponse
                            .getTaskId()).get("deviceIdList");
                    List<String> bizCodeList = deviceIdMap.get(taskListResponse
                            .getTaskId()).get("bizCodeList");
                    List<String> scriptList = deviceIdMap.get(taskListResponse
                            .getTaskId()).get("scriptList");
                    Set<String> instanceNameList = Sets.newHashSet();
                    if (!CollectionUtils.isEmpty(deviceIdList)) {
                        List<CmdbInstance> mapList = getInstanceName(Joiner.on(",").join(deviceIdList));
                        if (!CollectionUtils.isEmpty(mapList)) {
                            for (CmdbInstance map : mapList) {
                                if (map.getDeviceName() != null)
                                instanceNameList.add((String) map.getDeviceName());
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(bizCodeList)) {
                        for (String bizCode : bizCodeList) {
                            String bizName = cmdbHelper.getBizSysName(bizCode);
                            if (bizName != null) instanceNameList.add(bizName);
                        }
                    }
                    if (!CollectionUtils.isEmpty(scriptList)) {
                        instanceNameList.addAll(scriptList);
                    }
                    taskListResponse.setRange(Joiner.on(",").join(instanceNameList));
                }

                listTask.add(taskListResponse);
            }
        }

        PageResponse<CompTaskListResponse> response = new PageResponse();
        response.setResult(listTask);
        response.setCount(pageResponse.getCount());
        return response;
    }

    /**
     * 获取用户列表信息
     *
     * @param namespace 命名空间
     * @return
     */
    @Override
    @ResAction(resType = "subaccount", action = "view")
    public PageResponse<CompUserDetailResponse> getUserList(@PathVariable("namespace") String namespace,
                                                            @RequestParam(value = "username", required = false)
                                                                    String username,
                                                            @RequestParam(value = "order_by", required = false,
                                                                    defaultValue = "-createTime") String orderBy,
                                                            @RequestParam(value = "page_size", required = false,
                                                                    defaultValue = "20") Integer pageSize,
                                                            @RequestParam(value = "page", required = false,
                                                                    defaultValue = "1") Integer page) {
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
        // 1.本地数据鉴权过滤
        logger.debug("method[getUserList] param namespace {}, username, orderBy", namespace, username, orderBy);
//        CompositeResource param = new CompositeResource();
//        param.setNamespace(namespace);
//        param.setType(reqCtx.getResType());
//        List<CompositeResource> queryResourceList = compositeResDao.queryResourceList(param);
//
//        if (null == queryResourceList || queryResourceList.size() == 0) {
//            throw new ResourceActionAuthException();
//        }
        ListOrgAccountsResponse listOrgAccount = userServiceClient.listOrgAccounts(namespace, null, null,  username,
                orderBy, pageSize, page);
        List<CompUserDetailResponse> response = Lists.newArrayList();
        if (listOrgAccount != null && !CollectionUtils.isEmpty(listOrgAccount.getResults())) {
            for (AccountDTO account : listOrgAccount.getResults()) {
                CompUserDetailResponse compUserDetailResponse = jacksonBaseParse(CompUserDetailResponse.class, account);
                response.add(compUserDetailResponse);
            }
        }
        PageResponse baseResponse = new PageResponse();
        baseResponse.setResult(response);
        baseResponse.setCount(listOrgAccount.getCount());
        return baseResponse;
    }


    @Override
    @ResAction(action = "addSchedule", resType = "task")
    public BaseResponse addScheduling(@PathVariable("task_id") final String taskId) {
        logger.info("method[addScheduling] taskId is {}", taskId);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[addScheduling]Username is {}.", authCtx.getUser().getUsername());
//        CompositeResource resource = resDao.queryResourceByUuid(authCtx.getUser().getNamespace(), authCtx.getResType
//                (), taskId);
//        if (null == resource) {
//            String tipMsg = "The template with uuid '{}' for namespace {} is not exist.";
//            throw new BaseException(tipMsg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_NOT_EXIST,
//                    taskId, authCtx.getUser().getNamespace());
//        }
        // 鉴权
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), resource, authCtx.getResAction(), authCtx.getFlattenConstraints());
        //调原子层
        taskScheduleService.addScheduling(taskId);
        return new BaseResponse();
    }

    @Override
    @ResAction(action = "addSchedule", resType = "task")
    public BaseResponse exec(@PathVariable("task_id") final String taskId) {
        logger.info("method[exec] taskId is {}", taskId);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[exec]Username is {}.", authCtx.getUser().getUsername());
//        CompositeResource resource = resDao.queryResourceByUuid(authCtx.getUser().getNamespace(), authCtx.getResType
//                (), taskId);
//        if (null == resource) {
//            String tipMsg = "The template with uuid '{}' for namespace {} is not exist.";
//            throw new BaseException(tipMsg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_NOT_EXIST,
//                    taskId, authCtx.getUser().getNamespace());
//        }
//        // 鉴权
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), resource, authCtx.getResAction(), authCtx.getFlattenConstraints());
        //调原子层
        taskScheduleService.execute(taskId);
        return new BaseResponse();
    }

    @Override
    @ResAction(action = "stopSchedule", resType = "task")
    public BaseResponse stopScheduling(@PathVariable("task_id") final String taskId) {
        logger.info("method[addScheduling] taskId is {}", taskId);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[addScheduling]Username is {}.", authCtx.getUser().getUsername());
//        CompositeResource resource = resDao.queryResourceByUuid(authCtx.getUser().getNamespace(), authCtx.getResType
//                (), taskId);
//        if (null == resource) {
//            String tipMsg = "The template with uuid '{}' for namespace {} is not exist.";
//            throw new BaseException(tipMsg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_NOT_EXIST,
//                    taskId, authCtx.getUser().getNamespace());
//        }
//        // 鉴权
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), resource, authCtx.getResAction(), authCtx.getFlattenConstraints());
        //调原子层
        taskScheduleService.stopScheduling(taskId);
        return new BaseResponse();
    }

    @Override
    public List<CompItemData> getItemListByTaskId(@PathVariable("task_id") String taskId) {
        List<TaskObjectDetailResponse> deviceList = taskDeviceService.findByTaskId(taskId);
        if (!CollectionUtils.isEmpty(deviceList)) {
            Set<String> templateIds = deviceList.stream().map(TaskObjectDetailResponse::getTemplateId).collect(Collectors.toSet());
            List<ItemsDetailResponse> itemList = itemsService.listItemsByTemplateIds(Joiner.on(",").join(templateIds));
            return itemList.stream().map(item -> {
                CompItemData itemData = new CompItemData();
                itemData.setItemId(item.getItemId());
                itemData.setItemName(item.getName());
                return itemData;
            }).distinct().collect(Collectors.toList());
        }
       return Lists.emptyList();
    }
}
