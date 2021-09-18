package com.aspire.ums.cmdb.collect.service;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collect.entity.CollectChangeLogEntity;
import com.aspire.ums.cmdb.collect.entity.Page;
import com.aspire.ums.cmdb.module.entity.FormBean;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectService
 * Author:   zhu.juwang
 * Date:     2019/3/12 14:23
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface CollectChangeLogService {

    /**
     * 根据模型ID 查询配置异常信息
     * @param moduleId 模型ID
     * @param requestInfo 请求的信息 包括 展示列信息、查询条件信息
     * @return 异常信息列表
     */
    Page getChangeLogsByModuleId(String moduleId, Map<String, Object> requestInfo);

    /**
     * 根据批次号, 获取改变列表
     * @param batchId 批次号
     * @return
     */
    List<CollectChangeLogEntity> getChangeLogsByBatchId(String batchId);

    /**
     * 获取指定批次号的更新详情信息
     * @param batchId 批次号
     * @return
     */
    Map<String, Object> getChangeLogDetailByBatchId(String batchId);

    /**
     * 发送邮件告警
     * @param sendRequest 发送的请求信息
     * @return 发送邮件告警
     */
    Map<String, String> sendNotice(JSONObject sendRequest);

    /**
     * 根据模型ID 查询配置异常信息
     * @param instanceId 主机ID
     * @param batchId 模型ID
     * @param formBeans 列名集合
     * @return 异常信息列表
     */
    Map getChangeLogByBatchId(String instanceId, String batchId, List<FormBean> formBeans);

    /**
     * 新增异常配置记录
     * @param changeLogEntity
     */
    void insertVO(CollectChangeLogEntity changeLogEntity);

    /**
     * 获取邮件内容
     * @param deviceIp 设备ID
     * @param changeContent 变更内容
     * @param alarmLevel 告警等级
     * @return
     */
    StringBuilder getEmailContent(String deviceIp, String changeContent, String alarmLevel);
}
