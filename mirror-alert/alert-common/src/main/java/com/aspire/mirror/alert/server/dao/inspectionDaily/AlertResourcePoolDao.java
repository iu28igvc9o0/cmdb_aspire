package com.aspire.mirror.alert.server.dao.inspectionDaily;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.alert.server.dao.inspectionDaily.po.AlertDeviceTypeConfig;
import com.aspire.mirror.alert.server.dao.inspectionDaily.po.AlertDeviceTypeTop;
import com.aspire.mirror.alert.server.dao.inspectionDaily.po.AlertInspectionDaily;
import com.aspire.mirror.alert.server.dao.inspectionDaily.po.AlertMoniterObjectTop;
import com.aspire.mirror.alert.server.dao.inspectionDaily.po.AlertResourcePool;
import com.aspire.mirror.alert.server.dao.inspectionDaily.po.AlertResourcePoolNew;
import com.aspire.mirror.alert.server.vo.inspectionDaily.AlertRourcePoolVo;
import com.aspire.mirror.alert.server.vo.inspectionDaily.AlertTotalVo;
import com.aspire.mirror.common.entity.Page;


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
public interface AlertResourcePoolDao {
    
	/**
	 * 查询资源池告警总数
	 * @param page
	 * @return
	 */
    List<AlertResourcePool> getResourcePoolCountAlert(AlertRourcePoolVo page);
    
    /**
	 * 查询资源池告警分布
	 * @param page
	 * @return
	 */
    List<AlertResourcePool> getResourcePoolaRecordAlert(Page page);
    
    List<AlertResourcePoolNew> getResourcePoolaRecordAlertNew(AlertRourcePoolVo page);
    
    /**
     * 查询告警分布数量
     * @param page
     * @return
     */
    Integer getResourcePoolaRecordAlertCount(Page page);
    
    /**
     * 查询设备top10
     * @param page
     * @return
     */
    List<AlertDeviceTypeTop> getDeviceTop10Alert(AlertRourcePoolVo page);
    
    /**
     * 查询告警指标top10
     * @param page
     * @return
     */
    List<AlertMoniterObjectTop> getMoniterTop10Alert(AlertRourcePoolVo page);
    
    
    //查询top分页数据
    List<AlertResourcePool> getDeviceTypeList(AlertRourcePoolVo page);
    //查询top分页数量
    Integer getDeviceTypeListCount(Page page);
    
    /**
     * 巡检日报
     * @param page
     * @return
     */
    List<AlertInspectionDaily> getInspectionDaily(Page page);
    
    Integer getInspectionDailyCount(Page page);
    
    List<Map<String,Object>> exportInspectionDaily(Page page);
    
    void insertAlertTotal(List<AlertTotalVo> list);
    
    void insertAlertDeviceTop(List<AlertDeviceTypeTop> list);
    
    void insertAlertMonisterTop(List<AlertMoniterObjectTop> list);
    
    void insertAlertDistribution(List<AlertResourcePoolNew> list);
    
    //查询配置的deviceType项
   List<AlertDeviceTypeConfig> getDeviceTypeConfiged(@Param(value = "type") String type);
    
   void deleteCountByMonth(@Param(value = "month") String month);
   
   void deleteRecordByMonth(@Param(value = "month") String month);
   
   void deleteDeviceByMonth(@Param(value = "month") String month);
   
   void deleteMoniterByMonth(@Param(value = "month") String month);
   //从分布表查数据
   List<AlertResourcePoolNew> getResourcePoolaRecordFromNew(@Param(value = "month") String month);
   
   
   void updateAlertMonisterTop(AlertMoniterObjectTop list);
   
   void updateAlertDeviceTop(AlertDeviceTypeTop list);
   
   void updateAlertDistribution(AlertResourcePoolNew list);
   
   void updateAlertTotal(AlertTotalVo list);
   
    
}
