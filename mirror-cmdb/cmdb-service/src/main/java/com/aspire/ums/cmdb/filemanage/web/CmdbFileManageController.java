package com.aspire.ums.cmdb.filemanage.web;


import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.filemanage.ICmdbFileManageAPI;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManage;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManageQueryRequest;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManageRequest;
import com.aspire.ums.cmdb.filemanage.service.ICmdbFileManageService;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbFileManageController
 * Author:   luowenbo
 * Date:     2020 01-14 15:34
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbFileManageController implements ICmdbFileManageAPI {

    @Autowired
    private ICmdbFileManageService fileManageService;

    @Override
    public JSONObject insert(@RequestBody CmdbFileManage fileManage) {
        log.info("Request into CmdbFileManageController.insert() params -> {}", fileManage);
        JSONObject jsonObject = new JSONObject();
        try {
            fileManageService.insert(fileManage);
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            log.error("insert CmdbFileManage project error.", e);
        }
        return jsonObject;
    }

    @Override
    public JSONObject update(@RequestBody CmdbFileManageRequest fileManage) {
        log.info("Request into CmdbFileManageController.update() params -> {}", fileManage);
        JSONObject jsonObject = new JSONObject();
        try {
            fileManageService.update(fileManage);
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            log.error("update CmdbFileManage project error.", e);
        }
        return jsonObject;
    }

    @Override
    public JSONObject delete(@RequestBody CmdbFileManageRequest fileManage) {
        log.info("Request into CmdbFileManageController.delete() params -> {}", fileManage);
        JSONObject jsonObject = new JSONObject();
        try {
            fileManageService.delete(fileManage);
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            log.error("delete CmdbFileManage project error.", e);
        }
        return jsonObject;
    }

    @Override
    public Result<CmdbFileManage> getFileManageList(@RequestBody CmdbFileManageQueryRequest request) {
        log.info("Request into CmdbFileManageController.getFileManageList() params -> {}", request);
        try {
            return fileManageService.getFileManageList(request);
        } catch (Exception e) {
            log.error("list CmdbFileManage error.", e);
        }
        return null;
    }

    @Override
    public CmdbFileManage getOneFile(@PathVariable("id") String id) {
        log.info("Request into CmdbFileManageController.getOneFile() params -> {}", id);
        return fileManageService.getOne(id);
    }

    @Override
    public List<Map<String, Object>> getFileObjectList(@RequestBody String fileType) {
        log.info("Request into CmdbFileManageController.getFileObjectList() params -> {}", fileType);
        // 单个参数的json解析会将json字符串直接传过来
        JSONObject json = JSONObject.parseObject(fileType);
        return fileManageService.getFileObjectList(json.get("fileType").toString());
    }
}
