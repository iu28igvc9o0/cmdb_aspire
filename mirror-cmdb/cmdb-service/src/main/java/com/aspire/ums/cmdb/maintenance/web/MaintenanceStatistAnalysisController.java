package com.aspire.ums.cmdb.maintenance.web;

import com.aspire.ums.cmdb.maintenance.IMaintenStatistAnalysisAPI;
import com.aspire.ums.cmdb.maintenance.payload.MaintenStatistAnalysisRequest;
import com.aspire.ums.cmdb.maintenance.service.MaintenanceStatistAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class MaintenanceStatistAnalysisController implements IMaintenStatistAnalysisAPI {

    @Autowired
    private MaintenanceStatistAnalysisService maintenanceStatistAnalysisService;

    @Override
    public List<Map<String,Object>> firstLayer(@RequestBody(required = false) MaintenStatistAnalysisRequest request) {
        log.info("MaintenanceStatistAnalysisController firstLayer() param is {} ",request);
        return maintenanceStatistAnalysisService.firstLayer(request);
    }

    @Override
    public List<Map<String, Object>> secondLayer(@RequestBody(required = false) MaintenStatistAnalysisRequest request) {
        log.info("MaintenanceStatistAnalysisController secondLayer() param is {} ",request);
        return maintenanceStatistAnalysisService.secondLayer(request);
    }

    @Override
    public List<Map<String, Object>> thirdLayer(@RequestBody(required = false) MaintenStatistAnalysisRequest request) {
        log.info("MaintenanceStatistAnalysisController thirdLayer() param is {} ",request);
        return maintenanceStatistAnalysisService.thirdLayer(request);
    }

    @Override
    public List<Map<String, Object>> fourthLayer(@RequestBody(required = false) MaintenStatistAnalysisRequest request) {
        log.info("MaintenanceStatistAnalysisController fourthLayer() param is {} ",request);
        return maintenanceStatistAnalysisService.fourthLayer(request);
    }

    @Override
    public List<Map<String, Object>> maintenancePeriodAnalysis(@RequestBody MaintenStatistAnalysisRequest request) {
        log.info("MaintenanceStatistAnalysisController maintenancePeriodAnalysis() param is {} ",request);
        return maintenanceStatistAnalysisService.maintenancePeriodAnalysis(request);
    }
}
