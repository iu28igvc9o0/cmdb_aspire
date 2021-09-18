package com.aspire.ums.cmdb.v2.index.web;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.index.ItCloudScreenValidateAPI;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenExportRequest;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenValidateRequest;
import com.aspire.ums.cmdb.index.payload.ScreenValidate;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenValidateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @ClassName ItCloudScreenValidateController
 * @Description 大屏验证控制层实现类
 * @Author luowenbo
 * @Date 2020/5/6 16:08
 * @Version 1.0
 */
@EnableAsync
@RestController
@Slf4j
public class ItCloudScreenValidateController implements ItCloudScreenValidateAPI {
    @Autowired
    private ItCloudScreenValidateService service;

    @Override
    public JSONObject validate(@RequestBody ItCloudScreenValidateRequest req) {
        log.info("Request into ItCloudScreenValidateController.validate()  params -> {}", req);
        if ("IsImport".equals(req.getValidateType())) {
            return service.validateIsImport(req);
        } else if("IsComplete".equals(req.getValidateType())) {
            return service.validateIsComplete(req);
        }else {
            return service.validateDataIsCorrect(req);
        }
    }

    @Override
    public List<ScreenValidate> listAll() {
        log.info("Request into ItCloudScreenValidateController.listAll()  params -> {}");
        return service.listAll();
    }

    @Override
    public JSONObject createExcel(@RequestBody ItCloudScreenExportRequest req) {
        log.info("Request into ItCloudScreenValidateController.createExcel()  params -> {}",req);
        JSONObject rs = service.judgeIfHasCreate(req);
        if(rs.getBoolean("flag")) {
            // 生成Excel是否已经完成的标识
            service.createFileUnique(req);
            // 异步处理生成Excel文件
            service.createExcel(req);
        }
        return rs;
    }

    @Override
    public JSONObject exportExcel(@RequestBody ItCloudScreenExportRequest req) {
        log.info("Request into ItCloudScreenValidateController.exportExcel()  params -> {}",req);
        return service.judgeIfHasExist(req);
    }
}
