package com.aspire.ums.cmdb.maintenance.web;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.ICmdbComponentInfoAPI;
import com.aspire.ums.cmdb.maintenance.payload.CmdbComponentInfo;
import com.aspire.ums.cmdb.maintenance.payload.CmdbComponentInfoQueryRequest;
import com.aspire.ums.cmdb.maintenance.service.ICmdbComponentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CmdbComponentInfoController
 * Author:   luowenbo
 * Date:     2020/02/07 15:38
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbComponentInfoController implements ICmdbComponentInfoAPI {

    @Autowired
    private ICmdbComponentInfoService cmdbComponentInfoService;

    @Override
    public JSONObject save(@RequestBody CmdbComponentInfo obj) {
        log.info("Request into CmdbComponentInfoController.save()  params -> {}", obj);
        JSONObject jsonObject = new JSONObject();
        try {
            cmdbComponentInfoService.save(obj);
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            log.error("Create maintenance Component error.", e);
        }
        return jsonObject;
    }

    @Override
    public JSONObject delete(@RequestBody CmdbComponentInfo obj) {
        log.info("Request into CmdbComponentInfoController.delete()  params -> {}", obj);
        JSONObject jsonObject = new JSONObject();
        try {
            cmdbComponentInfoService.delete(obj);
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            log.error("Delete maintenance Component error.", e);
        }
        return jsonObject;
    }

    @Override
    public JSONObject update(@RequestBody CmdbComponentInfo obj) {
        log.info("Request into CmdbComponentInfoController.update()  params -> {}", obj);
        JSONObject jsonObject = new JSONObject();
        try {
            cmdbComponentInfoService.update(obj);
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            log.error("update maintenance Component error.", e);
        }
        return jsonObject;
    }

    @Override
    public Result<CmdbComponentInfo> list(@RequestBody CmdbComponentInfoQueryRequest request) {
        log.info("Request into CmdbComponentInfoController.list()  params -> {}");
        try {
            return cmdbComponentInfoService.list(request);
        } catch (Exception e) {
            log.error("List maintenance Component error.", e);
        }
        return null;
    }
}
