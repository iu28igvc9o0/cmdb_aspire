package com.migu.tsg.microservice.atomicservice.composite.controller.template;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.service.template.ICompItemsService;
import com.aspire.mirror.composite.service.template.payload.CompItemsBatchCreateRequest;
import com.aspire.mirror.composite.service.template.payload.CompItemsCreateRequest;
import com.aspire.mirror.composite.service.template.payload.CompItemsCreateResponse;
import com.aspire.mirror.composite.service.template.payload.CompItemsDetailResponse;
import com.aspire.mirror.composite.service.template.payload.CompItemsPageRequest;
import com.aspire.mirror.composite.service.template.payload.CompItemsUpdateRequest;
import com.aspire.mirror.composite.service.template.payload.CompItemsUpdateResponse;
import com.aspire.mirror.composite.service.template.payload.CompMonitorApiServerConfig;
import com.aspire.mirror.composite.service.template.payload.CompMonitorDetailResponse;
import com.aspire.mirror.composite.service.template.payload.CompTemplateItemListRequest;
import com.aspire.mirror.template.api.dto.ApiServerConfigDetailResponse;
import com.aspire.mirror.template.api.dto.ItemsBatchCreateRequest;
import com.aspire.mirror.template.api.dto.ItemsCreateRequest;
import com.aspire.mirror.template.api.dto.ItemsCreateResponse;
import com.aspire.mirror.template.api.dto.ItemsDetailResponse;
import com.aspire.mirror.template.api.dto.ItemsPageRequest;
import com.aspire.mirror.template.api.dto.ItemsUpdateRequest;
import com.aspire.mirror.template.api.dto.ItemsUpdateResponse;
import com.aspire.mirror.template.api.dto.TemplateDetailResponse;
import com.google.common.collect.Maps;
import com.migu.tsg.microservice.atomicservice.composite.Constants;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.template.ApiServerConfigServiceCient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.template.ItemDatasCollectServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.template.ItemsServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.template.TemplateServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.Authentication;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.ResAction;
import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LogCodeDefine;
import com.migu.tsg.microservice.atomicservice.composite.service.common.payload.BaseResponse;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;

/**
 * 监控项管理实现
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.migu.tsg.microservice.atomicservice.composite.controller.template
 * 类名称:    CompItemsController.java
 * 类描述:    监控项管理实现
 * 创建人:    JinSu
 * 创建时间:  2018/8/8 15:29
 * 版本:      v1.0
 */
@RestController
@LogCodeDefine("1050812")
public class CompItemsController extends CommonResourceController implements ICompItemsService {

    private final Logger logger = LoggerFactory.getLogger(CompItemsController.class);
    @Autowired
    private ItemsServiceClient itemsService;

    @Autowired
    private TemplateServiceClient templateService;


//    @Autowired
//    private CompositeResourceDao compositeResDao;

    @Autowired
    private ApiServerConfigServiceCient apiServerConfigService;

    @Autowired
    private ItemDatasCollectServiceClient itemDatasCollectService;

