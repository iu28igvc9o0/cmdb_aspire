package com.aspire.ums.cmdb.collectApproval.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
* 描述：
* @author
* @date 2019-06-18 20:55:56
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbCollectApproval {

    /**
     * ID
     */
    private String id;

    /**
     * CI对应的模型ID
     */
    private String moduleId;

    /**
     * 变更审核-code对应模型id
     */
    private String ownerModuleId;

    /**
     * 实例ID
     */
    private String instanceId;

    /**
     * 码表名称
     */
    private String filedName;

    /**
     * 采集字段ID
     */
    private String codeId;

    /**
     * 字段旧值
     */
    private String oldValue;
    /**
     * 字段新值
     */
    private String currValue;
    /**
     * 审批状态 (0:未审批,1:审批通过,2:审批驳回)
     */
    private Integer approvalStatus;
    /**
     * 审批类型 (add,update,delete)
     */
    private String approvalType;
    /**
     * 审批人
     */
    private String approvalUser;
    /**
     * 审批时间
     */
    private Date approvalTime;
    /**
     * 审批描述
     */
    private String approvalDescribe;
    /**
     * 操作类型
     */
    private String operaterType;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 操作时间
     */
    private Date operatorTime;

    private String resourceData;
}
