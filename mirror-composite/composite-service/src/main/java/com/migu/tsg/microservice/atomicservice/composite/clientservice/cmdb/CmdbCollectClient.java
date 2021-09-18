package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
public interface CmdbCollectClient {

    @RequestMapping(value = "/cmdb/dict/getDict/{code}", method = RequestMethod.GET)
    JSONArray getDictList(@PathVariable("code") String code);
    /**
     * 查询模型列表, 返回模型下配置的采集配置信息
     * @return 模型列表
     */
    @RequestMapping(value = "/cmdb/collect/module/list", method = RequestMethod.GET)
    JSONArray getModuleList();

    /**
     * 根据模型ID, 返回模型下配置的采集配置信息
     * @return 采集配置列表
     */
    @RequestMapping(value = "/cmdb/collect/{moduleId}", method = RequestMethod.GET)
    JSONArray getCollectsByModuleId(@PathVariable("moduleId") String moduleId);

    /**
     * 根据模型ID, 返回模型下所有字段信息
     * @return 字段列表
     */
    @RequestMapping(value = "/cmdb/collect/forms/{moduleId}", method = RequestMethod.GET)
    JSONArray getFormsByModuleId(@PathVariable("moduleId") String moduleId);

    /**
     * 根据采集配置ID, 返回采集的历史记录
     * @return 采集记录列表
     */
    @RequestMapping(value = "/cmdb/collect/getCollectRecord/{collectId}", method = RequestMethod.GET)
    JSONObject getCollectRecordByCollectId(@PathVariable("collectId") String collectId,
                                           @RequestParam("pageNumber") Integer pageNumber,
                                           @RequestParam("pageSize") Integer pageSize);

    /**
     * 新增采集配置
     * @return 新增状态 成功/失败 失败原因
     */
    @RequestMapping(value = "/cmdb/collect/{moduleId}", method = RequestMethod.POST, produces = MediaType
            .APPLICATION_JSON_UTF8_VALUE)
    Map<String, String> saveCollect(@PathVariable("moduleId") String moduleId, @RequestBody JSONObject requestInfo);

    /**
     * 删除采集配置
     * @return 删除状态 成功/失败 失败原因
     */
    @RequestMapping(value = "/cmdb/collect/{collectId}", method = RequestMethod.DELETE)
    Map<String, String> deleteCollect(@PathVariable("collectId") String collectId);

    /**
     * 获取配置异常列表
     * @return 配置异常信息列表
     */
    @RequestMapping(value = "/cmdb/collect/changelog/{moduleId}", method = RequestMethod.POST)
    JSONObject getChangeLogs(@PathVariable("moduleId") String moduleId, @RequestBody Map<String, Object> requestInfo);

    /**
     * 获取配置异常详情
     * @return 配置异常信息详情
     */
    @RequestMapping(value = "/cmdb/collect/changelog/detail/{batchId}", method = RequestMethod.GET)
    Map<String, Object> getChangeLogDetail(@PathVariable("batchId") String batchId);

    /**
     * 发送邮件告警
     * @return 发送邮件告警
     */
    @RequestMapping(value = "/cmdb/collect/changelog/sendmail/", method = RequestMethod.POST)
    Map<String, String> sendNotice(@RequestBody JSONObject sendRequest);
}
