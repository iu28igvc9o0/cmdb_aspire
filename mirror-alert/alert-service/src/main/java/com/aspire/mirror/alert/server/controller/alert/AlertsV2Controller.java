package com.aspire.mirror.alert.server.controller.alert;

import com.aspire.mirror.alert.api.dto.*;
import com.aspire.mirror.alert.api.dto.alert.AlertsConfirmRequest;
import com.aspire.mirror.alert.api.dto.bpm.AlertsOrderRequest;
import com.aspire.mirror.alert.api.dto.notify.AlertsNotifyRequest;
import com.aspire.mirror.alert.api.dto.alert.QueryParams;
import com.aspire.mirror.alert.api.dto.notify.SmsNotifyParams;
import com.aspire.mirror.alert.api.service.alert.AlertsV2Service;
import com.aspire.mirror.alert.server.biz.ftp.FtpService;
import com.aspire.mirror.alert.server.util.DateUtils;
import com.aspire.mirror.alert.server.vo.bpm.AlertBpmStartCallBack;
import com.aspire.mirror.alert.server.vo.alert.AlertsOperationRequestVo;
import com.aspire.mirror.alert.server.helper.AuthHelper;
import com.aspire.mirror.alert.server.util.ExportExcelUtil;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import com.aspire.mirror.alert.server.biz.model.AlertFieldBiz;
import com.aspire.mirror.alert.server.biz.alert.AlertsBizV2;
import com.aspire.mirror.alert.server.biz.notify.IAlertNotifyTemplateBiz;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.alert.server.controller.CommonController;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.aspire.mirror.alert.server.util.Criteria;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.aspire.mirror.alert.server.util.PayloadParseUtil.jacksonBaseParse;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.controller.alert
 * @Author: baiwenping
 * @CreateTime: 2020-03-06 16:27
 * @Description: ${Description}
 */
@Slf4j
@RestController
public class AlertsV2Controller extends CommonController implements AlertsV2Service {
    @Autowired
    private AlertsBizV2 alertsBizV2;
    @Autowired
    private AlertFieldBiz alertFieldBiz;
    @Autowired
    private FtpService ftpService;
    @Autowired
    private IAlertNotifyTemplateBiz alertNotifyTemplateBiz;
    @Autowired
    private AuthHelper authHelper;

    /**
     * 活动/确认 告警 查询列表
     *
     * @param queryParams
     * @return
     */
    @Override
    public PageResponse<Map<String, Object>> query(@RequestBody QueryParams queryParams) {
        setDataPermission(queryParams);
        List<AlertFieldVo> modelFromRedisList = alertFieldBiz.getModelFromRedis(AlertConfigConstants.REDIS_MODEL_ALERT, null);

        return alertsBizV2.findPage(generateCriteria(queryParams, modelFromRedisList));
    }

    /**
     * 导出警报列表数据
     *
     * @param queryParams
     */
    @Override
    public Map<String, Object> export(@RequestBody QueryParams queryParams) {
        setDataPermission(queryParams);
        Long time = System.currentTimeMillis();
        List<AlertFieldVo> modelFromRedisList = alertFieldBiz.getModelFromRedis(AlertConfigConstants.REDIS_MODEL_ALERT, null);
        List<String> headerList = Lists.newArrayList();
        List<String> keyList = Lists.newArrayList();
        modelFromRedisList = modelFromRedisList.stream().sorted(Comparator.comparing(AlertFieldVo::getListShowSort)).collect(Collectors.toList());
        modelFromRedisList.stream().forEach(alertFieldRequestDTO -> {
            if (AlertConfigConstants.YES.equals(alertFieldRequestDTO.getIsListShow())) {
                headerList.add(alertFieldRequestDTO.getListShowName());
                keyList.add(alertFieldRequestDTO.getFieldCode());
            }
        });
        Map<String, Object> map = new HashMap<>();
        Criteria criteria = generateCriteria(queryParams, modelFromRedisList);
        String exportField = getExportField(modelFromRedisList);
        criteria.setExportField(exportField);
        List<Map<String, Object>> pageResult = alertsBizV2.list(criteria);
        log.info("-----1.query his use-----: {} ms", (System.currentTimeMillis() - time));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != pageResult && pageResult.size() > 0) {
            for (Map<String, Object> map1 : pageResult) {
                for (Map.Entry<String, Object> m : map1.entrySet()) {
                    if (m.getKey().equals("alert_start_time") && m.getValue() != null) {
                        Date date = DateUtils.stringToDate(DateUtils.DEFAULT_DATETIME_FMT, m.getValue().toString());
                        String alert_start_time = df.format(date);
                        map1.put("alert_start_time", alert_start_time);
                    } else if (m.getKey().equals("create_time") && m.getValue() != null) {
                        Date date = DateUtils.stringToDate(DateUtils.DEFAULT_DATETIME_FMT, m.getValue().toString());
                        String create_time = df.format(date);
                        map1.put("create_time", create_time);
                    } else if (m.getKey().equals("cur_moni_time") && m.getValue() != null) {
                        Date date = DateUtils.stringToDate(DateUtils.DEFAULT_DATETIME_FMT, m.getValue().toString());
                        String cur_moni_time = df.format(date);
                        map1.put("cur_moni_time", cur_moni_time);
                    }
                }

            }
        }
        log.info("dataLists的值=======###￥￥￥" + pageResult.size());

