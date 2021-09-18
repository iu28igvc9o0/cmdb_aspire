package com.aspire.ums.cmdb.maintenance.web;

import com.aspire.ums.cmdb.maintenance.ICmdbIncrementStatistAPI;
import com.aspire.ums.cmdb.maintenance.payload.CmdbIncrementStatistRequest;
import com.aspire.ums.cmdb.maintenance.service.ICmdbIncrementStatistService;
import com.netflix.discovery.converters.Auto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName CmdbIncrementStatistController
 * @Description 维保增量按照时间维度统计——控制层实现类
 * @Author luowenbo
 * @Date 2020/2/19 16:35
 * @Version 1.0
 */
@RestController
@Slf4j
public class CmdbIncrementStatistController implements ICmdbIncrementStatistAPI {

    @Autowired
    private ICmdbIncrementStatistService cmdbIncrementStatistService;

    @Override
    public List<Map<String, Object>> statistIncrementByTime(@RequestBody CmdbIncrementStatistRequest req) {
        log.info("Request into CmdbIncrementStatistController.statistIncrementByTime()  params -> {}", req);
        return cmdbIncrementStatistService.statistIncrementByTime(req);
    }
}
