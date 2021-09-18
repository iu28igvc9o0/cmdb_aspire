package com.migu.tsg.microservice.atomicservice.composite.controller.project;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectRequest;
import com.migu.tsg.microservice.atomicservice.architect.dto.CreateProjectResponse;
import com.migu.tsg.microservice.atomicservice.architect.dto.FetchProjectResponse;
import com.migu.tsg.microservice.atomicservice.architect.dto.FetchProjectTemplateResponse;
import com.migu.tsg.microservice.atomicservice.architect.dto.model.ProjectTemplateRoleDTO;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.log.LogClientService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.log.payload.LogEventPayload;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.project.ProjectServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.project.TemplatesServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.RoleServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload.response.CompAuthFilterResponse;
import com.migu.tsg.microservice.atomicservice.composite.common.KeyValue;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestConstraintEnum;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LastLogCodeEnum;
import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeDefine;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.LogEventUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ResourceAuthHelper;
import com.migu.tsg.microservice.atomicservice.composite.dao.CompositeResourceDao;
import com.migu.tsg.microservice.atomicservice.composite.dao.ResourceProjectDao;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.CompositeResource;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.ResourceProject;
import com.migu.tsg.microservice.atomicservice.composite.exception.BaseException;
import com.migu.tsg.microservice.atomicservice.composite.exception.ResourceActionAuthException;
import com.migu.tsg.microservice.atomicservice.composite.exception.ResultErrorEnum;
import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.project.ICompProjectService;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompProjectCreateRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompProjectResource;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompProjectResourceResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompProjectResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompProjectStatusResponse;
import com.migu.tsg.microservice.atomicservice.composite.service.project.payload.CompProjectUpdateRequest;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.ICompRbacRoleService;
import com.migu.tsg.microservice.atomicservice.composite.service.rbac.payload.CompRoleCreatePayload;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRoleRequest;
import com.migu.tsg.microservice.atomicservice.rbac.dto.InsertRoleResponse;

import net.sf.json.JSONArray;

/**
 * 项目controller Project Name:composite-service File
 * Name:CompProjectTemplateController.java Package
 * Name:com.migu.tsg.microservice.atomicservice.composite.controller.project
 * ClassName: CompProjectTemplateController <br/>
 * date: 2017年9月29日 上午10:41:43 <br/>
 * 项目controller
 * 
 * @author baiwp
 * @version
 * @since JDK 1.6
 */
