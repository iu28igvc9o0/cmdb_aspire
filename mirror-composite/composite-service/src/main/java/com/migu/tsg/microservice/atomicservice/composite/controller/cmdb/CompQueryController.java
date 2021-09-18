package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.ICompQueryService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.CmdbQueryClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.CommonResourceController;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CompQueryController
 * Author:   zhu.juwang
 * Date:     2019/3/12 15:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class CompQueryController extends CommonResourceController implements ICompQueryService {

    private Logger logger = LoggerFactory.getLogger(CompQueryController.class);
    @Autowired
    private CmdbQueryClient queryClient;


    @Override
    public JSONArray getQueryList(@RequestParam(value = "moduleId", required = false) String moduleId,
                                  @RequestParam(value = "menuType", required = false) String menuType) {
        RequestAuthContext authContext = RequestAuthContext.currentRequestAuthContext();
        String userName = authContext.getUser().getUsername();
        return queryClient.getQueryList(moduleId, userName, menuType);
    }

    @Override
    public Map<String, String> insertOrUpdateQuery(HttpServletResponse response, @RequestBody JSONObject entity) {
        if (!entity.containsKey("id") || entity.get("id") == null || entity.getString("id").equals("")) {
            RequestAuthContext authContext = RequestAuthContext.currentRequestAuthContext();
            String userName = authContext.getUser().getUsername();
            entity.put("user", userName);
        }
        Map<String, String> returnMap = queryClient.insertOrUpdateQuery(entity);
        if (returnMap.get("code").equals("error")) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
        return returnMap;
    }

    @Override
    public Map<String, String> deleteQuery(HttpServletResponse response, @PathVariable("queryId") String queryId) {
        Map<String, String> returnMap = queryClient.deleteQuery(queryId);
        if (returnMap.get("code").equals("error")) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
        return returnMap;
    }
}
