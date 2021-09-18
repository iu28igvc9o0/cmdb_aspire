package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.composite.service.cmdb.payload.AutoDiscoveryRuleInsertVO;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectController
 * Author:   zhu.juwang
 * Date:     2019/3/12 15:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@FeignClient(value = "CMDB")
public interface CmdbDiscoveryClient {

    /**
     * 查询模型列表, 返回模型下配置的新发现规则的列表
     * @return 模型列表
     */
    @RequestMapping(value = "/cmdb/discovery/module/list", method = RequestMethod.GET)
    JSONArray getModuleList();

    /**
     * 获取指定moduleId的规则列表
     * @param moduleId 模型ID
     * @param pageNumber 当前页数
     * @param pageSize 每页数量
     * @return 规则列表
     */
    @RequestMapping(value = "/cmdb/discovery/rule/list/{moduleId}", method = RequestMethod.POST)
    JSONObject getRuleList(@PathVariable("moduleId") String moduleId,
                                  @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                  @RequestParam(value = "pageSize", required = false) Integer pageSize, @RequestBody(required = false) JSONObject param);

    /**
     * 新增规则
     * @param moduleId 模型ID
     * @param ruleEntity 规则实体
     * @return
     */
    @RequestMapping(value = "/cmdb/discovery/rule/insert/{moduleId}", method = RequestMethod.POST)
    Map<String, String> insertRule(@PathVariable("moduleId") String moduleId,
                                          @RequestBody AutoDiscoveryRuleInsertVO ruleEntity);

    /**
     * 修改规则
     * @param moduleId 模型ID
     * @param ruleEntity 规则实体
     * @return
     */
    @RequestMapping(value = "/cmdb/discovery/rule/update/{moduleId}/{ruleId}", method = RequestMethod.PUT)
    Map<String, String> updateRule(@PathVariable("moduleId") String moduleId,
                                          @PathVariable("ruleId") String ruleId,
                                          @RequestBody AutoDiscoveryRuleInsertVO ruleEntity);

    /**
     * 删除规则
     * @param moduleId 模型ID
     * @param ruleIds 删除实体
     * @return
     */
    @RequestMapping(value = "/cmdb/discovery/rule/delete/{moduleId}/", method = RequestMethod.POST)
    Map<String, String> deleteRule(@PathVariable("moduleId") String moduleId,
                                          @RequestBody List<String> ruleIds);
}
