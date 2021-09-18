package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.cmic;

import com.aspire.mirror.composite.service.cmdb.cmic.ICmdbIPAPI;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.cmdb.cmic.ICmdbIPClient;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: CmdbIPController
 * Author:   zhu.juwang
 * Date:     2020/5/27 10:43
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@Slf4j
public class CmdbIPController implements ICmdbIPAPI {

    @Autowired
    private ICmdbIPClient ipClient;

    @Override
    public Map<String, Object> statisticsIpUseInfo(@RequestBody(required = false) Map<String, Object> conditions) {
        return ipClient.statisticsIpUseInfo(conditions);
    }
}
