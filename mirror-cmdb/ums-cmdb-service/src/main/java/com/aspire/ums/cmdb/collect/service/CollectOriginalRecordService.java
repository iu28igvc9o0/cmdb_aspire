package com.aspire.ums.cmdb.collect.service;

import com.alibaba.fastjson.JSONObject;
import com.aspire.ums.cmdb.collect.entity.CollectEntity;
import com.aspire.ums.cmdb.collect.entity.CollectOriginalRecordEntity;
import com.aspire.ums.cmdb.collect.entity.InsertCollectEntity;
import com.aspire.ums.cmdb.collect.entity.Page;

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
public interface CollectOriginalRecordService {
    /**
     * 根据采集配置ID, 查询采集的历史记录
     * @param collectId 采集ID
     * @param page 分页数据
     * @return 采集历史记录集合
     */
    Page getCollectRecordsByCollectId(String collectId, Page<Map> page);

    /**
     * 根据collectId 删除对象信息
     * @param collectId 采集配置ID
     */
    void deleteVOByCollectId(String collectId);

    /**
     * 新增采集记录
     * @param recordEntity 采集记录信息
     * @return
     */
    void insertVO(CollectOriginalRecordEntity recordEntity);

    /**
     * 根据taskId 获取所有的配置信息
     * @param taskId 任务ID
     * @return 返回配置信息列表
     */
    List<Map<String, String>> getCollectInfoByTaskId(String taskId);

    /**
     * 对采集过来的数据 进行解析 - 数据覆盖. 结果对比 异常通知 发送
     * @param batchId 批次号
     * @param noticeMap 需要告警通知的集合
     * @param taskCollect 任务集合
     * @param formData 采集的字段信息
     */
    void resolveData(String batchId, Map<String, Map<String, List<JSONObject>>> noticeMap, Map<String, String> taskCollect, String formData);

    /**
     * 根据任务ID 更新采集任务数据
     * @param recordEntity 采集任务数据
     */
    void updateVOByTaskId(Map<String, Object> recordEntity);
}
