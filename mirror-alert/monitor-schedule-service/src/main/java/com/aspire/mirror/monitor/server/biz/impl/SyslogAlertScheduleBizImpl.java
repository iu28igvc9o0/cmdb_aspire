package com.aspire.mirror.monitor.server.biz.impl;

import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.log.api.dto.CreateLogAlertLinkedReq;
import com.aspire.mirror.log.api.dto.LogAlertRuleDetail;
import com.aspire.mirror.log.api.dto.SysLogResponse;
import com.aspire.mirror.log.api.dto.SysLogSearchRequest;
import com.aspire.mirror.monitor.server.biz.SyslogAlertScheduleBiz;
import com.aspire.mirror.monitor.server.client.SysLogServiceClient;
import com.aspire.mirror.monitor.server.domain.AlertModel;
import com.aspire.mirror.monitor.server.util.DateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.*;

@Slf4j
@Service
@Transactional
public class SyslogAlertScheduleBizImpl implements SyslogAlertScheduleBiz {
    @Value("${alertScan.centerRoom.kafka.topicName:TOPIC_SYSTEM_ALERTS}")
    private String topicName;
    @Value("${alertScan.centerRoom.kafka.partionsCount:9}")
    private Integer	topicPartionsCount;
    @Autowired
    private SysLogServiceClient sysLogServiceClient;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Async
    public void SyslogAlertScheduleService(LogAlertRuleDetail logAlertRuleDetail, SysLogSearchRequest sysLogSearchRequest) {
        // 根据规则获取日志数据
        PageResponse<SysLogResponse> logData = sysLogServiceClient.getLogData(sysLogSearchRequest);
        List<String> ipList = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(logData.getResult())) {
//            List<AlertFieldRequestDTO> alertFieldList = alertHandleHelper.getModelField(AlertConfigConstants.REDIS_MODEL_ALERT);
            for (SysLogResponse sysLogResponse : logData.getResult()) {
                String ip = sysLogResponse.getIp();
                if (StringUtils.isEmpty(ip)) {
                    continue;
                }
                ip = ip.trim();
                // 相同规则相同ip一次只有一个告警
                if (ipList.contains(ip)) {
                    continue;
                }
                ipList.add(ip);
                // 创建日志告警
                AlertModel alertModel = new AlertModel();
                String uuid = UUID.randomUUID().toString();
                alertModel.setAlertId(uuid);
                alertModel.setAlertLevel(logAlertRuleDetail.getAlertLevel());
                String dateStr = DateUtil.formatDate(DateUtil.DATE_POINT_FORMAT1);
                alertModel.setCurMoniTime(dateStr);
                alertModel.setAlertStartTime(dateStr);
                alertModel.setDeviceIP(ip);

                alertModel.setMonitorIndex(sysLogResponse.getMassage());
                alertModel.setSource("sysLog");
                alertModel.setBusinessSystem(sysLogResponse.getPool());
                alertModel.setZbxItemId(logAlertRuleDetail.getUuid());
                alertModel.setKeyComment(logAlertRuleDetail.getKeyComment());
                alertModel.setMoniResult(alertModel.MONI_RESULT_ACTIVE);
                String lastIpNum = ip.substring(ip.lastIndexOf(".") + 1);
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                try {
                    String json = mapper.writeValueAsString(alertModel);
                    if (StringUtils.isNumeric(lastIpNum)) {
                        int partionNum = Integer.parseInt(lastIpNum) % topicPartionsCount;
                        kafkaTemplate.send(topicName, partionNum, json);
                    } else {
                        //ip配置不对的也发送到ums，后续人工定位问题
                        kafkaTemplate.send(topicName, json);
                    }
                    sysLogServiceClient.insertLogAlertLinked(new CreateLogAlertLinkedReq(logAlertRuleDetail.getUuid(), uuid));
                } catch (JsonProcessingException e) {
                }
            }
        }
    }

}
