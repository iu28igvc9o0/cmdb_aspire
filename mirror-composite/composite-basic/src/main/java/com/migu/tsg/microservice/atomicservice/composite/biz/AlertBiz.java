package com.migu.tsg.microservice.atomicservice.composite.biz;

import com.aspire.mirror.common.entity.PageResponse;
import com.migu.tsg.microservice.atomicservice.composite.vo.alert.AlertFieldDetailVo;
import com.migu.tsg.microservice.atomicservice.composite.vo.alert.QueryParamsVo;

import java.util.List;
import java.util.Map;

/**
 * @author baiwenping
 * @Title: AlertBiz
 * @Package com.migu.tsg.microservice.atomicservice.composite.biz
 * @Description: TODO
 * @date 2021/3/16 11:25
 */
public interface AlertBiz {
    /**
     * 判断是否基础组件，基础服务不调用外部接口
     */
    boolean isBasic();

    /**
     * 查询告警模型
     * @param tableName
     * @param sort
     * @return
     */
    List<AlertFieldDetailVo> getModelFromRedis (String tableName, String sort);

    /**
     * 统计条件下告警对应的设备数量
     * @param queryParams
     * @return
     */
    PageResponse<Map<String, Object>> queryDeviceAlertList(QueryParamsVo queryParams);
}
