package com.aspire.ums.cmdb.demand.entity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2019-05-09 16:28:20
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