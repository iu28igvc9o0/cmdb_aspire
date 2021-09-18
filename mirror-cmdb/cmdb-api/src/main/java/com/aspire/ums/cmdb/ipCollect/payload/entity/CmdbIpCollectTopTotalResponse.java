package com.aspire.ums.cmdb.ipCollect.payload.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/5 19:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CmdbIpCollectTopTotalResponse implements Serializable {
    private static final long serialVersionUID = 491053678075116440L;
    // IP总数
    private Integer ipTotal;
    // IPV4总数
    private Integer ipv4Total;
    // IPV6总数
    private Integer ipv6Total;
}
