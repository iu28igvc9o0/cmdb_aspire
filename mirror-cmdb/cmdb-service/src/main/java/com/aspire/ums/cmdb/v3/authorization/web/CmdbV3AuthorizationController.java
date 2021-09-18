package com.aspire.ums.cmdb.v3.authorization.web;

import com.aspire.ums.cmdb.v3.authorization.ICmdbV3AuthorizationAPI;
import com.aspire.ums.cmdb.v3.authorization.payload.CmdbV3Authorization;
import com.aspire.ums.cmdb.v3.authorization.service.ICmdbV3AuthorizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbV3AuthorizationController
 * Author:   hangfang
 * Date:     2020/1/14
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbV3AuthorizationController implements ICmdbV3AuthorizationAPI {

    @Autowired
    private ICmdbV3AuthorizationService authorizationService;
    @Override
    public List<CmdbV3Authorization> list(@RequestParam(value = "authOwner", required = false) String authOwner) {
        return authorizationService.list(authOwner);
    }
}
