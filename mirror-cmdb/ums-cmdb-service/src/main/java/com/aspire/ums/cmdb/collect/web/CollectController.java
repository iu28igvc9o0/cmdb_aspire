package com.aspire.ums.cmdb.collect.web;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collect.entity.CollectEntity;
import com.aspire.ums.cmdb.collect.entity.InsertCollectEntity;
import com.aspire.ums.cmdb.collect.entity.Page;
import com.aspire.ums.cmdb.collect.service.CollectChangeLogService;
import com.aspire.ums.cmdb.collect.service.CollectOriginalRecordService;
import com.aspire.ums.cmdb.collect.service.CollectService;
import com.aspire.ums.cmdb.module.entity.FormBean;
import com.aspire.ums.cmdb.module.entity.Module;
import com.aspire.ums.cmdb.module.service.FormService;
import com.aspire.ums.cmdb.module.service.ModuleService;

import lombok.extern.slf4j.Slf4j;

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
@RefreshScope
@RestController
@Slf4j
@RequestMapping("/cmdb/collect")
public class CollectController extends BaseController<CollectController> {
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private FormService formService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private CollectOriginalRecordService recordService;
    @Autowired
    private CollectChangeLogService changeLogService;
    /**
     * 查询模型列表, 返回模型下配置的采集配置信息
     * @return 模型列表
     */
    @RequestMapping(value = "/module/list", method = RequestMethod.GET)
    public JSONArray getModuleList() {
        List<Module> parentModule = moduleService.selectModule();
        JSONArray returnList = new JSONArray();
        if (parentModule != null && parentModule.size() >0) {
            for (Module parent : parentModule) {
                JSONObject parentObject = (JSONObject) JSON.toJSON(parent);
                JSONArray jsonArray = new JSONArray();
                if (parent.getChildModules() != null && parent.getChildModules().size() >0) {
                    for (Module child : parent.getChildModules()) {
                        JSONObject childObject = (JSONObject) JSON.toJSON(child);
                        //获取模型下 配置的采集配置信息
                        List<Map> mapList= collectService.getCollectListMapByModuleId(child.getId());
                        JSONArray collectArray = new JSONArray();
                        if (mapList != null && mapList.size() >0) {
                            collectArray = (JSONArray) JSON.toJSON(mapList);
                        }
                        childObject.put("collects", collectArray);
                        jsonArray.add(childObject);
                    }
                }
                parentObject.put("childModules", jsonArray);
                returnList.add(parentObject);
            }
        }
        return returnList;
    }

    /**
     * 根据模型ID, 返回模型下配置的采集配置信息
     * @return 采集配置列表
     */
    @RequestMapping(value = "/{moduleId}", method = RequestMethod.GET)
    public JSONArray getCollectsByModuleId(@PathVariable("moduleId") String moduleId) {
        List<Map> mapList= collectService.getCollectListMapByModuleId(moduleId);
        if (mapList == null || mapList.size() == 0) {
            return new JSONArray();
        }
        return (JSONArray) JSON.toJSON(mapList);
    }

    /**
     * 根据模型ID, 返回模型下所有字段信息
     * @return 字段列表
     */
    @RequestMapping(value = "/forms/{moduleId}", method = RequestMethod.GET)
    public JSONArray getFormsByModuleId(@PathVariable("moduleId") String moduleId) {
        Module module = new Module();
        module.setId(moduleId);
        List<FormBean> mapList= null;
        try {
            mapList = formService.getForms(module);
        } catch (Exception e) {
            log.error("Get form list error. ", e);
        }
        if (mapList == null || mapList.size() == 0) {
            return new JSONArray();
        }
        return (JSONArray) JSON.toJSON(mapList);
    }

    /**
     * 根据采集配置ID, 返回采集的历史记录
     * @return 采集记录列表
     */
    @RequestMapping(value = "/getCollectRecord/{collectId}", method = RequestMethod.GET)
    public JSONObject getCollectRecordByCollectId(@PathVariable("collectId") String collectId, @Param("pageNumber") Integer pageNumber,
                                                  @Param("pageSize") Integer pageSize) {
        Page page = new Page(pageNumber, pageSize);
        recordService.getCollectRecordsByCollectId(collectId, page);
        return (JSONObject) JSON.toJSON(page);
    }

    /**
     * 新增采集配置
     * @return 新增状态 成功/失败 失败原因
     */
    @RequestMapping(value = "/{moduleId}", method = RequestMethod.POST)
    public Map<String, String> saveCollect(@PathVariable("moduleId") String moduleId, @RequestBody InsertCollectEntity collectEntity) {
        return collectService.saveCollect(collectEntity);
    }

    /**
     * 删除采集配置
     * @return 删除状态 成功/失败 失败原因
     */
    @RequestMapping(value = "/{collectId}", method = RequestMethod.DELETE)
    public Map<String, String> deleteCollect(@PathVariable("collectId") String collectId) {
        CollectEntity deleteEntity = new CollectEntity();
        deleteEntity.setId(collectId);
        Map<String, String> returnMap = collectService.deleteVO(deleteEntity);
        if (returnMap.get("code").equals("success")) {
            getResponse().setStatus(HttpStatus.NO_CONTENT.value());
        } else {
            getResponse().setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return returnMap;
    }

    /**
     * 获取配置异常列表
     * @return 配置异常信息列表
     */
    @RequestMapping(value = "/changelog/{moduleId}", method = RequestMethod.POST)
    public JSONObject getChangeLogs(@PathVariable("moduleId") String moduleId, @RequestBody Map<String, Object> requestInfo) {
        Page page = changeLogService.getChangeLogsByModuleId(moduleId, requestInfo);
        return (JSONObject) JSON.toJSON(page);
    }

    /**
     * 获取配置异常详情
     * @return 配置异常信息详情
     */
    @RequestMapping(value = "/changelog/detail/{batchId}", method = RequestMethod.GET)
    public Map<String, Object> getChangeLogDetail(@PathVariable("batchId") String batchId) {
        return changeLogService.getChangeLogDetailByBatchId(batchId);
    }

    /**
     * 发送邮件告警
     * @return 发送邮件告警
     */
    @RequestMapping(value = "/changelog/sendmail/", method = RequestMethod.POST)
    public Map<String, String> sendNotice(@RequestBody JSONObject sendRequest) {
        return changeLogService.sendNotice(sendRequest);
    }
}
