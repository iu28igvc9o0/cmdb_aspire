package com.migu.tsg.microservice.atomicservice.composite.controller.cmdb.process.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: ImportTemplate
 * Author:   zhu.juwang
 * Date:     2019/8/5 16:37
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@ConfigurationProperties(prefix = "cmdb.import.template")
@Data
@Component
public class ImportTemplate {
    private Maintenance maintenance;
    @Data
    public static class Maintenance {
        private String project;
        private String repairevent;
        private String problemevent;
        private String equipmentproblem;
        private String maintensoftware;
        private String maintensoftwarerecord;
        private String maintenanceProjectBindDevice;
        private String portRelation;
        private String resourceassign;
    }
}
