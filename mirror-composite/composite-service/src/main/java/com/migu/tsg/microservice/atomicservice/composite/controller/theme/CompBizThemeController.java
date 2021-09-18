package com.migu.tsg.microservice.atomicservice.composite.controller.theme;

import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import com.aspire.mirror.common.constant.Constant;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.common.entity.Result;
import com.aspire.mirror.composite.service.theme.ICompBizThemeService;
import com.aspire.mirror.composite.service.theme.payload.CompBizThemeCreateRequest;
import com.aspire.mirror.composite.service.theme.payload.CompBizThemeCreateResponse;
import com.aspire.mirror.composite.service.theme.payload.CompBizThemeDataDetailResponse;
import com.aspire.mirror.composite.service.theme.payload.CompBizThemeDetailResponse;
import com.aspire.mirror.composite.service.theme.payload.CompBizThemeDimDetailResponse;
import com.aspire.mirror.composite.service.theme.payload.CompBizThemePageRequest;
import com.aspire.mirror.composite.service.theme.payload.CompBizThemeUpdateRequest;
import com.aspire.mirror.composite.service.theme.payload.CompBizThemeUpdateResponse;
import com.aspire.mirror.composite.service.theme.payload.CompThemeDataCreateRequest;
import com.aspire.mirror.composite.service.theme.payload.CompThemeLogValidRequest;
import com.aspire.mirror.template.api.dto.ItemsDetailResponse;
import com.aspire.mirror.theme.api.dto.BizThemeCreateRequest;
import com.aspire.mirror.theme.api.dto.BizThemeCreateResponse;
import com.aspire.mirror.theme.api.dto.BizThemeDataDetailResponse;
import com.aspire.mirror.theme.api.dto.BizThemeDetailResponse;
import com.aspire.mirror.theme.api.dto.BizThemeDimDetailResponse;
import com.aspire.mirror.theme.api.dto.BizThemePageRequest;
import com.aspire.mirror.theme.api.dto.BizThemeSearchRequest;
import com.aspire.mirror.theme.api.dto.BizThemeUpdateRequest;
import com.aspire.mirror.theme.api.dto.BizThemeUpdateResponse;
import com.aspire.mirror.theme.api.dto.ThemeDataCreateRequest;
import com.aspire.mirror.theme.api.dto.ThemeLogValidRequest;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.Constants;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.restful.common.ICommonRestfulClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.template.ItemsServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.theme.BizThemeServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.Authentication;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LastLogCodeEnum;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ResourceAuthHelper;
import com.migu.tsg.microservice.atomicservice.composite.exception.BaseException;
import com.migu.tsg.microservice.atomicservice.composite.exception.ResultErrorEnum;
import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil.jacksonBaseParse;

/**
 * 业务主题控制层
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller.theme
 * 类名称:    CompBizThemeController.java
 * 类描述:    业务主题控制层
 * 创建人:    JinSu
 * 创建时间:  2018/10/24 19:40
 * 版本:      v1.0
 */
@RestController
@RefreshScope
public class CompBizThemeController extends CommonResourceController implements ICompBizThemeService {
    private final Logger logger = LoggerFactory.getLogger(CompBizThemeController.class);

    @Autowired
    private BizThemeServiceClient bizThemeService;

//    @Autowired
//    protected CompositeResourceDao compositeResDao;

    @Autowired
    protected ResourceAuthHelper resAuthHelper;

//    @Autowired
//    private CmdbHelper cmdbHelper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ItemsServiceClient itemsService;

    @Value("${theme_access_url:http://localhost:8129/v1/theme/createThemeData}")
    private String themeAccessUrl;

