package com.migu.tsg.microservice.atomicservice.composite.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import com.aspire.mirror.composite.service.inspection.payload.ConfigDict;
import com.aspire.mirror.ops.api.domain.ChildGroupQueryModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.migu.tsg.microservice.atomicservice.composite.AbstractPrivilegeResource;
import com.migu.tsg.microservice.atomicservice.composite.AbstractSpacePrivilegeResource;
import com.migu.tsg.microservice.atomicservice.composite.Constants;
import com.migu.tsg.microservice.atomicservice.composite.PaginateResponse;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.BizSystemClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.ConfigDictClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance.InstanceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.log.LogClientService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.opsmanage.OpsGroupClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.rabc.payload.response.CompAuthFilterResponse;
import com.migu.tsg.microservice.atomicservice.composite.common.CompResHelper;
import com.migu.tsg.microservice.atomicservice.composite.common.KeyValue;
import com.migu.tsg.microservice.atomicservice.composite.common.sql.SelectBuilder;
import com.migu.tsg.microservice.atomicservice.composite.common.sql.WhereBuilder;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.logcontext.LastLogCodeEnum;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PaginateUtil;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.ResourceAuthHelper;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.RoleUtils;
import com.migu.tsg.microservice.atomicservice.composite.dao.CompositeResourceDao;
import com.migu.tsg.microservice.atomicservice.composite.dao.po.CompositeResource;
import com.migu.tsg.microservice.atomicservice.composite.exception.BaseException;
import com.migu.tsg.microservice.atomicservice.composite.exception.ResultErrorEnum;
import com.migu.tsg.microservice.atomicservice.composite.vo.rbac.RbacResource;

//import com.migu.tsg.microservice.atomicservice.composite.controller.registries.RegistriesUtil;


public class CommonResourceController {
    protected static final Pattern UUID_PATTERN = Pattern.compile("^[\\da-f]{8}-([\\da-f]{4}-){3}[\\da-f]{12}$",
            Pattern.CASE_INSENSITIVE);

    protected static final String ATTR_SPACE_RES = "space_resource";

    private static final int MAX_LEN = 128;

    public static final String PRECINT_ID_WILDCARD_SUFFIX = "-*";

    private static final String REQUEST_BODY_OBJECT_ATTR = CommonResourceController.class.getSimpleName()
            + "::requestBodyBean";

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonResourceController.class);

    @Autowired
    protected CompositeResourceDao resDao;

    @Autowired
    protected ResourceAuthHelper resAuthHelper;

    @Autowired
    protected LogClientService logClient;

    @Autowired
    protected RoleUtils roleUtils;

    @Autowired
    protected ConfigDictClient configDict;

    @Autowired
    protected InstanceClient instanceClient;

    @Autowired
    protected BizSystemClient bizSystemClient;

    @Autowired
    protected OpsGroupClient opsGroupClient;

    @Value("${systemType:}")
    private String systemType;

