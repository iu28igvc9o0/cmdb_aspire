package com.aspire.ums.cmdb.collect.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CollectEntity
 * Author:   zhu.juwang
 * Date:     2019/3/12 10:23
 * Description: 采集实体类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
public class CollectEntity {
    private String id;
    private String moduleId;
    private String formId;
    private String frequency;
    private String needAlarm;
    private String alermLevel;
    private String needNotice;
    private List<CollectEmployeeEntity> employeeList;
    private String visitType;
    //@JSONField(name = "visitRequest")
    private String visitRequest;
    private String disabled;
}
