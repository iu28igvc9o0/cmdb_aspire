package com.aspire.mirror.alert.server.biz.alert;

import com.aspire.mirror.alert.server.vo.bpm.AlertBpmStartCallBack;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsDetail;
import com.aspire.mirror.alert.server.vo.alert.AlertMonitorObjectVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsOperationRequestVo;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsV2;
import com.aspire.mirror.alert.server.vo.alert.AlertsV2Vo;
import com.aspire.mirror.alert.server.util.Criteria;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.biz
 * @Author: baiwenping
 * @CreateTime: 2020-02-25 15:21
 * @Description: ${Description}
 */
public interface AlertsBizV2 {
    /**
     * 根据条件查询
     * @param alertQuery
     * @return
     */
    List<AlertsV2Vo> select(AlertsV2 alertQuery);

    /**
     * 修改告警数据
     *
     * @param map 告警修改对象
     * @return 影响数据条数
     */
    int updateByPrimaryKey (Map<String, Object> map);

    /**
     * 新增告警数据
     *
     * @param map 告警修改对象
     * @return 影响数据条数
     */
    String insert (Map<String, Object> map);

    /**
     * 根据告警ID集合删除告警记录
     *
     * @param alertIdArrays 告警ID集合
     * @return int 影响数据条数
     */
    int deleteByPrimaryKeyArrays(String[] alertIdArrays);

    /**
     * 根据告警id删除上报记录
     * @param alertId
     */
    void deleteAlertsDetail(String alertId);

    /**
     * 新增告警上报记录
     * @param alertDetail
     */
    void insertAlertsDetail(AlertsDetail alertDetail);

    /**
     * 根据主键查询alert
     *
     * @param alertId 告警ID
     * @return AlertsDTO 告警对象
     */
    AlertsV2Vo selectAlertByPrimaryKey(String alertId);

    /**
     * 根据id更新告警监控时间
     * @param alertId
     * @param curMoniTime
     */
    void updateCurMoniTime(String alertId, Date curMoniTime);

    /**
     * 根据Id查询告警全部字段数据
     * @param alertIds
     * @return
     */
    List<Map<String, Object>> selectByIds (List<String> alertIds);

    /**
     * 根据条件获取所有实例
     *
     * @param example
     * @return
     */
    List<Map<String, Object>> list(Criteria example);

    /**
     * 分页查询
     *
     * @param example
     * @return
     */
    PageResponse<Map<String, Object>> findPage(Criteria example);

    /**
     * 查询详情
     * @auther baiwenping
     * @Description
     * @Date 14:58 2020/3/12
     * @Param [alertId]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    Map<String, Object> detailById(String alertId);

    List<AlertMonitorObjectVo> getMonitObjectList();

    /**
     * 告警转派. <br/>
     * <p>
     *
     * @return void 告警转派
     */
    void alertTransfer(String namespace, String alertIds, String userIds);


    /**
     * 告警确认. <br/>
     * <p>
     *
     * @return void 告警确认
     */
    void  alertConfirm(AlertsOperationRequestVo request);

    void  alertObserve(Map<String,Object> request);

    /**
     * 派单分流
     * @param namespace
     * @param alertIds
     * @param orderType
     * @return
     */
    AlertBpmStartCallBack switchOrder(String namespace, String alertIds, Integer orderType);

    /**
     * 手工清除告警.  <br/>
     * <p>
     *
     * @param request
     */
    void manualClear(AlertsOperationRequestVo request);

    /**
     * 手动派单
     *
     * @param alertIds 告警ID集合
     * @param orderType
     */
    String genOrder(String namespace, String alertIds, Integer orderType);

    /** 
    * 修改通知状态
    * @auther baiwenping
    * @Description 
    * @Date 18:52 2020/3/23
    * @Param [unserName, alertIds]
    * @return void
    **/
    void notifyStatus(String status, List<String> alertIds);

    /**
     * 告警通知记录日志
     * @param type
     * @param namespace
     * @param alertIds
     * @param destination
     * @param message
     * @param status
     */
    void recordNotifyLog(String type, String namespace, String alertIds, List<String> destination, String message, String status);

    /**
     *
     * @param alertIds
     * @param status
     */
    void updateNotifyStatus (List<String> alertIds, String status);

    /**
     *
     * @param deviceIds
     * @return
     */
    List<Map<String, Object>> getDeviceNewestAlertLevelList(List<String> deviceIds);

    /**
     *
     * @param itemIds
     * @return
     */
    List<Map<String, Object>> getItemNewestAlertLevelList(String prefix, List<String> itemIds);

    /**
     * 分页查询
     *
     * @param example
     * @return
     */
    PageResponse<Map<String, Object>> queryDeviceAlertList(Criteria example);

    /**
     * 根据级别统计告警所属设备数
     * @param example
     * @return
     */
    List<Map<String, Object>> summaryDeviceAlertsByLevel(Criteria example);

    /**
     *
     * @param list
     * @return
     */
    List<String> checkOrderStatus(List<String> list);

    /**
     * 发送ping状态到kafka，用于cmdb消费
     * @param alert
     */
    void putPingStatusToKafka(AlertsV2Vo alert, String deviceType);
}
