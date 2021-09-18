package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.restful.bpm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: BpmConfig
 * Author:   zhu.juwang
 * Date:     2020/5/14 11:17
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@ConfigurationProperties(prefix = "cmdb.idcTenant.bpm")
@Data
@Component
public class BpmConfig {
    private String orderReportUrl;
    private String alertErrorReportUrl;
}
