package com.aspire.mirror.alert.server.biz.bpm;

import com.aspire.mirror.alert.server.dao.bpm.po.AlertOrderHandle;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 描述：
 *
 * @author
 * @date 2019-08-14 19:35:33
 */
public interface IAlertOrderHandleBiz {
    /**
     * 获取所有实例
     *
     * @return 返回所有实例数据
     */
    List<AlertOrderHandle> list();

    /**
     * 根据主键ID 获取数据信息
     *
     * @param id 实例信息
     * @return 返回实例信息的数据信息
     */
    AlertOrderHandle get(Integer id);

    /**
     * 新增实例
     * @param orderId
     * @param execTime
     * @param status
     */
    ResponseEntity<String> insert(String orderId, String execTime, String status, String execUser, String account);

    /**
     * 修改实例
     *
     * @param entity 实例数据
     * @return
     */
    void update(AlertOrderHandle entity);

    /**
     * 删除实例
     *
     * @param ids 实例数据
     * @return
     */
    void delete(String operater, String... ids);

    /**
     * 根据工单号删除工单状态记录
     * @param orderId
     */
    void deleteByOrderId (String orderId);
}