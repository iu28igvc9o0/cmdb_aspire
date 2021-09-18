package com.aspire.ums.cmdb.collectApproval.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbApprovalUpdateReq
 * Author:   hangfang
 * Date:     2019/9/17
 * Description: DESCRIPTION
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdbApprovalUpdateReq {

    private List<String> partUpdateList;

    private CmdbCollectApprovalQuery allUpdateQuery;

    private Integer updateStatus;

    private String refuseDesc;
}
