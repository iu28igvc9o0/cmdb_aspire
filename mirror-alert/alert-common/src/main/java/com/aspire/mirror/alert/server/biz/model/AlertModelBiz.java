package com.aspire.mirror.alert.server.biz.model;

import com.aspire.mirror.alert.server.dao.model.po.AlertModel;
import com.aspire.mirror.alert.server.vo.model.AlertModelVo;

import java.util.List;

public interface AlertModelBiz {

    /**
     *  添加告警模型
     */
    void insertAlertModel(AlertModelVo request);
    /**
     *  获取告警模型列表
     */
    List<AlertModel> getAlertModelList(String modelName, String tableName);
    /**
     *  获取告警模型树
     */
    Object getAlertModelTreeData();
    /**
     *  删除告警模型
     */
    void deleteAlertModel(String userName,List<String> request);
    /**
     *  获取告警模型详情
     */
    AlertModel getAlertModelDetail(String id);
    /**
     *  编辑告警模型数据
     */
    void updateAlertModel(AlertModelVo request);
}