    /**
     * 创建监控项
     *
     * @param itemsCreateRequest 监控项创建请求对象
     * @return CompItemsCreateResponse 创建监控项返回
     */
    @Override
    @ResAction(action = "create", resType = "template")
    public CompItemsCreateResponse createdItems(@RequestParam("resource_type") String resourceType, @RequestParam
            ("action") String action, @RequestBody @Validated CompItemsCreateRequest itemsCreateRequest) {
        logger.info("method[createdItems]  request body is {}.", itemsCreateRequest);
//        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
//        CompositeResource resource = this.compositeResDao.queryByUuidAndNamespace(itemsCreateRequest.getTemplateId(),
//                authCtx.getUser().getNamespace());
//        logger.debug("method[createdItems] resource: {}, resAction: {}, namespace:{}", resource, authCtx.getResAction
//                (), authCtx.getUser().getNamespace());
//        resAuthHelper.resourceActionVerify(authCtx.getUser(), resource, authCtx.getResAction(),
//                authCtx.getFlattenConstraints());

        if (null == itemsCreateRequest.getUnits()) {
            itemsCreateRequest.setUnits("");
        }
        ItemsCreateRequest createRequest = new ItemsCreateRequest();


        BeanUtils.copyProperties(itemsCreateRequest, createRequest);
        if (StringUtils.isEmpty(createRequest.getStatus())) {
            createRequest.setStatus(Constants.Item.STATUS);
        }
        if (StringUtils.isEmpty(createRequest.getType())) {
            createRequest.setType(Constants.Item.TYPE);
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        if (itemsCreateRequest.getDelay()!= null && pattern.matcher(itemsCreateRequest.getDelay()).matches()) {
            createRequest.setDelay(itemsCreateRequest.getDelay());
        }
        if (StringUtils.isEmpty(createRequest.getSysType())) {

            TemplateDetailResponse templateDetailResponse = templateService.findByPrimaryKey(createRequest.getTemplateId());
            createRequest.setSysType(templateDetailResponse.getSysType());
//            if (!StringUtils.isEmpty(createRequest.getBizThemeId())) {
//                createRequest.setSysType(Constants.Item.SYSTYPE_MIRROR);
//            } else {
//                createRequest.setSysType(Constants.Item.SYSTYPE_ZABBIX);
//            }
        }

        ItemsCreateResponse createResponse = itemsService.createdItems(createRequest);
        CompItemsCreateResponse response = new CompItemsCreateResponse();
        if (null != createRequest) {
            BeanUtils.copyProperties(createResponse, response);
        }
        logger.info("method[createdItems]  response is {}", response);
        return response;
    }

    /**
     * 批量创建监控项
     *
     * @param request 监控项批量创建请求对象
     * @return List<CompItemsCreateResponse> 批量创建监控项返回
     */
    @Override
    @ResAction(action = "create", resType = "template")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseResponse batchCreateItems(@RequestParam("resource_type") String resourceType, @RequestParam("action")
            String action, @RequestBody @Validated CompItemsBatchCreateRequest request) {
        logger.info("method[batchCreateItems] request body is {}.", request);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[batchCreateItems]Username is {}.", authCtx.getUser().getUsername());
//        List<String> templateIdList = Lists.newArrayList();
//        for (CompItemsCreateRequest compItemsCreateRequest : request.getListItem()) {
//            templateIdList.add(compItemsCreateRequest.getTemplateId());
//        }
//        List<CompositeResource> resources = this.compositeResDao.queryResourceByUuidlist(templateIdList);
        // 批量创建监控项权限鉴权
//        KeyValue<List<CompositeResource>, List<CompAuthFilterResponse>> authResult = resAuthHelper.filterResourceList
//                (authCtx.getUser(), authCtx.getResAction(), resources, authCtx.getFlattenConstraints());
//        List<CompAuthFilterResponse> authList = authResult.getValue();
//        if (authList.size() < resources.size()) {
//            throw new ResourceActionAuthException();
//        }
        //调原子层做批量创建
        ItemsBatchCreateRequest createRequest = new ItemsBatchCreateRequest();
        List<ItemsCreateRequest> itemsCreateRequestList = Lists.newArrayList();
        for (CompItemsCreateRequest compItemsCreateRequest : request.getListItem()) {
            if (StringUtils.isEmpty(compItemsCreateRequest.getStatus())) {
                compItemsCreateRequest.setStatus(Constants.Template.STATUS);
            }
            if (StringUtils.isEmpty(compItemsCreateRequest.getType())) {
                compItemsCreateRequest.setType(Constants.Item.TYPE);
            }
            if (StringUtils.isEmpty(compItemsCreateRequest.getSysType())) {
                TemplateDetailResponse templateDetailResponse = templateService.findByPrimaryKey(compItemsCreateRequest.getTemplateId());
                compItemsCreateRequest.setSysType(templateDetailResponse.getSysType());
            }
            if (null == compItemsCreateRequest.getUnits()) {
                compItemsCreateRequest.setUnits("");
            }
            ItemsCreateRequest itemsCreateRequest = new ItemsCreateRequest();
            BeanUtils.copyProperties(compItemsCreateRequest, itemsCreateRequest);
            Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
            if (compItemsCreateRequest.getDelay()!= null && pattern.matcher(compItemsCreateRequest.getDelay()).matches()) {
                itemsCreateRequest.setDelay(compItemsCreateRequest.getDelay());
            }
            itemsCreateRequestList.add(itemsCreateRequest);
        }
        createRequest.setListItem(itemsCreateRequestList);
        List<ItemsCreateResponse> createResponse = itemsService.batchCreateItems(createRequest);
        logger.info("method[batchCreateItems]  response is {}", createResponse);
        return new BaseResponse();
    }

    /**
     * 批量删除监控项
     *
     * @param itemIds 主键（以逗号分隔）
     * @return BaseResponse 批量删除监控项返回
     */
    @Override
    @ResAction(action = "delete", resType = "template")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BaseResponse deleteByPrimaryKeyArrays(@PathVariable("item_ids") String itemIds, @RequestParam
            ("resource_type") String resourceType, @RequestParam("action") String action) {
        logger.info("method[deleteByPrimaryKeyArrays] request itemIds is {}.", itemIds);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[deleteByPrimaryKeyArrays]Username is {},namespace is {}.", authCtx.getUser().getUsername(),
                authCtx.getUser().getNamespace());
        RbacResource rbacData = new RbacResource();
        rbacData.setResTypeAction(authCtx.getResAction());
        logger.info("[deleteByPrimaryKeyArrays]The rbacResource is {}.", rbacData);
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());
        itemsService.deleteByPrimaryKeyArrays(itemIds);
        return new BaseResponse();
    }

    /**
     * 监控项修改请求
     *
     * @param itemId
     * @param itemsUpdateRequest 监控项修改请求对象
     * @return CompItemsUpdateResponse 监控项修改返回对象
     */
    @Override
    @ResAction(action = "update", resType = "template")
    public CompItemsUpdateResponse modifyByPrimaryKey(@PathVariable("item_id") String itemId, @RequestParam
            ("resource_type") String resourceType, @RequestParam("action") String action, @RequestBody
            CompItemsUpdateRequest itemsUpdateRequest) {
        logger.info("method[modifyByPrimaryKey] request body is {}.", itemsUpdateRequest);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[modifyByPrimaryKey]Username is {},namespace is {}.", authCtx.getUser().getUsername(), authCtx
                .getUser().getNamespace());
