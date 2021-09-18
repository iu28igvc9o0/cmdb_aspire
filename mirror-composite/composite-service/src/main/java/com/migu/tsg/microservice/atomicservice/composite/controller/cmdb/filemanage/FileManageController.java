package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.filemanage;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.filemanage.ICmdbFileManageAPI;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManage;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManageQueryRequest;
import com.aspire.ums.cmdb.filemanage.payload.CmdbFileManageRequest;
import com.google.gson.JsonObject;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.filemanage.FileManageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: FileManageController
 * Author:   luowenbo
 * Date:     2020/01/17
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */

@RestController
@Slf4j
public class FileManageController implements ICmdbFileManageAPI {

    @Autowired
    private FileManageClient fileManageClient;

    @Override
    public JSONObject insert(CmdbFileManageRequest fileManage) {
        CmdbFileManage cmdbFileManage = new CmdbFileManage(fileManage);
        try {
            if(fileManage.getFileData() != null) {
                cmdbFileManage.setFileData(fileManage.getFileData().getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileManageClient.insert(cmdbFileManage);
    }

    @Override
    public JSONObject update(@RequestBody CmdbFileManageRequest fileManage) {
        return fileManageClient.update(fileManage);
    }

    @Override
    public JSONObject delete(@RequestBody CmdbFileManageRequest fileManage) {
        return fileManageClient.delete(fileManage);
    }

    @Override
    public Result<CmdbFileManage> getFileManageList(@RequestBody CmdbFileManageQueryRequest request) {
        Result<CmdbFileManage> result = fileManageClient.getFileManageList(request);
        return result;
    }

    @Override
    public JSONObject getOneFile(@PathVariable("id") String id, HttpServletResponse response) {
        CmdbFileManage cmdbFileManage = fileManageClient.getOneFile(id);
        JSONObject jsonObject = new JSONObject();
        try {
            byte[] buffer = cmdbFileManage.getFileData();
            response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode
                    (cmdbFileManage.getFileName(), "UTF-8"))));
            response.setHeader("Connection", "close");
            response.addHeader("Content-Length", "" + buffer.length);
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            jsonObject.put("flag", "success");
        } catch (Exception e) {
            log.error("下载文件失败!", e);
            jsonObject.put("flag", "error");
            jsonObject.put("msg", e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return jsonObject;
    }

    @Override
    public List<Map<String, String>> getFileObjectList(@RequestBody String fileType) {
        return fileManageClient.getFileObjectList(fileType);
    }
}
