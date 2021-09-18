package com.aspire.ums.cmdb.v3.module.web;

import com.aspire.ums.cmdb.v3.module.ICmdbV3ModuleCodeSettingAPI;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCodeSetting;
import com.aspire.ums.cmdb.v3.module.service.ICmdbV3ModuleCodeSettingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2020/2/10
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbV3ModuleCodeSettingController implements ICmdbV3ModuleCodeSettingAPI {

    @Autowired
    private ICmdbV3ModuleCodeSettingService moduleCodeSettingService;
    @Override
    public Map<String, Object> save(@RequestBody List<CmdbV3ModuleCodeSetting> moduleCodeSettings) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            moduleCodeSettingService.insertByBatch(moduleCodeSettings);
            resultMap.put("success", true);
            resultMap.put("message", "新增成功!");
        } catch (Exception e) {
            resultMap.put("success", false);
            resultMap.put("message", "新增失败!");
            throw new RuntimeException("新增失败" + e.getMessage());
        }
        return resultMap;
    }
}
