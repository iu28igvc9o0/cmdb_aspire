package com.aspire.mirror.indexproxy.zabbix;

import com.aspire.app.ums.zabbixApi.IZabbixApiSendResp;
import com.aspire.app.ums.zabbixApi.ZabbixApiHelper;
import com.aspire.app.ums.zabbixApi.model.ZbxApiSvrInfo;
import com.aspire.mirror.indexproxy.util.JsonUtil;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.TypeRef;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * zabbixApi访问门面    <br/>
 * Project Name:collect-service
 * File Name:ZabbixApiFacade.java
 * Package Name:com.aspire.mirror.collect.zabbix
 * ClassName: ZabbixApiFacade <br/>
 * date: 2018年9月4日 下午7:51:09 <br/>
 *
 * @author pengguihua
 * @since JDK 1.6
 */
@Component
@Slf4j
public class ZabbixApiFacade {
    private static final String TEMPLATE_GROUP_NAME = "zbxApi_collect_service";
    private static final String JSON_TEMPLATE_FILE = "zabbix/zabbixApi_json_template.xml";

    @PostConstruct
    private void initLoadZbxJsonTemplates() {
        try {
            Resource res = new ClassPathResource(JSON_TEMPLATE_FILE);
            ZabbixApiHelper.loadJsonTemplates(TEMPLATE_GROUP_NAME, res.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException("Failed to init load zabbix Api template json file.", e);
        }
    }

    /**
     * 功能描述: 使用指定   请求JSON模版   以及    填充参数，发起zabbixAPI请求并返回响应
     * <p>
     *
     * @param svrInfo
     * @param jsonTempId
     * @param bizParams
     * @return
     * @throws Exception
     */
    public <T> T requestZbxApi(
            ZbxApiSvrInfo svrInfo, String jsonTempId, TypeRef<T> modelType, Object... bizParams) throws Exception {
        return requestZbxApiJson(svrInfo, getZbxApiTemplateJson(jsonTempId), modelType, bizParams);
    }

    public <T> T requestZbxApi( ZbxApiSvrInfo svrInfo, boolean isLogout, String jsonTempId, TypeRef<T> modelType, Object... bizParams) throws Exception{
        return requestZbxApiJson(svrInfo, isLogout, getZbxApiTemplateJson(jsonTempId), modelType, bizParams);
    }
    /**
     * 获取zabbixApi的请求模版JSON. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param jsonTempId
     * @return
     */
    public String getZbxApiTemplateJson(String jsonTempId) {
        return ZabbixApiHelper.getJsonTemplateById(TEMPLATE_GROUP_NAME, jsonTempId);
    }

    /**
     * 根据请求json及格式化参数, 调用zabbixApi. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @param svrInfo
     * @param json
     * @param modelType
     * @param bizParams
     * @return
     * @throws Exception
     */
    public <T> T requestZbxApiJson(
            ZbxApiSvrInfo svrInfo, String json, TypeRef<T> modelType, Object... bizParams) throws Exception {
        return requestZbxApiJson(svrInfo, true, json, modelType, bizParams);
    }
    public <T> T requestZbxApiJson(
            ZbxApiSvrInfo svrInfo, boolean isLogout, String json, TypeRef<T> modelType, Object... bizParams) throws Exception {
        try {
            if (StringUtils.isEmpty(ZabbixApiHelper.getLoginedAuthToken())) {
                String zbxApiUrl = svrInfo.getUrl();
                String zbxApiUser = svrInfo.getUserName();
                String zbxApiPass = svrInfo.getPassword();
                ZabbixApiHelper.login(zbxApiUrl, zbxApiUser, zbxApiPass);
            }
            // 填充基础参数
            List<Object> paramList = new ArrayList<Object>();
            paramList.add(ZabbixApiHelper.getLoginedAuthToken());
            paramList.add(ZabbixApiHelper.getRandomSeq());
            if (bizParams != null) {
                for (Object paramItem : bizParams) {
                    paramList.add(paramItem);
                }
            }
            json = String.format(json, paramList.toArray());

            IZabbixApiSendResp reqSender = ZabbixApiHelper.getDefaultZabbixApiSendResp();
            JSONObject response = reqSender.request(ZabbixApiHelper.getLoginedZabbixApiUrl(), json);
            if (response == null) {
                return null;
            }
            DocumentContext jsonCtx = JsonUtil.buildDefaultJsonPathContext(response.toString());
            return jsonCtx.read("$.result", modelType);
        } catch (Exception e) {
            log.error("zabbix api is error!", e);
        } finally {
            if (isLogout) {
                // 由于同步登出需要耗费好几秒，此处改成异步登出
                final String zbxApiUrl = ZabbixApiHelper.getLoginedZabbixApiUrl();
                final String apiToken = ZabbixApiHelper.getLoginedAuthToken();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ZabbixApiHelper.logout(zbxApiUrl, apiToken);
                    }
                });
            }
        }
        return null;
    }
    public void logout() {
        // 由于同步登出需要耗费好几秒，此处改成异步登出
        final String zbxApiUrl = ZabbixApiHelper.getLoginedZabbixApiUrl();
        final String apiToken = ZabbixApiHelper.getLoginedAuthToken();
        ZabbixApiHelper.logout(zbxApiUrl, apiToken);
    }

}
