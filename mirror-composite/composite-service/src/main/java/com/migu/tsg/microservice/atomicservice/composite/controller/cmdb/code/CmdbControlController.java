package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.code;

import com.aspire.mirror.composite.service.cmdb.code.ICmdbControlAPI;
import com.aspire.ums.cmdb.code.payload.CmdbControlType;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.code.CmdbControlClient;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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
    private CmdbControlClient controlClient;

    @Override
    public List<CmdbControlType> list(@RequestBody Map<String, Object> queryParams) {
        return controlClient.list(queryParams);
    }

    @Override
    public CmdbControlType get(@RequestParam("controlId") String controlId) {
        return controlClient.get(controlId);
    }

    @Override
    public Map<String, String> valid(@RequestParam("filed") String filed, @RequestParam("value") String value) {
        return controlClient.valid(filed, value);
    }

    @Override
    public void saveControl(HttpServletResponse response, @RequestBody CmdbControlType controlType) {
        controlClient.saveControl(controlType);
    }

    @Override
    public void deleteControl(HttpServletResponse response, @RequestBody List<CmdbControlType> controlTypeList) {
        controlClient.deleteControl(controlTypeList);
    }
}