//        CompositeResource resource = this.compositeResDao.queryByUuidAndNamespace(itemsUpdateRequest.getTemplateId(),
//                authCtx.getUser().getNamespace());
//        resAuthHelper.resourceActionVerify(authCtx.getUser(), resource, authCtx.getResAction(),
//                authCtx.getFlattenConstraints());
        ItemsUpdateRequest updateRequest = new ItemsUpdateRequest();
        BeanUtils.copyProperties(itemsUpdateRequest, updateRequest);
        ItemsUpdateResponse updateResponse = itemsService.modifyByPrimaryKey(itemId, updateRequest);
        CompItemsUpdateResponse response = new CompItemsUpdateResponse();
        BeanUtils.copyProperties(updateResponse, response);
        return response;
    }

    /**
     * 监控项详情
     *
     * @param itemId 监控项主键
     * @return CompItemsDetailResponse 监控项详情返回对象
     */
    @Override
    @ResAction(action = "view", resType = "template")
    public CompItemsDetailResponse findByPrimaryKey(@PathVariable("item_id") String itemId) {
        logger.info("method[findByPrimaryKey] request itemId is {}.", itemId);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[findByPrimaryKey]Username is {},namespace is {}.", authCtx.getUser().getUsername(), authCtx
                .getUser().getNamespace());
        RbacResource rbacData = new RbacResource();
        rbacData.setResTypeAction(authCtx.getResAction());
        logger.info("[findByPrimaryKey]The rbacResource is {}.", rbacData);
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());
        ItemsDetailResponse detailResponse = itemsService.findByPrimaryKey(itemId);
        CompItemsDetailResponse response = new CompItemsDetailResponse();
        BeanUtils.copyProperties(detailResponse, response);
        return response;
    }

    /**
     * 监控项列表
     *
     * @param request 分页查询监控对象
     * @return
     */
    @Override
    @ResAction(action = "view", resType = "template")
    public PageResponse<CompItemsDetailResponse> pageList(@RequestBody CompItemsPageRequest request) {
        logger.info("method[pageList] request body is {}.", request);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        logger.info("[pageList]Username is {},namespace is {}.", authCtx.getUser().getUsername(), authCtx.getUser()
                .getNamespace());
        RbacResource rbacData = new RbacResource();
        rbacData.setResTypeAction(authCtx.getResAction());
        logger.info("[pageList]The rbacResource is {}.", rbacData);
//        resAuthHelper.resourceActionVerify(
//                authCtx.getUser(), rbacData, authCtx.getResAction(), authCtx.getFlattenConstraints());
        ItemsPageRequest pageRequest = new ItemsPageRequest();
        BeanUtils.copyProperties(request, pageRequest);
        if (pageRequest.getPageNo() == null) {
            pageRequest.setPageNo(1);
        }
        if (pageRequest.getPageSize() == null) {
            pageRequest.setPageSize(1000);
        }
        PageResponse<ItemsDetailResponse> pageResponse = itemsService.pageList(pageRequest);
        PageResponse<CompItemsDetailResponse> response = new PageResponse();
        response.setCount(pageResponse.getCount());
        List<CompItemsDetailResponse> compItemsDetailResponselist = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(pageResponse.getResult())) {
            for (ItemsDetailResponse itemsDetailResponse : pageResponse.getResult()) {
                CompItemsDetailResponse detailResponse = new CompItemsDetailResponse();
                BeanUtils.copyProperties(itemsDetailResponse, detailResponse);
                compItemsDetailResponselist.add(detailResponse);
            }
        }
        response.setResult(compItemsDetailResponselist);
        return response;
    }

    @Override
    @ResAction(action = "view", resType = "template")
    public PageResponse<CompMonitorDetailResponse> relationItemList(@RequestParam("room") String room, @RequestParam
            (value = "name", required = false) String name, @RequestParam(value = "template_id", required = false)
            String templateId, @RequestParam(value = "key", required = false) String key) {
        if (StringUtils.isEmpty(room)) {
            logger.error("查询条件机房不能为空");
            return null;
        }
        ApiServerConfigDetailResponse apiServerConfigDetailResponse = apiServerConfigService.findByRoom(room);
        if (apiServerConfigDetailResponse == null) {
            logger.error("机房没有zabbix配置");
            return null;
        }
        CompTemplateItemListRequest request = new CompTemplateItemListRequest();
        CompMonitorApiServerConfig config = new CompMonitorApiServerConfig();
        BeanUtils.copyProperties(apiServerConfigDetailResponse, config);
        config.setServerType(CompMonitorApiServerConfig.SERVER_TYPE_ZABBIX);
        request.setApiServerConfig(config);
        Map<String, Object> params = Maps.newHashMap();
        params.put("item_name", name);
        params.put("template_id", Integer.valueOf(templateId));
        params.put("item_key", key);
        request.setParams(params);
        List<CompMonitorDetailResponse> response = itemDatasCollectService.queryZbxTemplateItemList(request);
        PageResponse<CompMonitorDetailResponse> pageResponse = new PageResponse<CompMonitorDetailResponse>();
        pageResponse.setCount(response.size());
        pageResponse.setResult(response);
        return pageResponse;
    }

    @Override
    @Authentication(anonymous = true)
    public List<Map<String, Object>> getThemeCalcResult(@PathVariable("item_id") String itemId, @RequestParam(value = "start_time", required = false) String startTime, @RequestParam(value="end_time", required = false) String endTime) {
        return itemsService.getThemeCalcResult(itemId, startTime, endTime);
    }
}
