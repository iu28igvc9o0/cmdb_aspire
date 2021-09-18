package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.v3.condication;

import com.aspire.mirror.composite.service.cmdb.v3.condication.ICmdbV3AccessUserAPI;
import com.aspire.ums.cmdb.v3.condication.payload.CmdbV3AccessUser;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.v3.condication.ICmdbV3AccessUserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CmdbV3AccessUserController
 * Author:   zhu.juwang
 * Date:     2020/3/16 11:25
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class CmdbV3AccessUserController implements ICmdbV3AccessUserAPI {
    @Autowired
    private ICmdbV3AccessUserClient userClient;
    @Override
    public List<CmdbV3AccessUser> list() {
        return userClient.list();
    }
}
