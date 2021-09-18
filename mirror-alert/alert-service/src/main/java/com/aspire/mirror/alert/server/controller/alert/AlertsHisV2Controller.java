package com.aspire.mirror.alert.server.controller.alert;

import com.aspire.mirror.alert.api.dto.alert.QueryParams;
import com.aspire.mirror.alert.api.service.alert.AlertsHisV2Service;
import com.aspire.mirror.alert.server.biz.ftp.FtpService;
import com.aspire.mirror.alert.server.helper.AuthHelper;
import com.aspire.mirror.alert.server.util.DateUtils;
import com.aspire.mirror.alert.server.util.ExportExcelUtil;
import com.aspire.mirror.alert.server.biz.model.AlertFieldBiz;
import com.aspire.mirror.alert.server.biz.alert.AlertsHisBizV2;
import com.aspire.mirror.alert.server.constant.AlertConfigConstants;
import com.aspire.mirror.alert.server.controller.CommonController;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.aspire.mirror.alert.server.util.Criteria;
import com.aspire.mirror.common.entity.PageResponse;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.controller.alert
 * @Author: baiwenping
 * @CreateTime: 2020-03-12 15:05
 * @Description: ${Description}
 */
@Slf4j
@RestController
public class AlertsHisV2Controller extends CommonController implements AlertsHisV2Service {
    @Autowired
    private AlertsHisBizV2 alertsHisBizV2;
    @Autowired
    private AlertFieldBiz alertFieldBiz;
    @Autowired
    private FtpService ftpService;

    @Autowired
    private AuthHelper authHelper;

    /**
     * 告警查询
     *
     * @param queryParams
     * @return
     */
    @Override
    public PageResponse<Map<String, Object>> query(@RequestBody QueryParams queryParams) {
        setDataPermission(queryParams);
        List<AlertFieldVo> modelFromRedisList = alertFieldBiz.getModelFromRedis(AlertConfigConstants.REDIS_MODEL_ALERT_HIS, null);
        return alertsHisBizV2.findPage(generateCriteria(queryParams, modelFromRedisList));
    }

    /**
     * 导出历史告警列表数据
     *
     * @param queryParams
     */
    @Override
    public Map<String, Object> export(@RequestBody QueryParams queryParams) {
        setDataPermission(queryParams);
        Long time = System.currentTimeMillis();
        List<AlertFieldVo> modelFromRedisList = alertFieldBiz.getModelFromRedis(AlertConfigConstants.REDIS_MODEL_ALERT_HIS, null);
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
        List<Map<String, Object>> pageResult = alertsHisBizV2.list(criteria);
        log.info("-----1.query his use-----: {} ms" + (System.currentTimeMillis() - time));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (null != pageResult && pageResult.size() > 0) {
            for (Map<String, Object> map1 : pageResult) {
                for (Map.Entry<String, Object> m : map1.entrySet()) {
                    if (m.getKey().equals("alert_start_time") && m.getValue() != null) {
                        Date date = DateUtils.stringToDate(DateUtils.DEFAULT_DATETIME_FMT, m.getValue().toString());
                        String alert_start_time = df.format(date);
                        map1.put("alert_start_time", alert_start_time);
                    } else if (m.getKey().equals("clear_time") && m.getValue() != null) {
                        Date date = DateUtils.stringToDate(DateUtils.DEFAULT_DATETIME_FMT, m.getValue().toString());
                        String clear_time = df.format(date);
                        map1.put("clear_time", clear_time);
                    } else if (m.getKey().equals("cur_moni_time") && m.getValue() != null) {
                        Date date = DateUtils.stringToDate(DateUtils.DEFAULT_DATETIME_FMT, m.getValue().toString());
                        String cur_moni_time = df.format(date);
                        map1.put("cur_moni_time", cur_moni_time);
                    }
                }
            }
        }
        log.info("dataLists的值=======###￥￥￥" + pageResult.size());
        String title = "历史告警导出列表";
        String fileName = title + ".xlsx";
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
        return alertsHisBizV2.detailById(alertId);
    }
}

