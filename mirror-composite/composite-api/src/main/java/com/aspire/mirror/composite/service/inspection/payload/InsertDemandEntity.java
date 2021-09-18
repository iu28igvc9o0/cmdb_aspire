package com.aspire.mirror.composite.service.inspection.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 项目名称:
 * 包: com.aspire.mirror.composite.service.inspection.payload
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/13 14:47
 * 版本: v1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InsertDemandEntity {
    private CmdbDemandManager demandManager;
    private List<CmdbDemandResourceTypeValue> resourceTypeValueList;
    
    private String userName;
}
