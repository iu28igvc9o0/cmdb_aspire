package com.migu.tsg.microservice.atomicservice.rbac.dto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
* 项目名称: rbac-api <br>
* 包: com.migu.tsg.microservice.atomicservice.rbac.dto.model<br>
* 类名称: WorkOrderDraftDTO.java <br>
* 类描述: 服务台-工单起草类<br>
* 创建人: tongzhihong <br>
* 创建时间: 2020年09月16日下午3:12:59 <br>
* 版本: v1.0
*/

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WorkOrderDraftDTO {
    /**
     * 工单定义ID
     */
    private String formDefId;
    /**
     * 工单名
     */
    private String formName;
    /**
     * 完整URL
     */
    private String fullUrl;
    /**
     *  图表的URL
     */
    private String iconUrl;
}