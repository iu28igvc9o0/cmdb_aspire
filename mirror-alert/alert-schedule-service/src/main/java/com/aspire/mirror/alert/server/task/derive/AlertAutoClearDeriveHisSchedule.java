package com.aspire.mirror.alert.server.task.derive;

import com.aspire.mirror.alert.server.util.StringUtils;
import com.aspire.mirror.alert.server.biz.model.AlertFieldBiz;
import com.aspire.mirror.alert.server.biz.derive.IAlertDeriveAlertsBizV2;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 自动清除已消除的衍生日志
 */
@Component
@Slf4j
public class AlertAutoClearDeriveHisSchedule {

    @Autowired
    private IAlertDeriveAlertsBizV2 alertDeriveAlertsBizV2;
    @Autowired
    private AlertFieldBiz alertFieldBiz;

    @Scheduled(cron = "0 0 0 * * ?")
    public void run() {
        log.info("自动清除已消除的衍生日志开始");
        List<Map<String, Object>> list = alertDeriveAlertsBizV2.selectAlertHisIds();
        for (Map<String, Object> idMap: list) {
            String deriveAlertId = MapUtils.getString(idMap, "derive_alert_id");
            if (!StringUtils.isEmpty(deriveAlertId)) {
                alertDeriveAlertsBizV2.deleteByAlertId(deriveAlertId, alertFieldBiz.getModelField(AlertConfigConstants.REDIS_MODEL_DERIVE_ALERT_HIS));
            }
        }
        log.info("自动清除已消除的衍生日志结束");
    }
}
