package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.v3.screen;

import com.aspire.mirror.composite.service.cmdb.v3.screen.ICmdbScreenAnswerInfoAPI;
import com.aspire.ums.cmdb.v3.screen.payload.CmdbScreenAnswerInfo;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.screen.ICmdbScreenAnswerInfoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @projectName: CmdbScreenAnswerInfoController
 * @description: 类
 * @author: luowenbo
 * @create: 2020-07-03 18:02
 **/
@RestController
public class CmdbScreenAnswerInfoController implements ICmdbScreenAnswerInfoAPI {

    @Autowired
    private ICmdbScreenAnswerInfoClient answerInfoClient;

    @Override
    public Map<String, Object> save(@RequestBody CmdbScreenAnswerInfo req,
                                    @RequestParam("isAdmin") Boolean isAdmin) {
        return answerInfoClient.save(req,isAdmin);
    }

}
