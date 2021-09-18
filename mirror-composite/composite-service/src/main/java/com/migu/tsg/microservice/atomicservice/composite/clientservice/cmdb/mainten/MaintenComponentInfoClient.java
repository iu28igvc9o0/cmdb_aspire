package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.payload.CmdbComponentInfo;
import com.aspire.ums.cmdb.maintenance.payload.CmdbComponentInfoQueryRequest;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: MaintenComponentInfoClient
 * Author:   luowenbo
 * Date:     2020.2.7
 * Description:  维保部件信息调用接口客户端
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface MaintenComponentInfoClient {

    /*
     *  增加部件信息
     * */
    @PostMapping(value = "/cmdb/maintenance/component/save" )
    JSONObject save(@RequestBody CmdbComponentInfo obj);

    /*
     *  删除部件信息
     * */
    @PostMapping(value = "/cmdb/maintenance/component/delete" )
    JSONObject delete(@RequestBody CmdbComponentInfo obj);

    /*
     *  修改部件信息
     * */
    @PostMapping(value = "/cmdb/maintenance/component/update" )
    JSONObject update(@RequestBody CmdbComponentInfo obj);

    /*
     *  查询部件信息
     * */
    @PostMapping(value = "/cmdb/maintenance/component/list" )
    Result<CmdbComponentInfo> list(@RequestBody CmdbComponentInfoQueryRequest request);
}
