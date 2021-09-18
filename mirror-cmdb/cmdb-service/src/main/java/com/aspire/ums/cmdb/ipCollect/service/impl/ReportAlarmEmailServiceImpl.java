package com.aspire.ums.cmdb.ipCollect.service.impl;

import com.aspire.ums.cmdb.common.Constants;
import com.aspire.ums.cmdb.helper.JDBCHelper;
import com.aspire.ums.cmdb.ipCollect.mapper.CmdbIpCollectMapper;
import com.aspire.ums.cmdb.ipCollect.service.ReportAlarmEmailService;
import com.aspire.ums.cmdb.sqlManage.CmdbSqlManage;
import com.aspire.ums.cmdb.util.HttpUtil;
import com.aspire.ums.cmdb.util.JsonUtil;
import com.aspire.ums.cmdb.util.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fanwenhui
 * @date 2020-12-21 15:16
 * @description 报表数据告警邮件业务逻辑具体实现 - 存活IP,稽核报表的数据
 */
@Slf4j
@Service
public class ReportAlarmEmailServiceImpl implements ReportAlarmEmailService {

    @Autowired
    private JDBCHelper jdbcHelper;
    @Resource
    private CmdbIpCollectMapper mapper;
    @Value("${report.alert.mail:http://10.153.98.82:9196/sendHtmlMail}")
    private String alertMail;

    @Override
    public Map<String, String> sendAndGetReportAlarmContent(String type) {
        Map<String,String> retMap = new HashMap<>();
        Map<String, Object> statementParams = new HashMap<>();
        List<Map<String, String>> cmdbReportSql = mapper.findCmdbReportSql(type);
        String email = "";
        String subject = "";
        if (null == cmdbReportSql || cmdbReportSql.isEmpty()) {
            log.info("未查询到报表的告警配置项");
            return null;
        }
        Map<String,String> sendMailMap = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (Map<String, String> map : cmdbReportSql) {
            String reportSql = map.get("reportSql");
            if (StringUtils.isEmpty(reportSql)) {
                log.info("报表【{}】没有配置告警查询sql",map.get("reportName"));
                continue;
            }
            String reportParam = map.get("reportParam");
            if (StringUtils.isNotEmpty(reportParam)) {
                String[] split = reportParam.split(",");
                for (String param : split) {
                    String[] split1 = param.split(":");
                    statementParams.put(split1[0],split1[1]);
                }
            }
            CmdbSqlManage countManage = new CmdbSqlManage(reportSql, Constants.INSTANCE_AUTH_MODULE, Constants.UN_NEED_AUTH);
            Integer count = jdbcHelper.getInt(countManage, "", statementParams);
            if (count == 0) {
                log.info("报表【{}】没有数据，请手工核查",map.get("reportName"));
                if (StringUtils.isEmpty(email)) {
                    email = map.get("reportEmail");
                }
                if (StringUtils.isEmpty(subject)) {
                    subject = map.get("reportSubject");
                }
                sb.append("【").append(map.get("reportName")).append(map.get("typeName")).
                        append("没有数据，请手工核查").append("】").append("\n");
            }
        }
        if (sb.length() > 1) {
            sendMailMap.put("content",sb.toString());
            sendMailMap.put("sendTo",email);
            sendMailMap.put("subject", StringUtils.isNotEmpty(subject) ? subject : "告警邮件通知");
            sendMailMap.put("fromNickname", "cmic_noc_alarm@139.com");
            retMap = sendAlarmEmail(sendMailMap);
        }

        return retMap;
    }

    @Override
    public Map<String, String> sendAlarmEmail(Map<String, String> param) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            String json = JsonUtil.toJacksonJson(param);
            String postMsg = HttpUtil.post(alertMail, json);
            if (StringUtils.isNotEmpty(postMsg)) {
                resultMap = JsonUtil.jacksonConvert(postMsg, new TypeReference<Map<String, String>>() {});
                String success = resultMap.get("success");
                if ("true".equals(success)) {
                    log.info("报表数据核查告警邮件发送成功");
                }
            }
        } catch (Exception e) {
            log.error("报表数据核查告警邮件发送失败",e);
        }
        return resultMap;
    }
}
