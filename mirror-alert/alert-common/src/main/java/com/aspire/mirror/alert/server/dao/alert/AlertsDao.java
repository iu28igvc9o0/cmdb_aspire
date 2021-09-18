package com.aspire.mirror.alert.server.dao.alert;

import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsStatisticOverview;
import com.aspire.mirror.alert.server.vo.alert.AlertDeviceInformationVo;
import com.aspire.mirror.common.entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 告警DAO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.dao
 * 类名称:    AlertsDao.java
 * 类描述:    告警DAO
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:16
 * 版本:      v1.0
 */
@Mapper
public interface AlertsDao {
    /**
     * 分页查询
     * @param page 分页对象
     * @return List<Alerts> 告警列表返回
     */
    List<Alerts> pageList(Page page);

    /**
     * 查询count
     * @param page 分页对象
     * @return int 数据条数
     */
    int pageListCount(Page page);

    /**
     * 根据条件删除重复告警
     * @param delMap
     * @return
     */
    int deleteByParams(Map<String, Object> delMap);

    /**
     * 根据主键更新数据
     *
     * @param alerts 警告PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(Alerts alerts);


    /**
     * 根据主键查询
     *
     * @param alertId 主键
     * @return Alerts 返回对象
     */
    Alerts selectByPrimaryKey(@Param(value = "alertId") String alertId);

    /**
     * 查询所有相关告警列表
     *
     * @param deviceIp 告警ip
     * @return List<Alerts> 告警列表
     */
    List<Alerts> selectAllAlertGenerateList(@Param("deviceIp") String deviceIp, @Param("moniObject") String moniObject,
                                            @Param("alertLevel") String alertLevel);
    

    
    /**
     * 根据操作代码，获取警报
     */
    Map<String,Object> selectSummeryByOperateCode(@Param(value = "param") Map<String, Object> param,
                                                  @Param(value = "resFilterMap") Map<String, List<String>> resFilterMap);

    Map<String,Object> selectSummeryByOperateCodeOrder(@Param(value = "param") Map<String, Object> param,
                                                  @Param(value = "resFilterMap") Map<String, List<String>> resFilterMap);
    List<Alerts> selectOverview();
    /**
     * 查询告警列表
     *
     * @param alertIdArrays 告警id集合
     * @return List<Alerts> 告警列表
     */
    List<Alerts> selectByPrimaryKeyArrays(String[] alertIdArrays);

    /**
     * 根据告警对象查询告警列表
     *
     * @param alert 告警对象
     * @return List<Alerts> 告警列表
     */
    List<Alerts> select(Alerts alert);

    List<Alerts> selectLatest(@Param(value = "limit") Integer limit);
    List<Alerts> selectLatestByCurMoniTime(@Param(value = "limit") Integer limit,
                                           @Param(value = "resFilterMap") Map<String, List<String>> resFilterMap);

    List<AlertsStatisticOverview> selectByDateRange(@Param(value = "beginDate") Date beginDate, @Param(value = "endDate") Date endDate);
    /**
     * 查询满足工单的参数信息
     *
     * @param param 查询参数
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> selectOrderParam(Map<String, Object> param);
    /**
     * 查询满足工单的参数信息
     *
     * @param param 查询参数
     * @return List<Map<String, Object>>
     */
    List<Map<String, Object>> selectOrderParam1(Map<String, Object> param);
    /**
     * 根据工单ID修改工单状态
     *
     * @param orderId     工单ID
     * @param orderStatus 工单状态
     */
    void modOrderStatusByOrderId(@Param("orderId") String orderId, @Param("orderStatus") String orderStatus);

	
	/**
     * 新增
     * @param alerts zabbix告警数据
     */
    int insertProcessAlerts(Alerts alerts);

    List<String> selectMoniObj();
    List<String> selectHisMoniObj();

	List<Map<String, Object>> getAlertConfig();

	List<Map<String, String>> getFirstBusiness(Map<String, Object> param);

    List<Map<String, Object>> selectAuotAlertData(HashMap<Object, Object> param);

    void updateCurMoniTime(Map<String, Object> map);

    List<Map<String, Object>> getAlertsByCreateTime(@Param("startTime") String startTime,
                                       @Param("endTime") String endTime,
                                       @Param("autoType") String autoType,
                                       @Param("isConfirm") String isConfirm);
	
	int getAlertValue(@Param("ipMap") Map<String,List<String>> ipMap, @Param("alertLevel") List<String> alertLevel, @Param("itemIdList")List<String> itemIdList);


    AlertDeviceInformationVo queryAlertHList(Map<String,Object> map );

    AlertDeviceInformationVo queryAlertMList(Map<String,Object> map);

    Map<String,Object> selectAlertByAlertId(@Param(value = "alertId") String alertId);
}
