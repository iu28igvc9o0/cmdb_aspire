package com.aspire.mirror.alert.server.biz.monthReport;

import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;

import java.util.List;
import java.util.Map;

public interface AlertsScheduleBiz {

    void alert(String startTime,String endTime,String month);

    void device(String startTime,String endTime,String month);

    void alertIndex(String startTime,String endTime,String month);

    void alertSum(String startTime,String endTime,String month);

    void synchronize();

    void insertCmdb (Map<String, Object> ciMap, List<Map<String, Object>> list, List<AlertFieldVo> modelFromRedis);

    void deviceInspectionByDay();

    void bizSystemInspectionByDay();
}