    @Autowired
    private ICommonRestfulClient cmdbCommonService;
    /**
     * 创建主题
     *
     * @param createRequest 创建主题请求
     * @return 创建结果返回
     */
    @Override
    @ResAction(action = "create", resType = "theme")
    public CompBizThemeCreateResponse createdBizTheme(@RequestBody @Validated CompBizThemeCreateRequest createRequest) {
        logger.info("method[createdBizTheme] body is {}.", createRequest);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[createdTemplate]Username is {} namespace{}.", authCtx.getUser().getUsername(), authCtx.getUser
                ().getNamespace());
//        RbacResource rbacData = jacksonBaseParse(RbacResource.class, createRequest);
//        rbacData.setResTypeAction(authCtx.getResAction());
//        rbacData.setName(createRequest.getThemeName());
//        logger.info("[createdTemplate]The rbacResource is {}.", rbacData);
//        // 鉴权
////        resAuthHelper.resourceActionVerify(
////                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());
//        logger.info("user {} is trying to create template with name {}.",
//                authCtx.getUser().getUsername(), createRequest.getThemeName());
//        //  验证名字是否已经存在
//        CompositeResource existProjectRes = compositeResDao.queryResourceByName(authCtx.getUser().getNamespace(),
//                authCtx.getResType(), createRequest.getThemeName());
//        if (existProjectRes != null) {
//            String tipMsg = "The config with name '{}' for namespace {} is already exists.";
//            throw new BaseException(tipMsg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_ALREADY_EXIST,
//                    createRequest.getThemeName(), authCtx.getUser().getNamespace());
//        }
        if (StringUtils.isEmpty(createRequest.getUpSwitch())) {
            //设置上报开关开启
            createRequest.setUpSwitch(Constants.Theme.UP_SWiTCH);
        }
        if (StringUtils.isEmpty(createRequest.getStatus())) {
            //设置默认创建状态为临时
            createRequest.setStatus(Constants.Theme.STATUS);
        }
        BizThemeCreateRequest bizThemeCreateRequest = jacksonBaseParse(BizThemeCreateRequest.class, createRequest);
        BizThemeCreateResponse bizThemeCreateResponse = bizThemeService.createdBizTheme(bizThemeCreateRequest);

        // composite数据库中插入集群记录
//        CompositeResource compTemplate = new CompositeResource();
//        compTemplate.setUuid(bizThemeCreateResponse.getThemeId());
//        compTemplate.setName(bizThemeCreateResponse.getThemeName());
//        compTemplate.setType(Constants.Resource.RES_THEME);
//        compTemplate.setNamespace(authCtx.getUser().getNamespace());
//        compTemplate.setCreatedBy(authCtx.getUser().getUsername());
//        this.compositeResDao.insertCompositeResource(compTemplate);

        // TODO 记录用户创建主题事件
//        LogEventPayload logEvent = LogEventUtil.buildBasicLogEvent(authCtx, Constants.Resource.RES_REGION,
//                regionRes.getUuid(), regionRes.getName(), "delete", 1, "generic", regionRes);
//        LogEventUtil.popupDetail(logEvent, "region", regionRes.getName());
//        String jacksonJson = LogEventUtil.wrapLogEvents2Json(logEvent);
//        logClient.saveEventsLogInfo(jacksonJson);


        return jacksonBaseParse(CompBizThemeCreateResponse.class, bizThemeCreateResponse);
    }

    /**
     * 修改主题
     *
     * @param themeId       主题ID
     * @param updateRequest 修改请求
     * @return 修改结果返回
     */
    @Override
    @ResAction(action = "update", resType = "theme")
    public CompBizThemeUpdateResponse modifyByPrimaryKey(@PathVariable("theme_id") String themeId, @RequestBody
            CompBizThemeUpdateRequest updateRequest) {
        logger.info("method[modifyByPrimaryKey] themeId is {},  updateRequest body is {}", themeId, updateRequest);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[modifyByPrimaryKey]Username is {} namesapce is {}.", authCtx.getUser().getUsername(), authCtx
                .getUser().getNamespace());
////        CompositeResource compositeResource = new CompositeResource();
////        compositeResource.setUuid(themeId);
////        compositeResource.setNamespace(authCtx.getUser().getNamespace());
////        CompositeResource resource = this.compositeResDao.queryByUuidAndNamespace(themeId, authCtx.getUser()
////                .getNamespace());
//        resource.setName(updateRequest.getThemeName());
//        logger.info("[modifyByPrimaryKey] CompositeResource result is {}", resource);
        // 修改权限鉴权
//        resAuthHelper.resourceActionVerify(authCtx.getUser(), resource, authCtx.getResAction(),
//                authCtx.getFlattenConstraints());
//        CompositeResource existProjectRes = compositeResDao.queryResourceByName(authCtx.getUser().getNamespace(),
//                authCtx.getResType(), updateRequest.getThemeName());
//        if (existProjectRes != null && !existProjectRes.getUuid().equals(themeId)) {
//            String tipMsg = "The config with name '{}' for namespace {} is already exists.";
//            throw new BaseException(tipMsg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_ALREADY_EXIST,
//                    updateRequest.getThemeName(), authCtx.getUser().getNamespace());
//        }
        //调原子层做修改操作
        BizThemeUpdateRequest bizThemeUpdateRequest = jacksonBaseParse(BizThemeUpdateRequest.class, updateRequest);
        BizThemeUpdateResponse bizThemeUpdateResponse = bizThemeService.modifyByPrimaryKey(themeId,
                bizThemeUpdateRequest);

        //修改资源名称
