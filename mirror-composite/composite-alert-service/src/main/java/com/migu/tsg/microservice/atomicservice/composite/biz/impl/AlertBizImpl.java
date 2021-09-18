package com.migu.tsg.microservice.atomicservice.composite.biz.impl;

import com.aspire.mirror.alert.api.dto.alert.QueryField;
import com.aspire.mirror.alert.api.dto.alert.QueryParams;
import com.aspire.mirror.alert.api.dto.model.AlertFieldDetail;
import com.aspire.mirror.common.entity.PageResponse;
import com.migu.tsg.microservice.atomicservice.composite.biz.AlertBiz;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.alert.AlertsV2ServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.model.AlertFieldServiceClient;
import com.migu.tsg.microservice.atomicservice.composite.controller.util.PayloadParseUtil;
import com.migu.tsg.microservice.atomicservice.composite.vo.alert.AlertFieldDetailVo;
import com.migu.tsg.microservice.atomicservice.composite.vo.alert.QueryFieldVo;
import com.migu.tsg.microservice.atomicservice.composite.vo.alert.QueryParamsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author baiwenping
 * @Title: AlertBizImpl
 * @Package com.migu.tsg.microservice.atomicservice.composite.biz.impl
 * @Description: TODO
 * @date 2021/3/16 14:22
 */
@Component
public class AlertBizImpl implements AlertBiz {
    @Autowired
    private AlertFieldServiceClient alertFieldServiceClient;
    @Autowired
    private AlertsV2ServiceClient alertsV2ServiceClient;

    /**
     * 判断是否基础组件，基础服务不调用外部接口
     */
    @Override
    public boolean isBasic() {
        return false;
    }

    /**
     * 查询告警模型
     *
     * @param tableName
     * @param sort
     * @return
     */
    @Override
    public List<AlertFieldDetailVo> getModelFromRedis(String tableName, String sort) {
        List<AlertFieldDetail> fieldList = alertFieldServiceClient.getModelFromRedis(tableName, sort);
        return PayloadParseUtil.jacksonBaseParse(AlertFieldDetailVo.class, fieldList);
    }

    /**
     * 统计条件下告警对应的设备数量
     *
     * @param queryParams
     * @return
     */
    @Override
    public PageResponse<Map<String, Object>> queryDeviceAlertList(QueryParamsVo queryParams) {
        QueryParams query = new QueryParams();
        query.setFilterSceneId(queryParams.getFilterSceneId());
        query.setList(PayloadParseUtil.jacksonBaseParse(QueryField.class, queryParams.getList()));
        query.setPageNum(queryParams.getPageNum());
        query.setPageSize(queryParams.getPageSize());
        query.setSortName(queryParams.getSortName());
        return alertsV2ServiceClient.queryDeviceAlertList(query);
    }

    public static void main(String[] args) {
        QueryParamsVo q = new QueryParamsVo();
        List<QueryFieldVo> list = new ArrayList<>();
        QueryFieldVo queryFieldVo = new QueryFieldVo();
        queryFieldVo.setFieldName("dfd");
        queryFieldVo.setFieldType("sdfdddd");
        queryFieldVo.setFieldValue("haha");
        list.add(queryFieldVo);
        q.setList(list);
        q.setPageNum(1);
        QueryParams qp = PayloadParseUtil.jacksonBaseParse(QueryParams.class, q);
        System.out.println(qp.toString());
    }
}
