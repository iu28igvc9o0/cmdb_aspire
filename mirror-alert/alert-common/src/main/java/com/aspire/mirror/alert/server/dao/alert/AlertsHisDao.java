package com.aspire.mirror.alert.server.dao.alert;

import com.aspire.mirror.alert.server.dao.alert.po.AlertsHis;
import com.aspire.mirror.common.entity.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 告警DAO
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.alert.server.dao
 * 类名称:    AlertsHisDao.java
 * 类描述:    告警DAO
 * 创建人:    JinSu
 * 创建时间:  2018/9/18 16:16
 * 版本:      v1.0
 */
@Mapper
public interface AlertsHisDao {
    /**
     * 新增
     *
     * @param alerts 告警对象
     */
    void insert(AlertsHis alerts);


    /**
     * 分页查询
     *
     * @param page 分页对象
     * @return List<AlertsHis> 告警历史
     */
    List<AlertsHis> pageList(Page page);

    /**
     * 查询count
     * @param page 分页对象
     * @return int 数据条数
     */
    int pageListCount(Page page);

    int getAlertHisCount(Page page);
    List<AlertsHis> getAlertHisList(Page page);

    /**
     * 根据主键查询
     *
     * @param alertId 主键
     * @return AlertsHis 返回对象
     */
    AlertsHis selectByPrimaryKey(@Param(value = "alertId") String alertId);
    
     List<AlertsHis> selectListByPrimaryKey(List<String> alertIds);
    
    /**
     * 查询分页数量
     * @param   
     */
    int getAlertGenerateCount(Map<String, Object> hashmap);
    
   
    /**
     * 查询分页数据
     * @param   
     */
    List<AlertsHis> getAlertGenerateByPage(Map<String, Object> hashmap);
    
    
    /**
     * 查询所有相关告警列表
     *
     * @param deviceIp 告警ip 
     * @return  
     */
    List<AlertsHis> selectAllAlertGenerateList(@Param("deviceIp") String deviceIp, @Param("moniObject") String moniObject,
                                               @Param("alertLevel") String alertLevel);
    
  

    /**
     * 批量插入告警历史数据
     *
     * @param listAlert 告警历史
     * @return int 插入数据条数
     */
    int insertByBatch(List<AlertsHis> listAlert);
    
    
    /**
     * 根据主键更新数据
     *
     * @param alertsHis 警告PO对象
     * @return int 数据条数
     */
    int updateByPrimaryKey(AlertsHis alertsHis);
    /**
     * 根据告警对象查询告警历史列表
     *
     * @param alertHis 告警历史对象
     * @return List<Alerts> 告警列表
     */
    List<AlertsHis> select(AlertsHis alertHis);

    /**
     * 查询要关闭的告警工单
     * @return
     */
    List<AlertsHis> selectClosingAerts();


	List<Map<String, Object>> getRelateData(@Param("alertId")String alertId,@Param("pageSize")Integer pageSize
			,@Param("begin")Integer begin);
	
	int getRelateDataCount(@Param("alertId")String alertId);
	
	
	int getRelateHisDataCount(@Param("alertId")String alertId);
	
	List<Map<String, Object>> getRelateHisData(@Param("alertId")String alertId,@Param("pageSize")Integer pageSize
			,@Param("begin")Integer begin);
}
