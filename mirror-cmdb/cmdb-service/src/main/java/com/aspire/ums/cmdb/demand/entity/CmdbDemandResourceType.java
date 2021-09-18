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
public class CmdbDemandResourceType {

    /**
     * id
     */
    private String resourceTypeId;
    /**
     * 分类编码
     */
    private String resourceCode;
    /**
     * 所属大分类
     */
    private String resourceOwner;
    /**
     * 大分类排序
     */
    private Integer resourceOwnerOrder;
    /**
     * 分类名称
     */
    private String resourceType;
    /**
     * 父分类Id
     */
    private String parentTypeId;
    /**
     * 排序
     */
    private Integer resourceOrder;
}