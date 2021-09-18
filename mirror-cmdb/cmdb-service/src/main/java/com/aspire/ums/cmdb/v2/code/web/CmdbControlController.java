package com.aspire.ums.cmdb.v2.code.web;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.code.ICmdbControlAPI;
import com.aspire.ums.cmdb.code.payload.CmdbControlType;
import com.aspire.ums.cmdb.util.StringUtils;
import com.aspire.ums.cmdb.v2.code.service.ICmdbControlTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbCodeController
 * Author:   zhu.juwang
 * Date:     2019/5/13 18:58
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbControlController implements ICmdbControlAPI {

    @Autowired
    private ICmdbControlTypeService controlTypeService;

    @Override
    public List<CmdbControlType> list(@RequestBody Map<String, Object> queryParams) {
        try {
            log.info("Request CmdbControlController.list  data -> {}", queryParams);
            return controlTypeService.list(queryParams);
        } catch (Exception e) {
            log.error("Query cmdb code control error. Cause: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public CmdbControlType get(@RequestParam("controlId") String controlId) {
        try {
            log.info("Request CmdbControlController.get  data -> {}", controlId);
            CmdbControlType controlType = new CmdbControlType();
            controlType.setControlId(controlId);
            return controlTypeService.get(controlType);
        } catch (Exception e) {
            log.error("Query cmdb code control error. Cause: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Map<String, String> valid(@RequestParam("filed") String filed, @RequestParam("value") String value) {
        Map<String, String> returnMap = new HashMap<>();
        try {
            returnMap.put("flag", "false");
            CmdbControlType controlType = new CmdbControlType();
            if (("controlCode").equals(filed)) {
                controlType.setControlCode(value);
            }
            if (("controlName").equals(filed)) {
                controlType.setControlName(value);
            }
            List<CmdbControlType> list = controlTypeService.listByEntity(controlType);
            if (list != null && list.size() > 0) {
                returnMap.put("flag", "true");
            }
        }  catch (Exception e) {
            log.error("Valid cmdb code control code or name error. Cause: {}", e.getMessage(), e);
        }
        return returnMap;
    }

    @Override
    public void saveControl(HttpServletResponse response, @RequestBody CmdbControlType controlType) {
        try {
            log.info("Request CmdbControlController.saveControl  data -> {}", JSONObject.toJSON(controlType));
            if (StringUtils.isNotEmpty(controlType.getControlId())) {
                controlTypeService.update(controlType);
            } else {
                controlTypeService.insert(controlType);
            }
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            log.error("Save cmdb code control error. Cause: {}", e.getMessage(), e);
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteControl(HttpServletResponse response, @RequestBody List<CmdbControlType> controlTypeList) {
        try {
            log.info("Request CmdbControlController.deleteControl  data -> {}", JSONObject.toJSON(controlTypeList));
            controlTypeService.delete(controlTypeList);
            response.setStatus(HttpStatus.SC_NO_CONTENT);
        } catch (Exception e) {
            log.error("Delete cmdb code control error. Cause: {}", e.getMessage(), e);
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
