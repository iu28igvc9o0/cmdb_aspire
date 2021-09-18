package com.aspire.ums.cmdb.v3.es.controller;

import com.aspire.ums.cmdb.v3.es.ICmdbESAPI;
import com.aspire.ums.cmdb.v3.es.service.ICmdbESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CmdbESController
 * Author:   zhu.juwang
 * Date:     2020/2/17 9:40
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class CmdbESController implements ICmdbESAPI {

    @Autowired
    private ICmdbESService esService;

    @Override
    public Map<String, String> refreshModuleData(@RequestParam(value = "module_id", required = false) String moduleId) {
        return esService.writeModuleData(moduleId);
    }
}