//        compositeResDao.updateResourceNameById(resource.getId(), resource.getName());


        // TODO 记录用户更新主题事件
//        LogEventPayload logEvent = LogEventUtil.buildBasicLogEvent(authCtx, Constants.Resource.RES_REGION,
//                resource.getUuid(), resource.getName(), "update", 1, "generic", resource);
//        LogEventUtil.popupDetail(logEvent, "region", resource.getName());
//        String jacksonJson = LogEventUtil.wrapLogEvents2Json(logEvent);
//        logger.info("[modifyByPrimaryKey]log is {}.",jacksonJson);
//        logClient.saveEventsLogInfo(jacksonJson);
        return jacksonBaseParse(CompBizThemeUpdateResponse.class, bizThemeUpdateResponse);
    }

    /**
     * 删除主题
     *
     * @param themeId 主题ID
     */
    @Override
    @ResAction(action = "delete", resType = "theme")
    public void deleteByPrimaryKey(@PathVariable("theme_id") String themeId) {
        logger.info("method[modifyByPrimaryKey] themeId is {}", themeId);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[modifyByPrimaryKey]Username is {} namesapce is {}.", authCtx.getUser().getUsername(), authCtx
                .getUser().getNamespace());
//        CompositeResource compositeResource = new CompositeResource();
//        compositeResource.setUuid(themeId);
//        compositeResource.setNamespace(authCtx.getUser().getNamespace());
//        CompositeResource resource = this.compositeResDao.queryByUuidAndNamespace(themeId, authCtx.getUser()
//                .getNamespace());
//        resAuthHelper.resourceActionVerify(authCtx.getUser(), resource, authCtx.getResAction(),
//                authCtx.getFlattenConstraints());

        List<ItemsDetailResponse> listItems = itemsService.selectByThemeId(themeId);
        if (!CollectionUtils.isEmpty(listItems)) {
            String tipMsg = "The theme : {} is associated with a monitoring item and is not allowed to be deleted";
            throw new BaseException(tipMsg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.THEME_DELETE_UNSUCCESSFULLY,
                    themeId);
        }
        bizThemeService.deleteByPrimaryKey(themeId);

        //删除资源数据
//        compositeResDao.removeCompositeResource(authCtx.getUser().getNamespace(), Constants.Resource.RES_THEME,
//                themeId);
    }

    /**
     * 分页查询
     *
     * @param pageRequest 分页请求对象
     * @return 分页查询结果对象
     */
    @Override
