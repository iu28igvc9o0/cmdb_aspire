package com.aspire.ums.cmdb.v2.collectUnknown.web;

import com.aspire.ums.cmdb.allocate.payload.Result;
import com.aspire.ums.cmdb.collectUnknown.ICollectUnkownAPI;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknown;
import com.aspire.ums.cmdb.collectUnknown.payload.CmdbCollectUnknownQuery;
import com.aspire.ums.cmdb.v2.collectUnknown.service.CmdbCollectUnknownService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/10/11
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbCollectUnknownController implements ICollectUnkownAPI {

    @Autowired
    private CmdbCollectUnknownService collectUnknownService;

    @Override
    public Result<CmdbCollectUnknown> list(@RequestBody CmdbCollectUnknownQuery collectUnknownQuery) {
//        log.info("Request CmdbCollectUnknownController.list  data -> {}", collectUnknownQuery);
        return collectUnknownService.list(collectUnknownQuery);
    }

    @Override
    public Map<String, Object> insert(@RequestBody CmdbCollectUnknown collectUnknown) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            log.info("Request CmdbCollectUnknownController.insert  data -> {}", collectUnknown);
            collectUnknownService.insert(collectUnknown);
            resultMap.put("success", true);
            resultMap.put("message", "新增成功!");
        } catch (Exception e) {
            log.error("新增失败, 异常信息: {}", e.getMessage());
            resultMap.put("success", false);
            resultMap.put("message", "新增失败!" + e.getMessage());
        }
        return resultMap;
    }

    @Override
    public List<Map<String, Object>> insertByBatch(@RequestBody List<CmdbCollectUnknown> collectUnknown) {
        return collectUnknownService.insertByBatch(collectUnknown);
    }

    @Override
    public Map<String, Object> update(@RequestBody CmdbCollectUnknown collectUnknown) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            log.info("Request CmdbCollectUnknownController.update  data -> {}", collectUnknown);
            collectUnknownService.update(collectUnknown);
            resultMap.put("success", true);
            switch (collectUnknown.getHandleStatus()) {
                case 0:resultMap.put("message", "解屏蔽成功");break;
                case 1: resultMap.put("message", "维护成功");break;
                case 2: resultMap.put("message", "屏蔽成功");break;
            }
        } catch (Exception e) {
            resultMap.put("success", false);
            switch (collectUnknown.getHandleStatus()) {
                case 0: log.error("解屏蔽失败, 异常信息: {}", e.getMessage());
                    resultMap.put("message", "解屏蔽失败" + e.getMessage());
                break;
                case 1: log.error("维护失败, 异常信息: {}", e.getMessage());
                    resultMap.put("message", "维护失败" + e.getMessage());break;
                case 2: log.error("屏蔽失败, 异常信息: {}", e.getMessage());
                    resultMap.put("message", "屏蔽失败" + e.getMessage());break;
            }

        }
        return resultMap;
    }

    @Override
    public Map<String, Object> maintain(@RequestBody CmdbCollectUnknown collectUnknown) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            log.info("Request CmdbCollectUnknownController.update  data -> {}", collectUnknown);
            collectUnknownService.maintain(collectUnknown);
            resultMap.put("success", true);
            resultMap.put("message", "维护成功");
        } catch (Exception e) {
            log.error("维护失败, 异常信息: {}", e.getMessage());
            resultMap.put("success", false);
            resultMap.put("message", "维护失败" + e.getMessage());
        }
        return resultMap;
    }
}
