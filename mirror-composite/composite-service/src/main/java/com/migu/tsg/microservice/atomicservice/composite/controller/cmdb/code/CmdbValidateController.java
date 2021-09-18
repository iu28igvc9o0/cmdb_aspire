package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.code;

import com.aspire.mirror.composite.service.cmdb.code.ICmdbValidaterAPI;
import com.aspire.ums.cmdb.code.payload.CmdbCodeValidate;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.code.CmdbValidateClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbValidateController
 * Author:   zhu.juwang
 * Date:     2019/5/17 9:49
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbValidateController implements ICmdbValidaterAPI {

    @Autowired
    private CmdbValidateClient validateClient;

    @Override
    public List<CmdbCodeValidate> list() {
        return validateClient.list();
    }
}
