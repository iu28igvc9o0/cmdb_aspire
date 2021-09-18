package com.aspire.mirror.alert.server.biz.alert;

import com.aspire.mirror.alert.server.dao.alert.po.Alerts;
import com.aspire.mirror.alert.server.dao.alert.po.AlertsHis;
import com.aspire.mirror.alert.server.vo.alert.AlertStatisticSummaryVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsQueryVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsVo;
import com.aspire.mirror.alert.server.vo.alert.AlertsHisVo;
import com.aspire.mirror.common.entity.PageRequest;
import com.aspire.mirror.common.entity.PageResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AlertHomeBiz {

    PageResponse<AlertsVo> select(PageRequest pageRequest, String alertType);

    List<String> activeAlertSourceList();

    AlertStatisticSummaryVo getOverview(PageRequest pageRequest, String alertType);

    PageResponse<AlertsHisVo> selectHis(PageRequest pageRequest);

    List<Alerts> export(PageRequest pageRequest, String alertType);

    List<AlertsHis> exportHis(PageRequest pageRequest);

    ResponseEntity<String> getHomeAlertVoiceContent(boolean isUandS, AlertsQueryVo queryRequest);

    AlertStatisticSummaryVo hisOverview(PageRequest pageRequest);

}
