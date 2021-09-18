package com.aspire.ums.cmdb.v2.restful.common;

import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.restful.common.ICommonRestfulAPI;
import com.aspire.ums.cmdb.restful.payload.StatisticRequestEntity;
import com.aspire.ums.cmdb.v2.restful.service.ICommonRestfulService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CommonRestfulController
 * Author:   zhu.juwang
 * Date:     2020/3/11 10:56
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CommonRestfulController implements ICommonRestfulAPI {

    @Autowired
    private ICommonRestfulService restfulService;

    @Override
    public Result<Map<String, Object>> getInstanceList(@RequestBody Map<String, Object> params) {
        return restfulService.getInstanceList(params);
    }

    @Override
    public Result<Map<String, Object>> getInstanceListByName(@RequestBody Map<String, Object> params) {
        return restfulService.getInstanceListByName(params);
    }

    @Override
    public Map<String, Object> getInstanceDetail(@RequestBody Map<String, Object> params) {
        return restfulService.getInstanceDetail(params);
    }

    @Override
    public Object getInstanceStatistics(@RequestBody StatisticRequestEntity statisticRequestEntity) {
        return restfulService.getInstanceStatistics(statisticRequestEntity);
    }
}
