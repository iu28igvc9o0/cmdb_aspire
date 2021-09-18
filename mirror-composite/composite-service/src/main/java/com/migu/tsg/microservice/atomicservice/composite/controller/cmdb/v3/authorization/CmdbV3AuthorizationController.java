package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.v3.authorization;

import com.aspire.mirror.composite.service.cmdb.v3.authorization.ICmdbV3AuthorizationAPI;
import com.aspire.ums.cmdb.v3.authorization.payload.CmdbV3Authorization;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.authorization.ICmdbV3AuthorizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: RepairEventImportFactory
 * Author:   hangfang
 * Date:     2020/2/12
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbV3AuthorizationController implements ICmdbV3AuthorizationAPI {

    @Autowired
    private ICmdbV3AuthorizationClient authorizationClient;
    @Override
    public List<CmdbV3Authorization> list(@RequestParam(value = "authOwner", required = false) String authOwner) {
        return authorizationClient.list(authOwner);
    }
}
