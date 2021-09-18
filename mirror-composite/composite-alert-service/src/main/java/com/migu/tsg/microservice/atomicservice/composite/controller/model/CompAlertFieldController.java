package com.migu.tsg.microservice.atomicservice.composite.controller.model;

import com.aspire.mirror.alert.api.dto.model.AlertFieldDetail;
import com.aspire.mirror.alert.api.dto.model.AlertFieldRequest;
import com.aspire.mirror.common.entity.PageResponse;
import com.aspire.mirror.composite.payload.model.ICompAlertFieldDetail;
import com.aspire.mirror.composite.payload.model.ICompAlertFieldRequest;
import com.aspire.mirror.composite.service.model.ICompAlertsFieldService;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.model.AlertFieldServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.authcontext.RequestAuthContext;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class CompAlertFieldController implements ICompAlertsFieldService {

    private static final String SUCCESS = "SUCCESS";
    private static final String ERROR = "ERROR";

    @Autowired
    private AlertFieldServiceClient alertFieldServiceClient;

    @Override
    public String insertAlertModel(@RequestBody ICompAlertFieldRequest request) {
        if (null == request) {
            log.error("[AlertField] >>> Insert Alert Model Field Request is Null");
            throw new RuntimeException("[AlertModel] >>> Insert Alert Model Field Request is null");
        }
        log.info("[AlertField] >>> Insert Alert Model Field Request is {}", request);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        request.setCreator(authCtx.getUser().getUsername());
        try {
            alertFieldServiceClient.insertAlertModel(PayloadParseUtil.jacksonBaseParse(AlertFieldRequest.class, request));
        } catch (Exception e) {
            log.error("[AlertField] >>> Insert Alert Model Field Error is {}", e);
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public ICompAlertFieldDetail getAlertFieldDetailById(@RequestParam("id") String id) {
        return PayloadParseUtil.jacksonBaseParse(ICompAlertFieldDetail.class,
                alertFieldServiceClient.getAlertFieldDetailById(id));
    }

    @Override
    public String deleteAlertFieldDetailById(@RequestParam("id") String id,
                                           @RequestParam("modelId") String modelId) {
        RequestAuthContext requestAuthContext = RequestAuthContext.currentRequestAuthContext();
        try {
            alertFieldServiceClient.deleteAlertFieldDetailById(id, modelId, requestAuthContext.getUser().getUsername());
        } catch (Exception e) {
            log.error("[AlertField] >>> Delete Alert Model Field Error is {}", e);
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public String updateAlertField(@RequestBody ICompAlertFieldRequest request) {
        if (null == request) {
            log.error("[AlertField] >>> Update Alert Model Field Request is Null");
            throw new RuntimeException("[AlertModel] >>> Update Alert Model Field Request is null");
        }
        log.info("[AlertField] >>> Update Alert Model Field Request is {}", request);
        RequestAuthContext authCtx = RequestAuthContext.currentRequestAuthContext();
        request.setUpdater(authCtx.getUser().getUsername());
        try {
            alertFieldServiceClient.updateAlertField(PayloadParseUtil.jacksonBaseParse(AlertFieldRequest.class, request));
        } catch (Exception e) {
            log.error("[AlertField] >>> Update Alert Model Field Error is {}", e);
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public PageResponse<ICompAlertFieldDetail> getAlertFieldListByModelId(@RequestBody Map<String,Object> request) {
        log.info("[AlertField] >>> Getting Alert Model Field List Request is {}", request);
        PageResponse<AlertFieldDetail> alertFieldListByModelId = alertFieldServiceClient.getAlertFieldListByModelId(request);
        PageResponse<ICompAlertFieldDetail> response = new PageResponse<ICompAlertFieldDetail>();
        response.setCount(alertFieldListByModelId.getCount());
        response.setResult(PayloadParseUtil.jacksonBaseParse(ICompAlertFieldDetail.class,alertFieldListByModelId.getResult()));
        return response;
    }

    @Override
    public String updateLockStatus(@RequestParam("id") String id,
                                   @RequestParam("modelId") String modelId,
                                   @RequestParam("isLock") String isLock) {
        RequestAuthContext requestAuthContext = RequestAuthContext.currentRequestAuthContext();
        try {
            alertFieldServiceClient.updateLockStatus(id, modelId, isLock, requestAuthContext.getUser().getUsername());
        } catch (Exception e) {
            log.error("[AlertField] >>> Delete Alert Model Field Error is {}", e);
            return ERROR;
        }
        return SUCCESS;
    }

    @Override
    public String synchronizeField(@RequestParam("modelId") String modelId) {
        RequestAuthContext requestAuthContext = RequestAuthContext.currentRequestAuthContext();
        try {
            alertFieldServiceClient.synchronizeField(modelId,requestAuthContext.getUser().getUsername());
        } catch (Exception e) {
            log.error("[AlertField] >>> Synchronize Alert Model Field Error is {}", e);
            return ERROR;
        }
        return SUCCESS;
    }

    /**
     * 根据表名查询模型字段列表
     *
     * @param tableName
     * @return
     */
    @Override
    public List<ICompAlertFieldDetail> getModelFromRedis(@PathVariable(name = "table_name")String tableName, @RequestParam(name="sort", required = false) String sort) {
        return PayloadParseUtil.jacksonBaseParse(ICompAlertFieldDetail.class, alertFieldServiceClient.getModelFromRedis(tableName, sort));
    }
}
