package com.aspire.mirror.alert.server.controller.network;


import com.aspire.mirror.alert.api.dto.network.AlertNormResponse;
import com.aspire.mirror.alert.api.service.network.AlertsNetworkService;
import com.aspire.mirror.alert.server.biz.network.IAlertNetworkBiz;
import com.aspire.mirror.alert.server.dao.monitor.po.AlertNorm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hewang
 * @version 1.0
 * @date 2021/3/11 21:04
 */
@RestController
@Slf4j
public class AlertsNetworkController implements AlertsNetworkService {

    @Autowired
    private IAlertNetworkBiz alertNetworkBiz;


    @Override
    public List<AlertNormResponse> queryNetworkPortIndicators(@RequestParam(value = "indicatorName", required = false) String indicatorName,
                                                              @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                              @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        List<AlertNorm> alertNormList = alertNetworkBiz.queryNetworkPortIndicators(indicatorName,pageNum, pageSize);
        return getAlertNormResponses(alertNormList);
    }

    @Override
    public String addNetworkPortIndicators(@RequestBody List<AlertNormResponse> responses) {
        List<AlertNorm> list = new ArrayList<>();
        String message = alertNetworkBiz.addNetworkPortIndicators(list);
        return message;
    }

    @Override
    public List<AlertNormResponse> queryTopReportTypeConfiguration(@RequestParam(value = "userName",required = false) String userName) {
        List<AlertNorm> alertNormDTOS = alertNetworkBiz.queryTopReportTypeConfiguration(userName);
        return getAlertNormResponses(alertNormDTOS);
    }

    private List<AlertNormResponse> getAlertNormResponses(List<AlertNorm> alertNorms) {
        List<AlertNormResponse> alertNormResponses = new ArrayList<>();
        for (AlertNorm alertNorm : alertNorms) {
            AlertNormResponse alertNormResponse = new AlertNormResponse();
            BeanUtils.copyProperties(alertNorm,alertNormResponse);
            alertNormResponses.add(alertNormResponse);
        }
        return alertNormResponses;
    }

    @Override
    public String updateTopReportTypeConfiguration(@RequestBody List<AlertNormResponse> responses) {
        List<AlertNorm> list = new ArrayList<>();
        for (AlertNormResponse alertNormResponse : responses) {
            AlertNorm normDTO = new AlertNorm();
            BeanUtils.copyProperties(alertNormResponse,normDTO);
            list.add(normDTO);
        }
        String  message=alertNetworkBiz.updateTopReportTypeConfiguration(list);
        return message;
    }

    @Override
    public String deleteTopReportTypeConfiguration(@RequestParam(value = "id" ,required = false) Integer id) {
        String  message=alertNetworkBiz.deleteTopReportTypeConfiguration(id);
        return message;
    }

}
