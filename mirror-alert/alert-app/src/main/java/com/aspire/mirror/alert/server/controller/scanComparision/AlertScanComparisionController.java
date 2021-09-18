package com.aspire.mirror.alert.server.controller.scanComparision;

import com.aspire.mirror.alert.api.dto.scanComparision.AlertScanComparisionDetail;
import com.aspire.mirror.alert.api.dto.scanComparision.AlertScanComparisionReq;
import com.aspire.mirror.alert.api.service.scanComparision.AlertScanComparisionService;
import com.aspire.mirror.alert.server.biz.scanComparision.AlertScanComparisionBiz;
import com.aspire.mirror.alert.server.vo.scanComparision.AlertScanComparisionDetailVo;
import com.aspire.mirror.alert.server.vo.scanComparision.AlertScanComparisionReqVo;
import com.aspire.mirror.common.entity.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.aspire.mirror.alert.server.util.PayloadParseUtil.jacksonBaseParse;

@RestController
@Slf4j
public class AlertScanComparisionController implements AlertScanComparisionService {

    @Autowired
    private AlertScanComparisionBiz alertScanComparisionBiz;

    @Override
    public PageResponse<AlertScanComparisionDetail> getScanComparisionList(@RequestBody AlertScanComparisionReq request) {
        if (null == request) {
            log.error("[AlertService >>>]Get Scan Comparision Request is null");
            throw new RuntimeException("[composite >>>]Get Scan Comparision Request is null");
        }
        log.info("[AlertService >>>]Get Scan Comparision Request is {}", request);
        PageResponse<AlertScanComparisionDetail> response = new PageResponse<AlertScanComparisionDetail>();
        try {
            PageResponse<AlertScanComparisionDetailVo> scanComparisionList =
                    alertScanComparisionBiz.getScanComparisionList(jacksonBaseParse(AlertScanComparisionReqVo.class, request));
            response.setCount(scanComparisionList.getCount());
            response.setResult(jacksonBaseParse(AlertScanComparisionDetail.class,scanComparisionList.getResult()));
        } catch (Exception e) {
            log.error("[AlertService >>>]Get Scan Comparision Error is {}",e);
            throw new RuntimeException("[AlertService >>>]Get Scan Comparision Error is {}",e);
        }
        return response;
    }

    @Override
    public void deleteScanComparisionById(@RequestBody List<String> request) {
        if (CollectionUtils.isEmpty(request)) {
            log.error("[AlertService >>>]Delete Scan Comparision Request is null");
            throw new RuntimeException("[AlertService >>>]Delete Scan Comparision Request is null");
        }
        log.info("[AlertService >>>]Delete Scan Comparision Request is {}", request);
        try {
            alertScanComparisionBiz.deleteScanComparisionById(request);
        } catch (Exception e) {
            log.error("[AlertService >>>]Delete Scan Comparision Error is {}",e);
            throw new RuntimeException("[AlertService >>>]Delete Scan Comparision Request is {}",e);
        }
    }

    @Override
    public void synScanComparision(@RequestBody List<Map<String,String>> request) {
        if (CollectionUtils.isEmpty(request)) {
            log.error("[AlertService >>>]Syn Scan Comparision Request is null");
            throw new RuntimeException("[AlertService >>>]Syn Scan Comparision Request is null");
        }
        log.info("[AlertService >>>]Syn Scan Comparision Request is {}", request);
        try {
            alertScanComparisionBiz.synScanComparision(request);
        } catch (Exception e) {
            log.error("[AlertService >>>]Syn Scan Comparision Error is {}",e);
            throw new RuntimeException("[AlertService >>>]Syn Scan Comparision Error is {}",e);
        }
    }

    @Override
    public List<Map<String, Object>> exportScanComparision(@RequestBody AlertScanComparisionReq request){
        if (null == request) {
            log.error("[AlertService >>>]Export Scan Comparision Request is null");
            throw new RuntimeException("[AlertService >>>]Export Scan Comparision Request is null");
        }
        log.info("[AlertService >>>]Export Scan Comparision Request is {}", request);
        try {
            return alertScanComparisionBiz.exportScanComparision(jacksonBaseParse(AlertScanComparisionReqVo.class, request));
        } catch (Exception e) {
            log.error("[AlertService >>>]Export Scan Comparision Error is {}",e);
            throw new RuntimeException("[AlertService >>>]Export Scan Comparision Error is {}",e);
        }
    }
}
