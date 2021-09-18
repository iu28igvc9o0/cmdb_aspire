package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.PageBean;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecord;
import com.aspire.ums.cmdb.maintenance.payload.CmdbMaintenanceSoftwareRecordQuery;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2019/8/6
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface MaintenSoftwareRecordClient {

    /**
     *  获取使用列表
     * @return
     */
    @PostMapping(value = "/cmdb/maintenance/software/record/list" )
    PageBean<CmdbMaintenanceSoftwareRecord> list(@RequestBody CmdbMaintenanceSoftwareRecordQuery query);

    /**
     *  新增或更新
     * @return
     */
    @PostMapping(value = "/cmdb/maintenance/software/record/saveAndUpdate" )
    Map<String, Object> saveAndUpdate(@RequestBody List<CmdbMaintenanceSoftwareRecord> records);

    /**
     *  删除软件维保使用
     * @return
     */
    @DeleteMapping(value = "/cmdb/maintenance/software/record/delete" )
    Map<String, Object> delete(@RequestBody JSONObject data);
}
