package com.aspire.mirror.indexproxy.zabbixdatasync;

import com.aspire.app.ums.zabbixApi.model.ZbxApiSvrInfo;
import com.aspire.mirror.indexproxy.config.ProxyIdentityConfig;
import com.aspire.mirror.indexproxy.domain.MonitorHost;
import com.aspire.mirror.indexproxy.domain.MonitorItemDetailResponse;
import com.aspire.mirror.indexproxy.domain.TemplateItemListRequest;
import com.aspire.mirror.indexproxy.domain.ZabbixTemplateDetailResponse;
import com.aspire.mirror.indexproxy.util.JsonUtil;
import com.aspire.mirror.indexproxy.zabbix.ZabbixApiFacade;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.TypeRef;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * ZABBIX监控项collect业务   <br/>
 * Project Name:collect-service
 * File Name:ZabbixItemCollectBiz.java
 * Package Name:com.aspire.mirror.collect.biz
 * ClassName: ZabbixItemCollectBiz <br/>
 * date: 2018年9月6日 下午2:22:06 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@Component
public class ZabbixItemCollectBiz {

    public static final String ZBX_JSON_VAGUE_TEMPLATE = "template.list.searchByVagueName";
    public static final String ZBX_JSON_LIST_ITEMS = "item.listByParams";

    @Autowired
    private ZabbixApiFacade zbxFacade;

    @Autowired
    private ProxyIdentityConfig proxyIdentityConfig;

    public List<ZabbixTemplateDetailResponse> loadZbxTemplateList(boolean isLogout) throws Exception {
        ZbxApiSvrInfo zbxApiSvr = buildZbxApiSvrInfo(proxyIdentityConfig);
        TypeRef<List<ZabbixTemplateDetailResponse>> typeRef = new TypeRef<List<ZabbixTemplateDetailResponse>>() {
        };
        List<ZabbixTemplateDetailResponse> tempList = zbxFacade.requestZbxApi(zbxApiSvr, isLogout, "templateDetail.listall", typeRef);
        tempList.stream().forEach(item -> {
            item.setProxyIdentity(proxyIdentityConfig.getId());
            if (!CollectionUtils.isEmpty(item.getHosts())) {
                item.getHosts().stream().forEach(itemHost -> {
                    itemHost.setPool(proxyIdentityConfig.getPool());
                });
            }
        });
        return tempList;
    }

    /**
     * 查询监控项列表. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param
     * @return
     * @throws Exception
     */
    public List<MonitorItemDetailResponse> queryZbxDiscoverItem() throws Exception {
//
        String rawJson = zbxFacade.getZbxApiTemplateJson("item.discoverItemList");
        DocumentContext jsonCtx = JsonUtil.buildDefaultJsonPathContext(rawJson);

        ZbxApiSvrInfo zbxApiSvr = buildZbxApiSvrInfo(proxyIdentityConfig);
        TypeRef<List<MonitorItemDetailResponse>> typeRef = new TypeRef<List<MonitorItemDetailResponse>>() {
        };
        List<MonitorItemDetailResponse> resultList
                = zbxFacade.requestZbxApiJson(zbxApiSvr, jsonCtx.jsonString(), typeRef);
        return new ArrayList<>(new HashSet<>(resultList));    // 去重
    }

