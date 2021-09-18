package com.aspire.ums.cmdb.v3.module.web;

import com.aspire.ums.cmdb.v3.module.ICmdbV3ModuleGroupAPI;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 说明:
 * 工程: BPM
 * 作者: zhujuwang
 * 时间: 2021/2/24 14:40
 */
@RestController
@Slf4j
public class CmdbV3ModuleGroupController implements ICmdbV3ModuleGroupAPI {

    @Autowired
    private ICmdbV3ModuleGroupService moduleGroupService;

    @Override
    public List<Map<String, Object>> getModuleHeader(@RequestParam(value = "moduleId") String moduleId,
                                                     @RequestParam(value = "tableHeaderCode", required = false) String tableHeaderCode) {
        return moduleGroupService.getGroupHeader(moduleId, tableHeaderCode);
    }
}
