package com.aspire.ums.cmdb.deviceStatistic.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: SheetProperties
 * Author:   zhu.juwang
 * Date:     2019/10/30 10:46
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SheetProperties {

    private int sheetNum;
    private String sheetTitle;
    List<String[]> headers;

}
