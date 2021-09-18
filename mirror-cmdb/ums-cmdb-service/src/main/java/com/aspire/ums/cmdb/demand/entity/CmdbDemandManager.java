package com.aspire.ums.cmdb.demand.entity;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 描述：
* @author
* @date 2019-05-09 16:28:19
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbDemandManager {
    /**
     * id
     */
    private String demandId;
    /**
     * 部门
     */
    private String department;
    /**
     * 租户接口人
     */
    private String tenant;
    /**
     * 租户接口人电话
     */
    private String tenantPhone;
    /**
     * 应用系统
     */
    private String bizSystem;
    /**
     * 是否立项
     */
    private String isProject;
    /**
     * 立项时间
     */
    private String projectTime;
    /**
     * 资源需求提出时间
     */
    private String submitTime;
    /**
     * 是否主机代维
     */
    private String isHostMaintenance;
    /**
     * 是否有容灾高可用
     */
    private String isDisaster;
    /**
     * 容灾高可用类型
     */
    private String disasterType;
    /**
     * 带宽要求
     */
    private String wlanRequirement;
    /**
     * 资源预期投产时间
     */
    private String commissionTime;
    /**
     * 是否有资源池要求
     */
    private String isIdcRequirement;
    /**
     * 资源池要求
     */
    private String idcRequirement;
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 需求满足周期
     */
    private String cycleTime;
}