package com.aspire.mirror.alert.server.biz.exceptionAlert.impl;

import com.aspire.mirror.alert.server.biz.operateLog.IAlertOperateLogBiz;
import com.aspire.mirror.alert.server.constant.Constants;
import com.aspire.mirror.alert.server.util.AlertFilterCommonUtil;
import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.alert.server.biz.exceptionAlert.IAlertConfigExceptionBiz;
import com.aspire.mirror.alert.server.dao.exceptionAlert.AlertConfigExceptionMapper;
import com.aspire.mirror.alert.server.dao.exceptionAlert.po.AlertConfigException;
import com.aspire.mirror.alert.server.vo.exceptionAlert.AlertConfigExceptionVo;
import com.aspire.mirror.common.entity.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
@Service
public class AlertConfigExceptionBizImpl implements IAlertConfigExceptionBiz {

    @Autowired
    private AlertConfigExceptionMapper mapper;

    @Autowired
    private IAlertOperateLogBiz alertOperateLogBiz;

    /**
     * 获取所有实例
     *
     * @return 返回所有实例数据
     */
    public List<AlertConfigException> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     *
     * @param id 实例信息
     * @return 返回指定ID的数据信息
     */
    public AlertConfigException get(String id) {
        return mapper.get(id);
    }

    /**
     * 新增实例
     *
     * @param entity 实例数据
     * @return
     */
    public void insert(AlertConfigException entity) {
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(UUID.randomUUID().toString());
        }
        mapper.insert(entity);
        alertOperateLogBiz.insertOperateLog("新增规则", entity.getCreater(), Constants.OPERATE_TYPE_INSERT, Constants.OPERATE_TYPE_INSERT_DESC, Constants.OPERATE_MODEL_ALERT_EXCEPTION, Constants.OPERATE_MODEL_ALERT_EXCEPTION_DESC, entity.getId().toString());
    }

    /**
     * 修改实例
     *
     * @param entity 实例数据
     * @return
     */
    public void update(AlertConfigException entity) {

        AlertConfigException AlertConfigException = mapper.get(entity.getId().toString());
        String content = getOperateContent(entity, AlertConfigException);
        mapper.update(entity);
        alertOperateLogBiz.insertOperateLog(content, entity.getEditer(), Constants.OPERATE_TYPE_UPDATE, Constants.OPERATE_TYPE_UPDATE_DESC, Constants.OPERATE_MODEL_ALERT_EXCEPTION, Constants.OPERATE_MODEL_ALERT_EXCEPTION_DESC, entity.getId().toString());
    }

    private String getOperateContent (AlertConfigException newIsolate, AlertConfigException oldIsolate) {
        StringBuffer sb = new StringBuffer("修改内容,由");
        if(newIsolate == null || oldIsolate == null) {
            return sb.toString();
        }
        sb.append(AlertFilterCommonUtil.getOptionStr(oldIsolate.getOptionCondition())).append("改为").append(AlertFilterCommonUtil.getOptionStr(newIsolate.getOptionCondition()));
        return  sb.toString();
    }

    /**
     * 删除实例
     *
     * @param ids 实例数据
     * @return
     */
    public void delete(String operater, String... ids) {
        mapper.delete(ids);
        alertOperateLogBiz.insertOperateLog("删除规则", operater, Constants.OPERATE_TYPE_DELETE, Constants.OPERATE_TYPE_DELETE_DESC, Constants.OPERATE_MODEL_ALERT_EXCEPTION, Constants.OPERATE_MODEL_ALERT_EXCEPTION_DESC, ids);
    }

    /**
     * 启动/停用屏蔽规则
     *
     * @param status
     * @param ids
     */
    public void status(String status, String operater, String... ids) {
        mapper.status(status, ids);
        String content = "修改状态，由停用修改为启用";
        if (Constants.ISOLATE_STATUS_STOP.equals(status)) {
            content = "修改状态，由启用修改为停用";
        }
        alertOperateLogBiz.insertOperateLog(content, operater, Constants.OPERATE_TYPE_UPDATE, Constants.OPERATE_TYPE_UPDATE_DESC, Constants.OPERATE_MODEL_ALERT_EXCEPTION, Constants.OPERATE_MODEL_ALERT_EXCEPTION_DESC, ids);
    }

    public PageResponse<AlertConfigException> findPage(AlertConfigExceptionVo alertConfigExceptionVo) {
        List<AlertConfigException> pageWithResult = null;
        if (alertConfigExceptionVo.getPageSize() < 0) {
            pageWithResult = mapper.list();
        } else {

            pageWithResult = mapper.findPageWithResult(alertConfigExceptionVo);
        }
        Integer pageWithCount = mapper.findPageWithCount(alertConfigExceptionVo);
        PageResponse<AlertConfigException> page = new PageResponse<>();
        page.setCount(pageWithCount);
        page.setResult(pageWithResult);
        return page;
    }
}