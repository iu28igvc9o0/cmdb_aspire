package com.aspire.ums.cmdb.ipAudit.payload;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 更新处理状态参数类
 *
 * @Author: huanggongrui
 * @Datetime: 2020/5/28 14:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IpAuditUpdateRequest {
    // 稽核报表主体ID
    private List<String> mainId;
    // Ip地址库的主键数组
    private List<String> ipIds;
    // Ip地址库对应的网段库主键Id数组
    private List<String> segmentIds;
    // 处理状态: 待处理,暂不处理,处理中,已处理
    private String handleStatus;
    // 暂不处理原因
    private String notHandleReason;
    // 操作人
    private String operator;
    /**
     * 操作的ip类型
     * @see com.aspire.ums.cmdb.ipAudit.enums.IpAuditEnum
     */
    private String opIpType;
    private String businessId1; // 分配一级业务线ID
    private String businessName1; // 分配一级业务线名称
    private String businessId2; // 分配独立业务线ID
    private String businessName2; // 分配独立业务线名称
    private String ipUse; // 地址用途
    private String requestPerson; // 申请人
    private String requestTime; // 申请时间
    private String requestOrder; // 申请工单
    private String expirationDate; // 有效期
    private String remark; // 备注
}
