package com.aspire.ums.cmdb.ipCollect.payload.entity;

import com.aspire.ums.cmdb.common.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: fanshenquan
 * @Datetime: 2020/5/27 20:29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbIpClashResult extends Result<CmdbIpClashFindPageResponse> implements Serializable {
    private static final long serialVersionUID = -7757448736587372686L;
    private CmdbIpClashTopTotalResponse topTotal;
}
