package com.aspire.ums.cmdb.sync.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.BpmAccessTokenUtil;
import com.aspire.ums.cmdb.sync.entity.CommonResult;
import com.aspire.ums.cmdb.sync.entity.EIPDept;
import com.aspire.ums.cmdb.sync.entity.OrgVo;
import com.aspire.ums.cmdb.sync.service.EipSyncService;
import com.aspire.ums.cmdb.sync.util.WordToPinYinUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

@Service("eipDeptSyncToBpmService")
public class EipDeptSyncToBpmServiceImpl implements EipSyncService<EIPDept> {

    private static final Logger logger = LoggerFactory.getLogger(EipDeptSyncToBpmServiceImpl.class);

    @Value("${uc.org.manage.facade}")
    private String UC_REST_FACADE;
    @Value("${uc.eip.demension.id}")
    private String UC_EIP_DEMENSION_ID;

    private void callBpmRestful(String url, String content, String token) {
        try {
            final CloseableHttpClient httpClient = HttpClients.createDefault();
            final HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-type","application/json; charset=utf-8");
            if (StringUtils.isNotEmpty(token)) {
                httpPost.addHeader("Authorization", "Bearer " + token);
            }
            StringEntity se = new StringEntity(content, Charset.forName("UTF-8"));
            httpPost.setEntity(se);
            final HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            logger.info("POST Response Status: {}", statusCode);
            HttpEntity responseEntity = httpResponse.getEntity();
            String result = "";
            if (responseEntity != null) {
                // 将响应内容转换为字符串
                result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
                logger.info("POST {} 回执: {}", url, result);
            }
            if (HttpStatus.OK.value() == statusCode) {
                if (StringUtils.isNotEmpty(result)) {
                    CommonResult response = JSONObject.parseObject(result, CommonResult.class);
                    if (response == null) {
                        logger.error("POST {} BODY: {} 处理失败!", url, content);
                        return;
                    }
                    if (response.getState()) {
                        logger.error("POST {} BODY: {} 处理成功!", url, content);
                    } else {
                        logger.error("POST {} BODY: {} 处理失败!", url, content);
                    }
                }
            } else {
                logger.error("POST {} BODY: {} 处理失败!", url, content);
            }
        } catch (IOException e) {
            logger.error("POST {} BODY: {} 处理失败!", url, content, e);
        }
    }

    private OrgVo toOrgVo(EIPDept dept) {
        OrgVo vo = new OrgVo();
        vo.setId(dept.getDeptId());
        vo.setName(dept.getDeptName());
        vo.setParentId(dept.getParentId());
        vo.setOrderNo(Long.parseLong(dept.getSort()));
        vo.setCode(WordToPinYinUtil.ToFirstChar(dept.getDeptName()));
        vo.setPath(UC_EIP_DEMENSION_ID + "." + dept.getFullId().replace(",", ".") + ".");
        vo.setPathName("/" + dept.getFullName());
        return vo;
    }

    @Override
    public void add(List<EIPDept> list) {
        String url = UC_REST_FACADE + "org/addEipOrg";
        String token = BpmAccessTokenUtil.getToken();
        for (EIPDept dept : list) {
            OrgVo vo = toOrgVo(dept);
            callBpmRestful(url, JSON.toJSONString(vo), token);
        }
    }

    @Override
    public void modify(List<EIPDept> list) {
        String url = UC_REST_FACADE + "org/updateEipOrg";
        String token = BpmAccessTokenUtil.getToken();
        for (EIPDept dept : list) {
            OrgVo vo = toOrgVo(dept);
            callBpmRestful(url, JSON.toJSONString(vo), token);
        }
    }

    @Override
    public void delete(List<EIPDept> list) {
        String url = UC_REST_FACADE + "org/deleteOrg";
        String token = BpmAccessTokenUtil.getToken();
        StringBuilder builder = new StringBuilder();
        for (EIPDept dept : list) {
            OrgVo vo = toOrgVo(dept);
            builder.append(vo.getCode());
            builder.append(",");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        String codes = builder.toString();
        callBpmRestful(url, codes, token);
    }
}
