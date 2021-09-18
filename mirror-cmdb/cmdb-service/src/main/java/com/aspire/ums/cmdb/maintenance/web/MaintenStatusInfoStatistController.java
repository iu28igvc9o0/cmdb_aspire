package com.aspire.ums.cmdb.maintenance.web;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.maintenance.IMaintenStatusInfoStatist;
import com.aspire.ums.cmdb.maintenance.payload.MaintenStatusInfoStatistRequest;
import com.aspire.ums.cmdb.maintenance.service.MaintenStatusInfoStatistService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class MaintenStatusInfoStatistController implements IMaintenStatusInfoStatist {

    @Autowired
    private MaintenStatusInfoStatistService maintenStatusInfoStatistService;

    @Override
    public List<Map<String, Object>> statistMaintenStatusInfo(@RequestBody MaintenStatusInfoStatistRequest request) {
        log.info("Request into MaintenStatusInfoStatistController.statistMaintenStatusInfo()  params -> {}", request);
        List<Map<String, Object>> result = maintenStatusInfoStatistService.statistMaintenStatusInfo(request);
        return result;
    }

    @Override
    public Result<Map<String,Object>> getMaintenProjectList(@RequestBody MaintenStatusInfoStatistRequest request) {
        log.info("Request into MaintenStatusInfoStatistController.getMaintenProjectList()  params -> {}", request);
        return maintenStatusInfoStatistService.getMaintenProjectList(request);
    }
}
