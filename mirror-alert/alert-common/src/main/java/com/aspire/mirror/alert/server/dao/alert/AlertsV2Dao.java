package com.aspire.mirror.alert.server.dao.alert;

import com.aspire.mirror.alert.server.vo.alert.AlertMonitorObjectVo;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsV2;
import com.aspire.mirror.alert.server.util.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.dao
 * @Author: baiwenping
 * @CreateTime: 2020-02-25 15:30
 * @Description: ${Description}
 */
@Mapper
public interface AlertsV2Dao {

    /**
     * 根据告警对象查询告警列表
     *
     * @param alert 告警对象
     * @return List<Alerts> 告警列表
     */
    List<AlertsV2> select(AlertsV2 alert);

    /**
     * 修改告警数据
     *
     * @param map 告警修改对象
     * @return 影响数据条数
     */
    int updateByPrimaryKey (@Param("map") Map<String, Object> map);

    /**
     * 新增告警数据
     *
     * @param map 告警修改对象
     * @return 影响数据条数
     */
    void insert (@Param("map")Map<String, Object> map);

    /**
     * 批量删除告警
     * @param alertIdArrays 告警列表
     * @return int 影响数据条数
     */
    int deleteByPrimaryKeyArrays(String[] alertIdArrays);

    /**
     * 根据主键查询
     *
     * @param alertId 主键
     * @return Alerts 返回对象
     */
    AlertsV2 selectByPrimaryKey(@Param(value = "alertId") String alertId);
    AlertsV2 selectByOrderId(@Param(value = "orderId") String orderId);

    /** 
    * 
    * @auther baiwenping
    * @Description 
    * @Date 16:52 2020/2/27
    * @Param [map]
    * @return void
    **/
    
    void updateCurMoniTime(Map<String, Object> map);

    /**
     * 查询告警列表
     *
     * @param alertIdArrays 告警id集合
     * @return List<Alerts> 告警列表
     */
    List<AlertsV2> selectByPrimaryKeyArrays(String[] alertIdArrays);
    /**
     * 查询满足工单的参数信息
     *
     * @param param 查询参数
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> selectOrderParam1(Map<String, Object> param);
    /**
    * 
    * @auther baiwenping
    * @Description 
    * @Date 15:08 2020/3/6
    * @Param [alertIds]
    * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    **/
    List<Map<String, Object>> selectByIds (List<String> alertIds);

    /**
     * 根据id查询原始数据
     * @auther baiwenping
     * @Description
     * @Date 15:08 2020/3/6
     * @Param [alertIds]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    List<Map<String, Object>> selectDetailByIds (List<String> alertIds);

    /**
     * 获取所有实例
     *
     * @param example 实例信息
     * @return 返回所有实例数据
     */
    List<Map<String, Object>> listByEntity(@Param("example") Criteria example);

    /**
     * 分页查询
     *
     * @param example
     * @return
     */
    List<Map<String, Object>> findPageWithResult(Criteria example);

    /**
     * 分页查询计数
     *
     * @param example
     * @return
     */
    Integer findPageWithCount(Criteria example);

    /** 
    * 查询详情
    * @auther baiwenping
    * @Description 
    * @Date 14:58 2020/3/12
    * @Param [alertId]
    * @return java.util.Map<java.lang.String,java.lang.Object>
    **/
    Map<String, Object> detailById(String alertId);

    /**
     * 获取告警监控对象列表
     */
    List<AlertMonitorObjectVo> getMonitObjectList();

    void updateNotifyStatus (@Param("list") List<String> alertIds, @Param("status") String status);

    /**
     * 告警工单升级为调优工单
     */
    void updateOrderByOrderId (@Param("oldOrderId") String oldOrderId,
                             @Param("newOrderId") String newOrderId);

    List<Map<String,Object>> deviceCountByDeviceClass(Map<String, Object> param);

    /**
     *
     * @param deviceIds
     * @return
     */
    List<Map<String,Object>> getDeviceNewestAlertLevelList(@Param("deviceIds") List<String> deviceIds);

    /**
     *
     * @param itemIds
     * @return
     */
    List<Map<String,Object>> getItemNewestAlertLevelList(@Param("prefix") String prefix, @Param("itemIds") List<String> itemIds);


    /**
     * 分页查询
     *
     * @param example
     * @return
     */
    List<Map<String, Object>> queryDeviceAlertList(Criteria example);

    /**
     * 分页查询计数
     *
     * @param example
     * @return
     */
    Integer queryDeviceAlertCount(Criteria example);

    /**
     * 根据级别统计告警所属设备数
     * @param example
     * @return
     */
    List<Map<String, Object>> summaryDeviceAlertsByLevel(Criteria example);

    /**
     * 根据工单号查找存在的工单号
     * @param list
     * @return
     */
    List<Map<String, Object>> checkOrderStatus(@Param("list") List<String> list);
}
