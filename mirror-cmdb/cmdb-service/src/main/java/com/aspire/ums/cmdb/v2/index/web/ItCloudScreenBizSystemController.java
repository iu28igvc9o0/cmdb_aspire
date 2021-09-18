package com.aspire.ums.cmdb.v2.index.web;

import com.aspire.ums.cmdb.index.ItCloudScreenBizSystemAPI;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenRequest;
import com.aspire.ums.cmdb.v2.index.serivce.ItCloudScreenBizSystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName ItCloudScreenBizSystemController
 * @Description
 * @Author luowenbo
 * @Date 2020/3/23 14:37
 * @Version 1.0
 */
@RestController
@Slf4j
public class ItCloudScreenBizSystemController implements ItCloudScreenBizSystemAPI {

    @Autowired
    private ItCloudScreenBizSystemService bizSystemService;

    @Override
    public List<Map<String,Object>> getResourceAllocateByBizSystem(@RequestBody ItCloudScreenRequest req) {
        return bizSystemService.getBizSystemAllocation(req);
    }

    @Override
    public List<Map<String, Object>> getBizSystemNotInpect(@RequestBody ItCloudScreenRequest req) {
        return bizSystemService.getBizSystemNotInpect(req);
    }

    @Override
    public List<Map<String, Object>> allDataListExport(@RequestBody ItCloudScreenRequest req) {
        return bizSystemService.allDataListExport(req);
    }

    @Override
    public List<Map<String, Object>> partDataListExport(@RequestBody ItCloudScreenRequest req) {
        return bizSystemService.partDataListExport(req);
    }
}
