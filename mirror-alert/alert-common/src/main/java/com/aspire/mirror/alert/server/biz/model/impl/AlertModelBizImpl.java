package com.aspire.mirror.alert.server.biz.model.impl;

import com.aspire.mirror.alert.server.dao.operateLog.AlertOperateLogMapper;
import com.aspire.mirror.alert.server.dao.operateLog.po.AlertOperateLog;
import com.aspire.mirror.alert.server.biz.model.AlertModelBiz;
import com.aspire.mirror.alert.server.dao.model.AlertModelDao;
import com.aspire.mirror.alert.server.dao.model.po.AlertModel;
import com.aspire.mirror.alert.server.vo.model.AlertModelVo;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AlertModelBizImpl implements AlertModelBiz {

    @Autowired
    private AlertModelDao alertModelDao;
    @Autowired
    private AlertOperateLogMapper alertOperateLogMapper;

    @Override
    public void insertAlertModel(AlertModelVo request) {
        // 设置id
        request.setId( UUID.randomUUID().toString());
        // 写入数据库
        alertModelDao.insertAlertModel(request);
        // 添加操作日志
        AlertOperateLog alertOperateLog = new AlertOperateLog();
        alertOperateLog.setOperateContent("新增模型");
        alertOperateLog.setOperateModel("alert_model");
        alertOperateLog.setOperateModelDesc("告警模型");
        alertOperateLog.setOperater(request.getCreator());
        alertOperateLog.setOperateTime(new Date());
        alertOperateLog.setOperateType("insert");
        alertOperateLog.setOperateTypeDesc("新增");
        alertOperateLogMapper.insert(alertOperateLog);
    }

    @Override
    public List<AlertModel> getAlertModelList(String modelName, String tableName) {
        return alertModelDao.getAlertModelList(modelName, tableName);
    }

    @Override
    public Object getAlertModelTreeData() {
        List<AlertModel> res = Lists.newArrayList();
        List<AlertModel> alertModelTreeData = alertModelDao.getAlertModelTreeData();
        res.add(getTree(alertModelTreeData,null));
        return res;
    }

    private AlertModel getTree(List<AlertModel> data,
                               AlertModel alertModel) {
        if (null == alertModel) {
            alertModel = new AlertModel();
            alertModel.setId("all");
            alertModel.setParentId("-1");
            alertModel.setModelName("模型");
            alertModel.setDescription("根节点");
            alertModel.setSort(0);
            alertModel.setChildList(new ArrayList<AlertModel>());
            getTree(data, alertModel);
        } else {
            List<AlertModel> childList = alertModel.getChildList();
            for (AlertModel datum : data) {
                if (datum.getParentId().equals(alertModel.getId())) {
                    childList.add(datum);
                    getTree(data,datum);
                }
            }
            List<AlertModel> sorted =
                    childList.stream().sorted(Comparator.comparing(AlertModel::getSort)).collect(Collectors.toList());
            alertModel.setChildList(sorted);
        }
        return alertModel;
    }

    @Override
    public void deleteAlertModel(String userName,List<String> request) {
        alertModelDao.deleteAlertModel(request);
        // 操作日志
        AlertOperateLog alertOperateLog = new AlertOperateLog();
        alertOperateLog.setOperateContent("删除模型");
        alertOperateLog.setOperateModel("alert_model");
        alertOperateLog.setOperateModelDesc("告警模型");
        alertOperateLog.setOperater(userName);
        alertOperateLog.setOperateTime(new Date());
        alertOperateLog.setOperateType("delete");
        alertOperateLog.setOperateTypeDesc("删除");
        alertOperateLogMapper.insert(alertOperateLog);
    }

    @Override
    public AlertModel getAlertModelDetail(String id) {
        return alertModelDao.getAlertModelDetail(id);
    }

    @Override
    public void updateAlertModel(AlertModelVo request) {
        alertModelDao.updateAlertModel(request);
        // 操作日志
        AlertOperateLog alertOperateLog = new AlertOperateLog();
        alertOperateLog.setOperateContent("编辑模型");
        alertOperateLog.setOperateModel("alert_model");
        alertOperateLog.setOperateModelDesc("告警模型");
        alertOperateLog.setOperater(request.getUpdater());
        alertOperateLog.setOperateTime(new Date());
        alertOperateLog.setOperateType("update");
        alertOperateLog.setOperateTypeDesc("编辑");
        alertOperateLogMapper.insert(alertOperateLog);
    }
}