//    @Autowired
//    protected PrecinctServiceClient	precinctService;
    /*@Autowired
    private RegistriesUtil registryUtil;*/

    protected String resourceType = "service";
    protected String defaultLookupUrlFieldName = "name";
    protected String defaultLookupFieldName = "name";
    protected String defaultUuidFieldName = "uuid";
    protected String defaultSpaceFieldName = "space_name";
    protected String defaultSpaceUuidFieldName = "space_uuid";
    protected String subResourceOf = null;

    protected boolean addResourceConstraints = true;

    //commons/OrganizationResourceViewSet.get_queryset()
    public WhereBuilder<SelectBuilder> baseResQuery(Map<String, Object> args) {
        return new SelectBuilder()
                .where()
                .equalsTo(Constants.ResourceColumn.NAMESPACE, getOrgAccount())
                .equalsTo(Constants.ResourceColumn.TYPE, this.resourceType)
                .equalsTo(Constants.ResourceColumn.PROJECT_UUID, getProjectUuid());
    }

    @SuppressWarnings("unchecked")
    public WhereBuilder<SelectBuilder> baseResQuery() {
        return baseResQuery(Collections.EMPTY_MAP);
    }

    @SuppressWarnings("unchecked")
    protected Map<String, String> getRequestTempVars() {
        return (Map<String, String>) this.getRequestAttr(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    }

    // commons/OrganizationResourceViewSet.get_request_namespace()
    @Deprecated
    public String getRequestNamespace() {
        return getOrgAccount();
    }

    public String getOrgAccount() {
        Map<String, String> attrs = getRequestTempVars();

        String s = attrs.get("org_name");
        if (s != null && s.length() > 0) {
            return s;
        }

        s = attrs.get("orgAccount");
        if (s != null && s.length() > 0) {
            return s;
        }

        s = attrs.get("namespace");
        if (s != null && s.length() > 0) {
            return s;
        }

        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
        return reqCtx.getUser().getUsername();
    }

    // commons/RoleBaseViewSet.get_project_uuid()
    public String getProjectUuid() {
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = attrs.getRequest();

        Object uuid = req.getAttribute("project_uuid");
        if (uuid != null) {
            return (String) uuid;
        }

        String projectName = reqCtx.getParameterSingleValue("project_name");
        String orgAccount = getOrgAccount();

        CompositeResource res = resDao.queryResourceByName(orgAccount, Constants.RbacResource.PROJECT, projectName);
        if (res != null) {
            uuid = res.getUuid();
            req.setAttribute("project_uuid", uuid);
            return (String) uuid;
        }

        return "";
    }

    /**
     * 处理  站点-设备类型-设备  的权限请求参数. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param totalConstraints
     * @param awareModel
     */
    protected boolean applyGeneralAuthConstraints(final Map<String, Set<String>> totalConstraints, final
    GeneralAuthConstraintsAware awareModel) {
        // 个性化判断是否是去RBAC改造
        if ("simple".equals(systemType)) {
            return true;
        }

        Set<String> idcTypeList = totalConstraints.get(ResourceAuthHelper.AREA);
        Set<String> roomIdSet = totalConstraints.get(ResourceAuthHelper.ROOM); 
        Set<String> deviceTypeList = totalConstraints.get(ResourceAuthHelper.DEVICE_TYPE);
        Set<String> bizSystemList = totalConstraints.get(ResourceAuthHelper.BIZ_SYSTEM);
        Set<String> deviceIdList = totalConstraints.get(ResourceAuthHelper.DEVICE);
        if (!CollectionUtils.isEmpty(idcTypeList)) {
        	awareModel.setAuthIdcIdList(new ArrayList<>(idcTypeList));
        }
        if (!CollectionUtils.isEmpty(roomIdSet)) {
        	roomIdSet = handlePrecintWildcard(roomIdSet);
        	awareModel.setAuthPrecinctList(new ArrayList<>(roomIdSet));
        }	
        if (!CollectionUtils.isEmpty(deviceTypeList)) {
            deviceTypeList = handleDeviceTypeWildcard(deviceTypeList);
            awareModel.setAuthDeviceTypeList(new ArrayList<>(deviceTypeList));
        }
        if (!CollectionUtils.isEmpty(bizSystemList)) {
            bizSystemList = handleBizSysWildcard(bizSystemList);
            awareModel.setAuthBizSystemIdList(new ArrayList<>(bizSystemList));
        }
        if (!CollectionUtils.isEmpty(deviceIdList)) {
            awareModel.setAuthDeviceIdList(new ArrayList<>(deviceIdList));
        }
       
        Set<String> pipelineIdList = totalConstraints.get(ResourceAuthHelper.OPS_PIPELINE);
        Set<String> scriptIdList = totalConstraints.get(ResourceAuthHelper.OPS_SCRIPT);
        Set<String> yumIdList = totalConstraints.get(ResourceAuthHelper.OPS_YUM);
        Set<String> groupIdList = totalConstraints.get(ResourceAuthHelper.OPS_GROUP);
        if (!CollectionUtils.isEmpty(pipelineIdList)) {
            awareModel.setAuthPipelineIdList(pipelineIdList.stream().map(item -> item.substring(item.lastIndexOf("_")
                    + 1
            )).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(scriptIdList)) {
            awareModel.setAuthScriptIdList(scriptIdList.stream().map(item -> item.substring(item.lastIndexOf("_") + 1
            )).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(yumIdList)) {
            awareModel.setAuthYumIdList(yumIdList.stream().map(item -> item.substring(item.lastIndexOf("_") + 1
            )).collect(Collectors.toList()));
        }

        if (!CollectionUtils.isEmpty(groupIdList)) {
            ChildGroupQueryModel childGroupQueryModel = new ChildGroupQueryModel();
            childGroupQueryModel.setGroupIdList(new ArrayList<>(groupIdList));
            List<String> tempGroupIdList = opsGroupClient.queryAllChildGroup(childGroupQueryModel);
//            tempGroupIdList.addAll(groupIdList);
            awareModel.setAuthGroupIdList(new ArrayList<>(tempGroupIdList));
        }
        return awareModel.checkAuthConditions();
    }
//    private void addChildGroup(List<String> opsGroupList, Long groupId) {
//        List<OpsGroup> subGroupList = opsGroupDao.queryGroupByParentId(groupId);
//        if (!CollectionUtils.isEmpty(subGroupList)) {
//            for (OpsGroup opsGroup: subGroupList) {
//                if (!opsGroupList.contains(opsGroup.getGroupId().toString())) {
//                    opsGroupList.add(opsGroup.getGroupId().toString());
//                    addChildGroup(opsGroupList, opsGroup.getGroupId());
//                }
//            }
//        }
//    }
    // 参数为Map<String,Object>
    protected boolean applyGeneralAuthConstraintsWithMap(final Map<String, Set<String>> totalConstraints, Map<String,
            Object> awareModel) {
        // 个性化判断是否是去RBAC改造
        if ("simple".equals(systemType)) {
            return true;
        }

        Set<String> idcTypeList = totalConstraints.get(ResourceAuthHelper.AREA);
        Set<String> roomIdSet = totalConstraints.get(ResourceAuthHelper.ROOM); 
        Set<String> deviceTypeList = totalConstraints.get(ResourceAuthHelper.DEVICE_TYPE);
        Set<String> bizSystemList = totalConstraints.get(ResourceAuthHelper.BIZ_SYSTEM);
        Set<String> deviceIdList = totalConstraints.get(ResourceAuthHelper.DEVICE);

        if (!CollectionUtils.isEmpty(deviceIdList)) {
            if (!transFormatter("id", deviceIdList, awareModel)) {
                return false;
            }
        } else {
            if (!CollectionUtils.isEmpty(idcTypeList)) {
            	if (!transFormatter("idcType", roomIdSet, awareModel)) {
                    return false;
                }
            }
            if (!CollectionUtils.isEmpty(roomIdSet)) {
            	if (!transFormatter("roomId", roomIdSet, awareModel)) {
                    return false;
                }
            }
            if (!CollectionUtils.isEmpty(deviceTypeList)) {
                deviceTypeList = handleDeviceTypeWildcard(deviceTypeList);
                if (!transFormatter("device_type", deviceTypeList, awareModel)) {
                    return false;
                }
            }
            if (!CollectionUtils.isEmpty(bizSystemList)) {
                bizSystemList = handleBizSysWildcard(bizSystemList);
                if (!transFormatter("bizSystem", bizSystemList, awareModel)) {
                    return false;
                }
            }
        }
        return !CollectionUtils.isEmpty(idcTypeList) || !CollectionUtils.isEmpty(roomIdSet) 
        		|| !CollectionUtils.isEmpty(deviceTypeList) || !CollectionUtils.isEmpty(deviceIdList) 
        		|| !CollectionUtils.isEmpty(bizSystemList);
    }

    /*
     *  将set<String>转为字符串加到请求参数中
     *  @key params中的key值
     *  @rsSet 参考的权限集合
     *  @params 请求的参数
     * */
    private boolean transFormatter(String key, Set<String> rsSet, Map<String, Object> params) {
        boolean rs = false;
        // 判断用户参数中是否存在key值
        if (params.containsKey(key)) {
            // 存在，则判断它是否在用户允许的权限集合中
            String value = params.get(key).toString();
            // 直接返回，不做处理
            // 需要访问的条件不在权限集合中，直接返回空
            rs = rsSet.contains(value);
        } else {
            // 不存在，则将用户允许的权限集合，组装成字符串，加到请求参数中
            StringBuilder sbStr = null;
            boolean flag = true;
            for (String item : rsSet) {
                if (flag) {
                    sbStr = new StringBuilder();
                    sbStr.append(item);
                    flag = false;
                } else {
                    sbStr.append(",");
                    sbStr.append(item);
                }
            }
            if (sbStr != null) {
                params.put(key, sbStr.toString());
            }
            rs = true;
        }
        return rs;
    }

    Set<String> handleIdcWildcard(Set<String> idcSet) {
        Set<String> result = new HashSet<String>();
//        List<ConfigDict> allConfigDict = configDict.getDictsByType("roomId", null, null, null);
        List<Map<String, String>> listIdc = instanceClient.getIdcByIds(Joiner.on(",").join(idcSet));

        for (Map<String, String> configDict : listIdc) {
            result.add(configDict.get("code"));
        }
        return result;
    }

    Set<String> handleBizSysWildcard(Set<String> bizSystemList) {
        Set<String> result = new HashSet<String>();
//        List<ConfigDict> allConfigDict = configDict.getDictsByType("bizSystem", null, null, null);
        List<Map<String, String>> mapList = bizSystemClient.getBizSystemByIds(Joiner.on(",").join(bizSystemList));
        for (Map<String, String> configDict : mapList) {
            result.add(configDict.get("code"));
        }
        return result;
    }

    Set<String> handleDeviceTypeWildcard(Set<String> deviceTypeList) {
        Set<String> result = new HashSet<String>();

        List<ConfigDict> allConfigDict = configDict.getDictsByType("device_type", null, null, null);
        for (ConfigDict configDict : allConfigDict) {
            if (deviceTypeList.contains(configDict.getId())) {
                result.add(configDict.getValue());
            }
        }
        return result;
    }

    /**
     * 处理precint通配符, 把通配符下的所有precint都查出来 <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param rawPrecintSet
     * @return
     */
    Set<String> handlePrecintWildcard(Set<String> rawPrecintSet) {
        Set<String> result = new HashSet<String>();

//        List<ConfigDict> allConfigDict = configDict.getDictsByType("roomId", null, null, null);
        List<Map<String, String>> listroom = instanceClient.getRoomByIds(Joiner.on(",").join(rawPrecintSet));

        for (Map<String, String> configDict : listroom) {
            result.add(configDict.get("code"));
        }
//        for (String precintId : rawPrecintSet) {
//            if (!precintId.endsWith(PRECINT_ID_WILDCARD_SUFFIX)) {
//                result.add(precintId);
//                continue;
//            }
//            String parentPrecintId = precintId.substring(0, precintId.length() - PRECINT_ID_WILDCARD_SUFFIX.length());
//            result.add(parentPrecintId);
//            Map<String, Object> map = precinctService.getListLikePrecinctId(parentPrecintId, true);
//            if (map.containsKey("data")) {
//                TypeReference<List<String>> typeRef = new TypeReference<List<String>>() {};
//                List<String> subPrecintIdList = jacksonBaseParse(typeRef, map.containsKey("data"));
//                result.addAll(subPrecintIdList);
//            }
//        }
        return result;
    }

    // commons/OrganizationResourceViewSet.filter_user_resource()
    public KeyValue<List<CompositeResource>, List<CompAuthFilterResponse>> filterUserResource(String orgAccount,
                                                                                              List<CompositeResource>
                                                                                                      ress,
                                                                                              Map<String, String>
                                                                                                      constraints) {
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();

        return resAuthHelper.filterResourceList(reqCtx.getUser(), reqCtx.getResAction(), ress, constraints);
    }

    @SuppressWarnings("unchecked")
    public KeyValue<List<CompositeResource>, List<CompAuthFilterResponse>> filterUserResource(String orgAccount,
                                                                                              List<CompositeResource>
                                                                                                      ress) {
        return filterUserResource(orgAccount, ress, Collections.EMPTY_MAP);
    }

    public String getProjectName() {
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
        String s = reqCtx.getParameterSingleValue("project_name");
        return s != null ? s : "";
    }

    // commons/OrganizationResourceViewSet.add_list_privilege()
    public List<? extends AbstractPrivilegeResource> addListPrivilege(String orgAccount,
                                                                      List<? extends AbstractPrivilegeResource> resps,
                                                                      List<CompAuthFilterResponse> privs,
                                                                      List<CompositeResource> ress) {
        if (resps == null || resps.size() == 0) {
            return resps;
        }

        if (resps.get(0) instanceof AbstractSpacePrivilegeResource) {
            // return_space_and_project = true
            // @todo:原代码中做了查询，是否需要？
            CompResHelper.copyProps(ress, resps, new String[]{"projectUuid,projectName,spaceUuid,spaceName"},
                    new String[]{"uuid:privResUuid"});
        }

        CompResHelper.copyProps(privs, resps, new String[]{"resTypeActionList:resourceActions"},
                new String[]{"resource.uuid:privResUuid"});

        return resps;
    }

    // commons/OrganizationResourceViewSet.add_privilege()
    public AbstractPrivilegeResource addPrivilege(
            String orgAccount, AbstractPrivilegeResource resp, CompositeResource res) {
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
        ///	废代码，暂时保留三个月
//        if (orgAccount == null || orgAccount.length() == 0) {
//            orgAccount = this.getOrgAccount();
//        }

        List<String> actions = resAuthHelper.resourceActions(reqCtx.getUser(), reqCtx.getResType(),
                reqCtx.getFlattenConstraints(), res.getFlatten());

        if (resp instanceof AbstractSpacePrivilegeResource) {
            AbstractSpacePrivilegeResource spr = (AbstractSpacePrivilegeResource) resp;
            spr.setProjectUuid(res.getProjectUuid());
            spr.setProjectName(res.getProjectName());
            spr.setSpaceUuid(res.getSpaceUuid());
            spr.setSpaceName(res.getSpaceName());
        }

        resp.setResourceActions(actions);

        return resp;
    }

    public AbstractPrivilegeResource addPrivilege(AbstractPrivilegeResource resp, CompositeResource res) {
        return addPrivilege(null, resp, res);
    }

    public PaginateResponse paginateResponse(int count, List<?> results) {
        PaginateResponse resp = new PaginateResponse();

        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
        int pageCount = PaginateUtil.resolveResultPageCount(reqCtx, count);
        int pageSize = PaginateUtil.resolveRequestPageInfo(reqCtx).getValue();

        resp.setTotalCount(count);
        resp.setPageCount(pageCount);
        resp.setPageSize(pageSize);
        resp.setResults(results);

        return resp;
    }

    public PaginateResponse paginateResponseEmpty() {
        return paginateResponse(0, null);
    }

    public Pair<Integer, Integer> getPagination(int defaultPageSize) {
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();

        int pageSize = defaultPageSize;

        String s = reqCtx.getParameterSingleValue("page_size");
        if (s != null && s.length() > 0) {
            pageSize = Integer.parseInt(s);
        }

        int page = 1;
        s = reqCtx.getParameterSingleValue("page");
        if (s != null && s.length() > 0) {
            page = Integer.parseInt(s);
        }

        return Pair.of(page, pageSize);
    }

    public Pair<Integer, Integer> getPagination() {
        return getPagination(20);
    }

    // roles/contraints.get_constraint_by_type()
    public Map<String, String> getConstraintByType(String type, String val) {
        return roleUtils.getConstraints(type, val);
    }

    // commons/OrganizationResourceViewSet.get_object()
    public CompositeResource getResource(WhereBuilder<SelectBuilder> builder, Map<String, String> filter,
                                         boolean validate, Map<String, String> constraints, String lookupUrlName,
                                         String lookupFieldName) {
        if (builder == null) {
            builder = baseResQuery();
        }

        if (filter == null) {
            filter = getFilter(lookupUrlName, lookupFieldName);
        }

//        if(filter!=null) {
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            builder.equalsTo(entry.getKey(), entry.getValue(), true);
        }
//        }

        List<CompositeResource> ress = resDao.queryResourcesByConds(builder.build(), null);
        if (ress == null || ress.size() == 0) {
            throw new RuntimeException("Resource not exist");
        }

        CompositeResource res = ress.get(0);

        String orgAccount = this.getOrgAccount();
        if (validate) {
            if (constraints == null) {
                constraints = new HashMap<>();
            }

            if (subResourceOf != null) {
                constraints.putAll(this.getConstraintByType(subResourceOf, res.getName()));
            }

            if (addResourceConstraints) {
                constraints.putAll(getResourceConstraints(res));
            }

            this.validateAction(orgAccount, res, constraints);
        }

        return res;
    }

    public CompositeResource getResource() {
        return this.getResource(null, null, true, null, null, null);
    }

    //commons/RoleBaseViewSet.validate_action()
    public void validateAction(String orgAccount, CompositeResource resource, Map<String, String> constraints) {
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();

        // @todo:没有使用orgAccount(namespace)?
        resAuthHelper.resourceActionVerify(reqCtx.getUser(), resource, reqCtx.getResAction(), constraints);
    }

    // RoleBaseViewSet.validate_action()
    public boolean validateAction(String orgAccount, Object resource, Map<String, String> constraints) {
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();

        String name = (String) CompResHelper.getProperty(resource, defaultLookupFieldName); // name
        String uuid = (String) CompResHelper.getProperty(resource, defaultUuidFieldName); // uuid

        RbacResource rabcRes = new RbacResource();
        rabcRes.setName(name);
        rabcRes.setUuid(uuid);

        if (constraints == null) {
            constraints = reqCtx.getFlattenConstraints();
        } else {
            constraints.putAll(reqCtx.getFlattenConstraints());
        }

        resAuthHelper.resourceActionVerify(reqCtx.getUser(), rabcRes, reqCtx.getResAction(), constraints);

        return true;
    }

    // validate_mix_filter
    public List<CompAuthFilterResponse> validateMixFilter(String orgAccount, List<RbacResource> rbacResList,
                                                          Map<String, String> constraints) {
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
        return resAuthHelper.resourcesMixedFilter(reqCtx.getUser(), rbacResList, constraints);
    }

    @SuppressWarnings("unchecked")
    public List<CompAuthFilterResponse> validateMixFilter(String orgAccount, List<RbacResource> rbacResList) {
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
        return resAuthHelper.resourcesMixedFilter(reqCtx.getUser(), rbacResList, Collections.EMPTY_MAP);
    }

    // commons/OrganizationResourceViewSet.get_filter_kwargs()
    public Map<String, String> getFilter(String lookupUrlName, String lookupFieldName) {
        String urlName;
        if (lookupUrlName != null && lookupUrlName.length() > 0) {
            urlName = lookupUrlName;
        } else {
            urlName = defaultLookupUrlFieldName;
        }

        String nameField;
        if (lookupFieldName != null && lookupFieldName.length() > 0) {
            nameField = lookupFieldName;
        } else {
            nameField = defaultLookupFieldName;
        }

        Map<String, String> attrs = getRequestTempVars();
        String urlNameVal = attrs.get(urlName);

        Map<String, String> result = new HashMap<>();

        if (urlNameVal != null && this.isValidUuid(urlNameVal)) {
            nameField = defaultUuidFieldName;
        } else {
            String spaceName = this.getSpaceName(null);
            if (spaceName != null && spaceName.length() > 0) {
                result.put(defaultSpaceUuidFieldName, this.getSpaceUuid());
            }
        }

        result.put(nameField, urlNameVal); // @todo:urlNameVal==null也加入？

        return result;
    }

    public boolean isValidUuid(String val) {
        return UUID_PATTERN.matcher(val).matches();
    }

    public CompositeResource getSpaceResource(String orgAccount, String spaceName) {
        return resDao.queryResourceByName(orgAccount, Constants.RbacResource.SPACE, spaceName);
    }

    // commons/OrganizationResourceViewSet.get_space_name
    public String getSpaceName(CompositeResource res) {
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
        String spaceName = reqCtx.getParameterSingleValue(defaultSpaceFieldName);

        Object obj = getRequestBody();
        if (obj instanceof Map) {
            Object spaceObj = ((Map) obj).get(defaultSpaceFieldName);
            if (null != spaceObj) {
                spaceName = (String) spaceObj;
            }
        }

        if (spaceName == null || spaceName.length() == 0) {
            if (res != null) {
                spaceName = res.getSpaceName();
            }
        }

        return spaceName;
    }

    public String getSpaceUuid() {
        String name = this.getSpaceName(null);

        CompositeResource spaceRes = (CompositeResource) this.getRequestAttr(ATTR_SPACE_RES);
        if (spaceRes != null) {
            return spaceRes.getUuid();
        }
        if (org.apache.commons.lang.StringUtils.isNotBlank(name)) {
            spaceRes = this.getSpaceResource(this.getOrgAccount(), name);
            if (spaceRes != null) {
                this.setRequestAttr(ATTR_SPACE_RES, spaceRes);

                return spaceRes.getUuid();
            }
        }
        return "";
    }

    // get_resource_constraints
    public Map<String, String> getResourceConstraints(CompositeResource res) {
        return roleUtils.getConstraintsByResource(res);
    }

    public int removeCompositeResource(CompositeResource res) {
        resDao.removeCompositeResource(res.getNamespace(), res.getType(), res.getUuid());

        return 1;
    }

    // commons/OrganizationResourceViewSet.gen_resource_name
    public String genResourceName(String orgAccount, String name, String spaceName) {
        if (spaceName == null) {
            spaceName = getSpaceName(null);
        }

        if (!StringUtils.isEmpty(spaceName) && name.indexOf(':') < 0) {
            name = name + "-" + spaceName;
            if (name.length() > MAX_LEN) {
                name = name.substring(0, MAX_LEN);
            }

            return name;
        }

        return name;
    }

    public String genResourceName(String orgAccount, String name) {
        return genResourceName(orgAccount, name, null);
    }

    // commons/OrganizationResourceViewSet.get_basic_constraints()
    public Map<String, String> getBasicConstraints() {
        Map<String, String> cs = this.getConstraintByType("project_name", this.getProjectName());
        cs.putAll(this.getConstraintByType("space_name", this.getSpaceName(null)));

        return cs;
    }

    // validate_name_unique
    public void validateNameUnique(String orgAccount, String name) {
        if (!this.isNameUnique(orgAccount, name)) {
            String tipMsg = "The build with name '{}' for namespace {} is not exist.";
            throw new BaseException(tipMsg, LastLogCodeEnum.VALIDATE_ERROR, ResultErrorEnum.BIZ_NAME_ALREADY_EXIST,
                    name, orgAccount);
        }
    }

    // is_name_unique
    public boolean isNameUnique(String orgAccount, String name) {
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
        WhereBuilder<SelectBuilder> wb = new SelectBuilder()
                .where()
                .equalsTo(Constants.ResourceColumn.NAMESPACE, orgAccount)
                .equalsTo(Constants.ResourceColumn.NAME, name)
                .equalsTo(Constants.ResourceColumn.TYPE, reqCtx.getResType());

        String s = this.getSpaceUuid();
        if (!StringUtils.isEmpty(s)) {
            wb.equalsTo(Constants.ResourceColumn.SPACE_UUID, s);
        }

        s = this.getProjectUuid();
        if (!StringUtils.isEmpty(s)) {
            wb.equalsTo(Constants.ResourceColumn.PROJECT_UUID, s);
        }

        List<CompositeResource> rs = resDao.queryResourcesByConds(wb.build(), null);

        boolean f = rs == null || rs.size() == 0;

        // @todo:用redis验证尚未存入数据库的名称是否有重复

        return f;
    }

    public String getUserFullName() {
        RequestAuthContext reqCtx = RequestAuthContext.currentRequestAuthContext();
        return reqCtx.getUser().getUsername();
    }

    /* public String getUserToken() {
         // @todo:create pipeline代码中用到user token，按原逻辑是调用其他模块时使用。
         // 现在内部调用不经过鉴权，所以可不传token。并且现在zuul没有向封装层传token，当前取不到值
         // 此方法实现暂从header中取(当前值为null)

         ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
         HttpServletRequest req = attrs.getRequest();

         return req.getHeader("Authorization");
         //写死的一个用于验证的token
         return registryUtil.USER_TOKEN;
     }
 */
    protected List<RbacResource> convertResource(List<Map<String, Object>> ress) {
        List<RbacResource> rbacRess = new ArrayList<>(ress.size());

        RbacResource rr;
        for (Map<String, Object> res : ress) {
            rr = new RbacResource();

            rr.setUuid((String) res.get("uuid"));
            rr.setName((String) res.get("name"));
            rr.setResTypeAction((String) res.get("action"));
            rr.setConsProject((String) res.get("project_uuid")); // @todo:?
            // rr.setConsSpace((String) res.get("xxx"));
            // rr.setConsCluster((String) res.get("xxx"));
            // rr.setSubAccount((String) res.get("xxx"));

            rbacRess.add(rr);
        }

        return rbacRess;
    }

    private Object getRequestAttr(String attrName) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = attrs.getRequest();

        return req.getAttribute(attrName);
    }

    private void setRequestAttr(String attrName, Object val) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = attrs.getRequest();

        req.setAttribute(attrName, val);
    }

    private Object getRequestBody() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = attrs.getRequest();

        Object obj = req.getAttribute(REQUEST_BODY_OBJECT_ATTR);
        if (obj != null) {
            return obj;
        }
        InputStream input = null;
        try {
            input = req.getInputStream();
            ObjectMapper mapper = new ObjectMapper();

            obj = mapper.readValue(input, Object.class);
            req.setAttribute(REQUEST_BODY_OBJECT_ATTR, obj);

            return obj;
        } catch (IOException e) {
            LOGGER.debug("IOException");
            return null;
        } catch (Exception e) {
            LOGGER.debug("IOException");
            return null;
        } finally {
            if (null != input) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.debug("IOException");
                }
            }

        }

    }
}
