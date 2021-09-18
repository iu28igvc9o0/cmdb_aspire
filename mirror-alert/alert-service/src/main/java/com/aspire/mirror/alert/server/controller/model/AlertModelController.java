package com.aspire.mirror.alert.server.controller.model;

import com.aspire.mirror.alert.api.dto.model.AlertModelDetail;
import com.aspire.mirror.alert.api.dto.model.AlertModelRequest;
import com.aspire.mirror.alert.api.service.model.AlertsModelService;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import com.aspire.mirror.alert.server.biz.model.AlertModelBiz;
import com.aspire.mirror.alert.server.vo.model.AlertModelVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.aspire.mirror.alert.server.util.PayloadParseUtil.jacksonBaseParse;

@RestController
@Slf4j
public class AlertModelController implements AlertsModelService {

    @Autowired
    private AlertModelBiz alertModelBiz;

    @Override
    public void insertAlertModel(@RequestBody AlertModelRequest request) {

        if (null == request) {
            log.error("[AlertModel] >>> Insert Alert Model Request is Null");
            throw new RuntimeException("[AlertModel] >>> Insert Alert Model Request is null");
        }
        log.info("[AlertModel] >>> Insert Alert Model Request is {}", request);
        alertModelBiz.insertAlertModel(jacksonBaseParse( AlertModelVo.class, request));

    }

    @Override
    public List<AlertModelDetail> getAlertModelList(@RequestParam(value = "modelName", required = false) String modelName,
                                                     @RequestParam(value = "tableName", required = false) String tableName) {
        return PayloadParseUtil.jacksonBaseParse(AlertModelDetail.class, alertModelBiz.getAlertModelList(modelName, tableName));
    }

    @Override
    public Object getAlertModelTreeData() {
        return alertModelBiz.getAlertModelTreeData();
    }

    @Override
    public void deleteAlertModel(@RequestParam(value = "userName") String userName,@RequestBody List<String> request) {
        alertModelBiz.deleteAlertModel(userName, request);
    }

    @Override
    public AlertModelDetail getAlertModelDetail(@RequestParam(value = "id") String id) {
        return PayloadParseUtil.jacksonBaseParse(AlertModelDetail.class,alertModelBiz.getAlertModelDetail(id));
    }

    @Override
    public void updateAlertModel(@RequestBody AlertModelRequest request) {
        alertModelBiz.updateAlertModel(jacksonBaseParse(AlertModelVo.class, request));
    }
}
