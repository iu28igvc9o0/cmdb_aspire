package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.condication;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSetting;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3CondicationSettingQuery;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: ICmdbV3CondicationSettingClient
 * Author:   zhu.juwang
 * Date:     2020/1/10 9:39
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface ICmdbV3CondicationSettingClient {
    /**
     * 获取组织列表
     * @param
     * @return
     */
    @PostMapping("/v3/cmdb/condication/list")
    Result<CmdbV3CondicationSetting> list(@RequestBody CmdbV3CondicationSettingQuery settingQuery);

    @PostMapping("/v3/cmdb/condication/save")
    Map<String, String> save(@RequestBody CmdbV3CondicationSetting setting);

    @PutMapping("/v3/cmdb/condication/update")
    Map<String, String> update(@RequestBody CmdbV3CondicationSetting setting);

    @DeleteMapping("/v3/cmdb/condication/delete")
    Map<String, String> delete(@RequestBody CmdbV3CondicationSetting setting);

    @PostMapping("/v3/cmdb/condication/get")
    CmdbV3CondicationSetting get(@RequestBody CmdbV3CondicationSetting setting);

    @GetMapping("/v3/cmdb/condication/validate/unique")
    JSONObject validConditionUnique( @RequestParam(value = "code",required = false) String code,
                                     @RequestParam(value = "name",required = false) String name);
}
