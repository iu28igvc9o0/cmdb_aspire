package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.index;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenExportRequest;
import com.aspire.ums.cmdb.index.payload.ItCloudScreenValidateRequest;
import com.aspire.ums.cmdb.index.payload.ScreenValidate;

/**
 * @ClassName ItCloudScreenValidateClient
 * @Description
 * @Author luowenbo
 * @Date 2020/5/6 17:24
 * @Version 1.0
 */
@FeignClient(value = "CMDB")
public interface ItCloudScreenValidateClient {

    /*
     *  验证数据完整性和准确性
     * */
    @RequestMapping(value = "/cmdb/index/overview/validate", method = RequestMethod.POST)
    JSONObject validate(@RequestBody ItCloudScreenValidateRequest req);

    /*
     *  查询，按照新增时间列出前5条
     * */
    @RequestMapping(value = "/cmdb/index/overview/validate/list", method = RequestMethod.POST)
    List<ScreenValidate> listAll();

    /*
     *  导出数据的生成
     * */
    @RequestMapping(value = "/cmdb/index/overview/excel/create", method = RequestMethod.POST)
    JSONObject createExcel(@RequestBody ItCloudScreenExportRequest req);

    /*
     *  数据下载
     * */
    @RequestMapping(value = "/cmdb/index/overview/excel/export", method = RequestMethod.POST)
    JSONObject exportExcel(@RequestBody ItCloudScreenExportRequest req);
}
