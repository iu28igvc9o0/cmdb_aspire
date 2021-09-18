package com.aspire.ums.cmdb.demand.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: InsertDemandEntity
 * Author:   zhu.juwang
 * Date:     2019/5/9 16:37
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InsertDemandEntity {
    private CmdbDemandManager demandManager;
    private List<CmdbDemandResourceTypeValue> resourceTypeValueList;
    private String userName;
    private List<CmdbDemandChanged> changedList;
}
