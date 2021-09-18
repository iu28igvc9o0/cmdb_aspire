package com.aspire.mirror.alert.server.biz.monthReport;

import com.aspire.mirror.alert.server.vo.monthReport.AlertEsDataVo;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface AlertMonReportBiz {

    Map<String,Object> viewByIdcType(Map<String, String> map);

    List<Map<String,Object>> viewByIp(Map<String, String> map);

    List<Map<String,Object>> viewByKeyComment(Map<String, String> map);

    Map<String, Object> getIdcTypeUserRate(AlertEsDataVo request) throws ParseException;

    List<Map<String, Object>> getIdcTypeTrends(AlertEsDataVo request);
}
