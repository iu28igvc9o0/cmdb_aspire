package com.aspire.ums.bills.log.web;

import com.aspire.mirror.common.entity.Result;
import com.aspire.ums.bills.common.ListResult;
import com.aspire.ums.bills.log.BillsLogAPI;
import com.aspire.ums.bills.log.payload.BillsLog;
import com.aspire.ums.bills.log.payload.BillsLogRequest;
import com.aspire.ums.bills.log.service.BillLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: BillsLogController
 * Author:   hangfang
 * Date:     2021/3/5
 * Description: 描述
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class BillsLogController implements BillsLogAPI {

    @Autowired
    private BillLogService logService;

    @Override
    public ListResult<Map<String, Object>> listBillLogs(@RequestBody BillsLogRequest billsLogRequest) {
        return logService.listBillLogs(billsLogRequest);
    }

    @Override
    public Result saveBillLog(@RequestBody BillsLog bizLogEntity) {
        return logService.saveBillLog(bizLogEntity);
    }


}
