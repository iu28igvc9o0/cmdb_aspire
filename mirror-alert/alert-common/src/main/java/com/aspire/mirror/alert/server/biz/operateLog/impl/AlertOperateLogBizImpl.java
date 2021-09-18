package com.aspire.mirror.alert.server.biz.operateLog.impl;

import com.aspire.mirror.alert.server.biz.operateLog.IAlertOperateLogBiz;
import com.aspire.mirror.alert.server.dao.operateLog.AlertOperateLogMapper;
import com.aspire.mirror.alert.server.dao.operateLog.po.AlertOperateLog;
import com.aspire.mirror.alert.server.vo.operateLog.AlertOperateLogDTO;
import com.aspire.mirror.common.entity.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:34
 */
@Service
public class AlertOperateLogBizImpl implements IAlertOperateLogBiz {

    @Autowired
    private AlertOperateLogMapper mapper;

    /**
     * 获取所有实例
     *
     * @return 返回所有实例数据
     */
    public List<AlertOperateLog> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     *
     * @param id 实例信息
     * @return 返回指定ID的数据信息
     */
    public AlertOperateLog get(Long id) {
        return mapper.get(id);
    }

    /**
     * 新增实例
     *
     * @param entity 实例数据
     * @return
     */
    public void insert(AlertOperateLog entity) {
        mapper.insert(entity);
    }

    /**
     * 修改实例
     *
     * @param entity 实例数据
     * @return
     */
    public void update(AlertOperateLog entity) {
        mapper.update(entity);
    }

    /**
     * 删除实例
     *
     * @param id 实例数据
     * @return
     */
    public void delete(Long id) {
        mapper.delete(id);
    }

    /**
     * 分页查询
     *
     * @param alertOperateLogDTO
     * @return
     */
    public PageResponse<AlertOperateLog> findPage(AlertOperateLogDTO alertOperateLogDTO) {
        Integer pageWithCount = mapper.findPageWithCount(alertOperateLogDTO);
        List<AlertOperateLog> pageWithResult = mapper.findPageWithResult(alertOperateLogDTO);
        PageResponse<AlertOperateLog> page = new PageResponse<>();
        page.setCount(pageWithCount);
        page.setResult(pageWithResult);
        return page;
    }

    /**
     * 新增告警规则操作记录
     *
     * @param content
     * @param operater
     * @param operateType
     * @param operateTypeDesc
     * @param relationIds
     */
    public void insertOperateLog(String content, String operater, String operateType, String operateTypeDesc, String operateModel, String operateModelDesc, String... relationIds) {
        List<AlertOperateLog> alertOperateLogs = new ArrayList<>();
        for (String relationId : relationIds) {
            AlertOperateLog alertOperateLog = new AlertOperateLog();
            alertOperateLog.setOperateContent(content);
//            alertOperateLog.setOperateModel(Constants.OPERATE_MODEL_ALERT_ISOLATE);
//            alertOperateLog.setOperateModelDesc(Constants.OPERATE_MODEL_ALERT_ISOLATE_DESC);
            alertOperateLog.setOperateModel(operateModel);
            alertOperateLog.setOperateModelDesc(operateModelDesc);
            alertOperateLog.setOperater(operater);
            alertOperateLog.setOperateType(operateType);
            alertOperateLog.setOperateTypeDesc(operateTypeDesc);
            alertOperateLog.setRelationId(relationId);
            alertOperateLog.setOperateTime(new Date());
//        alertOperateLog.setRemark();
            alertOperateLogs.add(alertOperateLog);
        }

        mapper.insertBatch(alertOperateLogs);
    }
}