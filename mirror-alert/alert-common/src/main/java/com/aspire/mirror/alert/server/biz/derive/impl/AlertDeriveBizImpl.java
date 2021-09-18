package com.aspire.mirror.alert.server.biz.derive.impl;

import com.aspire.mirror.alert.server.biz.derive.IAlertDeriveBiz;
import com.aspire.mirror.alert.server.biz.operateLog.IAlertOperateLogBiz;
import com.aspire.mirror.alert.server.constant.Constants;
import com.aspire.mirror.alert.server.dao.derive.AlertDeriveMapper;
import com.aspire.mirror.alert.server.dao.derive.po.AlertDerive;
import com.aspire.mirror.alert.server.util.AlertFilterCommonUtil;
import com.aspire.mirror.alert.server.vo.derive.AlertDeriveVo;
import com.aspire.mirror.common.entity.PageResponse;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
@Service
public class AlertDeriveBizImpl implements IAlertDeriveBiz {

    @Autowired
    private AlertDeriveMapper mapper;

    @Autowired
    private IAlertOperateLogBiz alertOperateLogBiz;

    /**
     * 获取所有实例
     *
     * @return 返回所有实例数据
     */
    public List<AlertDerive> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     *
     * @param id 实例信息
     * @return 返回指定ID的数据信息
     */
    public AlertDerive get(String id) {
        return mapper.get(id);
    }

    /**
     * 校验名称是否可用
     * @param name
     * @return
     */
    public boolean checkName (String name) {
        List<AlertDerive> alertDerives = mapper.checkName(name);
        if (CollectionUtils.isEmpty(alertDerives)) {
            return true;
        }
        return false;
    }

    /**
     * 新增实例
     *
     * @param entity 实例数据
     * @return
     */
    public void insert(AlertDerive entity) {
        mapper.insert(entity);
        alertOperateLogBiz.insertOperateLog("新增规则", entity.getCreater(), Constants.OPERATE_TYPE_INSERT, Constants.OPERATE_TYPE_INSERT_DESC, Constants.OPERATE_MODEL_ALERT_DERIVE, Constants.OPERATE_MODEL_ALERT_DERIVE_DESC, entity.getId().toString());
    }

    /**
     * 修改实例
     *
     * @param entity 实例数据
     * @return
     */
    public void update(AlertDerive entity) {

        AlertDerive alertDerive = mapper.get(entity.getId().toString());
        String content = getOperateContent(entity, alertDerive);
        mapper.update(entity);
        alertOperateLogBiz.insertOperateLog(content, entity.getEditer(), Constants.OPERATE_TYPE_UPDATE, Constants.OPERATE_TYPE_UPDATE_DESC, Constants.OPERATE_MODEL_ALERT_DERIVE, Constants.OPERATE_MODEL_ALERT_DERIVE_DESC, entity.getId().toString());
    }

    private String getOperateContent (AlertDerive newDerive, AlertDerive oldDerive) {
        StringBuffer sb = new StringBuffer("修改内容,由");
        if(newDerive == null || oldDerive == null) {
            return sb.toString();
        }
        sb.append(AlertFilterCommonUtil.getOptionStr(oldDerive.getOptionCondition())).append("改为").append(AlertFilterCommonUtil.getOptionStr(newDerive.getOptionCondition()));
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
        alertOperateLogBiz.insertOperateLog("删除规则", operater, Constants.OPERATE_TYPE_DELETE, Constants.OPERATE_TYPE_DELETE_DESC, Constants.OPERATE_MODEL_ALERT_DERIVE, Constants.OPERATE_MODEL_ALERT_DERIVE_DESC, ids);
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
        alertOperateLogBiz.insertOperateLog(content, operater, Constants.OPERATE_TYPE_UPDATE, Constants.OPERATE_TYPE_UPDATE_DESC, Constants.OPERATE_MODEL_ALERT_DERIVE, Constants.OPERATE_MODEL_ALERT_DERIVE_DESC, ids);
    }

    public PageResponse<AlertDerive> findPage(AlertDeriveVo alertDeriveVo) {
        List<AlertDerive> pageWithResult = null;
        if (alertDeriveVo.getPageSize() < 0) {
            pageWithResult = mapper.list();
        } else {

            pageWithResult = mapper.findPageWithResult(alertDeriveVo);
        }
        Integer pageWithCount = mapper.findPageWithCount(alertDeriveVo);
        PageResponse<AlertDerive> page = new PageResponse<>();
        page.setCount(pageWithCount);
        page.setResult(pageWithResult);
        return page;
    }

    @Override
    public void copyDerive(String id) {
        // 获取衍生规则信息
        AlertDerive alertDerive = mapper.get(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String name = alertDerive.getName();
        if (org.apache.commons.lang.StringUtils.isNotBlank(name) && name.contains("_")) {
            name = name.split("_")[0];
        }
        alertDerive.setName(name + "_" + sdf.format(new Date()));
        alertDerive.setStatus("0");
        mapper.insert(alertDerive);
        alertOperateLogBiz.insertOperateLog("新增规则", alertDerive.getCreater(), Constants.OPERATE_TYPE_INSERT, Constants.OPERATE_TYPE_INSERT_DESC, Constants.OPERATE_MODEL_ALERT_DERIVE, Constants.OPERATE_MODEL_ALERT_DERIVE_DESC, alertDerive.getId().toString());
    }
}