        String title = "告警导出列表";
        String fileName = title + ".xlsx";
        //excel constuct
        ExportExcelUtil eeu = new ExportExcelUtil();
        SXSSFWorkbook book = new SXSSFWorkbook(128);
        book.setCompressTempFiles(true);
        try {
            eeu.exportExcel(book, 0, title, headerList.toArray(new String[0]), pageResult, keyList.toArray(new String[0]));
        } catch (Exception e) {
            log.error("generate excel failed,", e);
            map.put("code", "0005");
            map.put("message", e.getMessage());
            return map;
        }

        //主动释放资源
        pageResult.clear();
        ByteArrayOutputStream ops = null;
        ByteArrayInputStream in = null;
        log.info("-----2.create excel use-----: {} ms", (System.currentTimeMillis() - time));
        try {
            ops = new ByteArrayOutputStream();
            book.write(ops);
            byte[] b = ops.toByteArray();
            in = new ByteArrayInputStream(b);
            map = ftpService.uploadtoFTP(fileName, in);
            ops.flush();
            log.info("-----3.upload excel to ftp use-----: {} ms", (System.currentTimeMillis() - time));
        } catch (Exception e) {
            log.error("导出excel失败，失败原因：", e);
            map.put("code", "0005");
            map.put("message", e.getMessage());
        } finally {
            IOUtils.closeQuietly(book);
            IOUtils.closeQuietly(ops);
            IOUtils.closeQuietly(in);
            return map;
        }
    }

    /**
     * 查询详情
     *
     * @param alertId
     * @return
     */
    @Override
    public Map<String, Object> detail(@PathVariable(name = "alert_id") String alertId) {
        return alertsBizV2.detailById(alertId);
    }

    /**
     * 根据ids获取处理后的告警列表
     *
     * @param alertIds 告警Id
     * @return
     */
    public List<Map<String, Object>> queryHandleList(@RequestParam("alert_ids") List<String> alertIds) {
        return alertsBizV2.selectByIds(alertIds);
    }

    @Override
    public List<AlertConfigDict> getMonitObjectList() {
        return PayloadParseUtil.jacksonBaseParse(AlertConfigDict.class, alertsBizV2.getMonitObjectList());
    }

    /**
     * 告警转派
     *
     * @param alertsOperationRequest 人员id集合，逗号分隔
     * @return ResponseEntity 返回
     */
    @Override
    public ResponseEntity<String> alertTransfer(@RequestBody AlertsTransferRequest alertsOperationRequest) {

        if (StringUtils.isEmpty(alertsOperationRequest.getAlertIds())) {
            log.error("listByPrimaryKeyArrays param alertIds is null");
            return null;
        }

        if (StringUtils.isEmpty(alertsOperationRequest.getUserNames())) {
            log.error("listByPrimaryKeyArrays param userNames is null");
            return null;
        }

        String namespace = alertsOperationRequest.getNamespace();
        String alertIds = alertsOperationRequest.getAlertIds();
        String userNames = alertsOperationRequest.getUserNames();

        alertsBizV2.alertTransfer(namespace, alertIds, userNames);

        return new ResponseEntity<String>("success", HttpStatus.OK);

    }

    /**
     * 告警确认
     *
     * @param alertsOperationRequest 告警id集合，逗号分隔
     * @return ResponseEntity 返回
     */
    @Override
    public ResponseEntity<String> alertConfirm(@RequestBody AlertsConfirmRequest alertsOperationRequest) {

        if (StringUtils.isEmpty(alertsOperationRequest.getAlertIds())) {
            log.error("listByPrimaryKeyArrays param alertIds is null");
            return null;
        }

        alertsBizV2.alertConfirm(jacksonBaseParse(AlertsOperationRequestVo.class, alertsOperationRequest));

        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> alertObserve(@RequestBody Map<String, Object> request) {
        if (null == request.get("alertIds")) {
            log.error("listByPrimaryKeyArrays param alertIds is null");
            return null;
        }
        alertsBizV2.alertObserve(request);
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    /**
     * 告警工单
     *
     * @param alertsOperationRequest 告警ID集合
     * @return
     */
    @Override
    public ResponseEntity<String> alertOrder(@RequestBody AlertsOrderRequest alertsOperationRequest) {
        String successNum = "0";
        try {
            if (StringUtils.isEmpty(alertsOperationRequest.getAlertIds())) {
                throw new RuntimeException("alertIds is null");
            }

            String namespace = alertsOperationRequest.getNamespace();
            String alertIds = alertsOperationRequest.getAlertIds();
            log.info("111111method[alertOrder] param is {}", alertsOperationRequest);
            Integer orderType = alertsOperationRequest.getOrderType();
            AlertBpmStartCallBack callBack = alertsBizV2.switchOrder(namespace, alertIds, orderType);
            if (callBack.isStatus()) {
                successNum = callBack.getSuccess() + "";
            } else {
                return new ResponseEntity<String>(callBack.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            log.error("alertOrder error:" + e.getMessage(), e);

            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        System.out.println("===============================" + new ResponseEntity<String>("success:" + successNum, HttpStatus.OK));
        return new ResponseEntity<String>("success:" + successNum, HttpStatus.OK);
    }

    /**
     * 告警已通知功能
     *
     * @param request 告警ID集合
     * @return 处理结果
     */
    @Override
    public ResponseEntity<String> notifyStatus(@PathVariable(name = "status") String status, @RequestBody Map<String, String> request) {
        String alertIds = (String) request.get("alertIds");
        if (StringUtils.isEmpty(alertIds)) {
            return new ResponseEntity<String>("alertIds is null", HttpStatus.NOT_FOUND);
        }
        alertsBizV2.notifyStatus(status, Arrays.asList(alertIds.split(",")));
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    /**
     * @return org.springframework.http.ResponseEntity<java.lang.String>
     * @auther baiwenping
     * @Description
     * @Date 14:09 2020/3/16
     * @Param [alertsOperationRequest]
     **/
    @Override
    public ResponseEntity<String> alertRemove(@RequestBody AlertsClearRequest alertsOperationRequest) {

        if (StringUtils.isEmpty(alertsOperationRequest.getAlertIds())) {
            log.error("alertRemove param alert_ids is null");
            return null;
        }
        alertsBizV2.manualClear(jacksonBaseParse(AlertsOperationRequestVo.class, alertsOperationRequest));
        return new ResponseEntity<String>("success", HttpStatus.OK);

    }

    /**
     * 告警邮件通知
     *
     * @param alertsNotifyRequest 告警ID集合
     * @return
     */
    @Override
    public ResponseEntity<String> alertemailNotify(@RequestBody AlertsNotifyRequest alertsNotifyRequest) {
        if (StringUtils.isEmpty(alertsNotifyRequest.getAlertIds())) {
            throw new RuntimeException("alertIds are null");
        }
        if (alertsNotifyRequest.getEmails() == null || alertsNotifyRequest.getEmails().size() == 0) {
            throw new RuntimeException("emails are null");
        }
        String alertIds = alertsNotifyRequest.getAlertIds();
        List<String> emails = alertsNotifyRequest.getEmails();
        String[] emailsArray = emails.toArray(new String[emails.size()]);
        String subject = "告警邮件通知";
        String[] alertIdArr = alertIds.split(",");
        List<Map<String, Object>> maps = alertsBizV2.selectByIds(Arrays.asList(alertIdArr));
        try {
            Map<String, String> resultMap = alertNotifyTemplateBiz.sendEmail(AlertConfigConstants.MESSAGE_TEMPLATE_ALERT_TEMPLATE,
                    subject, false, emails, null, maps);
            String message = resultMap.get("content");
            if (StringUtils.isEmpty(message)) {
                message = resultMap.get("desc");

            }
            log.info("Send mail subject: {} \n Content: {} \n Receivers: {}", subject, resultMap.get("content"), emailsArray);
            alertsBizV2.recordNotifyLog(AlertConfigConstants.MESSAGE_TYPE_EMAIL,
                    alertsNotifyRequest.getUsername(), alertIds, emails, message, "1");
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    /**
     * 告警短信通知
     *
     * @param alertsNotifyRequest 告警ID集合
     * @return
     */
    @Override
    public ResponseEntity<String> recordSMSNotify(@RequestBody AlertsNotifyRequest alertsNotifyRequest) {
        if (StringUtils.isEmpty(alertsNotifyRequest.getAlertIds())) {
            throw new RuntimeException("alertIds are null");
        }
        if (alertsNotifyRequest.getMobiles() == null || alertsNotifyRequest.getMobiles().size() == 0) {
            throw new RuntimeException("mobiles are null");
        }
        try {
            String alertIds = alertsNotifyRequest.getAlertIds();
            List<String> mobiles = alertsNotifyRequest.getMobiles();
            String status = "1";
            String[] alertIdArr = alertIds.split(",");
            List<Map<String, Object>> maps = alertsBizV2.selectByIds(Arrays.asList(alertIdArr));
            for (Map<String, Object> map : maps) {
                SmsNotifyParams smsNotifyParams = new SmsNotifyParams();
                smsNotifyParams.setParams(map);
                smsNotifyParams.setMobiles(mobiles);
                Map<String, String> resultMap = alertNotifyTemplateBiz.sendSms(AlertConfigConstants.MESSAGE_TEMPLATE_ALERT_TEMPLATE,
                        mobiles, map);
                String message = resultMap.get("content");
                if (!"0000".equals(resultMap.get("code"))) {
                    status = "0";
                }
                if (!StringUtils.isEmpty(message)) {
                    message = resultMap.get("desc");
                }
                alertsBizV2.recordNotifyLog(AlertConfigConstants.MESSAGE_TYPE_SMS, alertsNotifyRequest.getUsername(), alertIds, mobiles, message, status);
            }
            if ("1".equals(status)) {
                return new ResponseEntity<String>("success", HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            log.error("sendSms is error {}", e);
            return new ResponseEntity<String>("error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 查询单设备告警列表
     * @param queryParams
     * @return
     */
    public PageResponse<Map<String, Object>> queryDeviceAlertList(@RequestBody QueryParams queryParams) {
        setDataPermission(queryParams);
        List<AlertFieldVo> modelFromRedisList = alertFieldBiz.getModelFromRedis(AlertConfigConstants.REDIS_MODEL_ALERT, null);

        return alertsBizV2.queryDeviceAlertList(generateCriteria(queryParams, modelFromRedisList));
    }

    /**
     * 根据级别统计告警所属设备数
     * @param queryParams
     * @return
     */
    public List<Map<String, Object>> summaryDeviceAlertsByLevel(@RequestBody QueryParams queryParams) {
        setDataPermission(queryParams);
        List<AlertFieldVo> modelFromRedisList = alertFieldBiz.getModelFromRedis(AlertConfigConstants.REDIS_MODEL_ALERT, null);
        return alertsBizV2.summaryDeviceAlertsByLevel(generateCriteria(queryParams, modelFromRedisList));
    }

    /**
     * 校验工单状态，返回告警未关闭的工单号
     * @param params
     * @return
     */
    public Map<String, Object> checkOrderStatus(@RequestBody Map<String, List<String>> params) {
        Map<String, Object> returnMap = Maps.newHashMap();
        List<String> orderList = params.get("orderList");
        if (CollectionUtils.isEmpty(orderList)) {
            return returnMap;
        }
        List<String> existList = alertsBizV2.checkOrderStatus(orderList);

        returnMap.put("orderList", existList);
        return returnMap;
    }
}
