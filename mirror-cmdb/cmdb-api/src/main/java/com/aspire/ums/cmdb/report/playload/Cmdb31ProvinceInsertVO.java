package com.aspire.ums.cmdb.report.playload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: Cmdb31ProvinceInsertVO
 * Author:   zhu.juwang
 * Date:     2020/3/24 14:16
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Cmdb31ProvinceInsertVO {
    private Cmdb31ProvinceReport provinceReport;
    private List<Cmdb31ProvinceValue> valueList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Cmdb31ProvinceValue {
        private String settingId;
        private String reportValue;
        private String calcExp;
    }
}
