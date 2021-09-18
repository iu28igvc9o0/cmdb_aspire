package com.migu.tsg.microservice.atomicservice.composite.controller.model;

import com.aspire.mirror.alert.api.dto.model.AlertModelRequest;
import com.aspire.mirror.composite.payload.model.ICompAlertModelDetail;
import com.aspire.mirror.composite.payload.model.ICompAlertModelRequest;
import com.aspire.mirror.composite.service.model.ICompAlertsModelService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.model.AlertModelServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@Slf4j
public class CompAlertModelController implements ICompAlertsModelService {

    @Autowired
    private AlertModelServiceClient alertModelClient;

    private static final String SUCCESS = "SUCCESS";
    private static final String ERROR = "ERROR";

    @Override
    public String insertAlertModel(@RequestBody ICompAlertModelRequest request) {
        if (null == request) {
            log.error("[AlertModel] >>> Insert Alert Model Request is Null");
            throw new RuntimeException("[AlertModel] >>> Insert Alert Model Request is null");
        }
        log.info("[AlertModel] >>> Insert Alert Model Request is {}", request);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        request.setCreator(authCtx.getUser().getUsername());
        try {
            alertModelClient.insertAlertModel(PayloadParseUtil.jacksonBaseParse(AlertModelRequest.class, request));
        } catch (Exception e) {
            log.error("[AlertField] >>> Insert Alert Model Error is {}", e);
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public List<ICompAlertModelDetail> getAlertModelList(@RequestParam(value = "modelName", required = false) String modelName,
                                                          @RequestParam(value = "tableName", required = false) String tableName) {
        return PayloadParseUtil.jacksonBaseParse(ICompAlertModelDetail.class, alertModelClient.getAlertModelList(modelName,tableName));
    }

    @Override
    public Object getAlertModelTreeData() {
        return alertModelClient.getAlertModelTreeData();
    }

    @Override
    public String deleteAlertModel(@RequestBody List<String> request) {
        log.info("[AlertModel] >>> Delete Alert Model Request is {}", request);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        try {
            alertModelClient.deleteAlertModel(authCtx.getUser().getUsername(),request);
        } catch (Exception e) {
            log.error("[AlertField] >>> Delete Alert Model Error is {}", e);
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public ICompAlertModelDetail getAlertModelDetail(@RequestParam(value = "id") String id) {
        return PayloadParseUtil.jacksonBaseParse(ICompAlertModelDetail.class,alertModelClient.getAlertModelDetail(id));
    }

    @Override
    public String updateAlertModel(@RequestBody ICompAlertModelRequest request) {
        log.info("[AlertModel] >>> Update Alert Model Request is {}", request);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        request.setUpdater(authCtx.getUser().getUsername());
        try {
            alertModelClient.updateAlertModel(PayloadParseUtil.jacksonBaseParse(AlertModelRequest.class,request));
        } catch (Exception e) {
            log.error("[AlertField] >>> Update Alert Model Error is {}", e);
            return ERROR;
        }
        return SUCCESS;
    }
}
