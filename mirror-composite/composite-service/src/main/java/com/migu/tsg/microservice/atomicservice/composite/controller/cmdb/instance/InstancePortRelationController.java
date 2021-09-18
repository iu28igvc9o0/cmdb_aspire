package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.instance;

import com.aspire.mirror.composite.service.cmdb.instance.IInstancePortRelAPI;
import com.aspire.ums.cmdb.common.Result;
import com.aspire.ums.cmdb.instance.payload.CmdbInstancePortQuery;
import com.aspire.ums.cmdb.instance.payload.CmdbInstancePortRelation;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.instance.InstancePortRelationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: InstancePortRelationController
 * Author:   hangfang
 * Date:     2019/9/8
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class InstancePortRelationController implements IInstancePortRelAPI {

    @Autowired
    private InstancePortRelationClient portRelationClient;

    @Override
        public List<Map<String, String>> getInstanceIpByPool(@RequestParam("pool") String pool) {
        return portRelationClient.getInstanceIpByPool(pool);
    }

    @Override
    public Boolean selectByPortAndId(@RequestBody CmdbInstancePortRelation portRelation) {
        return portRelationClient.selectByPortAndId(portRelation);
    }

    @Override
    public Result<CmdbInstancePortRelation> list(@RequestBody CmdbInstancePortQuery instancePortQuery) {
        return portRelationClient.list(instancePortQuery);
    }

    @Override
    public Map<String, Object> delete(@RequestParam("id") String id) {
        return portRelationClient.delete(id);
    }

    @Override
    public Map<String, Object> insert(@RequestBody CmdbInstancePortRelation instancePortRelation) {
        return portRelationClient.insert(instancePortRelation);
    }
}
