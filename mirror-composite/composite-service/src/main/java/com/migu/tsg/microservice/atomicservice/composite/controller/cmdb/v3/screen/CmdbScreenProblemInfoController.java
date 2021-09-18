package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.v3.screen;

import com.aspire.mirror.composite.service.cmdb.v3.screen.ICmdbScreenProblemInfoAPI;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenProblemInfo;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenProblemInfoRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.screen.ICmdbScreenProblemInfoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @projectName: CmdbScreenProblemInfoController
 * @description: 类
 * @author: luowenbo
 * @create: 2020-07-03 18:03
 **/
@RestController
public class CmdbScreenProblemInfoController implements ICmdbScreenProblemInfoAPI {

    @Autowired
    private ICmdbScreenProblemInfoClient problemInfoClient;

    @Override
    public Map<String, Object> update(@RequestBody CmdbScreenProblemInfo req) {
        return problemInfoClient.update(req);
    }

    @Override
    public Map<String, Object> save(@RequestBody CmdbScreenProblemInfo req) {
        return problemInfoClient.save(req);
    }

    @Override
    public Result<CmdbScreenProblemInfo> list(@RequestBody CmdbScreenProblemInfoRequest req) {
        return problemInfoClient.list(req);
    }

    @Override
    public CmdbScreenProblemInfo listDetail(@PathVariable("id") String id) {
        CmdbScreenProblemInfo rs =  problemInfoClient.listDetail(id);
        return rs;
    }
}
