package com.aspire.ums.bills.log.service.impl;

import com.alibaba.fastjson.JSON;
import com.aspire.mirror.common.entity.Result;
import com.aspire.ums.bills.Constants;
import com.aspire.ums.bills.client.EsFeignClient;
import com.aspire.ums.bills.common.ListResult;
import com.aspire.ums.bills.config.RequestAuthContext;
import com.aspire.ums.bills.log.payload.BillsLog;
import com.aspire.ums.bills.log.payload.BillsLogRequest;
import com.aspire.ums.bills.log.service.BillLogService;
import com.aspire.ums.bills.util.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: LogServiceImpl
 * Author:   hangfang
 * Date:     2021/3/4
 * Description: 描述
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@Service
public class BillLogServiceImpl implements BillLogService {

    @Autowired
    EsFeignClient esFeignClient;


    @Override
    public Result saveBillLog(BillsLog bizLogEntity) {
        // 获取IP和用户
        if (StringUtils.isEmpty(bizLogEntity.getIp())) {
            bizLogEntity.setIp(RequestAuthContext.getRequestIp());
        }
        if (StringUtils.isEmpty(bizLogEntity.getOperateUser()) & StringUtils.isNotEmpty(RequestAuthContext.getRequestHeadUserName())) {
            bizLogEntity.setOperateUser(RequestAuthContext.getRequestHeadUserName());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        bizLogEntity.setOperateTime(sdf.format(new Date()));
        List<Map<String, Object>> logList = new ArrayList<>();
        logList.add(new ObjectMapper().convertValue(bizLogEntity, new TypeReference<Map<String, Object>>(){}));
        esFeignClient.insert(logList, Constants.ES_INDEX,Constants.ES_TYPE);
        return Result.success();
    }

    /**
     * 获取ES数据集合
     * @param bizLogRequest 查询设置 包含:
     * {
     * 	"params": [{  // 查询参数设置
     * 		"operator": "操作符",
     * 		"filed": "字段名称",
     * 		"value": "字段值"
     * 	}],
     * 	"sort": [{  // 排序信息设置
     * 		"filed": "排序字段",
     * 		"type": "排序方式"
     * 	}],
     * 	"index": "", //索引名称
     * 	"type": "",  //类型
     * 	"currentPage": 1, // 当前页数
     * 	"pageSize": 100 // 每页记录数
     * }
     * @return ES中符合的记录集
     */
    @Override
    public ListResult<Map<String, Object>> listBillLogs(BillsLogRequest bizLogRequest) {
        List<Map<String, String>> queryFields = new ArrayList();
        List<Map<String, String>> sortFields = new ArrayList();
        Map<String, Object> esQuery = new HashMap<>();
        esQuery.put("index", Constants.ES_INDEX);
        esQuery.put("type", Constants.ES_TYPE);
        esQuery.put("currentPage", bizLogRequest.getCurrentPage());
        esQuery.put("pageSize", bizLogRequest.getPageSize());
        Map<String, String> queryFieldMap = new HashMap<>();
        Map<String, String> sortFieldMap = new HashMap<>();
        // ip
        queryFieldMap.put("filed", "ip");
        queryFieldMap.put("operator", "like");
        queryFieldMap.put("value", bizLogRequest.getIp());
        queryFields.add(queryFieldMap);
        queryFieldMap = new HashMap<>();
        // operateType
        queryFieldMap.put("filed", "operateType");
        queryFieldMap.put("operator", "=");
        queryFieldMap.put("value", bizLogRequest.getOperateType());
        queryFields.add(queryFieldMap);
        // operateOBJ
        queryFieldMap = new HashMap<>();
        queryFieldMap.put("filed", "operateOBJ");
        queryFieldMap.put("operator", "like");
        queryFieldMap.put("value", bizLogRequest.getOperateOBJ());
        queryFields.add(queryFieldMap);

        // getOperateUser
        queryFieldMap = new HashMap<>();
        queryFieldMap.put("filed", "operateUser");
        queryFieldMap.put("operator", "like");
        queryFieldMap.put("value", bizLogRequest.getOperateUser());
        queryFields.add(queryFieldMap);
        // operateTime
        queryFieldMap = new HashMap<>();
        queryFieldMap.put("filed", "operateTime");
        queryFieldMap.put("operator", "between");
        queryFieldMap.put("filed_type", "datetime");
        queryFieldMap.put("value", bizLogRequest.getOperateTime());
        queryFields.add(queryFieldMap);
        esQuery.put("params", queryFields);
        sortFieldMap.put("filed", "operateTime");
        sortFieldMap.put("type","desc");
        sortFields.add(sortFieldMap);
        esQuery.put("sort", sortFields);
        log.info("data: {}", JSON.toJSONString(esQuery));
        return  esFeignClient.list(esQuery);
    }
}
