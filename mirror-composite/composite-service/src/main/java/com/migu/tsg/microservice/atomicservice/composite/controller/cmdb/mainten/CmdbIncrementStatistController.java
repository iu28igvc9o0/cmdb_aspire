package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.mainten;

import com.aspire.mirror.composite.service.cmdb.mainten.ICmdbIncrementStatistAPI;
import com.aspire.ums.cmdb.maintenance.payload.CmdbIncrementStatistRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.mainten.CmdbIncrementStatistClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName CmdbIncrementStatistController
 * @Description
 * @Author luowenbo
 * @Date 2020/2/19 17:08
 * @Version 1.0
 */
@RestController
@Slf4j
public class CmdbIncrementStatistController implements ICmdbIncrementStatistAPI {

    @Autowired
    private CmdbIncrementStatistClient cmdbIncrementStatistClient;

    @Override
    public List<Map<String, Object>> statistIncrementByTime(@RequestBody CmdbIncrementStatistRequest req) {
        return cmdbIncrementStatistClient.statistIncrementByTime(req);
    }
}
