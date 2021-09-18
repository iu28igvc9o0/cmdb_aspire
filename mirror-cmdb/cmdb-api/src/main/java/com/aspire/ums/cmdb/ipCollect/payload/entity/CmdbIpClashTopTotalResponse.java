package com.aspire.ums.cmdb.ipCollect.payload.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 冲突IP头栏统计实体类
 *
 * @Author: fanshenquan
 * @Datetime: 2020/5/27 18:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CmdbIpClashTopTotalResponse implements Serializable {
    private static final long serialVersionUID = -8161980032811322515L;
    // 冲突IP总数
    private Integer clashTotal;
    // IP冲突次数
    private Integer changeTotal;
    // 待处理IP数
    private Integer pendingTotal;
    // 已处理IP数
    private Integer processedTotal;
}
