package com.migu.tsg.microservice.atomicservice.composite.helper;

import com.aspire.mirror.common.entity.PageResponse;
import com.migu.tsg.microservice.atomicservice.composite.biz.AlertBiz;
import com.migu.tsg.microservice.atomicservice.composite.biz.UserBiz;
import com.migu.tsg.microservice.atomicservice.composite.vo.alert.AlertFieldDetailVo;
import com.migu.tsg.microservice.atomicservice.composite.vo.alert.QueryParamsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author baiwenping
 * @Title: AlertHelper
 * @Package com.migu.tsg.microservice.atomicservice.composite.helper
 * @Description: TODO
 * @date 2021/3/16 10:57
 */
@Component
@Slf4j
public class AlertHelper {

    @Autowired
    private List<AlertBiz> alertBizList;

    /**
     * 查询告警模型
     * @param tableName
     * @param sort
     * @return
     */
    public List<AlertFieldDetailVo> getModelFromRedis (String tableName, String sort) {
        for (AlertBiz alertBiz: alertBizList) {
            if (!alertBiz.isBasic()) {
                return alertBiz.getModelFromRedis(tableName, sort);
            }
        }
        return alertBizList.get(0).getModelFromRedis(tableName, sort);
    }

    /**
     * 统计条件下告警对应的设备数量
     * @param queryParams
     * @return
     */
    public PageResponse<Map<String, Object>> queryDeviceAlertList(QueryParamsVo queryParams) {
        for (AlertBiz alertBiz: alertBizList) {
            if (!alertBiz.isBasic()) {
                return alertBiz.queryDeviceAlertList(queryParams);
            }
        }
        return alertBizList.get(0).queryDeviceAlertList(queryParams);
    }
}
