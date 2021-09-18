package com.aspire.ums.cmdb.ipCollect.payload.entity;

import com.aspire.ums.cmdb.common.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/6/5 19:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class CmdbIpCollectResult extends Result<CmdbIpCollectResponse> implements Serializable {
    private static final long serialVersionUID = 6461432237959245274L;
    private CmdbIpCollectTopTotalResponse topTotal;
}
