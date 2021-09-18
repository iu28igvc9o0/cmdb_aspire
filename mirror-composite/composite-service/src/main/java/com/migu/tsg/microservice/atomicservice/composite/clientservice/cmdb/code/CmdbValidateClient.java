package com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.code;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aspire.ums.cmdb.code.payload.CmdbCodeValidate;

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
@FeignClient(value = "CMDB")
public interface CmdbValidateClient {

    /**
     * 控件验证方法列表
     *
     */
    @RequestMapping(value = "/cmdb/code/validate/list", method = RequestMethod.GET)
    List<CmdbCodeValidate> list();
}
