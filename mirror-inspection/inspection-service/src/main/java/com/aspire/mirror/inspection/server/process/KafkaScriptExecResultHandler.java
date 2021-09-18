package com.aspire.mirror.inspection.server.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aspire.mirror.inspection.api.dto.ReportItemExt;
import com.aspire.mirror.inspection.api.dto.ReportItemValue;
import com.aspire.mirror.inspection.api.dto.model.ReportItemDTO;
import com.aspire.mirror.inspection.server.biz.ReportItemBiz;
import com.aspire.mirror.inspection.server.clientservice.TemplateApiClient;
import com.aspire.mirror.inspection.server.clientservice.payload.ItemsDetailResponse;
import com.aspire.mirror.inspection.server.clientservice.payload.ObjectItemInfo;
import com.aspire.mirror.inspection.server.clientservice.payload.TriggersDetailResponse;
import com.aspire.mirror.inspection.server.util.JsonUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 处理脚本执行返回
 * <p>
 * 项目名称:  mirror平台
 * 包:        com.aspire.mirror.collect.biz.itemvaluefetch
 * 类名称:    ScriptMontiorItemValueFetchImpl.java
 * 类描述:    处理脚本执行返回
 * 创建人:    JinSu
 * 创建时间:  2019/11/13 13:40
 * 版本:      v1.0
 */
@Slf4j
@Component
//@ConditionalOnProperty(name="middleware.configuration.switch.kafka", havingValue="true")
class KafkaScriptExecResultHandler {
    private static final String CONTAIN_PREFIX = "::contains::";
    private static final String CRON_PREFIX = "::cron::";
    @Autowired
    private ReportItemBiz reportItemService;

    @Autowired
    private TemplateApiClient templateApiClient;

    private static final String SCRIPT_EXEC_RESULT_TOPIC = "topic_script_collect";

    public static final String ASPNODE_TYPE_PATTERN = "aspnode_type=(json|normal)";
    public static final String ASPNODE_DESC_PATTERN = "aspnode_desc=([\\s\\S]*)";

//    public static final String STATUS_NORESULT = "2";
//    public static final String STATUS_NORMAL = "0";

    protected final ExecutorService INSPECTION_ITEM_TASK_POOL = new ThreadPoolExecutor(
            20, 100, 5, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(1000));

    @KafkaListener(topics = SCRIPT_EXEC_RESULT_TOPIC)
    public void listen(ConsumerRecord<?, String> cr) throws Exception {
        log.info("KafkaScriptExecResultHandler got kafka msg with offset {}", cr.offset());
        ObjectItemInfo itemInfo = JsonUtil.jacksonConvert(cr.value(), ObjectItemInfo.class);
        List<ReportItemDTO> reportItemList = Lists.newArrayList();
        String aspnodeType = "normal";
        if (itemInfo.getExtendObj() != null) {
            INSPECTION_ITEM_TASK_POOL.submit(new Runnable() {
                @Override
                public void run() {
                    parseReportItem(itemInfo, reportItemList, aspnodeType);
                }
            });
        }
    }

