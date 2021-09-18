package com.aspire.mirror.alert.server.controller.isolate;

import com.aspire.mirror.alert.api.dto.AlertsClearRequest;
import com.aspire.mirror.alert.api.dto.bpm.AlertsOrderRequest;
import com.aspire.mirror.alert.api.dto.alert.QueryParams;
import com.aspire.mirror.alert.api.service.isolate.AlertIsolateAlertsV2Service;
import com.aspire.mirror.alert.server.biz.ftp.FtpService;
import com.aspire.mirror.alert.server.util.ExportExcelUtil;
import com.aspire.mirror.alert.server.util.MapUtils;
import com.aspire.mirror.alert.server.biz.model.AlertFieldBiz;
import com.aspire.mirror.alert.server.biz.isolate.IAlertIsolateAlertsBizV2;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.alert.server.controller.CommonController;
import com.aspire.mirror.alert.server.vo.alert.AlertsOperationRequestVo;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.aspire.mirror.alert.server.util.PayloadParseUtil.jacksonBaseParse;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.controller
 * @Author: baiwenping
 * @CreateTime: 2020-02-29 00:24
 * @Description: ${Description}
 */
@RestController
@Slf4j
public class AlertIsolateAlertsV2Controller extends CommonController implements AlertIsolateAlertsV2Service {
    @Autowired
    private IAlertIsolateAlertsBizV2 alertIsolateAlertsBizV2;
    @Autowired
    private AlertFieldBiz alertFieldBiz;
    @Autowired
    private FtpService ftpService;
    /**
     * 查询屏蔽记录列表
     *
     * @param queryParams
     * @return
     */
    public PageResponse<Map<String, Object>> list(@RequestBody QueryParams queryParams) {
        List<AlertFieldVo> modelFromRedisList = alertFieldBiz.getModelFromRedis(AlertConfigConstants.REDIS_MODEL_ISOLATE_ALERT, null);
        return alertIsolateAlertsBizV2.findPage(generateCriteria(queryParams, modelFromRedisList));
    }

    /**
     * 导出屏蔽记录列表
     *
     * @param queryParams
     * @return
     */
    public Map<String, Object> export(@RequestBody QueryParams queryParams) {
        Long time = System.currentTimeMillis();
        List<AlertFieldVo> modelFromRedisList = alertFieldBiz.getModelFromRedis(AlertConfigConstants.REDIS_MODEL_ISOLATE_ALERT, null);
        List<String> headerList = Lists.newArrayList();
        List<String> keyList = Lists.newArrayList();
        modelFromRedisList = modelFromRedisList.stream().sorted(Comparator.comparing(AlertFieldVo::getListShowSort)).collect(Collectors.toList());
        modelFromRedisList.stream().forEach(alertFieldRequestDTO -> {
            if (AlertConfigConstants.YES.equals(alertFieldRequestDTO.getIsListShow())) {
                headerList.add(alertFieldRequestDTO.getListShowName());
                keyList.add(alertFieldRequestDTO.getFieldCode());
            }
        });

        Criteria criteria = generateCriteria(queryParams, modelFromRedisList);
        String exportField = getExportField(modelFromRedisList);
        criteria.setExportField(exportField);
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> pageResult =
                alertIsolateAlertsBizV2.list(criteria);
        log.info("-----1.query his use-----: {} ms",(System.currentTimeMillis()-time));

        String title = "告警屏蔽记录";
        String fileName = title+".xlsx";
        //excel constuct
        ExportExcelUtil eeu = new ExportExcelUtil();
        SXSSFWorkbook book = new SXSSFWorkbook(128);
        book.setCompressTempFiles(true);
        try {
            eeu.exportExcel(book, 0, title, headerList.toArray(new String[0]), pageResult, keyList.toArray(new String[0]));
        } catch (Exception e) {
            log.error("generate excel failed,", e);
            map.put("code","0005");
            map.put("message", e.getMessage());
            return map;
        }

        //主动释放资源
        pageResult.clear();
        ByteArrayOutputStream ops = null;
        ByteArrayInputStream in = null;
        log.info("-----2.create excel use-----: {} ms",(System.currentTimeMillis()-time));
        try {
            ops = new ByteArrayOutputStream();
            book.write(ops);
            byte[] b = ops.toByteArray();
            in = new ByteArrayInputStream(b);
            map = ftpService.uploadtoFTP(fileName, in);
            ops.flush();
            log.info("-----3.upload excel to ftp use-----: {} ms",(System.currentTimeMillis()-time));
        } catch (Exception e) {
            log.error("导出excel失败，失败原因：", e);
            map.put("code","0005");
            map.put("message", e.getMessage());
        }finally {
            IOUtils.closeQuietly(book);
            IOUtils.closeQuietly(ops);
            IOUtils.closeQuietly(in);
            return map;
        }
    }

    /**
     * 告警工单
     *
     * @param alertsOperationRequest 告警ID集合
     * @return 处理结果
     */
    public Map<String, Object> alertOrder(@RequestBody AlertsOrderRequest alertsOperationRequest) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("code", "0000");
        if (StringUtils.isEmpty(alertsOperationRequest.getAlertIds())) {
            throw new RuntimeException("alertIds is null");
        }
        try {
            String username = alertsOperationRequest.getUsername();
            String alertIds = alertsOperationRequest.getAlertIds();
            log.info("111111method[alertOrder] param is {}", alertsOperationRequest);
            Integer orderType = alertsOperationRequest.getOrderType();
            String message = alertIsolateAlertsBizV2.genOrder(username, alertIds, orderType);
            if (StringUtils.isEmpty(message)) {
                map.put("code", "9999");
                map.put("message", "call bpm error!");
            } else if (!message.startsWith("success:")) {
                map.put("code", "9999");
                map.put("message", message);
            }
        } catch (Exception e) {
            log.error("alertOrder error:" + e.getMessage(), e);
            map.put("code", "9999");
            map.put("message", e.getMessage());
        }
        return map;
    }

    /**
     * 恢复告警
     *
     * @param params 告警ID集合
     * @return 处理结果
     */
    public Map<String, Object> alertRecovery(@RequestBody Map<String, Object> params) {
        String alertIds = MapUtils.getString(params, "alert_ids");
        if (StringUtils.isEmpty(alertIds)) {
            throw new RuntimeException("alert_ids is null");
        }

        return alertIsolateAlertsBizV2.alertRecovery(alertIds.split(","));
    }

    /**
     * @return org.springframework.http.ResponseEntity<java.lang.String>
     * @auther baiwenping
     * @Description
     * @Date 14:09 2020/3/16
     * @Param [alertsOperationRequest]
     **/
    @Override
    public Map<String, Object> remove(@RequestBody AlertsClearRequest alertsOperationRequest) {

        if (StringUtils.isEmpty(alertsOperationRequest.getAlertIds())) {
            log.error("alertRemove param alert_ids is null");
            return null;
        }
        alertIsolateAlertsBizV2.remove(jacksonBaseParse(AlertsOperationRequestVo.class, alertsOperationRequest));
        Map<String,Object> map = new HashMap<>(1);
        map.put("status", "success");
        return map;
    }
}
