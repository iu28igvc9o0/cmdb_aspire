package com.aspire.ums.cmdb.v2.code.web;

import com.aspire.ums.cmdb.code.ICmdbValidaterAPI;
import com.aspire.ums.cmdb.code.payload.CmdbCodeValidate;
import com.aspire.ums.cmdb.v2.code.service.ICmdbCodeValidateService;
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
    private ICmdbCodeValidateService validateService;

    @Override
    public List<CmdbCodeValidate> list() {
        return validateService.list();
    }
}
