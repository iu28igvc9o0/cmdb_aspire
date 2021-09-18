package com.aspire.mirror.alert.server.dao.monthReport;

import com.aspire.mirror.alert.server.vo.monthReport.AlertsMonReportAlertVo;
import com.aspire.mirror.alert.server.vo.monthReport.AlertsMonReportIdcTypeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AlertsMonReportDao {

    List<AlertsMonReportAlertVo> getDeviceData(Map<String,Object> param);

    void insertMonReportAlertDevice(AlertsMonReportAlertVo param);

    List<AlertsMonReportAlertVo> getAlertIndexData(Map<String,Object> param);

    void insertMonReportAlertIndex(AlertsMonReportAlertVo param);

    AlertsMonReportIdcTypeVo getAlertIdcData(Map<String,Object> param);

    List<Map<String,Object>> getAlertByDeviceType(Map<String,Object> param);

    void insertAlertMonReportIdc(AlertsMonReportIdcTypeVo param);

    List<Map<String,Object>> getIdcTypeAlertCount(Map<String,Object> param);

    void insertAlertMonReportSum(Map<String,Object> param);

    int getAlertIndexSum(Map<String,Object> param);

    List<Map<String,Object>> viewByIdcType(Map<String, String> map);

    List<Map<String,Object>> viewByIp(Map<String, String> map);

    List<Map<String,Object>> viewByKeyComment(Map<String, String> map);

}
