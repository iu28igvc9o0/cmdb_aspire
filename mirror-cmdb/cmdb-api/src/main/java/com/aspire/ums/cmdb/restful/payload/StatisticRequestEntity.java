package com.aspire.ums.cmdb.restful.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Copyright (C), 2015-2020, 卓望数码有限公司
 * FileName: StatisticRequestEntity
 * Author:   zhu.juwang
 * Date:     2020/6/22 10:34
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticRequestEntity {
    /**
     * 统计名称
     */
    private String name;
    /**
     * 统计查询参数
     */
    private Map<String, Object> params;
    /**
     * 返回值类型
     */
    private String responseType = "list";
}
