package com.aspire.mirror.alert.server.controller.model;

import com.aspire.mirror.alert.api.dto.model.AlertFieldRequest;
import com.aspire.mirror.alert.api.service.model.AlertsFieldService;
import com.aspire.mirror.alert.server.util.PayloadParseUtil;
import com.aspire.mirror.alert.server.biz.model.AlertFieldBiz;
import com.aspire.mirror.alert.server.dao.model.po.AlertField;
import com.aspire.mirror.alert.server.vo.model.AlertFieldVo;
import com.aspire.mirror.common.entity.PageResponse;
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
public class AlertFieldController implements AlertsFieldService {

    @Autowired
    private AlertFieldBiz alertFieldBiz;

    @Override
    public void insertAlertModel(@RequestBody AlertFieldRequest request) {
        if (null == request) {
            log.error("[AlertField] >>> Insert Alert Model Field Request is Null");
            throw new RuntimeException("[AlertModel] >>> Insert Alert Model Field Request is null");
        }
        log.info("[AlertField] >>> Insert Alert Model Field Request is {}", request);
        alertFieldBiz.insertAlertModel(PayloadParseUtil.jacksonBaseParse(AlertFieldVo.class, request));
    }

    @Override
    public com.aspire.mirror.alert.api.dto.model.AlertFieldDetail getAlertFieldDetailById(@RequestParam("id") String id) {
        return PayloadParseUtil.jacksonBaseParse(com.aspire.mirror.alert.api.dto.model.AlertFieldDetail.class, alertFieldBiz.getAlertFieldDetailById(id));
    }

    @Override
    public void deleteAlertFieldDetailById(@RequestParam("id") String id,
                                           @RequestParam("modelId") String modelId,
                                           @RequestParam("userName") String userName) {
        alertFieldBiz.deleteAlertFieldDetailById(id, modelId, userName);
    }

    @Override
    public void updateAlertField(@RequestBody AlertFieldRequest request) {
        alertFieldBiz.updateAlertField(PayloadParseUtil.jacksonBaseParse(AlertFieldVo.class, request));
    }

    @Override
    public PageResponse<com.aspire.mirror.alert.api.dto.model.AlertFieldDetail> getAlertFieldListByModelId(@RequestBody Map<String,Object> request) {
        PageResponse<AlertField> alertFieldListByModelId = alertFieldBiz.getAlertFieldListByModelId(request);
        PageResponse<com.aspire.mirror.alert.api.dto.model.AlertFieldDetail> response = new PageResponse<com.aspire.mirror.alert.api.dto.model.AlertFieldDetail>();
        response.setCount(alertFieldListByModelId.getCount());
        response.setResult(PayloadParseUtil.jacksonBaseParse(com.aspire.mirror.alert.api.dto.model.AlertFieldDetail.class, alertFieldListByModelId.getResult()));
        return response;
    }

    @Override
    public void updateLockStatus(@RequestParam("id") String id,
                                 @RequestParam("modelId") String modelId,
                                 @RequestParam("isLock") String isLock,
                                 @RequestParam("userName") String userName) {
        alertFieldBiz.updateLockStatus(id, modelId, isLock, userName);
    }

    @Override
    public void synchronizeField(@RequestParam("modelId") String modelId,
                                   @RequestParam("userName") String userName) {
        alertFieldBiz.synchronizeField(modelId,userName);
    }

    /**
     * 根据表名查询模型字段列表
     *
     * @param tableName
     * @return
     */
    @Override
    public List<com.aspire.mirror.alert.api.dto.model.AlertFieldDetail> getModelFromRedis(@PathVariable(name = "table_name")String tableName, @RequestParam(name="sort", required = false) String sort) {
        return PayloadParseUtil.jacksonBaseParse(com.aspire.mirror.alert.api.dto.model.AlertFieldDetail.class, alertFieldBiz.getModelFromRedis(tableName, sort));
    }
}
