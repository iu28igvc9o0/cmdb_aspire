package com.aspire.mirror.alert.server.biz.scanComparision;


import com.aspire.mirror.alert.server.vo.scanComparision.AlertScanComparisionDetailVo;
import com.aspire.mirror.alert.server.vo.scanComparision.AlertScanComparisionReqVo;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.List;
import java.util.Map;

public interface AlertScanComparisionBiz {

    PageResponse<AlertScanComparisionDetailVo> getScanComparisionList(AlertScanComparisionReqVo request);

    void deleteScanComparisionById(List<String> request);

    void synScanComparision(List<Map<String,String>> request);

    List<Map<String, Object>> exportScanComparision(AlertScanComparisionReqVo request);

    void alertScanComparisonSchedule();

    void compareCmdb();
}
