package com.aspire.mirror.composite.service.inspection.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CompEmployeeDetail
 * Author:   zhu.juwang
 * Date:     2019/3/13 17:38
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CompEmployeeDetail {
    /*
   ID
    */
    private String id;
    /*
    对象名称
     */
    private String employeeName;
}