    private void parseReportItem(ObjectItemInfo itemInfo, List<ReportItemDTO> reportItemList, String aspnodeType) {
        try {
            JSONObject itemObj = JSON.parseObject(itemInfo.getExtendObj());
//        String[] logArray = itemObj.getString("log").split("\\n");
//        for (String logItem : logArray) {
//            if (logItem.indexOf("aspnode_type=") != -1) {
//                aspnodeType = logItem.substring("aspnode_type=".length());
//                break;
//            }
//        }
            Matcher matcher = Pattern.compile(ASPNODE_TYPE_PATTERN).matcher(itemObj.getString("log"));
            if (matcher.find()) {
                aspnodeType = matcher.group(1);
            }
//            if (itemObj.getString("log") != null) {
//                aspnodeType = itemObj.getString("aspnode_msg");
//            }

            ReportItemDTO reportItemVO = new ReportItemDTO();
            BeanUtils.copyProperties(itemInfo, reportItemVO);
            reportItemVO.setKey(itemInfo.getItemKey());
            reportItemVO.setServerType(itemInfo.getApiServerType());
            reportItemVO.setClock(itemObj.getInteger("clock"));
            reportItemVO.setStatus(ReportItemDTO.STATUS_NORESULT);
            reportItemVO.setValue(itemObj.getString("aspnode_msg"));
            Integer resultStatus = itemObj.getInteger("aspnode_result");
            if (resultStatus != null) {
                // 脚本结果为2 表示人工判断  指标状态3表示人工判断  特殊处理
                if (resultStatus.toString().equals("2")) {
                    reportItemVO.setStatus(ReportItemDTO.STATUS_ARTIFICIAL_JUDGMENT);
                } else {
                    reportItemVO.setStatus(resultStatus.toString());
                }
            }
            List<ReportItemValue> reportItemValueList = Lists.newArrayList();
            if (aspnodeType.equals("normal")) {

                ReportItemExt reportItemExt = new ReportItemExt();
                reportItemExt.setLog(itemObj.getString("log"));
                reportItemExt.setExecStatus(itemObj.getBoolean("flag") ? "1" : "0");
                reportItemExt.setAgentIp(reportItemVO.getObjectId());
                reportItemVO.setReportItemExt(reportItemExt);
                //添加item value
                ReportItemValue reportItemValue = new ReportItemValue();
                List<ItemsDetailResponse> itemList = templateApiClient.listItemsByIdArr(itemInfo.getItemId());
                reportItemValue.setResultName(itemList.get(0).getName());
                reportItemValue.setResultStatus(reportItemVO.getStatus());
                reportItemValue.setResultValue(reportItemVO.getValue());
                String resultDesc = "";
                //设置item描述
                Matcher matcherdesc = Pattern.compile(ASPNODE_DESC_PATTERN).matcher(itemObj.getString("log"));
                if (matcherdesc.find()) {
                    resultDesc = matcherdesc.group(1);
                }
//            for (String logItem : logArray) {
//                if (logItem.indexOf("aspnode_desc=") != -1) {
//                    resultDesc = logItem.substring("aspnode_desc=".length());
//                    break;
//                }
//            }
                reportItemValue.setResultDesc(resultDesc);
                reportItemValueList.add(reportItemValue);
            } else if (aspnodeType.equals("json")) {
                if (itemObj.get("aspnode_msg") != null) {
                    JSONArray itemListArray = itemObj.getJSONArray("aspnode_msg");
                    for (int i = 0; i < itemListArray.size(); i++) {
                        JSONObject subItemObj = itemListArray.getJSONObject(i);
                        ReportItemValue reportItemValue = new ReportItemValue();
                        reportItemValue.setResultName(subItemObj.getString("result_name"));
                        reportItemValue.setResultStatus(ReportItemDTO.STATUS_NORESULT);
                        reportItemValue.setResultValue(subItemObj.getString("result_value"));
                        if (StringUtils.isNotEmpty(subItemObj.getString("result_status"))) {
                            if (subItemObj.getString("result_status").equals("2")) {
                                reportItemValue.setResultStatus(ReportItemDTO.STATUS_ARTIFICIAL_JUDGMENT);
                            } else {
                                reportItemValue.setResultStatus(subItemObj.getString("result_status"));
                            }
                        }
                        reportItemValue.setResultDesc(subItemObj.getString("result_desc"));
                        reportItemValueList.add(reportItemValue);
                    }
                }
                ReportItemExt reportItemExt = new ReportItemExt();
                reportItemExt.setLog(itemObj.getString("log"));
                reportItemExt.setExecStatus(itemObj.getBoolean("flag") ? "1" : "0");
                reportItemExt.setAgentIp(itemInfo.getObjectId());
                reportItemVO.setReportItemExt(reportItemExt);
            }
            triggerMatch(reportItemVO, reportItemValueList);
            reportItemVO.setReportItemValueList(reportItemValueList);
            reportItemService.insert(reportItemVO);
        } catch (Exception e) {
            log.error("===KafkaScriptExecResultHandler[parseReportItem] is error", e);
        }
    }