    public List<MonitorItemDetailResponse> queryZbxTemplateItemList(
            TemplateItemListRequest paramObj, boolean isLogout) throws Exception {
        Object templateIdParam = paramObj.getParamValue(TemplateItemListRequest.PARAM_TEMPLATE_ID);
        Object itemKeyParam = paramObj.getParamValue(TemplateItemListRequest.PARAM_ITEM_KEY);
        Object itemNameParam = paramObj.getParamValue(TemplateItemListRequest.PARAM_ITEM_NAME);
        Object hostIdsParam = paramObj.getParamValue(TemplateItemListRequest.PARAM_HOST_ID);
        Object hostIds = hostIdsParam == null ? null : hostIdsParam;
        Object templateId = templateIdParam == null ? null : templateIdParam;
        String itemKey = itemKeyParam == null ? "" : String.class.cast(itemKeyParam);
        String itemName = itemNameParam == null ? "" : String.class.cast(itemNameParam);

        String rawJson = zbxFacade.getZbxApiTemplateJson("item.listByParams");
        DocumentContext jsonCtx = JsonUtil.buildDefaultJsonPathContext(rawJson);
        if (templateId == null) {
            jsonCtx.delete("$.params.filter.templateid");
        } else {
            jsonCtx.set("$.params.filter.templateid", templateId);
        }
        if (hostIds == null) {
            jsonCtx.delete("$.params.hostids");
        } else {
            jsonCtx.set("$.params.hostids", hostIds);
        }

        if (StringUtils.isBlank(itemKey)) {
            jsonCtx.delete("$.params.search.key_");
        } else {
            jsonCtx.set("$.params.search.key_", itemKey);
        }

        if (StringUtils.isBlank(itemName)) {
            jsonCtx.delete("$.params.search.name");
        } else {
            jsonCtx.set("$.params.search.name", itemName);
        }
        ZbxApiSvrInfo zbxApiSvr = buildZbxApiSvrInfo(proxyIdentityConfig);
        TypeRef<List<MonitorItemDetailResponse>> typeRef = new TypeRef<List<MonitorItemDetailResponse>>() {
        };
        List<MonitorItemDetailResponse> resultList
                = zbxFacade.requestZbxApiJson(zbxApiSvr, isLogout, jsonCtx.jsonString(), typeRef);
        if (resultList == null) {
            resultList = Lists.newArrayList();
        }
        return new ArrayList<>(new HashSet<>(resultList));    // 去重
    }

    private ZbxApiSvrInfo buildZbxApiSvrInfo(ProxyIdentityConfig proxyIdentityConfig) {
//		if (!MonitorApiServerConfig.SERVER_TYPE_ZABBIX.equalsIgnoreCase(apiServerConfig.getServerType())) {
//			String tipMsg = "Error to build ZbxApiSvrInfo as the the monitor api server is not of type 'ZABBIX'";
//			throw new RuntimeException(tipMsg);
//		}
        ZbxApiSvrInfo zbxApiSvr = new ZbxApiSvrInfo();
        zbxApiSvr.setUrl(proxyIdentityConfig.getZabbixUrl());
        zbxApiSvr.setUserName(proxyIdentityConfig.getZabbixUrlName());
        zbxApiSvr.setPassword(proxyIdentityConfig.getZabbixUrlPasswd());
        return zbxApiSvr;
    }

    public List<MonitorHost> loadZbxHostList(boolean isLogout) throws Exception {
        ZbxApiSvrInfo zbxApiSvr = buildZbxApiSvrInfo(proxyIdentityConfig);
        TypeRef<List<MonitorHost>> typeRef = new TypeRef<List<MonitorHost>>() {
        };
        List<MonitorHost> hostList = zbxFacade.requestZbxApi(zbxApiSvr, isLogout, "host.listall", typeRef);
        hostList.stream().forEach(item -> {
            item.setProxyIdentity(proxyIdentityConfig.getId());
            item.setPool(proxyIdentityConfig.getPool());
        });
        return hostList;
    }

    public List<MonitorItemDetailResponse> loadZbxItemPrototypeByHostid(Object templateId, boolean isLogout) throws Exception {
        String rawJson = zbxFacade.getZbxApiTemplateJson("item.itemPrototypeList");
        DocumentContext jsonCtx = JsonUtil.buildDefaultJsonPathContext(rawJson);
        if (templateId == null) {
            jsonCtx.delete("$.params.filter.hostid");
        } else {
            jsonCtx.set("$.params.filter.hostid", templateId);
        }

//		DocumentContext jsonCtx = JsonUtil.buildDefaultJsonPathContext(rawJson);

        ZbxApiSvrInfo zbxApiSvr = buildZbxApiSvrInfo(proxyIdentityConfig);
        TypeRef<List<MonitorItemDetailResponse>> typeRef = new TypeRef<List<MonitorItemDetailResponse>>() {
        };
        List<MonitorItemDetailResponse> resultList
                = zbxFacade.requestZbxApiJson(zbxApiSvr, isLogout, jsonCtx.jsonString(), typeRef);
        return resultList;
    }

    public void logoutZabbix() throws Exception {
        zbxFacade.logout();
    }
}