@RestController
@LogCodeDefine("1050120")
public class CompProjectController implements ICompProjectService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CompProjectController.class);

    /**
     * 鉴权工具类
     */
    @Autowired
    private ResourceAuthHelper resAuthHelper;

    @Autowired
    private CompositeResourceDao compositeResDao;

    @Autowired
    private ResourceProjectDao resourceProjectDao;

    @Autowired
    private ProjectServiceClient projectServiceClient;

    @Autowired
    private TemplatesServiceClient templatesServiceClient;

    @Autowired
    private LogClientService logClient;

    @Autowired
    private ICompRbacRoleService iCompRbacRoleService;

    @Autowired
    private RoleServiceClient rbacClient;

    public static final int NAMELENGTH = 1;
    
    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "view", resType = "project")
    @LogCodeDefine("11")
    public List<CompProjectResponse> listProjects(@PathVariable("namespace") String namespace) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        CompositeResource queryParam = new CompositeResource();
        queryParam.setNamespace(authCtx.getUser().getNamespace());
        queryParam.setType(authCtx.getResType());
        List<CompositeResource> compProjectList = compositeResDao.queryResourceList(queryParam);

        // 在composite数据库查询集群记录为空，直接返回空集合
        if (CollectionUtils.isEmpty(compProjectList)) {
            return new ArrayList<CompProjectResponse>();
        }
        // 给项目增加项目的约束。
        for (CompositeResource compositeResource : compProjectList) {
            compositeResource.setProjectName(compositeResource.getName());
        }
        // 鉴权过滤
        KeyValue<List<CompositeResource>, List<CompAuthFilterResponse>> keyValue = resAuthHelper
                .filterResourceList(authCtx.getUser(), authCtx.getResAction(), compProjectList, null);
        List<CompositeResource> compResList = keyValue.getKey();
        if (compResList.isEmpty()) {
            return new ArrayList<CompProjectResponse>();
        }
        List<String> uuids = CompositeResource.getUuidList(compResList);

        // 调用原子层查询项目
        List<FetchProjectResponse> fetchProjectList = projectServiceClient.fetchProjectList(uuids);
        List<CompProjectResponse> projectDataList = PayloadParseUtil.jacksonBaseParse(CompProjectResponse.class,
                fetchProjectList);

        // 添加resource_actions
        for (CompProjectResponse projectData : projectDataList) {
            for (CompAuthFilterResponse authItem : keyValue.getValue()) {
                if (authItem.getResource() == null || authItem.getResource().getUuid() == null) {
                    continue;
                }
                String authUuid = authItem.getResource().getUuid();
                if (authUuid.equals(projectData.getUuid())) {
                    projectData.setResource_actions(authItem.getResTypeActionList());
                }
            }
        }
        return projectDataList;
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "view", resType = "project")
    @LogCodeDefine("12")
    public CompProjectResourceResponse listProjectResources(@PathVariable("namespace") String namespace) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        CompositeResource dbParam = new CompositeResource();
        dbParam.setNamespace(namespace);
        dbParam.setType(RequestConstraintEnum.knamespace_name.getResType());
        List<CompositeResource> knamespaceResList = compositeResDao.queryProjectResourceList(dbParam);
        knamespaceResList = resAuthHelper
                .filterResourceList(authCtx.getUser(), RequestConstraintEnum.knamespace_name.getResType() + ":bind",
                        knamespaceResList, authCtx.getFlattenConstraints())
                .getKey();

        CompositeResource registryProjectParam = new CompositeResource();
        registryProjectParam.setNamespace(namespace);
        registryProjectParam.setType(RequestConstraintEnum.priv_regis_proj.getResType());
        List<CompositeResource> registryProjectList = compositeResDao.queryProjectResourceList(registryProjectParam);
        registryProjectList = resAuthHelper
                .filterResourceList(authCtx.getUser(), RequestConstraintEnum.registry_project.getResType() + ":bind",
                        registryProjectList, authCtx.getFlattenConstraints())
                .getKey();
        CompProjectResourceResponse compProjectResourceResponse = new CompProjectResourceResponse();
        List<CompProjectResource> knamespaces = knamespaceResList.stream().map(knamespaceRes -> {
            CompProjectResource compProjectResource = new CompProjectResource();
            compProjectResource.setName(knamespaceRes.getName());
            compProjectResource.setParentName(knamespaceRes.getRegionName());
            compProjectResource.setProjectUuid(knamespaceRes.getProjectUuid());
            compProjectResource.setType(knamespaceRes.getType());
            return compProjectResource;
        }).collect(Collectors.toList());
        compProjectResourceResponse.setKnamespaces(knamespaces);
        List<CompProjectResource> registryProjects = registryProjectList.stream().map(registryProject -> {
            CompProjectResource compProjectResource = new CompProjectResource();
            String[] names = registryProject.getName().split(":");
            
            if (names.length > NAMELENGTH) {
                compProjectResource.setName(names[1]);
                compProjectResource.setParentName(names[0]);
            } else {
                return null;
            }
            compProjectResource.setProjectUuid(registryProject.getProjectUuid());
            compProjectResource.setType(registryProject.getType());
            return compProjectResource;
        }).collect(Collectors.toList());
        compProjectResourceResponse.setRegistryProjects(registryProjects);
        return compProjectResourceResponse;
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @ResAction(action = "create", resType = "project")
    @LogCodeDefine("13")
    public CompProjectResponse createProject(@PathVariable("namespace") String namespace,
            @RequestBody CompProjectCreateRequest compReq) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        // 调用鉴权方法
        CompositeResource resResource = jacksonBaseParse(CompositeResource.class, compReq);
        resAuthHelper.resourceActionVerify(authCtx.getUser(), resResource, authCtx.getResAction(),
                authCtx.getFlattenConstraints());

        String projectName = compReq.getName();
        // 判断参数是否为空
        if (StringUtils.isBlank(projectName)) {
            String tipMsg = "the {} attribute is required.";
            throw new BaseException(tipMsg, LastLogCodeEnum.VALIDATE_ERROR, 
                                            ResultErrorEnum.BIZ_PARAMETER_CHECK_FAIL, "name");
        }
        // 判断项目名称格式
        boolean flag = projectName.matches("^[A-Za-z][A-Za-z0-9\\-\\.]*[A-Za-z0-9]?(?!\\n)$");
        if (!flag) {
            throw new BaseException(LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_NOT_MATCH);
        }
        // 判断项目名称的唯一性
        CompositeResource existProjectRes = compositeResDao.queryResourceByName(authCtx.getUser().getNamespace(),
                authCtx.getResType(), projectName);
        if (existProjectRes != null) {
            throw new BaseException(LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_ALREADY_EXIST);
        }
        FetchProjectTemplateResponse projTemplate = queryProjectTemplate(authCtx.getUser().getNamespace(),
                compReq.getTemplate());
        // 校验角色依赖关系
        checkRoles(projTemplate, compReq);

        // 调用原子层创建项目
        CreateProjectRequest createProjectRequest = PayloadParseUtil.jacksonBaseParse(CreateProjectRequest.class,
                compReq);
        createProjectRequest.setNamespace(authCtx.getUser().getNamespace());
        CreateProjectResponse createProjectResponse = projectServiceClient.createProject(createProjectRequest);
        // 创建项目资源
        CompositeResource projectResource = new CompositeResource();
        projectResource.setUuid(createProjectResponse.getUuid());
        projectResource.setName(createProjectResponse.getName());
        projectResource.setType(authCtx.getResType());
        projectResource.setNamespace(authCtx.getUser().getNamespace());
        projectResource.setCreatedBy(authCtx.getUser().getUsername());
        compositeResDao.insertCompositeResource(projectResource);

        // 创建角色
        createRoles(projTemplate, compReq, projectName, projectResource.getUuid());

        List<Map<String, Object>> resourceNames = compReq.getResources();
        Map<String, List<String>> resTypeNameMap = new HashMap<>();
        // 根据类型不同过滤，方便查询
        if (CollectionUtils.isNotEmpty(resourceNames)) {
        	for (Map<String, Object> resourceNameMap : resourceNames) {
                String resourceName = (String) resourceNameMap.get("name");
                String type = (String) resourceNameMap.get("type");
                // 没有类型或资源名称的不合法数据不处理
                if (StringUtils.isEmpty(type) || StringUtils.isEmpty(resourceName)) {
                    continue;
                }
                type = type.toLowerCase(Locale.getDefault());
                if (RequestConstraintEnum.priv_regis_proj.getResType().equalsIgnoreCase(type)
                        && null != resourceNameMap.get("parent_name")) {
                    resourceName = ((String) resourceNameMap.get("parent_name")).toLowerCase(
                            Locale.getDefault()) + ":" + resourceName;
                }
                if (resTypeNameMap.containsKey(type)) {
                    List<String> nameList = resTypeNameMap.get(type);
                    nameList.add(resourceName);
                } else {
                    List<String> nameList = new ArrayList<>();
                    nameList.add(resourceName);
                    resTypeNameMap.put(type, nameList);
                }
            }
        }

        // 查询需要绑定的资源
        List<CompositeResource> bindResourceList = new ArrayList<>();
        for (Entry<String, List<String>> entry : resTypeNameMap.entrySet()) {
            bindResourceList.addAll(compositeResDao.queryResourcesByNameList(authCtx.getUser().getNamespace(),
                    entry.getKey(), entry.getValue()));
        }
        // 绑定资源
        ResourceProject[] resProjList = bindResourceList.stream().map(bindCompRes -> {
            ResourceProject resourceProject = new ResourceProject();
            resourceProject.setProjectUuid(createProjectResponse.getUuid());
            resourceProject.setResourceUuid(bindCompRes.getUuid());
            return resourceProject;
        }).toArray(size -> new ResourceProject[size]);
        if (null != resProjList && resProjList.length > 0) {
            resourceProjectDao.insertResourceProject(resProjList);
        }

        // 添加日志
        LogEventPayload logEvent = LogEventUtil.buildBasicLogEvent(authCtx, authCtx.getResType(),
                createProjectResponse.getUuid(), createProjectResponse.getName(), "create", 1, "generic",
                null);
        String logJson = LogEventUtil.wrapLogEvents2Json(logEvent);
        logClient.saveEventsLogInfo(logJson);

        CompProjectResponse compProjectResponse = PayloadParseUtil.jacksonBaseParse(CompProjectResponse.class,
                createProjectResponse);
        return compProjectResponse;
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "view", resType = "project")
    @LogCodeDefine("14")
    public CompProjectResponse getProjectDetail(@PathVariable("namespace") String namespace,
            @PathVariable("name") String name) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        CompositeResource compResource = compositeResDao.queryResourceByName(authCtx.getUser().getNamespace(),
                authCtx.getResType(), name);

        // 如果资源不存在，抛出异常
        if (compResource == null) {
            String msg = "There is no resource exists with orgAccount = {}, name = {}, type = {} ";
            throw new BaseException(msg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_NOT_EXIST,
                    authCtx.getUser().getNamespace(), name, authCtx.getResType());
        }

        // 鉴权过滤
        resAuthHelper.resourceActionVerify(authCtx.getUser(), compResource, authCtx.getResAction(),
                authCtx.getFlattenConstraints());
        String uuid = compResource.getUuid();
        // 调用原子层查询项目
        FetchProjectResponse fetchProjectResponse = projectServiceClient.fetchProjectByUuid(uuid);
        CompProjectResponse compProjectResponse = PayloadParseUtil.jacksonBaseParse(CompProjectResponse.class,
                fetchProjectResponse);

        // 给返回结果增加权限操作
        List<String> actions = resAuthHelper.resourceActions(authCtx.getUser(), authCtx.getResType(),
                authCtx.getFlattenConstraints(), compResource.getFlatten());
        compProjectResponse.setResource_actions(actions);
        compProjectResponse.setCreatedBy(compResource.getCreatedBy());
        return compProjectResponse;
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "update", resType = "project")
    @LogCodeDefine("15")
    public CompProjectResponse updateProjectResources(@PathVariable("namespace") String namespace,
            @PathVariable("name") String name, @RequestBody CompProjectUpdateRequest compReq) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();

       
        CompositeResource compResource = compositeResDao.queryResourceByName(authCtx.getUser().getNamespace(),
                authCtx.getResType(), name);

        // 如果资源不存在，抛出异常
        if (compResource == null) {
            String msg = "There is no resource exists with orgAccount = {}, name = {}, type = {} ";
            throw new BaseException(msg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_NOT_EXIST,
                    authCtx.getUser().getNamespace(), name, authCtx.getResType());
        }

        // 鉴权过滤,判断有没有删除条件
        resAuthHelper.resourceActionVerify(authCtx.getUser(), compResource, authCtx.getResAction(),
                authCtx.getFlattenConstraints());

        String uuid = compResource.getUuid();

        // 修改更新时间
        projectServiceClient.updateProject(uuid);

        // 解绑所有资源
        resourceProjectDao.removeResourceProjectByProjectUuid(uuid, null);

        List<Map<String, Object>> resourceNames = compReq.getResources();
        Map<String, List<String>> resTypeNameMap = new HashMap<>();
        // 根据类型不同过滤，方便查询
        for (Map<String, Object> resourceNameMap : resourceNames) {
            String resourceName = (String) resourceNameMap.get("name");
            String type = (String) resourceNameMap.get("type");
            // 没有类型或资源名称的不合法数据不处理
            if (StringUtils.isEmpty(type) || StringUtils.isEmpty(name)) {
                continue;
            }
            type = type.toLowerCase(Locale.getDefault());
            if (RequestConstraintEnum.priv_regis_proj.getResType().equalsIgnoreCase(type)
                    && null != resourceNameMap.get("parent_name")) {
                resourceName = ((String) resourceNameMap.get("parent_name")).toLowerCase(
                        Locale.getDefault()) + ":" + resourceName;
            }
            if (resTypeNameMap.containsKey(type)) {
                List<String> nameList = resTypeNameMap.get(type);
                nameList.add(resourceName);
            } else {
                List<String> nameList = new ArrayList<>();
                nameList.add(resourceName);
                resTypeNameMap.put(type, nameList);
            }
        }
        // 查询需要绑定的资源
        List<CompositeResource> bindResourceList = new ArrayList<>();
        for (Entry<String, List<String>> entry : resTypeNameMap.entrySet()) {
            bindResourceList.addAll(compositeResDao.queryResourcesByNameList(authCtx.getUser().getNamespace(),
                    entry.getKey(), entry.getValue()));
        }
        // 绑定资源
        ResourceProject[] resProjList = bindResourceList.stream().map(bindCompRes -> {
            ResourceProject resourceProject = new ResourceProject();
            resourceProject.setProjectUuid(uuid);
            resourceProject.setResourceUuid(bindCompRes.getUuid());
            return resourceProject;
        }).toArray(size -> new ResourceProject[size]);
        if (null != resProjList && resProjList.length > 0) {
            resourceProjectDao.insertResourceProject(resProjList);
        }

        // 查询项目信息返回
        FetchProjectResponse fetchProjectResponse = projectServiceClient.fetchProjectByUuid(uuid);
        CompProjectResponse compProjectStatusResponse = PayloadParseUtil.jacksonBaseParse(CompProjectResponse.class,
                fetchProjectResponse);
        // 添加日志
        LogEventPayload logEvent = LogEventUtil.buildBasicLogEvent(authCtx, authCtx.getResType(),
                compResource.getUuid(), compResource.getName(), "update", 1, "generic", compResource);
        String logJson = LogEventUtil.wrapLogEvents2Json(logEvent);
        logClient.saveEventsLogInfo(logJson);

        return compProjectStatusResponse;
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResAction(action = "delete", resType = "project")
    @LogCodeDefine("16")
    public BaseResponse deleteProject(@PathVariable("namespace") String namespace, @PathVariable("name") String name) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();

        CompositeResource compResource = compositeResDao.queryResourceByName(authCtx.getUser().getNamespace(),
                authCtx.getResType(), name);

        // 如果资源不存在，抛出异常
        if (compResource == null) {
            String msg = "There is no resource exists with orgAccount = {}, name = {}, type = {} ";
            throw new BaseException(msg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_NOT_EXIST,
                    authCtx.getUser().getNamespace(), name, authCtx.getResType());
        }

        // 查询项目绑定的资源
        CompositeResource param = new CompositeResource();
        param.setNamespace(authCtx.getUser().getNamespace());
        param.setProjectUuid(compResource.getUuid());
        List<CompositeResource> bindResAndRoleList = compositeResDao.queryResourceList(param);
        List<CompositeResource> roleResList = bindResAndRoleList.stream().filter(bindResAndRole -> {
            return RequestConstraintEnum.role_name.getResType().equalsIgnoreCase(bindResAndRole.getType());
        }).collect(Collectors.toList());
        bindResAndRoleList.removeAll(roleResList);
        // 如果大于0，返回错误，有资源没有解绑
        // 私有资源需手动删除，无法自动解绑
        if (CollectionUtils.isNotEmpty(bindResAndRoleList)) {
            List<Map<String, Object>> errorList = bindResAndRoleList.stream().map(bindResAndRole -> {
                Map<String, Object> errorMap = new HashMap<String, Object>();
                errorMap.put("uuid", bindResAndRole.getUuid());
                errorMap.put("type", bindResAndRole.getType());
                errorMap.put("name", bindResAndRole.getName());
                return errorMap;
            }).collect(Collectors.toList());
            throw new BaseException(JSONArray.fromObject(errorList).toString(), 
                    LastLogCodeEnum.GENERAL_ERROR, ResultErrorEnum.BIZ_RESOURCE_NOT_RELEASED);
        }

        // 鉴权过滤,判断有没有删除条件
        resAuthHelper.resourceActionVerify(authCtx.getUser(), compResource, authCtx.getResAction(),
                authCtx.getFlattenConstraints());

        String uuid = compResource.getUuid();
        // 解绑所有资源
        resourceProjectDao.removeResourceProjectByProjectUuid(uuid, null);

        // 删除所有角色
        if (CollectionUtils.isNotEmpty(roleResList)) {
            roleResList.stream().forEach(roleRes -> {
                iCompRbacRoleService.deleteRoleByName(authCtx.getUser().getNamespace(), roleRes.getName());
            });
        }

        // 调用原子层删除项目
        projectServiceClient.deleteProject(uuid);
        // 删除项目资源
        compositeResDao.removeCompositeResource(authCtx.getUser().getNamespace(), authCtx.getResType(), uuid);
        // 添加日志
        LogEventPayload logEvent = LogEventUtil.buildBasicLogEvent(authCtx, authCtx.getResType(), uuid,
                compResource.getName(), "delete", 1, "generic", compResource);
        String logJson = LogEventUtil.wrapLogEvents2Json(logEvent);
        logClient.saveEventsLogInfo(logJson);
        return new BaseResponse();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @ResAction(action = "view", resType = "project")
    @LogCodeDefine("17")
    public CompProjectStatusResponse getProjectStatus(@PathVariable("namespace") String namespace,
            @PathVariable("name") String name) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();

        CompositeResource compResource = compositeResDao.queryResourceByName(authCtx.getUser().getNamespace(),
                authCtx.getResType(), name);

        // 如果资源不存在，抛出异常
        if (compResource == null) {
            String msg = "There is no resource exists with orgAccount = {}, name = {}, type = {} ";
            throw new BaseException(msg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_NOT_EXIST,
                    authCtx.getUser().getNamespace(), name, authCtx.getResType());
        }

        // 鉴权过滤
        resAuthHelper.resourceActionVerify(authCtx.getUser(), compResource, authCtx.getResAction(),
                authCtx.getFlattenConstraints());
        String uuid = compResource.getUuid();
        // 调用原子层查询项目
        FetchProjectResponse fetchProjectResponse = projectServiceClient.fetchProjectByUuid(uuid);
        CompProjectStatusResponse compProjectStatusResponse = PayloadParseUtil
                .jacksonBaseParse(CompProjectStatusResponse.class, fetchProjectResponse);

        // 查询项目绑定的资源
        CompositeResource param = new CompositeResource();
        param.setNamespace(authCtx.getUser().getNamespace());
        param.setProjectUuid(uuid);
        List<CompositeResource> bindResAndRoleList = compositeResDao.queryProjectResourceList(param);
        // 过滤掉角色，剩下的即为返回的资源
        bindResAndRoleList = bindResAndRoleList.stream().filter(bindRes -> {
            return !RequestConstraintEnum.role_name.getResType().equalsIgnoreCase(bindRes.getType());
        }).collect(Collectors.toList());
        // 转化资源
        List<CompProjectResource> bindResList = bindResAndRoleList.stream().map(bindResAndRole -> {
            CompProjectResource compProjectResource = new CompProjectResource();
            if (RequestConstraintEnum.priv_regis_proj.getResType().equalsIgnoreCase(bindResAndRole.getType())) {
                String[] names = bindResAndRole.getName().split(":");
                if (names.length > NAMELENGTH) {
                    compProjectResource.setName(names[1]);
                    compProjectResource.setParentName(names[0]);
                } else {
                    return null;
                }
            } else {
                compProjectResource.setName(bindResAndRole.getName());
                compProjectResource.setParentName(bindResAndRole.getRegionName());
            }
            compProjectResource.setProjectUuid(bindResAndRole.getProjectUuid());
            compProjectResource.setType(bindResAndRole.getType());
            compProjectResource.setCreatedAt(Instant.ofEpochMilli(bindResAndRole.getCreatedAt().getTime())
                    .atOffset(ZoneOffset.ofHours(8)).toInstant().toString());
            return compProjectResource;
        }).collect(Collectors.toList());
        compProjectStatusResponse.setResources(bindResList);

        // 给返回结果增加权限操作
        List<String> actions = resAuthHelper.resourceActions(authCtx.getUser(), authCtx.getResType(),
                authCtx.getFlattenConstraints(), compResource.getFlatten());
        compProjectStatusResponse.setResource_actions(actions);
        return compProjectStatusResponse;
    }

    /**
     * 
     * queryProjectTemplate:根据项目模板名称获取详细信息. <br/>
     * 作者： baiwp
     * 
     * @param namespace
     * @param templateName
     * @return
     */
    private FetchProjectTemplateResponse queryProjectTemplate(String namespace, String templateName) {
        // 判断参数是否为空
        if (StringUtils.isBlank(templateName)) {
            String tipMsg = "the {} attribute is required.";
            throw new BaseException(tipMsg, LastLogCodeEnum.VALIDATE_ERROR,
                                            ResultErrorEnum.BIZ_PARAMETER_CHECK_FAIL, "template");
        }

        // 查询项目模板资源
        CompositeResource queryParam = new CompositeResource();
        queryParam.setType(RequestConstraintEnum.project_template.getResType());
        queryParam.setName(templateName);
        List<CompositeResource> compProjTempList = compositeResDao.queryResourceList(queryParam);
        // 如果资源不存在，抛出异常
        if (CollectionUtils.isEmpty(compProjTempList)) {
            String msg = "There is no resource exists with orgAccount = {}, name = {}, type = {} ";
            throw new BaseException(msg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_NOT_EXIST,
                    namespace, templateName, RequestConstraintEnum.project_template.getResType());
        }

        // 调用原子接口查询项目模板详细信息
        String uuid = compProjTempList.get(0).getUuid();
        List<FetchProjectTemplateResponse> fetchProjectTemplateResponseList = templatesServiceClient
                .fetchProjectTemplateList(Arrays.asList(uuid));
        // 如果返回结果为空，抛出异常
        if (CollectionUtils.isEmpty(fetchProjectTemplateResponseList)) {
            String msg = "There is no resource exists with orgAccount = {}, name = {}, type = {} ";
            throw new BaseException(msg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_NOT_EXIST,
                    namespace, templateName, RequestConstraintEnum.project_template.getResType());
        }

        return fetchProjectTemplateResponseList.get(0);
    }

    /**
     * 
     * createRoles:(创建角色). <br/>
     * 作者： baiwp
     * 
     * @param projTemplate
     * @param compReq
     * @param projectName
     * @param namespace
     */
    private void createRoles(FetchProjectTemplateResponse projTemplate, CompProjectCreateRequest compReq,
            String projectName, String projectUuid) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        Map<Integer, String> uuidMaps = new HashMap<>();
        // 创建角色
        List<ProjectTemplateRoleDTO> roles = projTemplate.getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            return;
        }
        List<Map<String, Object>> requestRoles = compReq.getRoles();
        for (Map<String, Object> requestRole : requestRoles) {
            String requestIdObj = String.valueOf(requestRole.get("id"));
            if (StringUtils.isEmpty(requestIdObj)) {
                continue;
            }
            Integer requestId = Integer.parseInt(requestIdObj);
            for (ProjectTemplateRoleDTO role : roles) {
                Integer id = role.getId();
                if (requestId.equals(id)) {
                    List<Integer> dependsOnList = role.getDependsOn();
                    List<Map<String, Object>> parentsRoleMap = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(dependsOnList)) {
                        for (Integer integer : dependsOnList) {
                            createParentRoles(projTemplate, projectName, uuidMaps, integer, projectUuid);
                            Map<String, Object> map = new HashMap<>();
                            map.put("uuid", uuidMaps.get(integer));
                            parentsRoleMap.add(map);
                        }
                    }
                    if (StringUtils.isEmpty(uuidMaps.get(requestId))) {
                        String roleStr = com.alibaba.fastjson.JSONObject.toJSONString(role);
                        roleStr = roleStr.replaceAll("\\[name\\]", projectName);
                        @SuppressWarnings("unchecked")
                        Map<String, Object> roleMap = PayloadParseUtil.jacksonBaseParse(Map.class, roleStr);
                        roleMap.put("parents", parentsRoleMap);
                        CompRoleCreatePayload compRoleCreatePayload = PayloadParseUtil
                                .jacksonBaseParse(CompRoleCreatePayload.class, roleMap);
                        // 调用接口创建角色
                        List<CompRoleCreatePayload> roleCreateList = createRole(authCtx.getUser().getNamespace(),
                                compRoleCreatePayload, projectUuid);
                        String uuid = null;
                        if (CollectionUtils.isNotEmpty(roleCreateList)) {
                            uuid = roleCreateList.get(0).getUuid();
                        }
                        uuidMaps.put(requestId, uuid);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 递归创建父角色 createParentRoles:(这里用一句话描述这个方法的作用). <br/>
     * 作者： baiwp
     * 
     * @param projTemplate
     * @param projectName
     * @param uuidMaps
     * @param parentId
     */
    private void createParentRoles(FetchProjectTemplateResponse projTemplate, String projectName,
            Map<Integer, String> uuidMaps, Integer parentId, String projectUuid) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        // 创建角色
        List<ProjectTemplateRoleDTO> roles = projTemplate.getRoles();
        for (ProjectTemplateRoleDTO role : roles) {
            Integer id = role.getId();
            if (parentId.equals(id)) {
                List<Integer> dependsOnList = role.getDependsOn();
                List<Map<String, Object>> parentsRoleMap = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(dependsOnList)) {
                    for (Integer integer : dependsOnList) {
                        createParentRoles(projTemplate, projectName, uuidMaps, integer, projectUuid);
                        Map<String, Object> map = new HashMap<>();
                        map.put("uuid", uuidMaps.get(integer));
                        parentsRoleMap.add(map);
                    }
                }
                if (StringUtils.isEmpty(uuidMaps.get(parentId))) {
                    String roleStr = com.alibaba.fastjson.JSONObject.toJSONString(role);
                    roleStr = roleStr.replaceAll("\\[name\\]", projectName);
                    @SuppressWarnings("unchecked")
                    Map<String, Object> roleMap = PayloadParseUtil.jacksonBaseParse(Map.class, roleStr);
                    roleMap.put("parents", parentsRoleMap);
                    CompRoleCreatePayload compRoleCreatePayload = PayloadParseUtil
                            .jacksonBaseParse(CompRoleCreatePayload.class, roleMap);
                    // 调用接口创建角色
                    List<CompRoleCreatePayload> roleCreateList = createRole(authCtx.getUser().getNamespace(),
                            compRoleCreatePayload, projectUuid);
                    String uuid = null;
                    if (CollectionUtils.isNotEmpty(roleCreateList)) {
                        uuid = roleCreateList.get(0).getUuid();
                    }
                    uuidMaps.put(parentId, uuid);
                    break;
                }

            }
        }
    }

    /**
     * 校验角色依赖关系，---依赖的父角色必须选 checkRoles:(这里用一句话描述这个方法的作用). <br/>
     * 作者： baiwp
     * 
     * @param projTemplate
     * @param compReq
     */
    private void checkRoles(FetchProjectTemplateResponse projTemplate, CompProjectCreateRequest compReq) {
        List<ProjectTemplateRoleDTO> roles = projTemplate.getRoles();
        List<Map<String, Object>> requestRoles = compReq.getRoles();
        List<Integer> roleIds = requestRoles.stream().map(requestRole -> {
            String requestIdObj = String.valueOf(requestRole.get("id"));
            return Integer.parseInt(requestIdObj);
        }).collect(Collectors.toList());
        for (Map<String, Object> requestRole : requestRoles) {
            String requestIdObj = String.valueOf(requestRole.get("id"));
            if (StringUtils.isEmpty(requestIdObj)) {
                continue;
            }
            Integer requestId = Integer.parseInt(requestIdObj);
            for (ProjectTemplateRoleDTO role : roles) {
                Integer id = role.getId();
                if (requestId.equals(id)) {
                    List<Integer> dependsOnList = role.getDependsOn();
                    if (CollectionUtils.isNotEmpty(dependsOnList)) {
                        // 依赖角色必须也被选择
                        for (Integer integer : dependsOnList) {
                            if (!roleIds.contains(integer)) {
                                String tipMsg = "the role {} must depond on parent role";
                                throw new BaseException(tipMsg, LastLogCodeEnum.VALIDATE_ERROR, 
                                        ResultErrorEnum.BIZ_PARAMETER_CHECK_FAIL, role.getName());
                            }
                        }
                    }
                }
            }
        }
    }

    private List<CompRoleCreatePayload> createRole(@PathVariable("namespace") String namespace,
            @RequestBody CompRoleCreatePayload createRole, String projectUuid) {
        LOGGER.debug("createRole method param is : createRole={}, projectUuid={}", createRole, projectUuid);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        String resType = RequestConstraintEnum.role_name.getResType();
        String action = "create";
        String resAction = new StringBuffer(resType).append(":").append(action).toString();

        List<CompRoleCreatePayload> createRoleList = new ArrayList<CompRoleCreatePayload>();
        createRoleList.add(createRole);
        // 填充namespace
        List<String> roleNameList = new ArrayList<String>();
        for (CompRoleCreatePayload payload : createRoleList) {
            payload.setNamespace(namespace);
            roleNameList.add(payload.getName());
        }

        // 鉴权过滤
        List<CompositeResource> authRoleList = PayloadParseUtil.parse2CompResList(createRoleList, false);
        List<CompositeResource> filterList = resAuthHelper
                .filterResourceList(authCtx.getUser(), resAction, authRoleList, authCtx.getFlattenConstraints())
                .getKey();
//        if (authRoleList.size() != filterList.size()) {
//            throw new ResourceActionAuthException();
//        }

        List<CompositeResource> existRoleResList = compositeResDao.queryResourcesByNameList(namespace, resType,
                roleNameList);
        if (existRoleResList != null && !existRoleResList.isEmpty()) {
            throw new BaseException(LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_ALREADY_EXIST);
        }

        // 调用RBAC服务创建资源
        List<InsertRoleRequest> rbacCreatRoleRequest = PayloadParseUtil.jacksonBaseParse(InsertRoleRequest.class,
                createRoleList);
        List<InsertRoleResponse> rbacCreateRoleResponse = rbacClient.insertRoles(rbacCreatRoleRequest);
        List<CompRoleCreatePayload> resultList = PayloadParseUtil.jacksonBaseParse(CompRoleCreatePayload.class,
                rbacCreateRoleResponse);

        // 解析相关参数，准备Composite层写入资源记录
        String username = authCtx.getUser().getUsername();

        List<CompositeResource> compRoleList = PayloadParseUtil.parse2CompResList(resultList, true);
        for (CompositeResource compRes : compRoleList) {
            compRes.setNamespace(namespace);
            compRes.setCreatedBy(username);
            compRes.setCreatedAt(new Date());
            compRes.setProjectUuid(projectUuid);
        }
        // DAO写入Composite资源记录
        compositeResDao.insertCompositeResource(compRoleList.toArray(new CompositeResource[0]));
        return resultList;
    }

}
