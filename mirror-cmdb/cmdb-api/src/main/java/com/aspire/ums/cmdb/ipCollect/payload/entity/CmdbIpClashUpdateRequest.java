package com.aspire.ums.cmdb.ipCollect.payload.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * 更新处理状态参数类
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/28 14:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CmdbIpClashUpdateRequest implements Serializable {
    private static final long serialVersionUID = -7541426528813360144L;
    // 主体ID
    private List<String> mainId;
    // 处理状态 0-待处理,1-暂不处理,2-处理中,3-已处理
    private String handleStatus;
    // 暂不处理原因
    private String notHandleReason;
    // 操作人
    private String operator;
}