//    @zhujian(a= xxx, b =resAuthHelper.claass c= method="verifyActionAndGetResource" )
    @ResAction(action = "view", resType = "theme")
    public PageResponse<CompBizThemeDetailResponse> pageList(@RequestBody @Validated CompBizThemePageRequest
                                                                     pageRequest) {
        System.out.println(themeAccessUrl);
        logger.info("method[pageList] pageRequest body is {}", pageRequest);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[list]Username is {}.", authCtx.getUser().getUsername());
//        RbacResource rbacData = jacksonBaseParse(RbacResource.class, pageRequest);
//        //查询鉴权
//        rbacData.setResTypeAction(authCtx.getResAction());
//        logger.info("[list]The rbacResource is {}.", rbacData);
        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
            Map<String, Set<String>> totalConstraints = resAuthHelper.verifyActionAndGetResource(
                    authCtx.getUser(), authCtx.getResAction(), authCtx.getFlattenConstraints());

            if (!super.applyGeneralAuthConstraints(totalConstraints, pageRequest)) {
                return new PageResponse<>();
            }
        }
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());

        BizThemePageRequest bizThemePageRequest = new BizThemePageRequest();
        BeanUtils.copyProperties(pageRequest, bizThemePageRequest);
        if (StringUtils.isEmpty(bizThemePageRequest.getSortName())) {
            bizThemePageRequest.setSortName("create_time");
        }
        if (StringUtils.isEmpty(bizThemePageRequest.getOrder())) {
            bizThemePageRequest.setOrder("desc");
        }
        PageResponse<BizThemeDetailResponse> pageResponse = bizThemeService.pageList(bizThemePageRequest);
        PageResponse<CompBizThemeDetailResponse> response = new PageResponse<CompBizThemeDetailResponse>();
        response.setCount(pageResponse.getCount());
        List<CompBizThemeDetailResponse> compBizThemeResponse = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageResponse.getResult())) {
            for (BizThemeDetailResponse bizThemeDetailResponse : pageResponse.getResult()) {
                CompBizThemeDetailResponse compBizThemeDetailResponse = new CompBizThemeDetailResponse();
                BeanUtils.copyProperties(bizThemeDetailResponse, compBizThemeDetailResponse);
                if (compBizThemeDetailResponse.getObjectType().equals("2")) {
//                    String bizName = cmdbHelper.getBizSysName();
                    Map<String, Object> params = Maps.newHashMap();
                    params.put("condicationCode", "business_detail");
                    params.put("token", "5245ed1b-6345-11e");
                    params.put("module_id", "9212e88a698d43cbbf9ec35b83773e2d");
                    params.put("id", compBizThemeDetailResponse.getObjectId());
                    Map<String, Object> resultInstance = cmdbCommonService.getInstanceDetail(params);
                    if (resultInstance != null && resultInstance.get("bizSystem") != null) {
                        String bizName = (String) resultInstance.get("bizSystem");
                        compBizThemeDetailResponse.setBizName(bizName);
                    }
                }
                compBizThemeResponse.add(compBizThemeDetailResponse);
            }
        }
        response.setResult(compBizThemeResponse);
        return response;
    }

    /**
     * 根据业务编码查询
     *
     * @param bizCode 业务编码
     * @return List<CompBizThemeDetailResponse> 主题列表
     */
    @Override
    @ResAction(action = "view", resType = "theme")
    public List<CompBizThemeDetailResponse> selectByBizCode(@PathVariable("biz_code") String bizCode) {
        logger.info("method[selectByBizCode] biz_code is {}", bizCode);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[selectByBizCode]Username is {}.", authCtx.getUser().getUsername());
//        RbacResource rbacData = new RbacResource();
//        //查询鉴权
//        rbacData.setResTypeAction(authCtx.getResAction());
//        logger.info("[findByPrimaryKey]The rbacResource is {}.", rbacData);
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());
        GeneralAuthConstraintsAware authParam = new GeneralAuthConstraintsAware();
        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
            Map<String, Set<String>> totalConstraints = resAuthHelper.verifyActionAndGetResource(
                    authCtx.getUser(), authCtx.getResAction(), authCtx.getFlattenConstraints());
            if (!super.applyGeneralAuthConstraints(totalConstraints, authParam)) {
                return new ArrayList<>();
            }
        }
        //调原子层
        BizThemeSearchRequest bizThemeSearchRequest = new BizThemeSearchRequest();
        // 赋数据权限字段
        BeanUtils.copyProperties(authParam, bizThemeSearchRequest);
        bizThemeSearchRequest.setObjectId(bizCode);
        bizThemeSearchRequest.setObjectType("2");
        List<BizThemeDetailResponse> listBizTheme = bizThemeService.select(bizThemeSearchRequest);
        List<CompBizThemeDetailResponse> response = Lists.newArrayList();
        for (BizThemeDetailResponse bizThemeDetailResponse : listBizTheme) {
            CompBizThemeDetailResponse compBizThemeDetailResponse = jacksonBaseParse(CompBizThemeDetailResponse
                    .class, bizThemeDetailResponse);
            response.add(compBizThemeDetailResponse);
        }
        return response;
    }

    /**
     * 根据关联对象类型获取主题列表
     *
     * @param objectType 关联对象类型
     * @return List<CompBizThemeDetailResponse> 主题列表
     */
    @Override
    public List<CompBizThemeDetailResponse> selectByObjectType(@PathVariable("object_type") String objectType) {
        logger.info("method[selectByObjectType] object_type is {}", objectType);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[selectByBizCode]Username is {}.", authCtx.getUser().getUsername());
        RbacResource rbacData = new RbacResource();
        //查询鉴权
        rbacData.setResTypeAction(authCtx.getResAction());
        logger.info("[findByPrimaryKey]The rbacResource is {}.", rbacData);
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());

        //调原子层
        BizThemeSearchRequest bizThemeSearchRequest = new BizThemeSearchRequest();
        bizThemeSearchRequest.setObjectType(objectType);
        bizThemeSearchRequest.setStatus("0");
        List<BizThemeDetailResponse> listBizTheme = bizThemeService.select(bizThemeSearchRequest);
        List<CompBizThemeDetailResponse> response = Lists.newArrayList();
        for (BizThemeDetailResponse bizThemeDetailResponse : listBizTheme) {
            CompBizThemeDetailResponse compBizThemeDetailResponse = jacksonBaseParse(CompBizThemeDetailResponse
                    .class, bizThemeDetailResponse);
            response.add(compBizThemeDetailResponse);
        }
        return response;
    }

    /**
     * 单条记录查询
     *
     * @param themeId 主题ID
     * @return CompBizThemeDetailResponse 主题详情
     */
    @Override
    @ResAction(action = "view", resType = "theme")
    public CompBizThemeDetailResponse findByPrimaryKey(@PathVariable("theme_id") String themeId) {
        logger.info("method[findByPrimaryKey]themeId is {}", themeId);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[findByPrimaryKey]Username is {}.", authCtx.getUser().getUsername());
//        CompositeResource resource = compositeResDao.queryResourceByUuid(authCtx.getUser().getNamespace(), Constants
//                .Resource.RES_THEME, themeId);
//        if (null == resource) {
//            String tipMsg = "The template with uuid '{}' for namespace {} is not exist.";
//            throw new BaseException(tipMsg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_NOT_EXIST,
//                    themeId, authCtx.getUser().getNamespace());
//        }
        GeneralAuthConstraintsAware authParam = new GeneralAuthConstraintsAware();
        if (!authCtx.getUser().isAdmin() && !authCtx.getUser().isSuperUser()) {
            Map<String, Set<String>> totalConstraints = resAuthHelper.verifyActionAndGetResource(
                    authCtx.getUser(), authCtx.getResAction(), authCtx.getFlattenConstraints());
            if (!super.applyGeneralAuthConstraints(totalConstraints, authParam)) {
                return new CompBizThemeDetailResponse();
            }
        }
        //调原子层
        BizThemeDetailResponse bizThemeDetailResponse = bizThemeService.findByPrimaryKey(themeId, authParam);
        CompBizThemeDetailResponse response = jacksonBaseParse(CompBizThemeDetailResponse.class,
                bizThemeDetailResponse);
        if (!CollectionUtils.isEmpty(bizThemeDetailResponse.getDimList())) {
            List<CompBizThemeDimDetailResponse> listCompDim = Lists.newArrayList();
            for (BizThemeDimDetailResponse bizThemeDimDetailResponse : bizThemeDetailResponse.getDimList()) {
                listCompDim.add(jacksonBaseParse(CompBizThemeDimDetailResponse.class, bizThemeDimDetailResponse));
            }
            response.setDimList(listCompDim);
        }
//        if (response.getObjectType().equals("2")) {
//            String bizName = response.getObjectId();
//            response.setBizName(bizName);
//        }

        return response;
    }

    /**
     * 获取主题名称是否存在资源
     *
     * @param themeName
     * @return
     */
    @Override
    @ResAction(action = "view", resType = "theme")
    public BaseResponse validThemeName(@PathVariable("theme_name") String themeName) {
//        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
//        CompositeResource existProjectRes = compositeResDao.queryResourceByName(authCtx.getUser().getNamespace(),
//                authCtx.getResType(), themeName);
//        if (existProjectRes != null) {
//            String tipMsg = "The config with name '{}' for namespace {} is already exists.";
//            throw new BaseException(tipMsg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_RESOURCE_ALREADY_EXIST,
//                    themeName, authCtx.getUser().getNamespace());
//        }
        return new BaseResponse();
    }

    /**
     * 接收主题数据
     *
     * @param themeDataCreateRequest 接收主题数据请求
     * @return Result 接收主题数据请求结果
     */
    @Override
    @Authentication(anonymous = true)
    public Result createThemeData(@RequestBody @Validated CompThemeDataCreateRequest themeDataCreateRequest) {
        String ip = getIpAddress(request);
        ThemeDataCreateRequest createRequest = jacksonBaseParse(ThemeDataCreateRequest.class, themeDataCreateRequest);
//        List<ThemeDataItemCreateRequest> themeDataItemList = Lists.newArrayList();
//        for (CompThemeDataItemCreateRequest compThemeDataItem : themeDataCreateRequest.getData()) {
//            themeDataItemList.add(jacksonBaseParse(ThemeDataItemCreateRequest.class, compThemeDataItem));
//        }
//        createRequest.setData(themeDataItemList);
        createRequest.setHost(ip);
        return bizThemeService.createThemeData(createRequest);
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * @param themeId
     * @param host
     * @param bizCode
     * @param themeCode
     * @return
     */
    @Override
    @ResAction(action = "view", resType = "theme")
    public CompBizThemeDataDetailResponse getThemeData(@PathVariable("theme_id") String themeId, @RequestParam(value
            = "host", required = false) String host, @RequestParam(value = "biz_code", required = false) String
                                                               bizCode, @RequestParam(value = "theme_code", required
            = false) String themeCode) {
        if (StringUtils.isEmpty(themeId)) {
            return null;
        }
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[getThemeData]Username is {}.", authCtx.getUser().getUsername());
//        RbacResource rbacData = new RbacResource();
//        rbacData.setUuid(themeId);
//        //查询鉴权
//        rbacData.setResTypeAction(authCtx.getResAction());
//        logger.info("[list]The rbacResource is {}.", rbacData);
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());
        BizThemeDataDetailResponse bizThemeDataDetail = bizThemeService.getThemeData(themeId, host, bizCode, themeCode);
        return jacksonBaseParse(CompBizThemeDataDetailResponse.class, bizThemeDataDetail);
    }

    @Override
    @ResAction(action = "view", resType = "theme")
    public CompBizThemeDataDetailResponse getThemeDataHis(@PathVariable("theme_id") String themeId) {
        if (StringUtils.isEmpty(themeId)) {
            return null;
        }
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[getThemeDataHis]Username is {}.", authCtx.getUser().getUsername());
//        RbacResource rbacData = new RbacResource();
//        rbacData.setUuid(themeId);
//        //查询鉴权
//        rbacData.setResTypeAction(authCtx.getResAction());
//        logger.info("[list]The rbacResource is {}.", rbacData);
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());
        BizThemeDataDetailResponse bizThemeDataDetail = bizThemeService.getThemeDataHis(themeId);
        return jacksonBaseParse(CompBizThemeDataDetailResponse.class, bizThemeDataDetail);
    }

    @Override
    @ResAction(action = "view", resType = "theme")
    public Map<String, Object> getTrendMapData(@RequestParam(value = "index_name") String indexName,
                                               @RequestParam(value = "theme_code") String themeCode,
                                               @RequestParam(value = "last_up_time_str") String lastUpTimeStr,
                                               @RequestParam(value = "start") String start,
                                               @RequestParam(value = "end") String end) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[pageList]Username is {}.", authCtx.getUser().getUsername());
        RbacResource rbacData = new RbacResource();
        //查询鉴权
        rbacData.setResTypeAction(authCtx.getResAction());
        logger.info("[list]The rbacResource is {}.", rbacData);
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());
        return bizThemeService.getTrendMapData(indexName, themeCode, lastUpTimeStr, start, end);
    }

    @Override
    @ResAction(action = "view", resType = "theme")
    public void downloadExplainDoc(HttpServletResponse response) {
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[downloadExplainDoc]Username is {}.", authCtx.getUser().getUsername());
        RbacResource rbacData = new RbacResource();
        //查询鉴权
        rbacData.setResTypeAction(authCtx.getResAction());
        logger.info("[list]The rbacResource is {}.", rbacData);
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());
        Resource res = new ClassPathResource(Constant.THEME_EXPLAIN_DOC_PATH);
        try {
            logger.debug("res isexist:{} path:{}", res.exists(), res.getFile().getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            response.setHeader("content-Type", "application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + "theme.doc");
            bis = new BufferedInputStream(res.getInputStream());
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    @Override
//    public String getThemeDateAccessUrl() {
//        return themeAccessUrl;
//    }

    @Override
    public List<Map<String, Object>> validLogContent(@RequestBody @Validated CompThemeLogValidRequest validParam) {
        ThemeLogValidRequest themeLogValidRequest = jacksonBaseParse(ThemeLogValidRequest.class, validParam);
        return bizThemeService.validLogContent(themeLogValidRequest);
    }
}
