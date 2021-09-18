package com.aspire.ums.cmdb.report.playload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: Cmdb31ProvinceReprotQueryResponse
 * Author:   hangfang
 * Date:     2020/4/20
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cmdb31ProvinceReprotQueryResponse {
    /**
     * 表id
     */
    private String tableId;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 表数据
     */
    private List<Map<String, Object>> dataList;
}
