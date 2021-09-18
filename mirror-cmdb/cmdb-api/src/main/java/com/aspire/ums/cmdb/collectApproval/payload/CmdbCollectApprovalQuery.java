package com.aspire.ums.cmdb.collectApproval.payload;


import com.aspire.mirror.common.auth.GeneralAuthConstraintsAware;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbCollectApprovalQurery
 * Author:   HANGFANG
 * Date:     2019/6/24 16:54
 * Description: ${DESCRIPTION}
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CmdbCollectApprovalQuery extends GeneralAuthConstraintsAware {
    /**
     * 主机ID
     */
    private String instanceId;
    /**
     * 模型ID
     */
    private String moduleId;
    /**
     * 采集项配置名称
     */
    private String codeFiledName;
    /**
     * 操作人
     */
    private String operator;
    /**
     * 采集类型
     */
    private String operaterType;
    /**
     * 采集类型(add,update,delete)
     */
    private String approvalType;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 当前页数
     */
    private Integer pageNum;
    /**
     * 分页数
     */
    private Integer pageSize;
    /**
     * 审核状态
     */
    private String approvalStatus;

    /**
     * 审核类型（detail从ES中获取已审核数据, approval只显示未审批）
     */
    private String type;

    private Map<String, String> primaryQuery;

//    /**
//     * 资源池
//     */
//    private String idcType;
//    /**
//     * 设备类型
//     */
//    private String deviceType;
//    /**
//     * 设备分类
//     */
//    private String deviceClass;

//    private String idcAuth;
}
