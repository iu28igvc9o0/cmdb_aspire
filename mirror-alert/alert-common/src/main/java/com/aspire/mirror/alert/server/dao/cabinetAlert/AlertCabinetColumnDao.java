package com.aspire.mirror.alert.server.dao.cabinetAlert;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aspire.mirror.alert.server.dao.cabinetAlert.po.AlertCabinetColumnConfig;
import com.aspire.mirror.alert.server.dao.cabinetAlert.po.AlertCabinetColumnConfigData;
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
public interface AlertCabinetColumnDao {
    /**
     * 新增
     *
     * @param alerts 告警对象
     */
	void batchInsertConfig(List<AlertCabinetColumnConfig> config);
    
    /**
     * 修改
     *
     * @param alerts 告警对象
     */
    int updateConfig(AlertCabinetColumnConfig config);
    
    
    void updateConfigDataByConfig(AlertCabinetColumnConfigData config);
    
    void updateConfigDataById(Map<String,Object> params);
    
    /**
     * 根据主键查询
     *
     * @param alertId 主键
     * @return AlertsHis 返回对象
     */
    AlertCabinetColumnConfig selectConfigByPrimaryKey(@Param(value = "id") int id);
    
    List<AlertCabinetColumnConfig> getConfigList();
    
    AlertCabinetColumnConfig getConfig(Map<String,Object> params);
    
    List<AlertCabinetColumnConfigData> getConfigDataList(Map<String,Object> params);
    
    //查询列头柜
    List<AlertCabinetColumnConfigData> selectCabinetList(Map<String,Object> params);
    //查询没有初始化的列头柜
    List<AlertCabinetColumnConfigData> selectNotInitialCabinetList(Map<String,Object> params);
    //查询不存在的列头柜
    void delConfigData(Map<String,Object> params);
    
    
    void batchInsertCabinetColumnData(List<AlertCabinetColumnConfigData> configData);


    /**
     * 机柜告警分页查询
     *
     * @param page 分页对象
     * @return List<AlertsHis> 告警历史
     */
    List<Map<String,Object>> queryCabinetAlertPageList(Page page);

    /**
     * 查询count
     * @param page 分页对象
     * @return int 数据条数
     */
    int queryCabinetAlertPageListCount(Page page);
    
    
    /**
     * 列头柜页面分页查询
     *
     * @param page 分页对象
     * @return List<AlertsHis> 告警历史
     */
    List<AlertCabinetColumnConfigData> queryCCAlertPageList(Page page);

    /**
     * 查询count
     * @param page 分页对象
     * @return int 数据条数
     */
    int queryCCAlertPageListCount(Page page);
    
    /**
     * 列头柜页面分页列头柜查询
     *
     * @param page 分页对象
     * @return List<AlertsHis> 告警历史
     */
    List<AlertCabinetColumnConfigData> queryCCAlertHPageList(Page page);

    /**
     * 查询列头柜count
     * @param page 分页对象
     * @return int 数据条数
     */
    int queryCCAlertHPageListCount(Page page);
    
    /**
     * 机柜页面分页列头柜查询
     *
     * @param page 分页对象
     * @return List<AlertsHis> 告警历史
     */
    List<AlertCabinetColumnConfigData> queryCCAlertMPageList(Page page);

    /**
     * 查询机柜count
     * @param page 分页对象
     * @return int 数据条数
     */
    int queryCCAlertMPageListCount(Page page);
    
    /**
     * 电源页面分页列头柜查询
     *
     * @param page 分页对象
     * @return List<AlertsHis> 告警历史
     */
    List<AlertCabinetColumnConfigData> queryCCAlertDPageList(Page page);

    /**
     * 查询电源count
     * @param page 分页对象
     * @return int 数据条数
     */
    int queryCCAlertDPageListCount(Page page);
    
    /**
     * 正常页面分页列头柜查询
     *
     * @param page 分页对象
     * @return List<AlertsHis> 告警历史
     */
    List<AlertCabinetColumnConfigData> queryCCAlertNPageList(Page page);

    /**
     * 查询正常count
     * @param page 分页对象
     * @return int 数据条数
     */
    int queryCCAlertNPageListCount(Page page);
    
    /**
     * 禁用页面分页列头柜查询
     *
     * @param page 分页对象
     * @return List<AlertsHis> 告警历史
     */
    List<AlertCabinetColumnConfigData> queryCCAlertFPageList(Page page);

    /**
     * 查询禁用count
     * @param page 分页对象
     * @return int 数据条数
     */
    int queryCCAlertFPageListCount(Page page);
    
    
    /**
     * 设备电源告警页面分页查询
     *
     * @param page 分页对象
     * @return List<AlertsHis> 告警历史
     */
    List<Map<String,Object>> queryDeviceAlertPageList(Page page);

    /**
     *  设备电源告警查询count
     * @param page 分页对象
     * @return int 数据条数
     */
    int queryDeviceAlertPageListCount(Page page);
    
    /**
     * 影响业务分页查询
     *
     * @param page 分页对象
     * @return List<AlertsHis> 告警历史
     */
    List<Map<String,Object>> queryBizSystemPageList(Page page);

    /**
     * 影响业务查询count
     * @param page 分页对象
     * @return int 数据条数
     */
    int queryBizSystemHisPageListCount(Page page);
    
    /**
     * 影响业务分页查询
     *
     * @param page 分页对象
     * @return List<AlertsHis> 告警历史
     */
    List<Map<String,Object>> queryBizSystemHisPageList(Page page);

    /**
     * 影响业务查询count
     * @param page 分页对象
     * @return int 数据条数
     */
    int queryBizSystemPageListCount(Page page);

    
    /**
     *正常的影响业务页面分页查询
     *
     * @param page 分页对象
     * @return List<AlertsHis> 告警历史
     */
    List<Map<String,Object>> queryBizSystemNormalPageList(Page page);

    /**
     * 正常的影响业务查询count
     * @param page 分页对象
     * @return int 数据条数
     */
    int queryBizSystemNormalPageListCount(Page page);
    
    /**
     * 查询时间范围内的机柜下的电源告警
     * @param params
     * @return
     */
    int queryPowerDeviceCount(Map<String,Object> params);
    
    /**
     * 查询列头柜的机柜告警数量，或者机柜告警
     * @param params
     * @return
     */
    int queryCabinetCount(Map<String,Object> params);
    
    
    int updateConfigDataAll();
    
    
    List<Map<String,Object>> queryRelateBizSystemList(Page page);
    
    int queryRelateBizSystemListCount(Page page);
    
    int queryCabinetPowerDeviceCount(Map<String,Object> params);

    int queryDeviceAlertColumnPageListCount(Page page);

    List<Map<String, Object>> queryDeviceAlertColumnPageList(Page page);
}
