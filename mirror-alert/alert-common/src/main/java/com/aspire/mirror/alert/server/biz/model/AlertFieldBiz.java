package com.aspire.mirror.alert.server.biz.model;

import com.aspire.mirror.alert.server.dao.model.po.AlertField;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.List;
import java.util.Map;

public interface AlertFieldBiz {

    /** 
    * 
    * @auther baiwenping
    * @Description 
    * @Date 15:47 2020/2/21
    * @Param [tableName]
    * @return java.util.List<com.aspire.mirror.alert.server.vo.model.AlertFieldRequestDTO>
    **/
    
    List<AlertFieldVo> getModelFromRedis (String tableName, String sort);

    /**
     *  添加告警模型字段
     */
    void insertAlertModel(AlertFieldVo requestDTO);
    /**
     *  根据ID获取告警模型字段详情
     */
    AlertField getAlertFieldDetailById(String id);
    /**
     *  根据ID删除告警模型字段详情
     */
    void deleteAlertFieldDetailById(String id, String modelId, String userName);
    /**
     *  修改告警模型字段
     */
    void updateAlertField(AlertFieldVo requestDTO);
    /**
     * 获取告警模型字段列表
     */
    PageResponse<AlertField> getAlertFieldListByModelId(Map<String,Object> request);
    /**
     * 修改锁定状态
     */
    void updateLockStatus(String id, String modelId, String isLock, String userName);
    /**
     * 同步告警模型字段
     */
    void synchronizeField(String modelId, String userName);

    /**
     * 告警模型字段缓存
     * @param tableName
     * @return
     */
    List<AlertFieldVo> getModelField (String tableName);
}
