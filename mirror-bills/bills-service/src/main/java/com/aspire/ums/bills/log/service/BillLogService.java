package com.aspire.ums.bills.log.service;

import com.aspire.mirror.common.entity.Result;
import com.aspire.ums.bills.common.ListResult;
import com.aspire.ums.bills.log.payload.BillsLog;
import com.aspire.ums.bills.log.payload.BillsLogRequest;

import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: LogService
 * Author:   hangfang
 * Date:     2021/3/4
 * Description: 描述
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface BillLogService {

    Result saveBillLog(BillsLog bizLogEntity);

    ListResult<Map<String, Object>> listBillLogs(BillsLogRequest billsLogRequest);
}
