package com.aspire.mirror.alert.server.controller.derive;

import com.aspire.mirror.alert.api.dto.alert.QueryParams;
import com.aspire.mirror.alert.api.service.derive.AlertDeriveAlertsV2Service;
import com.aspire.mirror.alert.server.biz.ftp.FtpService;
import com.aspire.mirror.alert.server.util.ExportExcelUtil;
import com.aspire.mirror.alert.server.biz.model.AlertFieldBiz;
import com.aspire.mirror.alert.server.biz.derive.IAlertDeriveAlertsBizV2;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @BelongsProject: mirror-alert
 * @BelongsPackage: com.aspire.mirror.alert.server.v2.controller
 * @Author: baiwenping
 * @CreateTime: 2020-02-29 00:24
 * @Description: ${Description}
 */
@RestController
@Slf4j
public class AlertDeriveAlertsV2Controller extends CommonController implements AlertDeriveAlertsV2Service {
    @Autowired
    private IAlertDeriveAlertsBizV2 alertDeriveAlertsBizV2;
    @Autowired
    private AlertFieldBiz alertFieldBiz;
    @Autowired
    private FtpService ftpService;
    /**
     * 查询衍生记录列表
     *
     * @param queryParams
     * @return
     */
    public PageResponse<Map<String, Object>> list(@RequestBody QueryParams queryParams) {
        List<AlertFieldVo> modelFromRedisList = alertFieldBiz.getModelFromRedis(AlertConfigConstants.REDIS_MODEL_DERIVE_ALERT, null);
        return alertDeriveAlertsBizV2.findPage(generateCriteria(queryParams, modelFromRedisList));
    }

    /**
     * 导出衍生记录列表
     *
     * @param queryParams
     * @return
     */
    public Map<String, Object> export(@RequestBody QueryParams queryParams) {
        Long time = System.currentTimeMillis();
        List<AlertFieldVo> modelFromRedisList = alertFieldBiz.getModelFromRedis(AlertConfigConstants.REDIS_MODEL_DERIVE_ALERT, null);
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
        List<Map<String, Object>> pageResult =
                alertDeriveAlertsBizV2.list(criteria);
        log.info("-----1.query his use-----: {} ms",(System.currentTimeMillis()-time));

        String title = "告警衍生记录";
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
}
