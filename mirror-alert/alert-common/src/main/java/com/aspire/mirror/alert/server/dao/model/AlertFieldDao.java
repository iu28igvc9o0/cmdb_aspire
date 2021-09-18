package com.aspire.mirror.alert.server.dao.model;

import com.aspire.mirror.alert.server.dao.model.po.AlertField;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AlertFieldDao {

    /**
     *  添加告警模型字段
     */
    void insertAlertModel(AlertFieldVo requestDTO);
    /**
     *  根据ID获取告警模型字段详情
     */
    AlertField getAlertFieldDetailById(@Param("id") String id);
    /**
     *  根据ID删除告警模型字段详情
     */
    void deleteAlertFieldDetailById(@Param("id") String id,@Param("modelId") String modelId);
    /**
     *  修改告警模型字段
     */
    void updateAlertField(AlertFieldVo requestDTO);
    /**
     * 获取告警模型字段列表
     */
    List<AlertField> getAlertFieldListByModelId(Map<String, Object> param);
    int getAlertFieldListCountByModelId(Map<String, Object> param);
    /**
     *  获取告警模型列表
     */
    void updateLockStatus(@Param("id") String id,@Param("isLock") String isLock);
    List<AlertFieldVo>  getAlertFieldListByTableName(@Param("tableName") String tableName, @Param("sort") String sort);
    /**
     * 告警模型字段表 添加列
     */
    void addFieldColumn(Map<String, Object> map);
    /**
     * 告警模型字段表 删除列
     */
    void deleteFieldColumn(@Param("tableName") String tableName,
                           @Param("fieldCode") String fieldCode);
}
