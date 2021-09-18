package com.aspire.mirror.composite.service.inspection.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 项目名称:
 * 包: com.aspire.mirror.composite.service.inspection.payload
 * 类名称:
 * 类描述:
 * 创建人: PJX
 * 创建时间: 2019/5/13 14:48
 * 版本: v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbDemandResourceTypeValue {
    
    /**
     * 需求ID
     */
    private String demandId;
    /**
     * 资源类型ID
     */
    private String resourceTypeId;
    /**
     * 资源需求数量
     */
    private String resourceCount;
    /**
     * 使用场景描述
     */
    private String resourceScene;
    /**
     * 使用用途
     */
    private String resourceUse;
}
