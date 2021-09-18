package com.aspire.mirror.alert.server.biz.alert;

import com.aspire.mirror.alert.server.vo.alert.AlertStatisticSummaryVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;

import java.util.List;

public interface AlertIntelligentBiz {

    PageResponse<AlertsVo> queryAlertIntelligent(PageRequest pageRequest, String alertId);

    AlertStatisticSummaryVo alertIntelligentOverview(PageRequest pageRequest);

    List<AlertsVo> exportAlertIntelligentData(PageRequest pageRequest);

}