    private void triggerMatch(ReportItemDTO reportItemVO, List<ReportItemValue> reportItemValueList) {

        if (!reportItemValueList.isEmpty()) {
            List<TriggersDetailResponse> triggerList = templateApiClient.listTriggersByItemIdArr(reportItemVO.getItemId());
            List<ItemsDetailResponse> itemList = templateApiClient.listItemsByIdArr(reportItemVO.getItemId());
            if (!CollectionUtils.isEmpty(itemList)) {
                reportItemVO.getReportItemExt().setItemGroup(itemList.get(0).getItemGroup());
                reportItemVO.getReportItemExt().setItemName(itemList.get(0).getName());
                reportItemVO.getReportItemExt().setBizGroup(itemList.get(0).getBizGroup());
            }
            if (!CollectionUtils.isEmpty(triggerList)) {
                List<String> expressionList = Lists.newArrayList();
                triggerList.stream().forEach(item -> expressionList.add(item.getExpression()));
                reportItemVO.getReportItemExt().setExpression(Joiner.on(",").join(expressionList));
                //阀值判断
                // 当前情况只有一个触发器，暂时默认多个触发器关系为且
                for (ReportItemValue reportItemValue : reportItemValueList) {
                    if (!reportItemValue.getResultStatus().equals(ReportItemDTO.STATUS_ARTIFICIAL_JUDGMENT)) {
                        if (StringUtils.isEmpty(reportItemValue.getResultValue())) {
                            reportItemValue.setResultStatus(ReportItemDTO.STATUS_NORESULT);
                        } else {
                            reportItemValue.setResultStatus(ReportItemDTO.STATUS_NORMAL);
                            for (TriggersDetailResponse trigger : triggerList) {
                                int containIdx = trigger.getExpression().indexOf(CONTAIN_PREFIX);
                                int cronIdx = trigger.getExpression().indexOf(CRON_PREFIX);
                                if (containIdx >= 0) {
                                    String containStr = trigger.getExpression().substring(containIdx + CONTAIN_PREFIX.length());
                                    //值包含逗号，或处理
                                    String[] containArray = containStr.split(",");
                                    for (String containItem : containArray) {
                                        if (reportItemValue.getResultValue().contains(containItem)) {
                                            reportItemValue.setResultStatus(ReportItemDTO.STATUS_EXCEPTION);
                                            break;
                                        }
                                    }
                                } else if (cronIdx >= 0) {
                                    String cronStr = trigger.getExpression().substring(cronIdx + CRON_PREFIX.length());
                                    Pattern p = Pattern.compile(cronStr);
                                    Matcher m = p.matcher(reportItemValue.getResultValue());
                                    while (m.find()) {
                                        reportItemValue.setResultStatus(ReportItemDTO.STATUS_EXCEPTION);
                                    }
                                } else {
                                    Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
                                    String sourceValue = reportItemValue.getResultValue().trim();
                                    Boolean isNum = true;
                                    if (!pattern.matcher(sourceValue).matches()) {
                                        // 非数字值，做字符串比较
                                        sourceValue = String.format("'%s'", sourceValue);
                                        isNum = false;
                                    }

                                    //值包含逗号，或处理
                                    String[] expressionValueArray = trigger.getExpressionValue(trigger.getExpression()).split(",");
                                    String match = trigger.getMatch(trigger.getExpression());
                                    if (match.equals("!=")) {
                                        Boolean isMatch = true;
                                        for (String expressionValue : expressionValueArray) {
                                            if (!isNum) {
                                                expressionValue = String.format("'%s'", expressionValue);
                                            }
                                            Expression compiledExp = AviatorEvaluator.compile(sourceValue + match + expressionValue);
                                            if (!Boolean.class.cast(compiledExp.execute())) {
                                                isMatch = false;
                                                break;
                                            }
                                        }
                                        if (isMatch) {
                                            reportItemValue.setResultStatus(ReportItemDTO.STATUS_EXCEPTION);
                                        }
                                    } else {
                                        for (String expressionValue : expressionValueArray) {
                                            if (!isNum) {
                                                expressionValue = String.format("'%s'", expressionValue);
                                            }
                                            Expression compiledExp = AviatorEvaluator.compile(sourceValue + match + expressionValue);
                                            boolean isMatch = Boolean.class.cast(compiledExp.execute());
                                            if (isMatch) {
                                                reportItemValue.setResultStatus(ReportItemDTO.STATUS_EXCEPTION);
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (reportItemValue.getResultStatus().equals(ReportItemDTO.STATUS_EXCEPTION)) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
