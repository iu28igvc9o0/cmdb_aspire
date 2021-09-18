package com.aspire.mirror.alert.server.biz.bpm.impl;

import com.aspire.mirror.alert.server.biz.bpm.IAlertOrderHandleBiz;
import com.aspire.mirror.alert.server.constant.OrderStatusEnum;
import com.aspire.mirror.alert.server.dao.bpm.AlertOrderHandleMapper;
import com.aspire.mirror.alert.server.dao.alert.AlertsV2Dao;
import com.aspire.mirror.alert.server.dao.bpm.po.AlertOrderHandle;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsV2;
import com.aspire.mirror.common.util.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 描述：
 *
 * @author baiwenping
 * @date 2019-08-14 19:35:33
 */
@Service
public class AlertOrderHandleBizImpl implements IAlertOrderHandleBiz {

    @Resource
    private AlertOrderHandleMapper mapper;
    @Resource
    private AlertsV2Dao alertsV2Dao;

    /**
     * 获取所有实例
     *
     * @return 返回所有实例数据
     */
    public List<AlertOrderHandle> list() {
        return mapper.list();
    }

    /**
     * 根据主键ID 获取数据信息
     *
     * @param id 实例信息
     * @return 返回指定ID的数据信息
     */
    public AlertOrderHandle get(Integer id) {
        return mapper.get(id);
    }

    /**
     * 新增实例
     * @param orderId 工单编号
     * @param execTime 执行时间
     * @param status 状态
     */
    public ResponseEntity<String> insert(String orderId, String execTime, String status, String execUser, String account) {
        //已关闭则删除
        if (OrderStatusEnum.FINISH.getCode().equals(status)) {
            mapper.deleteByOrderId(orderId);
            return new ResponseEntity<>("{\"status\":\"success\"}", HttpStatus.OK);
        }
        List<AlertOrderHandle> list = mapper.getByOrderId(orderId);
        Date date = DateUtil.getDate(execTime, DateUtil.DATE_TIME_CH_FORMAT);
        if (date == null) {
            date = new Date();
        }
        if (CollectionUtils.isEmpty(list)) {

            AlertsV2 alertsV2 = alertsV2Dao.selectByOrderId(orderId);
            if (alertsV2 == null) {
                // 如果工单对应告警不存在，则不插入对应工单状态
                return new ResponseEntity<>("{\"status\":\"orderId not exist\"}", HttpStatus.OK);
            }
            // 新增
            AlertOrderHandle alertOrderHandle = new AlertOrderHandle();
            alertOrderHandle.setOrderId(orderId);
            alertOrderHandle.setStatus(status);
            alertOrderHandle.setAccount(account);
            alertOrderHandle.setExecUser(execUser);
            alertOrderHandle.setExecTime(date);
            mapper.insert(alertOrderHandle);
        } else {
            // 更新
            for (AlertOrderHandle alertOrderHandle: list) {
                alertOrderHandle.setExpireStatus(null);
                alertOrderHandle.setAccount(account);
                alertOrderHandle.setExecUser(execUser);
                alertOrderHandle.setExecTime(date);
                alertOrderHandle.setStatus(status);
                mapper.update(alertOrderHandle);
            }
        }
        return new ResponseEntity<>("{\"status\":\"success\"}", HttpStatus.OK);
    }

    /**
     * 修改实例
     *
     * @param entity 实例数据
     */
    public void update(AlertOrderHandle entity) {

        mapper.update(entity);
    }

    /**
     * 删除实例
     *
     * @param ids 实例数据
     */
    public void delete(String operater, String... ids) {
        mapper.delete(ids);
    }

    /**
     * 根据工单号删除工单状态记录
     * @param orderId
     */
    public void deleteByOrderId (String orderId) {
        mapper.deleteByOrderId(orderId);
    }
}