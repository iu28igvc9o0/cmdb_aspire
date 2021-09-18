package com.migu.tsg.microservice.atomicservice.composite.controller.bills.log;

import com.aspire.mirror.composite.service.bills.log.BillsLogAPI;
import com.aspire.ums.bills.common.ListResult;
import com.aspire.ums.bills.log.payload.BillsLogRequest;
import com.migu.tsg.microservice.atomicservice.composite.clientservice.bills.log.CmdbBillsLogClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Copyright (C), 2015-2019, 卓望数码有限公司
 * FileName: CmdbBillLogController
 * Author:   hangfang
 * Date:     2021/3/5
 * Description: 描述
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
public class CmdbBillLogController implements BillsLogAPI {

    @Autowired
    private CmdbBillsLogClient billsLogClient;
    @Override
    public ListResult<Map<String, Object>> listBillLogs(@RequestBody BillsLogRequest billsLogRequest) {
        return billsLogClient.listBillLogs(billsLogRequest);
    }
}
