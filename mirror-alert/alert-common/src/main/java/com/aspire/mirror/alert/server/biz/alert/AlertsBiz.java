package com.aspire.mirror.alert.server.biz.alert;

import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsDetail;
import com.aspire.mirror.alert.server.dao.notify.po.AlertsNotify;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsRecord;
import com.aspire.mirror.alert.server.vo.alert.AlertDeviceInformationVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.alert.server.vo.notify.NotifyPageVo;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 告警业务层接口
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.biz.impl
 * 类名称:    AlertsBiz.java
 * 类描述:    告警业务层接口
 * 创建人:    JinSu
 * 创建时间:  2018/9/14 15:55
 * 版本:      v1.0
 */
public interface AlertsBiz {

    /**
     * 修改感觉数据
     *
     * @param alertsVo 告警修改对象
     * @return 影响数据条数
     */
    int updateByPrimaryKey(AlertsVo alertsVo);

    /**
     * 根据主键查询alert
     *
     * @param alertId 告警ID
     * @return AlertsDTO 告警对象
     */
    AlertsVo selectAlertByPrimaryKey(String alertId);

    /**
     * 根据条件查询
     * @param alertQuery
     * @return
     */
    List<AlertsVo> select(Alerts alertQuery);
    
   
    /**
     * 告警上报分页
     *
     * @param alertId 告警Id
     * @return
     */
    PageResponse<AlertsDetail> alertGenerateListByPage(String alertId, String pageNo, String pageSize);


	/**
     * 告警操作分页
     *
     * @param alertId 告警Id
     * @return
     */
	PageResponse<AlertsRecord> alertRecordListByPage(String alertId, String pageNo, String pageSize);


	/**
     * 告警通知分页
     *
     * @param alertId 告警Id
     * @return
     */
	NotifyPageVo<AlertsNotify> alertNotifyListByPage(String alertId, String pageNo, String pageSize, String reportType);


    /**
     * 告警 相关告警
     *
     * @param alertId 告警ID集合
     * @return List<AlertsDTO> 告警列表
     */
    List<AlertsVo> selectAlertGenerateList(String alertId);


    /**
     * 根据主键查询alertRecord
     *
     * @param alertId 告警ID
     * @return alertRecord 告警对象
     */
    List<AlertsRecord> selectAlertRecordByPrimaryKey(String alertId);

    /**
     * 根据主键查询alertNotify
     *
     * @param alertId 告警ID
     * @return alertNotify 告警对象
     */
    List<AlertsNotify> selectalertNotifyByPrimaryKey(String alertId);



    /**
     * 告警列表
     *
     * @param page 告警查询page对象
     * @return PageResponse<AlertsDTO> 告警分页返回对象
     */
    PageResponse<AlertsVo> pageList(PageRequest page);

    /**
     * 告警列表查询
     *
     * @param alertIdArrays 告警ID集合
     * @return List<AlertsDTO> 告警列表
     */
    List<AlertsVo> selectByPrimaryKeyArrays(String[] alertIdArrays);


    /**
     * 告警修改备注. <br/>
     * <p>
     * 作者： pengguihua
     *
     * @return void 告警修改备注
     */
    void  updateNote(String alertId, String note);

    /**
     * 根据工单ID修改工单状态
     *
     * @param orderId     工单ID
     * @param orderStatus 工单状态
     */
    void modOrderStatusByOrderId(String orderId, String orderStatus);

    List<Map<String,Object>> getAlertConfig();

	List<Map<String, String>> getFirstBusiness(Map<String, Object> param);

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
     * 提供bpm侧接口，同步告警事件
     * @param oldOrderId
     * @param orderId
     * @param type
     * @param orderStatus
     * @param userName
     * @return
     */
    String upgrade(String oldOrderId, String orderId, String type,String orderStatus, String userName);

    /**
     * 根据id更新告警监控时间
     * @param alertId
     * @param curMoniTime
     */
    void updateCurMoniTime(String alertId, Date curMoniTime);

    int selectAlert(PageRequest pageRequest);

    int getAlertValue(Map<String,List<String>> ipMap, List<String> alertLevel, List<String> itemIdList);

    AlertDeviceInformationVo queryAlertHList(Map<String, Object> map);

    AlertDeviceInformationVo queryAlertMList(Map<String, Object> map);

    Map<String,Object> selectAlertByAlertId(String alertId);


}
