package com.aspire.mirror.composite.service.inspection.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CompCollectDetail
 * Author:   zhu.juwang
 * Date:     2019/3/13 17:37
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompCollectDetail {
    private String id;
    private String moduleId;
    private String formId;
    private String frequency;
    private String needAlarm;
    private String alermLevel;
    private String needNotice;
    private List<CompEmployeeDetail> employeeList;
    private String disabled;
}
