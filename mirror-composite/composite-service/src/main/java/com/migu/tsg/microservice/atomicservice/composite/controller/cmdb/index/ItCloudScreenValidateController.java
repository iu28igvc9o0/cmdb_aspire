package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.index;

import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.index.ItCloudScreenValidateAPI;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenExportRequest;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenValidateRequest;
import com.aspire.ums.cmdb.index.payload.ScreenValidate;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.index.ItCloudScreenValidateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName ItCloudScreenValidateController
 * @Description
 * @Author luowenbo
 * @Date 2020/5/6 17:22
 * @Version 1.0
 */
@RestController
public class ItCloudScreenValidateController implements ItCloudScreenValidateAPI {

    @Autowired
    private ItCloudScreenValidateClient client;

    @Override
    public JSONObject validate(@RequestBody ItCloudScreenValidateRequest req) {
        return client.validate(req);
    }

    @Override
    public List<ScreenValidate> listAll() {
        return client.listAll();
    }

    @Override
    public JSONObject createExcel(@RequestBody ItCloudScreenExportRequest req) {
        return client.createExcel(req);
    }

    @Override
    public JSONObject exportExcel(@RequestBody ItCloudScreenExportRequest req) {
        return client.exportExcel(req);
    }
}
