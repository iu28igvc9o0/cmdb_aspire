package com.aspire.mirror.alert.server.dao.model;

import com.aspire.mirror.alert.server.dao.model.po.AlertModel;
import com.aspire.mirror.alert.server.vo.model.AlertModelVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertModelDao {

    /**
     *  添加告警模型
     */
    void insertAlertModel(AlertModelVo request);
    /**
     *  获取告警模型列表
     */
    List<AlertModel> getAlertModelList(@Param("modelName") String modelName,
                                       @Param("tableName") String tableName);

    List<AlertModel> getAlertModelTreeData();
    /**
     *  删除告警模型
     */
    void deleteAlertModel(@Param("list") List<String> request);

    /**
     *  根据id获取告警模型详情
     */
    AlertModel getAlertModelDetail(@Param("id") String id);
    /**
     *  编辑告警模型
     */
    void updateAlertModel(AlertModelVo request);
}
