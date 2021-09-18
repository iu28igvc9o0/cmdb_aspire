package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.module;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelation;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelationDetail;
import com.aspire.ums.cmdb.v3.module.payload.CmdbV3ModuleCiRelationDetailResponse;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ICmdbV3ModuleCiRelationClient
 * Author:   hangfang
 * Date:     2020/4/27
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface ICmdbV3ModuleCiRelationClient {
    /**
     * 根据模型id获取所模型关系
     * @param
     * @return
     */
    @GetMapping("/v3/cmdb/module/relation/listByModuleId")
    List<CmdbV3ModuleCiRelation> listByModuleId(@RequestParam("moduleId") String moduleId);


    /**
     * 根据模型id获取所模型关系
     * @param
     * @return
     */
    @GetMapping("/v3/cmdb/module/relation/listRDetailByModuleId")
    List<CmdbV3ModuleCiRelationDetail> listRDetailByModuleId(@RequestParam("moduleId") String moduleId);

    /**
     * 保存模型关系
     * @param
     * @return
     */
    @PostMapping("/v3/cmdb/module/relation/save")
    Map<String, Object> save(@RequestParam("userName") String username, @RequestBody CmdbV3ModuleCiRelation relation);

    /**
     * 更新模型关系
     * @param
     * @return
     */
    @PostMapping("/v3/cmdb/module/relation/update")
    Map<String, Object> update(@RequestParam("userName") String username, @RequestBody CmdbV3ModuleCiRelation relation);

    /**
     * 保存模型关系
     * @param
     * @return
     */
    @DeleteMapping("/v3/cmdb/module/relation/deleteById")
    Map<String, Object> deleteById(@RequestParam("id") String id);

    /**
     * 根据关系查询当前ci关系详情
     * @param
     * @return
     */
    @GetMapping("/v3/cmdb/module/relation/getCiRelationDetail")
    CmdbV3ModuleCiRelationDetailResponse getCiRelationDetail(@RequestParam("relationId") String relationId,
                                                             @RequestParam("instanceId") String instanceId);


}
